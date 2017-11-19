package com.daratus.node.console;

import com.daratus.node.NodeContext;
import com.daratus.node.domain.Node;

/**
 * 
 * @author Zilvinas Vaira
 *
 */
public class LoginAPICommand extends AbstractParametrizedCommand {

    private final String apiPath;
    
    private final NodeContext context;
    
    private final Node commandNode;
    
    private final int optional = 2;
    
    public LoginAPICommand(String [] commandParameters, String apiPath, NodeContext context) {
        super(commandParameters);
        this.apiPath = apiPath;
        this.context = context;
        commandNode = new Node();
    }
    
    @Override
    public boolean parseParameters(String[] commandParameters) {
        if(commandParameters.length > optional){
            commandNode.setSecretKey(commandParameters[1]);
            commandNode.setShortCode(commandParameters[2]);
        }
        return true;
    }

    @Override
    public void doExecute() {
        if (!context.isAuthenticated()) {
            if(commandParameters.length > optional){
                // If parameters are set try to login using parameters.
                context.authenticate(apiPath, commandNode);
            }else{
                // Try to login from using file data if parameters are omited.
                Node fileNode = context.createNodeFromFile();
                context.authenticate(apiPath, fileNode);
            }
        }
    }
    
}
