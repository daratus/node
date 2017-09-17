package com.daratus.node;

public class LogedinState extends NodeState{

    private NodeState initialState;
    
    public LogedinState(NodeState initialState) {
        this.initialState = initialState;
    }

    @Override
    public void handle(NodeContext context) {
        if(!context.isLoggedin()){
            context.setNodeState(initialState);
        }else if(context.isRunning()){
            context.setNodeState(nextState);
        }
    }

}
