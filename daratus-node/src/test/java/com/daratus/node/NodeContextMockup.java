package com.daratus.node;

import com.daratus.node.domain.NullTask;

public class NodeContextMockup extends NodeContext {

    public NodeContextMockup() {
        super(null, null, null);
    }
    
    @Override
    public void run() {
        while(isBlocked()){
            try {
                System.out.println("Executing loop!");
                Thread.sleep(1 * NullTask.SECONDS_CONVERSION_RATE / 10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
}
