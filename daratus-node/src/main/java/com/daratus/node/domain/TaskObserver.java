package com.daratus.node.domain;

/**
 * 
 * @author Zilvinas Vaira
 *
 */
public interface TaskObserver {
    
    /**
     * 
     * @param task
     */
    public void notify(Task task);

}
