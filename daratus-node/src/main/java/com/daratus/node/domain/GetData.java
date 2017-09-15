package com.daratus.node.domain;

import com.daratus.node.ScrapingConnector;

public class GetData extends Task {
    

    @Override
    public void execute(ScrapingConnector connector){
        String htmlResponse = connector.scrape(targetURL);
        
        buildData(htmlResponse);
        buildUrls(htmlResponse);
        
        System.out.println("Got HTML response: ");
        System.out.print(htmlResponse != null);
        System.out.println();
        
        setCompleted(true);
    }

}
