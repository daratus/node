package com.daratus.node;

import java.io.IOException;

import com.daratus.node.domain.Task;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author Zilvinas Vaira
 *
 */
public class OperationalState extends NodeState{

    private NodeState initialState;
    
    public OperationalState(NodeState initialState) {
        this.initialState = initialState;
    }
    
    public void getNextTask(String apiPath, NodeContext context){
        APIConnector apiConnector = context.getAPIConnector();
        ObjectMapper mapper = context.getMapper();
        String jsonResponse = apiConnector.sendRequest(apiPath + context.getName(), RequestMethod.GET);
        if(jsonResponse != null){
            try {
                Task task = mapper.readValue(jsonResponse, Task.class);
                System.out.println("Got a task:");
                System.out.println(task.getClass().getSimpleName());
                System.out.println(task);
                context.setCurrentTask(task);
            } catch (IOException e) {
                System.out.println("Could not read task from server!");
            }
        }
    }

    public void executeCurrentTask(NodeContext context){
        if(context != null){
            
        }else{
            System.out.println("No task is currently available! Or must login first!");
        }
    }
    
    @Override
    public void handle(NodeContext context) {
        if(!context.isAuthenticated()){
            context.setCurrentState(initialState);
        }else if(context.isBlocked() && isNextState()){
            Thread contextThread = new Thread(context);
            context.setCurrentState(nextState);
            contextThread.start();
        }
    }

}
