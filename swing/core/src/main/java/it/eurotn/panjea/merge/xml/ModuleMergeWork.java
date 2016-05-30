package it.eurotn.panjea.merge.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSParser;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class ModuleMergeWork implements Runnable {

    private final class ResourceComparator implements Comparator<Resource> {
        @Override
        public int compare(Resource o1, Resource o2) {
            return getPluginOrder(o1).compareTo(getPluginOrder(o2));
        }

        private Integer getPluginOrder(Resource resource) {
            String uri = "";
            try {
                uri = resource.getURL().toString();
            } catch (final Exception e) {
                uri = "";
            }

            Integer order = 999;
            for (int i = 0; i < PLUGIN_ORDER.length; i++) {
                if (uri.matches(".*META-INF.*" + PLUGIN_ORDER[i] + ".*")) {
                    order = i;
                    break;
                }
            }

            return order;
        }
    }

    private static final String[] PLUGIN_ORDER = new String[] { "core", "anagrafica", "contabilita", "magazzino",
            "ordini", "preventivi", "pagamenti", "swing" };

    public static Document carica(Resource resource) {
        System.setProperty(DOMImplementationRegistry.PROPERTY, "org.apache.xerces.dom.DOMImplementationSourceImpl");

        try {
            final DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();

            final DOMImplementation domImpl = registry.getDOMImplementation("LS 3.0");

            final DOMImplementationLS implLS = (DOMImplementationLS) domImpl;

            final LSParser parser = implLS.createLSParser(DOMImplementationLS.MODE_SYNCHRONOUS, null);

            return parser.parseURI(resource.getURL().toString());
        } catch (final Exception e) {
            // logger.error("--> Non riesco a caricare il file di context : " + resource);
            System.err.println("ERRORE NEL FILE " + resource.toString());
            e.printStackTrace();
        }

        return null;
    }

    protected static void close(InputStream iStream, OutputStream oStream) throws IOException {
        try {
            if (iStream != null) {
                iStream.close();
            }
        } finally {
            if (oStream != null) {
                oStream.close();
            }
        }
    }

    public static InputStream getEntry(JarFile jarFile, String entryName) {

        final ZipEntry entry = jarFile.getEntry("META-INF" + File.separator + entryName);
        InputStream input = null;
        try {
            input = jarFile.getInputStream(entry);
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return input;
    }

    public static void printDocument(Document doc, OutputStream out) {

        try {
            final TransformerFactory tf = TransformerFactory.newInstance();
            final Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            transformer.transform(new DOMSource(doc), new StreamResult(new OutputStreamWriter(out, "UTF-8")));
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private final List<Resource> xmlResources;

    private final String fileDestination;

    public ModuleMergeWork(List<Resource> xmlResources, String fileDestination) {
        super();
        this.xmlResources = xmlResources;
        this.fileDestination = fileDestination;
    }

    private Document createBaseDocument() {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (final ParserConfigurationException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        builder.setErrorHandler(new ErrorHandler() {

            @Override
            public void error(SAXParseException exception) throws SAXException {
                System.err.println(exception.getMessage());
            }

            @Override
            public void fatalError(SAXParseException exception) throws SAXException {
                System.err.println(exception.getMessage());
            }

            @Override
            public void warning(SAXParseException exception) throws SAXException {
                System.err.println(exception.getMessage());
            }
        });
        Document baseDocument = null;
        try {
            baseDocument = builder.parse(
                    new InputSource(new StringReader("<!DOCTYPE beans SYSTEM \"spring-beans.dtd\"><beans></beans>")));
        } catch (SAXException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return baseDocument;
    }

    private Element getElementById(Document baseDocument, Node node) {
        Element element = null;
        final Node idAttribute = node.getAttributes().getNamedItem("id");
        if (idAttribute != null) {
            final String idPlugin = idAttribute.getNodeValue();
            if (!StringUtils.isEmpty(idPlugin)) {
                element = getElementById(baseDocument, idPlugin);
            }
        }
        return element;
    }

    private Element getElementById(Document baseDocument, String id) {
        // String path = String
        // .format("//*[@id = '%1$s' or @Id = '%1$s' or @ID = '%1$s' or @iD = '%1$s' ]", id);
        // NodeList nodes = null;
        // try {
        // nodes = (NodeList) xPath.evaluate(path, baseDocument, XPathConstants.NODESET);
        // element = (Element) nodes.item(0);
        // } catch (XPathExpressionException e) {
        // e.printStackTrace();
        // }
        return baseDocument.getElementById(id);
    }

    public Document reload(Document document) {
        Document doc = null;
        try {
            final DOMSource domSource = new DOMSource(document);
            final StringWriter writer = new StringWriter();
            final StreamResult result = new StreamResult(writer);
            final TransformerFactory tf = TransformerFactory.newInstance();
            final Transformer transformer = tf.newTransformer();
            final DocumentType doctype = document.getDoctype();
            if (doctype != null) {
                transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype.getSystemId());
            }
            transformer.transform(domSource, result);

            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(true);
            final DocumentBuilder db = factory.newDocumentBuilder();
            db.setErrorHandler(new ErrorHandler() {

                @Override
                public void error(SAXParseException exception) throws SAXException {
                    System.err.println(exception.getMessage());
                }

                @Override
                public void fatalError(SAXParseException exception) throws SAXException {
                    System.err.println(exception.getMessage());
                }

                @Override
                public void warning(SAXParseException exception) throws SAXException {
                    System.err.println(exception.getMessage());
                }
            });
            final InputSource is = new InputSource();
            final String xml = writer.toString();
            // xml = xml.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>",
            // "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE beans SYSTEM
            // \"spring-beans.dtd\">");
            is.setCharacterStream(new StringReader(xml));
            doc = db.parse(is);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return doc;
    }

    /**
     * Cerca i bean delle eccezioni generali e li riporta nelle ultime posizioni
     *
     */
    private void reorderException(Document baseDocument) {

        final Element elementExceptionHandler = getElementById(baseDocument, "exceptionHandler");
        if (elementExceptionHandler == null) {
            return;
        }

        final Element elementErrorException = getElementById(baseDocument, "errorException");
        final Element elementThrowableException = getElementById(baseDocument, "throwableException");
        // logger.debug("--> ExceptionHandler: " + elementExceptionHandler);

        final NodeList childrenProps = elementExceptionHandler.getChildNodes();
        if (childrenProps != null) {
            for (int i = 0; i < childrenProps.getLength(); i++) {
                final Node prop = childrenProps.item(i);
                if ((prop.getNodeName().equals("property"))
                        && (prop.getAttributes().getNamedItem("name").getNodeValue().equals("delegateList"))) {

                    final NodeList childrenLists = prop.getChildNodes();
                    if (childrenLists != null) {
                        for (int z = 0; z < childrenProps.getLength(); z++) {
                            final Node list = childrenLists.item(z);
                            if (list != null && "list".equals(list.getNodeName())) {
                                // rimuovo i nodi
                                list.removeChild(elementErrorException);
                                list.removeChild(elementThrowableException);

                                // reinserisco i nodi in fondo
                                list.appendChild(elementErrorException);
                                list.appendChild(elementThrowableException);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void run() {
        long time = Calendar.getInstance().getTimeInMillis();
        Document baseDocument = createBaseDocument();

        Collections.sort(xmlResources, new ResourceComparator());

        for (final Resource resource : xmlResources) {
            final Document moduleDocument = carica(resource);
            final Element page = moduleDocument.getDocumentElement();
            final NodeList nodes = page.getChildNodes();
            for (int i = 0; i < nodes.getLength(); i++) {
                final Node node = nodes.item(i);
                final int type = node.getNodeType();
                if (type == Node.ELEMENT_NODE) {
                    traverse(baseDocument, nodes.item(i), null);
                }
            }
            baseDocument = reload(baseDocument);
        }
        final long eseguito = Calendar.getInstance().getTimeInMillis() - time;
        time = Calendar.getInstance().getTimeInMillis();
        reorderException(baseDocument);
        salva(baseDocument, fileDestination);
        System.out.println(fileDestination + ":" + eseguito + ":" + (Calendar.getInstance().getTimeInMillis() - time));
    }

    public void salva(Document document, String fileNameMerged) {
        try {
            System.setProperty(DOMImplementationRegistry.PROPERTY, "org.apache.xerces.dom.DOMImplementationSourceImpl");

            final DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();

            final DOMImplementation domImpl = registry.getDOMImplementation("LS 3.0");

            final DOMImplementationLS implLS = (DOMImplementationLS) domImpl;
            final LSSerializer dom3Writer = implLS.createLSSerializer();

            final LSOutput output = implLS.createLSOutput();

            final OutputStream outputStream = new FileOutputStream(new File(fileNameMerged));
            output.setByteStream(outputStream);
            output.setEncoding("UTF-8");
            dom3Writer.write(document, output);
        } catch (final Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void traverse(Document baseDocument, Node node, Node documentNode) {
        final int type = node.getNodeType();
        if (type == Node.ELEMENT_NODE) {

            if (node.getNodeName().equals("constructor-arg")) {
                documentNode.appendChild(baseDocument.importNode(node, true));
            } else if (node.getNodeName().equals("bean")) {
                final Element element = getElementById(baseDocument, node);

                // Element element = null;
                // String path = String.format(
                // "//*[@id = '%1$s' or @Id = '%1$s' or @ID = '%1$s' or @iD = '%1$s' ]",
                // node.getAttributes().getNamedItem("id").getNodeValue());
                // XPath xPath = XPathFactory.newInstance().newXPath();
                // NodeList nodes = null;
                // try {
                // nodes = (NodeList) xPath.evaluate(path, baseDocument, XPathConstants.NODESET);
                // element = (Element) nodes.item(0);
                // } catch (XPathExpressionException e) {
                // e.printStackTrace();
                // }

                if (element == null) {
                    // logger.debug("-->Il bean " + node.getNodeName()
                    // + " non è stato trovato nel xml base quindi lo inserisco.");
                    baseDocument.getDocumentElement().appendChild(baseDocument.importNode(node, true));
                    return;
                } else {
                    final NodeList children = node.getChildNodes();
                    if (children != null) {
                        for (int i = 0; i < children.getLength(); i++) {
                            traverse(baseDocument, children.item(i), element);
                        }
                    }
                }
            } else {
                if (node.getNodeName().equals("property")) {

                    final NodeList attributiDoc = documentNode.getChildNodes();

                    boolean propertyTrovata = false;
                    for (int i = 0; i < attributiDoc.getLength(); i++) {
                        final Node node2 = attributiDoc.item(i);

                        if ((node2.getAttributes() != null) && (node2.getAttributes().getNamedItem("name") != null)) {
                            if (node2.getAttributes().getNamedItem("name").getNodeValue()
                                    .equals(node.getAttributes().getNamedItem("name").getNodeValue())) {
                                propertyTrovata = true;

                                final NodeList children = node.getChildNodes();
                                if (children != null) {
                                    for (int z = 0; z < children.getLength(); z++) {
                                        traverse(baseDocument, children.item(z), node2);
                                    }
                                }
                            }
                        }
                    }

                    if (propertyTrovata == false) {
                        documentNode.appendChild(baseDocument.importNode(node, true));
                    }

                } else {
                    if ((node.getNodeName().equals("list")) || (node.getNodeName().equals("map"))) {

                        final NodeList children = node.getChildNodes();
                        if (children != null) {
                            for (int i = 0; i < children.getLength(); i++) {
                                final Node nodeList = children.item(i);
                                final NodeList childrenDoc = documentNode.getChildNodes();
                                for (int z = 0; z < childrenDoc.getLength(); z++) {
                                    final Node node2 = childrenDoc.item(z);

                                    if (node2.getNodeName().equals("list")) {
                                        node2.appendChild(baseDocument.importNode(nodeList, true));
                                    } else {
                                        if ((node2.getNodeName().equals("map"))) {

                                            final Node nodeEntryMap = nodeList;
                                            if ((nodeEntryMap.getAttributes() != null)
                                                    && ((nodeEntryMap.getAttributes().getNamedItem("key") != null)
                                                            || (nodeEntryMap.getAttributes()
                                                                    .getNamedItem("value-ref") != null)
                                                            || (nodeEntryMap.getAttributes()
                                                                    .getNamedItem("value") != null))) {

                                                boolean entryFound = false;
                                                if ((nodeEntryMap.getAttributes().getNamedItem("key") != null)) {

                                                    final NodeList childMapBase = node2.getChildNodes();
                                                    if (childMapBase != null) {
                                                        for (int a = 0; a < childMapBase.getLength(); a++) {
                                                            final Node ref = childMapBase.item(a);
                                                            if ((ref.getAttributes() != null)
                                                                    && (ref.getAttributes().getNamedItem("key")
                                                                            .getNodeValue()
                                                                            .equals(nodeEntryMap.getAttributes()
                                                                                    .getNamedItem("key")
                                                                                    .getNodeValue()))) {
                                                                entryFound = true;
                                                            }
                                                        }
                                                    }
                                                }
                                                if ((nodeEntryMap.getAttributes().getNamedItem("value-ref") != null)) {
                                                    final NodeList childMapBase = node2.getChildNodes();
                                                    if (childMapBase != null) {
                                                        for (int a = 0; a < childMapBase.getLength(); a++) {
                                                            final Node ref = childMapBase.item(a);
                                                            if ((ref.getAttributes() != null)
                                                                    && (ref.getAttributes()
                                                                            .getNamedItem("value-ref") != null)
                                                                    && (ref.getAttributes().getNamedItem("value-ref")
                                                                            .getNodeValue()
                                                                            .equals(nodeEntryMap.getAttributes()
                                                                                    .getNamedItem("value-ref")
                                                                                    .getNodeValue()))) {
                                                                entryFound = true;
                                                            }
                                                        }
                                                    }
                                                }

                                                if ((nodeEntryMap.getAttributes().getNamedItem("value") != null)) {
                                                    final NodeList childMapBase = node2.getChildNodes();
                                                    if (childMapBase != null) {
                                                        for (int a = 0; a < childMapBase.getLength(); a++) {
                                                            final Node ref = childMapBase.item(a);
                                                            if ((ref.getAttributes() != null)
                                                                    && (ref.getAttributes()
                                                                            .getNamedItem("value") != null)
                                                                    && (ref.getAttributes().getNamedItem("value")
                                                                            .getNodeValue()
                                                                            .equals(nodeEntryMap.getAttributes()
                                                                                    .getNamedItem("value")
                                                                                    .getNodeValue()))) {
                                                                entryFound = true;
                                                            }
                                                        }
                                                    }
                                                }

                                                if (!entryFound) {
                                                    node2.appendChild(baseDocument.importNode(nodeEntryMap, true));
                                                } else {
                                                    final NodeList childEntryMap = nodeEntryMap.getChildNodes();
                                                    for (int y = 0; y < childEntryMap.getLength(); y++) {
                                                        if (childEntryMap != null) {
                                                            final Node nodeValueEntryMap = childEntryMap.item(y);

                                                            if (nodeValueEntryMap.getNodeName().equals("key")) {
                                                                node2.appendChild(
                                                                        baseDocument.importNode(nodeList, true));
                                                            } else {
                                                                if (nodeValueEntryMap.getNodeName().equals("list")) {
                                                                    if ((nodeEntryMap.getAttributes()
                                                                            .getNamedItem("key") != null)) {

                                                                        final NodeList childMapBase = node2
                                                                                .getChildNodes();
                                                                        if (childMapBase != null) {
                                                                            for (int a = 0; a < childMapBase
                                                                                    .getLength(); a++) {

                                                                                final Node keyBase = childMapBase
                                                                                        .item(a).getAttributes() != null
                                                                                                ? childMapBase.item(a)
                                                                                                        .getAttributes()
                                                                                                        .getNamedItem(
                                                                                                                "key")
                                                                                                : null;

                                                                                if (keyBase != null && keyBase
                                                                                        .getNodeValue()
                                                                                        .equals(nodeEntryMap
                                                                                                .getAttributes()
                                                                                                .getNamedItem("key")
                                                                                                .getNodeValue())) {
                                                                                    traverse(baseDocument,
                                                                                            nodeValueEntryMap,
                                                                                            childMapBase.item(a));
                                                                                }

                                                                            }
                                                                        }

                                                                    }
                                                                }
                                                            }

                                                        }
                                                    }
                                                }
                                            } else {
                                                if (nodeEntryMap.getAttributes() != null) {
                                                    node2.appendChild(baseDocument.importNode(nodeEntryMap, true));
                                                }
                                            }

                                        }
                                    }

                                }

                                // }
                            }

                        }

                    }
                }
            }
        }
    }
}