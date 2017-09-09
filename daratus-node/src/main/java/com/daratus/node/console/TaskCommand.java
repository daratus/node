package com.daratus.node.console;

import com.daratus.node.NodeContext;

public class TaskCommand extends AbstractCommand{
    
    private NodeContext context;
    
    public TaskCommand(String commandToken, NodeContext context) {
        super(commandToken);
        this.context = context;
    }

    @Override
    public void parseParameters(String[] commandParameters) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void execute() {
        context.executeCurrentTask();
    }
    

}
