package it.eurotn.panjea.preventivi.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.domain.Azienda;
import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.RapportoBancarioSedeEntita;
import it.eurotn.panjea.anagrafica.domain.SedeAzienda;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.AziendaLite;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.anagrafica.manager.interfaces.AziendeManager;
import it.eurotn.panjea.anagrafica.manager.interfaces.SediAziendaManager;
import it.eurotn.panjea.anagrafica.manager.rapportibancarisedeentita.interfaces.RapportiBancariSedeEntitaManager;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino.ETipologiaCodiceIvaAlternativo;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.ProvenienzaPrezzo;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.provvigioni.interfaces.RigaDocumentoVariazioneProvvigioneStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.provvigioni.interfaces.TipoVariazioneProvvigioneStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.sconti.interfaces.RigaDocumentoVariazioneScontoStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.sconti.interfaces.TipoVariazioneScontoStrategy;
import it.eurotn.panjea.magazzino.util.ParametriCreazioneRigaArticolo;
import it.eurotn.panjea.magazzino.util.RigaDestinazione;
import it.eurotn.panjea.ordini.domain.documento.TipoAreaOrdine;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.preventivi.domain.RigaArticolo;
import it.eurotn.panjea.preventivi.domain.RigaPreventivo;
import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo;
import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo.StatoAreaPreventivo;
import it.eurotn.panjea.preventivi.domain.documento.TipoAreaPreventivo;
import it.eurotn.panjea.preventivi.domain.documento.interfaces.AreaPreventivoVerificaManager;
import it.eurotn.panjea.preventivi.manager.documento.interfaces.AreaPreventivoCancellaManager;
import it.eurotn.panjea.preventivi.manager.documento.interfaces.AreaPreventivoManager;
import it.eurotn.panjea.preventivi.manager.documento.interfaces.RigaPreventivoManager;
import it.eurotn.panjea.preventivi.manager.documento.interfaces.TipiAreaPreventivoManager;
import it.eurotn.panjea.preventivi.manager.evasione.interfaces.PreventiviEvasioneGenerator;
import it.eurotn.panjea.preventivi.manager.evasione.interfaces.PreventiviEvasioneManager;
import it.eurotn.panjea.preventivi.manager.interfaces.PreventiviMovimentazioneManager;
import it.eurotn.panjea.preventivi.manager.interfaces.RigheCollegateManager;
import it.eurotn.panjea.preventivi.service.exception.ClientePotenzialePresenteException;
import it.eurotn.panjea.preventivi.service.interfaces.PreventiviDocumentoService;
import it.eurotn.panjea.preventivi.util.AreaPreventivoFullDTO;
import it.eurotn.panjea.preventivi.util.AreaPreventivoFullDTOStampa;
import it.eurotn.panjea.preventivi.util.AreaPreventivoRicerca;
import it.eurotn.panjea.preventivi.util.RigaEvasione;
import it.eurotn.panjea.preventivi.util.RigaMovimentazione;
import it.eurotn.panjea.preventivi.util.RigaPreventivoDTO;
import it.eurotn.panjea.preventivi.util.parametriricerca.ParametriRicercaAreaPreventivo;
import it.eurotn.panjea.preventivi.util.parametriricerca.ParametriRicercaMovimentazione;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rate.manager.interfaces.AreaRateManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

@Stateless(name = "Panjea.PreventiviDocumentoService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.PreventiviDocumentoService")
public class PreventiviDocumentoServiceBean implements PreventiviDocumentoService {

    private static Logger logger = Logger.getLogger(PreventiviDocumentoServiceBean.class);

    @EJB
    private TipiAreaPreventivoManager tipiAreaPreventivoManager;

    @EJB
    private AreaPreventivoManager areaPreventivoManager;

    @EJB
    private AreaPreventivoCancellaManager areaPreventivoCancellaManager;

    @EJB
    private RigaPreventivoManager rigaPreventivoManager;

    @EJB
    private AreaRateManager areaRateManager;

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    private AreaPreventivoVerificaManager areaPreventivoVerificaManager;

    @EJB
    private RapportiBancariSedeEntitaManager rapportiBancariSedeEntitaManager;

