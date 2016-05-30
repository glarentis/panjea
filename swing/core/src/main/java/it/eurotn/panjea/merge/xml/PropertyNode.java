package it.eurotn.panjea.merge.xml;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PropertyNode implements XmlNode {

    private Node pluginElement;
    private boolean elaboraChidl;
    private NodeList children;
    private Node elementFromBaseDocument;

    /**
     *
     * @param pluginElement
     */
    public PropertyNode(final Node pluginElement) {
        this.pluginElement = pluginElement;
    }

    @Override
    public NodeList getChildren() {
        return children;
    }

    @Override
    public Node getElementFromBaseDocument() {
        return elementFromBaseDocument;
    }

    @Override
    public boolean isElaboraChild() {
        return elaboraChidl;
    }

    @Override
    public Document mergeElement(Document baseDocument, Node baseElement) {
        Node baseNodeProperty = null;
        String nomeProp = pluginElement.getNodeName();
        if (StringUtils.isEmpty(nomeProp)) {
            return baseDocument;
        }

        for (Node child = baseElement.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (nomeProp.equals(child.getNodeName())) {
                baseNodeProperty = child;
            }
        }

        if (baseNodeProperty != null) {
            elaboraChidl = true;
            children = pluginElement.getChildNodes();
            elementFromBaseDocument = baseNodeProperty;
        } else {
            baseDocument.getDocumentElement().appendChild(baseDocument.adoptNode(pluginElement).cloneNode(true));
        }
        return baseDocument;
    }

}
