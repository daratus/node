package com.daratus.node;

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
        context.getNextTask(apiPath);
    }

    public void executeCurrentTask(NodeContext context){
        context.executeCurrentTask();
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
