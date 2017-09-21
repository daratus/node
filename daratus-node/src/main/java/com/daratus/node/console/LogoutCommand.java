package com.daratus.node.console;

import com.daratus.node.NodeContext;

/**
 * 
 * @author Zilvinas Vaira
 *
 */
public class LogoutCommand extends AbstractCommand {

    private NodeContext context;
    
    public LogoutCommand(String[] commandParameters, NodeContext context) {
        super(commandParameters);
        this.context = context;
    }

    public void execute() {
        context.logout();
    }

}
