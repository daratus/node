package com.daratus.node.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import com.daratus.node.NodeContext;
import com.daratus.node.domain.Node;
import com.daratus.node.windows.dialogs.LoginDialog;

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
        /*String nodeId = JOptionPane.showInputDialog("Please enter Node ID!");
        
        if(nodeId !=null && !nodeId.isEmpty()){
            context.authenticate(apiPath, nodeId);
        }*/
        //context.authenticate(apiPath);
        context.authenticate(apiPath);
        if (!context.isAuthenticated()) {
            LoginDialog loginDialog = new LoginDialog();
            int result = loginDialog.showDialog();
            if (result == JOptionPane.OK_OPTION) {
                if (!loginDialog.getNodeCodeFieldText().isEmpty() 
                        && !loginDialog.getNodeSecretKeyFieldText().isEmpty()) {
                    Node node = new Node();
                    node.setSecretKey(loginDialog.getNodeSecretKeyFieldText());
                    node.setShortCode(loginDialog.getNodeCodeFieldText());
                    context.authenticate(apiPath, node);
                }
            }   
        }
    }

}
