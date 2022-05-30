//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.zurdavid.petrinets.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Iterator;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class PNMLWopedParser {
    private File pnmlDatei;
    private XMLEventReader xmlParser = null;
    private String lastId = null;
    private boolean isInitialMarking = false;
    private boolean isName = false;
    private boolean isText = false;

    public static void main(String[] args) {
        if (args.length > 0) {
            File pnmlDatei = new File(args[0]);
            if (pnmlDatei.exists()) {
                PNMLWopedParser pnmlParser = new PNMLWopedParser(pnmlDatei);
                pnmlParser.initParser();
                pnmlParser.parse();
            } else {
                System.err.println("Die Datei " + pnmlDatei.getAbsolutePath() + " wurde nicht gefunden!");
            }
        } else {
            System.out.println("Bitte eine Datei als Parameter angeben!");
        }

    }

    public PNMLWopedParser(File pnml) {
        this.pnmlDatei = pnml;
    }

    public final void initParser() {
        try {
            InputStream dateiEingabeStrom = new FileInputStream(this.pnmlDatei);
            XMLInputFactory factory = XMLInputFactory.newInstance();

            try {
                this.xmlParser = factory.createXMLEventReader(dateiEingabeStrom);
            } catch (XMLStreamException var4) {
                System.err.println("XML Verarbeitungsfehler: " + var4.getMessage());
                var4.printStackTrace();
            }
        } catch (FileNotFoundException var5) {
            System.err.println("Die Datei wurde nicht gefunden! " + var5.getMessage());
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
                System.err.println("Fehler beim Parsen des PNML Dokuments. " + var4.getMessage());
                var4.printStackTrace();
            }
        }

    }

    private void handleStartEvent(XMLEvent event) {
        StartElement element = event.asStartElement();
        if (element.getName().toString().toLowerCase().equals("transition")) {
            this.handleTransition(element);
        } else if (element.getName().toString().toLowerCase().equals("place")) {
            this.handlePlace(element);
        } else if (element.getName().toString().toLowerCase().equals("arc")) {
            this.handleArc(element);
        } else if (element.getName().toString().toLowerCase().equals("name")) {
            this.isName = true;
        } else if (element.getName().toString().toLowerCase().equals("position")) {
            this.handlePosition(element);
        } else if (element.getName().toString().toLowerCase().equals("initialmarking")) {
            this.isInitialMarking = true;
        } else if (element.getName().toString().toLowerCase().equals("text")) {
            this.isText = true;
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
        Iterator attributes = element.getAttributes();

        while(attributes.hasNext()) {
            Attribute attr = (Attribute)attributes.next();
            if (attr.getName().toString().toLowerCase().equals("x")) {
                x = attr.getValue();
            } else if (attr.getName().toString().toLowerCase().equals("y")) {
                y = attr.getValue();
            }
        }

        if (x != null && y != null && this.lastId != null) {
            this.setPosition(this.lastId, x, y);
        } else if (x == null || y == null || this.lastId != null) {
            System.err.println("Unvollständige Position wurde verworfen!");
        }

    }

    private void handleTransition(StartElement element) {
        String transitionId = null;
        Iterator attributes = element.getAttributes();

        while(attributes.hasNext()) {
            Attribute attr = (Attribute)attributes.next();
            if (attr.getName().toString().toLowerCase().equals("id")) {
                transitionId = attr.getValue();
                break;
            }
        }

        if (transitionId != null) {
            this.newTransition(transitionId);
            this.lastId = transitionId;
        } else {
            System.err.println("Transition ohne id wurde verworfen!");
            this.lastId = null;
        }

    }

    private void handlePlace(StartElement element) {
        String placeId = null;
        Iterator attributes = element.getAttributes();

        while(attributes.hasNext()) {
            Attribute attr = (Attribute)attributes.next();
            if (attr.getName().toString().toLowerCase().equals("id")) {
                placeId = attr.getValue();
                break;
            }
        }

        if (placeId != null) {
            this.newPlace(placeId);
            this.lastId = placeId;
        } else {
            System.err.println("Stelle ohne id wurde verworfen!");
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
            if (attr.getName().toString().toLowerCase().equals("id")) {
                arcId = attr.getValue();
            } else if (attr.getName().toString().toLowerCase().equals("source")) {
                source = attr.getValue();
            } else if (attr.getName().toString().toLowerCase().equals("target")) {
                target = attr.getValue();
            }
        }

        if (arcId != null && source != null && target != null) {
            this.newArc(arcId, source, target);
        } else {
            System.err.println("Unvollständige Kante wurde verworfen!");
        }

        this.lastId = null;
    }

    public void newTransition(String id) {
        System.out.println("Transition mit id " + id + " wurde gefunden.");
    }

    public void newPlace(String id) {
        System.out.println("Stelle mit id " + id + " wurde gefunden.");
    }

    public void newArc(String id, String source, String target) {
        System.out.println("Kante mit id " + id + " von " + source + " nach " + target + " wurde gefunden.");
    }

    public void setPosition(String id, String x, String y) {
        System.out.println("Setze die Position des Elements " + id + " auf (" + x + ", " + y + ")");
    }

    public void setName(String id, String name) {
        System.out.println("Setze den Namen des Elements " + id + " auf " + name);
    }

    public void setTokens(String id, String tokens) {
        System.out.println("Setze die Markenanzahl des Elements " + id + " auf " + tokens);
    }
}