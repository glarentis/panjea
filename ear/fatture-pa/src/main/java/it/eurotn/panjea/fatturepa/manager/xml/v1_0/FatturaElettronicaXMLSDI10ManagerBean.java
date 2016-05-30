package it.eurotn.panjea.fatturepa.manager.xml.v1_0;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.fatturepa.domain.AziendaFatturaPA;
import it.eurotn.panjea.fatturepa.domain.Trasmissione;
import it.eurotn.panjea.fatturepa.manager.xml.interfaces.FatturaElettronicaXMLBodyManager;
import it.eurotn.panjea.fatturepa.manager.xml.interfaces.FatturaElettronicaXMLHeaderManager;
import it.eurotn.panjea.fatturepa.manager.xml.interfaces.FatturaElettronicaXMLManager;
import it.eurotn.panjea.fatturepa.xml.XmlMarshaller;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.gov.fatturapa.sdi.fatturapa.FormatoTrasmissioneType;
import it.gov.fatturapa.sdi.fatturapa.IFatturaElettronicaType;
import it.gov.fatturapa.sdi.fatturapa.v1.FatturaElettronicaBodyType;
import it.gov.fatturapa.sdi.fatturapa.v1.FatturaElettronicaHeaderType;
import it.gov.fatturapa.sdi.fatturapa.v1.FatturaElettronicaType;

@Stateless(name = "Panjea.FatturaElettronicaXMLSDI10Manager")
@SecurityDomain("PanjeaLoginModule")
@LocalBinding(jndiBinding = "Panjea.FatturaElettronicaXMLSDI10Manager")
public class FatturaElettronicaXMLSDI10ManagerBean implements FatturaElettronicaXMLManager {

    private static final Logger LOGGER = Logger.getLogger(FatturaElettronicaXMLSDI10ManagerBean.class);

    @EJB(mappedName = "FatturaElettronicaXMLSDI10HeaderManager")
    private FatturaElettronicaXMLHeaderManager xmlHeaderManager;

    @EJB(mappedName = "FatturaElettronicaXMLSDI10BodyManager")
    private FatturaElettronicaXMLBodyManager xmlBodyManager;

    /**
     * In base al codice di errore generato dal Sdi ne restituisce la descrizione.
     *
     * @param errorCode
     *            codice di errore
     * @return descrizione
     */
    public static String getSDIErrorDescription(String errorCode) {
        String descrizioneErrore = "";
        if ("EI01".equals(errorCode)) {
            descrizioneErrore = "File vuoto";
        } else if ("EI02".equals(errorCode)) {
            descrizioneErrore = "Servizio non disponibile";
        } else if ("EI03".equals(errorCode)) {
            descrizioneErrore = "Utente non abilitato";
        } else {
            descrizioneErrore = "Errore generico";
        }
        return descrizioneErrore;
    }

    @Override
    public IFatturaElettronicaType caricaFatturaElettronicaType(String xmlContent) {

        XmlMarshaller<FatturaElettronicaType> mr = new XmlMarshaller<FatturaElettronicaType>();

        InputStream xmlInputStream = new ByteArrayInputStream(xmlContent.getBytes());

        IFatturaElettronicaType fatturaElettronicaType = null;
        try {
            fatturaElettronicaType = mr.unmarshal(FatturaElettronicaType.class, xmlInputStream, null);
        } catch (Exception e) {
            LOGGER.error("-->errore ", e);
            throw new RuntimeException(e);
        }

        return fatturaElettronicaType;
    }

    @Override
    public String getXmlData(IFatturaElettronicaType fatturaElettronicaType) {

        QName qname = new QName("http://www.fatturapa.gov.it/sdi/fatturapa/v1.0", "FatturaElettronica");
        JAXBElement<FatturaElettronicaType> jaxbElement = new JAXBElement<FatturaElettronicaType>(qname,
                FatturaElettronicaType.class, null, (FatturaElettronicaType) fatturaElettronicaType);

        XmlMarshaller<FatturaElettronicaType> mr = new XmlMarshaller<FatturaElettronicaType>();

        String data = null;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            mr.writeDocument(jaxbElement, bos, "fatturapa_v1.0.xsl");
            data = bos.toString();
        } catch (Exception e) {
            LOGGER.error("Exception caught while unmarshalling object.", e);
        }

        return data;
    }

    @Override
    public String getXmlData(Trasmissione trasmissione, AziendaFatturaPA aziendaPA, AreaMagazzino areaMagazzino) {

        FatturaElettronicaType fattura = new FatturaElettronicaType();
        fattura.setVersione(FormatoTrasmissioneType.SDI_10.getCodice());
        fattura.setFatturaElettronicaHeader(
                (FatturaElettronicaHeaderType) xmlHeaderManager.getHeader(trasmissione, aziendaPA, areaMagazzino));
        fattura.getFatturaElettronicaBody().add((FatturaElettronicaBodyType) xmlBodyManager.getBody(areaMagazzino));

        return getXmlData(fattura);
    }

}
