package it.eurotn.panjea.ordini.manager.documento.evasione;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.proxy.HibernateProxy;
import org.jboss.annotation.IgnoreDependency;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GroupingList;
import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.domain.CambioValuta;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.manager.interfaces.ValutaManager;
import it.eurotn.panjea.anagrafica.service.exception.CambioNonPresenteException;
import it.eurotn.panjea.conai.domain.RigaConaiComponente;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.lotti.domain.Lotto;
import it.eurotn.panjea.lotti.domain.RigaLotto;
import it.eurotn.panjea.lotti.exception.EvasioneLottiException;
import it.eurotn.panjea.lotti.exception.LottiException;
import it.eurotn.panjea.lotti.exception.LottoNonPresenteException;
import it.eurotn.panjea.lotti.exception.RimanenzaLottiNonValidaException;
import it.eurotn.panjea.lotti.manager.interfaces.LottiManager;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.AttributoArticolo;
import it.eurotn.panjea.magazzino.domain.AttributoRiga;
import it.eurotn.panjea.magazzino.domain.ConfigurazioneDistinta;
import it.eurotn.panjea.magazzino.domain.DatiGenerazione;
import it.eurotn.panjea.magazzino.domain.DatiGenerazione.TipoGenerazione;
import it.eurotn.panjea.magazzino.domain.FormuleRigaArticoloCalculator;
import it.eurotn.panjea.magazzino.domain.FormuleRigaArticoloCalculator.FormulaCalculatorClosure;
import it.eurotn.panjea.magazzino.domain.FormuleRigaArticoloEvasioneCalculator;
import it.eurotn.panjea.magazzino.domain.IRigaArticoloDocumento;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.RigaArticoloDistinta;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.domain.RigaTestata;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino.StatoAreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.TipoMovimento;
import it.eurotn.panjea.magazzino.exception.FormuleTipoAttributoException;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoManager;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.RigaMagazzinoManager;
import it.eurotn.panjea.magazzino.manager.interfaces.MagazzinoSettingsManager;
import it.eurotn.panjea.magazzino.manager.interfaces.SediMagazzinoManager;
import it.eurotn.panjea.magazzino.service.exception.DocumentiEsistentiPerAreaMagazzinoException;
import it.eurotn.panjea.magazzino.service.exception.DocumentoAssenteAvvisaException;
import it.eurotn.panjea.magazzino.service.exception.DocumentoAssenteBloccaException;
import it.eurotn.panjea.magazzino.service.exception.QtaLottiMaggioreException;
import it.eurotn.panjea.magazzino.service.exception.RigheLottiNonValideException;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.panjea.magazzino.util.ParametriCreazioneRigaArticolo;
import it.eurotn.panjea.ordini.domain.RigaOrdine;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCarico;
import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCaricoLotto;
import it.eurotn.panjea.ordini.exception.TipoAreaPartitaDestinazioneRichiestaException;
import it.eurotn.panjea.ordini.manager.documento.evasione.interfaces.DistintaCaricoManager;
import it.eurotn.panjea.ordini.manager.documento.evasione.interfaces.EvasioneGenerator;
import it.eurotn.panjea.ordini.manager.documento.interfaces.RigaOrdineManager;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.partite.domain.TipoAreaPartita;
import it.eurotn.panjea.partite.domain.TipoAreaPartita.TipoOperazione;
import it.eurotn.panjea.partite.manager.interfaces.TipiAreaPartitaManager;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;
import it.eurotn.util.KeyFromValueProvider;
import it.eurotn.util.PanjeaEJBUtil;

@Stateless(name = "Panjea.EvasioneGenerator")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.EvasioneGenerator")
public class EvasioneGeneratorBean implements EvasioneGenerator {

    private static final Logger LOGGER = Logger.getLogger(EvasioneGeneratorBean.class);

    @Resource
    private SessionContext sessionContext;

    @EJB
    private MagazzinoSettingsManager magazzinoSettingsManager;

    @EJB
    private TipiAreaPartitaManager tipiAreaPartitaManager;

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    private ValutaManager valutaManager;

    @EJB
    private RigaOrdineManager rigaOrdineManager;

    @EJB
    private SediMagazzinoManager sediMagazzinoManager;

    @EJB
    private LottiManager lottiManager;

    @EJB
    private RigaMagazzinoManager rigaMagazzinoManager;

    @EJB
    @IgnoreDependency
    private DistintaCaricoManager distintaCaricoManager;

    @EJB
    private AreaMagazzinoManager areaMagazzinoManager;

    /**
     * Aggiunge se necessario le righe conai componente alla riga articolo.
     *
     * @param rigaArticoloMagazzino
     *            riga articolo
     * @return riga articolo modificata
     */
    private RigaArticolo aggiungiRigheConaiComponente(RigaArticolo rigaArticoloMagazzino) {
        ParametriCreazioneRigaArticolo parametri = new ParametriCreazioneRigaArticolo();
        parametri.setIdArticolo(rigaArticoloMagazzino.getArticolo().getId());
        parametri.setGestioneConai(rigaArticoloMagazzino.getAreaMagazzino().getTipoAreaMagazzino().isGestioneConai());
        parametri.setIdEntita(rigaArticoloMagazzino.getAreaMagazzino().getDocumento().getEntita().getId());
        parametri.setNotaCredito(rigaArticoloMagazzino.getAreaMagazzino().getTipoAreaMagazzino().getTipoDocumento()
                .isNotaCreditoEnable());
        Set<RigaConaiComponente> righeComponenteConai = rigaMagazzinoManager.getDao()
                .creaRigheConaiComponente(rigaArticoloMagazzino, parametri);
        rigaArticoloMagazzino.setRigheConaiComponente(righeComponenteConai);
        return rigaArticoloMagazzino;
    }

