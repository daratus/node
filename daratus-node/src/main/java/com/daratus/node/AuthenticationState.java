package com.daratus.node;

import java.util.EnumSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * 
 * @author Zilvinas Vaira
 *
 */
public class AuthenticationState extends NodeState{

    private Set<NodeCommand> enabledCommands = EnumSet.of( 
            NodeCommand.EXIT, 
            NodeCommand.HELP, 
            NodeCommand.HOST, 
            NodeCommand.LOGIN,
            NodeCommand.REGISTER
    );

    @Override
    public void getNextTask(String apiPath, NodeContext context) {
        Logger logger = context.getLogger(this.getClass().getSimpleName());
        logger.warning(getGreeting(context) + " Can not get next task!");
    }

    @Override
    public void executeCurrentTask(NodeContext context) {
        Logger logger = context.getLogger(this.getClass().getSimpleName());
        logger.warning(getGreeting(context) + " Can not execute task!");
    }
    
    @Override
    public void handle(NodeContext context) {
        if(context.isBlocked()){
            context.setBlocked(false);
            Logger logger = context.getLogger(this.getClass().getSimpleName());
            logger.warning(getGreeting(context) + " Can not start/stop task loop!");
        }
        if(context.isAuthenticated() && isNextState()){
            context.setCurrentState(nextState);
        }
    }

    @Override
    public String getGreeting(NodeContext context) {
        return "No user is currently authenticated!";
    }
    
    @Override
    public Set<NodeCommand> getEnabledCommands(){
        return enabledCommands;
    }

}
