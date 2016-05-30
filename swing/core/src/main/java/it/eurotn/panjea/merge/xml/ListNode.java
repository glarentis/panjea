package it.eurotn.panjea.merge.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ListNode implements XmlNode {

    private Node pluginElement;

    public ListNode(final Node pluginElement) {
        this.pluginElement = pluginElement;
    }

    @Override
    public NodeList getChildren() {
        return null;
    }

    @Override
    public Node getElementFromBaseDocument() {
        return null;
    }

    @Override
    public boolean isElaboraChild() {
        return false;
    }

    @Override
    public Document mergeElement(Document baseDocument, Node baseElement) {
        // List<Element> listaElement = pluginElement.elements();
        // for (Element element : listaElement) {
        // baseElement.element("list").elements().add(element.createCopy());
        // }
        return baseDocument;
    }

}
