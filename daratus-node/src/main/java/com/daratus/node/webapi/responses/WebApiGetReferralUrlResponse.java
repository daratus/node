package com.daratus.node.webapi.responses;

import com.daratus.node.domain.Node;

public class WebApiGetReferralUrlResponse {

private boolean status;
    
    private String body;
    
    private String error;
    
    private String message;
    
    public String getBody() {
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
