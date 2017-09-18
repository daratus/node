package com.daratus.node;

/**
 * 
 * @author Zilvinas Vaira
 *
 */
public class AuthenticationState extends NodeState{

    @Override
    public void getNextTask(String apiPath, NodeContext context) {
        System.out.println("User is not auhenticated! Can not get next task!");
    }

    @Override
    public void executeCurrentTask(NodeContext context) {
        System.out.println("User is not auhenticated! Can execute task!");
    }
    
    @Override
    public void handle(NodeContext context) {
        context.setRunning(false);
        if(context.isAuthenticated() && isNextState()){
            context.setCurrentState(nextState);
        }
    }


}
