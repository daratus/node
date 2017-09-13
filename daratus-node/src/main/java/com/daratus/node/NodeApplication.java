package com.daratus.node;

import java.util.Scanner;

import com.daratus.node.console.AbstractCommand;
import com.daratus.node.console.CommandFactory;
import com.daratus.node.console.DefaultCommand;
import com.fasterxml.jackson.databind.ObjectMapper;

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
        ObjectMapper mapper = new ObjectMapper();
        NodeContext context = new NodeContext(apiConnector, scrappingConnector, mapper);
        
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
