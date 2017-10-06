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

    private APIConnector apiConnector;
    
    private String host = "";
    
    private Integer port = null;
    
    private String scheme = "";
    
    private boolean hasRequiredParameters = false;

    private Logger logger;
    
    private NodeMessenger messenger;
    
    public HostCommand(String[] commandParameters, NodeContext context) {
        super(commandParameters);
        apiConnector = context.getAPIConnector();
        logger = context.getLogger(this.getClass().getSimpleName());
        context.getMessenger();
    }

    @Override
    protected void parseParameters(String[] commandParameters) {
        hasRequiredParameters = commandParameters.length > 3;
        if(hasRequiredParameters){
            host = commandParameters[1];
            port = Integer.valueOf(commandParameters[2]);
            scheme = commandParameters[3];
        }else{
            String commandToken = commandParameters.length > 0 ? commandParameters[0] : "unknown";
            logger.warning("One or more required parameters are missing for command '" + commandToken + "'!");
        }
    }

    @Override
    public void doExecute() {
        if(hasRequiredParameters){
            apiConnector.setHostDetails(host, port, scheme);
            HttpHost host = apiConnector.getHost();
            messenger.info("Host has been updated to '"+host.toURI()+"'!");
        }
    }

}
