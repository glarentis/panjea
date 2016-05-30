// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Unmarshaller.java

package it.eurotn.rich.report.jasperserver;

import java.io.StringReader;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.Argument;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ListItem;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.OperationResult;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.Request;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceProperty;
import com.sun.org.apache.xerces.internal.parsers.DOMParser;

public class Unmarshaller {

    public static String readPCDATA(Node textNode) {
        return readPCDATA(textNode, true);
    }

    public static String readPCDATA(Node textNode, boolean trim) {
        NodeList list_child = textNode.getChildNodes();
        for (int ck = 0; ck < list_child.getLength(); ck++) {
            Node child_child = list_child.item(ck);
            Node ns = child_child.getNextSibling();
            if (ns != null) {
                child_child = ns;
            }
            short nt = child_child.getNodeType();
            if (nt == 4) {
                if (trim) {
                    return child_child.getNodeValue().trim();
                } else {
                    return child_child.getNodeValue();
                }
            }
        }

        for (int ck = 0; ck < list_child.getLength(); ck++) {
            Node child_child = list_child.item(ck);
            Node ns = child_child.getNextSibling();
            if (ns != null) {
                child_child = ns;
            }
            short nt = child_child.getNodeType();
            if (nt == 3) {
                if (trim) {
                    return child_child.getNodeValue().trim();
                } else {
                    return child_child.getNodeValue();
                }
            }
        }

        return "";
    }

    public static Object unmarshal(Class c, StringReader sr) throws Exception {
        Unmarshaller u = new Unmarshaller();
        return u.unmarshal(sr);
    }

    public static Object unmarshalXml(String xmlString) throws Exception {
        Unmarshaller u = new Unmarshaller();
        return u.unmarshal(xmlString);
    }

    public Unmarshaller() {
    }

    private Argument readArgument(Node argumentNode) {
        Argument argument = new Argument();
        NamedNodeMap nodeAttributes = argumentNode.getAttributes();
        if (nodeAttributes.getNamedItem("name") != null) {
            argument.setName(nodeAttributes.getNamedItem("name").getNodeValue());
        }
        argument.setValue(readPCDATA(argumentNode));
        return argument;
    }

    private OperationResult readOperationResult(Node operationResultNode) {
        OperationResult or = new OperationResult();
        NamedNodeMap nodeAttributes = operationResultNode.getAttributes();
        if (nodeAttributes.getNamedItem("version") != null) {
            or.setVersion(nodeAttributes.getNamedItem("version").getNodeValue());
        }
        NodeList childsOfChild = operationResultNode.getChildNodes();
        for (int c_count = 0; c_count < childsOfChild.getLength(); c_count++) {
            Node child_child = childsOfChild.item(c_count);
            if (child_child.getNodeType() == 1 && child_child.getNodeName().equals("returnCode")) {
                or.setReturnCode(Integer.parseInt(readPCDATA(child_child)));
                continue;
            }
            if (child_child.getNodeType() == 1 && child_child.getNodeName().equals("returnMessage")) {
                or.setMessage(readPCDATA(child_child));
                continue;
            }
            if (child_child.getNodeType() == 1 && child_child.getNodeName().equals("resourceDescriptor")) {
                or.getResourceDescriptors().add(readResourceDescriptor(child_child));
            }
        }

        return or;
    }

    private Request readRequest(Node requestNode) {
        Request request = new Request();
        NamedNodeMap nodeAttributes = requestNode.getAttributes();
        if (nodeAttributes.getNamedItem("operationName") != null) {
            request.setOperationName(nodeAttributes.getNamedItem("operationName").getNodeValue());
        }
        if (nodeAttributes.getNamedItem("locale") != null) {
            request.setLocale(nodeAttributes.getNamedItem("locale").getNodeValue());
        }
        NodeList childsOfChild = requestNode.getChildNodes();
        for (int c_count = 0; c_count < childsOfChild.getLength(); c_count++) {
            Node child_child = childsOfChild.item(c_count);
            if (child_child.getNodeType() == 1 && child_child.getNodeName().equals("argument")) {
                request.getArguments().add(readArgument(child_child));
            }
            if (child_child.getNodeType() == 1 && child_child.getNodeName().equals("resourceDescriptor")) {
                request.setResourceDescriptor(readResourceDescriptor(child_child));
            }
        }

        return request;
    }

