package it.eurotn.panjea.fatturepa.manager;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Query;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.ejb.QueryImpl;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Nazione;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.service.exception.PreferenceNotFoundException;
import it.eurotn.panjea.anagrafica.service.interfaces.PreferenceService;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.fatturepa.domain.AreaMagazzinoFatturaPA;
import it.eurotn.panjea.fatturepa.domain.AreaNotificaFatturaPA;
import it.eurotn.panjea.fatturepa.domain.AziendaFatturaPA;
import it.eurotn.panjea.fatturepa.domain.FatturaPASettings;
import it.eurotn.panjea.fatturepa.domain.NotificaFatturaPA;
import it.eurotn.panjea.fatturepa.domain.StatoFatturaPA;
import it.eurotn.panjea.fatturepa.domain.Trasmissione;
import it.eurotn.panjea.fatturepa.domain.XMLFatturaPA;
import it.eurotn.panjea.fatturepa.manager.exception.XMLCreationException;
import it.eurotn.panjea.fatturepa.manager.interfaces.AziendaFatturaPAManager;
import it.eurotn.panjea.fatturepa.manager.interfaces.FatturaPASettingsManager;
import it.eurotn.panjea.fatturepa.manager.interfaces.FatturePAManager;
import it.eurotn.panjea.fatturepa.manager.xml.FatturaElettronicaXMLManagerFactory;
import it.eurotn.panjea.fatturepa.manager.xml.interfaces.FatturaElettronicaXMLManager;
import it.eurotn.panjea.fatturepa.util.AreaMagazzinoFatturaPARicerca;
import it.eurotn.panjea.fatturepa.util.AreaNotificaFatturaPADTO;
import it.eurotn.panjea.fatturepa.util.ParametriRicercaFatturePA;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino.StatoAreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzinoLite;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;
import it.eurotn.util.PanjeaEJBUtil;
import it.gov.fatturapa.sdi.fatturapa.FormatoTrasmissioneType;
import it.gov.fatturapa.sdi.fatturapa.IFatturaElettronicaType;
import it.gov.fatturapa.sdi.messaggi.XMLtoHTMLTransformer;

@Stateless(name = "Panjea.FatturePAManager")
@SecurityDomain("PanjeaLoginModule")
@LocalBinding(jndiBinding = "Panjea.FatturePAManager")
public class FatturePAManagerBean implements FatturePAManager {

    private static final Logger LOGGER = Logger.getLogger(FatturePAManagerBean.class);

    @Resource
    private SessionContext context;

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    private AziendaFatturaPAManager aziendaFatturaPAManager;

    @EJB
    private AreaMagazzinoManager areaMagazzinoManager;

    @EJB
    private FatturaPASettingsManager fatturaPASettingsManager;

    @EJB
    private it.eurotn.codice.generator.interfaces.ProtocolloGenerator protocolloGenerator;

    @EJB
    private PreferenceService preferenceService;

    @Override
    public void cancellaAreaMagazzinoFatturaPA(Integer idAreaMagazzino) {

        AreaMagazzinoFatturaPA areaMagazzinoFatturaPA = caricaAreaMagazzinoFatturaPA(idAreaMagazzino);
        if (areaMagazzinoFatturaPA != null) {
            try {
                // cancello il file firmato se esiste
                cancellaXMLFirmato(areaMagazzinoFatturaPA.getXmlFattura());

                // cancello tutte le notifiche se esistono
                cancellaAreaNotificheFatturaPA(areaMagazzinoFatturaPA.getId());

                panjeaDAO.delete(areaMagazzinoFatturaPA);
            } catch (Exception e) {
                LOGGER.error("--> errore durante la cancellazione dell'area magazzino fattura PA", e);
                throw new RuntimeException("errore durante la cancellazione dell'area magazzino fattura PA", e);
            }
        }
    }

    /**
     * Cancella tutte le notifiche dell'area magazzino PA.
     *
     * @param idAreaMagazzinoFatturaPA
     *            id area
     */
    private void cancellaAreaNotificheFatturaPA(Integer idAreaMagazzinoFatturaPA) {

        Query query = panjeaDAO.prepareNamedQuery("AreaNotificaFatturaPA.deleteByIdAreaMagazzinoFatturaPa");
        query.setParameter("paramIdAreaMagazzinoFatturaPA", idAreaMagazzinoFatturaPA);

        try {
            panjeaDAO.executeQuery(query);
        } catch (Exception e) {
            LOGGER.error("--> errore durante la cancellazione delle notifiche dell'area magazzino PA", e);
            throw new RuntimeException("errore durante la cancellazione delle notifiche dell'area magazzino PA", e);
        }
    }

    @Override
    public void cancellaXMLFatturaPA(Integer idAreaMagazzino) throws PreferenceNotFoundException {
        LOGGER.debug("--> Enter cancellaXMLFatturaPA");

        AreaMagazzinoFatturaPA areaMagazzinoFatturaPA = caricaAreaMagazzinoFatturaPA(idAreaMagazzino);
        if (areaMagazzinoFatturaPA != null) {

            // se l'xml era firmato vado a cancellarlo
            cancellaXMLFirmato(areaMagazzinoFatturaPA.getXmlFattura());

            areaMagazzinoFatturaPA.getXmlFattura().setXmlFattura(null);
            areaMagazzinoFatturaPA.getXmlFattura().setXmlFileName(null);
            areaMagazzinoFatturaPA.getXmlFattura().setXmlFileNameFirmato(null);
            areaMagazzinoFatturaPA.getXmlFattura().setXmlFirmato(false);
            salvaAreaMagazzinoFatturaPA(areaMagazzinoFatturaPA);
        }

        LOGGER.debug("--> Exit cancellaXMLFatturaPA");
    }

