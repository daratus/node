package com.daratus.node.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import com.daratus.node.NodeContext;
import com.daratus.node.domain.Node;
import com.daratus.node.windows.dialogs.LoginDialogPanel;

/**
 * 
 * @author Zilvinas Vaira
 *
 */
public class LoginListener implements ActionListener {

    private String apiPath = "";
    
    private NodeContext context;
    
    public LoginListener(String apiPath, NodeContext context) {
        this.context = context;
        this.apiPath = apiPath;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Node node = context.createNodeFromFile();
        
        // First try to login with node details from file
        if(node != null){
            context.authenticate(apiPath, node);
        }
        // Then if not yet logged in ask user for details
        if (!context.isAuthenticated()) {
            LoginDialogPanel loginDialog = new LoginDialogPanel();
            int result = loginDialog.showDialog();
            if (result == JOptionPane.OK_OPTION) {
                if (!loginDialog.getNodeCodeFieldText().isEmpty() 
                        && !loginDialog.getNodeSecretKeyFieldText().isEmpty()) {
                    node = new Node();
                    node.setSecretKey(loginDialog.getNodeSecretKeyFieldText());
                    node.setShortCode(loginDialog.getNodeCodeFieldText());
                    context.authenticate(apiPath, node);
                }
            }   
        }
    }

}
