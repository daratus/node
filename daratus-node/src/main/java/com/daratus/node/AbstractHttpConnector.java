package com.daratus.node;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public abstract class AbstractHttpConnector {

    protected CloseableHttpClient httpClient;
    
    protected Logger logger = Logger.getLogger(APIHttpConnector.class.getSimpleName());
    
    public AbstractHttpConnector() {
        httpClient = HttpClients.createDefault();
    }
    
    protected String parseResponseContent(CloseableHttpResponse response) throws UnsupportedOperationException, IOException{
        String responseToken = "";
        if(response!=null && response.getStatusLine().getStatusCode() == 200){
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while( (line = reader.readLine()) != null){
                stringBuilder.append(line);
            }
            responseToken = stringBuilder.toString();
        }
        return responseToken;
    }
    
}
