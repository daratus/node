package com.daratus.node.console;

import java.util.Properties;

import org.apache.http.HttpHost;

import com.daratus.node.APIConnector;
import com.daratus.node.NodeContext;
import com.daratus.node.NodeMessenger;
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
        NodeMessenger messenger = context.getMessenger();
        if(evaluate(AbstractCommand.HELP)){
            NodeState currentState = context.getCurrentState();
            APIConnector apiConnector = context.getAPIConnector();
            HttpHost host = apiConnector.getHost();
            Properties properties = context.getProperties();
            String version = properties.getProperty("version");
            String title = properties.getProperty("title");

            messenger.info("### " + title + " v" + version + " ###");
            messenger.info("# Current node state: " + currentState.getGreeting(context));
            messenger.info("# Current host details: " + host.toURI());
            messenger.info("#");
            messenger.info("# Available commands:");
            messenger.info("# " + AbstractCommand.REGISTER + " <name> - registers new node, name must be unique.");
            messenger.info("# " + AbstractCommand.LOGIN + " <id> - logins with existing node.");
            messenger.info("# " + AbstractCommand.LOGOUT + " - logouts current node.");
            messenger.info("# " + AbstractCommand.HOST + " <host> <port> <scheme> - updates Daratus API host details.");
            messenger.info("# " + AbstractCommand.HELP + " - prints title and available commands.");
            messenger.info("# " + AbstractCommand.EXIT + " - quits the program.");
            messenger.info("#");
            messenger.info("# Manual execution commands:");
            messenger.info("# " + AbstractCommand.NEXT + " - receives a task from the server.");
            messenger.info("# " + AbstractCommand.EXECUTE + " - executes a task if there is one.");
            messenger.info("#");
            messenger.info("# Automatic execution commands:");
            messenger.info("# " + AbstractCommand.START + " starts automatic tasks execution.");
            messenger.info("# " + AbstractCommand.STOP + " stops automatic tasks execution.");
        }else if(evaluate(AbstractCommand.EXIT)){
            context.exit();
        }else{
            messenger.warning("Unrecognized command!");
        }
    }

}
