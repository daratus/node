package com.daratus.node;

/**
 * 
 * @author Zilvinas Vaira
 *
 */
public abstract class NodeState {
    
    protected NodeState nextState = null;
    
    public void setNextState(NodeState nextstate){
        this.nextState = nextstate;
    }
    
    public abstract void handle(NodeContext context);

}