    /**
     * Aggiunge se necessario le righe lotto alla riga articolo.
     *
     * @param rigaArticoloMagazzino
     *            riga articolo
     * @param rigaEvasione
     *            riga distinta
     * @return riga articolo modificata
     */
    private RigaArticolo aggiungiRigheLotto(RigaArticolo rigaArticoloMagazzino, RigaDistintaCarico rigaEvasione) {
        // controllo se ci sono righe lotto da creare
        List<RigaDistintaCaricoLotto> righeDistintaLotto = distintaCaricoManager
                .caricaRigheDistintaCaricoLotto(rigaEvasione);
        if (righeDistintaLotto.isEmpty() && Hibernate.isInitialized(rigaEvasione.getRigheDistintaLotto())) {
            righeDistintaLotto = rigaEvasione.getRigheDistintaLotto();
        }
        if (righeDistintaLotto != null && !righeDistintaLotto.isEmpty()) {

            TipoMovimento tipoMovimento = TipoMovimento.SCARICO;
            if (rigaArticoloMagazzino.getAreaMagazzino() != null
                    && rigaArticoloMagazzino.getAreaMagazzino().getTipoAreaMagazzino() != null
                    && rigaArticoloMagazzino.getAreaMagazzino().getTipoAreaMagazzino().getTipoMovimento() != null) {
                tipoMovimento = rigaArticoloMagazzino.getAreaMagazzino().getTipoAreaMagazzino().getTipoMovimento();
            }

            // Se ho più righe dello stesso lotto devo creare una sola riga lotto con la somma della
            // qta
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Map<String, RigaLotto> righeLotto = new HashMap<String, RigaLotto>();
            for (RigaDistintaCaricoLotto rigaDistintaCaricoLotto : righeDistintaLotto) {
                Lotto lotto = lottiManager.caricaLotto(rigaDistintaCaricoLotto.getCodiceLotto(),
                        rigaDistintaCaricoLotto.getDataScadenza(), rigaArticoloMagazzino.getArticolo());
                if (lotto == null) {
                    switch (tipoMovimento) {
                    case SCARICO:
                        LottoNonPresenteException lottoNonPresenteException = new LottoNonPresenteException(
                                rigaDistintaCaricoLotto.getCodiceLotto(), rigaDistintaCaricoLotto.getDataScadenza(),
                                rigaArticoloMagazzino.getArticolo());
                        throw new GenericException("Lotto non trovato", lottoNonPresenteException);
                    default:
                        lotto = new Lotto();
                        lotto.setArticolo(rigaArticoloMagazzino.getArticolo());
                        lotto.setCodice(rigaDistintaCaricoLotto.getCodiceLotto());
                        lotto.setDataScadenza(rigaDistintaCaricoLotto.getDataScadenza());
                        lotto = lottiManager.salvaLotto(lotto);
                        break;
                    }
                }

                RigaLotto rigaLotto = righeLotto
                        .get(lotto.getCodice() + "#" + dateFormat.format(lotto.getDataScadenza()));
                if (rigaLotto == null) {
                    rigaLotto = creaRigaLotto(lotto, rigaArticoloMagazzino);
                }
                int numDecimali = 6;
                if (rigaArticoloMagazzino.getArticolo().getNumeroDecimaliQta() != null) {
                    numDecimali = rigaArticoloMagazzino.getArticolo().getNumeroDecimaliQta();
                }
                rigaLotto.setQuantita(PanjeaEJBUtil
                        .roundToDecimals(rigaLotto.getQuantita() + rigaDistintaCaricoLotto.getQuantita(), numDecimali));
                righeLotto.put(lotto.getCodice() + "#" + dateFormat.format(lotto.getDataScadenza()), rigaLotto);
            }
            rigaArticoloMagazzino.setRigheLotto(new HashSet<RigaLotto>(righeLotto.values()));
        }

        return rigaArticoloMagazzino;
    }

