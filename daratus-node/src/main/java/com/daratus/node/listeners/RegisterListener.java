package com.daratus.node.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import com.daratus.node.NodeContext;
import com.daratus.node.domain.Node;
import com.daratus.node.windows.dialogs.RegisterDialog;

public class RegisterListener implements ActionListener{

    private String apiPath = "";
    
    private NodeContext context;
    
    public RegisterListener(String apiPath, NodeContext context) {
        this.context = context;
        this.apiPath = apiPath;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        RegisterDialog registerDialog = new RegisterDialog();
        int result = registerDialog.showDialog();
        if (result == JOptionPane.OK_OPTION) {
            if (!registerDialog.getEnteredUserEmail().isEmpty() 
                    && !registerDialog.getEnteredUserName().isEmpty() 
                    && !registerDialog.getEnteredNodeName().isEmpty()) {
                Node node = new Node();
                node.setName(registerDialog.getEnteredNodeName());
                node.setUserEmail(registerDialog.getEnteredUserEmail());
                node.setUserName(registerDialog.getEnteredUserName());
                
                context.registerNode(apiPath, node);
            }
        }
    }

}