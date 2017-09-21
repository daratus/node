package com.daratus.node;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

public class APIHttpConnector extends AbstractHttpConnector implements APIConnector{

    private URIBuilder uriBuilder;
    
    private HttpHost target;
    
    private String entityToken = "";
    
    private Logger logger = Logger.getLogger(APIHttpConnector.class.getSimpleName());
    
    public APIHttpConnector(String host, int port, String scheme) {
        super();
        uriBuilder = new URIBuilder();
        target = new HttpHost(host, port, scheme);
    }
    
    public void setJsonEntity(String json){
        entityToken = json;
    }

    public String sendRequest(String path, RequestMethod method) {
        httpClient = HttpClients.createDefault();
        String responseToken = null;
        try {
            uriBuilder.setHost(target.getHostName());
            uriBuilder.setPort(target.getPort());
            uriBuilder.setScheme(target.getSchemeName());
            uriBuilder.setPath(path);
            URI uri = uriBuilder.build();
            
            CloseableHttpResponse response = null;
            switch (method) {
            case GET:
                HttpGet getRequest = new HttpGet(uri);
                getRequest.addHeader("accept", "application/json");
                logger.info("Sending GET request to Daratus API path '" + path + "' for host '" + uriBuilder.getHost() + "'!");
                response = httpClient.execute(getRequest);
                logger.info("Got response! Status is '" + response.getStatusLine() + "'!");
                break;
            case POST:
                HttpPost postRequest = new HttpPost(uri);
                postRequest.addHeader("accept", "application/json");
                if(!entityToken.isEmpty()){
                    postRequest.setEntity(new StringEntity(entityToken, ContentType.APPLICATION_JSON));
                    entityToken = "";
                }else{
                    logger.warning("Post data is empty!");
                }
                logger.info("Sending POST request to Daratus API path '" + path + "' for host '" + uriBuilder.getHost() + "'!");
                response = httpClient.execute(target, postRequest);
                logger.info("Got response! Status is '" + response.getStatusLine() + "'!");
                break;
            case PUT:
                break;
            case DELETE:
                break;
            }
            
            responseToken = parseResponseContent(response);
            httpClient.close();
            
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        } 
        
        return responseToken;
    }
    
}
