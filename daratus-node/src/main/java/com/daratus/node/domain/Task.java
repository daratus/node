package com.daratus.node.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.daratus.node.ScrapingConnector;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * 
 * @author Zilvinas Vaira
 *
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
    @Type(value = GetData.class),
    @Type(value = GetUrls.class),
    @Type(value = NullTask.class)
})
public abstract class Task {
    
    private Long id;
    
    protected String targetURL = "";
    
    protected Map<String, String> data = new HashMap<String, String>();
    
    private List<String> urls = new ArrayList<String>();
    
    public void setTargetURL(String targetURL) {
        this.targetURL = targetURL;
    }
    
    public void setData(Map<String, String> data) {
        this.data = data;
    }
    
    public void setUrls(List<String> nextUrls) {
        this.urls = nextUrls;
    }
    
    public abstract void execute(ScrapingConnector connector);
    
    @Override
    public String toString() {
        return targetURL;
    }

}
