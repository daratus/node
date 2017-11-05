package com.daratus.node.console;

import java.util.logging.Logger;

import com.daratus.node.NodeContext;

/**
 * 
 * @author Zilvinas Vaira
 *
 */
public class NodeAPICommand extends AbstractParametrizedCommand implements APICommand{

    /**
     * 
     */
    private String name = "";
    
    /**
     * 
     */
    private NodeContext context;

    /**
     * 
     */
    protected String apiPath = "";

    protected Logger logger;
    
    /**
     * 
     * @param commandParameters
     * @param apiPath
     * @param context
     */
    public NodeAPICommand(String [] commandParameters, String apiPath, NodeContext context) {
        super(commandParameters);
        this.apiPath = apiPath;
        this.context = context;
        logger = context.getLogger(this.getClass().getSimpleName());
    }

    @Override
    public void parseParameters(String[] commandParameters) {
        if(commandParameters.length > 1){
            name = commandParameters[1];
        }else{
            String commandToken = commandParameters.length > 0 ? commandParameters[0] : "unknown";
            logger.warning("Required parameter is missing for command '" + commandToken + "'!");
        }
    }
    
    @Override
    public void doExecute() {
        if(commandParameters.length > 1){
            //context.authenticate(apiPath, name);
        }
    }

}
