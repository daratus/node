package com.daratus.node;

import java.util.EnumSet;
import java.util.Set;
import java.util.logging.Logger;

import com.daratus.node.console.AbstractCommand;

/**
 * 
 * @author Zilvinas Vaira
 *
 */
public class BlockedState extends NodeState{

    private NodeState initialState;

    private NodeState logedinState;
    
    private Set<NodeCommand> enabledCommands = EnumSet.of( 
            NodeCommand.EXIT, 
            NodeCommand.HELP, 
            NodeCommand.STOP
    );
    
    public BlockedState(NodeState initialState, NodeState logedinState) {
        this.initialState = initialState;
        this.logedinState = logedinState;

    }
    
    @Override
    public void getNextTask(String apiPath, NodeContext context) {
        Logger logger = context.getLogger(this.getClass().getSimpleName());
        logger.warning(getGreeting(context) + " Can not get next task manually!");
    }

    @Override
    public void executeCurrentTask(NodeContext context) {
        Logger logger = context.getLogger(this.getClass().getSimpleName());
        logger.warning(getGreeting(context) + " Can execute task manually!");
    }
    
    @Override
    public void handle(NodeContext context) {
        if(!context.isAuthenticated()){
            context.setCurrentState(initialState);
            context.setBlocked(false);
        }else if(!context.isBlocked()){
            context.setCurrentState(logedinState);
        }
    }

    @Override
    public String getGreeting(NodeContext context) {
        return "Node ID is '" + context.getName() + "'! Node is operating automaticaly. In order to break the loop use '"+AbstractCommand.STOP+"' command!";
    }

    @Override
    public Set<NodeCommand> getEnabledCommands() {
        return enabledCommands;
    }

}
