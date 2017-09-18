package com.daratus.node.console;

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
    }

    @Override
    public void parseParameters(String[] commandParameters) {
        if(commandParameters.length > 1){
            name = commandParameters[1];
        }else{
            System.out.println("Some of the required parameters are missing!");
        }
    }
    
    @Override
    public void doExecute() {
        context.authenticate(apiPath, name);
    }

}
