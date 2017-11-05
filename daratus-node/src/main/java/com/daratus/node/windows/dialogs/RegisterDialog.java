package com.daratus.node.windows.dialogs;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class RegisterDialog extends JPanel{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    JTextField userEmailTextField;
    JTextField ethAddressTextField;
    JTextField referalCodeTextField;
    
    public RegisterDialog() {
        super();
        
        JLabel label = new JLabel("Email*");
        this.add(label);
        userEmailTextField = new JTextField(10);
        this.add(userEmailTextField);
        
        label = new JLabel("Ethereum address");
        this.add(label);
        ethAddressTextField = new JTextField(25);
        this.add(ethAddressTextField);
        
        label = new JLabel("Referral code");
        this.add(label);
        referalCodeTextField = new JTextField(7);
        this.add(referalCodeTextField);
    }
    
    public int showDialog() {
        return JOptionPane.showConfirmDialog(null,
                this,
                "Registration dialog",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
    }
    
    public String getEnteredUserEmail() {
        return userEmailTextField.getText();
    }
    
    public String getEnteredEthAddress() {
        return ethAddressTextField.getText();
    }
    
    public String getEnteredReferalCode() {
        return referalCodeTextField.getText();
    }
    
}
