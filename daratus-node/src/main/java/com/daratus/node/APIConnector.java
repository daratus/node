package com.daratus.node;

import org.apache.http.HttpHost;

public interface APIConnector {

    public void setHostDetails(String host, int port, String scheme);
    
    public void setJsonEntity(String json);
    
    public HttpHost getHost();
    
    public String sendRequest(String path, RequestMethod method);
    
}
