package com.daratus.node;

import java.util.Scanner;

import org.apache.http.client.utils.URIBuilder;

import com.daratus.node.console.AbstractCommand;
import com.daratus.node.console.CommandFactory;
import com.daratus.node.console.DefaultCommand;
import com.daratus.node.domain.TaskFactory;

/**
 * Hello world!
 *
 */
public class NodeApplication 
{
    public static void main( String[] args )
    {
        
        URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setHost("86.100.97.40");
        uriBuilder.setScheme("http");
        uriBuilder.setPort(8080);

        APIConnector apiConnector = new APIConnector(uriBuilder);
        TaskFactory taskFactory = new TaskFactory();
        NodeContext context = new NodeContext(apiConnector, taskFactory);
        
        Scanner scanner = new Scanner(System.in);
        CommandFactory factory = new CommandFactory(context);
        AbstractCommand command = new DefaultCommand(AbstractCommand.HELP);
        command.execute();
        while (! command.evaluate(AbstractCommand.EXIT)) {
            command = factory.createCommand(scanner.nextLine());
            command.execute();
        }
        scanner.close();
         
    }
}
