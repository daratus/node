package com.daratus.node;

import java.io.IOException;

import com.daratus.node.console.APICommand;
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
    
    private boolean isRunning = false;
    
    private String name = null;
    
    private Task currentTask;

    private final Task nullTask = new NullTask();
    
    public NodeContext(APIConnector apiConnector, ScrapingConnector scrapingConnector, ObjectMapper mapper) {
        this.apiConnector = apiConnector;
        this.scrapingConnector = scrapingConnector;
        this.mapper = mapper;
        setCurrentTask(nullTask);
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
    
    public void setRunning(boolean isRunnig){
        this.isRunning = isRunnig;
    }
    
    public boolean isBlocked(){
        return isRunning;
    }
    
    public void authenticate(String apiPath, String name){
        if(!isAuthenticated()){
            String jsonResponse = apiConnector.sendRequest(apiPath + name, RequestMethod.GET);
            if(jsonResponse != null){
                System.out.println("Got response!");
                System.out.println(jsonResponse);
                setName(name);
            }
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
        this.name = null;
    }
    
    protected void getNextTask(String apiPath){
        String jsonResponse = apiConnector.sendRequest(apiPath + getName(), RequestMethod.GET);
        if(jsonResponse != null){
            try {
                Task task = mapper.readValue(jsonResponse, Task.class);
                System.out.println("Got a task:");
                System.out.println(task.getClass().getSimpleName());
                System.out.println(task);
                setCurrentTask(task);
            } catch (IOException e) {
                System.out.println("Could not read task from server!");
            }
        }
    }
    
    protected void executeCurrentTask(){
        currentTask.execute(scrapingConnector);
    }
    
    public void setCurrentTask(Task currentTask) {
        if(currentTask != null){
            this.currentTask = currentTask;
        }else{
            this.currentTask = nullTask;
        }
        this.currentTask.addTaskObserver(this);
    }
    
    private void sendResponse(Task task){
        try {
            apiConnector.setJsonEntity("result", mapper.writeValueAsString(task));
            apiConnector.sendRequest(APICommand.NEXT_TASK_PATH + getName(), RequestMethod.POST);
            setCurrentTask(nullTask);
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
        while(isBlocked()){
            executeCurrentTask();
        }
    }
    
}
