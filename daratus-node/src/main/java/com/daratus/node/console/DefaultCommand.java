package com.daratus.node.console;

public class DefaultCommand extends AbstractCommand {

    public DefaultCommand() {
        super("");
    }

    public DefaultCommand(String commandToken) {
        super(commandToken);
    }

    @Override
    public void parseParameters(String[] commandParameters) {
        // Do nothing
    }
    
    @Override
    public void execute() {
        if(evaluate(AbstractCommand.HELP)){
            System.out.println("## Daratus Node v1.0 ##");
            System.out.println("#");
            System.out.println("# Available commands:");
            System.out.println("# " + AbstractCommand.REGISTER + " <name> - registers new node, name must be unique.");
            System.out.println("# " + AbstractCommand.LOGIN + " <name> - logins with existing node.");
            System.out.println("# " + AbstractCommand.NEXT + " - receives a task from the server.");
            System.out.println("# " + AbstractCommand.EXECUTE + " - executes a task if there is one.");
            System.out.println("# " + AbstractCommand.HELP + " - prints title and available commands.");
            System.out.println("# " + AbstractCommand.EXIT + " - quits the program.");
        }else if(!evaluate(AbstractCommand.EXIT)){
            System.out.println("Unrecognized command!");
        }
    }

}
