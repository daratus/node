package com.daratus.node;

public interface ContextObserver {

    /**
     * 
     * @param task
     */
    public void notify(NodeContext context);

}
