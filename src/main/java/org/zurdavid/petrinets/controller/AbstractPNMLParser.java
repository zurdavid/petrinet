package org.zurdavid.petrinets.controller;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Iterator;

public abstract class AbstractPNMLParser {
    private final File pnmFile;
    private XMLEventReader xmlParser = null;
    private String lastId = null;
    private boolean isInitialMarking = false;
    private boolean isName = false;
    private boolean isText = false;


    public AbstractPNMLParser(File pnml) {
        this.pnmFile = pnml;
    }

    public final void initParser() {
        try {
            InputStream fileInputStream = new FileInputStream(this.pnmFile);
            XMLInputFactory factory = XMLInputFactory.newInstance();

            try {
                this.xmlParser = factory.createXMLEventReader(fileInputStream);
            } catch (XMLStreamException e) {
                System.err.println("XML processing error: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found! " + e.getMessage());
        }

    }

    public final void parse() {
        while(this.xmlParser.hasNext()) {
            try {
                XMLEvent event = this.xmlParser.nextEvent();
                switch(event.getEventType()) {
                    case 1:
                        this.handleStartEvent(event);
                        break;
                    case 2:
                        String name = event.asEndElement().getName().toString().toLowerCase();
                        if (name.equals("initialmarking")) {
                            this.isInitialMarking = false;
                        } else if (name.equals("name")) {
                            this.isName = false;
                        } else if (name.equals("text")) {
                            this.isText = false;
                        }
                    case 3:
                    case 5:
                    case 6:
                    case 7:
                    default:
                        break;
                    case 4:
                        if (!this.isText || this.lastId == null) {
                            break;
                        }

                        Characters ch = event.asCharacters();
                        if (!ch.isWhiteSpace()) {
                            this.handleText(ch.getData());
                        }
                        break;
                    case 8:
                        this.xmlParser.close();
                }
            } catch (XMLStreamException var4) {
                System.err.println("Error occurred while parsing PNML document. " + var4.getMessage());
                var4.printStackTrace();
            }
        }

    }

    private void handleStartEvent(XMLEvent event) {
        StartElement element = event.asStartElement();
        String elementName = element.getName().toString().toLowerCase();
        switch (elementName) {
            case "transition":
                handleTransition(element); break;
            case "place":
                handlePlace(element); break;
            case "arc":
                handleArc(element); break;
            case "name":
                isName = true; break;
            case "position":
                handlePosition(element); break;
            case "initialmarking":
                isInitialMarking = true; break;
            case "text":
                isText = true; break;
        }
    }

    private void handleText(String text) {
        if (this.isName) {
            this.setName(this.lastId, text);
        } else if (this.isInitialMarking) {
            this.setTokens(this.lastId, text);
        }

    }

    private void handlePosition(StartElement element) {
        String x = null;
        String y = null;
        Iterator<Attribute> attributes = element.getAttributes();

        while(attributes.hasNext()) {
            Attribute attr = attributes.next();
            String axis = attr.getName().toString().toLowerCase();
            if (axis.equals("x")) {
                x = attr.getValue();
            } else if (axis.equals("y")) {
                y = attr.getValue();
            }
        }

        if (x != null && y != null && this.lastId != null) {
            this.setPosition(this.lastId, x, y);
        } else if (x == null || y == null) {
            System.err.println("Discarded incomplete position!");
        }

    }

    private void handleTransition(StartElement element) {
        String transitionId = null;
        Iterator<Attribute> attributes = element.getAttributes();

        while(attributes.hasNext()) {
            Attribute attr = attributes.next();
            String attrName = attr.getName().toString().toLowerCase();

            if (attrName.equals("id")) {
                transitionId = attr.getValue();
                break;
            }
        }

        if (transitionId != null) {
            this.newTransition(transitionId);
            this.lastId = transitionId;
        } else {
            System.err.println("Discarded transition missing id!");
            this.lastId = null;
        }

    }

    private void handlePlace(StartElement element) {
        String placeId = null;
        Iterator attributes = element.getAttributes();

        while(attributes.hasNext()) {
            Attribute attr = (Attribute)attributes.next();
            String attrName = attr.getName().toString().toLowerCase();

            if (attrName.equals("id")) {
                placeId = attr.getValue();
                break;
            }
        }

        if (placeId != null) {
            this.newPlace(placeId);
            this.lastId = placeId;
        } else {
            System.err.println("Discarded place missing id!");
            this.lastId = null;
        }

    }

    private void handleArc(StartElement element) {
        String arcId = null;
        String source = null;
        String target = null;
        Iterator attributes = element.getAttributes();

        while(attributes.hasNext()) {
            Attribute attr = (Attribute)attributes.next();
            String attrName = attr.getName().toString().toLowerCase();
            if (attrName.equals("id")) {
                arcId = attr.getValue();
            } else if (attrName.equals("source")) {
                source = attr.getValue();
            } else if (attrName.equals("target")) {
                target = attr.getValue();
            }
        }

        if (arcId != null && source != null && target != null) {
            this.newArc(arcId, source, target);
        } else {
            System.err.println("Incomplete edge was discarded!");
        }

        this.lastId = null;
    }

    abstract protected void newTransition(String id);

    abstract public void newPlace(String id);

    abstract public void newArc(String id, String source, String target);

    abstract public void setPosition(String id, String x, String y);

    abstract public void setName(String id, String name);

    abstract public void setTokens(String id, String tokens);
}
