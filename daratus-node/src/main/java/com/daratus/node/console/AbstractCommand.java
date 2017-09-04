package com.daratus.node.console;

public abstract class AbstractCommand {

    public static final String EXIT = "exit";
    
    public static final String HELP = "help";
    
    public static final String LOGIN = "login";
    
    public static final String REGISTER = "register";
    
    public static final String NEXT = "next";
    
    public static final String EXECUTE = "execute";
    
    protected String commandToken;

    public AbstractCommand(String commandToken) {
        this.commandToken = commandToken;
    }

    public boolean evaluate(String commandToken){
        return this.commandToken.equals(commandToken);
    }
    
    public abstract void parseParameters(String [] commandParameters);
    
    public abstract void execute();

}
