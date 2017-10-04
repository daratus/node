package com.daratus.node.console;

import java.util.Scanner;

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

/**
 * Hello world!
 *
 */
public class ConsoleApplication {
    public static void main(String[] args) {

        APIHttpConnector apiConnector = new APIHttpConnector("mvp.daratus.com", 8080, "http");
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
        context.setCurrentState(initialState);

        Scanner scanner = new Scanner(System.in);
        CommandFactory factory = new CommandFactory(context);
        AbstractCommand command = new DefaultCommand(AbstractCommand.HELP, context);
        command.execute();
        while (!command.evaluate(AbstractCommand.EXIT)) {
            command = factory.createCommand(scanner.nextLine());
            command.execute();
        }
        scanner.close();

    }
}
