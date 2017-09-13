package com.daratus.node.domain;

import com.daratus.node.ScrapingConnector;

public class GetData extends Task {

    @Override
    public void execute(ScrapingConnector connector){
        String htmlResponse = connector.scrape(targetURL);
        System.out.println("Got HTML response!");
        System.out.println(htmlResponse!=null);
    }

}
