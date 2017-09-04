package com.daratus.node;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.daratus.node.domain.Task;
import com.daratus.node.domain.TaskFactory;

public class APIConnector {

    private URIBuilder uriBuilder;
    
    private CloseableHttpClient httpClient;
    
    public APIConnector(URIBuilder uriBuilder) {
        this.uriBuilder = uriBuilder;
        httpClient = HttpClients.createDefault();
    }
    
    public void sendRequest(String path){
        try {
            uriBuilder.setPath("/data-contract/next-task/");
            URI uri = uriBuilder.build();
            HttpGet getRequest = new HttpGet(uri);
            getRequest.addHeader("accept", "application/json");
            
            HttpResponse response = httpClient.execute(getRequest);
            
            if(response.getStatusLine().getStatusCode() == 200){
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                TaskFactory taskFactory = new TaskFactory();
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while( (line = reader.readLine()) != null){
                    stringBuilder.append(line);
                }
                
                Task task = taskFactory.createTaskFromJson(stringBuilder.toString());
            }

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
        
    }
    
}
