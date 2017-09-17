package com.daratus.node;

public class RunningState extends NodeState{

    private NodeState initialState;

    private NodeState logedinState;
    
    public RunningState(NodeState initialState, NodeState logedinState) {
        this.initialState = initialState;
        this.logedinState = logedinState;

    }
    
    @Override
    public void handle(NodeContext context) {
        if(!context.isLoggedin()){
            context.setNodeState(initialState);
        }else if(!context.isRunning()){
            context.setNodeState(logedinState);
        }else{
            
        }
        
    }
    
    

}
