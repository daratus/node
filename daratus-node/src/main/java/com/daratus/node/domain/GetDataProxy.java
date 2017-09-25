package com.daratus.node.domain;

public class GetDataProxy extends GetData {

    @Override
    protected void setCompleted(boolean isCompleted){
        this.isCompleted = isCompleted;
        notifyObservers(new GetData(this));
    }
    
}
