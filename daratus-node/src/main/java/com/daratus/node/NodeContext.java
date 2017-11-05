package com.daratus.node;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Logger;

import javax.xml.xpath.XPath;

import org.apache.http.HttpHost;
import org.jsoup.helper.W3CDom;

import com.daratus.node.console.APICommand;
import com.daratus.node.console.AbstractCommand;
import com.daratus.node.domain.Node;
import com.daratus.node.domain.NullTask;
import com.daratus.node.domain.Task;
import com.daratus.node.domain.TaskObserver;
import com.daratus.node.webapi.responses.WebApiGetReferralUrlResponse;
import com.daratus.node.webapi.responses.WebApiNodeRegisterResponse;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author Zilvinas Vaira
 *
 */
public class NodeContext implements TaskObserver, Runnable{
    
    private APIConnector apiConnector;
    
    private APIConnector webApiConnector;
    
    private ScrapingConnector scrapingConnector;

    private ObjectMapper mapper;
    
    private W3CDom w3cDom;
    
    private XPath xPath;
    
    protected NodeState currentState = null;
    
    private boolean isBlocked = false;
    
    private String name = null;
    
    private Node node = null;
    
    //private String secretKey = null;
    
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
    
    /*public void setName(String name) {
        if(this.name == null){
            this.name = name;
        }
    }*/
    
    /*public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }*/
    
    public String getSecretKey() {
        return node.getSecretKey();
    }
    
    /*public String getName() {
        return node.getShortCode();
    }*/

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
     * @param apiPath
     * @param name
     * @see NodeCommand.LOGIN
     */
    /*public void authenticate(String apiPath, String name){
        if(!isAuthenticated()){
            messenger.info("Sending authetication request to Daratus API for node ID '" + name + "'!");
            String jsonResponse = apiConnector.sendRequest(apiPath + name, RequestMethod.GET);
            if(jsonResponse != null){
                setName(name);
                messenger.info("Found node ID '" + name + "' on server! Succesfuly authenticated!");
            }
        }else{
            messenger.error("User is already authenticated! Please use '" + AbstractCommand.LOGOUT + "' first!");
        }
        currentState.handle(this);
    }*/
    
    public void authenticate(String apiPath) {
        if(!isAuthenticated()){
            File file = new File("node.txt");
            if (file.exists()) {
                try {
                    FileInputStream fileIn = new FileInputStream("node.txt");
                    ObjectInputStream in = new ObjectInputStream(fileIn);
                    Node node = (Node) in.readObject();
                    in.close();
                    fileIn.close();
                    authenticate(apiPath, node);
                 } catch (IOException i) {
                    i.printStackTrace();
                 } catch (ClassNotFoundException c) {
                    c.printStackTrace();
                 } 
            }
        }else{
            messenger.error("User is already authenticated! Please use '" + AbstractCommand.LOGOUT + "' first!");
        }
        currentState.handle(this);
    }
    
    public void registerThroughCmdLie(String apiPath) {
        String userEmail = "";
        while (userEmail.isEmpty()) {
            messenger.info("Enter email:");
            Scanner scanner = new Scanner(System.in);
            userEmail = scanner.nextLine();
            if (userEmail.isEmpty())
                messenger.info("Email is empty!");
        }
        
        String ethAddress = "";
        messenger.info("Enter ethereum address (optional):");
        Scanner scanner = new Scanner(System.in);
        ethAddress = scanner.nextLine();

        String refCode = "";
        messenger.info("Enter your referral code (optional):");
        scanner = new Scanner(System.in);
        refCode = scanner.nextLine();
        
        Node node = new Node();
        node.setUserEmail(userEmail);
        node.setEthAddress(ethAddress);
        node.setReferalCode(refCode);
        registerNode(apiPath, node);
    }
    