    @EJB
    private PreventiviMovimentazioneManager preventiviMovimentazioneManager;

    @EJB
    private PreventiviEvasioneManager preventiviEvasioneManager;

    @EJB
    private PreventiviEvasioneGenerator preventiviEvasioneGenerator;

    @EJB
    private RigheCollegateManager righeCollegateManager;

    @EJB
    private SediAziendaManager sediAziendaManager;

    @EJB
    private AziendeManager aziendeManager;

    @Override
    public void aggiungiVariazione(Integer idAreaPreventivo, BigDecimal variazione, BigDecimal percProvvigione,
            RigaDocumentoVariazioneScontoStrategy variazioneScontoStrategy,
            TipoVariazioneScontoStrategy tipoVariazioneScontoStrategy,
            RigaDocumentoVariazioneProvvigioneStrategy variazioneProvvigioneStrategy,
            TipoVariazioneProvvigioneStrategy tipoVariazioneProvvigioneStrategy) {
        areaPreventivoManager.aggiungiVariazione(idAreaPreventivo, variazione, percProvvigione,
                variazioneScontoStrategy, tipoVariazioneScontoStrategy, variazioneProvvigioneStrategy,
                tipoVariazioneProvvigioneStrategy);
    }

    @Override
    public AreaPreventivo cambiaStatoSePossibile(AreaPreventivo areaPreventivo, StatoAreaPreventivo statoDaApplicare)
            throws ClientePotenzialePresenteException {
        return areaPreventivoManager.cambiaStatoSePossibile(areaPreventivo, statoDaApplicare);
    }

    @Override
    // @RolesAllowed("gestioneDocPreventivo")
    public void cancellaAreaPreventivo(AreaPreventivo areaPreventivo) {
        areaPreventivoCancellaManager.cancellaAreaPreventivo(areaPreventivo);
    }

    @Override
    public void cancellaAreePreventivo(List<AreaPreventivo> areePreventivo) {
        for (AreaPreventivo areaPreventivo : areePreventivo) {
            cancellaAreaPreventivo(areaPreventivo);
        }
    }

    @Override
    public AreaPreventivo cancellaRigaPreventivo(RigaPreventivo riga) {
        return areaPreventivoCancellaManager.cancellaRigaPreventivo(riga);
    }

    @Override
    public void cancellaTipoAreaPreventivo(TipoAreaPreventivo tipoAreaPreventivo) {
        tipiAreaPreventivoManager.cancellaTipoAreaPreventivo(tipoAreaPreventivo);
    }

    @Override
    public AreaPreventivo caricaAreaPreventivo(AreaPreventivo areaPreventivo) {
        return areaPreventivoManager.caricaAreaPreventivo(areaPreventivo);
    }

    @Override
    public AreaPreventivoFullDTO caricaAreaPreventivoControlloFullDTO(Map<Object, Object> paramenters) {
        return caricaAreaPreventivoFullDTOStampa(paramenters);
    }

    @Override
    public AreaPreventivoFullDTO caricaAreaPreventivoFullDTO(AreaPreventivo areaPreventivo) {
        logger.debug("--> Enter caricaAreaPreventivoFullDTO");

        AreaPreventivoFullDTO areaPreventivoFullDTO = new AreaPreventivoFullDTO();
        // se l'area preventivo è in sessione eseguo una refresh per ricaricare i
        // dati nella sessione (il numero di righe) altrimenti chiamo una carica "completa"
        AreaPreventivo areaPreventivoLoad = null;
        if (panjeaDAO.getEntityManager().contains(areaPreventivo)) {
            panjeaDAO.getEntityManager().refresh(areaPreventivo);
            areaPreventivoLoad = areaPreventivo;
        } else {
            areaPreventivoLoad = areaPreventivoManager.caricaAreaPreventivo(areaPreventivo);
        }

        areaPreventivoFullDTO.setAreaPreventivo(areaPreventivoLoad);

        // caricamento e set di areaPartite
        AreaRate areaRateLoad = areaRateManager.caricaAreaRate(areaPreventivoLoad.getDocumento());
        areaPreventivoFullDTO.setAreaRateEnabled(areaRateLoad.getId() != null);
        areaPreventivoFullDTO.setAreaRate(areaRateLoad);

        if (logger.isDebugEnabled()) {
            logger.debug("--> Exit caricaAreaPreventivoFullDTO " + areaPreventivoFullDTO);
        }
        return areaPreventivoFullDTO;
    }

