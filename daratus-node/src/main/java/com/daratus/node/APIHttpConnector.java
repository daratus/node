package com.daratus.node;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

public class APIHttpConnector extends AbstractHttpConnector implements APIConnector{

    private URIBuilder uriBuilder;
    
    private HttpHost target;
    
    private String entityToken = "";
    
    public APIHttpConnector(String host, int port, String scheme) {
        super();
        uriBuilder = new URIBuilder();
        target = new HttpHost(host, port, scheme);
    }
    
    public void setJsonEntity(String name, String json){
        entityToken = name + "=" + json;
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
                if(!entityToken.isEmpty()){
                    postRequest.setEntity(new StringEntity(entityToken, ContentType.APPLICATION_JSON));
                    entityToken = "";
                }
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
