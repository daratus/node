package com.daratus.node.console;

import com.daratus.node.NodeContext;

public class GetReferralCommand extends AbstractCommand implements APICommand{

    private String apiPath;
    
    private NodeContext context;
    
    public GetReferralCommand(String[] commandParameters, String apiPath, NodeContext context) {
        super(commandParameters);
        this.apiPath = apiPath;
        this.context = context;
    }

    @Override
    public void execute() {
        context.getReferralLink(apiPath);
    }
    
}
