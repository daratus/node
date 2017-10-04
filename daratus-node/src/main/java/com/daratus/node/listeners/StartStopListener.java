package com.daratus.node.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.daratus.node.NodeContext;

public class StartStopListener implements ActionListener {

    private NodeContext context;
    
    private boolean isRunnig = false;
    
    public StartStopListener(NodeContext context, boolean isRunnig) {
        this.context = context;
        this.isRunnig = isRunnig;

    }
    
    @Override
    public void actionPerformed(ActionEvent arg0) {
        context.setBlocked(isRunnig);
        context.handleCurrentState();
    }

}
