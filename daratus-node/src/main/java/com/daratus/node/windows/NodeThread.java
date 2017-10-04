package com.daratus.node.windows;

import java.awt.Color;

import javax.swing.JFrame;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.jsoup.helper.W3CDom;

import com.daratus.node.APIHttpConnector;
import com.daratus.node.AuthenticationState;
import com.daratus.node.BlockedState;
import com.daratus.node.NodeContext;
import com.daratus.node.NodeState;
import com.daratus.node.OperationalState;
import com.daratus.node.ScrapingHttpConnector;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NodeThread implements Runnable {

    @Override
    public void run() {

        APIHttpConnector apiConnector = new APIHttpConnector("86.100.97.40", 8080, "http");
        ScrapingHttpConnector scrappingConnector = new ScrapingHttpConnector();
        ObjectMapper mapper = new ObjectMapper();

        W3CDom w3cDom = new W3CDom();

        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();

        NodeState initialState = new AuthenticationState();
        NodeState logedinState = new OperationalState(initialState);
        initialState.setNextState(logedinState);
        NodeState runningState = new BlockedState(initialState, logedinState);
        logedinState.setNextState(runningState);

        NodeContext context = new NodeContext(apiConnector, scrappingConnector, mapper, w3cDom, xPath);
        NodeWindow window = new NodeWindow("Daratus Node v0.0.2", context);

        context.setCurrentState(initialState);

        //JFrame window = new JFrame("Daratus Node v0.0.2");
        
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        window.getContentPane().setBackground(Color.WHITE);

    }

}
