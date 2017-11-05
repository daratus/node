package com.daratus.node.console;

/**
 * 
 * @author Zilvinas Vaira
 *
 */
public interface Command {
    
    public static final String EXIT = "exit";
    
    public static final String HELP = "help";

    public static final String HOST = "host";
    
    public static final String LOGIN = "login";
    
    public static final String LOGOUT = "logout";
    
    public static final String REGISTER = "register";
    
    public static final String NEXT = "next";

    public static final String START = "start";

    public static final String STOP = "stop";
    
    public static final String EXECUTE = "execute";
    
    public static final String REFERRAL = "referral";
    
    public void setParameters(String [] commandParameters);
    
    public boolean evaluate(String commandToken);

    public void execute();
}