    /**
     * Cancella se esiste l'XML firmato specificato.
     *
     * @param xmlFatturaPA
     *            xml della fattura
     * @throws PreferenceNotFoundException
     *             sollevata se la preferenza per la directory di salvataggio degli allegati non è configurata
     */
    private void cancellaXMLFirmato(XMLFatturaPA xmlFatturaPA) throws PreferenceNotFoundException {

        if (xmlFatturaPA.isXmlFirmato()) {
            File xmlSaveFolder = getXMLFatturaPAFolder();
            File xmlFirmatoFile = new File(
                    xmlSaveFolder.getPath() + File.separator + xmlFatturaPA.getXmlFileNameFirmato());
            if (xmlFirmatoFile.exists()) {
                xmlFirmatoFile.delete();
            }
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @Override
    public AreaMagazzinoFatturaPA caricaAreaMagazzinoFatturaPA(BigInteger identificativoSdI, String xmlFileName) {

        AreaMagazzinoFatturaPA areaMagazzinoFatturaPA = null;

        // cerco subito se esiste un'area magazzino con l'identificativo SdI della notifica
        Query query = panjeaDAO.prepareQuery(
                "select ampa from AreaMagazzinoFatturaPA ampa where ampa.identificativoSDI = :paramIdentificativoSDI");
        query.setParameter("paramIdentificativoSDI", identificativoSdI);
        try {
            areaMagazzinoFatturaPA = (AreaMagazzinoFatturaPA) panjeaDAO.getSingleResult(query);
        } catch (ObjectNotFoundException e) {
            try {
                // area magazzino fattura PA non trovata, la cerco in base al nome del file XML e aggiorno
                // l'identificativo SdI
                query = panjeaDAO.prepareQuery(
                        "select ampa from AreaMagazzinoFatturaPA ampa where ampa.xmlFattura.xmlFileName = :paramXmlFileName");
                query.setParameter("paramXmlFileName", xmlFileName);

                areaMagazzinoFatturaPA = (AreaMagazzinoFatturaPA) panjeaDAO.getSingleResult(query);
            } catch (Exception e1) {
                LOGGER.debug("--> Fattura non trovata per il file " + xmlFileName);
                areaMagazzinoFatturaPA = null;
            }
        } catch (Exception e1) {
            LOGGER.debug("--> Errore durante il caricamento della fattura con identificativo SdI " + identificativoSdI);
            areaMagazzinoFatturaPA = null;
        }

        return areaMagazzinoFatturaPA;
    }

    @Override
    public AreaMagazzinoFatturaPA caricaAreaMagazzinoFatturaPA(Integer idAreaMagazzino) {
        LOGGER.debug("--> Enter caricaAreaMagazzinoFatturaPA");

        Query query = panjeaDAO.prepareNamedQuery("AreaMagazzinoFatturaPA.caricaByIdAreaMagazzino");
        query.setParameter("paramIdAreaMagazzino", idAreaMagazzino);

        AreaMagazzinoFatturaPA areaMagazzinoFatturaPA = null;
        try {
            areaMagazzinoFatturaPA = (AreaMagazzinoFatturaPA) panjeaDAO.getSingleResult(query);
        } catch (ObjectNotFoundException e) {
            // se non trovo una area magazzino fattura pa ne creo una nuova e la salvo
            try {
                AreaMagazzinoLite areaMagazzinoLite = panjeaDAO.load(AreaMagazzinoLite.class, idAreaMagazzino);

                if (areaMagazzinoLite.isFatturaPA()) {
                    areaMagazzinoFatturaPA = new AreaMagazzinoFatturaPA();
                    areaMagazzinoFatturaPA.setAreaMagazzino(areaMagazzinoLite);
                    NotificaFatturaPA notificaFatturaPA = new NotificaFatturaPA();
                    notificaFatturaPA.setStatoFattura(StatoFatturaPA._DI);
                    notificaFatturaPA.setDatiEsito("Documento non ancora spedito al Sistema di Interscambio");
                    areaMagazzinoFatturaPA.setNotificaCorrente(notificaFatturaPA);

                    areaMagazzinoFatturaPA = salvaAreaMagazzinoFatturaPA(areaMagazzinoFatturaPA);

                }
            } catch (Exception e1) {
                LOGGER.error("--> errore durante il caricamento dell'area magazzino " + idAreaMagazzino, e);
                throw new RuntimeException("errore durante il caricamento dell'area magazzino " + idAreaMagazzino, e);
            }
        } catch (Exception e) {
            LOGGER.error("-->Errore durante il caricamento dell'area magazzino fattura PA per il documento "
                    + idAreaMagazzino);
            throw new RuntimeException(
                    "Errore durante il caricamento dell'area magazzino fattura PA per il documento " + idAreaMagazzino);
        }

        return areaMagazzinoFatturaPA;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<AreaMagazzinoFatturaPA> caricaAreaMagazzinoFatturaPANonConservate() {
        LOGGER.debug("--> Enter caricaAreaMagazzinoFatturaPANonConservate");

        Query query = panjeaDAO.prepareQuery(
                "select amfpa from AreaMagazzinoFatturaPA amfpa where amfpa.conservazioneSostitutivaEseguita = false and amfpa.identificativoSDI is not null");

        List<AreaMagazzinoFatturaPA> aree;
        try {
            aree = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            LOGGER.error("--> errore durante la ricerca delle fattue pa per la conservazione sostitutiva", e);
            throw new RuntimeException("errore durante la ricerca delle fattue pa per la conservazione sostitutiva", e);
        }

        LOGGER.debug("--> Exit caricaAreaMagazzinoFatturaPANonConservate");
        return aree;
    }

    @Override
    public AreaNotificaFatturaPA caricaAreaNotificaFatturaPA(Integer id) {
        LOGGER.debug("--> Enter caricaAreaNotificaFatturaPA");

        AreaNotificaFatturaPA areaNotificaFatturaPA;
        try {
            areaNotificaFatturaPA = panjeaDAO.load(AreaNotificaFatturaPA.class, id);

            // nell'area caricata setto anche l'esito trasformato in html tramite il relativo xsl se esiste
            String esitoHTML = XMLtoHTMLTransformer.getHTML(
                    areaNotificaFatturaPA.getNotificaFatturaPA().getStatoFattura(),
                    areaNotificaFatturaPA.getNotificaFatturaPA().getDatiEsito());

            // esito di default utilizzato se l'esito è vuoto o non è possibile ottenere l'html
            String defaultEsitoHTML = "<html>" + StringUtils.replace(
                    StringUtils.defaultString(areaNotificaFatturaPA.getNotificaFatturaPA().getDatiEsito()), "\n",
                    "<br>") + "</html>";

            areaNotificaFatturaPA.getNotificaFatturaPA()
                    .setDatiEsitoHTML(StringUtils.defaultString(esitoHTML, defaultEsitoHTML));
        } catch (Exception e) {
            LOGGER.error("--> errore durante il caricamento dell'area notifica fattura pa", e);
            throw new RuntimeException("errore durante il caricamento dell'area notifica fattura pa", e);
        }

        LOGGER.debug("--> Exit caricaAreaNotificaFatturaPA");
        return areaNotificaFatturaPA;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<AreaNotificaFatturaPADTO> caricaAreaNotificheFatturaPA(Integer idAreaMagazzinoFatturaPA) {
        LOGGER.debug("--> Enter caricaAreaNotificheFatturaPA");

        StringBuilder sb = new StringBuilder();
        sb.append("select notifiche.areaMagazzinoFatturaPA.id as idAreaMagazzinoFatturaPA, ");
        sb.append("           notifiche.id as id, ");
        sb.append("           notifiche.notificaFatturaPA.statoFattura as statoFatturaPA, ");
        sb.append("           notifiche.notificaFatturaPA.data as data ");
        sb.append("from AreaNotificaFatturaPA notifiche ");
        sb.append("where notifiche.areaMagazzinoFatturaPA.id = :paramIdAreaMagazzinoFatturaPA ");
        sb.append("order by notifiche.notificaFatturaPA.data");

        Query query = panjeaDAO.prepareQuery(sb.toString(), AreaNotificaFatturaPADTO.class, null);
        query.setParameter("paramIdAreaMagazzinoFatturaPA", idAreaMagazzinoFatturaPA);

        List<AreaNotificaFatturaPADTO> notifiche;
        try {
            notifiche = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            LOGGER.error("--> errore durante il caricamento delle notifiche dell'area magazzino fattura pa", e);
            throw new RuntimeException("errore durante il caricamento delle notifiche dell'area magazzino fattura pa",
                    e);
        }

        LOGGER.debug("--> Exit caricaAreaNotificheFatturaPA");
        return notifiche;
    }

    @Override
    public IFatturaElettronicaType caricaFatturaElettronicaType(String xmlContent) {

        IFatturaElettronicaType fatturaType = null;

        FatturaElettronicaXMLManager xmlManager = FatturaElettronicaXMLManagerFactory.getXMLManager(xmlContent,
                context);

        fatturaType = xmlManager.caricaFatturaElettronicaType(xmlContent);

        return fatturaType;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<AreaMagazzinoFatturaPA> caricaFatturePAAperte() {
        LOGGER.debug("--> Enter caricaFatturePAAperte");

        List<Integer> statiChiusura = new ArrayList<Integer>();
        for (StatoFatturaPA stato : StatoFatturaPA.getStatiChiusura()) {
            statiChiusura.add(stato.ordinal());
        }

        StringBuilder sb = new StringBuilder(300);
        sb.append("select distinct amfpa.id as id, amfpa.identificativoSDI as identificativoSDI ");
        sb.append("from AreaNotificaFatturaPA anpa inner join anpa.areaMagazzinoFatturaPA amfpa ");
        sb.append("														 inner join amfpa.areaMagazzino am ");
        sb.append("														 inner join am.documento doc ");
        sb.append("where doc.codiceAzienda = :codiceAzienda and ");
        sb.append("  		  amfpa.identificativoSDI is not null and ");
        sb.append("			  anpa.notificaFatturaPA.statoFattura not in (:statiFattura ) ");

        Query query = panjeaDAO.prepareQuery(sb.toString(), AreaMagazzinoFatturaPA.class, null);
        query.setParameter("codiceAzienda", getAzienda());
        query.setParameter("statiFattura", StatoFatturaPA.getStatiChiusura());

        List<AreaMagazzinoFatturaPA> aree = new ArrayList<AreaMagazzinoFatturaPA>();
        try {
            aree = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            LOGGER.error("--> errore durante il caricamento delle fatture PA aperte.", e);
            throw new RuntimeException("errore durante il caricamento delle fatture PA aperte.", e);
        }

        LOGGER.debug("--> Exit caricaFatturePAAperte");
        return aree;
    }

    private void checkDatiEntita(AreaMagazzino areaMagazzino) throws XMLCreationException {

        EntitaLite entita = areaMagazzino.getDocumento().getEntita();
        SedeEntita sedeEntita = areaMagazzino.getDocumento().getSedeEntita();
        if (!entita.isFatturazionePA() || StringUtils.isBlank(sedeEntita.getCodiceUfficioPA())) {
            throw new XMLCreationException(
                    "Non tutti i dati relativi alla pubblica amministrazione per la creazione dell'XML sono presenti sull'entità o sulla sede.",
                    areaMagazzino.getDocumento());
        }
    }

    @Override
    public boolean checkEmailMessageIDNotifica(String messageID) {
        LOGGER.debug("--> Enter checkEmailMessageIDNotifica");

        Query query = panjeaDAO.prepareQuery(
                "select count(notPA.id) from NotificaFatturaPA notPA where notPA.emailMessageID = :paramMessageID");
        query.setParameter("paramMessageID", messageID);

        Long nrMessage = new Long(0);
        try {
            nrMessage = (Long) panjeaDAO.getSingleResult(query);
        } catch (Exception e) {
            LOGGER.error("--> errore durante la verifica del message ID", e);
            throw new RuntimeException("errore durante la verifica del message ID", e);
        }

        LOGGER.debug("--> Exit checkEmailMessageIDNotifica");
        return nrMessage > 0;
    }

    @Override
    public AreaMagazzinoFatturaPA creaXMLFatturaPA(Integer idAreaMagazzino) throws XMLCreationException {

        // Non capisco il perchè ma a volte l'area caricata mi risulta essere un proxy anche se dalle API
        // EntityManager.find ritorna null se l'oggetto non esiste e l'istanza se l'oggetto esiste. Esiste un bug sul
        // bugtraker di Hibernate in cui si dice che a discapito di quello che dicono le API in alcuni casi hibernate
        // può ritornare un proxy con il metodo find sul quale è possibile ottenere i valori dai metodi get.
        //
        // Siccome avere un'ara proxy mi genera degli errori in seguito nella generazione dell'XML, pulisco la sessione
        // e in questo modo ottengo sempre un area inizializzata
        panjeaDAO.getEntityManager().clear();

        AreaMagazzino areaMagazzino = new AreaMagazzino();
        areaMagazzino.setId(idAreaMagazzino);
        areaMagazzino = areaMagazzinoManager.caricaAreaMagazzino(areaMagazzino);

        checkDatiEntita(areaMagazzino);

        AreaMagazzinoFatturaPA areaMagazzinoFatturaPA = caricaAreaMagazzinoFatturaPA(idAreaMagazzino);

        if (areaMagazzinoFatturaPA == null) {
            return areaMagazzinoFatturaPA;
        }

        // se la fattura non è confermata o forzata oppure ha già un xml creato non faccio niente
        if (areaMagazzino.getStatoAreaMagazzino() != StatoAreaMagazzino.CONFERMATO
                && areaMagazzino.getStatoAreaMagazzino() != StatoAreaMagazzino.FORZATO) {
            throw new XMLCreationException(
                    "XML non creato. Confermare il documento prima di poter procedere con la creazione",
                    areaMagazzino.getDocumento());
        }
        if (!StringUtils.isBlank(areaMagazzinoFatturaPA.getXmlFattura().getXmlFattura())) {
            throw new XMLCreationException("Il file XML è già presente quindi non è stato creato.",
                    areaMagazzino.getDocumento());
        }

        FatturaPASettings fatturaPASettings = fatturaPASettingsManager.caricaFatturaPASettings();

        AziendaFatturaPA aziendaPA = aziendaFatturaPAManager.caricaAziendaFatturaPA();

        Trasmissione trasmissione = new Trasmissione();
        trasmissione.setCodiceIdentificativoFiscale(aziendaPA.getCodiceFiscale());
        Nazione nazione = new Nazione();
        nazione.setCodice(aziendaPA.getSedeNazione());
        trasmissione.setNazione(nazione);
        trasmissione.setProgressivoInvio(getProgressivoInvioFattura(areaMagazzinoFatturaPA, fatturaPASettings));
        trasmissione.setCodiceDestinatario(areaMagazzino.getDocumento().getSedeEntita().getCodiceUfficioPA());
        trasmissione.setFormatoTrasmissione(fatturaPASettings.getFormatoTrasmissione());

        FatturaElettronicaXMLManager xmlManager = FatturaElettronicaXMLManagerFactory
                .getXMLManager(trasmissione.getFormatoTrasmissione(), context);

        try {
            String fileXml = xmlManager.getXmlData(trasmissione, aziendaPA, areaMagazzino);

            // se ho un xml precedentemente generato devo copiare tutti i valori non obbligatori presenti
            if (!StringUtils.isBlank(areaMagazzinoFatturaPA.getXmlFattura().getXmlFatturaOriginale())) {
                IFatturaElettronicaType fatturaCreata = caricaFatturaElettronicaType(fileXml);
                IFatturaElettronicaType fatturaPrecedente = caricaFatturaElettronicaType(
                        areaMagazzinoFatturaPA.getXmlFattura().getXmlFatturaOriginale());
                fatturaCreata.copyNotRequiredProperty(fatturaPrecedente);
                areaMagazzinoFatturaPA = creaXMLFatturaPA(idAreaMagazzino, fatturaCreata);
            } else {
                areaMagazzinoFatturaPA = salvaXMLFatturaPA(areaMagazzinoFatturaPA, trasmissione.getProgressivoInvio(),
                        trasmissione.getNazione().getCodice(), trasmissione.getCodiceIdentificativoFiscale(), fileXml);
            }
        } catch (Exception e) {
            throw new XMLCreationException(e.getMessage(), areaMagazzinoFatturaPA.getAreaMagazzino().getDocumento());
        }

        return areaMagazzinoFatturaPA;
    }

    @Override
    public AreaMagazzinoFatturaPA creaXMLFatturaPA(Integer idAreaMagazzino,
            IFatturaElettronicaType fatturaElettronicaType) throws XMLCreationException, PreferenceNotFoundException {

        AreaMagazzinoFatturaPA areaMagazzinoFatturaPA = caricaAreaMagazzinoFatturaPA(idAreaMagazzino);

        if (areaMagazzinoFatturaPA == null) {
            return null;
        }

        FormatoTrasmissioneType formatoTrasmissione = null;
        for (FormatoTrasmissioneType formato : FormatoTrasmissioneType.values()) {
            if (formato.getCodice().equals(fatturaElettronicaType.getVersione())) {
                formatoTrasmissione = formato;
                break;
            }
        }

        FatturaElettronicaXMLManager xmlManager = FatturaElettronicaXMLManagerFactory.getXMLManager(formatoTrasmissione,
                context);

        String progressivoInvio = getProgressivoInvioFattura(areaMagazzinoFatturaPA,
                fatturaPASettingsManager.caricaFatturaPASettings());
        String nazione = fatturaElettronicaType.getDatiTrasmissione().getIdTrasmittente().getIdPaese();
        String codiceIdentificativoFiscale = fatturaElettronicaType.getDatiTrasmissione().getIdTrasmittente()
                .getIdCodice();

        fatturaElettronicaType.getDatiTrasmissione().setProgressivoInvio(progressivoInvio);
        String xmlData = xmlManager.getXmlData(fatturaElettronicaType);

        areaMagazzinoFatturaPA = salvaXMLFatturaPA(areaMagazzinoFatturaPA, progressivoInvio, nazione,
                codiceIdentificativoFiscale, xmlData);

        return areaMagazzinoFatturaPA;
    }

    @Override
    public byte[] downloadXMLFirmato(String fileName) throws PreferenceNotFoundException {

        File xmlFatturaPAFolder = getXMLFatturaPAFolder();
        File xmlFatturaFile = new File(xmlFatturaPAFolder.getPath() + File.separator + fileName);

        byte[] xmlContent = null;
        if (xmlFatturaFile.exists()) {
            try {
                xmlContent = FileUtils.readFileToByteArray(xmlFatturaFile);
            } catch (Exception e) {
                LOGGER.error("--> errore durante la lettura del file XML firmato " + fileName, e);
                throw new GenericException("errore durante la lettura del file XML firmato " + fileName);
            }
        }
        return xmlContent;
    }

    /**
     * @return codice azienda corrente
     */
    private String getAzienda() {
        return ((JecPrincipal) context.getCallerPrincipal()).getCodiceAzienda();
    }

    private String getProgressivoInvioFattura(AreaMagazzinoFatturaPA areaMagazzinoFatturaPA,
            FatturaPASettings fatturaPASettings) throws XMLCreationException {
        String progressivo = "";

        // se avevo già associato un progressivo di invio per l'xml lo uso altrimenti vado a prenderlo dal protocollo
        if (areaMagazzinoFatturaPA.getXmlFattura().getProgressivoInvio() != null) {
            progressivo = areaMagazzinoFatturaPA.getXmlFattura().getProgressivoInvio();
        } else {
            if (!StringUtils.isBlank(fatturaPASettings.getRegistroProtocollo())) {
                // in numero messimo ammesso è di 5 cifre quindi usi 99999 come max value
                progressivo = protocolloGenerator.nextCodice(fatturaPASettings.getRegistroProtocollo(),
                        new Integer(99999));
                progressivo = StringUtils.leftPad(progressivo, 5, "0");
            } else {
                throw new XMLCreationException(
                        "Protocollo per la generazione dell'xml non configurato nelle preferenze.",
                        areaMagazzinoFatturaPA.getAreaMagazzino().getDocumento());
            }
        }

        return progressivo;
    }

    private File getXMLFatturaPAFolder() throws PreferenceNotFoundException {

        String cartellaAllegatiPath = preferenceService.caricaPreference("dirAllegati").getValore();

        File cartellaAllegati = new File(cartellaAllegatiPath);
        if (StringUtils.isBlank(cartellaAllegatiPath) || !cartellaAllegati.isDirectory()
                || !cartellaAllegati.exists()) {
            throw new GenericException(
                    "La cartella impostata per salvare gli allegati non è corretta. Valore impostato: "
                            + cartellaAllegatiPath);
        }

        cartellaAllegatiPath = cartellaAllegatiPath + File.separator + "xmlFatturaPA";

        cartellaAllegati = new File(cartellaAllegatiPath);
        if (!cartellaAllegati.exists()) {
            cartellaAllegati.mkdir();
        }

        return cartellaAllegati;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<AreaMagazzinoFatturaPARicerca> ricercaFatturePA(ParametriRicercaFatturePA parametri) {
        LOGGER.debug("--> Enter ricercaFatturePA");

        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        sb.append("am.id as idAreaMagazzino, ");
        sb.append("doc.codiceAzienda as azienda, ");
        sb.append("am.documento_id as idDocumento, ");
        sb.append("doc.tipo_documento_id as idTipoDocumento, ");
        sb.append("doc.entita_id as idEntita, ");
        sb.append("am.dataRegistrazione as dataRegistrazione, ");
        sb.append("am.statoAreaMagazzino as statoNum, ");
        sb.append("am.tipoAreaMagazzino_id as idTipoAreaMagazzino, ");
        sb.append("tam.tipoMovimento as tipoMovimentoNum, ");
        sb.append("tam.valoriFatturato as valoriFatturato, ");
        sb.append("tipoDoc.tipoEntita as tipoEntitaNum, ");
        sb.append("entDoc.codice as codiceEntita, ");
        sb.append("anag.denominazione as denominazioneEntita, ");
        sb.append("tipoDoc.codice as codiceTipoDocumento, ");
        sb.append("tipoDoc.descrizione as descrizioneTipoDocumento, ");
        sb.append("doc.dataDocumento as dataDocumento, ");
        sb.append("doc.importoInValutaAzienda as totaleDocumentoInValutaAzienda, ");
        sb.append("doc.codiceValuta as codiceValuta, ");
        sb.append("doc.importoInValuta as totaleDocumentoInValuta, ");
        sb.append("doc.tassoDiCambio as totaleDocumentoTassoDiCambio, ");
        sb.append("sedeAnag.id as idSede, ");
        sb.append("sedeAnag.descrizione as descrizioneSede, ");
        sb.append("sedeAnag.indirizzo as indirizzoSede, ");
        sb.append("locSede.descrizione as descrizioneLocalitaSede, ");
        sb.append("sedeEnt.codice as codiceSede, ");
        sb.append("max(amftpa.xmlFileName) as fileXmlFattura, ");
        sb.append("max(amftpa.xmlFileNameFirmato) as fileXmlFatturaFirmato, ");
        sb.append(
                "cast((SUBSTR(max(amftpa.xmlFattura), LOCATE('<ProgressivoInvio>',max(amftpa.xmlFattura))+18,LOCATE('</ProgressivoInvio>',max(amftpa.xmlFattura)) - LOCATE('<ProgressivoInvio>',max(amftpa.xmlFattura))-18)) as char(5)) as progressivoInvio, ");
        sb.append("notificaftpa.statoFattura as statoFatturaPANum, ");
        sb.append("notificaftpa.esitoPositivo as statoFatturaEsitoPositivo, ");
        sb.append("doc.codice as numeroDocumento, ");
        sb.append("doc.codiceOrder as numeroOrderDocumento ");
        sb.append("from maga_area_magazzino am inner join docu_documenti doc on am.documento_id=doc.id ");
        sb.append("					   inner join anag_sedi_entita sedeEnt on doc.sedeEntita_id=sedeEnt.id ");
        sb.append(
                "					   inner join anag_sedi_anagrafica sedeAnag on sedeEnt.sede_anagrafica_id=sedeAnag.id ");
        sb.append("					   left join geog_localita locSede on sedeAnag.localita_id=locSede.id ");
        sb.append("					   inner join anag_entita entDoc on doc.entita_id=entDoc.id ");
        sb.append("					   inner join anag_anagrafica anag on entDoc.anagrafica_id=anag.id ");
        sb.append("					   left join ftpa_area_magazzino amftpa on am.id=amftpa.areaMagazzino_id ");
        sb.append(
                "					   left join ftpa_notifiche_fattura notificaftpa on notificaftpa.id=amftpa.notificaCorrente_id ");
        sb.append("					   inner join docu_tipi_documento tipoDoc on doc.tipo_documento_id=tipoDoc.id ");
        sb.append("					   inner join maga_tipi_area_magazzino tam on am.tipoAreaMagazzino_id=tam.id ");
        sb.append(" where doc.codiceAzienda=:paramCodiceAzienda and entDoc.fatturazionePA=true ");
        sb.append(
                " and tipoDoc.classeTipoDocumento = 'it.eurotn.panjea.anagrafica.classedocumento.impl.ClasseFattura' ");

        if (parametri.getIdAreeDaRicercare() != null && !parametri.getIdAreeDaRicercare().isEmpty()) {
            sb.append(" and am.id in (");
            sb.append(StringUtils.join(parametri.getIdAreeDaRicercare(), ","));
            sb.append(") ");
        }

        if ((parametri.getStatiAreaMagazzino() != null) && (!parametri.getStatiAreaMagazzino().isEmpty())) {
            List<Integer> statiArray = new ArrayList<Integer>();
            for (StatoAreaMagazzino stato : parametri.getStatiAreaMagazzino()) {
                statiArray.add(stato.ordinal());
            }
            sb.append(" and (am.statoAreaMagazzino in (" + StringUtils.join(statiArray, ",") + ")) ");
        }

        if ((parametri.getStatiFatturaPA() != null) && (!parametri.getStatiFatturaPA().isEmpty())) {
            List<Integer> statiArray = new ArrayList<Integer>();
            for (StatoFatturaPA stato : parametri.getStatiFatturaPA()) {
                statiArray.add(stato.ordinal());
            }
            sb.append(" and (notificaftpa.statoFattura in (" + StringUtils.join(statiArray, ",") + ")) ");
        }

        if (parametri.getNumeroDocumentoIniziale() != null && !parametri.getNumeroDocumentoIniziale().isEmpty()) {
            sb.append(" and (doc.codiceOrder >= "
                    + PanjeaEJBUtil.addQuote(parametri.getNumeroDocumentoIniziale().getCodiceOrder()) + ") ");
        }
        if (parametri.getNumeroDocumentoFinale() != null && !parametri.getNumeroDocumentoFinale().isEmpty()) {
            sb.append(" and (doc.codiceOrder <= "
                    + PanjeaEJBUtil.addQuote(parametri.getNumeroDocumentoFinale().getCodiceOrder()) + ") ");
        }
        if (parametri.getAnnoCompetenza() != null && parametri.getAnnoCompetenza() != -1) {
            sb.append(" and am.annoMovimento=" + parametri.getAnnoCompetenza());
        }
        if ((parametri.getEntita() != null) && (parametri.getEntita().getId() != null)) {
            sb.append(" and (entDoc.id = " + parametri.getEntita().getId() + " ) ");
        }

        if (parametri.getDataDocumento().getDataIniziale() != null) {
            sb.append(" and doc.dataDocumento >= ");
            sb.append(
                    PanjeaEJBUtil.addQuote(DateFormatUtils.format(parametri.getDataDocumentoIniziale(), "yyyy-MM-dd")));
        }
        if (parametri.getDataDocumento().getDataFinale() != null) {
            sb.append(" and doc.dataDocumento <=  ");
            sb.append(PanjeaEJBUtil.addQuote(DateFormatUtils.format(parametri.getDataDocumentoFinale(), "yyyy-MM-dd")));
        }
        if (parametri.getDataRegistrazione().getDataIniziale() != null) {
            sb.append(" and am.dataRegistrazione >= ");
            sb.append(PanjeaEJBUtil
                    .addQuote(DateFormatUtils.format(parametri.getDataRegistrazioneIniziale(), "yyyy-MM-dd")));
        }
        if (parametri.getDataRegistrazione().getDataFinale() != null) {
            sb.append(" and am.dataRegistrazione <= ");
            sb.append(PanjeaEJBUtil
                    .addQuote(DateFormatUtils.format(parametri.getDataRegistrazioneFinale(), "yyyy-MM-dd")));
        }

        sb.append(" group by am.id ");

        if (!StringUtils.isBlank(parametri.getProgressivoInvio())) {
            sb.append(" having progressivoInvio = ");
            sb.append(parametri.getProgressivoInvio());
        }

        String[] alias = new String[] { "idAreaMagazzino", "azienda", "idDocumento", "idTipoDocumento", "idEntita",
                "dataRegistrazione", "statoNum", "idTipoAreaMagazzino", "tipoMovimentoNum", "valoriFatturato",
                "tipoEntitaNum", "codiceEntita", "denominazioneEntita", "codiceTipoDocumento",
                "descrizioneTipoDocumento", "dataDocumento", "totaleDocumentoInValutaAzienda", "codiceValuta",
                "totaleDocumentoInValuta", "totaleDocumentoTassoDiCambio", "idSede", "descrizioneSede", "indirizzoSede",
                "descrizioneLocalitaSede", "codiceSede", "fileXmlFattura", "fileXmlFatturaFirmato", "progressivoInvio",
                "statoFatturaEsitoPositivo" };

        Query query = panjeaDAO.prepareSQLQuery(sb.toString(), AreaMagazzinoFatturaPARicerca.class,
                Arrays.asList(alias));
        query.setParameter("paramCodiceAzienda", getAzienda());
        ((SQLQuery) ((QueryImpl) query).getHibernateQuery()).addScalar("statoFatturaPANum", Hibernate.INTEGER);
        ((SQLQuery) ((QueryImpl) query).getHibernateQuery()).addScalar("numeroDocumento", Hibernate.STRING);
        ((SQLQuery) ((QueryImpl) query).getHibernateQuery()).addScalar("numeroOrderDocumento", Hibernate.STRING);

        List<AreaMagazzinoFatturaPARicerca> result = Collections.emptyList();
        try {
            result = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            LOGGER.error("-->errore nella ricerca documento", e);
            throw new RuntimeException(e);
        }

        LOGGER.debug("--> Exit ricercaFatturePA");
        return result;
    }

    @Override
    public AreaMagazzinoFatturaPA salvaAreaMagazzinoFatturaPA(AreaMagazzinoFatturaPA areaMagazzinoFatturaPA) {
        LOGGER.debug("--> Enter salvaAreaMagazzinoFatturaPA");

        // verifico dopo il salvataggio se è cambiata la notifica corrente perchè se lo è vado ad aggiungerla a quelle
        // dell'area
        Integer idNotificaFatturaPAOld = null;

        try {
            if (areaMagazzinoFatturaPA.getId() != null) {
                SQLQuery query = panjeaDAO
                        .prepareNativeQuery("select notificaCorrente_id from ftpa_area_magazzino where id = "
                                + areaMagazzinoFatturaPA.getId());
                idNotificaFatturaPAOld = (Integer) query.uniqueResult();
            }

            areaMagazzinoFatturaPA = panjeaDAO.save(areaMagazzinoFatturaPA);

            if ((idNotificaFatturaPAOld == null && areaMagazzinoFatturaPA.getNotificaCorrente() != null)
                    || (areaMagazzinoFatturaPA.getNotificaCorrente() != null && !Objects.equals(idNotificaFatturaPAOld,
                            areaMagazzinoFatturaPA.getNotificaCorrente().getId()))) {
                AreaNotificaFatturaPA areaNotificaFatturaPA = new AreaNotificaFatturaPA();
                areaNotificaFatturaPA.setAreaMagazzinoFatturaPA(areaMagazzinoFatturaPA);
                areaNotificaFatturaPA.setNotificaFatturaPA(areaMagazzinoFatturaPA.getNotificaCorrente());
                panjeaDAO.save(areaNotificaFatturaPA);

                // se la nuova notifica è di scarto devo assegnare un progressivo diverso per il futuro invio ( quando
                // l'xml verrà rigenerato utilizzerà il nuovo progressivo e aggiornerà il nome del file)
                if (!areaMagazzinoFatturaPA.getNotificaCorrente().isEsitoPositivo()) {
                    FatturaPASettings fatturaPASettings = fatturaPASettingsManager.caricaFatturaPASettings();

                    areaMagazzinoFatturaPA.getXmlFattura().setProgressivoInvio(null);
                    String nuovoProgressivo = getProgressivoInvioFattura(areaMagazzinoFatturaPA, fatturaPASettings);
                    areaMagazzinoFatturaPA.getXmlFattura().setProgressivoInvio(nuovoProgressivo);
                    areaMagazzinoFatturaPA = panjeaDAO.save(areaMagazzinoFatturaPA);
                }
            }
        } catch (Exception e) {
            LOGGER.error("--> errore durante il salvataggio dell'area magazzino fattura PA", e);
            throw new RuntimeException("errore durante il salvataggio dell'area magazzino fattura PA", e);
        }

        LOGGER.debug("--> Exit salvaAreaMagazzinoFatturaPA");
        return areaMagazzinoFatturaPA;
    }

    @Override
    public void salvaFatturaPAComeInviata(Integer idAreaMagazzino) {
        LOGGER.debug("--> Enter salvaFatturaPAComeInviata");

        AreaMagazzinoFatturaPA areaMagazzinoFatturaPA = caricaAreaMagazzinoFatturaPA(idAreaMagazzino);

        NotificaFatturaPA notificaFatturaPA = new NotificaFatturaPA();
        notificaFatturaPA.setStatoFattura(StatoFatturaPA._IN);
        notificaFatturaPA.setDatiEsito("Documento inviato al Sistema di Interscambio. File "
                + areaMagazzinoFatturaPA.getXmlFattura().getXmlFileNameFirmato());
        areaMagazzinoFatturaPA.setNotificaCorrente(notificaFatturaPA);

        salvaAreaMagazzinoFatturaPA(areaMagazzinoFatturaPA);

        LOGGER.debug("--> Exit salvaFatturaPAComeInviata");
    }

    @Override
    public void salvaXMLFatturaFirmato(Integer idAreaMagazzino, byte[] xmlContent, String xmlFileName)
            throws PreferenceNotFoundException {

        File xmlSaveFolder = getXMLFatturaPAFolder();

        AreaMagazzinoFatturaPA areaMagazzinoFatturaPA = caricaAreaMagazzinoFatturaPA(idAreaMagazzino);

        if (areaMagazzinoFatturaPA != null) {
            try {
                FileUtils.writeByteArrayToFile(new File(xmlSaveFolder.getPath() + File.separator + xmlFileName),
                        xmlContent);

                areaMagazzinoFatturaPA.getXmlFattura().setXmlFirmato(true);
                areaMagazzinoFatturaPA.getXmlFattura().setXmlFileNameFirmato(xmlFileName);
            } catch (IOException e) {
                LOGGER.error("--> errore durante il salvataggio del file XML firmato.", e);
                throw new GenericException("errore durante il salvataggio del file XML firmato");
            }
        }
    }

    private AreaMagazzinoFatturaPA salvaXMLFatturaPA(AreaMagazzinoFatturaPA areaMagazzinoFatturaPA,
            String progressivoInvio, String nazione, String codiceIdentificativoFiscale, String xmlContent)
                    throws PreferenceNotFoundException {

        // se il precedente xml era firmato vado a cancellarlo
        cancellaXMLFirmato(areaMagazzinoFatturaPA.getXmlFattura());

        // Rebuild filename.
        String fileName = MessageFormat.format("{0}{1}_{2}.xml",
                new Object[] { nazione, codiceIdentificativoFiscale, progressivoInvio });

        XMLFatturaPA xmlFatturaPA = new XMLFatturaPA();
        xmlFatturaPA.setProgressivoInvio(progressivoInvio);
        xmlFatturaPA.setXmlFattura(xmlContent);
        xmlFatturaPA.setXmlFatturaOriginale(xmlContent);
        xmlFatturaPA.setXmlFileName(fileName);

        areaMagazzinoFatturaPA.setXmlFattura(xmlFatturaPA);
        return salvaAreaMagazzinoFatturaPA(areaMagazzinoFatturaPA);
    }

}
