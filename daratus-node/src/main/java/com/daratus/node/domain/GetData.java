package com.daratus.node.domain;

import org.w3c.dom.Document;

import com.daratus.node.NodeContext;
import com.daratus.node.NodeMessenger;
import com.daratus.node.ScrapingConnector;

/**
 * 
 * @author Zilvinas Vaira
 *
 */
public class GetData extends Task {
    
    public GetData() { }

    public GetData(GetDataProxy proxy) {
        this.targetURL = proxy.targetURL;
        this.data = proxy.data;
        this.urls = proxy.urls;
        this.isCompleted = proxy.isCompleted;
    }
    
    @Override
    public void execute(NodeContext context){
        ScrapingConnector connector = context.getScrapingConnector();
        NodeMessenger messenger = context.getMessenger();

        messenger.info("Executing '" + this.getClass().getSimpleName() + "' command...");
        System.out.println("Requesting HTML data from '" + targetURL + "' website...");
        String htmlResponse = connector.scrape(targetURL);
        messenger.info("Got response, it is " + (htmlResponse.isEmpty() ? "" : "NOT") + " empty!");
        
        Document htmlDocument = parseHtmlDocument(htmlResponse, context);
        boolean hasChanges = false;
        if(buildData(htmlDocument, context)){
            hasChanges = true;
        }
        if(buildUrls(htmlDocument, context)){
            hasChanges = true;
        }
        setCompleted(hasChanges);
        
    }

}
