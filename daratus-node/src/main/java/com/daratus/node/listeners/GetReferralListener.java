package com.daratus.node.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.daratus.node.NodeContext;

public class GetReferralListener implements ActionListener{

    private NodeContext context;
    
    private String apiPath;
    
    public GetReferralListener(String apiPath, NodeContext context) {
        this.context = context;
        this.apiPath = apiPath;
    }
    
    @Override
    public void actionPerformed(ActionEvent arg0) {
        context.getReferralLink(apiPath);
    }

    
    
}
