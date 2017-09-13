package com.daratus.node;

import com.daratus.node.domain.Task;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NodeContext {
    
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
    }
    
    public void executeCurrentTask(){
        if(currentTask != null){
            currentTask.execute(scrapingConnector);
            currentTask = null;
        }else{
            System.out.println("No task is currently available!");
        }
    }
}
