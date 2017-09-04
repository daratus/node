package com.daratus.node.console;

import com.daratus.node.APIConnector;

public class APICommand extends AbstractCommand{

    public static final String NEXT_TASK_PATH = "/data-contract/next-task/";

    public static final String NODE_LOGIN_PATH = "";

    public static final String NODE_REGISTER_PATH = "";
    
    private APIConnector apiConnector;
    
    public APICommand(String commandToken, APIConnector apiConnector) {
        super(commandToken);
        this.apiConnector = apiConnector;
    }

    @Override
    public void parseParameters(String[] commandParameters) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void execute() {
        // TODO Auto-generated method stub
        
    }

}
