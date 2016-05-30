package it.eurotn.panjea.merge.xml;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class PluginMerge2 {
    static Logger logger = Logger.getLogger(PluginMerge2.class);

    public static void main(String arg[]) {
        PluginMerge2 merge = new PluginMerge2();
        merge.startTest();
        // DIR_REF = "/media/dati/sviluppo/java/workspace/panjea-desktop/build";
    }

    public void checkDoc(Node n) {
        if (n instanceof Text) {
            if (((Text) n).getData() == null) {
                System.err.println("*********** " + ((Text) n).getParentNode().getAttributes().getNamedItem("id"));
            }
        }

        NodeList l = n.getChildNodes();
        for (int i = 0; i < l.getLength(); ++i) {
            checkDoc(l.item(i));
        }
    }

    private String convert(Node node) {
        return "";
        // return new XMLDocument(node).toString();
        // try {
        // TransformerFactory factory = TransformerFactory.newInstance();
        // Transformer transformer = factory.newTransformer();
        // DOMSource source = new DOMSource(node);
        // StringWriter writer = new StringWriter();
        // StreamResult result = new StreamResult(writer);
        // transformer.transform(source, result);
        // return writer.toString();
        // } catch (TransformerFactoryConfigurationError | TransformerException e) {
        // System.err.println(node.getNodeName());
        // throw new RuntimeException(e);
        // }
    }

    private void print(Node node) {
        System.setProperty(DOMImplementationRegistry.PROPERTY, "org.apache.xerces.dom.DOMImplementationSourceImpl");

        DOMImplementationRegistry registry = null;
        try {
            registry = DOMImplementationRegistry.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | ClassCastException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        DOMImplementation domImpl = registry.getDOMImplementation("LS 3.0");

        DOMImplementationLS implLS = (DOMImplementationLS) domImpl;
        LSSerializer dom3Writer = implLS.createLSSerializer();
        dom3Writer.setNewLine("\\l\\n");

        LSOutput output = implLS.createLSOutput();

        ByteArrayOutputStream content = new ByteArrayOutputStream();
        output.setByteStream(System.out);
        output.setEncoding("UTF-8");
        dom3Writer.write(node, output);
    }

    private void save(Node node) {
        System.setProperty(DOMImplementationRegistry.PROPERTY, "org.apache.xerces.dom.DOMImplementationSourceImpl");

        DOMImplementationRegistry registry = null;
        try {
            registry = DOMImplementationRegistry.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | ClassCastException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        DOMImplementation domImpl = registry.getDOMImplementation("LS 3.0");

        DOMImplementationLS implLS = (DOMImplementationLS) domImpl;
        LSSerializer dom3Writer = implLS.createLSSerializer();

        LSOutput output = implLS.createLSOutput();

        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(new File("panjea-context.xml"));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        output.setByteStream(outputStream);
        output.setEncoding("UTF-8");
        dom3Writer.write(node, output);
    }

    public void startTest() {
        ClassLoader cl = this.getClass().getClassLoader();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);

        try {
            writeContextMerge(resolver.getResources("classpath*:/META-INF/panjea-pages-context.xml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Document traverse(Document baseDocument, Node nodePlugin, Node documentNode) {
        if (logger.isDebugEnabled()) {
            logger.debug("-->Node traverse: " + convert(documentNode));
        }

        XmlNode node = XmlNodeFactory.getInstance(nodePlugin);
        if (node == null) {
            return baseDocument;
        }
        baseDocument = node.mergeElement(baseDocument, documentNode);
        if (node.isElaboraChild()) {
            NodeList children = node.getChildren();
            for (int i = 0; i < children.getLength(); i++) {
                traverse(baseDocument, children.item(i), node.getElementFromBaseDocument());
            }
        }
        return baseDocument;
    }

    public void writeContextMerge(Resource[] resources) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
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
        Document baseDocument = builder.parse(new InputSource(new StringReader(
                "<!DOCTYPE beans PUBLIC '-//SPRING//DTD BEAN//EN' 'spring-beans.dtd'><beans></beans>")));

        DocumentBuilderFactory factoryPlugin = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        DocumentBuilder builderPlugin = factoryPlugin.newDocumentBuilder();
        for (Resource xmlPluginResource : resources) {
            Document documentPlugin = builderPlugin.parse(xmlPluginResource.getInputStream());
            NodeList elements = documentPlugin.getDocumentElement().getChildNodes();
            for (int i = 0; i < elements.getLength(); i++) {
                Node nodePlugin = elements.item(i);
                baseDocument = traverse(baseDocument, nodePlugin, null);
            }

        }

        // docMerged = reorderException(docMerged);
        // OutputFormat format = OutputFormat.createPrettyPrint();
        // XMLWriter writer = new XMLWriter(System.out, format);
        // writer.write(baseDocument);
        print(baseDocument);
        save(baseDocument);
        return;
    }
}