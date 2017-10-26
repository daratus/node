package com.daratus.node;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.xml.xpath.XPath;

import org.jsoup.helper.W3CDom;

import com.daratus.node.console.APICommand;
import com.daratus.node.console.AbstractCommand;
import com.daratus.node.domain.Node;
import com.daratus.node.domain.NullTask;
import com.daratus.node.domain.Task;
import com.daratus.node.domain.TaskObserver;
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
    
    private ScrapingConnector scrapingConnector;

    private ObjectMapper mapper;
    
    private W3CDom w3cDom;
    
    private XPath xPath;
    
    protected NodeState currentState = null;
    
    private boolean isBlocked = false;
    
    private String name = null;
    
    private String secretKey = null;
    
    private Task currentTask;

    private final Task nullTask = new NullTask();

    private NodeMessenger messenger = null;
    
    private Logger logger;
    
    private List<ContextObserver> stateObservers = new ArrayList<ContextObserver>();
    
    private Properties properties = new Properties();
    
    public NodeContext(APIConnector apiConnector, ScrapingConnector scrapingConnector, ObjectMapper mapper, W3CDom w3cDom, XPath xPath) {
        this.apiConnector = apiConnector;
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
    
    public void setName(String name) {
        if(this.name == null){
            this.name = name;
        }
    }
    
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
    
    public String getSecretKey() {
        return secretKey;
    }
    
    public String getName() {
        return name;
    }

    public boolean isAuthenticated(){
        return name != null;
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
    public void authenticate(String apiPath, String name){
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
    }
    
    public void authenticate(String apiPath) {
        if(!isAuthenticated()){
            File f = new File("node-key.txt");
            if(f.exists() && !f.isDirectory()) {
                String secretKey = null;
                try {
                    secretKey = new String(Files.readAllBytes(Paths.get(f.getName())));
                    secretKey = secretKey.replace("\n", "").replace("\r", "");
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (secretKey != null) {
                    messenger.info("Got secretkey, sending authetication request to Daratus API for node");
                    String jsonResponse = apiConnector.sendRequest(apiPath + secretKey, RequestMethod.GET);
                    Node loggedInNode = null;
                    if(jsonResponse != null && !jsonResponse.isEmpty()){
                        try {
                            loggedInNode = new ObjectMapper().readValue(jsonResponse, Node.class);
                            if (loggedInNode.getId() != null) {
                                messenger.info("Successfully loged as '" + loggedInNode.getName() + "'!");
                                setName(loggedInNode.getId().toString());
                                setSecretKey(secretKey);
                            }
                        } catch (JsonParseException e) {
                            e.printStackTrace();
                        } catch (JsonMappingException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        messenger.info("Login failed! invalid node key found! Please try to re-register!");
                    }
                } else {
                    messenger.info("Couldn't find node-key, please register at first!!");
                }
            } else {
                messenger.info("Couldn't find node-key, please register at first!!");
            }
        }else{
            messenger.error("User is already authenticated! Please use '" + AbstractCommand.LOGOUT + "' first!");
        }
        currentState.handle(this);
    }
    
    /**
     * 
     * @see NodeCommand.LOGOUT
     */
    public void logout(){
        if(isAuthenticated()){
            messenger.info("Succesfully loged out node ID '" + name + "'!");
            this.name = null;
        }else{
            messenger.warning("Could not logout! There is no node authenticated!");
        }
        currentState.handle(this);
    }
    
    public void registerNode(String apiPath, Node node) {
        if(!isAuthenticated()){
            messenger.info("Sending node registration '" + node.getName() + "'!");
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = "";
            try {
                jsonString = mapper.writeValueAsString(node);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            apiConnector.setJsonEntity(jsonString);
            String jsonResponse = apiConnector.sendRequest(apiPath, RequestMethod.POST);
            Node registeredNode = null;
            if(jsonResponse != null){
                try {
                    registeredNode = new ObjectMapper().readValue(jsonResponse, Node.class);
                    if (registeredNode.getId() != null) {
                        messenger.info("Found node '" + registeredNode.getName() + "' on server! Succesfuly registered!");
                        PrintWriter writer = new PrintWriter("node-key.txt", "UTF-8");
                        writer.println(registeredNode.getSecretKey());
                        writer.close();
                        setName(registeredNode.getId().toString());
                        setSecretKey(registeredNode.getSecretKey());
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
        messenger.info("Sending next task request to Daratus API for node ID '" + name + "'!");
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
            System.out.println("Sending task result response to Daratus API to path '" + APICommand.NEXT_TASK_PATH + getName() + "'!");
            apiConnector.sendRequest(APICommand.NEXT_TASK_PATH + getName(), RequestMethod.POST);
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
