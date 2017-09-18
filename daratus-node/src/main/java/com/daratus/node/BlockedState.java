package com.daratus.node;

/**
 * 
 * @author Zilvinas Vaira
 *
 */
public class BlockedState extends NodeState{

    private NodeState initialState;

    private NodeState logedinState;
    
    public BlockedState(NodeState initialState, NodeState logedinState) {
        this.initialState = initialState;
        this.logedinState = logedinState;

    }
    
    @Override
    public void getNextTask(String apiPath, NodeContext context) {
        System.out.println("Tasks are requested automatically! Can not get next task manually!");
    }

    @Override
    public void executeCurrentTask(NodeContext context) {
        System.out.println("Tasks are requested automatically! Can execute task manually!");
    }
    
    @Override
    public void handle(NodeContext context) {
        if(!context.isAuthenticated()){
            context.setCurrentState(initialState);
            context.setRunning(false);
        }else if(!context.isBlocked()){
            context.setCurrentState(logedinState);
        }
    }

}
