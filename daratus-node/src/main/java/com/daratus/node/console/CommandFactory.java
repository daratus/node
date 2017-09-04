package com.daratus.node.console;

import java.util.HashMap;
import java.util.Map;

import com.daratus.node.APIConnector;

public class CommandFactory {

    private Map<String, AbstractCommand> commandPool = new HashMap<String, AbstractCommand>();
    
    private DefaultCommand unrecognizedCommand = new DefaultCommand();
    
    private APIConnector apiConnector;
    
    public CommandFactory(APIConnector apiConnector) {
        this.apiConnector = apiConnector;
    }
    
    public AbstractCommand createCommand(String commandLine){
        String [] commandParameters = commandLine.split(" ");
        String commandToken = commandParameters[0];
        AbstractCommand command = unrecognizedCommand;
        if( commandPool.containsKey(commandToken)){
            command = commandPool.get(commandToken);
            command.parseParameters(commandParameters);
        }else{
            command = createCommand(commandParameters);
            commandPool.put(commandToken, command);
        }
        return command;
    }
    
    private AbstractCommand createCommand(String [] commandParameters){
        String commandToken = commandParameters[0];
        if(commandToken.equals(AbstractCommand.HELP)){
            return new DefaultCommand(commandToken);
        }else{
            return unrecognizedCommand;
        }
    }
    
}
