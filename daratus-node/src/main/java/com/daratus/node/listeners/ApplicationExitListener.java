package com.daratus.node.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.daratus.node.windows.NodeWindow;

/**
 * 
 * @author Zilvinas Vaira
 *
 */
public class ApplicationExitListener extends WindowAdapter implements ActionListener {

    private NodeWindow window;

    private boolean isSystemTraySupported;

    public ApplicationExitListener(NodeWindow window, boolean isSystemTraySupported) {
        this.window = window;
        this.isSystemTraySupported = isSystemTraySupported;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        if(isSystemTraySupported){
            window.setVisible(false);
        }else{
            window.exit();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        window.exit();
    }

}
