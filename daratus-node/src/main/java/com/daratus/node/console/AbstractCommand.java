package com.daratus.node.console;

/**
 * 
 * @author Zilvinas Vaira
 *
 */
public abstract class AbstractCommand implements Command{

    /**
     * 
     */
    protected String [] commandParameters = null;

    /**
     * 
     * @param commandParameters
     */
    public AbstractCommand(String [] commandParameters) {
        setParameters(commandParameters);
    }

    public void setParameters(String [] commandParameters){
        this.commandParameters = commandParameters;
    }
    
    public boolean evaluate(String commandToken){
        if(commandParameters.length > 0){
            return commandToken.equals(commandParameters[0]);
        }else{
            return false;
        }
    }

}
