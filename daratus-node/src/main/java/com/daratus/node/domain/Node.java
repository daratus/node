package com.daratus.node.domain;

import java.util.ArrayList;
import java.util.Date;

/**
 * 
 * @author Zilvinas Vaira
 *
 */
public class Node{
    
    /**
     * 
     */
    private Long id;

    private String shortCode = "";
    
    private String name;
    
    private String userEmail;
    
    private String userName;
    
    private Date registeredAt = new Date();
    
    private Task currentTask;
    
    private String location;
    
    private Task nullTask = new NullTask();
    
    private int totalExecutedTasksCount = 0;
    
    private ArrayList<Long> executedTasksTimes = new ArrayList<Long>();
    private ArrayList<Long> activeTimes = new ArrayList<Long>();
    
    private String secretKey;
    
    public Node(String name) {
        this.name = name;
        setCurrentTask(null);
    }

    public Node() {
        setCurrentTask(null);
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public String getLocation() {
        return location;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getUserName() {
        return this.userName;
    }
    
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    
    public String getUserEmail() {
        return this.userEmail;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public void setRegisteredAt(Date registeredAt) {
        this.registeredAt = registeredAt;
    }
    
    public Date getRegisteredAt( ) {
        return registeredAt;
    }
    
    public void receiveTask(Task task){
        if (task == null) {
            activeTimes.add((new Date()).getTime());
        }
        setCurrentTask(task);
    }
    
    public void receiveResult(Task result){
        //currentTask.execute(result);
        totalExecutedTasksCount++;
        executedTasksTimes.add((new Date()).getTime());
        setCurrentTask(null);
    }
    
    private void setCurrentTask(Task task){
        if(task != null){
            currentTask = task;
        }else{
            currentTask = nullTask;
        }
    }
    
    public Task getCurrentTask(){
        return currentTask;
    }
    
    public int getTotalExecutedTasksCount() {
        return totalExecutedTasksCount;
    }
    
    public ArrayList<Long> getExecutedTaskTimes() {
        return executedTasksTimes;
    }
    
    public ArrayList<Long> getActiveTimes() {
        return activeTimes;
    }
    
    public String getSecretKey() {
        return secretKey;
    }
    
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
    
}
