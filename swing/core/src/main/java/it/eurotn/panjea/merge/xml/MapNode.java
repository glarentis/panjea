package it.eurotn.panjea.merge.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class MapNode implements XmlNode {

    private Node pluginElement;

    public MapNode(final Node pluginElement) {
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
        // List<Element> mapElement = pluginElement.elements();
        // for (Element element : mapElement) {
        // Element keyElement = (Element) baseElement
        // .selectSingleNode("//entry[@key='" + element.attributeValue("key") + "']");
        // if (keyElement == null) {
        // baseElement.element("map").add(element.createCopy());
        // } else {
        // if (keyElement.elements().size() > 0) {
        // Element listaBase = (Element) keyElement.elements().get(0);
        // Element listaPlugin = (Element) element.elements().get(0);
        // List<Element> elementiListaPlugin = listaPlugin.elements();
        // for (Element valore : elementiListaPlugin) {
        // listaBase.add(valore.createCopy());
        // }
        // }
        // }
        return baseDocument;
    }
}
