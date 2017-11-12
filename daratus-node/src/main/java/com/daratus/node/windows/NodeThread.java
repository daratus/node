package com.daratus.node.windows;

import java.util.Properties;

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

        APIHttpConnector apiConnector = new APIHttpConnector("mvp.daratus.com", 8080, "http");
        APIHttpConnector webApiConnector = new APIHttpConnector("mvp.daratus.com", 80, "http");

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
        
        NodeContext context = new NodeContext(apiConnector, webApiConnector, scrappingConnector, mapper, w3cDom, xPath);
        Properties properties = context.getProperties();
        String version = properties.getProperty("version");
        String title = properties.getProperty("title");

        NodeWindow window = new NodeWindow(title + " v" + version, context);
        
        context.setCurrentState(initialState);

        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setMinimumSize(window.getPreferredSize());
        window.setVisible(true);

    }

}
