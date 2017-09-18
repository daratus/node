package com.daratus.node;

public class InitialState extends NodeState{

    @Override
    public void handle(NodeContext context) {
        context.setRunning(false);
        if(context.isAuthenticated() && isNextState()){
            context.setCurrentState(nextState);
        }
    }

}
