package com.daratus.node.domain;

import com.daratus.node.NodeContext;

/**
 * 
 * @author Zilvinas Vaira
 *
 */
public class NullTask extends Task {

    public static final long SECONDS_CONVERSION_RATE = 1000L;
    
    private int sleepInterval = 3;
    
    /**
     * 
     * @param seconds
     */
    public void setSleepInterval(int seconds){
        sleepInterval = seconds;
    }
    
    /**
     * 
     * @return
     */
    public int getSleepInterval() {
        return sleepInterval;
    }
    
    @Override
    public void execute(NodeContext context) {
        logger.info("Sleeping for " + sleepInterval + " seconds...");
        try {
            Thread.sleep(sleepInterval * SECONDS_CONVERSION_RATE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("Done sleeping!");
        setCompleted(false);
    }

}
