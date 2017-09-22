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
        String responseToken = null;
        try {
            HttpGet getRequest = new HttpGet(new URI(uriToken));
            logger.info("Sending GET request to '" + uriToken + "'!");
            CloseableHttpResponse response = httpClient.execute(getRequest);
            logger.info("Got response! Status is '" + response.getStatusLine() + "'!");
            responseToken = parseResponseContent(response);
            httpClient.close();
        } catch (URISyntaxException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return responseToken;
    }
    
}