    @Override
    public AreaPreventivoFullDTOStampa caricaAreaPreventivoFullDTOStampa(Map<Object, Object> paramenters) {
        Integer idAreaOrdine = (Integer) paramenters.get("id");

        AreaPreventivo areaOrdine = new AreaPreventivo();
        areaOrdine.setId(idAreaOrdine);

        AreaPreventivoFullDTO areaOrdineFullDTO = caricaAreaPreventivoFullDTO(areaOrdine);
        List<RigaPreventivo> righeOrdine = caricaRighePreventivo(areaOrdine);

        AziendaLite aziendaLite = aziendeManager.caricaAzienda();
        Azienda azienda = new Azienda();
        azienda.setId(aziendaLite.getId());
        SedeAzienda sedeAzienda = null;
        try {
            sedeAzienda = sediAziendaManager.caricaSedePrincipaleAzienda(azienda);
        } catch (AnagraficaServiceException e) {
            logger.error("--> errore durante il caricamento della sede principale dell'azienda", e);
            throw new RuntimeException("errore durante il caricamento della sede principale dell'azienda", e);
        }

        AreaPreventivoFullDTOStampa fullDTOStampa = new AreaPreventivoFullDTOStampa(
                areaOrdineFullDTO.getAreaPreventivo(), areaOrdineFullDTO.getAreaRate(), righeOrdine, sedeAzienda);

        SedeEntita sedeEntita = areaOrdineFullDTO.getAreaPreventivo().getDocumento().getSedeEntita();
        RapportoBancarioSedeEntita rapportoBancario = caricaRapportoBancarioSedeEntita(sedeEntita);
        fullDTOStampa.setRapportoBancarioSedeEntita(rapportoBancario);

        return fullDTOStampa;
    }

    @Override
    public List<RigaMovimentazione> caricaMovimentazione(ParametriRicercaMovimentazione parametriRicercaMovimentazione,
            int page, int sizeOfPage) {
        return preventiviMovimentazioneManager.caricaMovimentazione(parametriRicercaMovimentazione, page, sizeOfPage);
    }

    /**
     * carica il rapporto bancario dell'entita. Restituisce il primo rapporto bancario che trova per la SedeEntita.
     *
     * @param sedeEntita
     *            sede di cui caricare il rapporto
     * @return rapporto bancario caricato, <code>null</code> se non esiste
     */
    private RapportoBancarioSedeEntita caricaRapportoBancarioSedeEntita(SedeEntita sedeEntita) {
        RapportoBancarioSedeEntita rapporto = null;

        if (sedeEntita != null) {
            try {
                List<RapportoBancarioSedeEntita> rapporti = rapportiBancariSedeEntitaManager
                        .caricaRapportiBancariSedeEntita(sedeEntita.getId(), false);
                if (rapporti != null && !rapporti.isEmpty()) {
                    rapporto = rapporti.get(0);
                }
            } catch (AnagraficaServiceException e) {
                throw new RuntimeException(e);
            }
        }
        return rapporto;
    }

    @Override
    public RigaPreventivo caricaRigaPreventivo(RigaPreventivo rigaPreventivo) {
        return rigaPreventivoManager.getDao(rigaPreventivo).caricaRigaPreventivo(rigaPreventivo);
    }

    @Override
    public List<RigaDestinazione> caricaRigheCollegate(RigaPreventivo rigaPreventivo) {
        return righeCollegateManager.caricaRigheCollegate(rigaPreventivo);
    }

    @Override
    public List<RigaEvasione> caricaRigheEvasione(Integer idAreaPreventivo) {
        return preventiviEvasioneManager.caricaRigheEvasione(idAreaPreventivo);
    }

    @Override
    // @RolesAllowed("visualizzaDocPreventivo")
    public List<RigaPreventivo> caricaRighePreventivo(AreaPreventivo areaPreventivo) {
        return rigaPreventivoManager.getDao().caricaRighePreventivo(areaPreventivo);
    }

