package com.daratus.node;

public class AuthenticatedState extends NodeState{

    private NodeState initialState;
    
    public AuthenticatedState(NodeState initialState) {
        this.initialState = initialState;
    }

    @Override
    public void handle(NodeContext context) {
        if(!context.isAuthenticated()){
            context.setCurrentState(initialState);
        }else if(context.isRunning()){
            Thread contextThread = new Thread(context);
            context.setCurrentState(nextState);
            contextThread.start();
        }
    }

}
