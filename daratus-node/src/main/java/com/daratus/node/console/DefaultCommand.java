package com.daratus.node.console;

public class DefaultCommand extends AbstractCommand {

    public DefaultCommand() {
        super(null);
    }

    public DefaultCommand(String [] commandParameters) {
        super(commandParameters);
    }
    
    public DefaultCommand(String commandToken) {
        super(new String[]{commandToken});
    }

    public void execute() {
        if(evaluate(AbstractCommand.HELP)){
            System.out.println("### Daratus Node v1.0 ###");
            System.out.println("#");
            System.out.println("# Available commands:");
            System.out.println("# " + AbstractCommand.REGISTER + " <name> - registers new node, name must be unique.");
            System.out.println("# " + AbstractCommand.LOGIN + " <id> - logins with existing node.");
            System.out.println("# " + AbstractCommand.LOGOUT + " - logouts current node.");
            System.out.println("# " + AbstractCommand.HELP + " - prints title and available commands.");
            System.out.println("# " + AbstractCommand.EXIT + " - quits the program.");
            System.out.println("# Manual execution commands:");
            System.out.println("# " + AbstractCommand.NEXT + " - receives a task from the server.");
            System.out.println("# " + AbstractCommand.EXECUTE + " - executes a task if there is one.");
            System.out.println("# Automatic execution commands:");
            System.out.println("# " + AbstractCommand.START + " starts automatic tasks execution.");
            System.out.println("# " + AbstractCommand.STOP + " stops automatic tasks execution.");
        }else if(!evaluate(AbstractCommand.EXIT)){
            System.out.println("Unrecognized command!");
        }
    }

}
