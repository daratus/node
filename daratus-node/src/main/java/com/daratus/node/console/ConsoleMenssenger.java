package com.daratus.node.console;

import java.io.PrintStream;

import com.daratus.node.NodeMessenger;

public class ConsoleMenssenger implements NodeMessenger {
    
    private PrintStream out;
    private PrintStream err;
    
    public ConsoleMenssenger(PrintStream out, PrintStream err) {
       this.out = out;
       this.err = err;
    }

    @Override
    public void info(Object message) {
        out.println(message);
    }

    @Override
    public void warning(Object message) {
        err.println(message);
    }

    @Override
    public void error(Object message) {
        err.println(message);
    }

}
