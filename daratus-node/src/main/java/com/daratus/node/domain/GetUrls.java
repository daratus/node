package com.daratus.node.domain;

import com.daratus.node.ScrapingConnector;

/**
 * 
 * @author Zilvinas Vaira
 *
 */
public class GetUrls extends Task {

    @Override
    public void execute(ScrapingConnector connector) {
        System.out.println("Executing '" + this.getClass().getSimpleName() + "' command...");
        System.out.println("Requesting HTML data from '" + targetURL + "' website...");
        String htmlResponse = connector.scrape(targetURL);
        System.out.println("Got response, it is " + (htmlResponse.isEmpty() ? "" : "NOT") + " empty!");
        
        buildUrls(htmlResponse);
        setCompleted(true);
    }

}
