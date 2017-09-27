package com.daratus.node.domain;

import org.w3c.dom.Document;

import com.daratus.node.NodeContext;
import com.daratus.node.ScrapingConnector;

/**
 * 
 * @author Zilvinas Vaira
 *
 */
public class GetUrls extends Task {

    @Override
    public void execute(NodeContext context) {
        ScrapingConnector connector = context.getScrapingConnector();

        System.out.println("Executing '" + this.getClass().getSimpleName() + "' command...");
        System.out.println("Requesting HTML data from '" + targetURL + "' website...");
        String htmlResponse = connector.scrape(targetURL);
        System.out.println("Got response, it is " + (htmlResponse.isEmpty() ? "" : "NOT") + " empty!");

        Document htmlDocument = parseHtmlDocument(htmlResponse, context);
        setCompleted(buildUrls(htmlDocument, context));
    }

}
