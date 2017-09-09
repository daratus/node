package com.daratus.node;

public interface APIConnector {

    public String sendRequest(String path, RequestMethod method);
    
}
