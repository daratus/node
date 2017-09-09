package com.daratus.node.console;

import com.daratus.node.RequestMethod;
import com.daratus.node.NodeContext;
import com.daratus.node.domain.Task;
import com.daratus.node.domain.TaskFactory;

public class NextTaskAPICommand extends APICommand{

    private NodeContext context;
    
    private TaskFactory taskFactory;
    
    public NextTaskAPICommand(String commandToken, String apiPath, NodeContext context) {
        super(commandToken, apiPath, context.getAPIConnector());
        this.taskFactory = context.getFactory();
        this.context = context;
    }

    @Override
    public void parseParameters(String[] commandParameters) {
        // Do nothing
    }
    
    @Override
    public void execute() {
        if(context.isLoggedin()){
            String jsonResponse = apiConnector.sendRequest(apiPath + context.getName(), RequestMethod.GET);
            if(jsonResponse != null){
                Task task = taskFactory.createTaskFromJson(jsonResponse);
                
                System.out.println("Got a task:");
                System.out.println(task.getClass().getSimpleName());
                System.out.println(task);

                context.setCurrentTask(task);
            }
        }else{
            System.out.println("Must login first!");
        }
    }

}
