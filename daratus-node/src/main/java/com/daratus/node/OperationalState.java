package com.daratus.node;

/**
 * 
 * @author Zilvinas Vaira
 *
 */
public class OperationalState extends NodeState{

    private NodeState initialState;
    
    private Thread contextThread = null;
    
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
                    // TODO Auto-generated catch block
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
                System.out.println("Previous task is not finished yet! Aborting ...");
            }
        }
    }

}
