package com.daratus.node.listeners;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.daratus.node.windows.NodeWindow;

public class TrayMouseListener extends MouseAdapter {

    private NodeWindow window;
    
    public TrayMouseListener(NodeWindow window) {
        this.window = window;
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1){
            window.setVisible(true);
        }
    }

}
