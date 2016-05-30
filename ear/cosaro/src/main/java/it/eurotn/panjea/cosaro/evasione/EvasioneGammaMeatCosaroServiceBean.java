package it.eurotn.panjea.cosaro.evasione;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.math.BigDecimal;
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
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.beanio.BeanReader;
import org.beanio.BeanReaderErrorHandlerSupport;
import org.beanio.StreamFactory;
import org.beanio.UnidentifiedRecordException;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;
import org.joda.time.DateTime;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.cosaro.CosaroSettings;
import it.eurotn.panjea.cosaro.CosaroSettingsBean;
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

@Stateless(name = "Panjea.Cosaro.GammaMeatEvasione")
@SecurityDomain("PanjeaLoginModule")
@LocalBinding(jndiBinding = "Panjea.Cosaro.GammaMeatEvasione")
public class EvasioneGammaMeatCosaroServiceBean implements EvasioneCosaroService {

    private static Logger logger = Logger.getLogger(EvasioneGammaMeatCosaroServiceBean.class);

    @EJB
    private CosaroSettings cosaroSettings;

    @EJB
    private PanjeaMessage panjeaMessage;

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    private TipiAreaMagazzinoManager tipiAreaMagazzinoManager;

    @EJB
    private AreaMagazzinoManager areaMagazzinoManager;

    @EJB
    private LottiManager lottiManager;

    @EJB
    private RigaMagazzinoManager rigaMagazzinoManager;

    @EJB
    private JbossMailService jbossMailService;

    private HashMap<Integer, RigaDistintaCarico> righeDistintaCaricate;

    /**
     *
     * @param rigaEvasione
     *            rigaevasione con la chiave della riga distinta da caricare.
     * @return rigaDistintaCarico
     */
    private RigaDistintaCarico caricaRigaDistintaCarico(RigaEvasioneLottoGammaMeat rigaEvasione) {
        if (righeDistintaCaricate == null) {
            righeDistintaCaricate = new HashMap<Integer, RigaDistintaCarico>();
        }
        if (!righeDistintaCaricate.containsKey(rigaEvasione.getIdRigaDistinta())) {
            StringBuilder sb = new StringBuilder();
            sb.append("select rd from RigaDistintaCarico rd ");
            sb.append("inner join rd.rigaArticolo ra ");
            sb.append("inner join ra.areaOrdine ao ");
            sb.append("inner join ao.documento doc ");
            sb.append("where rd.id=:idRiga");
            Query query = panjeaDAO.prepareQuery(sb.toString());
            query.setParameter("idRiga", rigaEvasione.getIdRigaDistinta());
            RigaDistintaCarico rdc = null;
            try {
                rdc = (RigaDistintaCarico) panjeaDAO.getSingleResult(query);
                righeDistintaCaricate.put(rigaEvasione.getIdRigaDistinta(), rdc);
            } catch (DAOException e) {
                // non trovo la riga...la salto
                logger.warn("Non trovo la riga " + rigaEvasione.getIdRigaDistinta() + " per l'ordine "
                        + rigaEvasione.getIdRigaDistinta() + " durante l'importazione delle bilance. La salto");
            }
        }
        return righeDistintaCaricate.get(rigaEvasione.getIdRigaDistinta());
    }

