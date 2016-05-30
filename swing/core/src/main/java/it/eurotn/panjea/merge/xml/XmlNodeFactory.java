package it.eurotn.panjea.merge.xml;

import org.w3c.dom.Node;

public class XmlNodeFactory {

    public static XmlNode getInstance(Node pluginElement) {
        int type = pluginElement.getNodeType();
        if (type != Node.ELEMENT_NODE) {
            return null;
        }

        switch (pluginElement.getNodeName()) {
        case "bean":
            return new BeanNode(pluginElement);
        case "property":
            return new PropertyNode(pluginElement);
        case "list":
            // return new ListNode(pluginElement);
        case "map":
            // return new MapNode(pluginElement);
        default:
            return null;
        }
    }
}
