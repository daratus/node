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
        if(parseParameters(commandParameters)) {
            doExecute();
        }
    }

    /**
     * 
     * @param commandParameters
     * @return
     */
    protected abstract boolean parseParameters(String [] commandParameters);
    
    /**
     * 
     */
    public abstract void doExecute();

}
