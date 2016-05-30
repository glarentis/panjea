package it.eurotn.panjea.cosaro.produzione;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Query;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.beanio.BeanReader;
import org.beanio.BeanReaderErrorHandler;
import org.beanio.BeanReaderException;
import org.beanio.StreamFactory;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.cosaro.CosaroSettings;
import it.eurotn.panjea.cosaro.CosaroSettingsBean;
import it.eurotn.panjea.cosaro.RigaProduzione;
import it.eurotn.panjea.lotti.domain.Lotto;
import it.eurotn.panjea.lotti.domain.RigaLotto;
import it.eurotn.panjea.lotti.exception.RimanenzaLottiNonValidaException;
import it.eurotn.panjea.lotti.exception.RimanenzaLottoNonValida;
import it.eurotn.panjea.lotti.exception.RimanenzeLottiNonValideException;
import it.eurotn.panjea.lotti.manager.interfaces.LottiManager;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.FormuleRigaArticoloCalculator;
import it.eurotn.panjea.magazzino.domain.FormuleRigaArticoloEvasioneCalculator;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.magazzino.exception.TotaleDocumentoNonCoerenteException;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoManager;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.RigaMagazzinoManager;
import it.eurotn.panjea.magazzino.service.exception.RigheLottiNonValideException;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;
import it.eurotn.panjea.magazzino.util.ParametriCreazioneRigaArticolo;
import it.eurotn.panjea.magazzino.util.ParametriRicercaAreaMagazzino;
import it.eurotn.panjea.mail.service.JbossMailService;
import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.panjea.parametriricerca.domain.Periodo.TipoPeriodo;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.service.interfaces.PanjeaMessage;
import it.eurotn.util.PanjeaEJBUtil;

@Stateless(name = "Panjea.Cosaro.Produzione")
@SecurityDomain("PanjeaLoginModule")
@LocalBinding(jndiBinding = "Panjea.Cosaro.Produzione")
public class ProduzioneCosaroServiceBean implements ProduzioneCosaroService {

    private class FileReaderErrorHandler implements BeanReaderErrorHandler {

        /**
         * Costruttore.
         */
        public FileReaderErrorHandler() {
            super();
        }

        @Override
        public void handleError(BeanReaderException ex) throws Exception {
            if (ex.getRecordContext().getRecordText().equals("")
                    || (ex.getRecordContext().getRecordText().getBytes().length == 1
                            && ex.getRecordContext().getRecordText().getBytes()[0] == 26)) {
                // salto perchè i file di aton finiscono sempre con una riga che contiene un
                // crattere identificato
                // da 26
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("--> Riga non valida ( carattere ascii di chiusura file ), salto");
                }

                return;
            }

