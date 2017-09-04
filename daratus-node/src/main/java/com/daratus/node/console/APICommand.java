package com.daratus.node.console;

import com.daratus.node.APIConnector;

public abstract class APICommand extends AbstractCommand{

    public static final String NEXT_TASK_PATH = "/data-contract/next-task/";

    public static final String NODE_LOGIN_PATH = "/node/login/";

    public static final String NODE_REGISTER_PATH = "/node/register/";
    
    protected APIConnector apiConnector;
    
    protected String apiPath = "";

    
    public APICommand(String commandToken, String apiPath, APIConnector apiConnector) {
        super(commandToken);
        this.apiPath = apiPath;
        this.apiConnector = apiConnector;
    }

}
