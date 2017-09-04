package com.daratus.node.console;

import com.daratus.node.NodeContext;

public class NodeAPICommand extends APICommand{

    private String name = "";
    
    private NodeContext context;
    
    public NodeAPICommand(String commandToken, String apiPath, NodeContext context) {
        super(commandToken, apiPath, context.getConnector());
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
    public void execute() {
        String jsonResponse = apiConnector.sendGetRequest(apiPath + name);
        if(jsonResponse != null){
            System.out.println("Got response!");
            System.out.println(jsonResponse);
            context.setName(name);
        }
    }



}
