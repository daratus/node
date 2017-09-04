package com.daratus.node.console;

import com.daratus.node.NodeApplication;

public class NodeAPICommand extends APICommand{

    private String name = "";
    
    private NodeApplication application;
    
    public NodeAPICommand(String commandToken, String apiPath, NodeApplication application) {
        super(commandToken, apiPath, application.getConnector());
        this.application = application;
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
            application.setName(name);
        }
    }



}
