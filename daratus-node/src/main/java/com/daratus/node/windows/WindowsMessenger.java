package com.daratus.node.windows;

import java.awt.Color;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;

import javax.swing.JLabel;

import com.daratus.node.NodeMessenger;

public class WindowsMessenger implements NodeMessenger{
    
    private NodeWindow window;
    

    public WindowsMessenger(NodeWindow window) {
        this.window = window;
    }

    @Override
    public void info(Object message) {
        JLabel messageLabel = window.getMessageLabel();
        TrayIcon trayIcon = window.getTrayIcon();
        trayIcon.displayMessage("Node", message.toString(), MessageType.INFO);
        messageLabel.setText(message.toString());
        messageLabel.setForeground(Color.BLACK);
    }

    @Override
    public void warning(Object message) {
        JLabel messageLabel = window.getMessageLabel();
        TrayIcon trayIcon = window.getTrayIcon();
        trayIcon.displayMessage("Node", message.toString(), MessageType.WARNING);
        messageLabel.setText(message.toString());
        messageLabel.setForeground(Color.ORANGE);
    }

    @Override
    public void error(Object message) {
        JLabel messageLabel = window.getMessageLabel();
        TrayIcon trayIcon = window.getTrayIcon();
        trayIcon.displayMessage("Node", message.toString(), MessageType.ERROR);
        messageLabel.setText(message.toString());
        messageLabel.setForeground(Color.RED);
    }

}
