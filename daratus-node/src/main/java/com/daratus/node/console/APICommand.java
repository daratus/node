package com.daratus.node.console;

import com.daratus.node.APIConnector;

public abstract class APICommand extends AbstractCommand{

    public static final String NEXT_TASK_PATH = "/node/task/";

    public static final String NODE_PATH = "/node/";

    protected APIConnector apiConnector;
    
    protected String apiPath = "";

    
    public APICommand(String commandToken, String apiPath, APIConnector apiConnector) {
        super(commandToken);
        this.apiPath = apiPath;
        this.apiConnector = apiConnector;
    }

}
