package com.daratus.node;

import java.util.Scanner;

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
        
        APIHttpConnector apiConnector = new APIHttpConnector("86.100.97.40", 8080, "http");
        ScrapingHttpConnector scrappingConnector = new ScrapingHttpConnector();
        TaskFactory taskFactory = new TaskFactory(scrappingConnector);
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
