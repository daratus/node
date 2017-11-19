package com.daratus.node.console;

import java.util.logging.Logger;

import com.daratus.node.NodeContext;
import com.daratus.node.domain.Node;

/**
 * 
 * @author Zilvinas Vaira
 *
 */
public class RegisterAPICommand extends AbstractParametrizedCommand {

    private final String apiPath;

    private final Logger logger;
    
    private Node node = null;

    private NodeContext context;

    public RegisterAPICommand(String [] commandParameters, String apiPath, NodeContext context) {
        super(commandParameters);
        this.apiPath = apiPath;
        this.context = context;
        logger = context.getLogger(this.getClass().getSimpleName());
        node = new Node();
    }

    @Override
    public boolean parseParameters(String[] commandParameters) {
        int required = 1;
        int provided = commandParameters.length>0 ? commandParameters.length-1 : 0;
        boolean hasRequiredParameters = commandParameters.length > required;
        if(hasRequiredParameters){
            node.setUserEmail(commandParameters[1]);
        }else{
            String commandToken = commandParameters.length > 0 ? commandParameters[0] : "unknown";
            logger.warning((required-provided) +" of required " + required + " parameters are missing for command '" + commandToken + "'!");
        }
        if(commandParameters.length > required+1){
            node.setEthAddress(commandParameters[2]);
        }
        if(commandParameters.length > required+2){
            node.setReferalCode(commandParameters[3]);
        }
        return hasRequiredParameters;
    }
    
    @Override
    public void doExecute() {
        if (!context.isAuthenticated()) {
            context.registerNode(context.getAPIConnector(), apiPath, node);
        }
    }

}
