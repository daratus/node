package com.daratus.node;

public class InitialState extends NodeState{

    
    
    @Override
    public void handle(NodeContext context) {
        if(context.isLoggedin()){
            context.setNodeState(nextState);
        }
    }

}
