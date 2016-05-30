package it.gov.fatturapa.sdi.messaggi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import it.eurotn.panjea.fatturepa.domain.StatoFatturaPA;
import it.gov.fatturapa.sdi.fatturapa.FormatoTrasmissioneType;

public final class XMLtoHTMLTransformer {

    private static final Logger LOGGER = Logger.getLogger(XMLtoHTMLTransformer.class);

    /**
     * Costruttore.
     */
    private XMLtoHTMLTransformer() {
    }

    private static FormatoTrasmissioneType getFormatoTrasmissione(String xmlContent) {

        FormatoTrasmissioneType formatoTrasmissioneType = null;
        if (!StringUtils.isBlank(xmlContent)) {
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(new InputSource(new StringReader(xmlContent)));
                Element rootElement = document.getDocumentElement();

                String formatoTrasmisisoneString = rootElement.getAttributes().getNamedItem("versione").getNodeValue();

                formatoTrasmissioneType = FormatoTrasmissioneType.fromCodice(formatoTrasmisisoneString);
            } catch (Exception e) {
                LOGGER.error("--> errore durante il parse del file XML", e);
                formatoTrasmissioneType = null;
            }
        }

        return formatoTrasmissioneType;
    }

    /**
     * Genera l'HTML del file xml passato come parametro in base allo stato della fattura.
     *
     * @param statoFatturaPA
     *            stato fattura
     * @param xmlContent
     *            contenuto dell'xml
     * @return HTML generato
     */
    public static String getHTML(StatoFatturaPA statoFatturaPA, String xmlContent) {

        String html = null;

        // sono stati creati appositamente in Panjea quindi non avranno mai un foglio di stile e andranno visualizzati
        // così come sono
        if (statoFatturaPA == StatoFatturaPA._DI || statoFatturaPA == StatoFatturaPA._IN) {
            return html;
        }

        FormatoTrasmissioneType formatoTrasmissione = getFormatoTrasmissione(xmlContent);
        if (formatoTrasmissione != null) {
            byte[] xsl = getXSL(statoFatturaPA, formatoTrasmissione);

            if (xsl != null) {

                try {
                    TransformerFactory transformerFactory = TransformerFactory.newInstance();

                    Source xslDoc = new StreamSource(new ByteArrayInputStream(xsl));
                    Source xmlDoc = new StreamSource(new ByteArrayInputStream(xmlContent.getBytes()));

                    ByteArrayOutputStream htmlFile = new ByteArrayOutputStream();
                    Transformer trasform = transformerFactory.newTransformer(xslDoc);
                    trasform.transform(xmlDoc, new StreamResult(htmlFile));

                    html = new String(htmlFile.toByteArray());
                } catch (Exception e) {
                    LOGGER.error("--> errore durante la trasformazione del file da XML a HTML.", e);
                }
            }
        }

        return html;
    }

    private static byte[] getXSL(StatoFatturaPA statoFatturaPA, FormatoTrasmissioneType formatoTrasmissioneType) {

        byte[] xsl = null;

        try {
            // ricavo il nome del file in base allo stato della fattura
            String xslFileName = statoFatturaPA.name() + ".xsl";

            // ricavo il package dove sono contenuti i fogli di stile in base al formato di trasmissione
            String packageVersionName = "";
            switch (formatoTrasmissioneType) {
            case SDI_10:
                packageVersionName = "v1";
                break;
            case SDI_11:
                packageVersionName = "v1_1";
                break;
            default:
                packageVersionName = "v1";
                break;
            }

            String basePackageName = "it/gov/fatturapa/sdi/messaggi/";

            InputStream resourceAsStream = XMLtoHTMLTransformer.class.getClassLoader()
                    .getResourceAsStream(basePackageName + packageVersionName + "/xslt/" + xslFileName);

            xsl = IOUtils.toByteArray(resourceAsStream);
        } catch (Exception e) {
            LOGGER.error("--> errore durante il caricamento del foglio di stile per lo stato " + statoFatturaPA.name()
                    + " e il formato " + formatoTrasmissioneType.getCodice(), e);
        }

        return xsl;
    }
}
