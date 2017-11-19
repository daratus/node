package com.daratus.node;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.xml.xpath.XPath;

import org.jsoup.helper.W3CDom;

import com.daratus.node.console.AbstractCommand;
import com.daratus.node.domain.Node;
import com.daratus.node.domain.NullTask;
import com.daratus.node.domain.Task;
import com.daratus.node.domain.TaskObserver;
import com.daratus.node.webapi.responses.WebApiGetReferralUrlResponse;
import com.daratus.node.webapi.responses.WebApiNodeRegisterResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author Zilvinas Vaira
 *
 */
public class NodeContext implements TaskObserver, Runnable{
    
    public static final String NODE_CONFIG_FILE_NAME = "node.drt";
    
    private APIConnector apiConnector;
    
    private APIConnector webApiConnector;
    
    private ScrapingConnector scrapingConnector;

    private ObjectMapper mapper;
    
    private W3CDom w3cDom;
    
    private XPath xPath;
    
    protected NodeState currentState = null;
    
    private boolean isBlocked = false;
    
    private Node node = null;
    
    private Task currentTask;

    private final Task nullTask = new NullTask();

    private NodeMessenger messenger = null;
    
    private Logger logger;
    
    private List<ContextObserver> stateObservers = new ArrayList<ContextObserver>();
    
    private Properties properties = new Properties();
    
    public NodeContext(APIConnector apiConnector, APIConnector webApiConnector, ScrapingConnector scrapingConnector, ObjectMapper mapper, W3CDom w3cDom, XPath xPath) {
        this.apiConnector = apiConnector;
        this.webApiConnector = webApiConnector;
        this.scrapingConnector = scrapingConnector;
        this.mapper = mapper;
        this.w3cDom = w3cDom;
        this.xPath = xPath;
        nullTask.addTaskObserver(this);
        setCurrentTask(nullTask);
        logger = getLogger(this.getClass().getSimpleName());
        try {
            properties.load(this.getClass().getClassLoader().getResourceAsStream("application.properties"));
        } catch (IOException e) {
            logger.warning("Could not load properties!");
        }
    }
    
    public Logger getLogger(String className){
        return Logger.getLogger(className);
    }
    
    public NodeMessenger getMessenger() {
        return messenger;
    }

    public void setMessenger(NodeMessenger messenger) {
        this.messenger = messenger;
    }

    public Properties getProperties(){
        return properties;
    }

    public APIConnector getAPIConnector() {
        return apiConnector;
    }

    public APIConnector getWebAPIConnector() {
        return webApiConnector;
    }
    
    public ScrapingConnector getScrapingConnector() {
        return scrapingConnector;
    }
    
    public ObjectMapper getMapper() {
        return mapper;
    }
    
    public W3CDom getW3cDom() {
        return w3cDom;
    }
    
    public XPath getxPath() {
        return xPath;
    }

    public void setCurrentState(NodeState state){
        this.currentState = state;
        notifyObservers();
    }

    public NodeState getCurrentState() {
        return currentState;
    }
    
    /**
     * 
     * @param isBlocked
     * @see NodeCommand.START
     * @see NodeCommand.STOP
     */
    public void setBlocked(boolean isBlocked){
        if(!this.isBlocked && !isBlocked){
            logger.warning("Can not execute stop command. It is already stoped!");
        }else if(!isBlocked){
            messenger.warning("Stop request queued... please wait!");
        }
        this.isBlocked = isBlocked;
        currentState.handle(this);
    }
    
    public boolean isBlocked(){
        return isBlocked;
    }
    
    public void setNode(Node node) {
        if(this.node == null){
            this.node = node;
        }
    }
    
    public Node getNode() {
        return node;
    }
    
    public String getSecretKey() {
        return node.getSecretKey();
    }
    
    public boolean isAuthenticated(){
        return node != null;
    }
    
    public void setCurrentTask(Task currentTask) {
        if(currentTask != null){
            this.currentTask = currentTask;
        }else{
            this.currentTask = nullTask;
        }
    }

    /**
     * 
     * @see NodeCommand.EXIT
     */
    public void exit(){
        messenger.info("Bye Bye...!");
        if(isBlocked){
            setBlocked(false);
        }
        if(isAuthenticated()){
            logout();
        }
    }
    
