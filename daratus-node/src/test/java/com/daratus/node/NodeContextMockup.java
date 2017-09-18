package com.daratus.node;

import com.daratus.node.domain.NullTask;

public class NodeContextMockup extends NodeContext {

    public NodeContextMockup() {
        super(null, null, null);
    }
    
    public NodeState getNodeState(){
        return currentState;
    }
    
    @Override
    public void run() {
        while(isRunning()){
            try {
                System.out.println("Executing loop!");
                Thread.sleep(1 * NullTask.SECONDS_CONVERSION_RATE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
}
