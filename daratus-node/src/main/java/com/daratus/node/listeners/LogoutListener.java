package com.daratus.node.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.daratus.node.NodeContext;

public class LogoutListener implements ActionListener {

    private NodeContext context;
    
    public LogoutListener(NodeContext context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        context.logout();
        context.handleCurrentState();
    }

}