    @Override
    public List<RigaPreventivoDTO> caricaRighePreventivoDTO(AreaPreventivo areaPreventivo) {
        return rigaPreventivoManager.getDao().caricaRighePreventivoDTO(areaPreventivo);
    }

    @Override
    public List<TipoAreaPreventivo> caricaTipiAreaPreventivo(String fieldSearch, String valueSearch,
            boolean loadTipiDocumentoDisabilitati) {
        return tipiAreaPreventivoManager.caricaTipiAreaPreventivo(fieldSearch, valueSearch,
                loadTipiDocumentoDisabilitati);
    }

    @Override
    public List<TipoDocumento> caricaTipiDocumentiPreventivo() {
        return tipiAreaPreventivoManager.caricaTipiDocumentiPreventivo();
    }

    @Override
    public TipoAreaPreventivo caricaTipoAreaPreventivo(Integer id) {
        return tipiAreaPreventivoManager.caricaTipoAreaPreventivo(id);
    }

    @Override
    public TipoAreaPreventivo caricaTipoAreaPreventivoPerTipoDocumento(Integer idTipoDocumento) {
        return tipiAreaPreventivoManager.caricaTipoAreaPreventivoPerTipoDocumento(idTipoDocumento);
    }

    @Override
    public void collegaTestata(Set<Integer> righePreventivoDaCambiare) {
        rigaPreventivoManager.collegaTestata(righePreventivoDaCambiare);
    }

    @Override
    public AreaPreventivoFullDTO copiaPreventivo(Integer idAreaPreventivo) {
        AreaPreventivo areaClone = areaPreventivoManager.copiaPreventivo(idAreaPreventivo);
        return caricaAreaPreventivoFullDTO(areaClone);
    }

    @Override
    public RigaArticolo creaRigaArticolo(ParametriCreazioneRigaArticolo parametriCreazioneRigaArticolo) {
        return rigaPreventivoManager.creaRigaArticolo(parametriCreazioneRigaArticolo);
    }

    @Override
    public boolean creaRigaNoteAutomatica(AreaPreventivo areaPreventivo, String note) {
        return rigaPreventivoManager.creaRigaNoteAutomatica(areaPreventivo, note);
    }

    @Override
    public void evadiPreventivi(List<RigaEvasione> rigaEvasione, TipoAreaOrdine tipoAreaOrdine, DepositoLite deposito,
            Date dataOrdine, AgenteLite agente, Date dataConsegnaOrdine) {
        preventiviEvasioneGenerator.evadiPreventivi(rigaEvasione, tipoAreaOrdine, deposito, dataOrdine, agente,
                dataConsegnaOrdine);
    }

    @Override
    public void inserisciRaggruppamentoArticoli(Integer idAreaPreventivo, ProvenienzaPrezzo provenienzaPrezzo,
            Integer idRaggruppamentoArticoli, Date data, Integer idSedeEntita, Integer idListinoAlternativo,
            Integer idListino, Importo importo, CodiceIva codiceIvaAlternativo, Integer idTipoMezzo,
            Integer idZonaGeografica, boolean noteSuDestinazione, String codiceValuta, String codiceLingua,
            Integer idAgente, ETipologiaCodiceIvaAlternativo tipologiaCodiceIvaAlternativo,
            BigDecimal percentualeScontoCommerciale) {

        rigaPreventivoManager.inserisciRaggruppamentoArticoli(idAreaPreventivo, provenienzaPrezzo,
                idRaggruppamentoArticoli, data, idSedeEntita, idListinoAlternativo, idListino, importo,
                codiceIvaAlternativo, idTipoMezzo, idZonaGeografica, noteSuDestinazione, codiceValuta, codiceLingua,
                idAgente, tipologiaCodiceIvaAlternativo, percentualeScontoCommerciale);

    }

    @Override
    public AreaPreventivoFullDTO ricalcolaPrezziPreventivo(Integer idAreaPreventivo) {
        areaPreventivoManager.ricalcolaPrezziPreventivo(idAreaPreventivo);
        AreaPreventivo areaPreventivo = new AreaPreventivo();
        areaPreventivo.setId(idAreaPreventivo);
        return caricaAreaPreventivoFullDTO(areaPreventivo);
    }

