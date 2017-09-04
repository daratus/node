package com.daratus.node.console;

import com.daratus.node.NodeApplication;
import com.daratus.node.domain.Task;
import com.daratus.node.domain.TaskFactory;

public class NextTaskAPICommand extends APICommand{

    private NodeApplication application;
    
    private TaskFactory taskFactory;
    
    public NextTaskAPICommand(String commandToken, String apiPath, NodeApplication application) {
        super(commandToken, apiPath, application.getConnector());
        this.taskFactory = application.getFactory();
        this.application = application;
    }

    @Override
    public void parseParameters(String[] commandParameters) {
        // Do nothing
    }
    
    @Override
    public void execute() {
        if(application.isLoggedin()){
            String jsonResponse = apiConnector.sendGetRequest(apiPath + application.getName());
            if(jsonResponse != null){
                Task task = taskFactory.createTaskFromJson(jsonResponse);
                
                System.out.println("Got a task:");
                System.out.println(task.getClass().getSimpleName());
                System.out.println(task);

                application.setCurrentTask(task);
            }
        }else{
            System.out.println("Must login first!");
        }
    }

}
