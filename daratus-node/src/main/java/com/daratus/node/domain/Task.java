package com.daratus.node.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

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
    
    protected List<String> urls = new ArrayList<String>();
    
    private boolean isCompleted = false;
    
    private List<TaskObserver> taskObservers = new ArrayList<TaskObserver>();

    @Deprecated
    private Random randomizer = new Random();
    
    public void setTargetURL(String targetURL) {
        this.targetURL = targetURL;
    }
    
    public void setData(Map<String, String> data) {
        this.data = data;
    }
    
    public void setUrls(List<String> nextUrls) {
        this.urls = nextUrls;
    }
    
    protected void setCompleted(boolean isCompleted){
        this.isCompleted = isCompleted;
        Iterator<TaskObserver> iterator = taskObservers.iterator();
        while (iterator.hasNext()) {
            iterator.next().notify(this);
        }
    }
    
    public boolean isCompleted(){
        return isCompleted;
    }

    public void addTaskObserver(TaskObserver taskObserver){
        taskObservers.add(taskObserver);
    }
    
    public abstract void execute(ScrapingConnector connector);
    
    protected void buildUrls(String htmlResponse){
        // Generating fake urls
        ListIterator<String> iterator = urls.listIterator();
        while (iterator.hasNext()) {
            iterator.next();
            iterator.set("http://86.100.97.40:8080/template?id=" + randomizer.nextInt());
        }
    }
    
    protected void buildData(String htmlResponse){
        // Generating fake data
        Iterator<Entry<String, String>> dataIterator = data.entrySet().iterator();
        while (dataIterator.hasNext()) {
            Entry<String, String> entry = dataIterator.next();
            entry.setValue(entry.getKey().substring(0, 1) + randomizer.nextInt());
        }
    }
    
    @Override
    public String toString() {
        return targetURL;
    }

}