    @Override
    // @RolesAllowed("visualizzaDocPreventivo")
    public List<AreaPreventivoRicerca> ricercaAreePreventivo(
            ParametriRicercaAreaPreventivo parametriRicercaAreaPreventivo) {
        return areaPreventivoManager.ricercaAreePreventivo(parametriRicercaAreaPreventivo);
    }

    @Override
    // @RolesAllowed("gestioneDocPreventivo")
    public AreaPreventivoFullDTO salvaAreaPreventivo(AreaPreventivo areaPreventivo, AreaRate areaRate) {
        logger.debug("--> Enter salvaAreaPreventivo");

        boolean cambioSconto = areaRateManager.checkCambioScontoCommerciale(areaRate);
        if (cambioSconto) {
            CodicePagamento codicePagamento = areaRate.getCodicePagamento();
            BigDecimal scontoComm = (codicePagamento != null) ? codicePagamento.getPercentualeScontoCommerciale()
                    : BigDecimal.ZERO;
            rigaPreventivoManager.aggiornaScontoCommerciale(areaPreventivo, scontoComm);
        }

        boolean cambioStato = areaPreventivoVerificaManager.checkCambioStato(areaPreventivo, areaRate);
        // se devo cambiare lo stato vado anche a svalidare le righe magazzino
        if (cambioStato) {
            areaPreventivo.getDatiValidazioneRighe().invalida();
            areaPreventivo.setStatoAreaPreventivo(StatoAreaPreventivo.PROVVISORIO);
        }

        AreaPreventivo areaPreventivoSave = areaPreventivoManager.salvaAreaPreventivo(areaPreventivo);

        if (areaRate != null) {
            // aggiorna AreaPartite con il documento salvato
            areaRate.setDocumento(areaPreventivoSave.getDocumento());
            areaRateManager.salvaAreaRate(areaRate);
        }

        AreaPreventivoFullDTO areaPreventivoFullDTO = caricaAreaPreventivoFullDTO(areaPreventivoSave);
        logger.debug("--> Exit salvaAreaPreventivo");
        return areaPreventivoFullDTO;
    }

    @Override
    public RigaPreventivo salvaRigaPreventivo(RigaPreventivo riga, boolean checkRiga) {
        if (checkRiga) {
            return rigaPreventivoManager.getDao(riga).salvaRigaPreventivo(riga);
        }
        return rigaPreventivoManager.getDao(riga).salvaRigaPreventivoNoCheck(riga);
    }

    @Override
    public TipoAreaPreventivo salvaTipoAreaPreventivo(TipoAreaPreventivo tipoAreaPreventivo) {
        return tipiAreaPreventivoManager.salvaTipoAreaPreventivo(tipoAreaPreventivo);
    }

    @Override
    public void spostaRighe(Set<Integer> righeDaSpostare, Integer idDest) {
        areaPreventivoManager.spostaRighe(righeDaSpostare, idDest);
    }

    @Override
    public AreaPreventivo totalizzaDocumento(AreaPreventivo areaPreventivo, AreaRate areaRate) {
        return areaPreventivoManager.totalizzaDocumento(areaPreventivo, areaRate);
    }

    @Override
    public AreaPreventivoFullDTO validaRighePreventivo(AreaPreventivo areaPreventivo, AreaRate areaRate,
            boolean cambioStato) {
        logger.debug("--> Enter validaRighePreventivo");

        AreaPreventivo areaPreventivoConfermata = null;
        try {
            areaPreventivoConfermata = areaPreventivoManager.validaRighePreventivo(areaPreventivo, areaRate,
                    cambioStato);
        } catch (Exception e) {
            logger.error("--> errore nel validare le righe preventivo ", e);
            throw new RuntimeException("errore nel validare le righe preventivo", e);
        }

        AreaPreventivoFullDTO areaPreventivoFullDTO = caricaAreaPreventivoFullDTO(areaPreventivoConfermata);
        logger.debug("--> Exit validaRighePreventivo");
        return areaPreventivoFullDTO;
    }
}
