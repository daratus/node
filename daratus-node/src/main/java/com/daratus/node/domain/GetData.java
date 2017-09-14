package com.daratus.node.domain;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;

import com.daratus.node.ScrapingConnector;

public class GetData extends Task {
    
    private Random randomizer = new Random();

    @Override
    public void execute(ScrapingConnector connector){
        String htmlResponse = connector.scrape(targetURL);
        Iterator<Entry<String, String>> iterator = data.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<String, String> entry = iterator.next();
            data.put(entry.getKey(), entry.getKey().substring(0, 1) + randomizer.nextInt());
        }
        
        System.out.println("Got HTML response!");
        System.out.println(htmlResponse!=null);
    }

}
