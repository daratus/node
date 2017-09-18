package com.daratus.node;

public class NodeStateMockup extends NodeState {

    public NodeState getNextSate(){
        return nextState;
    }
    
    @Override
    public void handle(NodeContext context) {
        // Do nothing
    }

    @Override
    public void getNextTask(String apiPath, NodeContext context) {
        // Do nothing
    }

    @Override
    public void executeCurrentTask(NodeContext context) {
        // Do nothing
    }

}
