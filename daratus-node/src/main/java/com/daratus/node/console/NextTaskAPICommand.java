package com.daratus.node.console;

import com.daratus.node.NodeContext;

public class NextTaskAPICommand extends AbstractCommand implements APICommand{

    protected String apiPath = "";
    
    private NodeContext context;
    
    public NextTaskAPICommand(String [] commandParameters, String apiPath, NodeContext context) {
        super(commandParameters);
        this.apiPath = apiPath;
        this.context = context;
    }

    public void execute() {
        context.getNextTask(apiPath);
    }

}