            throw new RuntimeException(ex);
        }
    }

    private static final Logger LOGGER = Logger.getLogger(ProduzioneCosaroServiceBean.class);

    @EJB
    private PanjeaMessage panjeaMessage;

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    private AreaMagazzinoManager areaMagazzinoManager;

    @EJB
    private LottiManager lottiManager;

    @EJB
    private RigaMagazzinoManager rigaMagazzinoManager;

    @EJB
    private CosaroSettings cosaroSettings;

    @EJB
    private JbossMailService jbossMailService;

    private List<RigaProduzione> righeProduzione;

    private Map<RigaProduzione, String> righeNonImportate;

    /**
     * @param tam
     *            tipo area magazzino da associare al documento.
     * @param dataProduzione
     *            la data per il documento
     * @return crea un documento di proiduzione.
     */
    private AreaMagazzino creaDocumentoDiProduzione(Date dataProduzione, TipoAreaMagazzino tam) {
        if (tam.getDepositoOrigine() == null || tam.getDepositoDestinazione() == null) {
            throw new RuntimeException(
                    "Impostare il deposito di origine e destinazione nel TipoAreaMagazzino con codice "
                            + tam.getTipoDocumento().getCodice());
        }

        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("--> Creo il documento di carico");
            }
            // passo dal calendar per avere ora e minuti di creazione oltre che a evitare di
            // chiamare il metodo
            // deprecato Date.getYear()
            Calendar cal = Calendar.getInstance();
            cal.setTime(dataProduzione);

            Documento documento = new Documento();

            documento.setDataDocumento(cal.getTime());
            documento.setImposta(new Importo("EUR", BigDecimal.ONE));
            documento.setTotale(new Importo("EUR", BigDecimal.ONE));
            documento.setTipoDocumento(tam.getTipoDocumento());
            AreaMagazzino am = new AreaMagazzino();
            am.setDocumento(documento);
            am.setTipoAreaMagazzino(tam);
            am.setAnnoMovimento(cal.get(Calendar.YEAR));
            am.setDataRegistrazione(cal.getTime());
            am.setDepositoOrigine(tam.getDepositoOrigine());
            am.setDepositoDestinazione(tam.getDepositoDestinazione());
            am.setAggiornaCostoUltimo(tam.isAggiornaCostoUltimo());
            am.setDocumentoCreatoDaAreaMagazzino(true);
            am = areaMagazzinoManager.salvaAreaMagazzino(am, true);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("--> Documento di carico creato con id " + am.getId());
            }
            return am;
        } catch (Exception e) {
            LOGGER.error("--> Errore nel creare il documento di produzione", e);
            throw new RuntimeException("Errore nel creare il documento di produzione", e);
        }
    }

    /**
     *
     * @param rigaLottoDistinta
     *            dati per il lotto da creare
     * @param articolo
     *            l'articolo associato al lotto
     * @return lotto creato.
     */
    private Lotto creaLotto(RigaProduzione rigaLottoDistinta, ArticoloLite articolo) {
        Lotto lotto = new Lotto();
        lotto.setArticolo(articolo);
        lotto.setCodice(rigaLottoDistinta.getLotto());
        lotto.setDataScadenza(rigaLottoDistinta.getDataScadenza());
        lotto = lottiManager.salvaLotto(lotto);
        return lotto;
    }

    /**
     *
     * @param areaMagazzino
     *            areaMagazzino da associare alal riga
     * @param rigaProduzione
     *            carico produzione con i dati per la riga
     * @return rigaArticolo aggiunta all'area di magazzino.
     */
    private RigaArticolo creaRigaArticolo(AreaMagazzino areaMagazzino, RigaProduzione rigaProduzione) {
        LOGGER.debug("--> Enter creaRigaArticolo con rigaDistintaCarico " + rigaProduzione);

        Query query = panjeaDAO
                .prepareQuery("select id from Articolo where codice='" + rigaProduzione.getCodice() + "'");
        Integer idArticolo = null;
        try {
            idArticolo = (Integer) panjeaDAO.getSingleResult(query);
        } catch (ObjectNotFoundException e) {
            // se non c'è un'articolo non importo la riga, ma il documento viene creato
            LOGGER.error("--> Errore, articolo con codice " + rigaProduzione.getCodice() + " non trovato", e);
            righeNonImportate.put(rigaProduzione, "Articolo con codice " + rigaProduzione.getCodice() + " non trovato");
            return null;
        } catch (Exception e) {
            LOGGER.error("--> Errore, nel caricare l'articolo con codice " + rigaProduzione.getCodice(), e);
            righeNonImportate.put(rigaProduzione,
                    "Errore nel caricare l'articolo con codice " + rigaProduzione.getCodice());
            return null;
        }

        ParametriCreazioneRigaArticolo parametriCreazione = new ParametriCreazioneRigaArticolo();
        parametriCreazione.setIdArticolo(idArticolo);
        parametriCreazione.setTipoMovimento(areaMagazzino.getTipoAreaMagazzino().getTipoMovimento());
        parametriCreazione.setProvenienzaPrezzo(areaMagazzino.getTipoAreaMagazzino().getProvenienzaPrezzo());
        parametriCreazione.setImporto(areaMagazzino.getDocumento().getTotale().clone());

        RigaArticolo rigaArticolo = (RigaArticolo) rigaMagazzinoManager.getDao(parametriCreazione)
                .creaRigaArticolo(parametriCreazione);
        rigaArticolo.setQta(0.0);
        rigaArticolo.setAreaMagazzino(areaMagazzino);

        // setto i vari attributi a zero per non avere attributo obbligatori nulli
        for (it.eurotn.panjea.magazzino.domain.AttributoRiga attributoRiga : rigaArticolo.getAttributi()) {
            attributoRiga.setValore("0");
        }
        LOGGER.debug("--> Exit creaRigaArticolo");
        return rigaArticolo;
    }

    /**
     *
     * @param rigaArticolo
     *            rigaArticolo da associare alla rigaLotto
     * @param rigaProduzione
     *            riga lotto associata alla riga distinta con i dati per creare la riga lotto
     * @return rigaLotto creata .Se non viene trovato il lotto si crea anche questo.
     */
    private RigaLotto creaRigaLotto(RigaArticolo rigaArticolo, RigaProduzione rigaProduzione) {
        // Cerco il lotto
        LOGGER.debug("--> Enter creaRigaLotto");
        Lotto lotto = lottiManager.caricaLotto(rigaProduzione.getLotto(), rigaProduzione.getDataScadenza(),
                rigaArticolo.getArticolo());
        if (lotto == null) {
            lotto = creaLotto(rigaProduzione, rigaArticolo.getArticolo());
        }
        RigaLotto rigaLotto = new RigaLotto();
        rigaLotto.setLotto(lotto);
        rigaLotto.setQuantita(0.0);
        rigaLotto.setRigaArticolo(rigaArticolo);
        rigaArticolo.getRigheLotto().add(rigaLotto);
        LOGGER.debug("--> Exit creaRigaLotto");
        return rigaLotto;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @Override
    public void importa(File fileProduzione) {
        // verifico qui se importare il carico produzione o meno a seconda del valore della chiave
        // COSARO_IMPORTA_PRODUZIONE_GAMMA_MEAT o comunque se la chiave esiste
        if (fileProduzione.exists() && fileProduzione.isFile() && cosaroSettings.isGammaMeatEnable()) {
            String backupDestination = "/backup/";
            try {
                importaFile(fileProduzione);

                StringBuilder message = new StringBuilder();
                message = message.append("Lette ");
                message = message.append(righeProduzione.size());
                message = message.append(" righe da GammaMeat.<BR><BR>");
                message = message.append(fileProduzione.getName());

                if (righeNonImportate.size() > 0) {
                    message = message.append("<BR>Righe non importate: ");
                    message = message.append(righeNonImportate.size());
                    message = message.append("<BR><BR>");
                    for (RigaProduzione rigaProduzione : righeNonImportate.keySet()) {
                        String errorCause = righeNonImportate.get(rigaProduzione);
                        message = message.append(rigaProduzione.toString());
                        message = message.append("<BR>");
                        message = message.append(errorCause);
                        message = message.append("<BR>");
                        message = message.append("<BR>");
                    }

                    jbossMailService.send("Importazione incompleta", message.toString(), new File[] { fileProduzione });

                    backupDestination = "/error/";
                }
                // else {
                // jbossMailService.send("Importazione riuscita", message.toString(), new File[] {
                // fileProduzione });
                // }

                panjeaMessage.send(message.toString(), new String[] { "evasione" }, 8,
                        PanjeaMessage.MESSAGE_GENERIC_SELECTOR);
            } catch (Exception ex) {
                LOGGER.error("--> Errore nell'importazione del file " + fileProduzione.getName());
                backupDestination = "/error/";

                jbossMailService.send("Errore importazione produzione", ex.getMessage(), new File[] { fileProduzione });

                panjeaMessage.send(ex.getMessage() + "<BR>" + fileProduzione.getName(), new String[] { "evasione" }, 8,
                        PanjeaMessage.MESSAGE_GENERIC_SELECTOR);
            }

            try {
                File dirDest = new File(fileProduzione.getParentFile().getPath() + backupDestination);
                if (!dirDest.exists()) {
                    dirDest.mkdir();
                }
                FileUtils.moveFile(fileProduzione, new File(fileProduzione.getParentFile().getPath() + backupDestination
                        + Calendar.getInstance().getTimeInMillis() + "_" + fileProduzione.getName()));
            } catch (IOException e) {
                panjeaMessage.send("-->errore nel copiare il file delle bilance nella cartella di backup",
                        new String[] { "evasione" }, 8, PanjeaMessage.MESSAGE_GENERIC_SELECTOR);
                LOGGER.error("-->errore nel copiare il file delle bilace nella cartella di backup "
                        + fileProduzione.getParentFile().getPath(), e);
            }
        }
    }

    /**
     * Carico/creo un documento di produzione (tipoDocumento letto dalle preference con chiave
     * cosaroTipoDocProduzione.
     *
     * @param dataProduzione
     *            la data per il documento
     */
    private void importaCaricoProduzione(Date dataProduzione) {
        if (righeProduzione.isEmpty()) {
            return;
        }
        FormuleRigaArticoloCalculator formuleRigaArticoloCalculator = new FormuleRigaArticoloEvasioneCalculator();
        // Recupero il documento di carico
        AreaMagazzino areaMagazzino = lookupDocumentoProduzione(dataProduzione);
        @SuppressWarnings("unchecked")
        List<RigaMagazzino> righePresenti = (List<RigaMagazzino>) rigaMagazzinoManager.getDao()
                .caricaRigheMagazzino(areaMagazzino);
        Map<String, RigaArticolo> righeArticoloDocumentoProduzione = new HashMap<String, RigaArticolo>();
        for (RigaMagazzino rigaMagazzino : righePresenti) {
            if (rigaMagazzino instanceof RigaArticolo) {
                RigaArticolo riga = (RigaArticolo) rigaMagazzino;
                righeArticoloDocumentoProduzione.put(riga.getArticolo().getCodice(), riga);
            }
        }
        for (RigaProduzione rigaProduzione : righeProduzione) {
            // Cerco la rigaArticolo per il documento di carico. Se non trovo la riga per l'articolo
            // la creo
            RigaArticolo rigaArticolo = righeArticoloDocumentoProduzione.get(rigaProduzione.getCodice());
            if (rigaArticolo == null) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(
                            "--> il documento di carico non ha la riga con articolo " + rigaProduzione.getCodice());
                }
                rigaArticolo = creaRigaArticolo(areaMagazzino, rigaProduzione);
                // se non trovo l'articolo torno la riga null, quindi passo alla prossima riga
                if (rigaArticolo == null) {
                    continue;
                }
                righeArticoloDocumentoProduzione.put(rigaArticolo.getArticolo().getCodice(), rigaArticolo);
            } else {
                // Ricarico la rigaArticolo perchè potrebbe essere una rigaDistinta e solamente con
                // il DAO posso crearla
                // e caricare i suoi componenti.
                if (!rigaArticolo.isNew()) {
                    rigaArticolo = (RigaArticolo) rigaMagazzinoManager.getDao(rigaArticolo)
                            .caricaRigaMagazzino(rigaArticolo);
                    righeArticoloDocumentoProduzione.put(rigaArticolo.getArticolo().getCodice(), rigaArticolo);
                }
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("--> Trovata la riga nel doc. di carico con articolo " + rigaProduzione.getCodice()
                            + " con id " + rigaArticolo.getId());
                }
            }

            // Modifico gli attributi confezioni. L'attributo in questo caso di carico produzione
            // è l'attributo pezzi nell'ordine che ho copiato nell'evasione sull'attributo
            // confezioni perchè
            // dalla bilacia posso leggere O pezzi O colli ed in caso dei colli avrei perso il
            // valore dei pezzi
            // . Chiaramente devo sommare al valore che ho già sulla riga
            it.eurotn.panjea.magazzino.domain.AttributoRiga confezioniPresentiAttr = rigaArticolo
                    .getAttributo(CosaroSettingsBean.CONFEZIONI_ATTRIBUTO);
            if (confezioniPresentiAttr != null) {
                int numeroConfezioniPresenti = Integer.parseInt(confezioniPresentiAttr.getValore());
                // Confezioni sull'ordine
                int confezioniProduzione = rigaProduzione.getConfezioni();

                // Aggiorno la riga di magazzino del carico che sto aggiornando/creando
                confezioniPresentiAttr
                        .setValore(new Integer(numeroConfezioniPresenti + confezioniProduzione).toString());
                rigaArticolo.getAttributi().remove(confezioniPresentiAttr);
                rigaArticolo.getAttributi().add(confezioniPresentiAttr);
            }

            // Ciclo per i lotti della riga
            int numDecimali = 6;
            if (rigaArticolo.getArticolo().getNumeroDecimaliQta() != null) {
                numDecimali = rigaArticolo.getArticolo().getNumeroDecimaliQta();
            }

            RigaLotto rigaLotto = rigaArticolo.getRigaLotto(rigaProduzione.getLotto(),
                    rigaProduzione.getDataScadenza());
            if (rigaLotto == null) {
                rigaLotto = creaRigaLotto(rigaArticolo, rigaProduzione);
            }
            // Modifico la qta
            rigaLotto.setQuantita(
                    PanjeaEJBUtil.roundToDecimals(rigaLotto.getQuantita() + rigaProduzione.getQta(), numDecimali));
            rigaArticolo.setQta(
                    PanjeaEJBUtil.roundToDecimals((rigaArticolo.getQta() + rigaProduzione.getQta()), numDecimali));

            // Rigalotto è per riferimento quindi è modificata anche la riga lotto in
            // rigaArticolo.getRigheLotto (fa
            // schifo ma non ho tempo per cam
            try {
                rigaArticolo = (RigaArticolo) formuleRigaArticoloCalculator.calcola(rigaArticolo);
                // la bilancia spedisce anche righe senza dettaglio e con qta a zero. Queste non le
                // salvo.
                if (rigaArticolo.getQta() != 0.0) {
                    rigaArticolo = (RigaArticolo) rigaMagazzinoManager.getDao(rigaArticolo)
                            .salvaRigaMagazzino(rigaArticolo);
                    righeArticoloDocumentoProduzione.put(rigaArticolo.getArticolo().getCodice(), rigaArticolo);
                }
            } catch (RigheLottiNonValideException lnve) {
                LOGGER.error("-->errore. Lotti non validi", lnve);
                righeNonImportate.put(rigaProduzione, lnve.getHTMLMessage());
            } catch (RimanenzaLottiNonValidaException rnve) {
                LOGGER.error("-->errore. Lotti non validi", rnve);
                righeNonImportate.put(rigaProduzione, rnve.getHTMLMessage());
            } catch (Exception e) {
                if (e.getCause() instanceof RimanenzeLottiNonValideException) {
                    RimanenzeLottiNonValideException rnve = (RimanenzeLottiNonValideException) e.getCause();
                    LOGGER.error("-->errore. Lotti non validi", rnve);
                    StringBuilder sb = new StringBuilder("Rimanenze non valide per lo scarico dei componenti ");
                    for (RimanenzaLottoNonValida rimanenzaLottoNonValida : rnve.getRimanenzeLottoNonValide()) {
                        sb.append(rimanenzaLottoNonValida.getArticolo().getCodice());
                        sb.append(" - ");
                    }
                    righeNonImportate.put(rigaProduzione, sb.toString());
                }
                LOGGER.error("-->errore nel creare la riga di carico per la rigaDistinta" + rigaArticolo, e);
                righeNonImportate.put(rigaProduzione,
                        "Errore nel salvare la riga con articolo " + rigaProduzione.getCodice());
            }
        }
        try {
            LOGGER.debug("--> valido l'area di magazzino");
            areaMagazzinoManager.validaRigheMagazzino(areaMagazzino, null, false, true);
            LOGGER.debug("--> area di magazzino validata");
        } catch (TotaleDocumentoNonCoerenteException e) {
            LOGGER.error("--> Errore nel validare le righe nel documento di produzione", e);
            throw new RuntimeException("Errore nel validare le righe nel documento di produzione", e);
        }
    }

    /**
     * @param fileProduzione
     *            file da leggere per la produzione
     */
    private void importaFile(File fileProduzione) {
        BeanReader in = null;
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("--> Importo file " + fileProduzione.getAbsolutePath());
        }

        if (!fileProduzione.canRead()) {
            LOGGER.debug("--> Non posso leggere " + fileProduzione.getAbsolutePath());
        }
        try {
            StreamFactory factory = cosaroSettings.getStreamTemplate("PRODUZIONE.xml");
            if (factory == null) {
                return;
            }
            in = factory.createReader("produzione", fileProduzione);
            in.setErrorHandler(new FileReaderErrorHandler());

            righeProduzione = new ArrayList<RigaProduzione>();
            righeNonImportate = new HashMap<RigaProduzione, String>();

            RigaProduzione rigaProduzione = null;
            while ((rigaProduzione = (RigaProduzione) in.read()) != null) {
                righeProduzione.add(rigaProduzione);
            }

            String dataProduzioneString = fileProduzione.getName();
            Date dataProduzione = new SimpleDateFormat("yyyyMMdd").parse(dataProduzioneString);
            importaCaricoProduzione(dataProduzione);
        } catch (Exception e) {
            LOGGER.error("--> Errore nell'importare le righe dal file PROD di GammaMeat", e);
            throw new RuntimeException("Errore nell'importare le righe dal file PROD di GammaMeat", e);
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

    /**
     * @param dataProduzione
     *            la data del documento di produzione
     * @return documento per creare il carico.
     */
    private AreaMagazzino lookupDocumentoProduzione(Date dataProduzione) {
        ParametriRicercaAreaMagazzino parametriRicerca = new ParametriRicercaAreaMagazzino();
        // Setto il tipo documento
        parametriRicerca.getTipiAreaMagazzino().add(cosaroSettings.caricaTamProduzione());

        // Setto la data
        Periodo giornoProduzione = new Periodo();
        giornoProduzione.setTipoPeriodo(TipoPeriodo.DATE);
        giornoProduzione.setDataIniziale(dataProduzione);
        giornoProduzione.setDataFinale(dataProduzione);
        parametriRicerca.setDataDocumento(giornoProduzione);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("--> Cerco il documento di carico con tipoDocumento "
                    + parametriRicerca.getTipiAreaMagazzino().iterator().next().getTipoDocumento().getCodice()
                    + " in data " + giornoProduzione.getDataIniziale() + " - " + giornoProduzione.getDataFinale());
        }

        // Cerco il documento di carico. Se trovo più risultati prendo il primo. Se non ne trovo
        // creo il documento
        List<AreaMagazzinoRicerca> areeMagazzino = areaMagazzinoManager.ricercaAreeMagazzino(parametriRicerca);
        AreaMagazzino areaMagazzinoProduzione = null;
        if (areeMagazzino.size() == 0) {
            // TipoAreaMagazzino non ha tutte le variabili caricate. Le carico
            TipoAreaMagazzino tam = parametriRicerca.getTipiAreaMagazzino().iterator().next();
            try {
                tam = panjeaDAO.load(TipoAreaMagazzino.class, tam.getId());
            } catch (ObjectNotFoundException e) {
                LOGGER.error("-->errore non trovo il tam con id " + tam.getId(), e);
                throw new RuntimeException(e);
            }
            areaMagazzinoProduzione = creaDocumentoDiProduzione(dataProduzione, tam);
        } else {
            areaMagazzinoProduzione = new AreaMagazzino();
            areaMagazzinoProduzione.setId(areeMagazzino.get(0).getIdAreaMagazzino());
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("--> Area magazzino documento di carico trovato. Id " + areaMagazzinoProduzione.getId());
            }
            areaMagazzinoProduzione = areaMagazzinoManager.caricaAreaMagazzino(areaMagazzinoProduzione);
        }
        return areaMagazzinoProduzione;
    }

}
