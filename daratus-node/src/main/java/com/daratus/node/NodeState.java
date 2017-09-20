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
    
    public boolean isNextState(){
        return nextState != null;
    }
    
    public abstract void getNextTask(String apiPath, NodeContext context);
    
    public abstract void executeCurrentTask(NodeContext context);
    
    public abstract void handle(NodeContext context);
    
}
