package com.daratus.node.domain;

/**
 * 
 * @author Zilvinas Vaira
 *
 */
public class NullTask extends Task {

    public static final long SECONDS_CONVERSION_RATE = 10000000L;
    
    private int sleepInterval = 10;
    
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
    public void execute() {
        try {
            Thread.sleep(sleepInterval * SECONDS_CONVERSION_RATE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
