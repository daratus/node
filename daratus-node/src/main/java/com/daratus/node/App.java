package com.daratus.node;

import java.util.Scanner;

import org.apache.http.client.utils.URIBuilder;

import com.daratus.node.console.AbstractCommand;
import com.daratus.node.console.CommandFactory;
import com.daratus.node.console.DefaultCommand;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        
        URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setHost("86.100.97.40");
        uriBuilder.setScheme("http");
        uriBuilder.setPort(8080);
        APIConnector apiConnector = new APIConnector(uriBuilder);
        
        Scanner scanner = new Scanner(System.in);
        CommandFactory factory = new CommandFactory(apiConnector);
        AbstractCommand command = new DefaultCommand(AbstractCommand.HELP);
        command.execute();
        while (! command.evaluate(AbstractCommand.EXIT)) {
            command = factory.createCommand(scanner.nextLine());
            command.execute();
        }
        scanner.close();
        
        /*try {
            URIBuilder uriBuilder = new URIBuilder();
            uriBuilder.setHost("86.100.97.40");
            uriBuilder.setScheme("http");
            uriBuilder.setPort(8080);
            uriBuilder.setPath("/data-contract/next-task/");
            URI uri = uriBuilder.build();
            
            CloseableHttpClient httpClient = HttpClients.createDefault();
            
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
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        
    }
}
