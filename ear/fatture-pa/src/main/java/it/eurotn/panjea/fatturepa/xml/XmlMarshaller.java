package it.eurotn.panjea.fatturepa.xml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.util.JAXBSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class XmlMarshaller<T> {

    public static transient Logger logger;

    static {
        logger = Logger.getLogger(XmlMarshaller.class);
    }

    private boolean hasErrors = false;

    private StringBuilder buffer;

    private T result;

    /**
     * Costruttore.
     */
    public XmlMarshaller() {
        buffer = new StringBuilder();
    }

    public static <K> K readJAXBProperty(JAXBElement<K> element) {

        if (element == null) {
            return null;
        }

        K value = element.getValue();
        if (value == null) {
            return null;
        } else {
            return value;
        }
    }

    public static String readText(String s) {
        if (s == null || s.trim().length() == 0) {
            return null;
        } else {
            return s;
        }
    }

    public StringBuilder getBuffer() {
        return buffer;
    }

    public T getResult() {
        return result;
    }

    public boolean isHasErrors() {
        return hasErrors;
    }

    public void setBuffer(StringBuilder sb) {
        this.buffer = sb;
    }

    public void setHasErrors(boolean hasErrors) {
        this.hasErrors = hasErrors;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public T unmarshal(Class<T> docClass, InputStream inputStream, InputStream xsd) throws JAXBException, SAXException {
        return unmarshal(docClass, inputStream, xsd, null);
    }

    @SuppressWarnings("unchecked")
    public T unmarshal(Class<T> docClass, InputStream inputStream, InputStream xsd, String charsetName)
            throws JAXBException, SAXException {
        String packageName = docClass.getPackage().getName();
        JAXBContext jc = JAXBContext.newInstance(packageName);
        Unmarshaller u = jc.createUnmarshaller();

        if (xsd != null) {
            // Setting the Validation
            //
            Schema schema;
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

            // associate the schema factory with the resource resolver, which is
            // responsible for resolving the imported XSD's
            //
            schemaFactory.setResourceResolver(new ResourceResolver());

            schema = schemaFactory.newSchema(new StreamSource(xsd));
            u.setSchema(schema);

            hasErrors = false;
            result = null;

            u.setEventHandler(new ValidationEventHandler() {

                @Override
                public boolean handleEvent(ValidationEvent event) {
                    hasErrors = true;
                    MessageFormat mf = new MessageFormat("LINE: {0} COLUMN: {1} ERROR: {2}\n");
                    String error = mf.format(new Object[] { event.getLocator().getLineNumber(),
                            event.getLocator().getColumnNumber(), event.getMessage() });
                    buffer.append(error);
                    return true;
                }
            });
        }
        InputStreamReader r = new InputStreamReader(inputStream);
        if (charsetName != null) {
            try {
                r = new InputStreamReader(inputStream, charsetName);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        JAXBElement<T> doc = (JAXBElement<T>) u.unmarshal(r);

        if (hasErrors) {
            logger.info("ERRORI trovati:");
            logger.info(buffer.toString());
        }

        this.result = doc.getValue();
        return result;
    }

    public String validate(Class<T> docClass, T x_object, String xsd) throws JAXBException, SAXException, IOException {
        String packageName = docClass.getPackage().getName();
        JAXBContext jc = JAXBContext.newInstance(packageName);
        JAXBSource source = new JAXBSource(jc, x_object);

        // Setting the Validation
        //
        Schema schema;
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        // associate the schema factory with the resource resolver, which is
        // responsible for resolving the imported XSD's
        //
        schemaFactory.setResourceResolver(new ResourceResolver());

        InputStreamReader isr = null;
        InputStream xsdInputStream = null;
        try {
            xsdInputStream = XmlMarshaller.class.getClassLoader().getResourceAsStream("/fatturapa_v1.1.xsd");
            // isr = new InputStreamReader(xsdInputStream);
            // isr.read();

            File file = new File("fatturapa_v1.1.xsd");

            schema = schemaFactory.newSchema(new StreamSource(file));
            Validator validator = schema.newValidator();
            validator.setErrorHandler(new ErrorHandler() {

                @Override
                public void error(SAXParseException exception) throws SAXException {
                    hasErrors = true;
                    MessageFormat mf = new MessageFormat("LINE: {0} COLUMN: {1} ERROR: {2}\n");
                    String error = mf.format(new Object[] { exception.getLineNumber(), exception.getColumnNumber(),
                            exception.getMessage() });
                    buffer.append(error);
                }

                @Override
                public void fatalError(SAXParseException exception) throws SAXException {
                    hasErrors = true;
                    MessageFormat mf = new MessageFormat("LINE: {0} COLUMN: {1} ERROR: {2}\n");
                    String error = mf.format(new Object[] { exception.getLineNumber(), exception.getColumnNumber(),
                            exception.getMessage() });
                    buffer.append(error);
                }

                @Override
                public void warning(SAXParseException exception) throws SAXException {
                    // Nothing.
                    //
                    MessageFormat mf = new MessageFormat("LINE: {0} COLUMN: {1} ERROR: {2}\n");
                    String error = mf.format(new Object[] { exception.getLineNumber(), exception.getColumnNumber(),
                            exception.getMessage() });
                }
            });

            hasErrors = false;
            buffer = new StringBuilder();

            validator.validate(source);

            if (hasErrors) {
                logger.info("ERRORI trovati:");
                logger.info(buffer.toString());
                return buffer.toString();
            } else {
                return null;
            }
        } finally {
            // xsdInputStream.close();
            // isr.close();
        }
    }

    @SuppressWarnings("unchecked")
    public void writeDocument(JAXBElement<T> document, OutputStream os, String xsl) throws JAXBException, IOException {

        os.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"yes\"?>".getBytes());

        Class<T> clazz = (Class<T>) document.getValue().getClass();
        JAXBContext context = JAXBContext.newInstance(clazz.getPackage().getName());
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        // setto l'encoding in base al systema operativo
        String opSystem = System.getProperty("os.name").toLowerCase();
        if (StringUtils.contains(opSystem.toLowerCase(), "linux")) {
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        } else {
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "ISO-8859-1");
        }

        marshaller.setProperty("com.sun.xml.bind.xmlHeaders",
                "<?xml-stylesheet type='text/xsl' href=\"" + xsl + "\" ?>");
        marshaller.marshal(document, os);
    }

}
