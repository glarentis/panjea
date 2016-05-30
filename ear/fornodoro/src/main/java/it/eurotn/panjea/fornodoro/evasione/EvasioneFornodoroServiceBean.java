package it.eurotn.panjea.fornodoro.evasione;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Query;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.log4j.Logger;
import org.beanio.BeanReader;
import org.beanio.BeanReaderErrorHandler;
import org.beanio.BeanReaderException;
import org.beanio.StreamFactory;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;
import org.joda.time.DateTime;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.Preference;
import it.eurotn.panjea.anagrafica.service.exception.PreferenceNotFoundException;
import it.eurotn.panjea.anagrafica.service.interfaces.PreferenceService;
import it.eurotn.panjea.fornodoro.FornoDOroCostanti;
import it.eurotn.panjea.fornodoro.FornoDOroImporterMBean;
import it.eurotn.panjea.lotti.domain.Lotto;
import it.eurotn.panjea.lotti.domain.RigaLotto;
import it.eurotn.panjea.lotti.exception.RimanenzaLottiNonValidaException;
import it.eurotn.panjea.lotti.exception.RimanenzaLottoNonValida;
import it.eurotn.panjea.lotti.exception.RimanenzeLottiNonValideException;
import it.eurotn.panjea.lotti.manager.interfaces.LottiManager;
import it.eurotn.panjea.magazzino.domain.FormuleRigaArticoloCalculator;
import it.eurotn.panjea.magazzino.domain.FormuleRigaArticoloEvasioneCalculator;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.magazzino.exception.TotaleDocumentoNonCoerenteException;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoCancellaManager;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoManager;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.RigaMagazzinoManager;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.TipiAreaMagazzinoManager;
import it.eurotn.panjea.magazzino.service.exception.RigheLottiNonValideException;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;
import it.eurotn.panjea.magazzino.util.ParametriCreazioneRigaArticolo;
import it.eurotn.panjea.magazzino.util.ParametriRicercaAreaMagazzino;
import it.eurotn.panjea.mail.service.JbossMailService;
import it.eurotn.panjea.ordini.domain.AttributoRiga;
import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCarico;
import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCaricoLotto;
import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.panjea.parametriricerca.domain.Periodo.TipoPeriodo;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.service.interfaces.PanjeaMessage;
import it.eurotn.util.PanjeaEJBUtil;

/**
 *
 * @author fattazzo
 *
 *         Gli ordini vengono gestiti in queste 3 modalità:<br>
 *         <ol>
 *         <li>Ordini cliente produzione: creazione della missione --> creazione carico lotti
 *         automatico --> evasione manuale</li>
 *         <li>Ordini previsionali: evasione dell'ordine manuale su un carico produzione con cliente
 *         per produzione ( Forno d'oro )</li>
 *         <li>Ordini cliente: evasione manuale</li>
 *         </ol>
 *
 */
@Stateless(name = "Panjea.Fornodoro.Evasione")
@SecurityDomain("PanjeaLoginModule")
@LocalBinding(jndiBinding = "Panjea.Fornodoro.Evasione")
public class EvasioneFornodoroServiceBean implements EvasioneFornodoroService {

    private class FileReaderErrorHandler implements BeanReaderErrorHandler {

        /**
         * Costruttore.
         */
        public FileReaderErrorHandler() {
            super();
        }

        @Override
        public void handleError(BeanReaderException ex) throws Exception {
            if (ex.getRecordContext().getRecordText().getBytes().length == 1
                    && ex.getRecordContext().getRecordText().getBytes()[0] == 26) {
                // salto perchè i file di aton finiscono sempre con una riga che contiene un
                // crattere identificato
                // da 26
                if (logger.isDebugEnabled()) {
                    logger.debug("--> Riga non valida ( carattere ascii di chiusura file ), salto");
                }

                return;
            }

            throw new RuntimeException();
        }
    }

    private static Logger logger = Logger.getLogger(EvasioneFornodoroServiceBean.class);

    @EJB
    private PreferenceService preferenceService;

    @EJB
    private PanjeaMessage panjeaMessage;

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    private TipiAreaMagazzinoManager tipiAreaMagazzinoManager;

