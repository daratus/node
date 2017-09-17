package com.daratus.node.console;

import com.daratus.node.NodeContext;

public class StartStopCommand extends AbstractCommand {

    private NodeContext context;
    
    private boolean isRunnig = false;
    
    public StartStopCommand(String[] commandParameters, NodeContext context, boolean isRunnig) {
        super(commandParameters);
        this.context = context;
        this.isRunnig = isRunnig;
    }

    public void execute() {
        context.setRunning(isRunnig);
    }

}
