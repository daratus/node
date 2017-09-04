package com.daratus.node.console;

import com.daratus.node.APIConnector;

public class APICommand extends AbstractCommand{

    public static final String NEXT_TASK_PATH = "/data-contract/next-task/";

    public static final String NODE_LOGIN_PATH = "/node/login/";

    public static final String NODE_REGISTER_PATH = "/node/register/";
    
    private APIConnector apiConnector;
    
    private String apiPath = "";
    
    public APICommand(String commandToken, String apiPath, APIConnector apiConnector) {
        super(commandToken);
        this.apiPath = apiPath;
        this.apiConnector = apiConnector;
    }

    @Override
    public void parseParameters(String[] commandParameters) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void execute() {
        apiConnector.sendRequest(apiPath);
    }

}