    @EJB
    private AreaMagazzinoManager areaMagazzinoManager;

    @EJB
    private AreaMagazzinoCancellaManager areaMagazzinoCancellaManager;

    @EJB
    private LottiManager lottiManager;

    @EJB
    private RigaMagazzinoManager rigaMagazzinoManager;

    @EJB
    private JbossMailService jbossMailService;

    private HashMap<String, RigaDistintaCarico> righeDistintaCaricate;

    /**
     *
     * @param rigaEvasione
     *            rigaevasione con la chiave della riga distinta da caricare.
     * @return rigaDistintaCarico
     */
    private RigaDistintaCarico caricaRigaDistintaCarico(RigaEvasione rigaEvasione) {
        if (righeDistintaCaricate == null) {
            righeDistintaCaricate = new HashMap<String, RigaDistintaCarico>();
        }
        if (!righeDistintaCaricate.containsKey(rigaEvasione.getChiave())) {
            StringBuilder sb = new StringBuilder();
            sb.append("select rd from RigaDistintaCarico rd ");
            sb.append("inner join rd.rigaArticolo ra ");
            sb.append("inner join ra.areaOrdine ao ");
            sb.append("inner join ao.documento doc ");
            sb.append("where doc.codice.codice=:codiceDocumento ");
            sb.append("and ra.ordinamento=:numeroRiga ");
            sb.append("and doc.tipoDocumento.codice=:codiceTipoDocumento ");
            Query query = panjeaDAO.prepareQuery(sb.toString());
            query.setParameter("codiceDocumento", rigaEvasione.getNumeroOrdine().toString());
            query.setParameter("numeroRiga", new Double(rigaEvasione.getNumRiga() * 1000));
            query.setParameter("codiceTipoDocumento", rigaEvasione.getTipoDocumento());
            RigaDistintaCarico rdc = null;
            try {
                rdc = (RigaDistintaCarico) panjeaDAO.getSingleResult(query);
                righeDistintaCaricate.put(rigaEvasione.getChiave(), rdc);
            } catch (DAOException e) {
                // non trovo la riga...la salto
                logger.warn("Non trovo la riga " + rigaEvasione.getNumRiga() + " per l'ordine "
                        + rigaEvasione.getChiave() + " durante l'importazione delle bilance. La salto");
            }
        }
        return righeDistintaCaricate.get(rigaEvasione.getChiave());
    }

