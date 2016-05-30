package it.eurotn.panjea.merge.xml;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class BeanNode implements XmlNode {

    private Node pluginElement;
    private boolean elaboraChild;
    private Node elementBase;

    public BeanNode(final Node pluginElement) {
        this.pluginElement = pluginElement;
    }

    private Document addElement(Document baseDocument) {
        baseDocument.getDocumentElement().appendChild(baseDocument.adoptNode(pluginElement).cloneNode(true));
        return baseDocument;
    }

    @Override
    public NodeList getChildren() {
        return pluginElement.getChildNodes();
    }

    @Override
    public Node getElementFromBaseDocument() {
        return elementBase;
    }

    @Override
    public boolean isElaboraChild() {
        return elaboraChild;
    }

    @Override
    public Document mergeElement(Document baseDocument, Node baseElement) {
        elaboraChild = false;
        Node idAttribute = pluginElement.getAttributes().getNamedItem("id");
        if (idAttribute == null) {
            return baseDocument;
        }
        String idPlugin = idAttribute.getNodeValue();
        if (StringUtils.isEmpty(idPlugin)) {
            return baseDocument;
        }

        // elementBase = baseDocument.getElementById(idPlugin);
        String path = String.format("//*[@id = '%1$s' or @Id = '%1$s' or @ID = '%1$s' or @iD = '%1$s' ]", idPlugin);
        XPath xPath = XPathFactory.newInstance().newXPath();
        NodeList nodes = null;
        try {
            nodes = (NodeList) xPath.evaluate(path, baseDocument, XPathConstants.NODESET);
            elementBase = nodes.item(0);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }

        if (elementBase == null) {
            baseDocument = addElement(baseDocument);
        } else {
            elaboraChild = true;
        }
        return baseDocument;
    }

}
