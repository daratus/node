package com.daratus.node.console;

/**
 * 
 * @author Zilvinas Vaira
 *
 */
public abstract class AbstractParametrizedCommand extends AbstractCommand{
    
    /**
     * 
     * @param commandParameters
     */
    public AbstractParametrizedCommand(String [] commandParameters) {
        super(commandParameters);
    }
    
    /**
     * 
     */
    public void execute() {
        parseParameters(commandParameters);
        doExecute();
    }

    /**
     * 
     * @param commandParameters
     */
    protected abstract void parseParameters(String [] commandParameters);
    
    /**
     * 
     */
    public abstract void doExecute();

}
