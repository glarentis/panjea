package it.eurotn.panjea.contabilita.service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jboss.annotation.IgnoreDependency;
import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.documenti.service.exception.AreeCollegatePresentiException;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentiCollegatiPresentiException;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentoDuplicateException;
import it.eurotn.panjea.anagrafica.documenti.service.exception.TipoDocumentoNonConformeException;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.AziendaLite;
import it.eurotn.panjea.anagrafica.manager.interfaces.AziendeManager;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;
import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.centricosto.domain.CentroCosto;
import it.eurotn.panjea.centricosto.manager.interfaces.CentriCostoManager;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.AreaContabile.StatoAreaContabile;
import it.eurotn.panjea.contabilita.domain.AreaContabileLite;
import it.eurotn.panjea.contabilita.domain.Conto.SottotipoConto;
import it.eurotn.panjea.contabilita.domain.ControPartita;
import it.eurotn.panjea.contabilita.domain.EstrattoConto;
import it.eurotn.panjea.contabilita.domain.Giornale;
import it.eurotn.panjea.contabilita.domain.GiornaleIva;
import it.eurotn.panjea.contabilita.domain.NoteAreaContabile;
import it.eurotn.panjea.contabilita.domain.RegistroIva.TipoRegistro;
import it.eurotn.panjea.contabilita.domain.RigaContabile;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileCancellaManager;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileManager;
import it.eurotn.panjea.contabilita.manager.interfaces.BilancioManager;
import it.eurotn.panjea.contabilita.manager.interfaces.ContabilitaAnagraficaManager;
import it.eurotn.panjea.contabilita.manager.interfaces.ContabilitaAnnualeManager;
import it.eurotn.panjea.contabilita.manager.interfaces.ControlliManager;
import it.eurotn.panjea.contabilita.manager.interfaces.EstrattoContoManager;
import it.eurotn.panjea.contabilita.manager.interfaces.LibroGiornaleManager;
import it.eurotn.panjea.contabilita.manager.interfaces.LiquidazioneIvaManager;
import it.eurotn.panjea.contabilita.manager.interfaces.PianoContiManager;
import it.eurotn.panjea.contabilita.manager.interfaces.RegistroIvaManager;
import it.eurotn.panjea.contabilita.manager.interfaces.SaldoManager;
import it.eurotn.panjea.contabilita.manager.interfaces.SituazioneEPManager;
import it.eurotn.panjea.contabilita.manager.interfaces.StatoAreaContabileManager;
import it.eurotn.panjea.contabilita.manager.interfaces.StrutturaContabileManager;
import it.eurotn.panjea.contabilita.service.exception.AperturaEsistenteException;
import it.eurotn.panjea.contabilita.service.exception.AreaContabileDuplicateException;
import it.eurotn.panjea.contabilita.service.exception.ChiusuraAssenteException;
import it.eurotn.panjea.contabilita.service.exception.ChiusuraEsistenteException;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.contabilita.service.exception.ContiBaseException;
import it.eurotn.panjea.contabilita.service.exception.ContiEntitaAssentiException;
import it.eurotn.panjea.contabilita.service.exception.ContoRapportoBancarioAssenteException;
import it.eurotn.panjea.contabilita.service.exception.DocumentiNonStampatiGiornaleException;
import it.eurotn.panjea.contabilita.service.exception.FormulaException;
import it.eurotn.panjea.contabilita.service.exception.GiornaliNonValidiException;
import it.eurotn.panjea.contabilita.service.exception.RigheContabiliNonValidiException;
import it.eurotn.panjea.contabilita.service.exception.StatoAreaContabileNonValidoException;
import it.eurotn.panjea.contabilita.service.interfaces.ContabilitaService;
import it.eurotn.panjea.contabilita.util.AreaContabileDTO;
import it.eurotn.panjea.contabilita.util.AreaContabileFullDTO;
import it.eurotn.panjea.contabilita.util.DocumentoEntitaDTO;
import it.eurotn.panjea.contabilita.util.FatturatoDTO;
import it.eurotn.panjea.contabilita.util.LiquidazioneIvaDTO;
import it.eurotn.panjea.contabilita.util.ParametriAperturaContabile;
import it.eurotn.panjea.contabilita.util.ParametriChiusuraContabile;
import it.eurotn.panjea.contabilita.util.ParametriRicercaBilancio;
import it.eurotn.panjea.contabilita.util.ParametriRicercaBilancioConfronto;
import it.eurotn.panjea.contabilita.util.ParametriRicercaEstrattoConto;
import it.eurotn.panjea.contabilita.util.ParametriRicercaMovimentiContabili;
import it.eurotn.panjea.contabilita.util.ParametriRicercaSituazioneEP;
import it.eurotn.panjea.contabilita.util.RegistroLiquidazioneDTO;
import it.eurotn.panjea.contabilita.util.RigaContabileDTO;
import it.eurotn.panjea.contabilita.util.RisultatoControlloProtocolli;
import it.eurotn.panjea.contabilita.util.RisultatoControlloRateSaldoContabile;
import it.eurotn.panjea.contabilita.util.SaldoConti;
import it.eurotn.panjea.contabilita.util.SaldoConto;
import it.eurotn.panjea.contabilita.util.SaldoContoConfronto;
import it.eurotn.panjea.contabilita.util.SituazioneEpDTO;
import it.eurotn.panjea.contabilita.util.TotaliCodiceIvaDTO;
import it.eurotn.panjea.iva.domain.AreaIva;
import it.eurotn.panjea.iva.manager.interfaces.AreaIvaManager;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoManager;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.pagamenti.service.exception.CodicePagamentoNonTrovatoException;
import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.panjea.parametriricerca.domain.Periodo.TipoPeriodo;
import it.eurotn.panjea.partite.domain.TipoAreaPartita;
import it.eurotn.panjea.partite.manager.interfaces.TipiAreaPartitaManager;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rate.manager.interfaces.AreaRateManager;
import it.eurotn.panjea.rate.manager.interfaces.RateGenerator;
import it.eurotn.panjea.tesoreria.domain.AreaTesoreria;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaTesoreriaManager;
import it.eurotn.security.JecPrincipal;

