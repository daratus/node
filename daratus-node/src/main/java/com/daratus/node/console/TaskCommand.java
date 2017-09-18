package com.daratus.node.console;

import com.daratus.node.NodeContext;
import com.daratus.node.NodeState;

public class TaskCommand extends AbstractCommand{
    
    private NodeContext context;
    
    public TaskCommand(String [] commandParameters, NodeContext context) {
        super(commandParameters);
        this.context = context;
    }

    public void execute() {
        NodeState state = context.getCurrentState();
        state.executeCurrentTask(context);
    }
    

}
