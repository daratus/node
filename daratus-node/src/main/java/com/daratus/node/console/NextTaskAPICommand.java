package com.daratus.node.console;

import java.io.IOException;

import com.daratus.node.APIConnector;
import com.daratus.node.NodeContext;
import com.daratus.node.RequestMethod;
import com.daratus.node.domain.Task;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NextTaskAPICommand extends AbstractCommand implements APICommand{

    protected String apiPath = "";
    
    private NodeContext context;
    
    private ObjectMapper mapper;
    
    public NextTaskAPICommand(String [] commandParameters, String apiPath, NodeContext context) {
        super(commandParameters);
        this.apiPath = apiPath;
        this.context = context;
        this.mapper = context.getMapper();
    }

    public void execute() {
        if(context.isLoggedin()){
            APIConnector apiConnector = context.getAPIConnector();
            String jsonResponse = apiConnector.sendRequest(apiPath + context.getName(), RequestMethod.GET);
            if(jsonResponse != null){
                try {
                    Task task = mapper.readValue(jsonResponse, Task.class);
                    System.out.println("Got a task:");
                    System.out.println(task.getClass().getSimpleName());
                    System.out.println(task);
                    context.setCurrentTask(task);
                } catch (JsonParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (JsonMappingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                //Task task = taskFactory.createTaskFromJson(jsonResponse);
                
            }
        }else{
            System.out.println("Must login first!");
        }
    }

}
