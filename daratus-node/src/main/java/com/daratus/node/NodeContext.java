package com.daratus.node;

import java.io.IOException;
import java.util.logging.Logger;

import com.daratus.node.console.APICommand;
import com.daratus.node.console.AbstractCommand;
import com.daratus.node.domain.NullTask;
import com.daratus.node.domain.Task;
import com.daratus.node.domain.TaskObserver;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NodeContext implements TaskObserver, Runnable{
    
    private APIConnector apiConnector;
    
    private ScrapingConnector scrapingConnector;

    private ObjectMapper mapper;
    
    protected NodeState currentState = null;
    
    private boolean isBlocked = false;
    
    private String name = null;
    
    private Task currentTask;

    private final Task nullTask = new NullTask();
    
    private Logger logger;
    
    public NodeContext(APIConnector apiConnector, ScrapingConnector scrapingConnector, ObjectMapper mapper) {
        this.apiConnector = apiConnector;
        this.scrapingConnector = scrapingConnector;
        this.mapper = mapper;
        nullTask.addTaskObserver(this);
        setCurrentTask(nullTask);
        logger = getLogger(this.getClass().getSimpleName());
    }
    
    public Logger getLogger(String className){
        return Logger.getLogger(className);
    }

    public APIConnector getAPIConnector() {
        return apiConnector;
    }
    
    public ObjectMapper getMapper() {
        return mapper;
    }
    
    public void handleCurrentState(){
        currentState.handle(this);
    }

    public void setCurrentState(NodeState state){
        this.currentState = state;
    }
    
    public NodeState getCurrentState() {
        return currentState;
    }
    
    public void setBlocked(boolean isBlocked){
        if(!this.isBlocked && !isBlocked){
            logger.warning("Can not execute stop command. It is already stoped!");
        }
        this.isBlocked = isBlocked;
    }
    
    public boolean isBlocked(){
        return isBlocked;
    }
    
    public void authenticate(String apiPath, String name){
        if(!isAuthenticated()){
            System.out.println("Sending authetication request to Daratus API for node ID '" + name + "'!");
            String jsonResponse = apiConnector.sendRequest(apiPath + name, RequestMethod.GET);
            if(jsonResponse != null){
                setName(name);
                System.out.println("Found node ID '" + name + "' on server! Succesfuly authenticated!");
            }
        }else{
            logger.warning("User is already authenticated! Please use '" + AbstractCommand.LOGOUT + "' first!");
        }
    }
    
    public void setName(String name) {
        if(this.name == null){
            this.name = name;
        }
    }
    
    public String getName() {
        return name;
    }

    public boolean isAuthenticated(){
        return name != null;
    }
    
    public void logout(){
        if(isAuthenticated()){
            System.out.println("Succesfully loged out node ID '" + name + "'!");
            this.name = null;
        }else{
            logger.warning("Could not logout! There is no node authenticated!");
        }
    }
    
    protected void getNextTask(String apiPath){
        System.out.println("Sending next taks request to Daratus API for node ID '" + name + "'!");
        String jsonResponse = apiConnector.sendRequest(apiPath + getName(), RequestMethod.GET);
        if(jsonResponse != null){
            try {
                Task task = mapper.readValue(jsonResponse, Task.class);
                task.addTaskObserver(this);
                System.out.println("Got a task '" + task.getClass().getSimpleName() + "' with target URL '" + task + "' from server!");
                setCurrentTask(task);
            } catch (IOException e) {
                logger.warning("Could not read task from server!");
            }
        }else{
            logger.warning("No response from server!");
        }
    }
    
    protected void executeCurrentTask(){
        currentTask.execute(scrapingConnector);
    }
    
    protected void executeTaskLoop(){
        while(isBlocked()){
            executeCurrentTask();
        }
    }
    
    public void setCurrentTask(Task currentTask) {
        if(currentTask != null){
            this.currentTask = currentTask;
        }else{
            this.currentTask = nullTask;
        }
    }
    
    private void sendResponse(Task task){
        try {
            apiConnector.setJsonEntity(mapper.writeValueAsString(task));
            logger.info("Sending task result response to Daratus API to path '" + APICommand.NEXT_TASK_PATH + getName() + "'!");
            apiConnector.sendRequest(APICommand.NEXT_TASK_PATH + getName(), RequestMethod.POST);
            setCurrentTask(nullTask);
            logger.info("Result has been sent!");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
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
