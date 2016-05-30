package it.eurotn.querybuilder.manager;

import java.io.StringWriter;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.querybuilder.domain.ParametriRicercaQueryBuilder;
import it.eurotn.querybuilder.domain.ProprietaEntity;
import it.eurotn.querybuilder.manager.interfaces.ParametriRicercaManager;

@Stateless(name = "Panjea.ParametriRicercaManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.ParametriRicercaManager")
public class ParametriRicercaManagerBean implements ParametriRicercaManager {

    private static final Logger LOGGER = Logger.getLogger(ParametriRicercaManagerBean.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @SuppressWarnings("unchecked")
    @Override
    public List<ParametriRicercaQueryBuilder> caricaParametriRicerca(Class<? extends EntityBase> clazz) {
        LOGGER.debug("--> Enter caricaParametriRicerca");

        Query query = panjeaDAO.prepareQuery(
                "select p from ParametriRicercaQueryBuilder p where managedClassName = '" + clazz.getName() + "'");

        List<ParametriRicercaQueryBuilder> results = null;
        try {
            results = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            LOGGER.error("--> errore durante il caricamento dei parametri ricerca per la classe " + clazz.getName(), e);
            throw new GenericException(
                    "errore durante il caricamento dei parametri ricerca per la classe " + clazz.getName(), e);
        }

        LOGGER.debug("--> Exit caricaParametriRicerca");
        return results;
    }

    private String getXMLProperties(List<ProprietaEntity> properties) {

        String result = "";

        if (CollectionUtils.isEmpty(properties)) {
            return result;
        }

        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("parametri");
            doc.appendChild(rootElement);

            for (ProprietaEntity prop : properties) {
                if (prop.isInSelect() || prop.getOperatore() != null) {
                    Element propNode = doc.createElement("property");
                    rootElement.appendChild(propNode);

                    Attr attr = doc.createAttribute("id");
                    attr.setValue(prop.getPropertyFullPath());
                    propNode.setAttributeNode(attr);

                    if (prop.isInSelect()) {
                        Element inSelect = doc.createElement("inSelect");
                        inSelect.appendChild(doc.createTextNode("1"));
                        propNode.appendChild(inSelect);
                    }

                    if (prop.getOperatore() != null) {
                        Element operatore = doc.createElement("operatore");
                        operatore.appendChild(doc.createTextNode(prop.getOperatore().name()));
                        propNode.appendChild(operatore);
                    }

                    if (prop.getFiltro() != null) {
                        Element operatore = doc.createElement("filtro");
                        operatore.appendChild(doc.createTextNode(prop.getFiltro().toString()));
                        propNode.appendChild(operatore);
                    }
                }
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            DOMSource source = new DOMSource(doc);
            StringWriter stringWriter = new StringWriter();
            StreamResult streamResult = new StreamResult(stringWriter);
            transformer.transform(source, streamResult);
            result = stringWriter.toString();
        } catch (Exception e) {
            LOGGER.error("--> errore durante la creazione dell'xml per i parametri di ricerca", e);
            throw new GenericException("errore durante la creazione dell'xml per i parametri di ricerca", e);
        }

        return result;
    }

    @Override
    public ParametriRicercaQueryBuilder salvaParametriRicerca(ParametriRicercaQueryBuilder parametri) {
        LOGGER.debug("--> Enter salvaParametriRicerca");

        parametri.setXmlProperties(getXMLProperties(parametri.getProprietaDaSalvare()));

        ParametriRicercaQueryBuilder parametriSalvati = null;
        try {
            parametriSalvati = panjeaDAO.save(parametri);
        } catch (Exception e) {
            LOGGER.error("--> errore durante il salvataggio dei parametri di ricerca per la classe "
                    + parametri.getManagedClassName(), e);
            throw new GenericException("errore durante il salvataggio dei parametri di ricerca per la classe "
                    + parametri.getManagedClassName(), e);
        }

        LOGGER.debug("--> Exit salvaParametriRicerca");
        return parametriSalvati;
    }
}
