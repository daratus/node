package com.daratus.node;

public interface APIConnector {

    public void setJsonEntity(String json);
    
    public String sendRequest(String path, RequestMethod method);
    
}
