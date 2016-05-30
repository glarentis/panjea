package it.eurotn.panjea.tesoreria.service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.proxy.HibernateProxy;
import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.documenti.manager.interfaces.DocumentiManager;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentoDuplicateException;
import it.eurotn.panjea.anagrafica.domain.Banca;
import it.eurotn.panjea.anagrafica.domain.CategoriaEntita;
import it.eurotn.panjea.anagrafica.domain.RapportoBancarioAzienda;
import it.eurotn.panjea.anagrafica.domain.RapportoBancarioSedeEntita;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.TipoPagamento;
import it.eurotn.panjea.anagrafica.domain.ZonaGeografica;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.service.exception.CambioNonPresenteException;
import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.contabilita.domain.AreaContabileLite;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileManager;
import it.eurotn.panjea.contabilita.manager.liquidazione.interfaces.LiquidazionePagamentiManager;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.pagamenti.service.exception.PagamentiException;
import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.panjea.parametriricerca.domain.Periodo.TipoPeriodo;
import it.eurotn.panjea.partite.domain.TipoAreaPartita;
import it.eurotn.panjea.partite.domain.TipoAreaPartita.TipoPartita;
import it.eurotn.panjea.partite.domain.TipoDocumentoBasePartite;
import it.eurotn.panjea.partite.domain.TipoDocumentoBasePartite.TipoOperazione;
import it.eurotn.panjea.partite.manager.interfaces.TipiAreaPartitaManager;
import it.eurotn.panjea.partite.util.ParametriRicercaRate;
import it.eurotn.panjea.rate.domain.Rata;
import it.eurotn.panjea.rate.domain.Rata.StatoRata;
import it.eurotn.panjea.rate.manager.interfaces.PagamentiAccontoGenerator;
import it.eurotn.panjea.rate.manager.interfaces.RateManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.tesoreria.domain.AreaAcconto;
import it.eurotn.panjea.tesoreria.domain.AreaAccredito;
import it.eurotn.panjea.tesoreria.domain.AreaAccreditoAssegno;
import it.eurotn.panjea.tesoreria.domain.AreaAnticipo;
import it.eurotn.panjea.tesoreria.domain.AreaAssegno;
import it.eurotn.panjea.tesoreria.domain.AreaBonifico;
import it.eurotn.panjea.tesoreria.domain.AreaChiusure;
import it.eurotn.panjea.tesoreria.domain.AreaDistintaBancaria;
import it.eurotn.panjea.tesoreria.domain.AreaEffetti;
import it.eurotn.panjea.tesoreria.domain.AreaInsoluti;
import it.eurotn.panjea.tesoreria.domain.AreaPagamenti;
import it.eurotn.panjea.tesoreria.domain.AreaTesoreria;
import it.eurotn.panjea.tesoreria.domain.Effetto;
import it.eurotn.panjea.tesoreria.domain.Pagamento;
import it.eurotn.panjea.tesoreria.domain.TesoreriaSettings;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaAccontoManager;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaAccreditoAssegnoManager;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaAccreditoManager;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaAnticipiManager;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaAssegnoManager;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaBonificoManager;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaChiusureManager;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaDistintaBancariaManager;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaEffettiManager;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaInsolutoManager;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaTesoreriaManager;
import it.eurotn.panjea.tesoreria.manager.interfaces.FlussiCBIManager;
import it.eurotn.panjea.tesoreria.manager.interfaces.StatisticheTesoreriaManager;
import it.eurotn.panjea.tesoreria.manager.interfaces.TesoreriaSettingsManager;
import it.eurotn.panjea.tesoreria.service.exception.CodiceSIAAssenteException;
import it.eurotn.panjea.tesoreria.service.exception.DataRichiestaDopoIncassoException;
import it.eurotn.panjea.tesoreria.service.exception.EntitaRateNonCoerentiException;
import it.eurotn.panjea.tesoreria.service.exception.RapportoBancarioPerFlussoAssenteException;
import it.eurotn.panjea.tesoreria.service.exception.TipoDocumentoChiusuraAssenteException;
import it.eurotn.panjea.tesoreria.service.interfaces.TesoreriaService;
import it.eurotn.panjea.tesoreria.solleciti.RigaSollecito;
import it.eurotn.panjea.tesoreria.solleciti.Sollecito;
import it.eurotn.panjea.tesoreria.solleciti.TemplateSolleciti;
import it.eurotn.panjea.tesoreria.solleciti.manager.interfaces.SollecitiManager;
import it.eurotn.panjea.tesoreria.solleciti.manager.interfaces.TemplateSollecitiManager;
import it.eurotn.panjea.tesoreria.util.AssegnoDTO;
import it.eurotn.panjea.tesoreria.util.EffettoDTO;
import it.eurotn.panjea.tesoreria.util.EntitaSituazioneRata;
import it.eurotn.panjea.tesoreria.util.ParametriCreazioneAreaChiusure;
import it.eurotn.panjea.tesoreria.util.ParametriRicercaAreaChiusure;
import it.eurotn.panjea.tesoreria.util.ParametriRicercaAreeTesoreria;
import it.eurotn.panjea.tesoreria.util.ParametriRicercaAssegni;
import it.eurotn.panjea.tesoreria.util.ParametriRicercaEffetti;
import it.eurotn.panjea.tesoreria.util.SituazioneEffetto;
import it.eurotn.panjea.tesoreria.util.SituazioneRata;
import it.eurotn.panjea.tesoreria.util.SituazioneRigaAnticipo;
import it.eurotn.panjea.tesoreria.util.parametriricerca.ParametriRicercaAcconti;

