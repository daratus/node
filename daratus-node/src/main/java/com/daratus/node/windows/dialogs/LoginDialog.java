package com.daratus.node.windows.dialogs;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LoginDialog extends JPanel{

    /**
     * 
     */
    private static final long serialVersionUID = 1001L;
    
    private JTextField nodeCodeField;
    
    private JTextField nodeSecretKeyField;
    
    public LoginDialog() {
        super();
        JLabel label = new JLabel("Node code");
        this.add(label);
        nodeCodeField = new JTextField(10);
        this.add(nodeCodeField);
        
        label = new JLabel("Node secret key");
        this.add(label);
        nodeSecretKeyField = new JTextField(30);
        this.add(nodeSecretKeyField);
    }
    
    /**
     * @deprecated Something is just not right here.
     * @return
     */
    public int showDialog() {
        return JOptionPane.showConfirmDialog(null,
                this,
                "Login dialog",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
    }
    
    public String getNodeCodeFieldText() {
        return nodeCodeField.getText();
    }
    
    public String getNodeSecretKeyFieldText() {
        return nodeSecretKeyField.getText(); 
    }
    
    
}
