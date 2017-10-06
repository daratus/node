package com.daratus.node;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

public class ScrapingHttpConnector extends AbstractHttpConnector implements ScrapingConnector{

    public String scrape(String uriToken) {
        httpClient = HttpClients.createDefault();
        String responseToken = "";
        try {
            uriToken = uriToken.startsWith("http") ? uriToken : "http://" + uriToken; 
            HttpGet getRequest = new HttpGet(new URI(uriToken));
            logger.info("Sending GET request to '" + uriToken + "'!");
            CloseableHttpResponse response = httpClient.execute(getRequest);
            logger.info("Got response! Status is '" + response.getStatusLine() + "'!");
            responseToken = parseResponseContent(response);
            httpClient.close();
        } catch (URISyntaxException | IOException e) {
            logger.warning("Could not scrape page and/or parse response for '" + uriToken + "'!");
        }
        return responseToken;
    }
    
}
