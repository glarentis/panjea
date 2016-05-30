package it.eurotn.panjea.fatturepa.manager.xml.v1_1;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;
import org.xml.sax.SAXException;

import it.eurotn.panjea.fatturepa.domain.AziendaFatturaPA;
import it.eurotn.panjea.fatturepa.domain.Trasmissione;
import it.eurotn.panjea.fatturepa.manager.xml.interfaces.FatturaElettronicaXMLBodyManager;
import it.eurotn.panjea.fatturepa.manager.xml.interfaces.FatturaElettronicaXMLHeaderManager;
import it.eurotn.panjea.fatturepa.manager.xml.interfaces.FatturaElettronicaXMLManager;
import it.eurotn.panjea.fatturepa.xml.XmlMarshaller;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.gov.fatturapa.sdi.fatturapa.FormatoTrasmissioneType;
import it.gov.fatturapa.sdi.fatturapa.IFatturaElettronicaType;
import it.gov.fatturapa.sdi.fatturapa.v1_1.FatturaElettronicaBodyType;
import it.gov.fatturapa.sdi.fatturapa.v1_1.FatturaElettronicaHeaderType;
import it.gov.fatturapa.sdi.fatturapa.v1_1.FatturaElettronicaType;

@Stateless(name = "Panjea.FatturaElettronicaXMLSDI11Manager")
@SecurityDomain("PanjeaLoginModule")
@LocalBinding(jndiBinding = "Panjea.FatturaElettronicaXMLSDI11Manager")
public class FatturaElettronicaXMLSDI11ManagerBean implements FatturaElettronicaXMLManager {

    private static final Logger LOGGER = Logger.getLogger(FatturaElettronicaXMLSDI11ManagerBean.class);

    @EJB(mappedName = "FatturaElettronicaXMLSDI11HeaderManager")
    private FatturaElettronicaXMLHeaderManager xmlHeaderManager;

    @EJB(mappedName = "FatturaElettronicaXMLSDI11BodyManager")
    private FatturaElettronicaXMLBodyManager xmlBodyManager;

    @Override
    public IFatturaElettronicaType caricaFatturaElettronicaType(String xmlContent) {

        XmlMarshaller<FatturaElettronicaType> mr = new XmlMarshaller<FatturaElettronicaType>();

        InputStream xmlInputStream = new ByteArrayInputStream(xmlContent.getBytes());

        IFatturaElettronicaType fatturaElettronicaType = null;
        try {
            fatturaElettronicaType = mr.unmarshal(FatturaElettronicaType.class, xmlInputStream, null);
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        return fatturaElettronicaType;
    }

    @Override
    public String getXmlData(IFatturaElettronicaType fatturaElettronicaType) {

        fatturaElettronicaType.cleanEmptyValues();

        QName qname = new QName("http://www.fatturapa.gov.it/sdi/fatturapa/v1.1", "FatturaElettronica");
        JAXBElement<FatturaElettronicaType> jaxbElement = new JAXBElement<FatturaElettronicaType>(qname,
                FatturaElettronicaType.class, null, (FatturaElettronicaType) fatturaElettronicaType);

        XmlMarshaller<FatturaElettronicaType> mr = new XmlMarshaller<FatturaElettronicaType>();

        String data = null;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            mr.writeDocument(jaxbElement, bos, "fatturapa_v1.1.xsl");
            data = bos.toString();
        } catch (Exception e) {
            LOGGER.error("Exception caught while unmarshalling object.", e);
        }

        return data;
    }

    @Override
    public String getXmlData(Trasmissione trasmissione, AziendaFatturaPA aziendaPA, AreaMagazzino areaMagazzino) {

        FatturaElettronicaType fattura = new FatturaElettronicaType();
        fattura.setVersione(FormatoTrasmissioneType.SDI_11.getCodice());
        fattura.setFatturaElettronicaHeader(
                (FatturaElettronicaHeaderType) xmlHeaderManager.getHeader(trasmissione, aziendaPA, areaMagazzino));
        fattura.getFatturaElettronicaBody().add((FatturaElettronicaBodyType) xmlBodyManager.getBody(areaMagazzino));
        fattura.cleanEmptyValues();

        return getXmlData(fattura);
    }

}
