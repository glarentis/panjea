package it.eurotn.panjea.merge.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public interface XmlNode {
    NodeList getChildren();

    Node getElementFromBaseDocument();

    boolean isElaboraChild();

    Document mergeElement(Document baseDocument, Node baseElement);

}
