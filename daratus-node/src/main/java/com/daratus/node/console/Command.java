package com.daratus.node.console;

public interface Command {
    
    public static final String EXIT = "exit";
    
    public static final String HELP = "help";
    
    public static final String LOGIN = "login";
    
    public static final String REGISTER = "register";
    
    public static final String NEXT = "next";
    
    public static final String EXECUTE = "execute";
    
    public void setParameters(String [] commandParameters);
    
    public boolean evaluate(String commandToken);

    public void execute();
}
