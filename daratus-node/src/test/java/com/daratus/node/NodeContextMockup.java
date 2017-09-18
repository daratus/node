package com.daratus.node;

public class NodeContextMockup extends NodeContext {

    public NodeContextMockup() {
        super(null, null, null);
    }
    
    public NodeState getNodeState(){
        return currentState;
    }
    
}
