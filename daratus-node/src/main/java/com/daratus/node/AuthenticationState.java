package com.daratus.node;

/**
 * 
 * @author Zilvinas Vaira
 *
 */
public class AuthenticationState extends NodeState{

    @Override
    public void handle(NodeContext context) {
        context.setRunning(false);
        if(context.isAuthenticated() && isNextState()){
            context.setCurrentState(nextState);
        }
    }

}
