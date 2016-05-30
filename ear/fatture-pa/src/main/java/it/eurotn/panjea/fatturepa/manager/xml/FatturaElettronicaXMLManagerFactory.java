package it.eurotn.panjea.fatturepa.manager.xml;

import java.io.StringReader;

import javax.ejb.SessionContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import it.eurotn.panjea.fatturepa.manager.xml.interfaces.FatturaElettronicaXMLManager;
import it.gov.fatturapa.sdi.fatturapa.FormatoTrasmissioneType;

public final class FatturaElettronicaXMLManagerFactory {

    private static final Logger LOGGER = Logger.getLogger(FatturaElettronicaXMLManagerFactory.class);

    /**
     * Costruttore.
     */
    private FatturaElettronicaXMLManagerFactory() {
    }

    private static String getElementValue(String tagName, Element element) {
        NodeList list = element.getElementsByTagName(tagName);
        if (list != null && list.getLength() > 0) {
            NodeList subList = list.item(0).getChildNodes();

            if (subList != null && subList.getLength() > 0) {
                return subList.item(0).getNodeValue();
            }
        }
        return null;
    }

    /**
     * Manager da utilizzare in base al formato di trasmissione scelto.
     *
     * @param formatoTrasmissione
     *            formato di trasmissione
     * @param context
     *            context
     * @return manager
     */
    public static FatturaElettronicaXMLManager getXMLManager(FormatoTrasmissioneType formatoTrasmissione,
            SessionContext context) {

        FatturaElettronicaXMLManager xmlManager = null;

        if (formatoTrasmissione != null) {
            xmlManager = (FatturaElettronicaXMLManager) context
                    .lookup("Panjea.FatturaElettronicaXML" + formatoTrasmissione.value() + "Manager");
        }

        return xmlManager;
    }

    /**
     * Manager da utilizzare in base al formato di trasmissione specificato nel file XML.
     *
     * @param xmlContent
     *            contenuto del file XML
     * @param context
     *            context
     * @return manager
     */
    public static FatturaElettronicaXMLManager getXMLManager(String xmlContent, SessionContext context) {

        FatturaElettronicaXMLManager xmlManager = null;

        if (xmlContent != null) {

            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(new InputSource(new StringReader(xmlContent)));
                Element rootElement = document.getDocumentElement();

                String formatoTrasmisisoneString = getElementValue("FormatoTrasmissione", rootElement);

                xmlManager = getXMLManager(FormatoTrasmissioneType.fromValue(formatoTrasmisisoneString), context);
            } catch (Exception e) {
                LOGGER.error("--> errore durante il parse del file XML", e);
                throw new RuntimeException("errore durante il parse del file XML", e);
            }
        }

        return xmlManager;
    }
}
