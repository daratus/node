package com.daratus.node.console;

import org.apache.http.HttpHost;

import com.daratus.node.APIConnector;
import com.daratus.node.NodeContext;
import com.daratus.node.NodeState;

public class DefaultCommand extends AbstractCommand {

    private NodeContext context;
    
    public DefaultCommand(NodeContext context) {
        this(new String[] {}, context);
    }

    public DefaultCommand(String [] commandParameters, NodeContext context) {
        super(commandParameters);
        this.context = context;
    }
    
    public DefaultCommand(String commandToken, NodeContext context) {
        this(new String[]{commandToken}, context);
    }

    public void execute() {
        if(evaluate(AbstractCommand.HELP)){
            NodeState currentState = context.getCurrentState();
            APIConnector apiConnector = context.getAPIConnector();
            HttpHost host = apiConnector.getHost();
            System.out.println("### Daratus Node v0.0.2 ###");
            System.out.println("# Current node state: " + currentState.getGreeting(context));
            System.out.println("# Current host details: " + host.toURI());
            System.out.println("#");
            System.out.println("# Available commands:");
            System.out.println("# " + AbstractCommand.REGISTER + " <name> - registers new node, name must be unique.");
            System.out.println("# " + AbstractCommand.LOGIN + " <id> - logins with existing node.");
            System.out.println("# " + AbstractCommand.LOGOUT + " - logouts current node.");
            System.out.println("# " + AbstractCommand.HOST + " <host> <port> <scheme> - updates Daratus API host details.");
            System.out.println("# " + AbstractCommand.HELP + " - prints title and available commands.");
            System.out.println("# " + AbstractCommand.EXIT + " - quits the program.");
            System.out.println("#");
            System.out.println("# Manual execution commands:");
            System.out.println("# " + AbstractCommand.NEXT + " - receives a task from the server.");
            System.out.println("# " + AbstractCommand.EXECUTE + " - executes a task if there is one.");
            System.out.println("#");
            System.out.println("# Automatic execution commands:");
            System.out.println("# " + AbstractCommand.START + " starts automatic tasks execution.");
            System.out.println("# " + AbstractCommand.STOP + " stops automatic tasks execution.");
        }else if(evaluate(AbstractCommand.EXIT)){
            System.out.println("Bye Bye...!");
        }else{
            System.out.println("Unrecognized command!");
        }
    }

}