    /**
     * Creo un carico per i lotti importati caricando/creando un documento di carico (tipoDocumento
     * letto dalle preference con chiave cosaroTipoDocCarico in data di oggi.
     *
     */
    @SuppressWarnings("unchecked")
    private void creaCaricoLotti() {
        if (righeDistintaCaricate.isEmpty()) {
            return;
        }

        String tipiOrdClientiProd = getPreferenceFor(FornoDOroImporterMBean.FORNODORO_TIPI_ORD_CLIENTI_PRODUZIONE);
        String tipiOrdPrevisionali = getPreferenceFor(FornoDOroImporterMBean.FORNODORO_TIPI_ORD_PREVISIONALI);
        String tipiOrdClienti = getPreferenceFor(FornoDOroImporterMBean.FORNODORO_TIPI_ORD_CLIENTI);

        FormuleRigaArticoloCalculator formuleRigaArticoloCalculator = new FormuleRigaArticoloEvasioneCalculator();
        // Recupero il documento di carico
        AreaMagazzino areaMagazzino = lookupDocumentoCarico();
        List<RigaMagazzino> righePresenti = (List<RigaMagazzino>) rigaMagazzinoManager.getDao()
                .caricaRigheMagazzino(areaMagazzino);
        Map<String, RigaArticolo> righeArticoloDocumentoCarico = new HashMap<String, RigaArticolo>();
        for (RigaMagazzino rigaMagazzino : righePresenti) {
            if (rigaMagazzino instanceof RigaArticolo) {
                RigaArticolo riga = (RigaArticolo) rigaMagazzino;
                righeArticoloDocumentoCarico.put(riga.getArticolo().getCodice(), riga);
            }
        }
        int righeCreate = 0;
        for (RigaDistintaCarico rigaDistintaCarico : righeDistintaCaricate.values()) {
            String codTipoDocRiga = rigaDistintaCarico.getRigaArticolo().getAreaOrdine().getTipoAreaOrdine()
                    .getTipoDocumento().getCodice();
            if (tipiOrdClienti.contains(codTipoDocRiga)) {
                // salto la riga perchè non devo creare il carico
                continue;
            }

            // Cerco la rigaArticolo per il documento di carico. Se non trovo la riga per l'articolo
            // la creo
            RigaArticolo rigaArticolo = righeArticoloDocumentoCarico
                    .get(rigaDistintaCarico.getRigaArticolo().getArticolo().getCodice());
            if (rigaArticolo == null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("--> il documento di carico non ha la riga con articolo "
                            + rigaDistintaCarico.getArticolo().getCodice());
                }
                rigaArticolo = creaRigaArticolo(areaMagazzino, rigaDistintaCarico);
                righeArticoloDocumentoCarico.put(rigaArticolo.getArticolo().getCodice(), rigaArticolo);
            } else {
                // Ricarico la rigaArticolo perchè potrebbe essere una rigaDistinta e solamente con
                // il DAO posso crearla
                // e caricare i suoi componenti.
                if (!rigaArticolo.isNew()) {
                    rigaArticolo = (RigaArticolo) rigaMagazzinoManager.getDao(rigaArticolo)
                            .caricaRigaMagazzino(rigaArticolo);
                    righeArticoloDocumentoCarico.put(rigaArticolo.getArticolo().getCodice(), rigaArticolo);
                }
                if (logger.isDebugEnabled()) {
                    logger.debug("--> Trovata la riga nel doc. di carico con articolo "
                            + rigaDistintaCarico.getArticolo().getCodice() + " con id " + rigaArticolo.getId());
                }
            }

            // Ciclo per i lotti della riga
            int numDecimali = 6;
            if (rigaArticolo.getArticolo().getNumeroDecimaliQta() != null) {
                numDecimali = rigaArticolo.getArticolo().getNumeroDecimaliQta();
            }
            for (RigaDistintaCaricoLotto rigaLottoDistinta : rigaDistintaCarico.getRigheDistintaLotto()) {
                RigaLotto rigaLotto = rigaArticolo.getRigaLotto(rigaLottoDistinta.getCodiceLotto(),
                        rigaLottoDistinta.getDataScadenza());
                if (rigaLotto == null) {
                    rigaLotto = creaRigaLotto(rigaArticolo, rigaLottoDistinta);
                }
                // Modifico la qta
                rigaLotto.setQuantita(PanjeaEJBUtil
                        .roundToDecimals(rigaLotto.getQuantita() + rigaLottoDistinta.getQuantita(), numDecimali));
                rigaArticolo.setQta(PanjeaEJBUtil
                        .roundToDecimals((rigaArticolo.getQta() + rigaLottoDistinta.getQuantita()), numDecimali));
            }
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
                    righeArticoloDocumentoCarico.put(rigaArticolo.getArticolo().getCodice(), rigaArticolo);
                }
                righeCreate++;
            } catch (RigheLottiNonValideException lnve) {
                logger.error("-->errore. Lotti non validi", lnve);
                throw new RuntimeException(lnve.getHTMLMessage(), lnve);
            } catch (RimanenzaLottiNonValidaException rnve) {
                logger.error("-->errore. Lotti non validi", rnve);
                throw new RuntimeException(rnve.getHTMLMessage(), rnve);
            } catch (Exception e) {
                if (e.getCause() instanceof RimanenzeLottiNonValideException) {
                    RimanenzeLottiNonValideException rnve = (RimanenzeLottiNonValideException) e.getCause();
                    logger.error("-->errore. Lotti non validi", rnve);
                    StringBuilder sb = new StringBuilder("Rimanenze non valide per lo scarico dei componenti ");
                    for (RimanenzaLottoNonValida rimanenzaLottoNonValida : rnve.getRimanenzeLottoNonValide()) {
                        sb.append(rimanenzaLottoNonValida.getArticolo().getCodice());
                        sb.append(" - ");
                    }
                    throw new RuntimeException(sb.toString(), rnve);
                }
                logger.error("--> Errore nel creare la riga di carico per la rigaDistinta" + rigaArticolo, e);
                throw new RuntimeException("Errore nel creare la riga di carico per la rigaDistinta" + rigaArticolo, e);
            }
        }

        // se devo creare il documento allora procedo con la validazione altrimenti lo cancello
        try {
            if (righeCreate > 0) {
                logger.debug("--> valido l'area di magazzino");
                areaMagazzinoManager.validaRigheMagazzino(areaMagazzino, null, false, true);
                logger.debug("--> area di magazzino validata");
            } else {
                areaMagazzinoCancellaManager.cancellaAreaMagazzino(areaMagazzino, true, true);
            }
        } catch (TotaleDocumentoNonCoerenteException e) {
            logger.error("-->errore nel validare le righe nel documento di carico lotti", e);
            throw new RuntimeException("Errore nel validare le righe nel documento di carico lotti", e);
        } catch (Exception e) {
            throw new RuntimeException("Errore durante il salvataggio del documento di carico", e);
        }
    }

    /**
     * @param tam
     *            tipo area magazzino da associare al documento.
     * @return crea un documento di carico per i lotti in data ordierna.
     */
    private AreaMagazzino creaDocumentoDiCarico(TipoAreaMagazzino tam) {
        if (tam.getDepositoOrigine() == null || tam.getDepositoDestinazione() == null) {
            throw new RuntimeException(
                    "Impostare il deposito di origine e destinazione nel TipoAreaMagazzino con codice "
                            + tam.getTipoDocumento().getCodice());
        }

        try {
            if (logger.isDebugEnabled()) {
                logger.debug("--> Creo il documento di carico");
            }
            Documento documento = new Documento();
            AreaMagazzino am = new AreaMagazzino();
            documento.setDataDocumento(new DateTime().toDate());
            documento.setImposta(new Importo("EUR", BigDecimal.ONE));
            documento.setTotale(new Importo("EUR", BigDecimal.ONE));
            documento.setTipoDocumento(tam.getTipoDocumento());
            am.setDocumento(documento);
            am.setTipoAreaMagazzino(tam);
            am.setAnnoMovimento(new DateTime().getYear());
            am.setDataRegistrazione(new DateTime().toDate());
            am.setDepositoOrigine(tam.getDepositoOrigine());
            am.setDepositoDestinazione(tam.getDepositoDestinazione());
            am.setAggiornaCostoUltimo(tam.isAggiornaCostoUltimo());
            am.setDocumentoCreatoDaAreaMagazzino(true);
            am = areaMagazzinoManager.salvaAreaMagazzino(am, true);
            if (logger.isDebugEnabled()) {
                logger.debug("--> Documento di carico creato con id " + am.getId());
            }
            return am;
        } catch (Exception e) {
            logger.error("--> Errore nel creare il documento per il carico dei lotti di cosato", e);
            throw new RuntimeException("Errore nel creare il documento per il carico dei lotti", e);
        }
    }

    /**
     *
     * @param rigaLottoDistinta
     *            dati per il lotto da creare
     * @return lotto creato.
     */
    private Lotto creaLotto(RigaDistintaCaricoLotto rigaLottoDistinta) {
        Lotto lotto = new Lotto();
        lotto.setArticolo(rigaLottoDistinta.getRigaDistintaCarico().getArticolo());
        lotto.setCodice(rigaLottoDistinta.getCodiceLotto());
        lotto.setDataScadenza(rigaLottoDistinta.getDataScadenza());
        lotto = lottiManager.salvaLotto(lotto);
        return lotto;
    }

    /**
     *
     * @param areaMagazzino
     *            areaMagazzino da associare alal riga
     * @param rigaDistintaCarico
     *            distinta di carico con i dati per la riga
     * @return rigaArticolo aggiunta all'area di magazzino.
     */
    private RigaArticolo creaRigaArticolo(AreaMagazzino areaMagazzino, RigaDistintaCarico rigaDistintaCarico) {
        logger.debug("--> Enter creaRigaArticolo con rigaDistintaCarico " + rigaDistintaCarico);
        ParametriCreazioneRigaArticolo parametriCreazione = new ParametriCreazioneRigaArticolo();
        parametriCreazione.setIdArticolo(rigaDistintaCarico.getRigaArticolo().getArticolo().getId());
        parametriCreazione.setTipoMovimento(areaMagazzino.getTipoAreaMagazzino().getTipoMovimento());
        parametriCreazione.setProvenienzaPrezzo(areaMagazzino.getTipoAreaMagazzino().getProvenienzaPrezzo());
        parametriCreazione.setImporto(areaMagazzino.getDocumento().getTotale().clone());
        parametriCreazione.setStrategiaTotalizzazioneDocumento(
                areaMagazzino.getTipoAreaMagazzino().getStrategiaTotalizzazioneDocumento());
        RigaArticolo rigaArticolo = (RigaArticolo) rigaMagazzinoManager.getDao(parametriCreazione)
                .creaRigaArticolo(parametriCreazione);
        rigaArticolo.setQta(0.0);
        rigaArticolo.setAreaMagazzino(areaMagazzino);
        // setto i vari attributi a zero per non avere attributo obbligatori nulli
        for (it.eurotn.panjea.magazzino.domain.AttributoRiga attributoRiga : rigaArticolo.getAttributi()) {
            attributoRiga.setValore("0");
        }
        logger.debug("--> Exit creaRigaArticolo");
        return rigaArticolo;
    }

    /**
     *
     * @param rigaArticolo
     *            rigaArticolo da associare alla rigaLotto
     * @param rigaLottoDistinta
     *            riga lotto associata alla riga distinta con i dati per creare la riga lotto
     * @return rigaLotto creata .Se non viene trovato il lotto si crea anche questo.
     */
    private RigaLotto creaRigaLotto(RigaArticolo rigaArticolo, RigaDistintaCaricoLotto rigaLottoDistinta) {
        // Cerco il lotto
        logger.debug("--> Enter creaRigaLotto");
        Lotto lotto = lottiManager.caricaLotto(rigaLottoDistinta.getCodiceLotto(), rigaLottoDistinta.getDataScadenza(),
                rigaArticolo.getArticolo());
        if (lotto == null) {
            lotto = creaLotto(rigaLottoDistinta);
        }
        RigaLotto rigaLotto = new RigaLotto();
        rigaLotto.setLotto(lotto);
        rigaLotto.setQuantita(0.0);
        rigaLotto.setRigaArticolo(rigaArticolo);
        rigaArticolo.getRigheLotto().add(rigaLotto);
        logger.debug("--> Exit creaRigaLotto");
        return rigaLotto;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @Override
    public void evadi(File fileSemaforo) {
        // Verifico la presenza del file semaforo
        if (fileSemaforo.exists()) {
            // Verifico che esista il file
            String pathFileEvasione = fileSemaforo.toString().replaceFirst("RESI.SEM", "RESI.TXT");
            File fileEvasione = new File(pathFileEvasione);
            if (fileEvasione.exists()) {
                String backupDestination = "/backup/";
                try {
                    evadiFile(fileEvasione);

                    panjeaMessage.send("Lette " + righeDistintaCaricate.size() + " righe dalle bilance",
                            new String[] { "evasione" }, 8, PanjeaMessage.MESSAGE_GENERIC_SELECTOR);
                } catch (RuntimeException ex) {
                    backupDestination = "/error/";

                    jbossMailService.send("Errore evasione", ex.getMessage(), new File[] { fileEvasione });

                    panjeaMessage.send(ex.getMessage() + "<BR>" + fileEvasione.getName(), new String[] { "evasione" },
                            8, PanjeaMessage.MESSAGE_GENERIC_SELECTOR);
                } finally {
                    // CANCELLO IL FILE SEMAFORO
                    fileSemaforo.delete();
                }

                try {
                    File dirDest = new File(fileEvasione.getParentFile().getPath() + backupDestination);
                    if (!dirDest.exists()) {
                        dirDest.mkdir();
                    }
                    FileUtils.moveFile(fileEvasione, new File(fileEvasione.getParentFile().getPath() + backupDestination
                            + Calendar.getInstance().getTimeInMillis() + ".txt"));
                } catch (IOException e) {
                    panjeaMessage.send("-->errore nel copiare il file delle bilance nella cartella di backup",
                            new String[] { "evasione" }, 8, PanjeaMessage.MESSAGE_GENERIC_SELECTOR);
                    logger.error("-->errore nel copiare il file delle bilace nella cartella di backup "
                            + fileEvasione.getParentFile().getPath(), e);
                }
            }
        }
    }

    /**
     *
     * @param fileEvasione
     *            file da leggere per l'evasione
     */
    private void evadiFile(File fileEvasione) {
        BeanReader in = null;

        String tipiOrdClientiProd = getPreferenceFor(FornoDOroImporterMBean.FORNODORO_TIPI_ORD_CLIENTI_PRODUZIONE);
        String tipiOrdPrevisionali = getPreferenceFor(FornoDOroImporterMBean.FORNODORO_TIPI_ORD_PREVISIONALI);

        boolean creaCarico = false;
        List<RigaDistintaCarico> righeDistintaDaCandellare = new ArrayList<RigaDistintaCarico>();

        try {
            StreamFactory factory = getStreamFactory("EVASIONE.xml");
            if (factory == null) {
                return;
            }
            righeDistintaCaricate = new HashMap<String, RigaDistintaCarico>();
            in = factory.createReader("evasione", fileEvasione);
            in.setErrorHandler(new FileReaderErrorHandler());

            RigaEvasione rigaEvasione = null;
            // Leggo le righe dal file e assegno le qta di evasione alle righeDistintaCarico
            Map<String, String> righeMancanti = new HashMap<String, String>();
            while ((rigaEvasione = (RigaEvasione) in.read()) != null) {
                RigaDistintaCarico rdc = caricaRigaDistintaCarico(rigaEvasione);
                if (rdc == null) {
                    if (rigaEvasione.getTipoRecord() == 4) {
                        logger.warn("Non trovo la riga distinta carico. Riga nel file RESI.txt " + rigaEvasione);
                        if (righeMancanti.containsKey(rigaEvasione.getNumeroOrdine().toString())) {
                            String item = righeMancanti.get(rigaEvasione.getNumeroOrdine().toString());
                            righeMancanti.put(rigaEvasione.getNumeroOrdine().toString(),
                                    item + "," + rigaEvasione.getNumRiga());
                        } else {
                            righeMancanti.put(rigaEvasione.getNumeroOrdine().toString(),
                                    rigaEvasione.getNumRiga() + "");
                        }
                    }
                    continue;
                }

                if (rigaEvasione.getTipoRecord() == 4) {
                    String codTipoDocRiga = rdc.getRigaArticolo().getAreaOrdine().getTipoAreaOrdine().getTipoDocumento()
                            .getCodice();

                    // creo il carico solo se c'è almeno una riga distinta di un tipo ordine
                    // contenuto in
                    // FORNODORO_TIPI_ORD_CLIENTI_PRODUZIONE
                    creaCarico = creaCarico || (tipiOrdClientiProd.contains(codTipoDocRiga))|| (tipiOrdPrevisionali.contains(codTipoDocRiga));

                    // se il tipo documento è contenuto in FORNODORO_TIPI_ORD_PREVISIONALI non creo
                    // il documento di
                    // evasione e cancello de righe distinta
                    if (tipiOrdPrevisionali.contains(codTipoDocRiga)) {
                     righeDistintaDaCandellare.add(rdc);
                    }

                    Double qtaFile = 0.0;
                    // se si tratta di un articolo a peso variabile ( um KG ) aggiorno la quantità
                    if ("KG".equals(rdc.getArticolo().getUnitaMisura().getCodice())) {
                        qtaFile = rigaEvasione.getQta();
                    } else {
                        // altrimenti la quantità è uguale ai pezzi evasi
                        qtaFile = Double.valueOf(rigaEvasione.getPezziEvasi());
                    }
                    rdc.setQtaEvasa(ObjectUtils.defaultIfNull(rdc.getQtaEvasa(), 0.0) + qtaFile);

                    // aggiorno gli attributi di pezzi e colli
                    for (AttributoRiga attributoRiga : rdc.getRigaArticolo().getAttributi()) {
                        if (FornoDOroCostanti.COLLI_ATTRIBUTO.equals(attributoRiga.getTipoAttributo().getCodice())) {
                            attributoRiga.setValore(rigaEvasione.getColliEvasi());
                        } else if (FornoDOroCostanti.PEZZI_ATTRIBUTO
                                .equals(attributoRiga.getTipoAttributo().getCodice())) {
                            attributoRiga.setValore(rigaEvasione.getPezziEvasi());
                        }
                    }

                    rdc.setForzata(rdc.getQtaDaEvadere().compareTo(rdc.getQtaEvasa()) != 0);
                    panjeaDAO.save(rdc.getRigaArticolo());
                    panjeaDAO.save(rdc);
                } else if (rigaEvasione.getTipoRecord() == 1 || rigaEvasione.getTipoRecord() == 2) {
                    // Creo le righe di evasione per i lotti
                    RigaDistintaCaricoLotto rigaDistintaCaricoLotto = new RigaDistintaCaricoLotto();
                    // se si tratta di un articolo a peso variabile ( um KG ) aggiorno la quantità
                    if ("KG".equals(rdc.getArticolo().getUnitaMisura().getCodice())) {
                        rigaDistintaCaricoLotto.setQuantita(rigaEvasione.getQta());
                    } else {
                        // altrimenti la quantità è uguale ai pezzi evasi
                        rigaDistintaCaricoLotto.setQuantita(Double.valueOf(rigaEvasione.getPezziEvasi()));
                    }
                    rigaDistintaCaricoLotto.setCodiceLotto(rigaEvasione.getNumLotto());
                    rigaDistintaCaricoLotto.setDataScadenza(rigaEvasione.getDataScadenza());
                    rigaDistintaCaricoLotto.setRigaDistintaCarico(rdc);
                    panjeaDAO.save(rigaDistintaCaricoLotto);
                }
            }

            if (!righeMancanti.isEmpty()) {
                StringBuilder sb = new StringBuilder("Mancano delle missioni in panjea.");
                for (Entry<String, String> item : righeMancanti.entrySet()) {
                    sb.append("ORDINE ").append(item.getKey()).append(" RIGHE ").append(item.getValue())
                            .append("<br\\>");
                }
                throw new RuntimeException("Righe missioni mancanti " + sb.toString());
            }

            // creo il carico automatico solo se c'è almeno una riga con tipo documento contenuto in
            // FORNODORO_TIPI_ORD_CLIENTI_PRODUZIONE o FORNODORO_TIPI_ORD_PREVISIONALI
            if (creaCarico) {
                creaCaricoLotti();
            }

            // se il tipo documento è contenuto in FORNODORO_TIPI_ORD_PREVISIONALI non creo il
            // documento di evasione e
            // cancello de righe distinta
            if (!righeDistintaDaCandellare.isEmpty()) {
                for (RigaDistintaCarico rigaDistintaCarico : righeDistintaDaCandellare) {
                    panjeaDAO.delete(rigaDistintaCarico);
                }
            }

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            logger.error("-->errore nell'importare le righe da file delle bilance", e);
            throw new RuntimeException("Errore nell'importare le righe dal file delle bilance", e);
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

    /**
     * @param key
     *            la chiave da ricercare nelle preference
     * @return il valore associato alla chiave ricercata
     */
    @SuppressWarnings("unchecked")
    private String getPreferenceFor(String key) {
        String value = "";
        javax.persistence.Query query = panjeaDAO.prepareNamedQuery("Preference.caricaPerChiave");
        query.setParameter("paramChiave", key);
        List<Preference> preferences = null;
        try {
            preferences = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            logger.error("--> errore ricerca preference con key " + key, e);
            throw new RuntimeException("Errore nella ricerca della chiave " + key, e);
        }
        if (preferences.size() > 0) {
            value = preferences.get(0).getValore();
        } else {
            throw new RuntimeException("Nelle preferenze generali manca la chiave " + key);
        }
        return value;
    }

    /**
     * @param templateFileName
     *            nome del file di template con il quale costruire il factoryStream
     * @return factoryStream per l'import. Null se non esiste il file di template o il file di
     *         template non è corretto. <br/>
     *         Eventuali errori vendono spediti sulla coda degli errori
     */
    protected StreamFactory getStreamFactory(String templateFileName) {
        // Recupero la cartella dove sono i file di template
        // Recupero la cartella dove esportare i file
        Preference preference = null;
        StreamFactory factory = null;
        try {
            preference = preferenceService.caricaPreference(FornoDOroImporterMBean.FORNODORO_DIR_TEMPLATE);
            String templateFile = new StringBuilder(preference.getValore()).append(templateFileName).toString();
            factory = StreamFactory.newInstance();
            factory.load(templateFile);
        } catch (PreferenceNotFoundException e) {
            throw new RuntimeException(
                    "Nelle preferenze generali manca la chiave " + FornoDOroImporterMBean.FORNODORO_DIR_TEMPLATE);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Errore nel caricare il template per l'importazione del file associato alla chiave "
                            + FornoDOroImporterMBean.FORNODORO_DIR_TEMPLATE);
        }
        return factory;
    }

    /**
     *
     * @return documento per creare il carico.
     */
    private AreaMagazzino lookupDocumentoCarico() {
        // Carico il codice dalle settings.
        String codiceTipoDocumento = getPreferenceFor(FornoDOroImporterMBean.FORNODORO_TIPO_DOC_CARICO);

        ParametriRicercaAreaMagazzino parametriRicerca = new ParametriRicercaAreaMagazzino();
        // Setto il tipo documento
        List<TipoAreaMagazzino> tipiAreaMagazzino = tipiAreaMagazzinoManager
                .caricaTipiAreaMagazzino("tipoDocumento.codice", null, false);
        for (TipoAreaMagazzino tipoAreaMagazzino : tipiAreaMagazzino) {
            if (tipoAreaMagazzino.getTipoDocumento().getCodice().equals(codiceTipoDocumento)) {
                parametriRicerca.getTipiAreaMagazzino().add(tipoAreaMagazzino);
                break;
            }
        }

        if (parametriRicerca.getTipiAreaMagazzino().size() != 1) {
            logger.error("-->errore nel trovare il tipo documento settato nei settings " + codiceTipoDocumento);
            throw new IllegalArgumentException(
                    "-->errore nel trovare il tipo documento settato nei settings " + codiceTipoDocumento);
        }
        // Setto la data
        Periodo oggi = new Periodo();
        oggi.setTipoPeriodo(TipoPeriodo.OGGI);
        parametriRicerca.setDataDocumento(oggi);
        if (logger.isDebugEnabled()) {
            logger.debug("--> Cerco il documento di carico con tipoDocumento " + codiceTipoDocumento + " in data "
                    + oggi.getDataIniziale() + " - " + oggi.getDataFinale());
        }

        // Cerco il documento di carico. Se trovo più risultati prendo il primo. Se non ne trovo
        // creo il documento
        List<AreaMagazzinoRicerca> areeMagazzino = areaMagazzinoManager.ricercaAreeMagazzino(parametriRicerca);
        AreaMagazzino areaMagazzinoCarico = null;
        if (areeMagazzino.size() == 0) {
            // TipoAreaMagazzino non ha tutte le variabili caricate. Le carico
            TipoAreaMagazzino tam = parametriRicerca.getTipiAreaMagazzino().iterator().next();
            try {
                tam = panjeaDAO.load(TipoAreaMagazzino.class, tam.getId());
                areaMagazzinoCarico = creaDocumentoDiCarico(tam);
            } catch (ObjectNotFoundException e) {
                logger.error("-->errore non trovo il tam con id " + tam.getId(), e);
                throw new RuntimeException(e);
            }
        } else {
            areaMagazzinoCarico = new AreaMagazzino();
            areaMagazzinoCarico.setId(areeMagazzino.get(0).getIdAreaMagazzino());
            if (logger.isDebugEnabled()) {
                logger.debug("--> Area magazzino documento di carico trovato. Id " + areaMagazzinoCarico.getId());
            }
            areaMagazzinoCarico = areaMagazzinoManager.caricaAreaMagazzino(areaMagazzinoCarico);
        }
        return areaMagazzinoCarico;
    }
}