    /**
     * Creo un carico per i lotti importati caricando/creando un documento di carico (tipoDocumento letto dalle
     * preference con chiave cosaroTipoDocCarico in data di oggi.
     *
     */
    @SuppressWarnings("unchecked")
    private void creaCaricoLotti() {
        if (righeDistintaCaricate.isEmpty()) {
            return;
        }
        // Elaboro solamente se ho determinati articoli
        boolean caricoProduzione = false;
        for (RigaDistintaCarico rigaDistintaCarico : righeDistintaCaricate.values()) {
            if ("a001".equals(rigaDistintaCarico.getRigaArticolo().getArticolo().getCodice())) {
                caricoProduzione = true;
                break;
            }
        }
        if (!caricoProduzione) {
            return;
        }

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
        for (RigaDistintaCarico rigaDistintaCarico : righeDistintaCaricate.values()) {
            // Cerco la rigaArticolo per il documento di carico. Se non trovo la riga per l'articolo
            // la creo
            RigaArticolo rigaArticolo = righeArticoloDocumentoCarico
                    .get(rigaDistintaCarico.getRigaArticolo().getArticolo().getCodice());
            if (!"a001".equals(rigaDistintaCarico.getRigaArticolo().getArticolo().getCodice())) {
                continue;
            }
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
                AttributoRiga confezioniEvase = rigaDistintaCarico.getRigaArticolo()
                        .getAttributo(CosaroSettingsBean.CONFEZIONI_ATTRIBUTO);
                if (confezioniEvase != null) {
                    int numeroPezzi = Integer.parseInt(confezioniEvase.getValore());
                    // Aggiorno la riga di magazzino del carico che sto aggiornando/creando
                    confezioniPresentiAttr.setValore(new Integer(numeroConfezioniPresenti + numeroPezzi).toString());
                    rigaArticolo.getAttributi().remove(confezioniPresentiAttr);
                    rigaArticolo.getAttributi().add(confezioniPresentiAttr);
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
        try {
            logger.debug("--> valido l'area di magazzino");
            areaMagazzinoManager.validaRigheMagazzino(areaMagazzino, null, false, true);
            logger.debug("--> area di magazzino validata");
        } catch (TotaleDocumentoNonCoerenteException e) {
            logger.error("-->errore nel validare le righe nel documento di carico lotti", e);
            throw new RuntimeException("Errore nel validare le righe nel documento di carico lotti", e);
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
    public void evadi(File folderImportazione) {
        if (folderImportazione.exists()) {
            // Verifico che esista il file
            File[] filesImportazione = folderImportazione.listFiles((FilenameFilter) new SuffixFileFilter("-DDT.csv"));
            if (filesImportazione == null) {
                return;
            }
            for (File fileEvasione : filesImportazione) {
                String backupDestination = "/backup/";
                try {
                    evadiFile(fileEvasione);
                    panjeaMessage.send("Lette " + righeDistintaCaricate.size() + " righe da gammaMeat",
                            new String[] { "evasione" }, 8, PanjeaMessage.MESSAGE_GENERIC_SELECTOR);
                } catch (RuntimeException ex) {
                    backupDestination = "/error/";

                    jbossMailService.send("Errore evasione", ex.getMessage(), new File[] { fileEvasione });

                    panjeaMessage.send(ex.getMessage() + "<BR>" + fileEvasione.getName(), new String[] { "evasione" },
                            8, PanjeaMessage.MESSAGE_GENERIC_SELECTOR);
                }

                try {
                    File dirDest = new File(fileEvasione.getParentFile().getPath() + backupDestination);
                    if (!dirDest.exists()) {
                        dirDest.mkdir();
                    }
                    FileUtils.moveFile(fileEvasione, new File(
                            fileEvasione.getParentFile().getPath() + backupDestination + fileEvasione.getName()));
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
        try {
            StreamFactory factory = cosaroSettings.getStreamTemplate("EVASIONEGAMMAMEAT.xml");
            if (factory == null) {
                return;
            }
            righeDistintaCaricate = new HashMap<Integer, RigaDistintaCarico>();
            in = factory.createReader("evasione", fileEvasione);
            in.setErrorHandler(new BeanReaderErrorHandlerSupport() {
                @Override
                public void unidentifiedRecord(UnidentifiedRecordException ex) throws Exception {
                    if (logger.isDebugEnabled()) {
                        logger.debug("--> Pazienza");
                    }
                }
            });

            RigaEvasioneLottoGammaMeat rigaEvasione = null;
            // Leggo le righe dal file e assegno le qta di evasione alle righeDistintaCarico
            Map<String, String> righeMancanti = new HashMap<String, String>();
            Object rigaLetta;
            while ((rigaLetta = in.read()) != null) {
                if (!(rigaLetta instanceof RigaEvasioneLottoGammaMeat)) {
                    continue;
                }
                rigaEvasione = (RigaEvasioneLottoGammaMeat) rigaLetta;
                RigaDistintaCarico rdc = caricaRigaDistintaCarico(rigaEvasione);
                if (rdc == null) {
                    logger.warn("Non trovo la riga distinta carico. Riga nel file " + fileEvasione.getAbsolutePath()
                            + " : " + rigaEvasione);
                    if (righeMancanti.containsKey(rigaEvasione.getNumeroOrdine().toString())) {
                        String item = righeMancanti.get(rigaEvasione.getNumeroOrdine().toString());
                        righeMancanti.put(rigaEvasione.getNumeroOrdine().toString(),
                                item + "," + rigaEvasione.getCodiceArticolo());
                    } else {
                        righeMancanti.put(rigaEvasione.getNumeroOrdine().toString(), rigaEvasione.getCodiceArticolo());
                    }

                    continue;
                }

                if (!StringUtils.isEmpty(rigaEvasione.getCodiceArticoloSostitutivo())) {
                    RigaDistintaCarico rigaDistintaCaricoSostitutiva = PanjeaEJBUtil.cloneObject(rdc);
                    rigaDistintaCaricoSostitutiva.setId(null);
                    rigaDistintaCaricoSostitutiva.setVersion(null);
                    rigaDistintaCaricoSostitutiva.setMoltiplicatoreQta(1.0);
                    Query query = panjeaDAO.prepareQuery("select a from ArticoloLite a where a.codice='"
                            + rigaEvasione.getCodiceArticoloSostitutivo() + "'");
                    ArticoloLite articoloLite = (ArticoloLite) panjeaDAO.getResultList(query).get(0);
                    rigaDistintaCaricoSostitutiva.setArticolo(articoloLite);
                    rdc = rigaDistintaCaricoSostitutiva;
                }
                rdc.setQtaEvasa(ObjectUtils.defaultIfNull(rdc.getQtaEvasa(), 0.0) + rigaEvasione.getQuantita());
                // forzo solamente se la qta < 1
                // negli altri casi la qta è sempre maggiore di 1 che è la qta predefinita
                // durante l'importazione di ordini coop o unicomm
                rdc.setForzata(Boolean.getBoolean(rigaEvasione.getForzato()));

                for (AttributoRiga attributoRiga : rdc.getRigaArticolo().getAttributi()) {
                    if (!CosaroSettingsBean.COLLI_ATTRIBUTO.equals(attributoRiga.getTipoAttributo().getCodice())) {
                        attributoRiga.setValore(rigaEvasione.getColli());
                    } else if (CosaroSettingsBean.CONFEZIONI_ATTRIBUTO
                            .equals(attributoRiga.getTipoAttributo().getCodice())) {
                        // Il valore delle confezioni sono sempre i pezzi evasi
                        attributoRiga.setValore(rigaEvasione.getConfezioni());
                    }
                }

                panjeaDAO.save(rdc.getRigaArticolo());
                panjeaDAO.save(rdc);
                // Creo le righe di evasione per i lotti
                RigaDistintaCaricoLotto rigaDistintaCaricoLotto = new RigaDistintaCaricoLotto();
                rigaDistintaCaricoLotto.setQuantita(rigaEvasione.getQuantita());
                rigaDistintaCaricoLotto.setCodiceLotto(rigaEvasione.getCodiceLotto());
                rigaDistintaCaricoLotto.setDataScadenza(rigaEvasione.getDataScadenza());
                rigaDistintaCaricoLotto.setRigaDistintaCarico(rdc);
                panjeaDAO.save(rigaDistintaCaricoLotto);
            }

            if (!righeMancanti.isEmpty()) {
                StringBuilder sb = new StringBuilder("Mancano delle missioni in panjea.");
                for (Entry<String, String> item : righeMancanti.entrySet()) {
                    sb.append("ORDINE ").append(item.getKey()).append(" RIGHE ").append(item.getValue())
                            .append("<br\\>");
                }
                throw new RuntimeException("Righe missioni mancanti " + sb.toString());
            }
            creaCaricoLotti();

        } catch (RuntimeException e) {
            logger.error("-->errore ", e);
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
     *
     * @return documento per creare il carico.
     */
    private AreaMagazzino lookupDocumentoCarico() {
        // Carico il codice dalle settings.

        ParametriRicercaAreaMagazzino parametriRicerca = new ParametriRicercaAreaMagazzino();
        parametriRicerca.getTipiAreaMagazzino().add(cosaroSettings.caricaTamProduzione());
        // Setto la data
        Periodo oggi = new Periodo();
        oggi.setTipoPeriodo(TipoPeriodo.OGGI);
        parametriRicerca.setDataDocumento(oggi);

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
