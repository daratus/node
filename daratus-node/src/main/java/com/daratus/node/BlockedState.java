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

    private NodeState authenticationState;

    private NodeState operationalState;
    
    private Set<NodeCommand> enabledCommands = EnumSet.of( 
            NodeCommand.EXIT, 
            NodeCommand.HELP, 
            NodeCommand.STOP
    );
    
    public BlockedState(NodeState authenticationState, NodeState operationalState) {
        this.authenticationState = authenticationState;
        this.operationalState = operationalState;

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
            context.setCurrentState(authenticationState);
            context.setBlocked(false);
        }else if(!context.isBlocked()){
            context.setCurrentState(operationalState);
        }
    }

    @Override
    public String getGreeting(NodeContext context) {
        return "Node '" + context.getNode().getShortCode() + "' ("+context.getNode().getId()+")! Node is operating automaticaly. In order to break the loop press '"+AbstractCommand.STOP+"' button!";
    }

    @Override
    public Set<NodeCommand> getEnabledCommands() {
        return enabledCommands;
    }

}