    /**
     * Crea una area magazzino basandosi sulla riga evasione.
     *
     * @param rigaEvasione
     *            riga di riferimento
     * @param dataEvasione
     *            data di evasione
     * @param dataCreazione
     *            data di creazione
     * @param annoCompetenza
     *            anno di competenza
     * @param uuid
     *            uuid di contabilizzazione
     * @return Area magazzino creata
     */
    private AreaMagazzino creaAreaMagazzino(RigaDistintaCarico rigaEvasione, Date dataCreazione, Date dataEvasione,
            Integer annoCompetenza, String uuid) {

        AreaOrdine areaOrdine = rigaEvasione.getRigaArticolo().getAreaOrdine();
        try {
            areaOrdine = panjeaDAO.loadLazy(AreaOrdine.class, areaOrdine.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Documento documento = new Documento();

        documento.setCodiceAzienda(areaOrdine.getDocumento().getCodiceAzienda());
        documento.setDataDocumento(dataEvasione);

        // carico il tipo area magazzino per avere tutti i dati che servono.
        // (es: numero protocollo del tipo documento per poi calcolare il
        // codice)
        TipoAreaMagazzino tipoAreaMagazzino = null;
        try {
            tipoAreaMagazzino = panjeaDAO.load(TipoAreaMagazzino.class,
                    rigaEvasione.getDatiEvasioneDocumento().getTipoAreaEvasione().getId());
        } catch (ObjectNotFoundException e) {
            LOGGER.error("--> errore durante il caricamento del tipo area magazzino. id: "
                    + rigaEvasione.getDatiEvasioneDocumento().getTipoAreaEvasione().getId(), e);
            throw new GenericException("errore durante il caricamento del tipo area magazzino. id: "
                    + rigaEvasione.getDatiEvasioneDocumento().getTipoAreaEvasione().getId(), e);
        }

        SedeEntita sedeEntita = rigaEvasione.getSedeEntitaEvasione();
        if (sedeEntita.getId() != null) {
            documento.setEntita(panjeaDAO.loadLazy(EntitaLite.class, sedeEntita.getEntita().getId()));
            documento.setSedeEntita(sedeEntita);
        }

        documento.setTipoDocumento(tipoAreaMagazzino.getTipoDocumento());
        Importo importoDoc = new Importo(areaOrdine.getDocumento().getTotale().getCodiceValuta(), BigDecimal.ONE);
        importoDoc.setImportoInValuta(BigDecimal.ZERO);
        importoDoc.setImportoInValutaAzienda(BigDecimal.ZERO);
        documento.setTotale(importoDoc);

        AreaMagazzino areaMagazzino = new AreaMagazzino();

        areaMagazzino.setContoTerzi(rigaEvasione.getDatiEvasioneDocumento().isContoTerzi());
        areaMagazzino.setDocumento(documento);
        areaMagazzino.setDataRegistrazione(dataEvasione);
        areaMagazzino.setStatoAreaMagazzino(StatoAreaMagazzino.PROVVISORIO);
        areaMagazzino.setAnnoMovimento(annoCompetenza);
        areaMagazzino.setTipoAreaMagazzino(tipoAreaMagazzino);
        areaMagazzino.setDepositoOrigine(areaOrdine.getDepositoOrigine());
        if (tipoAreaMagazzino.getTipoMovimento().isEnableDepositoDestinazione()) {
            areaMagazzino.setDepositoDestinazione(
                    ObjectUtils.defaultIfNull(rigaEvasione.getDatiEvasioneDocumento().getDepositoDestinazione(),
                            tipoAreaMagazzino.getDepositoDestinazione()));
        }
        areaMagazzino.setListino(areaOrdine.getListino());
        areaMagazzino.setListinoAlternativo(areaOrdine.getListinoAlternativo());
        areaMagazzino.setSedeVettore(areaOrdine.getSedeVettore());
        areaMagazzino.setVettore(areaOrdine.getVettore());

        if (sedeEntita.getId() != null) {
            sedeEntita = panjeaDAO.getEntityManager().merge(sedeEntita);
            if (sedeEntita.getZonaGeografica() != null) {
                areaMagazzino.setIdZonaGeografica(sedeEntita.getZonaGeografica().getId());
            }

            SedeMagazzino sedeMagazzino = sedeEntita.getSedeMagazzino();
            areaMagazzino.assegnaCodiceIvaAlternativo(sedeMagazzino);

            if (sedeEntita.isEreditaDatiCommerciali()) {
                sedeMagazzino = sediMagazzinoManager.caricaSedeMagazzinoBySedeEntita(sedeEntita, false);
            }
            areaMagazzino.setRaggruppamentoBolle(sedeMagazzino.isRaggruppamentoBolle());
            areaMagazzino.setAddebitoSpeseIncasso(sedeMagazzino.isCalcoloSpese());
            if (sedeMagazzino.getAspettoEsteriore() != null) {
                areaMagazzino.setAspettoEsteriore(sedeMagazzino.getAspettoEsteriore().getDescrizione());
            }
            areaMagazzino.setAggiornaCostoUltimo(areaMagazzino.getTipoAreaMagazzino().isAggiornaCostoUltimo());
            areaMagazzino.setStampaPrezzi(sedeMagazzino.isStampaPrezzo());
        }

        areaMagazzino.setCausaleTrasporto(areaOrdine.getCausaleTrasporto());
        areaMagazzino.setTipoPorto(areaOrdine.getTipoPorto());
        areaMagazzino.setTrasportoCura(areaOrdine.getTrasportoCura());
        areaMagazzino.setDataInizioTrasporto(dataEvasione);

        areaMagazzino.setUUIDContabilizzazione(uuid);

        DatiGenerazione datiGenerazione = new DatiGenerazione();
        datiGenerazione.setDataCreazione(dataCreazione);
        datiGenerazione.setDataGenerazione(dataEvasione);
        datiGenerazione.setTipoGenerazione(TipoGenerazione.EVASIONE);
        datiGenerazione.setUtente(getPrincipal().getUserName());
        areaMagazzino.setDatiGenerazione(datiGenerazione);

        return areaMagazzino;
    }

    /**
     * Crea una area area basandosi sulla riga evasione.
     *
     * @param rigaEvasione
     *            riga di riferimento
     * @param tipoAreaPartita
     *            il tipoAreaPartite associato
     * @return Area magazzino creata
     */
    private AreaRate creaAreaRate(RigaDistintaCarico rigaEvasione, TipoAreaPartita tipoAreaPartita) {

        AreaRate areaRate = new AreaRate();
        CodicePagamento codicePagamento = rigaEvasione.getDatiEvasioneDocumento().getCodicePagamento();

        if (codicePagamento != null && codicePagamento.getId() != null) {
            codicePagamento = panjeaDAO.loadLazy(CodicePagamento.class, codicePagamento.getId());
        }

        areaRate.setCodicePagamento(codicePagamento);

        SedeEntita sedeEntita = panjeaDAO.getEntityManager().merge(rigaEvasione.getSedeEntitaEvasione());

        SedeMagazzino sedeMagazzino = sedeEntita.getSedeMagazzino();
        if (sedeEntita.isEreditaDatiCommerciali()) {
            sedeMagazzino = sediMagazzinoManager.caricaSedeMagazzinoBySedeEntita(sedeEntita, false);
        }

        if (codicePagamento != null && codicePagamento.getId() != null && sedeMagazzino.isCalcoloSpese()
                && tipoAreaPartita.getTipoOperazione() == TipoOperazione.GENERA) {
            areaRate.setSpeseIncasso(codicePagamento.getImportoSpese());
            areaRate.setGiorniLimite(codicePagamento.getGiorniLimite());
            areaRate.setPercentualeSconto(codicePagamento.getPercentualeSconto());
        }

        return areaRate;
    }

    /**
     * Crea una riga lotto con qta=0.
     *
     * @param lotto
     *            lotto da associare
     * @param rigaArticolo
     *            riga articolo di riferimento
     * @return riga lotto creata
     */
    private RigaLotto creaRigaLotto(Lotto lotto, RigaArticolo rigaArticolo) {

        RigaLotto rigaLotto = new RigaLotto();
        rigaLotto.setLotto(lotto);
        rigaLotto.setQuantita(0.0);
        rigaLotto.setRigaArticolo(rigaArticolo);
        return rigaLotto;
    }

    /**
     * Crea una riga magazzino da una riga evasione.
     *
     * @param rigaEvasione
     *            {@link RigaDistintaCarico} di riferimento
     * @param areaMagazzino
     *            l'area magazzino del documento di evasione
     * @param ordinamento
     *            ordinamento assegnato alla riga
     * @param tassoDiCambio
     *            tasso di cambio utilizzato per la creazione della riga
     * @param formulaCalculator
     *            istanza di formulaCalculator. Passato per evitare di ricreare ogni volta l'istanza e il motore
     *            javaScript.
     * @return {@link RigaArticolo} creata
     */
    private RigaArticolo creaRigaMagazzino(RigaDistintaCarico rigaEvasione, AreaMagazzino areaMagazzino,
            double ordinamento, BigDecimal tassoDiCambio, FormuleRigaArticoloCalculator formulaCalculator) {

        it.eurotn.panjea.ordini.domain.RigaArticolo rigaOrdine = rigaEvasione.getRigaArticolo();

        // Carico prima l'istanza di rigaordine corretta, poi carica i dati relativi al tipo di riga
        // con
        // il dao corretto
        RigaOrdine rigaCaricata = rigaOrdineManager.getDao().caricaRigaOrdine(rigaOrdine);
        rigaCaricata = rigaOrdineManager.getDao(rigaCaricata).caricaRigaOrdine(rigaCaricata);
        // mi arriva un proxy nel caso di sostituzione articolo, indagare sul perchè e se evitabile
        if (rigaCaricata instanceof HibernateProxy) {
            rigaOrdine = (it.eurotn.panjea.ordini.domain.RigaArticolo) ((HibernateProxy) rigaCaricata)
                    .getHibernateLazyInitializer().getImplementation();
        } else {
            rigaOrdine = (it.eurotn.panjea.ordini.domain.RigaArticolo) rigaCaricata;
        }

        RigaArticolo rigaArticoloMagazzino = rigaOrdine.creaRigaArticoloMagazzino();
        if (areaMagazzino.getCodiceIvaAlternativo() != null) {
            rigaArticoloMagazzino.setCodiceIva(areaMagazzino.getCodiceIvaAlternativo());
        }

        // se l'articolo della riga evasione e quello della sua riga articolo
        // sono diversi mi trovo nel caso di una
        // sostituzione e devo riportare i dati del nuovo articolo sulla riga
        // magazzino
        if (!rigaEvasione.getArticolo().getId().equals(rigaOrdine.getArticolo().getId())) {
            Articolo articolo;
            try {
                articolo = panjeaDAO.load(Articolo.class, rigaEvasione.getArticolo().getId());
            } catch (Exception e) {
                LOGGER.error("--> errore durante il caricamento dell'articolo.", e);
                throw new GenericException("errore durante il caricamento dell'articolo.", e);
            }
            rigaArticoloMagazzino.setArticolo(articolo.getArticoloLite());
            PanjeaEJBUtil.copyProperties(rigaArticoloMagazzino, articolo);
            rigaArticoloMagazzino.setResa(articolo.getResa());

            rigaArticoloMagazzino.setUnitaMisura(articolo.getUnitaMisura().getCodice());
            rigaArticoloMagazzino.setUnitaMisuraQtaMagazzino(articolo.getUnitaMisuraQtaMagazzino().getCodice());

            List<AttributoRiga> attributiMagazzino = new ArrayList<AttributoRiga>();
            for (AttributoArticolo attributoArticolo : articolo.getAttributiArticolo()) {
                if (attributoArticolo.getInserimentoInRiga()) {
                    AttributoRiga attributo = new AttributoRiga();
                    PanjeaEJBUtil.copyProperties(attributo, attributoArticolo);
                    attributo.setId(null);
                    attributo.setRigaArticolo(rigaArticoloMagazzino);
                    attributiMagazzino.add(attributo);
                }
            }
            rigaArticoloMagazzino.setAttributi(attributiMagazzino);
        }

        rigaArticoloMagazzino.setLivello(rigaOrdine.getLivello() + 1);
        rigaArticoloMagazzino.setOrdinamento(ordinamento);
        rigaArticoloMagazzino.setQta(rigaEvasione.getQtaEvasa());
        rigaArticoloMagazzino.setQtaMagazzino(rigaEvasione.getQtaEvasa());
        rigaArticoloMagazzino.setMoltQtaOrdine(rigaEvasione.getMoltiplicatoreQta());
        rigaArticoloMagazzino.setAreaMagazzino(areaMagazzino);
        rigaArticoloMagazzino.setId(null);

        // setto gli importi e gli sconti presenti sulla riga distinta carico
        // perchè potrebbero essere diversi da quelli
        // della riga ordine di partenza
        rigaArticoloMagazzino.setVariazione1(rigaEvasione.getVariazione1());
        rigaArticoloMagazzino.setVariazione2(rigaEvasione.getVariazione2());
        rigaArticoloMagazzino.setVariazione3(rigaEvasione.getVariazione3());
        rigaArticoloMagazzino.setVariazione4(rigaEvasione.getVariazione4());

        Importo prezzoUnitario = rigaArticoloMagazzino.getPrezzoUnitario().clone();
        prezzoUnitario.setImportoInValuta(rigaEvasione.getPrezzoUnitario());
        prezzoUnitario.setTassoDiCambio(tassoDiCambio);
        int numeroDecimaliPrezzo = 2;
        if (rigaEvasione.getNumeroDecimaliPrezzo() != null) {
            numeroDecimaliPrezzo = rigaEvasione.getNumeroDecimaliPrezzo();
        }
        prezzoUnitario.calcolaImportoValutaAzienda(numeroDecimaliPrezzo);
        rigaArticoloMagazzino.setPrezzoUnitario(prezzoUnitario);

        // aggiungo le eventuali righe lotto
        rigaArticoloMagazzino = aggiungiRigheLotto(rigaArticoloMagazzino, rigaEvasione);
        final ConfigurazioneDistinta configurazioneDistinta = rigaOrdine.getConfigurazioneDistinta();

        if (rigaArticoloMagazzino instanceof RigaArticoloDistinta) {
            // carico le qta di attrezzaggio per ogni riga distinta/componente, ma non faccio altro;
            // la
            // qta di
            // attrezzaggio sarà sommata alla quantità sul salvataggio della rigaMagazzino
            // (it.eurotn.panjea.magazzino.manager.documento.RigaMagazzinoDistintaDAO.saveRigaMagazzino(RigaMagazzino)).
            formulaCalculator.setFormulaCalculatorClosure(new FormulaCalculatorClosure() {

                @Override
                public IRigaArticoloDocumento apply(IRigaArticoloDocumento rigaArticoloDocumento) {
                    if (rigaArticoloDocumento instanceof RigaArticolo) {
                        RigaArticolo riga = (RigaArticolo) rigaArticoloDocumento;
                        riga = rigaMagazzinoManager.getDao().caricaQtaAttrezzaggio(riga, configurazioneDistinta);
                        rigaArticoloDocumento = riga;
                    }
                    return rigaArticoloDocumento;
                }
            });
        }
        rigaArticoloMagazzino.updateBattute();

        // Calcolo le formule su qta e attributi
        // NB.: Da notare che dopo questo calcolo, le righe verranno ricalcolate al salvataggio,
        // quindi,
        // tra l'altro,
        // perchè calcolare qui ?
        try {
            rigaArticoloMagazzino = (RigaArticolo) formulaCalculator.calcola(rigaArticoloMagazzino);
        } catch (FormuleTipoAttributoException e) {
            LOGGER.error("-->errore nel calcolare le formule durante l'evasione ", e);
            throw new GenericException(e);
        }

        // aggiungo le eventuali righe conai
        TipoMovimento tm = rigaArticoloMagazzino.getAreaMagazzino().getTipoAreaMagazzino().getTipoMovimento();
        if (tm == TipoMovimento.CARICO || tm == TipoMovimento.SCARICO) {
            rigaArticoloMagazzino = aggiungiRigheConaiComponente(rigaArticoloMagazzino);
        }
        ((Session) panjeaDAO.getEntityManager().getDelegate()).evict(rigaOrdine);
        return rigaArticoloMagazzino;
    }

    /**
     *
     * @param rigaTestata
     *            rigaTestataOrdine
     * @param documentoEvasione
     *            documento evasione
     * @param ordinamento
     *            ordinamento della testata
     * @return rigaTestata creata
     */
    private RigaTestata creaRigaTestataCollegata(it.eurotn.panjea.ordini.domain.RigaTestata rigaTestata,
            AreaMagazzino documentoEvasione, double ordinamento) {
        // Cerco se la riga è già presente

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(
                "select r from RigaTestata r join fetch r.areaMagazzino am where r.rigaOrdineCollegata.id=:idRigaOrdineCollegata and r.areaMagazzino.id=:idAreaMagazzino");

        Query query = panjeaDAO.prepareQuery(stringBuilder.toString());
        query.setParameter("idRigaOrdineCollegata", rigaTestata.getId());
        query.setParameter("idAreaMagazzino", documentoEvasione.getId());

        RigaTestata rigaTestataCollegata = null;
        try {
            rigaTestataCollegata = (RigaTestata) panjeaDAO.getSingleResult(query);
        } catch (ObjectNotFoundException e) {
            rigaTestataCollegata = new RigaTestata();
            rigaTestataCollegata.setAreaMagazzino(documentoEvasione);
            rigaTestataCollegata.setAreaOrdineCollegata(rigaTestata.getAreaOrdine());
            rigaTestataCollegata.setOrdinamento(ordinamento);
            rigaTestataCollegata.setDescrizione(rigaTestata.getDescrizione());
            rigaTestataCollegata.setLivello(rigaTestata.getLivello() + 1);
            rigaTestataCollegata.setRigaOrdineCollegata(rigaTestata);
        } catch (DAOException e) {
            LOGGER.error("-->errore nel cercare la rigaTestataCollegata", e);
            throw new GenericException(e);
        }
        return rigaTestataCollegata;
    }

    /**
     * Crea una riga testata da una riga evasione.<br>
     * <B>NB:</B>Se la riga testata esiste già per l'ordine viene restituita la riga testata presente
     *
     * @param rigaEvasione
     *            {@link RigaDistintaCarico} di riferimento
     * @param areaMagazzino
     *            l'area magazzino del documento di evasione
     * @param ordinamento
     *            ordinamento assegnato alla riga
     * @return {@link RigaTestata} creata
     */
    private RigaTestata creaRigaTestataOrdine(RigaDistintaCarico rigaEvasione, AreaMagazzino areaMagazzino,
            double ordinamento) {

        AreaOrdine areaOrdine = rigaEvasione.getRigaArticolo().getAreaOrdine();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(
                "select r from RigaTestata r join fetch r.areaMagazzino am where r.areaOrdineCollegata.id=:areaOrdine and am.id=:paramIdAreaMagazzino and r.rigaOrdineCollegata is null");

        Query query = panjeaDAO.prepareQuery(stringBuilder.toString());
        query.setParameter("areaOrdine", rigaEvasione.getRigaArticolo().getAreaOrdine().getId());
        query.setParameter("paramIdAreaMagazzino", areaMagazzino.getId());

        RigaTestata rigaTestata;
        try {
            rigaTestata = (RigaTestata) panjeaDAO.getSingleResult(query);
        } catch (ObjectNotFoundException e) {
            // Ricarico l'area ordine perchè nella descrizione della testata possono essere
            // inserite svariate proprietà
            try {
                areaOrdine = panjeaDAO.load(AreaOrdine.class, areaOrdine.getId());
            } catch (ObjectNotFoundException e1) {
                LOGGER.error("-->errore nel cercare l'area ordine " + areaOrdine, e1);
                throw new PersistenceException(e1);
            }
            rigaTestata = new RigaTestata();
            rigaTestata.setAreaOrdineCollegata(areaOrdine);
            rigaTestata.setCodiceTipoDocumentoCollegato(areaOrdine.getDocumento().getTipoDocumento().getCodice());
            rigaTestata.setDataAreaMagazzinoCollegata(areaOrdine.getDataRegistrazione());
            rigaTestata.setLivello(0);
            rigaTestata.setOrdinamento(ordinamento);
            rigaTestata.setDescrizione(rigaTestata.generaDescrizioneTestata(rigaEvasione.getAreaSpedizione()));
            rigaTestata.setAreaMagazzino(areaMagazzino);
        } catch (DAOException e) {
            LOGGER.error("-->errore nel cercare la riga testata per l'ordine "
                    + rigaEvasione.getRigaArticolo().getAreaOrdine(), e);
            throw new PersistenceException(e);
        }
        return rigaTestata;
    }

    @Override
    public void evadiOrdini(List<RigaDistintaCarico> righeEvasione, AreaMagazzino documentoEvasione)
            throws EvasioneLottiException {
        // Raggruppo le righe per area ordine
        Map<AreaOrdine, List<RigaDistintaCarico>> distinteRaggruppate = PanjeaEJBUtil.listToMap(righeEvasione,
                new KeyFromValueProvider<RigaDistintaCarico, AreaOrdine>() {
                    @Override
                    public AreaOrdine keyFromValue(RigaDistintaCarico elem) {
                        return elem.getRigaArticolo().getAreaOrdine();
                    }
                });

        // carico il tasso di cambio per la data del documento
        CambioValuta cambioValuta = null;
        try {
            cambioValuta = valutaManager.caricaCambioValuta(
                    documentoEvasione.getDocumento().getTotale().getCodiceValuta(),
                    documentoEvasione.getDocumento().getDataDocumento());
        } catch (CambioNonPresenteException e1) {
            LOGGER.warn("--> tasso di cambio non presente per la valuta.", e1);
            throw new GenericException("tasso di cambio non presente per la valuta.", e1);
        }

        FormuleRigaArticoloCalculator formuleRigaArticoloCalculator = new FormuleRigaArticoloEvasioneCalculator();
        Query aggiornaRigheCollegate = panjeaDAO.prepareQuery(
                "update RigaMagazzino rm set rm.ordinamento=:ordinamento where rm.areaOrdineCollegata.id=:idAreaOrdine");
        EvasioneLottiException evasioneLottiException = new EvasioneLottiException();
        for (Entry<AreaOrdine, List<RigaDistintaCarico>> entryRigheDistinteCaricoRaggruppate : distinteRaggruppate
                .entrySet()) {
            double ordinamento = Calendar.getInstance().getTimeInMillis();
            RigaTestata rigaTestata = creaRigaTestataOrdine(entryRigheDistinteCaricoRaggruppate.getValue().get(0),
                    documentoEvasione, ordinamento);
            try {
                rigaTestata = panjeaDAO.save(rigaTestata);
            } catch (DAOException e) {
                LOGGER.error("-->errore nel salvare la riga testata", e);
                throw new GenericException(e);
            }

            if (rigaTestata.getOrdinamento() != ordinamento) {
                // la riga testata è già stata inserita quindi devo mettere tutte le righe collegate
                // all'ordine con
                // lo stesso ordinamento. Dopo riordino il tutto.
                aggiornaRigheCollegate.setParameter("ordinamento", rigaTestata.getOrdinamento());
                aggiornaRigheCollegate.setParameter("idAreaOrdine",
                        entryRigheDistinteCaricoRaggruppate.getKey().getId());
                aggiornaRigheCollegate.executeUpdate();
            }
            ordinamento = rigaTestata.getOrdinamento();

            // BRUTTO.
            // Le due hashmap sotto si potrebbero eliminare usando una funzione ricorsiva che faccia
            // in modo tale da
            // invertire l'ordine di salvataggio.
            // Salvo in una mappa le righe testata già create, associate con l'id della relativa
            // riga testata
            // nell'ordine.
            HashMap<Integer, RigaTestata> righeTestataGiaCreate = new HashMap<Integer, RigaTestata>();

            // Salvo in una mappa gli id delle righe collegate, perché mi servirà per aggiornare gli
            // elementi della
            // mappa sopra quando ci sono più livelli
            // La riga testata nuova infatti viene inizialmente associata alla riga testata del
            // gruppo di righe Poi nel
            // ciclo successivo (del while sotto) gli si assegna un'eventuale altra nuova riga
            // testata.
            // Questo però rende la riga testata nella mappa sopra disallineato col db.
            HashMap<Integer, Integer> idRigheTestateCollegate = new HashMap<Integer, Integer>();

            for (RigaDistintaCarico rigaDistintaCarico : entryRigheDistinteCaricoRaggruppate.getValue()) {
                RigaArticolo nuovaRigaMagazzino = creaRigaMagazzino(rigaDistintaCarico, documentoEvasione, ordinamento,
                        cambioValuta.getTasso(), formuleRigaArticoloCalculator);
                try {
                    panjeaDAO.getEntityManager().flush();
                    nuovaRigaMagazzino = (RigaArticolo) rigaMagazzinoManager.getDao(nuovaRigaMagazzino)
                            .salvaRigaMagazzino(nuovaRigaMagazzino);
                    nuovaRigaMagazzino.setRigaTestataMagazzinoCollegata(rigaTestata);
                    nuovaRigaMagazzino = panjeaDAO.saveWithoutFlush(nuovaRigaMagazzino);
                } catch (QtaLottiMaggioreException | RimanenzaLottiNonValidaException
                        | RigheLottiNonValideException e) {
                    evasioneLottiException.addException(rigaDistintaCarico, e);
                } catch (Exception e) {
                    LOGGER.error("-->errore nel salvare la riga", e);
                    throw new GenericException(e);
                }

                // Se ho delle testate le creo e le associo alla riga magazzino.
                Integer idRigaTestataOrdine = null;
                // Se ho un proxy non ricarico tutta la riga testata. Hibernate lo fa perchè ho
                // l'injenct a
                // livello
                // variabile e non proprietà
                // Devo usare il proxy ed il suo identifier
                idRigaTestataOrdine = PanjeaEJBUtil
                        .getLazyId(rigaDistintaCarico.getRigaArticolo().getRigaTestataCollegata());
                RigaMagazzino rigaMagazzinoDaCollegare = nuovaRigaMagazzino;

                while (idRigaTestataOrdine != null) {
                    it.eurotn.panjea.ordini.domain.RigaTestata rigaTestataOrdineCollegata;
                    try {
                        rigaTestataOrdineCollegata = panjeaDAO.load(it.eurotn.panjea.ordini.domain.RigaTestata.class,
                                idRigaTestataOrdine);
                    } catch (ObjectNotFoundException e1) {
                        LOGGER.error("-->errore nel caricare la rigaTestataOrdine con id "
                                + rigaDistintaCarico.getIdRigaTestata(), e1);
                        throw new GenericException(e1);
                    }

                    // Cerco se la riga Testata è stata creata per l'ordin di partenza, in caso
                    // contrario la
                    // creo e la
                    // metto in cache delle riche testate.
                    RigaTestata rigaTestataCollegata = righeTestataGiaCreate.get(idRigaTestataOrdine);
                    if (rigaTestataCollegata == null) {
                        rigaTestataCollegata = creaRigaTestataCollegata(rigaTestataOrdineCollegata, documentoEvasione,
                                ordinamento);
                        rigaTestataCollegata.setRigaTestataMagazzinoCollegata(rigaTestata);
                        try {
                            rigaTestataCollegata = panjeaDAO.saveWithoutFlush(rigaTestataCollegata);
                            righeTestataGiaCreate.put(idRigaTestataOrdine, rigaTestataCollegata);
                        } catch (DAOException e) {
                            LOGGER.error("-->errore nel salvare la rigaTestata nuova", e);
                            throw new GenericException(e);
                        }
                        idRigheTestateCollegate.put(rigaTestataCollegata.getId(), idRigaTestataOrdine);
                    }

                    try {
                        rigaMagazzinoDaCollegare.setRigaTestataMagazzinoCollegata(rigaTestataCollegata);
                        panjeaDAO.saveWithoutFlush(rigaMagazzinoDaCollegare);
                    } catch (DAOException e) {
                        LOGGER.error("-->errore nel salvare la rigaTestata magazzino collegata", e);
                        throw new GenericException(e);
                    }

                    // se sto modificando una riga testata creata nel ciclo precedente, devo
                    // aggiornare l'oggetto nella
                    // mappa delle righe testata create
                    if (rigaMagazzinoDaCollegare instanceof RigaTestata
                            && idRigheTestateCollegate.containsKey(rigaMagazzinoDaCollegare.getId())) {
                        int idRigaTestataOrdineDaAggiornare = idRigheTestateCollegate
                                .get(rigaMagazzinoDaCollegare.getId());
                        righeTestataGiaCreate.put(idRigaTestataOrdineDaAggiornare,
                                (RigaTestata) rigaMagazzinoDaCollegare);
                    }

                    idRigaTestataOrdine = rigaTestataOrdineCollegata.getRigaTestataCollegata() == null ? null
                            : rigaTestataOrdineCollegata.getRigaTestataCollegata().getId();
                    rigaMagazzinoDaCollegare = rigaTestataCollegata;
                }

                // Forzo la riga dell'ordine se è stata forzata manualmente o se l'articolo è a peso
                // variabile
                if (rigaDistintaCarico.getForzata() || rigaDistintaCarico.getArticolo().isGestioneQuantitaZero()) {
                    // Se la rigaOrdine è stata forzata per la chiusura aggiorno la riga ordine.
                    RigaOrdine rigaOrdine = rigaDistintaCarico.getRigaArticolo();
                    rigaOrdine = panjeaDAO.loadLazy(RigaOrdine.class, rigaOrdine.getId());
                    rigaOrdine.setEvasioneForzata(true);
                    try {
                        panjeaDAO.saveWithoutFlush(rigaOrdine);
                    } catch (Exception e) {
                        LOGGER.error("--> errore durante il salvataggio della riga ordine.", e);
                        throw new GenericException("errore durante il salvataggio della riga ordine.", e);
                    }
                }
            }
        }
        if (!evasioneLottiException.isEmpty()) {
            sessionContext.setRollbackOnly();
            throw evasioneLottiException;
        }
        // Riordino le righe
        panjeaDAO.getEntityManager().flush();
        areaMagazzinoManager.ordinaRighe(documentoEvasione);
    }

    @Override
    public List<AreaMagazzinoFullDTO> evadiOrdini(final List<RigaDistintaCarico> righeEvasione, final Date dataEvasione)
            throws TipoAreaPartitaDestinazioneRichiestaException, LottiException {
        // genero l'UUID per la contabilizzazione
        Random random = new Random();
        long r1 = random.nextLong();
        long r2 = random.nextLong();
        String uuid = Long.toHexString(r1) + Long.toHexString(r2);

        Date dataCreazione = Calendar.getInstance().getTime();

        Integer annoCompetenza = magazzinoSettingsManager.caricaMagazzinoSettings().getAnnoCompetenza();

        List<AreaMagazzinoFullDTO> areeMagazzinoFullDTO = new ArrayList<AreaMagazzinoFullDTO>();

        EventList<RigaDistintaCarico> eventList = new BasicEventList<RigaDistintaCarico>();
        eventList.addAll(righeEvasione);

        GroupingList<RigaDistintaCarico> groupingList = new GroupingList<RigaDistintaCarico>(eventList,
                new RigaDistintaCaricoComparator());

        for (List<RigaDistintaCarico> listRigheEvasione : groupingList) {

            // creo l'area magazzino
            AreaMagazzino areaMagazzino = creaAreaMagazzino(listRigheEvasione.get(0), dataCreazione, dataEvasione,
                    annoCompetenza, uuid);

            try {
                areaMagazzino = areaMagazzinoManager.salvaAreaMagazzino(areaMagazzino, false);
            } catch (DocumentoAssenteAvvisaException e1) {
                throw new IllegalStateException("Errore non previsto ", e1);
            } catch (DocumentoAssenteBloccaException e1) {
                throw new IllegalStateException("Errore non previsto ", e1);
            } catch (DocumentiEsistentiPerAreaMagazzinoException e1) {
                throw new IllegalStateException("Errore non previsto ", e1);
            }

            try {
                evadiOrdini(listRigheEvasione, areaMagazzino);
            } catch (EvasioneLottiException e) {
                LOGGER.error("-->errore errore nell'evadere il documento. Lotti assenti", e);
                throw e.getEccezioni().values().iterator().next();
            }
            TipoAreaPartita tipoAreaPartita = tipiAreaPartitaManager.caricaTipoAreaPartitaPerTipoDocumento(
                    listRigheEvasione.get(0).getDatiEvasioneDocumento().getTipoAreaEvasione().getTipoDocumento());
            AreaRate areaRate = null;
            if (tipoAreaPartita.getId() != null) {
                areaRate = creaAreaRate(listRigheEvasione.get(0), tipoAreaPartita);
            }
            // areaMagazzinoManager.validaRigheMagazzino(areaMagazzino, areaRate,
            // areaContabilePresente,
            // forzaStato)
            AreaMagazzinoFullDTO areaMagazzinoFullDTO = new AreaMagazzinoFullDTO();
            areaMagazzinoFullDTO.setAreaRate(areaRate);
            areaMagazzinoFullDTO.setAreaMagazzino(areaMagazzino);
            List<RigaMagazzino> righeMagazzino = new ArrayList<RigaMagazzino>();
            areaMagazzinoFullDTO.setRigheMagazzino(righeMagazzino);
            areeMagazzinoFullDTO.add(areaMagazzinoFullDTO);
        }
        LOGGER.debug("--> Exit getDocumenti");
        return areeMagazzinoFullDTO;
    }

    /**
     * Restituisce il principal corrente.
     *
     * @return principal corrente
     */
    private JecPrincipal getPrincipal() {
        return (JecPrincipal) sessionContext.getCallerPrincipal();
    }

}
