package com.daratus.node.console;

import com.daratus.node.APIConnector;
import com.daratus.node.domain.Task;
import com.daratus.node.domain.TaskFactory;

public class APICommand extends AbstractCommand{

    public static final String NEXT_TASK_PATH = "/data-contract/next-task/";

    public static final String NODE_LOGIN_PATH = "/node/login/";

    public static final String NODE_REGISTER_PATH = "/node/register/";
    
    private APIConnector apiConnector;
    
    private String apiPath = "";
    
    public APICommand(String commandToken, String apiPath, APIConnector apiConnector) {
        super(commandToken);
        this.apiPath = apiPath;
        this.apiConnector = apiConnector;
    }

    @Override
    public void parseParameters(String[] commandParameters) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void execute() {
        String jsonResponse = apiConnector.sendGetRequest(apiPath);
        TaskFactory taskFactory = new TaskFactory();
        Task task = taskFactory.createTaskFromJson(jsonResponse);
        System.out.println("Got a task:");
        System.out.println(task.getClass().getSimpleName());
        System.out.println(task);

    }

}