    private ResourceDescriptor readResourceDescriptor(Node rpNode) {
        ResourceDescriptor rd = new ResourceDescriptor();
        NamedNodeMap nodeAttributes = rpNode.getAttributes();
        if (nodeAttributes.getNamedItem("name") != null) {
            rd.setName(nodeAttributes.getNamedItem("name").getNodeValue());
        }
        if (nodeAttributes.getNamedItem("wsType") != null) {
            rd.setWsType(nodeAttributes.getNamedItem("wsType").getNodeValue());
        }
        if (nodeAttributes.getNamedItem("uriString") != null) {
            rd.setUriString(nodeAttributes.getNamedItem("uriString").getNodeValue());
        }
        if (nodeAttributes.getNamedItem("isNew") != null) {
            rd.setIsNew(nodeAttributes.getNamedItem("isNew").getNodeValue().equals("true"));
        }
        NodeList childsOfChild = rpNode.getChildNodes();
        for (int c_count = 0; c_count < childsOfChild.getLength(); c_count++) {
            Node child_child = childsOfChild.item(c_count);
            if (child_child.getNodeType() == 1 && child_child.getNodeName().equals("label")) {
                rd.setLabel(readPCDATA(child_child));
                continue;
            }
            if (child_child.getNodeType() == 1 && child_child.getNodeName().equals("description")) {
                rd.setDescription(readPCDATA(child_child));
                continue;
            }
            if (child_child.getNodeType() == 1 && child_child.getNodeName().equals("resourceProperty")) {
                rd.setResourceProperty(readResourceProperty(child_child));
                continue;
            }
            if (child_child.getNodeType() == 1 && child_child.getNodeName().equals("resourceDescriptor")) {
                rd.getChildren().add(readResourceDescriptor(child_child));
                continue;
            }
            if (child_child.getNodeType() == 1 && child_child.getNodeName().equals("parameter")) {
                rd.getParameters().add(readResourceParameter(child_child));
            }
        }

        return rd;
    }

    private ListItem readResourceParameter(Node rpNode) {
        ListItem rp = new ListItem();
        NamedNodeMap nodeAttributes = rpNode.getAttributes();
        if (nodeAttributes.getNamedItem("name") != null) {
            rp.setLabel(nodeAttributes.getNamedItem("name").getNodeValue());
        }
        if (nodeAttributes.getNamedItem("isListItem") != null) {
            rp.setIsListItem(nodeAttributes.getNamedItem("isListItem").getNodeValue().equals("true"));
        }
        rp.setValue(readPCDATA(rpNode));
        return rp;
    }

    private ResourceProperty readResourceProperty(Node rpNode) {
        ResourceProperty rp = new ResourceProperty(null);
        NamedNodeMap nodeAttributes = rpNode.getAttributes();
        if (nodeAttributes.getNamedItem("name") != null) {
            rp.setName(nodeAttributes.getNamedItem("name").getNodeValue());
        }
        NodeList childsOfChild = rpNode.getChildNodes();
        for (int c_count = 0; c_count < childsOfChild.getLength(); c_count++) {
            Node child_child = childsOfChild.item(c_count);
            if (child_child.getNodeType() == 1 && child_child.getNodeName().equals("value")) {
                rp.setValue(readPCDATA(child_child));
                continue;
            }
            if (child_child.getNodeType() == 1 && child_child.getNodeName().equals("resourceProperty")) {
                rp.getProperties().add(readResourceProperty(child_child));
            }
        }
        return rp;
    }

    public Object unmarshal(String xml) throws Exception {
        StringReader sreader = new StringReader(xml);
        return unmarshal(sreader);
    }

    public Object unmarshal(StringReader sreader) throws Exception {
        Node rootNode;
        DOMParser parser = new DOMParser();
        InputSource input_source = new InputSource(sreader);
        parser.parse(input_source);
        Document document = parser.getDocument();
        rootNode = document.getDocumentElement();
        if (rootNode.getNodeName().equals("request")) {
            return readRequest(rootNode);
        }
        if (rootNode.getNodeName().equals("operationResult")) {
            return readOperationResult(rootNode);
        } else {
            return null;
        }
    }
}
