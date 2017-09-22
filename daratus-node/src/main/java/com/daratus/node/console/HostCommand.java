package com.daratus.node.console;

import java.util.logging.Logger;

import org.apache.http.HttpHost;

import com.daratus.node.APIConnector;
import com.daratus.node.NodeContext;

/**
 * 
 * @author Zilvinas Vaira
 *
 */
public class HostCommand extends AbstractParametrizedCommand {

    private APIConnector apiConnector;
    
    private String host;
    
    private int port;
    
    private String scheme;

    private Logger logger;
    
    public HostCommand(String[] commandParameters, NodeContext context) {
        super(commandParameters);
        apiConnector = context.getAPIConnector();
        logger = context.getLogger(this.getClass().getSimpleName());
    }

    @Override
    protected void parseParameters(String[] commandParameters) {
        if(commandParameters.length > 3){
            host = commandParameters[1];
            port = Integer.valueOf(commandParameters[2]);
            scheme = commandParameters[3];
            apiConnector.setHostDetails(host, port, scheme);
            HttpHost host = apiConnector.getHost();
            System.out.println("Host has been updated to '"+host.toURI()+"'!");
        }else{
            String commandToken = commandParameters.length > 0 ? commandParameters[0] : "unknown";
            logger.warning("One or more required parameters are missing for command '" + commandToken + "'!");
        }
    }

    @Override
    public void doExecute() {
        // TODO Auto-generated method stub

    }

}
