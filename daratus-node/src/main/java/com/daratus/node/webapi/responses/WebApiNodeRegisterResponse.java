package com.daratus.node.webapi.responses;

import com.daratus.node.domain.Node;

public class WebApiNodeRegisterResponse {

    private boolean status;
    
    private Node body;
    
    private String error;
    
    private String message;
    
    public Node getBody() {
        return body;
    }
    
    public boolean getStatus() {
        return status;
    }
    
    public String getError() {
        return error;
    }
    
    public String getMessage() {
        return message;
    }
    
}