@Stateless(name = "Panjea.TesoreriaService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.TesoreriaService")
public class TesoreriaServiceBean implements TesoreriaService {

    private static Logger logger = Logger.getLogger(TesoreriaServiceBean.class);

    @EJB
    private AreaChiusureManager areaChiusureManager;

    @EJB
    private TesoreriaSettingsManager tesoreriaSettingsManager;

    @EJB
    private AreaAssegnoManager areaAssegnoManager;

    @EJB
    private RateManager rateManager;

    @EJB
    private AreaTesoreriaManager areaTesoreriaManager;

    @EJB
    private AreaDistintaBancariaManager areaDistintaBancariaManager;

    @EJB
    private AreaBonificoManager areaBonificoManager;

    @EJB
    private AreaContabileManager areaContabileManager;

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    private AreaEffettiManager areaEffettiManager;

    @EJB
    private AreaAccreditoManager areaAccreditoManager;

    @EJB
    private AreaAccreditoAssegnoManager areaAccreditoAssegnoManager;

    @EJB
    private FlussiCBIManager flussiCBIManager;

    @EJB
    private StatisticheTesoreriaManager statisticheTesoreriaManager;

    @EJB
    private AreaAccontoManager areaAccontoManager;

    @EJB
    private AreaInsolutoManager areaInsolutoManager;

    @EJB
    private AreaAnticipiManager areaAnticipiManager;

    @EJB
    private TipiAreaPartitaManager tipiAreaPartitaManager;

    @EJB
    private TemplateSollecitiManager templateSollecitiManager;

    @EJB
    private SollecitiManager sollecitiManager;

    @EJB
    private PagamentiAccontoGenerator pagamentiAccontoGenerator;

    @EJB
    private LiquidazionePagamentiManager liquidazionePagamentiManager;

    @EJB
    private DocumentiManager documentiManager;

    @Override
    public void annullaInsoluto(Effetto effetto) {
    }

    @Override
    public void assegnaRapportoBancarioAssegni(RapportoBancarioAzienda rapportoBancarioAzienda,
            List<AreaAssegno> areeAssegno) {
        areaAssegnoManager.assegnaRapportoBancarioAssegni(rapportoBancarioAzienda, areeAssegno);
    }

    @Override
    public BigDecimal calcolaImportoRateAperte(EntitaLite entita) {
        return statisticheTesoreriaManager.calcolaImportoRateAperte(entita);
    }

    @Override
    public void cancellaAcconto(AreaAcconto areaAcconto) {
        areaAccontoManager.cancellaAreaTesoreria(areaAcconto, true);
    }

    @Override
    public void cancellaAreaTesoreria(AreaTesoreria areaTesoreria) {
        areaTesoreriaManager.cancellaAreaTesoreria(areaTesoreria, true);
    }

    @Override
    public void cancellaAreeTesorerie(List<AreaTesoreria> listAree) {
        areaTesoreriaManager.cancellaAreeTesorerie(listAree, true);
    }

    @Override
    public void cancellaEffetto(Effetto effetto) {
        areaEffettiManager.cancellaEffetto(effetto);
    }

    @Override
    public void cancellaPagamento(Pagamento pagamento) {
    }

    @Override
    public void cancellaPagamentoAccontoLiquidazione(Pagamento pagamento) {
        liquidazionePagamentiManager.cancellaPagamentoAccontoLiquidazione(pagamento);
    }

    @Override
    public void cancellaSollecito(Sollecito sollecito) {
        sollecitiManager.cancellaSollecito(sollecito);
    }

