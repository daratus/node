package com.daratus.node;

public interface APIConnector {

    public void setJsonEntity(String name, String json);
    
    public String sendRequest(String path, RequestMethod method);
    
}
