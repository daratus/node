package com.daratus.node.domain;

import com.daratus.node.ScrapingConnector;

public class GetData extends Task {

    private ScrapingConnector connector;
    
    public GetData(ScrapingConnector connector) {
        this.connector = connector;
    }
    
    @Override
    public void execute(){
        String htmlResponse = connector.scrape(targetURL);
        System.out.println("Got HTML response!");
        System.out.println(htmlResponse!=null);
    }

}
