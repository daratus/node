package com.daratus.node;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;

public class APIHttpConnector extends AbstractHttpConnector implements APIConnector{

    private URIBuilder uriBuilder;
    
    private HttpHost target;
    
    public APIHttpConnector(String host, int port, String scheme) {
        super();
        uriBuilder = new URIBuilder();
        target = new HttpHost(host, port, scheme);
    }

    public String sendRequest(String path, RequestMethod method) {
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
                System.out.println(getRequest.getRequestLine());
                response = httpClient.execute(getRequest);
                break;
            case POST:
                HttpPost postRequest = new HttpPost(uri);
                postRequest.addHeader("accept", "application/json");
                response = httpClient.execute(target, postRequest);
                break;
            case PUT:
                break;
            case DELETE:
                break;
            }
            
            responseToken = parseResponseContent(response);
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return responseToken;
    }
    
}
