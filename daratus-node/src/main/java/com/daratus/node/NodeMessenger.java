package com.daratus.node;

public interface NodeMessenger {

    public void info(Object message);
    
    public void warning(Object message);
    
    public void error(Object message);
    
}
