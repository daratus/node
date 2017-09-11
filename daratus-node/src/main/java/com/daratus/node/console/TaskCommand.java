package com.daratus.node.console;

import com.daratus.node.NodeContext;

public class TaskCommand extends AbstractCommand{
    
    private NodeContext context;
    
    public TaskCommand(String [] commandParameters, NodeContext context) {
        super(commandParameters);
        this.context = context;
    }

    public void execute() {
        context.executeCurrentTask();
    }
    

}
