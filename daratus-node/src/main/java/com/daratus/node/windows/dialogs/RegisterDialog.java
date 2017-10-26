package com.daratus.node.windows.dialogs;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class RegisterDialog extends JPanel{

    JTextField userNameTextField;
    JTextField userEmailTextField;
    JTextField nodeNameTextField;
    
    public RegisterDialog() {
        super();
        
        JLabel label = new JLabel("Your name");
        this.add(label);
        userNameTextField = new JTextField(10);
        this.add(userNameTextField);
        
        label = new JLabel("Your email");
        this.add(label);
        userEmailTextField = new JTextField(10);
        this.add(userEmailTextField);
        
        label = new JLabel("Node name");
        this.add(label);
        nodeNameTextField = new JTextField(10);
        this.add(nodeNameTextField);
    }
    
    public int showDialog() {
        return JOptionPane.showConfirmDialog(null,
                this,
                "Registration dialog",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
    }
    
    public String getEnteredUserName() {
        return userNameTextField.getText();
    }
    
    public String getEnteredUserEmail() {
        return userEmailTextField.getText();
    }
    
    public String getEnteredNodeName() {
        return nodeNameTextField.getText();
    }
    
}