    @Override
    public void cancellaTemplateSolleciti(TemplateSolleciti templateSolleciti) {
        templateSollecitiManager.cancellaTemplateSolleciti(templateSolleciti);
    }

    @Override
    public List<AreaAcconto> caricaAreaAcconti(ParametriRicercaAcconti parametriRicercaAcconti) {
        return areaAccontoManager.caricaAreaAcconti(parametriRicercaAcconti);
    }

    @Override
    public AreaContabileLite caricaAreaContabileLite(AreaTesoreria areaTesoreria) {
        return areaContabileManager.caricaAreaContabileLiteByDocumento(areaTesoreria.getDocumento());
    }

    @Override
    public AreaTesoreria caricaAreaTesoreria(AreaTesoreria areaTesoreria) {
        try {
            return areaTesoreriaManager.caricaAreaTesoreria(areaTesoreria);
        } catch (ObjectNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @RolesAllowed("gestioneDocContabilita")
    public AreaTesoreria caricaAreaTesoreria(Documento documento) {
        return areaTesoreriaManager.caricaAreaTesoreria(documento);
    }

    @Override
    public AreaTesoreria caricaAreaTesoreria(Pagamento pagamento) {
        AreaTesoreria areaTesoreria = null;
        try {
            pagamento = panjeaDAO.load(Pagamento.class, pagamento.getId());
            Object area = null;
            if (pagamento.getAreaChiusure() != null) {
                area = pagamento.getAreaChiusure();
            } else if (pagamento.getAreaAcconto() != null) {
                area = pagamento.getAreaAcconto();
            }

            if (area instanceof HibernateProxy) {
                HibernateProxy proxy = (HibernateProxy) area;
                areaTesoreria = areaTesoreriaManager
                        .caricaAreaTesoreria((AreaTesoreria) proxy.getHibernateLazyInitializer().getImplementation());
            } else {
                areaTesoreria = areaTesoreriaManager.caricaAreaTesoreria((AreaTesoreria) area);
            }
        } catch (Exception e) {
            throw new RuntimeException("Errore durante il caricamento del pagamento", e);
        }
        return areaTesoreria;
    }

    @Override
    public AreaTesoreria caricaAreaTesoreriaByStatoEffetto(Effetto effetto) {

        AreaTesoreria areaTesoreria = null;

        try {
            // ricarico l'effetto
            effetto = panjeaDAO.load(Effetto.class, effetto.getId());

            switch (effetto.getStatoEffetto()) {
            case INSOLUTO:
                areaTesoreria = areaInsolutoManager.caricaAreaTesoreria(effetto.getAreaInsoluti().getId());
                break;
            case PRESENTATO:
                areaTesoreria = areaDistintaBancariaManager.caricaAreaDistinta(effetto.getAreaEffetti());
                break;
            case CHIUSO:
                areaTesoreria = areaAccreditoManager.caricaAreaTesoreria(effetto.getAreaAccredito().getId());
                break;
            default:
                break;
            }
        } catch (Exception e) {
            logger.error("--> errore durante il caricamento dell'area tesoreria in base allora stato dell'effetto "
                    + effetto.getId(), e);
            throw new RuntimeException(
                    "errore durante il caricamento dell'area tesoreria in base allora stato dell'effetto "
                            + effetto.getId(),
                    e);
        }
        return areaTesoreria;
    }

    @Override
    public List<AreaTesoreria> caricaAreeCollegate(AreaTesoreria areaTesoreria) {
        List<AreaEffetti> lisAreeEmissione = areaTesoreriaManager.getAreeEmissioneEffetti(areaTesoreria);
        List<AreaTesoreria> listAreeCollegate = new ArrayList<AreaTesoreria>();
        for (AreaEffetti areaEmissione : lisAreeEmissione) {
            listAreeCollegate.addAll(areaTesoreriaManager.caricaAreeCollegate(areaEmissione));
        }

        // TODO rivedere getAreeEmissioneEffetti ? aggiungere getAreeEmissionePagamenti ?
        if (areaTesoreria instanceof AreaPagamenti) {
            AreaPagamenti areaPagamenti = (AreaPagamenti) areaTesoreria;

            AreaBonifico bonifico = areaBonificoManager.caricaAreaBonifico(areaPagamenti);
            if (bonifico != null) {
                listAreeCollegate.add(bonifico);
            }
        }

        return listAreeCollegate;
    }

    @Override
    public List<Object> caricaAreeDocumento(Integer idDocumento) {
        return documentiManager.caricaAreeDocumento(idDocumento);
    }

    @Override
    @RolesAllowed("gestionePagamenti")
    public List<AssegnoDTO> caricaAssegni(ParametriRicercaAssegni parametriRicercaAssegni) {
        return areaAssegnoManager.caricaAssegni(parametriRicercaAssegni);
    }

    @Override
    public List<EffettoDTO> caricaEffettiDistintePerStampa(Map<Object, Object> parametri)
            throws RapportoBancarioPerFlussoAssenteException {

        List<TipoAreaPartita> tipoAreaPartiteDistinta = new ArrayList<TipoAreaPartita>();

        List<TipoDocumentoBasePartite> tipiDocumentoBase = tipiAreaPartitaManager.caricaTipiDocumentoBase();
        for (TipoDocumentoBasePartite tipoDocumentoBasePartite : tipiDocumentoBase) {
            tipoAreaPartiteDistinta.add(tipoDocumentoBasePartite.getTipoAreaPartita());
        }

        List<EffettoDTO> effetti = new ArrayList<EffettoDTO>();

        String areeDaStampare = (String) parametri.get("idAree");
        String[] areeArray = areeDaStampare.split(",");

        List<EntitaLite> entitaSenzaRapportoBancario = new ArrayList<>();

        for (String idArea : areeArray) {
            AreaTesoreria areaTesoreria = null;
            try {
                // faccio la load di area effetti perchè mi servono solo quelle,
                // in questo modo scarto immediatamete
                // tutte le altre. Se l'area che voglio caricare non è un'area
                // effetti mi verrà sollevata una
                // ObjectNotFoundException che vado a gestire
                areaTesoreria = panjeaDAO.load(AreaEffetti.class, new Integer(idArea));
                areaTesoreria = caricaAreaTesoreria(areaTesoreria);
            } catch (ObjectNotFoundException e) {
                areaTesoreria = null;
            } catch (Exception e) {
                logger.error("--> errore durante il caricamento dell'area tesoreria", e);
                throw new RuntimeException("errore durante il caricamento dell'area tesoreria", e);
            }

            if (areaTesoreria != null && !tipoAreaPartiteDistinta.contains(areaTesoreria.getTipoAreaPartita())) {
                for (Effetto effetto : ((AreaEffetti) areaTesoreria).getEffetti()) {

                    RapportoBancarioAzienda rapportoBancarioAzienda = areaTesoreria.getDocumento()
                            .getRapportoBancarioAzienda();
                    RapportoBancarioSedeEntita rapportoBancarioSedeEntita = effetto.getRata()
                            .getRapportoBancarioEntita();
                    if (rapportoBancarioSedeEntita == null) {
                        Rata rata = rateManager.associaRapportoBancario(effetto.getRata(),
                                effetto.getRata().getAreaRate(),
                                ((AreaEffetti) areaTesoreria).getTipoAreaPartita().getTipoPagamentoChiusura(), true);
                        if (rata.getRapportoBancarioEntita() == null) {
                            entitaSenzaRapportoBancario.add(effetto.getRata().getAreaRate().getDocumento().getEntita());
                        } else {
                            rapportoBancarioSedeEntita = rata.getRapportoBancarioEntita();
                        }
                    }
                    effetti.add(new EffettoDTO(effetto, rapportoBancarioAzienda, rapportoBancarioSedeEntita));
                }
            }
        }

        if (!entitaSenzaRapportoBancario.isEmpty()) {
            throw new RapportoBancarioPerFlussoAssenteException(entitaSenzaRapportoBancario);
        }

        return effetti;
    }

    @Override
    public AreaAssegno caricaImmagineAssegno(AreaAssegno areaAssegno) {
        return areaAssegnoManager.caricaImmagineAssegno(areaAssegno);
    }

    @Override
    public BigDecimal caricaImportoAnticipato(AreaEffetti areaEffetti, RapportoBancarioAzienda rapportoBancarioAzienda,
            Date dataValuta) {
        return areaAnticipiManager.caricaImportoAnticipato(areaEffetti, rapportoBancarioAzienda, dataValuta);
    }

    @Override
    public List<Pagamento> caricaPagamenti(Integer idRata) {
        Rata rata = new Rata();
        rata.setId(idRata);
        return areaChiusureManager.caricaPagamenti(rata);
    }

    @Override
    public List<Pagamento> caricaPagamentiByAreaAcconto(AreaAcconto areaAcconto) {
        return areaAccontoManager.caricaPagamentiByAreaAcconto(areaAcconto);
    }

    @Override
    public Rata caricaRata(Integer idRata) {
        return rateManager.caricaRata(idRata);
    }

    @Override
    public List<RigaSollecito> caricaRigheSollecito(Map<Object, Object> parametri) {
        return sollecitiManager.caricaRigheSollecito(parametri);
    }

    @Override
    public TesoreriaSettings caricaSettings() {
        return tesoreriaSettingsManager.caricaSettings();
    }

    @Override
    public List<SituazioneRata> caricaSituazioneRate(Map<Object, Object> parametri) {
        ParametriRicercaRate parametriRicerca = new ParametriRicercaRate();

        // stati rata
        String statiRataString = (String) parametri.get("statiRata");
        Set<StatoRata> statiRata = new TreeSet<StatoRata>();
        if (!statiRataString.isEmpty()) {
            String[] statiRataSplit = statiRataString.split(",");
            for (int i = 0; i < statiRataSplit.length; i++) {
                Integer statoOrdinal = new Integer(statiRataSplit[i]);
                statiRata.add(StatoRata.values()[statoOrdinal]);
            }
        }
        parametriRicerca.setStatiRata(statiRata);

        // tipi pagamento
        String tipiPagamentoString = (String) parametri.get("tipiPagamento");
        Set<TipoPagamento> tipiPagamento = new TreeSet<TipoPagamento>();
        if (!tipiPagamentoString.isEmpty()) {
            String[] tipiPagamentoSplit = tipiPagamentoString.split(",");
            for (int i = 0; i < tipiPagamentoSplit.length; i++) {
                Integer tipoPagamentoOrdinal = new Integer(tipiPagamentoSplit[i]);
                tipiPagamento.add(TipoPagamento.values()[tipoPagamentoOrdinal]);
            }
        }
        parametriRicerca.setTipiPagamento(tipiPagamento);
        parametriRicerca.setTipiPagamentoEsclusi(null);

        // tipo partita
        Integer tipoPartInt = (Integer) parametri.get("tipoPartita");
        TipoPartita tipoPartita = TipoPartita.values()[tipoPartInt];
        parametriRicerca.setTipoPartita(tipoPartita);

        // data scadenza
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
        String dataInizioString = (String) parametri.get("dataInizio");
        String dataFineString = (String) parametri.get("dataFine");
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
        parametriRicerca.setDataScadenza(periodo);

        // entità
        Integer idEntita = null;
        Integer idSedeEntita = null;
        if (parametri.get("idEntita") != null && !((String) parametri.get("idEntita")).trim().isEmpty()) {
            idEntita = Integer.parseInt(((String) parametri.get("idEntita")));
            // sede entità
            if (parametri.get("idSedeEntita") != null && !((String) parametri.get("idSedeEntita")).trim().isEmpty()) {
                idSedeEntita = Integer.parseInt(((String) parametri.get("idSedeEntita")));
            }
        }
        EntitaLite entitaLite = new EntitaLite();
        entitaLite.setId(idEntita);
        parametriRicerca.setEntita(entitaLite);
        SedeEntita sedeEntita = new SedeEntita();
        sedeEntita.setId(idSedeEntita);
        parametriRicerca.setSedeEntita(sedeEntita);

        // codice pagamento
        Integer idCodicePagamento = null;
        if (parametri.get("idCodicePagamento") != null
                && !((String) parametri.get("idCodicePagamento")).trim().isEmpty()) {
            idCodicePagamento = Integer.parseInt((String) parametri.get("idCodicePagamento"));
        }
        CodicePagamento codicePagamento = new CodicePagamento();
        codicePagamento.setId(idCodicePagamento);
        parametriRicerca.setCodicePagamento(codicePagamento);

        // importi e valuta
        BigDecimal importoIniziale = null;
        BigDecimal importoFinale = null;
        String codiceValuta = null;
        if (parametri.get("importoIniziale") != null && !((String) parametri.get("importoIniziale")).isEmpty()) {
            importoIniziale = new BigDecimal((String) parametri.get("importoIniziale"));
        }
        if (parametri.get("importoFinale") != null && !((String) parametri.get("importoFinale")).isEmpty()) {
            importoFinale = new BigDecimal((String) parametri.get("importoFinale"));
        }
        if (parametri.get("codiceValuta") != null && !((String) parametri.get("codiceValuta")).isEmpty()) {
            codiceValuta = (String) parametri.get("codiceValuta");
        }
        parametriRicerca.setImportoIniziale(importoIniziale);
        parametriRicerca.setImportoFinale(importoFinale);
        parametriRicerca.setCodiceValuta(codiceValuta);

        // rapporto bancario
        Integer idRapportoBancario = null;
        if (parametri.get("idRapportoBancario") != null && !((String) parametri.get("idRapportoBancario")).isEmpty()) {
            idRapportoBancario = Integer.parseInt(((String) parametri.get("idRapportoBancario")));
        }
        RapportoBancarioAzienda rapportoBancarioAzienda = new RapportoBancarioAzienda();
        rapportoBancarioAzienda.setId(idRapportoBancario);
        parametriRicerca.setRapportoBancarioAzienda(rapportoBancarioAzienda);

        // banca entita
        Integer idBancaEntita = null;
        if (parametri.get("idBancaEntita") != null && !((String) parametri.get("idBancaEntita")).isEmpty()) {
            idBancaEntita = Integer.parseInt(((String) parametri.get("idBancaEntita")));
        }
        Banca bancaEntita = new Banca();
        bancaEntita.setId(idBancaEntita);
        parametriRicerca.setBancaEntita(bancaEntita);

        // categoria entità
        Integer idCategoriaEntita = null;
        CategoriaEntita categoriaEntita = null;
        if (parametri.get("idCategoriaEntita") != null && !((String) parametri.get("idCategoriaEntita")).isEmpty()) {
            idCategoriaEntita = Integer.parseInt(((String) parametri.get("idCategoriaEntita")));
            categoriaEntita = new CategoriaEntita();
            categoriaEntita.setId(idCategoriaEntita);
        }
        parametriRicerca.setCategoriaEntita(categoriaEntita);

        // zona geografica
        Integer idZona = null;
        ZonaGeografica zonaGeografica = null;
        if (parametri.get("idZona") != null && !((String) parametri.get("idZona")).isEmpty()) {
            idZona = Integer.parseInt(((String) parametri.get("idZona")));
            zonaGeografica = new ZonaGeografica();
            zonaGeografica.setId(idZona);
        }
        parametriRicerca.setZonaGeografica(zonaGeografica);

        // agente
        Integer idAgente = null;
        AgenteLite agente = null;
        if (parametri.get("idAgente") != null && !((String) parametri.get("idAgente")).isEmpty()) {
            idAgente = Integer.parseInt(((String) parametri.get("idAgente")));
            agente = new AgenteLite();
            agente.setId(idAgente);
        }
        parametriRicerca.setAgente(agente);

        Boolean stampaDettaglio = null;
        if (parametri.get("stampaDettaglio") != null) {
            stampaDettaglio = new Boolean((String) parametri.get("stampaDettaglio"));
        }
        parametriRicerca.setStampaDettaglio(stampaDettaglio);

        return rateManager.ricercaRate(parametriRicerca);
    }

    /**
     *
     * @param idEntita
     *            id dell'entità per la quale caricare le rate da poter utilizzare per l'acconto.
     * @param tipoPartita
     *            tipo partita ATTIVA/PASSIVA
     * @return list di rate rate da poter pagare con un acconto.
     */
    @Override
    public List<SituazioneRata> caricaSituazioneRateDaUtilizzarePerAcconto(Integer idEntita, TipoPartita tipoPartita) {
        return statisticheTesoreriaManager.caricaSituazioneRateDaUtilizzarePerAcconto(idEntita, tipoPartita);
    }

    @Override
    public List<EntitaSituazioneRata> caricaSituazioneRateStampa(Map<Object, Object> parametri) {
        List<SituazioneRata> situazioneRate = caricaSituazioneRate(parametri);

        // carico le zone geografiche
        StringBuilder sb = new StringBuilder();
        sb.append("select distinct se.entita_id,zona.codice ");
        sb.append("from anag_sedi_entita se inner join anag_tipo_sede_entita tse on se.tipoSede_id = tse.id ");
        sb.append("					left join anag_zone_geografiche zona on se.zonaGeografica_id = zona.id ");
        sb.append("where tse.sede_principale = true ");
        Query query = panjeaDAO.getEntityManager().createNativeQuery(sb.toString());
        @SuppressWarnings("unchecked")
        List<Object[]> zone = query.getResultList();
        Map<Integer, String> zoneMap = new HashMap<Integer, String>();
        for (Object[] objects : zone) {
            zoneMap.put((Integer) objects[0], (String) objects[1]);
        }

        // raggruppo per rate per entita
        Map<Integer, List<SituazioneRata>> tmpEntita = new HashMap<Integer, List<SituazioneRata>>();
        for (SituazioneRata situazioneRata : situazioneRate) {
            if (situazioneRata.getEntita().getTipoEntita() != TipoEntita.AZIENDA) {
                if (tmpEntita.containsKey(situazioneRata.getEntita().getId())) {
                    tmpEntita.get(situazioneRata.getEntita().getId()).add(situazioneRata);
                } else {
                    List<SituazioneRata> rateTmp = new ArrayList<SituazioneRata>();
                    rateTmp.add(situazioneRata);
                    tmpEntita.put(situazioneRata.getEntita().getId(), rateTmp);
                }
            }
        }

        // creo le entità
        List<EntitaSituazioneRata> entitaSituazioniRata = new ArrayList<EntitaSituazioneRata>();
        for (Entry<Integer, List<SituazioneRata>> entry : tmpEntita.entrySet()) {
            EntitaSituazioneRata entita = new EntitaSituazioneRata();
            entita.setEntita(entry.getValue().get(0).getEntita());
            entita.setTipoPagamento(entry.getValue().get(0).getRata().getTipoPagamento());
            entita.setZona(zoneMap.get(entry.getValue().get(0).getEntita().getId()));
            // rate
            entita.setSituazioneRate(entry.getValue());
            entitaSituazioniRata.add(entita);
        }

        // ordino in base a zona e codice entità
        Comparator<EntitaSituazioneRata> comparator = new Comparator<EntitaSituazioneRata>() {

            @Override
            public int compare(EntitaSituazioneRata o1, EntitaSituazioneRata o2) {
                String zona1 = o1.getZona() == null ? "" : o1.getZona();
                String zona2 = o2.getZona() == null ? "" : o2.getZona();
                if (zona1.compareTo(zona2) != 0) {
                    return zona1.compareTo(zona2);
                } else {
                    return o1.getEntita().getCodice().compareTo(o2.getEntita().getCodice());
                }
            }
        };
        Collections.sort(entitaSituazioniRata, comparator);

        return entitaSituazioniRata;
    }

    @Override
    public List<Sollecito> caricaSolleciti() throws PagamentiException {
        return sollecitiManager.caricaSolleciti();
    }

    @Override
    public List<Sollecito> caricaSollecitiByCliente(Integer idCliente) throws PagamentiException {
        return sollecitiManager.caricaSollecitiByCliente(idCliente);
    }

    @Override
    public List<Sollecito> caricaSollecitiByRata(Integer idRata) throws PagamentiException {
        return sollecitiManager.caricaSollecitiByRata(idRata);
    }

    @Override
    public List<TemplateSolleciti> caricaTemplateSolleciti() throws PagamentiException {
        return templateSollecitiManager.caricaTemplateSolleciti();
    }

    @Override
    public TemplateSolleciti caricaTemplateSollecito(int idTemplateSollecito) {
        return templateSollecitiManager.caricaTemplateSollecito(idTemplateSollecito);
    }

    @Override
    public TipoDocumentoBasePartite caricaTipoDocumentoBase(TipoOperazione tipoOperazione)
            throws TipoDocumentoBaseException {
        return tipiAreaPartitaManager.caricaTipoDocumentoBase(tipoOperazione);
    }

    @Override
    public AreaAccreditoAssegno creaAreaAccreditoAssegno(ParametriCreazioneAreaChiusure parametriCreazioneAreaChiusure,
            List<AreaAssegno> assegni) {
        return areaAccreditoAssegnoManager.creaAreaAccreditoAssegno(parametriCreazioneAreaChiusure, assegni);
    }

    @Override
    public AreaAnticipo creaAreaAnticipo(List<SituazioneRigaAnticipo> situazioneRigaAnticipo) {
        return areaAnticipiManager.creaAreaAnticipo(situazioneRigaAnticipo);
    }

    @Override
    @RolesAllowed("gestionePagamenti")
    public AreaBonifico creaAreaBonifico(Date dataDocumento, String numeroDocumento, AreaPagamenti areaPagamenti,
            BigDecimal spese, Set<Pagamento> pagamenti) throws TipoDocumentoBaseException {
        return areaBonificoManager.creaAreaBonifico(dataDocumento, numeroDocumento, areaPagamenti, spese, pagamenti);
    }

    @Override
    @RolesAllowed("gestionePagamenti")
    public List<AreaChiusure> creaAreaChiusure(ParametriCreazioneAreaChiusure parametriCreazioneAreaChiusure,
            List<Pagamento> pagamenti)
                    throws DocumentoDuplicateException, CambioNonPresenteException, EntitaRateNonCoerentiException {
        return areaChiusureManager.creaAreaChiusure(parametriCreazioneAreaChiusure, pagamenti);
    }

    @Override
    @RolesAllowed("gestionePagamenti")
    public AreaDistintaBancaria creaAreaDistintaBancaria(Date dataDocumento, String nDocumento, AreaEffetti areaEffetti,
            BigDecimal spese, BigDecimal speseDistinta, Set<Effetto> effetti) throws TipoDocumentoBaseException {
        return areaDistintaBancariaManager.creaAreaDistintaBancaria(dataDocumento, nDocumento, areaEffetti, spese,
                speseDistinta, effetti);
    }

    @Override
    @RolesAllowed("gestionePagamenti")
    public AreaInsoluti creaAreaInsoluti(Date dataDocumento, String nDocumento, BigDecimal speseInsoluto,
            Set<Effetto> effetti) {
        return areaInsolutoManager.creaAreaInsoluti(dataDocumento, nDocumento, speseInsoluto, effetti);
    }

    @Override
    @RolesAllowed("gestionePagamenti")
    public List<AreaAccredito> creaAreeAccredito(List<Effetto> effetti, Date dataScritturaPosticipata)
            throws DataRichiestaDopoIncassoException {
        return areaAccreditoManager.creaAreeAccredito(effetti, dataScritturaPosticipata);
    }

    @Override
    public List<AreaChiusure> creaPagamentiConAcconto(List<Pagamento> pagamenti, List<AreaAcconto> acconti)
            throws TipoDocumentoBaseException {
        return pagamentiAccontoGenerator.creaPagamentiConAcconto(pagamenti, acconti);
    }

    @Override
    public String creaTesto(String testo, Sollecito sollecito)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        return templateSollecitiManager.creaTesto(testo, sollecito);
    }

    @Override
    @RolesAllowed("gestionePagamenti")
    public String generaFlusso(final int idDocumento) throws RapportoBancarioPerFlussoAssenteException,
            CodiceSIAAssenteException, TipoDocumentoChiusuraAssenteException {
        Documento documento = new Documento();
        documento.setId(idDocumento);
        AreaChiusure areaChiusure = (AreaChiusure) areaTesoreriaManager.caricaAreaTesoreria(documento);
        String pathFileGenerato = flussiCBIManager.generaFlusso(areaChiusure);
        return pathFileGenerato;
    }

    @Override
    public List<AreaChiusure> ricercaAreeChiusure(ParametriRicercaAreaChiusure parametriRicercaAreaChiusure) {
        return null;
    }

    @Override
    public List<AreaTesoreria> ricercaAreeTesorerie(ParametriRicercaAreeTesoreria parametriRicercaAreeTesoreria) {
        return areaTesoreriaManager.ricercaAreeTesorerie(parametriRicercaAreeTesoreria);
    }

    @Override
    public List<SituazioneEffetto> ricercaEffetti(ParametriRicercaEffetti parametriRicercaEffetti) {
        return areaEffettiManager.ricercaEffetti(parametriRicercaEffetti);
    }

    @Override
    @RolesAllowed("visualizzaRate")
    public List<SituazioneRata> ricercaRate(ParametriRicercaRate parametriRicercaRate) {
        return rateManager.ricercaRate(parametriRicercaRate);
    }

    @Override
    public AreaAcconto salvaAreaAcconto(AreaAcconto areaAcconto) {
        try {
            return areaAccontoManager.salvaAcconto(areaAcconto);
        } catch (TipoDocumentoBaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AreaTesoreria salvaAreaTesoreria(AreaTesoreria areaTesoreria) {
        return areaTesoreriaManager.salvaAreaTesoreria(areaTesoreria);
    }

    @Override
    public TesoreriaSettings salvaSettings(TesoreriaSettings tesoreriaSettings) {
        return tesoreriaSettingsManager.salva(tesoreriaSettings);
    }

    @Override
    public List<Sollecito> salvaSolleciti(List<Sollecito> solleciti) {
        List<Sollecito> sollecitiSalvati = new ArrayList<Sollecito>();

        for (Sollecito sollecito : solleciti) {
            sollecitiSalvati.add(salvaSollecito(sollecito));
        }

        return sollecitiSalvati;
    }

    @Override
    public Sollecito salvaSollecito(Sollecito sollecito) {
        return sollecitiManager.salvaSollecito(sollecito);
    }

    @Override
    public TemplateSolleciti salvaTemplateSolleciti(TemplateSolleciti templateSolleciti) {
        return templateSollecitiManager.salvaTemplateSolleciti(templateSolleciti);
    }

}
