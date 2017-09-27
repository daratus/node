package com.daratus.node.domain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.logging.Logger;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.daratus.node.NodeContext;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * 
 * @author Zilvinas Vaira
 *
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
    @Type(value = GetData.class),
    @Type(value = GetDataProxy.class),
    @Type(value = GetUrls.class),
    @Type(value = NullTask.class)
})
public abstract class Task {
    
    private Long id;
    
    protected String targetURL = "";
    
    protected Map<String, String> data = new HashMap<String, String>();
    
    protected List<String> urls = new ArrayList<String>();
    
    protected boolean isCompleted = false;
    
    private List<TaskObserver> taskObservers = new ArrayList<TaskObserver>();
    
    protected Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    @Deprecated
    private Random randomizer = new Random();
    
    public void setTargetURL(String targetURL) {
        this.targetURL = targetURL;
    }
    
    public String getTargetURL() {
        return targetURL;
    }
    
    public void setData(Map<String, String> data) {
        this.data = data;
    }
    
    public Map<String, String> getData() {
        return data;
    }
    
    public void setUrls(List<String> nextUrls) {
        this.urls = nextUrls;
    }
    
    public List<String> getUrls() {
        return urls;
    }
    
    protected void setCompleted(boolean isCompleted){
        this.isCompleted = isCompleted;
        notifyObservers(this);
    }
    
    protected void notifyObservers(Task task){
        Iterator<TaskObserver> iterator = taskObservers.iterator();
        while (iterator.hasNext()) {
            iterator.next().notify(task);
        }
    }
    
    @JsonIgnore
    public boolean isCompleted(){
        return isCompleted;
    }

    public void addTaskObserver(TaskObserver taskObserver){
        taskObservers.add(taskObserver);
    }
    
    public abstract void execute(NodeContext context);
    
    /**
     * 
     * @param htmlResponse
     * @param context
     * @return
     * @throws SAXException
     * @throws IOException
     */
    protected Document parseHtmlDocument(String htmlResponse, NodeContext context){
        W3CDom w3cDom = context.getW3cDom();
        Document htmlDocument = w3cDom.fromJsoup(Jsoup.parse(htmlResponse));
        return htmlDocument;
    }
    
    /**
     * Builds fake urls data without parsing.
     * 
     * @param htmlResponse
     */
    protected void buildUrls(String htmlResponse){
        // Generating fake urls
        ListIterator<String> iterator = urls.listIterator();
        while (iterator.hasNext()) {
            iterator.next();
            iterator.set("http://mvp.daratus.com:8080/template?id=" + randomizer.nextInt());
        }
    }

    /**
     * Builds urls data by parsing HTML DOM object.
     * 
     * @param htmlDocument
     * @throws XPathExpressionException 
     */
    protected boolean buildUrls(Document htmlDocument, NodeContext context){
        XPath xPath = context.getxPath();
        Element documentlElement = htmlDocument.getDocumentElement();
        boolean urlsHaveChanges = false;

        ListIterator<String> iterator = urls.listIterator();
        while (iterator.hasNext()) {
            String urlXpath = iterator.next();
            try {
                NodeList nodes = (NodeList) xPath.evaluate(urlXpath, documentlElement, XPathConstants.NODESET);
                boolean currentUrlHasChanges = false;
                for (int i = 0; i < nodes.getLength(); ++i) {
                    Element element = (Element) nodes.item(i);
                    
                    String hrefAttribute = element.getAttribute("href");
                    if(!hrefAttribute.isEmpty()){
                        iterator.set(hrefAttribute);
                        currentUrlHasChanges = urlsHaveChanges = true;
                        break;
                    }
                }
                if(!currentUrlHasChanges){
                    iterator.remove();
                }
            } catch (XPathExpressionException e) {
                logger.warning("Incorect XPath expression!");
            }
        }
        if(!urlsHaveChanges){
            logger.warning("No urls have been scraped!");
        }
        return urlsHaveChanges;
    }
    
    /**
     * Builds fake elements data without parsing.
     * 
     * @param htmlResponse
     */
    protected void buildData(String htmlResponse){
        // Generating fake data
        Iterator<Entry<String, String>> dataIterator = data.entrySet().iterator();
        while (dataIterator.hasNext()) {
            Entry<String, String> entry = dataIterator.next();
            entry.setValue(entry.getKey().substring(0, 1) + randomizer.nextInt());
        }
    }

    /**
     * Builds urls data by parsing HTML DOM object.
     * 
     * @param htmlDocument
     * @throws XPathExpressionException 
     */
    protected boolean buildData(Document htmlDocument, NodeContext context){
        XPath xPath = context.getxPath();
        Element documentlElement = htmlDocument.getDocumentElement();
        boolean dataHasChanges = false;
        
        Iterator<Entry<String, String>> dataIterator = data.entrySet().iterator();
        while (dataIterator.hasNext()) {
            Entry<String, String> entry = dataIterator.next();
            String dataXpath = entry.getValue();
            try {
                NodeList nodes = (NodeList) xPath.evaluate(dataXpath, documentlElement, XPathConstants.NODESET);
                boolean currentEntryHasChanges = false;
                for (int i = 0; i < nodes.getLength(); ++i) {
                    Element element = (Element) nodes.item(i);
                    String elementTextContent = element.getTextContent();
                    if(elementTextContent.isEmpty()){
                        String altAttribute = element.getAttribute("alt");
                        if(!altAttribute.isEmpty()){
                            entry.setValue(altAttribute);
                            currentEntryHasChanges = dataHasChanges = true;
                            break;
                        }
                    }else{
                        entry.setValue(elementTextContent);
                        currentEntryHasChanges = dataHasChanges = true;
                        break;
                    }
                }
                if(!currentEntryHasChanges){
                    dataIterator.remove();
                }
            } catch (XPathExpressionException e) {
                logger.warning("Incorect XPath expression!");
            }
        }
        if(!dataHasChanges){
            logger.warning("No data has been scraped!");
        }
        return dataHasChanges;
    }
    
    @Override
    public String toString() {
        return targetURL;
    }

}