/**
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.ContabilitaService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.ContabilitaService")
public class ContabilitaServiceBean implements ContabilitaService {

    /**
     * Comparator per ordinare le aree contabili per codice del documento discendente.
     *
     * @author Leonardo
     */
    private class NumeroDocumentoComparator implements Comparator<AreaContabile> {

        @Override
        public int compare(AreaContabile o1, AreaContabile o2) {
            return o2.getDocumento().getCodice().compareTo(o1.getDocumento().getCodice());
        }

    }

    private static Logger logger = Logger.getLogger(ContabilitaServiceBean.class);
    @Resource
    private SessionContext context;
    @EJB(mappedName = "Panjea.AreaContabileManager")
    private AreaContabileManager areaContabileManager;
    @EJB
    private BilancioManager bilancioManager;
    @EJB
    private ContabilitaAnnualeManager contabilitaAnnualeManager;
    @EJB
    private StrutturaContabileManager strutturaContabileManager;
    @EJB
    private SituazioneEPManager situazioneEPManager;
    @EJB
    private LibroGiornaleManager libroGiornaleManager;
    @EJB
    private StatoAreaContabileManager statoAreaContabileManager;
    @EJB
    private RegistroIvaManager registroIvaManager;
    @EJB
    private AreaIvaManager areaIvaManager;
    @EJB
    private AreaRateManager areaRateManager;
    @EJB
    private TipiAreaPartitaManager tipiAreaPartitaManager;
    @EJB
    private SaldoManager saldoManager;
    @EJB
    private AreaContabileCancellaManager areaContabileCancellaManager;
    @EJB
    private LiquidazioneIvaManager liquidazioneIvaManager;
    @EJB
    private EstrattoContoManager estrattoContoManager;
    @EJB
    private AziendeManager aziendeManager;
    @EJB
    private RateGenerator rateGenerator;
    @EJB
    private AreaTesoreriaManager areaTesoreriaManager;
    @EJB
    private AreaMagazzinoManager areaMagazzinoManager;
    @EJB
    private ControlliManager controlliManager;
    @EJB
    private PianoContiManager pianoContiManager;
    @EJB
    private CentriCostoManager centriCostoManager;
    @EJB
    @IgnoreDependency
    private ContabilitaAnagraficaManager contabilitaAnagraficaManager;

    @Override
    @RolesAllowed("gestionePeriodica")
    @TransactionAttribute(value = TransactionAttributeType.NEVER)
    public void aggiornaStampaGiornale(Giornale giornale, Map<AreaContabileDTO, Integer> mapAreeContabili,
            Map<RigaContabileDTO, List<Integer>> mapRigheContabili) throws ContabilitaException {
        logger.debug("--> Enter aggiornaStampaGiornale");

        int splitStep = 300;
        int counter = 0;

        // aggiorno le aree
        Map<AreaContabileDTO, Integer> subMapAree = new HashMap<AreaContabileDTO, Integer>();
        for (Entry<AreaContabileDTO, Integer> entry : mapAreeContabili.entrySet()) {

            subMapAree.put(entry.getKey(), entry.getValue());
            counter++;
            if (counter % splitStep == 0) {
                areaContabileManager.aggiornaAreeContabiliPerGiornale(subMapAree);
                counter = 0;
                subMapAree = new HashMap<AreaContabileDTO, Integer>();
            }
        }
        if (!subMapAree.isEmpty()) {
            areaContabileManager.aggiornaAreeContabiliPerGiornale(subMapAree);
        }

        // aggiorno le righe
        counter = 0;
        Map<RigaContabileDTO, List<Integer>> subMap = new HashMap<RigaContabileDTO, List<Integer>>();
        for (Entry<RigaContabileDTO, List<Integer>> entry : mapRigheContabili.entrySet()) {

            subMap.put(entry.getKey(), entry.getValue());
            counter++;
            if (counter % splitStep == 0) {
                areaContabileManager.aggiornaRigheContabiliPerGiornale(subMap);
                counter = 0;
                subMap = new HashMap<RigaContabileDTO, List<Integer>>();
            }
        }
        if (!subMap.isEmpty()) {
            areaContabileManager.aggiornaRigheContabiliPerGiornale(subMap);
        }

        libroGiornaleManager.salvaGiornale(giornale);
        logger.debug("--> Exit aggiornaStampaGiornale");
    }

    @Override
    @RolesAllowed("gestionePeriodica")
    public void aggiornaStampaRegistroIva(GiornaleIva giornaleIva, Map<AreaContabileDTO, Integer> mapAreeContabili,
            Map<TotaliCodiceIvaDTO, Integer> mapRigheIva) {
        logger.debug("--> Enter aggiornaStampaRegistroIva");

        areaContabileManager.aggiornaAreeContabiliPerRegistroIva(giornaleIva, mapAreeContabili);
        registroIvaManager.salvaGiornaleIva(giornaleIva);

        logger.debug("--> Exit aggiornaStampaRegistroIva");
    }

    @Override
    public BigDecimal calcoloSaldo(SottoConto sottoConto, CentroCosto centroCosto, Date data, Integer annoEsercizio) {
        AziendaLite aziendaLite;
        SaldoConto saldoConto = null;
        try {
            aziendaLite = aziendeManager.caricaAzienda(getAzienda());
            saldoConto = saldoManager.calcoloSaldo(sottoConto, centroCosto, data, annoEsercizio, aziendaLite);
        } catch (Exception e) {
            logger.warn("--> Tipi documento base mancanti");
            throw new RuntimeException(e);
        }
        return saldoConto.getSaldo();
    }

    @Override
    public BigDecimal calcoloSaldo(SottoConto conto, Date data, Integer annoEsercizio) {
        return calcoloSaldo(conto, null, data, annoEsercizio);
    }

    @Override
    @RolesAllowed("gestioneDocContabilita")
    public AreaContabile cambiaStatoAreaContabileInConfermato(AreaContabile areaContabile)
            throws StatoAreaContabileNonValidoException, ContabilitaException {
        logger.debug("--> Enter cambiaStatoAreaContabileInConfermato");
        areaContabile = statoAreaContabileManager.cambioStatoInConfermato(areaContabile);
        logger.debug("--> Exit cambiaStatoAreaContabileInConfermato");
        return areaContabile;
    }

    @Override
    @RolesAllowed("gestioneDocContabilita")
    public AreaContabile cambiaStatoAreaContabileInProvvisorio(AreaContabile areaContabile)
            throws StatoAreaContabileNonValidoException, ContabilitaException {
        logger.debug("--> Enter cambiaStatoAreaContabileInProvvisorio");
        areaContabile = statoAreaContabileManager.cambioStatoInProvvisorio(areaContabile);
        logger.debug("--> Exit cambiaStatoAreaContabileInProvvisorio");
        return areaContabile;
    }

    @Override
    @RolesAllowed("gestioneDocContabilita")
    public AreaContabile cambiaStatoAreaContabileInSimulato(AreaContabile areaContabile)
            throws StatoAreaContabileNonValidoException, ContabilitaException {
        logger.debug("--> Enter cambiaStatoAreaContabileInSimulato");
        areaContabile = statoAreaContabileManager.cambioStatoDaProvvisorioInSimulato(areaContabile);
        logger.debug("--> Exit cambiaStatoAreaContabileInSimulato");
        return areaContabile;
    }

    @Override
    @RolesAllowed("gestioneDocContabilita")
    public AreaContabile cambiaStatoAreaContabileInVerificato(AreaContabile areaContabile)
            throws StatoAreaContabileNonValidoException, ContabilitaException {
        logger.debug("--> Enter cambiaStatoAreaContabileInVerificato");
        areaContabile = statoAreaContabileManager.cambioStatoDaConfermatoInVerificato(areaContabile);
        logger.debug("--> Exit cambiaStatoAreaContabileInVerificato");
        return areaContabile;
    }

    @Override
    @RolesAllowed("gestioneDocContabilita")
    public void cancellaAreaContabile(AreaContabile areaContabile)
            throws DocumentiCollegatiPresentiException, AreeCollegatePresentiException {
        areaContabileCancellaManager.cancellaAreaContabile(areaContabile);
    }

    @Override
    public void cancellaAreaContabile(AreaContabile areaContabile, boolean deleteAreeCollegate,
            boolean forceDeleteAreeCollegate)
                    throws DocumentiCollegatiPresentiException, AreeCollegatePresentiException {
        areaContabileCancellaManager.cancellaAreaContabile(areaContabile, deleteAreeCollegate,
                forceDeleteAreeCollegate);
    }

    @TransactionAttribute(TransactionAttributeType.NEVER)
    @Override
    public void cancellaAreeContabili(List<Integer> idAreeContabili, boolean deleteAreeCollegate)
            throws DocumentiCollegatiPresentiException, AreeCollegatePresentiException {

        List<AreaContabile> areeDaCancellare = new ArrayList<AreaContabile>();
        for (Integer idAreaContabile : idAreeContabili) {
            // se devo cancellare l'interodocumento (deleteAreeCollegate=true allora forzo la cancellazione di tutte le
            // aree) in questo use case non avverto se ci sono altre aree da cancellare o meno.
            AreaContabile areaContabileDaCancellare = null;
            try {
                areaContabileDaCancellare = caricaAreaContabile(idAreaContabile);
                areeDaCancellare.add(areaContabileDaCancellare);
            } catch (ContabilitaException e) {
                logger.error("--> errore nel caricare l'area contabile " + idAreaContabile, e);
            }

        }
        // devo ordinare le aree Contabili secondo il numero documento in modo da avere la possibilita' di ripristinare
        // il protocollo al valore precedente dopo una cancellazione
        Collections.sort(areeDaCancellare, new NumeroDocumentoComparator());
        for (AreaContabile areaContabileDaCancellare : areeDaCancellare) {
            cancellaAreaContabile(areaContabileDaCancellare, deleteAreeCollegate, false);
        }
    }

    @Override
    @RolesAllowed("gestioneDocContabilita")
    public AreaContabile cancellaRigaContabile(RigaContabile rigaContabile) {
        return areaContabileCancellaManager.cancellaRigaContabile(rigaContabile);
    }

    @Override
    public AreaContabile cancellaRigheContabili(AreaContabile areaContabile) {
        return areaContabileCancellaManager.cancellaRigheContabili(areaContabile);
    }

    @Override
    public AreaContabile cancellaRigheContabili(List<RigaContabile> righeContabili) {
        return areaContabileCancellaManager.cancellaRigheContabili(righeContabili);
    }

    @Override
    public AreaContabile caricaAreaContabile(Integer id) throws ContabilitaException {
        return areaContabileManager.caricaAreaContabile(id);
    }

    @Override
    public AreaContabile caricaAreaContabileByDocumento(Integer idDocumento) {
        return areaContabileManager.caricaAreaContabileByDocumento(idDocumento);
    }

    @Override
    public AreaContabileFullDTO caricaAreaContabileFullDTO(Integer id) {
        logger.debug("--> Enter caricaAreaContabileFullDTO");
        AreaContabile areaContabile = areaContabileManager.caricaAreaContabile(id);
        List<RigaContabile> listRigheContabili = areaContabileManager.caricaRigheContabili(id);
        AreaContabileFullDTO areaContabileFullDTO = new AreaContabileFullDTO();
        areaContabileFullDTO.setAreaContabile(areaContabile);
        areaContabileFullDTO.setRigheContabili(listRigheContabili);
        areaContabileFullDTO = caricaAreeDocumento(areaContabileFullDTO, areaContabile.getDocumento());
        logger.debug("--> Exit caricaAreaContabileFullDTO");
        return areaContabileFullDTO;
    }

    @Override
    public AreaContabileFullDTO caricaAreaContabileFullDTOByDocumento(Integer idDocumento) {
        AreaContabile areaContabile = areaContabileManager.caricaAreaContabileByDocumento(idDocumento);
        if (areaContabile == null) {
            return null;
        }
        List<RigaContabile> listRigheContabili = areaContabileManager.caricaRigheContabili(areaContabile.getId());
        AreaContabileFullDTO areaContabileFullDTO = new AreaContabileFullDTO();
        areaContabileFullDTO.setAreaContabile(areaContabile);
        areaContabileFullDTO.setRigheContabili(listRigheContabili);
        areaContabileFullDTO = caricaAreeDocumento(areaContabileFullDTO, areaContabile.getDocumento());
        return areaContabileFullDTO;
    }

    @Override
    public AreaContabileLite caricaAreaContabileLiteByDocumento(Integer idDocumento) {
        Documento documento = new Documento();
        documento.setId(idDocumento);
        return areaContabileManager.caricaAreaContabileLiteByDocumento(documento);
    }

    /**
     * Carica un areaDocumento.
     *
     * @param areaContabileFullDTO
     *            .
     * @param documento
     *            .
     * @return .
     */
    private AreaContabileFullDTO caricaAreeDocumento(AreaContabileFullDTO areaContabileFullDTO, Documento documento) {
        AreaIva areaIva = areaIvaManager.caricaAreaIvaByDocumento(documento);
        areaContabileFullDTO.setAreaIva(areaIva);

        AreaRate areaRate = areaRateManager.caricaAreaRate(documento);
        areaContabileFullDTO.setAreaRate(areaRate);
        // inizializzazione dell'attributo areaPartitaEnabled verificando
        // inoltre il tipo operazione presente in tipo area partita :
        // areaPartiteEnabled == true se tipoAreaPartita.tipoOperazione ==
        // TipoOperazione.NESSUNA || tipoAreaPartita.tipoOperazione ==
        // TipoOperazione.GENERA
        boolean areaRateEnabled = (areaRate != null) && (areaRate.getId() != null)
                && ((TipoAreaPartita.TipoOperazione.GENERA.equals(areaRate.getTipoAreaPartita().getTipoOperazione()))
                        || (TipoAreaPartita.TipoOperazione.NESSUNA
                                .equals(areaRate.getTipoAreaPartita().getTipoOperazione())));
        areaContabileFullDTO.setAreaRateEnabled(areaRateEnabled);

        areaContabileFullDTO.setAreaMagazzinoLite(areaMagazzinoManager.checkAreaMagazzino(documento));

        AreaTesoreria areaTesoreria = areaTesoreriaManager.checkAreaTesoreria(documento);

        if (areaTesoreria != null) {
            areaContabileFullDTO.setAreaTesoreria(areaTesoreria);
        }

        return areaContabileFullDTO;
    }

    @Override
    public List<SaldoConto> caricaBilancio(ParametriRicercaBilancio parametriRicercaBilancio)
            throws TipoDocumentoBaseException, ContabilitaException {
        logger.debug("--> Enter caricaBilancio");
        SaldoConti saldoConti = bilancioManager.caricaBilancio(parametriRicercaBilancio);
        logger.debug("--> Exit caricaBilancio");
        return saldoConti.asList();
    }

    @Override
    public List<SaldoContoConfronto> caricaBilancioConfronto(
            ParametriRicercaBilancioConfronto parametriRicercaBilancioConfronto)
                    throws TipoDocumentoBaseException, ContabilitaException {
        logger.debug("--> Enter caricaBilancioConfronto");
        List<SaldoContoConfronto> righeBilancioConfronto = bilancioManager
                .caricaBilancioConfronto(parametriRicercaBilancioConfronto);
        logger.debug("--> Exit caricaBilancioConfronto #" + righeBilancioConfronto.size());
        return righeBilancioConfronto;
    }

    @SuppressWarnings("unchecked")
    @Override
    public EstrattoConto caricaEstrattoConto(Map<Object, Object> params) {
        ParametriRicercaEstrattoConto parametriRicerca = new ParametriRicercaEstrattoConto();

        // anno
        Integer anno = (Integer) params.get("anno");
        parametriRicerca.setAnnoCompetenza(anno);
        // periodo
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
        String dataInizioString = (String) params.get("dataInizio");
        String dataFineString = (String) params.get("dataFine");
        Date dataInizio = null;
        Date dataFine = null;
        try {
            if (dataInizioString != null && !dataInizioString.isEmpty()) {
                dataInizio = df.parse(dataInizioString);
            }
            if (dataFineString != null && !dataFineString.isEmpty()) {
                dataFine = df.parse(dataFineString);
            }
        } catch (ParseException e) {
            logger.error("--> Errore durante il parse della data.", e);
        }
        Periodo periodo = new Periodo();
        periodo.setTipoPeriodo(TipoPeriodo.DATE);
        periodo.setDataIniziale(dataInizio);
        periodo.setDataFinale(dataFine);
        parametriRicerca.setDataRegistrazione(periodo);
        // sottoconto
        Integer idSottoConto = null;
        SottoConto sottoConto = null;
        if (params.get("idSottoConto") != null && ((Integer) params.get("idSottoConto")).intValue() != -1) {
            idSottoConto = (Integer) params.get("idSottoConto");
            try {
                sottoConto = pianoContiManager.caricaSottoConto(idSottoConto);
            } catch (Exception e) {
                logger.error("--> errore durante il caricamento del sottoconto", e);
                throw new RuntimeException("errore durante il caricamento del sottoconto", e);
            }
        }
        parametriRicerca.setSottoConto(sottoConto);
        // centro di costo
        Integer idCentroCosto = null;
        CentroCosto centroCosto = null;
        if (params.get("idCentroCosto") != null && ((Integer) params.get("idCentroCosto")).intValue() != -1) {
            idCentroCosto = (Integer) params.get("idCentroCosto");
            centroCosto = new CentroCosto();
            centroCosto.setId(idCentroCosto);
            centroCosto = centriCostoManager.caricaCentroCosto(centroCosto);
        }
        parametriRicerca.setCentroCosto(centroCosto);
        // stati area contabile
        String statiAreaString = (String) params.get("statiArea");
        Set<StatoAreaContabile> statiArea = new TreeSet<StatoAreaContabile>();
        if (!statiAreaString.isEmpty()) {
            String[] statiAreaSplit = statiAreaString.split(",");
            for (int i = 0; i < statiAreaSplit.length; i++) {
                Integer statoOrdinal = new Integer(statiAreaSplit[i]);
                statiArea.add(StatoAreaContabile.values()[statoOrdinal]);
            }
        }
        parametriRicerca.setStatiAreaContabile(statiArea);

        List<String> idTipiDocumento = (List<String>) params.get("tipiDocumento");
        if (idTipiDocumento == null) {
            idTipiDocumento = new ArrayList<String>();
        }
        Set<TipoDocumento> tipiDocumento = new HashSet<TipoDocumento>();
        for (String idTipoDocumento : idTipiDocumento) {
            TipoDocumento tipoDocumento = new TipoDocumento();
            tipoDocumento.setId(new Integer(idTipoDocumento));
            tipoDocumento.setVersion(0);
            tipiDocumento.add(tipoDocumento);
        }
        parametriRicerca.setTipiDocumento(tipiDocumento);

        EstrattoConto estrattoConto;
        try {
            estrattoConto = caricaEstrattoConto(parametriRicerca);
        } catch (Exception e) {
            logger.error("--> errore durante il caricamento dell'estratto conto", e);
            throw new RuntimeException("errore durante il caricamento dell'estratto conto", e);
        }
        estrattoConto.setParametriRicerca(parametriRicerca);

        if (estrattoConto.getRigheEstrattoConto() == null || estrattoConto.getRigheEstrattoConto().isEmpty()) {
            estrattoConto = null;
        }

        return estrattoConto;
    }

    @Override
    @RolesAllowed("caricaEstrattoConto")
    public EstrattoConto caricaEstrattoConto(ParametriRicercaEstrattoConto parametriRicercaEstrattoConto)
            throws ContabilitaException, TipoDocumentoBaseException {
        logger.debug("--> Enter caricaEstrattoConto");
        EstrattoConto estrattoConto = null;
        try {
            estrattoConto = estrattoContoManager.caricaEstrattoConto(parametriRicercaEstrattoConto);
        } catch (Exception e) {
            logger.error("--> errore estratto conto", e);
            throw new RuntimeException(e);
        }
        logger.debug("--> Exit caricaEstrattoConto");
        return estrattoConto;
    }

    @Override
    public List<FatturatoDTO> caricaFatturato(Map<Object, Object> parameters) {

        List<FatturatoDTO> result = new ArrayList<FatturatoDTO>();

        Object parameterDataIniziale = parameters.get("DATA__REGISTRAZIONE_INIZIALE");
        Object parameterDataFinale = parameters.get("DATA__REGISTRAZIONE_FINALE");
        Integer annoCompetenza = (Integer) parameters.get("ANNO_COMPETENZA");
        String paramTipoEntita = (String) parameters.get("TIPO_ENTITA");
        boolean fatturatoPerSedi = new Boolean((String) parameters.get("FATTURATO_PER_SEDI")).booleanValue();

        Date dataIniziale = Calendar.getInstance().getTime();
        Date dataFinale = Calendar.getInstance().getTime();

        if (parameterDataIniziale instanceof String || parameterDataFinale instanceof String) {
            String dataInizialeString = (String) parameterDataIniziale;
            String dataFinaleString = (String) parameterDataFinale;
            DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
            try {
                dataIniziale = df.parse(dataInizialeString);
                dataFinale = df.parse(dataFinaleString);
            } catch (ParseException e) {
                logger.error("--> Errore durante il parse della data.", e);
            }
        } else {
            dataIniziale = (Date) parameterDataIniziale;
            dataFinale = (Date) parameterDataFinale;
        }

        TipoEntita tipoEntita = null;
        if ("C".equals(paramTipoEntita)) {
            tipoEntita = TipoEntita.CLIENTE;
        } else if ("F".equals(paramTipoEntita)) {
            tipoEntita = TipoEntita.FORNITORE;
        }

        result = areaContabileManager.caricaFatturato(annoCompetenza, dataIniziale, dataFinale, tipoEntita);

        List<FatturatoDTO> resultPerSedi = new ArrayList<FatturatoDTO>();
        if (fatturatoPerSedi) {
            resultPerSedi = areaContabileManager.caricaFatturatoPerSede(annoCompetenza, dataIniziale, dataFinale,
                    tipoEntita);
            result.addAll(resultPerSedi);

            Collections.sort(result, new Comparator<FatturatoDTO>() {

                @Override
                public int compare(FatturatoDTO o1, FatturatoDTO o2) {
                    String denominazione1 = o1.getEntita().getAnagrafica().getDenominazione();
                    String denominazione2 = o2.getEntita().getAnagrafica().getDenominazione();

                    int compareTo = denominazione1.compareTo(denominazione2);

                    if (compareTo == 0) {
                        String cod1 = o1.isDettaglioSede() ? StringUtils.defaultString(o1.getSedeEntita().getCodice())
                                : "";
                        String cod2 = o2.isDettaglioSede() ? StringUtils.defaultString(o2.getSedeEntita().getCodice())
                                : "";
                        compareTo = cod1.compareTo(cod2);
                    }

                    return compareTo;
                }
            });
        }

        return result;
    }

    @Override
    public GiornaleIva caricaGiornaleIvaPrecedente(GiornaleIva giornaleIva) {
        return registroIvaManager.caricaGiornaleIvaPrecedente(giornaleIva);
    }

    @Override
    public Giornale caricaGiornalePrecedente(Giornale giornaleAttuale) {
        return libroGiornaleManager.caricaGiornalePrecedente(giornaleAttuale);
    }

    @Override
    public List<Giornale> caricaGiornali(int annoCompetenza) throws ContabilitaException {
        return libroGiornaleManager.caricaGiornali(annoCompetenza);
    }

    @Override
    public List<GiornaleIva> caricaGiornaliIva(Integer anno, Integer mese) {
        logger.debug("--> Enter validaRigheContabili");
        List<GiornaleIva> giornaliIva = registroIvaManager.caricaGiornaliIva(anno, mese);
        logger.debug("--> Exit validaRigheContabili");
        return giornaliIva;
    }

    @Override
    public LiquidazioneIvaDTO caricaLiquidazioneIva(Date dataInizioPeriodo, Date dataFinePeriodo)
            throws ContabilitaException {
        return liquidazioneIvaManager.calcolaLiquidazione(dataInizioPeriodo, dataFinePeriodo);
    }

    @Override
    public NoteAreaContabile caricaNoteSede(SedeEntita sedeEntita) {
        return contabilitaAnagraficaManager.caricaNoteSede(sedeEntita);
    }

    @Override
    public List<RegistroLiquidazioneDTO> caricaRegistriLiquidazione(Integer anno)
            throws ContabilitaException, TipoDocumentoBaseException {
        logger.debug("--> Enter caricaRegistroLiquidazione");
        return liquidazioneIvaManager.caricaRegistriLiquidazione(anno);
    }

    @Override
    public List<DocumentoEntitaDTO> caricaRiepilogoDocumentiBlacklist(Map<Object, Object> params) {
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
        String dataInizioString = (String) params.get("dataIniziale");
        String dataFineString = (String) params.get("dataFinale");
        Date dataIniziale = null;
        Date dataFinale = null;
        try {
            if (dataInizioString != null && !dataInizioString.isEmpty()) {
                dataIniziale = df.parse(dataInizioString);
            }
            if (dataFineString != null && !dataFineString.isEmpty()) {
                dataFinale = df.parse(dataFineString);
            }
        } catch (ParseException e) {
            logger.error("--> Errore durante il parse della data.", e);
        }

        List<DocumentoEntitaDTO> documenti = controlliManager.caricaRiepilogoDocumentiBlacklist(dataIniziale,
                dataFinale);

        return documenti;
    }

    @Override
    public RigaContabile caricaRigaContabile(Integer id) throws ContabilitaException {
        return areaContabileManager.caricaRigaContabile(id);
    }

    @Override
    public RigaContabile caricaRigaContabileLazy(Integer id) {
        return areaContabileManager.caricaRigaContabileLazy(id);
    }

    @Override
    public List<RigaContabile> caricaRigheContabili(Integer idAreaContabile) {
        return areaContabileManager.caricaRigheContabili(idAreaContabile);
    }

    @Override
    public SituazioneEpDTO caricaSituazioneEconomicoPatrimoniale(
            ParametriRicercaSituazioneEP parametriRicercaSituazioneEP) {
        SituazioneEpDTO situazioneEpDTO;
        try {
            situazioneEpDTO = situazioneEPManager.caricaSituazioneEP(parametriRicercaSituazioneEP);
        } catch (ContabilitaException e) {
            logger.debug("-->Errore caricamento situazione e/p", e);
            throw new RuntimeException(e);
        } catch (TipoDocumentoBaseException e) {
            logger.debug("-->Errore caricamento situazione e/p", e);
            throw new RuntimeException(e);
        }
        return situazioneEpDTO;
    }

    @Override
    public List<TipoDocumento> caricaTipiDocumentoByTipoRegistro(TipoRegistro tipoRegistro) {
        return areaContabileManager.caricaTipiDocumentoByTipoRegistro(tipoRegistro);
    }

    @Override
    public TipoAreaPartita caricaTipoAreaPartitaPerTipoDocumento(TipoDocumento tipoDocumento) {
        return tipiAreaPartitaManager.caricaTipoAreaPartitaPerTipoDocumento(tipoDocumento);
    }

    @Override
    public List<String> caricaVariabiliFormulaControPartite() {
        return strutturaContabileManager.caricaVariabiliFormulaControPartite();
    }

    @Override
    public List<String> caricaVariabiliFormulaStrutturaContabile() {
        return strutturaContabileManager.caricaVariabiliFormulaStrutturaContabile();
    }

    @Override
    public List<RigaContabile> creaRigheContabili(AreaContabile areaContabile, List<ControPartita> list)
            throws ContabilitaException, FormulaException, ContoRapportoBancarioAssenteException,
            ContiEntitaAssentiException {
        return strutturaContabileManager.creaRigheContabili(areaContabile, list);
    }

    @Override
    public void creaRigheContabiliAutomatiche(AreaContabile areaContabile) {
        strutturaContabileManager.creaRigheContabiliAutomatiche(areaContabile,
                Calendar.getInstance().getTimeInMillis());
    }

    @Override
    @RolesAllowed("gestioneAnnuale")
    public void eseguiAperturaContabile(ParametriAperturaContabile parametriAperturaContabile)
            throws ContabilitaException, AperturaEsistenteException, ChiusuraAssenteException, ContiBaseException,
            TipoDocumentoBaseException {
        logger.debug("--> Enter eseguiAperturaContabile");
        contabilitaAnnualeManager.eseguiAperturaConti(parametriAperturaContabile.getAnno(),
                parametriAperturaContabile.getDataMovimento());
        logger.debug("--> Exit eseguiAperturaContabile");

    }

    @Override
    @RolesAllowed("gestioneAnnuale")
    public void eseguiChiusuraContabile(ParametriChiusuraContabile parametriChiusuraContabile)
            throws TipoDocumentoBaseException, ContabilitaException, ChiusuraEsistenteException,
            DocumentiNonStampatiGiornaleException, ContiBaseException, GiornaliNonValidiException {
        logger.debug("--> Enter eseguiChiusuraContabile");
        contabilitaAnnualeManager.eseguiChiusuraConti(parametriChiusuraContabile.getAnno(),
                parametriChiusuraContabile.getDataMovimento());
        logger.debug("--> Exit eseguiChiusuraContabile");
    }

    /**
     * @return codice della azienda corrente
     */
    private String getAzienda() {
        JecPrincipal jecPrincipal = (JecPrincipal) context.getCallerPrincipal();
        return jecPrincipal.getCodiceAzienda();
    }

    @Override
    public boolean isAreaPresente(Integer idDocumento) throws AnagraficaServiceException {
        return areaContabileManager.isAreaPresente(idDocumento);
    }

    @Override
    public boolean isRigheContabiliPresenti(AreaContabile areaContabile) throws ContabilitaException {
        return areaContabileManager.isRigheContabiliPresenti(areaContabile);
    }

    @Override
    public boolean isTipoAreaPresente(Integer idTipoDocumento) throws AnagraficaServiceException {
        return areaContabileManager.isTipoAreaPresente(idTipoDocumento);
    }

    @Override
    public List<RigaContabileDTO> ricercaControlloAreeContabili(ParametriRicercaMovimentiContabili parametriRicerca) {
        return areaContabileManager.ricercaControlloAreeContabili(parametriRicerca);
    }

    @Override
    public List<TotaliCodiceIvaDTO> ricercaRigheIva(ParametriRicercaMovimentiContabili parametriRicerca) {
        logger.debug("--> Enter ricercaRigheIva");
        List<TotaliCodiceIvaDTO> righeIvaDTO = registroIvaManager.ricercaRigheIva(parametriRicerca);
        logger.debug("--> Exit ricercaRigheIva");
        return righeIvaDTO;
    }

    @Override
    public List<TotaliCodiceIvaDTO> ricercaRigheRiepilogoCodiciIva(
            ParametriRicercaMovimentiContabili parametriRicercaMovimentiContabili) {
        return registroIvaManager.ricercaRigheRiepilogoCodiciIva(parametriRicercaMovimentiContabili);
    }

    @Override
    public AreaContabile salvaAreaContabile(AreaContabile areaContabile)
            throws ContabilitaException, AreaContabileDuplicateException, DocumentoDuplicateException {
        logger.debug("--> Enter salvaAreaContabile");
        AreaContabile ac = areaContabileManager.salvaAreaContabile(areaContabile, true);
        return ac;
    }

    @Override
    @RolesAllowed("gestioneDocContabilita")
    public AreaContabileFullDTO salvaAreaContabile(AreaContabile areaContabile, AreaRate areaRate)
            throws ContabilitaException, AreaContabileDuplicateException, DocumentoDuplicateException {
        logger.debug("--> Enter salvaDocumentoContabile");
        AreaContabile areaContabileSalvata = salvaAreaContabile(areaContabile);

        // se esiste l'area partite per il documento la salva
        if (areaRate != null) {

            CodicePagamento codicePagamentoOld = null;
            CodicePagamento codicePagamentoNew = areaRate.getCodicePagamento();

            AreaRate areaRateTmp = null;
            if (areaRate.getId() != null) {
                areaRateTmp = areaRateManager.caricaAreaRate(areaRate.getId());
                codicePagamentoOld = areaRateTmp.getCodicePagamento();
                areaRate = areaRateTmp;
                areaRate.setCodicePagamento(codicePagamentoNew);
            }

            areaRate.setDocumento(areaContabileSalvata.getDocumento());
            areaRateManager.salvaAreaRate(areaRate);

            if (codicePagamentoNew != null && !codicePagamentoNew.equals(codicePagamentoOld)) {
                areaContabileManager.checkInvalidaAreeCollegate(areaContabileSalvata);
            }
        }

        AreaContabileFullDTO areaContabileFullDTO = caricaAreaContabileFullDTO(areaContabileSalvata.getId());

        logger.debug("--> Exit salvaDocumentoContabile");
        return areaContabileFullDTO;
    }

    @Override
    @RolesAllowed("gestioneLiquidazioneIva")
    public AreaContabileFullDTO salvaDocumentoLiquidazione(AreaContabile areaContabile)
            throws AreaContabileDuplicateException, DocumentoDuplicateException, FormulaException,
            CodicePagamentoNonTrovatoException, ContoRapportoBancarioAssenteException,
            TipoDocumentoNonConformeException {
        logger.debug("--> Enter salvaDocumentoLiquidazione");
        return liquidazioneIvaManager.salvaDocumentoLiquidazione(areaContabile);
    }

    @Override
    @RolesAllowed("gestioneLiquidazioneIva")
    public GiornaleIva salvaGiornaleIvaRiepilogativo(GiornaleIva giornaleIva) {
        if (giornaleIva.getRegistroIva().getTipoRegistro() != TipoRegistro.RIEPILOGATIVO) {
            throw new RuntimeException("Il registro iva non è di tipologia riepilogativo");
        }
        return registroIvaManager.salvaGiornaleIva(giornaleIva);
    }

    @Override
    @RolesAllowed("gestioneDocContabilita")
    public RigaContabile salvaRigaContabile(RigaContabile rigaContabile) {
        logger.debug("--> Enter salvaRigaContabile");
        rigaContabile = areaContabileManager.salvaRigaContabile(rigaContabile);
        // Chiamando la sava dal service stacco la riga contabile dalla
        // sessione,
        // quindi inizializzo le liste lazy.
        rigaContabile.getRigheCentroCosto().size();
        logger.debug("--> Exit salvaRigaContabile");
        return rigaContabile;
    }

    @Override
    @RolesAllowed("gestioneDocContabilita")
    public void validaAreaContabile(int idAreaContabile) throws ContabilitaException, RigheContabiliNonValidiException {
        AreaContabile areaContabile = areaContabileManager.caricaAreaContabile(idAreaContabile);
        areaContabile = areaContabileManager.validaRigheContabili(areaContabile);
        AreaRate areaRate = areaRateManager.caricaAreaRate(areaContabile.getDocumento());
        if (areaRate.getId() != null) {
            areaRateManager.validaAreaRate(areaRate, areaContabile);
        }
    }

    @Override
    public AreaContabileFullDTO validaAreaIva(AreaIva areaIva, Integer idAreaContabile) {
        areaIvaManager.validaAreaIva(areaIva);
        return caricaAreaContabileFullDTO(idAreaContabile);
    }

    @Override
    @RolesAllowed("gestioneDocContabilita")
    public AreaContabileFullDTO validaRigheContabili(AreaContabile areaContabile)
            throws ContabilitaException, RigheContabiliNonValidiException {
        logger.debug("--> Enter validaRigheContabili");
        AreaContabile areaContabileSalvata = areaContabileManager.validaRigheContabili(areaContabile);
        AreaRate areaRate = areaRateManager.caricaAreaRate(areaContabileSalvata.getDocumento());
        if (areaRate.getId() != null && areaRate.isGenerazioneRateAllowed()) {
            if (BigDecimal.ZERO.compareTo(areaContabile.getDocumento().getTotale().getImportoInValutaAzienda()) != 0) {
                rateGenerator.generaRate(areaContabileSalvata, areaRate);
            } else {
                areaRateManager.validaAreaRate(areaRate, areaContabileSalvata);
            }
        }
        AreaContabileFullDTO areaContabileFullDTO = caricaAreaContabileFullDTO(areaContabileSalvata.getId());
        logger.debug("--> Exit validaRigheContabili");
        return areaContabileFullDTO;
    }

    @Override
    public List<RisultatoControlloProtocolli> verificaDataProtocolli(Map<Object, Object> params) {
        logger.debug("--> Enter verificaDataProtocolli");
        Integer annoVerifica = (Integer) params.get("anno");
        List<RisultatoControlloProtocolli> result = controlliManager.verificaDataProtocolli(annoVerifica);
        logger.debug("--> Exit verificaDataProtocolli");
        return result;
    }

    @Override
    public List<RisultatoControlloProtocolli> verificaProtocolliMancanti(Map<Object, Object> params) {
        logger.debug("--> Enter verificaProtocolliMancanti");
        Integer annoVerifica = (Integer) params.get("anno");
        List<RisultatoControlloProtocolli> result = controlliManager.verificaProtocolliMancanti(annoVerifica);
        logger.debug("--> Exit verificaProtocolliMancanti");
        return result;
    }

    @Override
    public List<AreaContabileDTO> verificaRigheSenzaCentriDiCosto() {
        return controlliManager.verificaRigheSenzaCentriDiCosto();
    }

    @Override
    public List<RisultatoControlloRateSaldoContabile> verificaSaldi(Map<Object, Object> parametri) {
        String sottoTipoContoValue = (String) parametri.get("sottotipoConto");
        if (sottoTipoContoValue == null) {
            throw new IllegalArgumentException("Manca il parametro sottotipoConto. Sever il values dell'enum.");
        }
        SottotipoConto sottoTipoConto = SottotipoConto.valueOf(sottoTipoContoValue);
        if (sottoTipoConto == null) {
            throw new IllegalArgumentException(
                    "Il valore del sottotipoConto è invalido. Valore =" + sottoTipoContoValue);
        }
        return controlliManager.verificaSaldi(sottoTipoConto);
    }

    @Override
    public List<RisultatoControlloRateSaldoContabile> verificaSaldi(SottotipoConto sottotipoConto) {
        return controlliManager.verificaSaldi(sottotipoConto);
    }

    @Override
    public List<?>[] verificheContabili(Map<Object, Object> params) {
        return new ArrayList<?>[] { (ArrayList<RisultatoControlloProtocolli>) verificaProtocolliMancanti(params),
                (ArrayList<RisultatoControlloProtocolli>) verificaDataProtocolli(params) };
    }
}
