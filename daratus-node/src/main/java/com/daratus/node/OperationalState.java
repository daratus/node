package com.daratus.node;

import java.util.EnumSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * 
 * @author Zilvinas Vaira
 *
 */
public class OperationalState extends NodeState{

    private NodeState initialState;
    
    private Thread contextThread = null;
    
    private Set<NodeCommand> enabledCommands = EnumSet.of(
            NodeCommand.EXIT, 
            NodeCommand.HELP, 
            NodeCommand.LOGOUT,
            NodeCommand.NEXT,
            NodeCommand.EXECUTE,
            NodeCommand.START,
            NodeCommand.GET_REFERRAL_LINK
    );
    
    public OperationalState(NodeState initialState) {
        this.initialState = initialState;
    }
    
    public void getNextTask(String apiPath, NodeContext context){
        context.getNextTask(apiPath);
    }

    public void executeCurrentTask(NodeContext context){
        context.executeCurrentTask();
    }
    
    private boolean prepareThread(NodeContext context){
        if(contextThread!=null){
            if(contextThread.isAlive()){
                return false;
            }else{
                try {
                    contextThread.join();
                    contextThread = new Thread(context);
                    return true;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }else{
            contextThread = new Thread(context);
            return true;
        }
    }
    
    @Override
    public void handle(NodeContext context) {
        if(!context.isAuthenticated()){
            context.setCurrentState(initialState);
        }else if(context.isBlocked() && isNextState()){
            if(prepareThread(context)){
                context.setCurrentState(nextState);
                contextThread.start();
            }else{
                Logger logger = context.getLogger(this.getClass().getSimpleName());
                logger.warning("Previous automatic execution is not yet stopped! Please wait while it ends. Aborting ...");
                context.setBlocked(false);
            }
        }
    }
    
    @Override
    public String getGreeting(NodeContext context) {
        return "Node '"  + context.getNode().getShortCode() + "' (" + context.getNode().getId() +")! Node is ready for the commands!";
    }

    @Override
    public Set<NodeCommand> getEnabledCommands(){
        return enabledCommands;
    }

}
