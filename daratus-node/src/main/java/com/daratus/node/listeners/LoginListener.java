package com.daratus.node.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import com.daratus.node.NodeContext;

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
        context.authenticate(apiPath);
    }

}
