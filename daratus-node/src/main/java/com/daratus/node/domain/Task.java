package com.daratus.node.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Task {
    
    public static final String GET_DATA = "getData";

    public static final String GET_URLS = "getUrls";
    
    private String id = "";
    
    protected String targetURL = "";
    
    private Map<String, String> data = new HashMap<String, String>();
    
    private List<String> nextUrls = new ArrayList<String>();
    
    public void setTargetURL(String targetURL) {
        this.targetURL = targetURL;
    }
    
    public void addDataItem(String dataItem){
        String key = "link" + (data.size() + 1);
        data.put(key, dataItem);
    }

    public void addDataItem(String key, String dataItem){
        data.put(key, dataItem);
    }
    
    public void addNextUrl(String nextUrl){
        nextUrls.add(nextUrl);
    }
    
    public abstract void execute();
    
    @Override
    public String toString() {
        return targetURL;
    }

}
