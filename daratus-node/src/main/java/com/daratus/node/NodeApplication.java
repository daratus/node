package com.daratus.node;

import com.daratus.node.domain.Task;
import com.daratus.node.domain.TaskFactory;

public class NodeApplication {
    
    private APIConnector connector;

    private TaskFactory factory;
    
    private String name = null;
    
    private Task currentTask = null;
    
    public NodeApplication(APIConnector connector, TaskFactory factory) {
        this.connector = connector;
        this.factory = factory;
    }

    public APIConnector getConnector() {
        return connector;
    }
    
    public TaskFactory getFactory() {
        return factory;
    }
    
    public boolean hasName(){
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
            
        }
    }
}
