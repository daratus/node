package com.daratus.node.console;

import com.daratus.node.RequestMethod;
import com.daratus.node.APIConnector;
import com.daratus.node.NodeContext;
import com.daratus.node.domain.Task;
import com.daratus.node.domain.TaskFactory;

public class NextTaskAPICommand extends AbstractCommand implements APICommand{

    protected String apiPath = "";
    
    private NodeContext context;
    
    private TaskFactory taskFactory;
    
    public NextTaskAPICommand(String [] commandParameters, String apiPath, NodeContext context) {
        super(commandParameters);
        this.apiPath = apiPath;
        this.context = context;
        this.taskFactory = context.getFactory();
    }

    public void execute() {
        if(context.isLoggedin()){
            APIConnector apiConnector = context.getAPIConnector();
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
