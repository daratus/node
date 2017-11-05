package com.daratus.node.console;

import com.daratus.node.NodeContext;

public class LoginCommand extends AbstractCommand implements APICommand {

protected String apiPath = "";
    
    private NodeContext context;
    
    public LoginCommand(String [] commandParameters, String apiPath, NodeContext context) {
        super(commandParameters);
        this.apiPath = apiPath;
        this.context = context;
    }

    @Override
    public void execute() {
        context.authenticate(apiPath);
        if (!context.isAuthenticated()) {
            context.authenticateThroughCmdLine(apiPath);
        }
        //context.authenticate(apiPath);
    }
    
    
}
