package com.daratus.node.console;

import java.util.logging.Logger;

import org.apache.http.HttpHost;

import com.daratus.node.APIConnector;
import com.daratus.node.NodeContext;
import com.daratus.node.NodeMessenger;

/**
 * 
 * @author Zilvinas Vaira
 *
 */
public class HostCommand extends AbstractParametrizedCommand {

    private final APIConnector apiConnector;

    private final Logger logger;
    
    private final NodeMessenger messenger;
    
    private String host = "";
    
    private Integer port = null;
    
    private String scheme = "";
    
    
    public HostCommand(String[] commandParameters, NodeContext context) {
        super(commandParameters);
        apiConnector = context.getAPIConnector();
        logger = context.getLogger(this.getClass().getSimpleName());
        messenger = context.getMessenger();
    }

    @Override
    protected boolean parseParameters(String[] commandParameters) {
        boolean hasRequiredParameters = commandParameters.length > 3;
        if(hasRequiredParameters){
            host = commandParameters[1];
            port = Integer.valueOf(commandParameters[2]);
            scheme = commandParameters[3];
        }else{
            String commandToken = commandParameters.length > 0 ? commandParameters[0] : "unknown";
            logger.warning("One or more required parameters are missing for command '" + commandToken + "'!");
        }
        return hasRequiredParameters;
    }

    @Override
    public void doExecute() {
        apiConnector.setHostDetails(host, port, scheme);
        HttpHost host = apiConnector.getHost();
        messenger.info("Host has been updated to '"+host.toURI()+"'!");
    }

}
