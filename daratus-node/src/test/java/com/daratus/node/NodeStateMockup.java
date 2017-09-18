package com.daratus.node;

public class NodeStateMockup extends NodeState {

    public NodeState getNextSate(){
        return nextState;
    }
    
    @Override
    public void handle(NodeContext context) {
        // TODO Auto-generated method stub

    }

}
