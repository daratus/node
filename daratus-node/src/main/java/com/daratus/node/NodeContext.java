package com.daratus.node;

import com.daratus.node.domain.Task;
import com.daratus.node.domain.TaskFactory;

public class NodeContext {
    
    private APIConnector connector;

    private TaskFactory factory;
    
    private String name = null;
    
    private Task currentTask = null;
    
    public NodeContext(APIConnector connector, TaskFactory factory) {
        this.connector = connector;
        this.factory = factory;
    }

    public APIConnector getAPIConnector() {
        return connector;
    }
    
    public TaskFactory getFactory() {
        return factory;
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
            currentTask.execute();
            currentTask = null;
        }else{
            System.out.println("No task is currently available!");
        }
    }
}
