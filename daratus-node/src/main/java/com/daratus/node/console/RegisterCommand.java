package com.daratus.node.console;

import java.util.logging.Logger;

import com.daratus.node.NodeContext;
import com.daratus.node.domain.Node;

public class RegisterCommand extends AbstractParametrizedCommand implements APICommand {

    private Node node = null;

    private NodeContext context;

    protected String apiPath = "";

    protected Logger logger;
    
    public RegisterCommand(String [] commandParameters, String apiPath, NodeContext context) {
        super(commandParameters);
        this.apiPath = apiPath;
        this.context = context;
        logger = context.getLogger(this.getClass().getSimpleName());
    }

    @Override
    public void parseParameters(String[] commandParameters) {
        /*if(commandParameters.length > 3){
            node = new Node();
            //node.setUserName(commandParameters[1]);
            node.setUserEmail(commandParameters[2]);
            node.setName(commandParameters[3]);
        }else{
            String commandToken = commandParameters.length > 0 ? commandParameters[0] : "unknown";
            logger.warning("Required parameter is missing for command '" + commandToken + "'!");
        }*/
    }
    
    @Override
    public void doExecute() {
        if (!context.isAuthenticated()) {
            context.registerThroughCmdLie(apiPath);
        }
    }

}