    public void authenticateThroughCmdLine(String apiPath) {
        String nodeCode = "";
        while (nodeCode.isEmpty()) {
            messenger.info("Enter node code:");
            Scanner scanner = new Scanner(System.in);
            nodeCode = scanner.nextLine();
            if (nodeCode.isEmpty())
                messenger.info("Node code is empty!");
        }
        
        String nodeSecretKey = "";
        while (nodeSecretKey.isEmpty()) {
            messenger.info("Enter node secret key:");
            Scanner scanner = new Scanner(System.in);
            nodeSecretKey = scanner.nextLine();
            if (nodeSecretKey.isEmpty())
                messenger.info("Node secret key is empty!");
        }
        
        Node node = new Node();
        node.setSecretKey(nodeSecretKey);
        node.setShortCode(nodeCode);
        authenticate(apiPath, node);
    }
    
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
            Node loggedInNode = null;
            if(jsonResponse != null && !jsonResponse.isEmpty()){
                try {
                    loggedInNode = new ObjectMapper().readValue(jsonResponse, Node.class);
                    if (loggedInNode.getId() != null) {
                        messenger.info("Found node '" + loggedInNode.getShortCode() + "' ("+loggedInNode.getId()+") on server! Successfully logged in!");
                        this.node = loggedInNode;
                        //setName(loggedInNode.getId().toString());
                        //setSecretKey(loggedInNode.getSecretKey());
                        
                        File file = new File("node.txt");
                        if(!file.exists()) {
                            try {
                                FileOutputStream fileOut = new FileOutputStream("node.txt");
                                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                                out.writeObject(loggedInNode);
                                out.close();
                                fileOut.close();
                             } catch (IOException i) {
                                i.printStackTrace();
                             }
                        }
                    }
                } catch (JsonParseException e) {
                    e.printStackTrace();
                } catch (JsonMappingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (loggedInNode == null) {
                messenger.info("Node login failed!");
            }
        }else{
            messenger.error("User is already authenticated! Please use '" + AbstractCommand.LOGOUT + "' first!");
        }
        currentState.handle(this);
    }
    
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
                    messenger.info("Your referring URL is "+url);
                    Desktop.getDesktop().browse(new URL(url).toURI());
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e) {
                    //e.printStackTrace();
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
    
    public void registerNode(String apiPath, Node node) {
        if(!isAuthenticated()){
            messenger.info("Sending node registration for user '" + node.getUserEmail() + "'!");
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = "";
            try {
                jsonString = mapper.writeValueAsString(node);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            webApiConnector.setJsonEntity(jsonString);
            String jsonResponse = webApiConnector.sendRequest(apiPath, RequestMethod.POST);
            
            Node registeredNode = null;
            WebApiNodeRegisterResponse webApiNodeRegisterResponse = null;
            if(jsonResponse != null && !jsonResponse.isEmpty()){
                try {
                    webApiNodeRegisterResponse = new ObjectMapper().readValue(jsonResponse, WebApiNodeRegisterResponse.class);
                    if (webApiNodeRegisterResponse.getStatus()) {
                        registeredNode = webApiNodeRegisterResponse.getBody();
                        if (registeredNode.getId() != null) {
                            messenger.info("Succesfuly registered with node "+registeredNode.getShortCode()+"("+registeredNode.getId()+"). "+webApiNodeRegisterResponse.getMessage());
                            FileOutputStream fileOut =new FileOutputStream("node.txt");
                            ObjectOutputStream out = new ObjectOutputStream(fileOut);
                            out.writeObject(registeredNode);
                            out.close();
                            fileOut.close();
                            this.node = registeredNode;
                            //setName(registeredNode.getId().toString());
                            //setSecretKey(registeredNode.getSecretKey());
                        }   
                    }
                } catch (JsonParseException e) {
                    e.printStackTrace();
                } catch (JsonMappingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
            }
            if (registeredNode == null) {
                if (webApiNodeRegisterResponse != null)
                    messenger.info("Node registration failed: "+webApiNodeRegisterResponse.getMessage());
                else
                    messenger.info("Node registration failed!");
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
            System.out.println("Sending task result response to Daratus API to path '" + APICommand.NEXT_TASK_PATH + getNode().getId() + "'!");
            apiConnector.sendRequest(APICommand.NEXT_TASK_PATH + getNode().getId(), RequestMethod.POST);
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
            getNextTask(APICommand.NEXT_TASK_PATH);
        }
    }

    public void run() {
        executeTaskLoop();
    }
    
}