    /**
     * 
     * @return
     */
    public Node createNodeFromFile() {
        File file = new File(NODE_CONFIG_FILE_NAME);
        Node node = null;
        if (file.exists()) {
            try {
                FileInputStream fileInput = new FileInputStream(file);
                ObjectInputStream objectInput = new ObjectInputStream(fileInput);
                node = (Node) objectInput.readObject();
                objectInput.close();
                fileInput.close();
             } catch (IOException | ClassNotFoundException i) {
                 logger.warning("Could not read Node details from file '" + NODE_CONFIG_FILE_NAME + "'!");
             } 
        }
        return node;
    }
    
    /**
     * 
     * @param node
     */
    public void storeNodeTofile(Node node){
        File file = new File(NODE_CONFIG_FILE_NAME);
        if(!file.exists()) {
            try {
                FileOutputStream fileOutput = new FileOutputStream(file);
                ObjectOutputStream objectOutput = new ObjectOutputStream(fileOutput);
                objectOutput.writeObject(node);
                objectOutput.close();
                fileOutput.close();
             } catch (IOException i) {
                 logger.warning("Could not write Node details to file '" + NODE_CONFIG_FILE_NAME + "'!");
             }
        }
    }
    
    /**
     * 
     * @param apiPath
     * @param node
     * @see NodeCommand.REGISTER
     */
    public void authenticate(String apiPath, Node node) {
        if(!isAuthenticated()){
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = "";
            try {
                jsonString = mapper.writeValueAsString(node);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            
            apiConnector.setJsonEntity(jsonString);
            String jsonResponse = apiConnector.sendRequest(apiPath, RequestMethod.POST);
            Node loginNode = null;
            if(jsonResponse != null && !jsonResponse.isEmpty()){
                try {
                    loginNode = new ObjectMapper().readValue(jsonResponse, Node.class);
                    if (loginNode.getId() != null) {
                        this.node = loginNode;
                        messenger.info("Found node '" + loginNode.getShortCode() + "' ("+loginNode.getId()+") on server! Successfully logged in!");
                        storeNodeTofile(loginNode);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (loginNode == null) {
                messenger.info("Node login failed!");
            }
        }else{
            messenger.error("User is already authenticated! Please use '" + AbstractCommand.LOGOUT + "' first!");
        }
        currentState.handle(this);
    }
    
    /**
     * @param apiPath
     * @see NodeCommand.REFERRAL
     */
    public void getReferralLink(String apiPath) {
        if(isAuthenticated()){
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = "";
            try {
                jsonString = mapper.writeValueAsString(node);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            
            webApiConnector.setJsonEntity(jsonString);
            String jsonResponse = webApiConnector.sendRequest(apiPath, RequestMethod.POST);
            if(jsonResponse != null && !jsonResponse.isEmpty()){
                WebApiGetReferralUrlResponse webApiGetReferralUrlResponse;
                try {
                    webApiGetReferralUrlResponse = new ObjectMapper().readValue(jsonResponse, WebApiGetReferralUrlResponse.class);
                    String url = webApiGetReferralUrlResponse.getBody();
                    messenger.info("Your referring URL is " + url);
                    Desktop.getDesktop().browse(new URL(url).toURI());
                } catch (IOException | URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }else{
            messenger.error("Please register or login to get your referring URL!");
        }
    }
    
    /**
     * 
     * @see NodeCommand.LOGOUT
     */
    public void logout(){
        if(isAuthenticated()){
            messenger.info("Succesfully loged out node '" + node.getShortCode() + "' ("+node.getId()+")!");
            this.node = null;
        }else{
            messenger.warning("Could not logout! There is no node authenticated!");
        }
        currentState.handle(this);
    }

    public void registerNodeWeb(APIConnector apiConnector, String apiPath, Node node) {
        if(!isAuthenticated()){
            messenger.info("Sending node registration for user '" + node.getUserEmail() + "'!");
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = "";
            try {
                jsonString = mapper.writeValueAsString(node);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            apiConnector.setJsonEntity(jsonString);
            String jsonResponse = apiConnector.sendRequest(apiPath, RequestMethod.POST);
            
            Node registerNode = null;
            WebApiNodeRegisterResponse webApiNodeRegisterResponse = null;
            if(jsonResponse != null && !jsonResponse.isEmpty()){
                try {
                    webApiNodeRegisterResponse = new ObjectMapper().readValue(jsonResponse, WebApiNodeRegisterResponse.class);
                    if (webApiNodeRegisterResponse.getStatus()) {
                        registerNode = webApiNodeRegisterResponse.getBody();
                        if (registerNode.getId() != null) {
                            this.node = registerNode;
                            messenger.info("Succesfuly registered with node "+registerNode.getShortCode()+"("+registerNode.getId()+"). "+webApiNodeRegisterResponse.getMessage());
                            storeNodeTofile(registerNode);
                        }else{
                            messenger.info("Node registration failed: empty node ID from server!");
                        }   
                    }else{
                        messenger.info("Node registration failed: " + webApiNodeRegisterResponse.getMessage());
                    }
                } catch (IOException e) {
                    logger.warning(e.getMessage());
                    messenger.info("Node registration failed: communication failure!");
                }
            }
        }else{
            messenger.error("User is already authenticated! Please use '" + AbstractCommand.LOGOUT + "' first!");
        }
        currentState.handle(this);
    }
    
    /**
     * 
     * @param apiPath
     * @param node
     * @see NodeCommand.REGISTER
     */
    public void registerNode(APIConnector apiConnector, String apiPath, Node node) {
        if(!isAuthenticated()){
            messenger.info("Sending node registration for user '" + node.getUserEmail() + "'!");
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = "";
            try {
                jsonString = mapper.writeValueAsString(node);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            apiConnector.setJsonEntity(jsonString);
            String jsonResponse = apiConnector.sendRequest(apiPath, RequestMethod.POST);
            
            Node registerNode = null;
            if(jsonResponse != null && !jsonResponse.isEmpty()){
                try {
                    registerNode = new ObjectMapper().readValue(jsonResponse, Node.class);
                    if (registerNode.getId() != null) {
                        this.node = registerNode;
                        messenger.info("Succesfuly registered with node "+registerNode.getShortCode()+"("+registerNode.getId()+")!");
                        storeNodeTofile(registerNode);
                    }else{
                        messenger.info("Node registration failed: empty node ID from server!");
                    }   
                } catch (IOException e) {
                    logger.warning(e.getMessage());
                    messenger.info("Node registration failed: communication failure!");
                }
            }
        }else{
            messenger.error("User is already authenticated! Please use '" + AbstractCommand.LOGOUT + "' first!");
        }
        currentState.handle(this);
    }
    
    /**
     * 
     * @param apiPath
     * @see NodeCommand.NEXT
     */
    protected void getNextTask(String apiPath){
        messenger.info("Sending next task request to Daratus API for node "+ node.getShortCode() +" (" + node.getId() + ")!");
        String jsonResponse = apiConnector.sendRequest(apiPath + getSecretKey(), RequestMethod.GET);
        if(jsonResponse != null){
            try {
                Task task = mapper.readValue(jsonResponse, Task.class);
                task.addTaskObserver(this);
                messenger.info("Got a task '" + task.getClass().getSimpleName() + "' with target URL '" + task + "' from server!");
                setCurrentTask(task);
            } catch (IOException e) {
                logger.warning("Could not read task from server!");
            }
        }else{
            logger.warning("No response from server!");
        }
    }
    
    /**
     * 
     * @see NodeCommand.EXECUTE
     */
    protected void executeCurrentTask(){
        currentTask.execute(this);
    }
    
    /**
     * 
     * @see NodeCommand.LOOP
     */
    protected void executeTaskLoop(){
        while(isBlocked()){
            System.out.println();
            System.out.println("...looping...");
            executeCurrentTask();
        }
        messenger.info("Task loop has been stopped succesfully!");
    }
    
    private void sendResponse(Task task){
        try {
            apiConnector.setJsonEntity(mapper.writeValueAsString(task));
            System.out.println("Sending task result response to Daratus API to path '" + APIConnector.NEXT_TASK_PATH + getNode().getId() + "'!");
            apiConnector.sendRequest(APIConnector.NEXT_TASK_PATH + getNode().getId(), RequestMethod.POST);
            setCurrentTask(nullTask);
            System.out.println("Result has been sent!");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    
    public void addContextObserver(ContextObserver stateObserver){
        stateObservers.add(stateObserver);
    }
    
    protected void notifyObservers(){
        Iterator<ContextObserver> iterator = stateObservers.iterator();
        while (iterator.hasNext()) {
            iterator.next().notify(this);
        }
    }
    
    public void notify(Task task) {
        if(task.isCompleted()){
            sendResponse(task);
        }else if(isBlocked()){
            getNextTask(APIConnector.NEXT_TASK_PATH);
        }
    }

    public void run() {
        executeTaskLoop();
    }
    
}
