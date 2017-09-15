package com.daratus.node;

import com.daratus.node.console.APICommand;
import com.daratus.node.domain.Task;
import com.daratus.node.domain.TaskObserver;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NodeContext implements TaskObserver{
    
    private APIConnector apiConnector;
    
    private ScrapingConnector scrapingConnector;

    private ObjectMapper mapper;
    
    private String name = null;
    
    private Task currentTask = null;
    
    public NodeContext(APIConnector apiConnector, ScrapingConnector scrapingConnector, ObjectMapper mapper) {
        this.apiConnector = apiConnector;
        this.scrapingConnector = scrapingConnector;
        this.mapper = mapper;
    }

    public APIConnector getAPIConnector() {
        return apiConnector;
    }
    
    public ObjectMapper getMapper() {
        return mapper;
    }
    
    public boolean isLoggedin(){
        return name != null;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public void setCurrentTask(Task currentTask) {
        this.currentTask = currentTask;
        this.currentTask.addTaskObserver(this);
    }
    
    public void executeCurrentTask(){
        if(currentTask != null){
            currentTask.execute(scrapingConnector);
        }else{
            System.out.println("No task is currently available!");
        }
    }
    
    private void sendResponse(Task task){
        try {
            apiConnector.setJsonEntity("result", mapper.writeValueAsString(task));
            apiConnector.sendRequest(APICommand.NEXT_TASK_PATH + getName(), RequestMethod.POST);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void notify(Task task) {
        if(task.isCompleted()){
            sendResponse(task);
        }
    }
    
}
