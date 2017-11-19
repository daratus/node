package com.daratus.node;

import org.apache.http.HttpHost;

public interface APIConnector {
    
    public static final String NEXT_TASK_PATH = "/node/task/";
    
    public static final String NODE_PATH = "/node/";
    
    public static final String NODE_LOGIN_PATH = "/node/login/";
    
    public static final String NODE_REGISTER_PATH = "/node/register";

    public static final String NODE_WEB_REGISTER_PATH = "/api/node/register";
    
    public static final String NODE_WEB_REFERRAL_PATH = "/api/node/referral-link";

    public void setHostDetails(String host, int port, String scheme);
    
    public void setJsonEntity(String json);
    
    public HttpHost getHost();
    
    public String sendRequest(String path, RequestMethod method);
    
    public void setHost(HttpHost httpHost);
    
}
