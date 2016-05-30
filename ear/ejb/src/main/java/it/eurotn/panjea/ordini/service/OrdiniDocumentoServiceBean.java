package it.eurotn.panjea.ordini.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Query;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.ejb.TransactionTimeout;
import org.jboss.annotation.security.SecurityDomain;

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
import it.eurotn.panjea.contabilita.service.exception.ContiBaseException;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.lotti.exception.EvasioneLottiException;
import it.eurotn.panjea.lotti.exception.LottiException;
import it.eurotn.panjea.lotti.exception.RimanenzaLottiNonValidaException;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.ConfigurazioneDistinta;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino.ETipologiaCodiceIvaAlternativo;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.ProvenienzaPrezzo;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoManager;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.RigaMagazzinoManager;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.StatiAreaMagazzinoManager;
import it.eurotn.panjea.magazzino.manager.interfaces.MagazzinoContabilizzazioneManager;
import it.eurotn.panjea.magazzino.manager.interfaces.articolo.DistintaBaseManager;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.provvigioni.interfaces.RigaDocumentoVariazioneProvvigioneStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.provvigioni.interfaces.TipoVariazioneProvvigioneStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.sconti.interfaces.RigaDocumentoVariazioneScontoStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.sconti.interfaces.TipoVariazioneScontoStrategy;
import it.eurotn.panjea.magazzino.service.exception.ContabilizzazioneException;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;
import it.eurotn.panjea.magazzino.util.ParametriCreazioneRigaArticolo;
import it.eurotn.panjea.magazzino.util.ParametriRicercaAreaMagazzino;
import it.eurotn.panjea.magazzino.util.RigaDestinazione;
import it.eurotn.panjea.ordini.domain.OrdineImportato;
import it.eurotn.panjea.ordini.domain.RigaArticolo;
import it.eurotn.panjea.ordini.domain.RigaOrdine;
import it.eurotn.panjea.ordini.domain.RigaOrdineImportata;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine.StatoAreaOrdine;
import it.eurotn.panjea.ordini.domain.documento.TipoAreaOrdine;
import it.eurotn.panjea.ordini.domain.documento.evasione.DistintaCarico;
import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCarico;
import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCaricoProduzione;
import it.eurotn.panjea.ordini.exception.CodicePagamentoAssenteException;
import it.eurotn.panjea.ordini.exception.CodicePagamentoEvasioneAssenteException;
import it.eurotn.panjea.ordini.exception.EntitaSenzaTipoDocumentoEvasioneException;
import it.eurotn.panjea.ordini.exception.TipoAreaPartitaDestinazioneRichiestaException;
import it.eurotn.panjea.ordini.manager.documento.evasione.DatiDistintaCaricoVerifica;
import it.eurotn.panjea.ordini.manager.documento.evasione.DistintaCaricoManagerBean;
import it.eurotn.panjea.ordini.manager.documento.evasione.interfaces.DistintaCaricoManager;
import it.eurotn.panjea.ordini.manager.documento.evasione.interfaces.DistintaCaricoVerificaManager;
import it.eurotn.panjea.ordini.manager.documento.interfaces.AreaOrdineCancellaManager;
import it.eurotn.panjea.ordini.manager.documento.interfaces.AreaOrdineManager;
import it.eurotn.panjea.ordini.manager.documento.interfaces.AreaOrdineProduzioneStampaManager;
import it.eurotn.panjea.ordini.manager.documento.interfaces.AreaOrdineVerificaManager;
import it.eurotn.panjea.ordini.manager.documento.interfaces.RigaOrdineManager;
import it.eurotn.panjea.ordini.manager.documento.interfaces.RigheCollegateManager;
import it.eurotn.panjea.ordini.manager.documento.interfaces.TipiAreaOrdineManager;
import it.eurotn.panjea.ordini.manager.documento.produzione.interfaces.OrdiniProduzioneManager;
import it.eurotn.panjea.ordini.manager.documento.righeinserimento.interfaces.RigheInserimentoManager;
import it.eurotn.panjea.ordini.manager.interfaces.ImportazioneOrdiniManager;
import it.eurotn.panjea.ordini.manager.interfaces.OrdineMovimentazioneManager;
import it.eurotn.panjea.ordini.service.interfaces.OrdiniDocumentoService;
import it.eurotn.panjea.ordini.util.AreaOrdineFullDTO;
import it.eurotn.panjea.ordini.util.AreaOrdineFullDTOStampa;
import it.eurotn.panjea.ordini.util.AreaOrdineRicerca;
import it.eurotn.panjea.ordini.util.AreaProduzioneFullDTOStampa;
import it.eurotn.panjea.ordini.util.ParametriRicercaProduzione;
import it.eurotn.panjea.ordini.util.RigaDistintaCaricoStampa;
import it.eurotn.panjea.ordini.util.RigaMovimentazione;
import it.eurotn.panjea.ordini.util.RigaOrdineDTO;
import it.eurotn.panjea.ordini.util.parametriricerca.ParametriRicercaAreaOrdine;
import it.eurotn.panjea.ordini.util.parametriricerca.ParametriRicercaEvasione;
import it.eurotn.panjea.ordini.util.parametriricerca.ParametriRicercaMovimentazione;
import it.eurotn.panjea.ordini.util.parametriricerca.ParametriRicercaOrdiniImportati;
import it.eurotn.panjea.ordini.util.rigaordine.builder.domain.RigaOrdineBuilder;
import it.eurotn.panjea.ordini.util.rigaordine.builder.domain.RigaOrdineFactoryBuilder;
import it.eurotn.panjea.ordini.util.righeinserimento.ParametriRigheOrdineInserimento;
import it.eurotn.panjea.ordini.util.righeinserimento.RigaOrdineInserimento;
import it.eurotn.panjea.ordini.util.righeinserimento.RigheOrdineInserimento;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.partite.domain.AreaPartite;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rate.manager.interfaces.AreaRateManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

/**
 * @author fattazzo
 */
@Stateless(name = "Panjea.OrdiniDocumentoService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.OrdiniDocumentoService")
public class OrdiniDocumentoServiceBean implements OrdiniDocumentoService {

    private static Logger logger = Logger.getLogger(OrdiniDocumentoServiceBean.class);

    @EJB
    private AreaOrdineProduzioneStampaManager areaOrdineProduzioneStampaManager;

    @EJB
    private DistintaCaricoVerificaManager distintaCaricoVerificaManager;

    @EJB
    private DistintaCaricoManager distintaCaricoManager;

    @EJB
    private TipiAreaOrdineManager tipiAreaOrdineManager;

    @EJB
    private AreaOrdineManager areaOrdineManager;

    @EJB
    private AreaRateManager areaRateManager;

    @EJB
    private MagazzinoContabilizzazioneManager magazzinoContabilizzazioneManager;

    @EJB
    private RigaOrdineManager rigaOrdineManager;

    @EJB
    private AreaOrdineCancellaManager areaOrdineCancellaManager;

    @EJB
    private OrdineMovimentazioneManager ordineMovimentazioneManager;

    @EJB
    private ImportazioneOrdiniManager importazioneOrdiniManager;

    @EJB
    private RigheCollegateManager righeCollegateManager;

    @EJB
    private AreaOrdineVerificaManager areaOrdineVerificaManager;

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    private RapportiBancariSedeEntitaManager rapportiBancariSedeEntitaManager;

    @EJB
    private OrdiniProduzioneManager ordiniProduzioneManager;

    @EJB
    private DistintaBaseManager distintaBaseManager;

    @EJB
    private AreaMagazzinoManager areaMagazzinoManager;

    @EJB
    private RigaMagazzinoManager rigaMagazzinoManager;

    @EJB
    private StatiAreaMagazzinoManager statiAreaMagazzinoManager;

    @EJB
    private SediAziendaManager sediAziendaManager;

    @EJB
    private AziendeManager aziendeManager;

    @EJB
    private RigheInserimentoManager righeInserimentoManager;

    @Override
    public void aggiornaDataConsegna(AreaOrdine areaOrdine, Date dataRiferimento) {
        rigaOrdineManager.aggiornaDataConsegna(areaOrdine, dataRiferimento);
    }

    @Override
    public List<RigaDistintaCarico> aggiornaRigheCaricoConDatiEvasione(
            List<RigaDistintaCarico> righeDistintaDaAggiornare) {
        return distintaCaricoManager.aggiornaRigheCaricoConDatiEvasione(righeDistintaDaAggiornare);
    }

    @Override
    public void aggiungiVariazione(Integer idAreaOrdine, BigDecimal variazione, BigDecimal percProvvigione,
            RigaDocumentoVariazioneScontoStrategy variazioneScontoStrategy,
            TipoVariazioneScontoStrategy tipoVariazioneScontoStrategy,
            RigaDocumentoVariazioneProvvigioneStrategy variazioneProvvigioneStrategy,
            TipoVariazioneProvvigioneStrategy tipoVariazioneProvvigioneStrategy) {
        logger.debug("--> Enter aggiungiVariazione");
        areaOrdineManager.aggiungiVariazione(idAreaOrdine, variazione, percProvvigione, variazioneScontoStrategy,
                tipoVariazioneScontoStrategy, variazioneProvvigioneStrategy, tipoVariazioneProvvigioneStrategy);
        logger.debug("--> Exit aggiungiVariazione");
    }

    @Override
    public RigaArticolo associaConfigurazioneDistintaARigaOrdine(RigaArticolo rigaArticolo,
            ConfigurazioneDistinta configurazioneDistintaDaAssociare) {
        return rigaOrdineManager.associaConfigurazioneDistintaARigaOrdine(rigaArticolo,
                configurazioneDistintaDaAssociare);
    }

    @Override
    @RolesAllowed("gestioneBloccoOrdine")
    public AreaOrdine bloccaOrdine(int idAreaOrdine, boolean blocca) {
        AreaOrdine areaOrdine = new AreaOrdine();
        areaOrdine.setId(idAreaOrdine);
        return areaOrdineManager.bloccaOrdine(areaOrdine, blocca);
    }

    @Override
    @RolesAllowed("gestioneBloccoOrdine")
    public List<AreaOrdineRicerca> bloccaOrdini(Collection<Integer> idOrdini, boolean blocca) {

        List<AreaOrdineRicerca> aree = new ArrayList<AreaOrdineRicerca>();

        for (Integer idAreaOrdine : idOrdini) {
            AreaOrdine areaOrdine = bloccaOrdine(idAreaOrdine, blocca);
            aree.add(new AreaOrdineRicerca(areaOrdine));
        }

        return aree;

    }

    @Override
    public Map<ArticoloLite, Double> calcolaGiacenze(DepositoLite depositoLite, Date data) {
        return distintaCaricoManager.calcolaGiacenze(depositoLite, data);
    }

    @Override
    @RolesAllowed("gestioneDocOrdine")
    public void cancellaAreaOrdine(AreaOrdine areaOrdine) {
        areaOrdineCancellaManager.cancellaAreaOrdine(areaOrdine);
    }

    @Override
    @RolesAllowed("gestioneDocOrdine")
    public void cancellaAreeOrdine(List<AreaOrdine> areeOrdine) {
        for (AreaOrdine areaOrdine : areeOrdine) {
            cancellaAreaOrdine(areaOrdine);
        }
    }

    @Override
    @RolesAllowed("gestioneBackOrder")
    public void cancellaOrdiniImportati(Collection<String> numeroOrdini) {
        importazioneOrdiniManager.cancellaOrdiniImportati(numeroOrdini);
    }

    @Override
    @RolesAllowed("gestioneBackOrder")
    public void cancellaOrdiniImportatiPerAgente(String codiceAgente) {
        importazioneOrdiniManager.cancellaOrdiniImportatiPerAgente(codiceAgente);
    }

    @Override
    @RolesAllowed("gestioneDocOrdine")
    public AreaOrdine cancellaRigaOrdine(RigaOrdine rigaOrdine) {
        return areaOrdineCancellaManager.cancellaRigaOrdine(rigaOrdine);
    }

    @Override
    public void cancellaRigheCollegate(int rigaOrdineOrigine, int rigaOrdineDestinazione) {
        righeCollegateManager.cancellaRigheCollegate(rigaOrdineOrigine, rigaOrdineDestinazione);
    }

    @Override
    @RolesAllowed("missioni")
    public void cancellaRigheDistintaCarico(Set<RigaDistintaCarico> righeDistintaCarico) {
        distintaCaricoManager.cancellaRigheDistintaCarico(righeDistintaCarico);
    }

    @Override
    public void cancellaRigheDistintaCaricoLotti(RigaDistintaCarico rigaDistintaCarico) {
        distintaCaricoManager.cancellaRigheDistintaCaricoLotti(rigaDistintaCarico);
    }

    @Override
    public void cancellaTipoAreaOrdine(TipoAreaOrdine tipoAreaOrdine) {
        tipiAreaOrdineManager.cancellaTipoAreaOrdine(tipoAreaOrdine);
    }

    @Override
    @RolesAllowed("visualizzaDocOrdine")
    public AreaOrdine caricaAreaOrdine(AreaOrdine areaOrdine) {
        return areaOrdineManager.caricaAreaOrdine(areaOrdine);
    }

    @Override
    @RolesAllowed("visualizzaDocOrdine")
    public AreaOrdineFullDTO caricaAreaOrdineControlloFullDTO(Map<Object, Object> paramenters) {
        AreaOrdineFullDTO areaOrdineFullDTO = caricaAreaOrdineFullDTOStampa(paramenters);
        List<RigaOrdine> righeOrdine = new ArrayList<RigaOrdine>();
        for (RigaOrdine rigaOrdine : areaOrdineFullDTO.getRigheOrdine()) {
            if (rigaOrdine instanceof RigaArticolo && !((RigaArticolo) rigaOrdine).isDatiImportazioneCoerenti()) {
                righeOrdine.add(rigaOrdine);
            }
        }
        areaOrdineFullDTO.setRigheOrdine(righeOrdine);
        if (areaOrdineFullDTO.getAreaOrdine().isDatiImportazioneCoerenti()
                && !areaOrdineFullDTO.getRigheOrdine().isEmpty()) {
            return null;
        }
        return areaOrdineFullDTO;
    }

    @Override
    @RolesAllowed("visualizzaDocOrdine")
    public AreaOrdineFullDTO caricaAreaOrdineFullDTO(AreaOrdine areaOrdine) {
        logger.debug("--> Enter caricaAreaOrdineFullDTO");

        AreaOrdineFullDTO areaOrdineFullDTO = new AreaOrdineFullDTO();
        // se l'area ordine è in sessione eseguo una refresh per ricaricare i
        // dati nella sessione (il numero di righe) altrimenti chiamo una carica
        // "completa"
        AreaOrdine areaOrdineLoad = null;
        if (panjeaDAO.getEntityManager().contains(areaOrdine)) {
            panjeaDAO.getEntityManager().refresh(areaOrdine);
            areaOrdineLoad = areaOrdine;
        } else {
            areaOrdineLoad = areaOrdineManager.caricaAreaOrdine(areaOrdine);
        }
        // set di AreaOrdine
        areaOrdineFullDTO.setAreaOrdine(areaOrdineLoad);

        // caricamento e set di areaPartite
        AreaRate areaRateLoad = areaRateManager.caricaAreaRate(areaOrdineLoad.getDocumento());
        // inizializzazione di enableAreaPartite
        areaOrdineFullDTO.setAreaRateEnabled(areaRateLoad.getId() != null);
        areaOrdineFullDTO.setAreaRate(areaRateLoad);

        if (logger.isDebugEnabled()) {
            logger.debug("--> Exit caricaAreaMagazzinoFullDTO " + areaOrdineFullDTO);
        }
        return areaOrdineFullDTO;
    }

    @Override
    @RolesAllowed("visualizzaDocOrdine")
    public AreaOrdineFullDTOStampa caricaAreaOrdineFullDTOStampa(Map<Object, Object> paramenters) {

        Integer idAreaOrdine = (Integer) paramenters.get("id");

        AreaOrdine areaOrdine = new AreaOrdine();
        areaOrdine.setId(idAreaOrdine);

        AreaOrdineFullDTO areaOrdineFullDTO = caricaAreaOrdineFullDTO(areaOrdine);

        List<RigaOrdine> righeOrdine = caricaRigheOrdineStampa(areaOrdineFullDTO.getAreaOrdine());
        righeOrdine = convertRigheOrdineToDomain(righeOrdine);

        AziendaLite aziendaLite = aziendeManager.caricaAzienda();
        Azienda azienda = new Azienda();
        azienda.setId(aziendaLite.getId());
        SedeAzienda sedeAzienda;
        try {
            sedeAzienda = sediAziendaManager.caricaSedePrincipaleAzienda(azienda);
        } catch (AnagraficaServiceException e) {
            logger.error("--> errore durante il caricamento della sede principale dell'azienda", e);
            throw new GenericException("errore durante il caricamento della sede principale dell'azienda", e);
        }

        AreaOrdineFullDTOStampa fullDTOStampa = new AreaOrdineFullDTOStampa(areaOrdineFullDTO.getAreaOrdine(),
                areaOrdineFullDTO.getAreaRate(), righeOrdine, sedeAzienda);

        SedeEntita sedeEntita = areaOrdineFullDTO.getAreaOrdine().getDocumento().getSedeEntita();
        RapportoBancarioSedeEntita rapportoBancario = caricaRapportoBancarioSedeEntitaAreaOrdine(sedeEntita);
        fullDTOStampa.setRapportoBancarioSedeEntita(rapportoBancario);

        return fullDTOStampa;
    }

    @Override
    @RolesAllowed("visualizzaDocOrdine")
    public AreaProduzioneFullDTOStampa caricaAreaOrdineProduzioneDTOStampa(Map<Object, Object> paramenters) {
        Integer idAreaOrdine = (Integer) paramenters.get("id");

        AreaOrdine areaOrdine = new AreaOrdine();
        areaOrdine.setId(idAreaOrdine);

        AreaOrdineFullDTO areaOrdineFullDTO = caricaAreaOrdineFullDTO(areaOrdine);
        List<RigaOrdine> righeOrdine = caricaRigheOrdineStampa(areaOrdineFullDTO.getAreaOrdine());
        for (RigaOrdine rigaOrdine : righeOrdine) {
            if (rigaOrdine instanceof RigaArticolo) {
                rigaOrdine = rigaOrdineManager.getDao().caricaQtaAttrezzaggio((RigaArticolo) rigaOrdine,
                        ((RigaArticolo) rigaOrdine).getConfigurazioneDistinta());
            }
        }
        righeOrdine = convertRigheOrdineToDomain(righeOrdine);
        return areaOrdineProduzioneStampaManager.caricaAreaOrdineProduzioneDTOStampa(areaOrdineFullDTO, righeOrdine);
    }

    @Override
    public DatiDistintaCaricoVerifica caricaDatiVerifica(Date dataInizioTrasporto) {
        return distintaCaricoVerificaManager.caricaDatiVerifica(dataInizioTrasporto);
    }

    @TransactionAttribute(TransactionAttributeType.NEVER)
    @Override
    @RolesAllowed("visualizzaDocOrdine")
    public List<RigaMovimentazione> caricaMovimentazione(ParametriRicercaMovimentazione parametriRicercaMovimentazione,
            int page, int sizeOfPage) {
        return ordineMovimentazioneManager.caricaMovimentazione(parametriRicercaMovimentazione, page, sizeOfPage);
    }

    /**
     * carica il rapporto bancario dell'entita. Restituisce il primo rapporto bancario che trova per la SedeEntita.
     *
     * @param sedeEntita
     *            sede di cui caricare il rapporto
     * @return rapporto bancario caricato, <code>null</code> se non esiste
     */
    private RapportoBancarioSedeEntita caricaRapportoBancarioSedeEntitaAreaOrdine(SedeEntita sedeEntita) {
        RapportoBancarioSedeEntita rapporto = null;

        if (sedeEntita != null) {
            try {
                List<RapportoBancarioSedeEntita> rapporti = rapportiBancariSedeEntitaManager
                        .caricaRapportiBancariSedeEntita(sedeEntita.getId(), false);
                if (!CollectionUtils.isEmpty(rapporti)) {
                    rapporto = rapporti.get(0);
                }
            } catch (AnagraficaServiceException e) {
                throw new RuntimeException(e);
            }
        }
        return rapporto;
    }

    @Override
    @RolesAllowed("visualizzaDocOrdine")
    public RigaOrdine caricaRigaOrdine(RigaOrdine rigaOrdine) {
        return rigaOrdineManager.getDao(rigaOrdine).caricaRigaOrdine(rigaOrdine);
    }

    @Override
    @RolesAllowed("visualizzaDocOrdine")
    public List<RigaDestinazione> caricaRigheCollegate(RigaOrdine rigaOrdine) {
        return righeCollegateManager.caricaRigheCollegate(rigaOrdine);
    }

    @Override
    public List<RigaDistintaCarico> caricaRigheDistintaCarico(List<DistintaCarico> distinte) {
        return distintaCaricoManager.caricaRigheDistintaCarico(distinte);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<RigaDistintaCaricoStampa> caricaRigheDistintaCarico(Map<Object, Object> parametri) {

        List<Integer> idDistinte = new ArrayList<Integer>();
        if (parametri.containsKey("idDistinte")) {
            List<String> tmp = (List<String>) parametri.get("idDistinte");
            for (String string : tmp) {
                idDistinte.add(new Integer(string));
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("select rd.id as idRigaDistintaCarico, ");
        sb.append("			rd.rigaArticolo as rigaArticolo, ");
        sb.append("			art as articolo, ");
        sb.append("			rd.qtaDaEvadere as qtaDaEvadere, ");
        sb.append("			ra.qta as qtaOrdinata, ");
        sb.append("            ra.numeroDecimaliQta as numeroDecimaliQta, ");
        sb.append("			doc.entita as entita, ");
        sb.append("			doc.sedeEntita.ordinamento as ordinamentoEntita, ");
        sb.append("			doc.sedeEntita as sedeEntita, ");
        sb.append("            vett as vettore ");
        sb.append(
                "from RigaDistintaCarico rd, Entita ent , Articolo art inner join rd.rigaArticolo ra inner join ra.areaOrdine ao inner join ao.documento doc left join ao.vettore vett left join doc.sedeEntita.zonaGeografica zona ");
        sb.append("where  rd.articolo.id = art.id and doc.entita.id = ent.id and ");
        sb.append("			 rd.distintaCarico.id in (:paramDistinte) ");
        sb.append(
                "order by vett.codice,zona.codice,doc.sedeEntita.ordinamento,doc.sedeEntita.id,art.posizione,art.codice ");

        Query query = panjeaDAO.prepareQuery(sb.toString(), RigaDistintaCaricoStampa.class, null);
        query.setParameter("paramDistinte", idDistinte);

        List<RigaDistintaCaricoStampa> righeStampa = new ArrayList<RigaDistintaCaricoStampa>();
        try {
            righeStampa = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            logger.error("--> errore durante il caricamento delle righe distinta per la stampa", e);
            throw new RuntimeException("errore durante il caricamento delle righe distinta per la stampa", e);
        }

        // Inizializzo gli attributi che sono lazy e sulla stampa della distinta
        // mi serve stamparli
        for (RigaDistintaCaricoStampa rigaDistintaCaricoStampa : righeStampa) {
            panjeaDAO.getEntityManager().merge(rigaDistintaCaricoStampa.getArticolo());
            rigaDistintaCaricoStampa.getArticolo().getAttributiArticolo().size();
        }

        return righeStampa;
    }

    @Override
    public List<it.eurotn.panjea.magazzino.domain.RigaArticoloDistinta> caricaRigheDistintaMagazzino(
            ParametriRicercaAreaMagazzino parametri) {

        List<it.eurotn.panjea.magazzino.domain.RigaArticoloDistinta> righeCaricate = new ArrayList<it.eurotn.panjea.magazzino.domain.RigaArticoloDistinta>();
        List<AreaMagazzinoRicerca> areeMagazzino = areaMagazzinoManager.ricercaAreeMagazzino(parametri);

        for (AreaMagazzinoRicerca area : areeMagazzino) {
            AreaMagazzino areaMagazzino = new AreaMagazzino();
            areaMagazzino.setId(area.getIdAreaMagazzino());

            List<? extends RigaMagazzino> righeArea = rigaMagazzinoManager.getDao().caricaRigheMagazzino(areaMagazzino);
            for (RigaMagazzino rigaMagazzino : righeArea) {
                if (rigaMagazzino instanceof it.eurotn.panjea.magazzino.domain.RigaArticoloDistinta) {
                    // carico la riga per avere tutti i suoi componenti
                    it.eurotn.panjea.magazzino.domain.RigaArticoloDistinta distinta = (it.eurotn.panjea.magazzino.domain.RigaArticoloDistinta) rigaMagazzinoManager
                            .getDao(rigaMagazzino).caricaRigaMagazzino(rigaMagazzino);
                    righeCaricate.add(distinta);
                }
            }
        }

        return righeCaricate;
    }

    @Override
    @RolesAllowed("visualizzaDocOrdine")
    public List<RigaDistintaCarico> caricaRigheEvasione(ParametriRicercaEvasione parametriRicercaEvasione) {
        return distintaCaricoManager.caricaRighePerEvasione(parametriRicercaEvasione);
    }

    @Override
    @RolesAllowed("visualizzaDocOrdine")
    public List<RigaDistintaCaricoProduzione> caricaRigheEvasioneProduzione(
            ParametriRicercaProduzione parametriRicercaProduzione) {
        return ordiniProduzioneManager.caricaRigheEvasioneProduzione(parametriRicercaProduzione);
    }

    @Override
    @RolesAllowed("missioni")
    public List<RigaDistintaCarico> caricaRigheInMagazzino() {
        return distintaCaricoManager.caricaRigheInMagazzino();
    }

    @Override
    @RolesAllowed("visualizzaDocOrdine")
    public List<RigaOrdine> caricaRigheOrdine(AreaOrdine areaOrdine) {
        return rigaOrdineManager.getDao().caricaRigheOrdine(areaOrdine);
    }

    @Override
    @RolesAllowed("visualizzaDocOrdine")
    public List<RigaOrdineDTO> caricaRigheOrdineDTO(AreaOrdine areaOrdine) {
        return rigaOrdineManager.getDao().caricaRigheOrdineDTO(areaOrdine);
    }

    @Override
    @RolesAllowed("gestioneBackOrder")
    public List<RigaOrdineImportata> caricaRigheOrdineImportate(ParametriRicercaOrdiniImportati parametri) {
        importazioneOrdiniManager.aggiornaRigheOrdineImportate(parametri);

        // non posso aggiornare i prezzi determinati
        // (aggiornaPrezziDeterminatiOrdiniImportati(parametri))
        // quando ricarico perchè mi sovrascriverebbe delle operazioni
        // specifiche eseguite solo per dolcelit
        // vedi AtonImporterServiceBean,
        // dolcelitAtonImporterService.importa(ordineImportato);
        return importazioneOrdiniManager.caricaRigheOrdineImportate(parametri);
    }

    @Override
    public RigheOrdineInserimento caricaRigheOrdineInserimento(ParametriRigheOrdineInserimento parametri) {
        return righeInserimentoManager.caricaRigheOrdineInserimento(parametri);
    }

    /**
     * Carica le righe ordine per la stampa.
     *
     * @param areaOrdine
     *            area ordine
     * @return righe caricate
     */
    private List<RigaOrdine> caricaRigheOrdineStampa(AreaOrdine areaOrdine) {
        areaOrdineManager.calcolaGiacenzaRigheOrdine(areaOrdine.getId());
        return rigaOrdineManager.getDao().caricaRigheOrdineStampa(areaOrdine);
    }

    @Override
    public List<TipoAreaOrdine> caricaTipiAreaOrdine(String fieldSearch, String valueSearch,
            boolean loadTipiDocumentoDisabilitati) {
        return tipiAreaOrdineManager.caricaTipiAreaOrdine(fieldSearch, valueSearch, loadTipiDocumentoDisabilitati);
    }

    @Override
    public List<TipoDocumento> caricaTipiDocumentiOrdine() {
        return tipiAreaOrdineManager.caricaTipiDocumentiOrdine();
    }

    @Override
    public TipoAreaOrdine caricaTipoAreaOrdine(Integer id) {
        return tipiAreaOrdineManager.caricaTipoAreaOrdine(id);
    }

    @Override
    public TipoAreaOrdine caricaTipoAreaOrdinePerTipoDocumento(Integer idTipoDocumento) {
        return tipiAreaOrdineManager.caricaTipoAreaOrdinePerTipoDocumento(idTipoDocumento);
    }

    @Override
    public void collegaTestata(Set<Integer> righeOrdineDaCambiare) {
        rigaOrdineManager.collegaTestata(righeOrdineDaCambiare);
    }

    @Override
    @RolesAllowed("gestioneBackOrder")
    @TransactionTimeout(3600)
    public Long confermaOrdiniImportati(Collection<OrdineImportato> ordiniDaConfermare) {
        Long result = importazioneOrdiniManager.confermaOrdiniImportati(ordiniDaConfermare);
        return result;
    }

    /**
     * Si preoccupa di convertire ed eventualmente raggruppare eventuali righe dove l'articolo padre e il prezzo sono
     * gli stessi.<br>
     * Nota che per il raggruppamento vengono unite le righe contigue con padre e prezzo uguali.
     *
     * @param righeOrdine
     *            la lista di RigaOrdineBuilder da scorrere e raggruppare
     * @return List<RigaOrdine>
     */
    private List<RigaOrdine> convertRigheOrdineToDomain(List<RigaOrdine> righeOrdine) {

        Map<String, RigaOrdine> righeComposte = new HashMap<String, RigaOrdine>();
        List<RigaOrdine> result = new ArrayList<RigaOrdine>();

        RigaOrdineFactoryBuilder factoryBuilder = new RigaOrdineFactoryBuilder();

        for (RigaOrdine rigaOrdineResult : righeOrdine) {
            RigaOrdineBuilder dtoBuilder = factoryBuilder.getBuilder(rigaOrdineResult);
            dtoBuilder.fillResult(rigaOrdineResult, result, righeComposte);
        }

        return result;
    }

    @Override
    public AreaOrdineFullDTO copiaOrdine(Integer idAreaOrdine) {

        Integer idAreaOrdineClone = areaOrdineManager.copiaOrdine(idAreaOrdine);

        AreaOrdine areaOrdine = new AreaOrdine();
        areaOrdine.setId(idAreaOrdineClone);
        AreaOrdineFullDTO areaOrdineFullDTO = caricaAreaOrdineFullDTO(areaOrdine);

        return areaOrdineFullDTO;
    }

    @Override
    @RolesAllowed("missioni")
    public List<DistintaCarico> creaDistintadiCarico(List<RigaDistintaCarico> righeEvasione) {
        return distintaCaricoManager.creaDistintadiCarico(righeEvasione);
    }

    @Override
    @RolesAllowed("gestioneDocOrdine")
    public RigaArticolo creaRigaArticolo(ParametriCreazioneRigaArticolo parametriCreazioneRigaArticolo) {
        return (RigaArticolo) rigaOrdineManager.getDao(parametriCreazioneRigaArticolo)
                .creaRigaArticolo(parametriCreazioneRigaArticolo);
    }

    @Override
    public boolean creaRigaNoteAutomatica(AreaOrdine areaOrdine, String note) {
        return rigaOrdineManager.creaRigaNoteAutomatica(areaOrdine, note);
    }

    @Override
    public void dividiRiga(Integer rigaOriginale, List<RigaArticolo> righeDivise) {
        rigaOrdineManager.dividiRiga(rigaOriginale, righeDivise);
    }

    @Override
    public void evadiOrdini(List<RigaDistintaCarico> righeEvasione, AreaMagazzino documentoEvasione)
            throws EvasioneLottiException {
        distintaCaricoManager.evadiOrdini(righeEvasione, documentoEvasione);
    }

    @TransactionAttribute(TransactionAttributeType.NEVER)
    @Override
    @RolesAllowed("evasione")
    public List<AreaMagazzino> evadiOrdini(List<RigaDistintaCarico> righeEvasione, Date dataEvasione)
            throws EntitaSenzaTipoDocumentoEvasioneException, ContabilizzazioneException, ContiBaseException,
            TipoAreaPartitaDestinazioneRichiestaException, CodicePagamentoEvasioneAssenteException,
            CodicePagamentoAssenteException, LottiException {
        List<AreaMagazzino> ordiniEvasi = distintaCaricoManager.evadiOrdini(righeEvasione, dataEvasione);
        String uuid = "";
        if (!ordiniEvasi.isEmpty()) {
            uuid = ordiniEvasi.get(0).getUUIDContabilizzazione();
        }
        if (!uuid.isEmpty()) {
            magazzinoContabilizzazioneManager.contabilizzaAreeMagazzino(uuid,
                    DistintaCaricoManagerBean.EVASIONE_MESSAGE_SELECTOR, false);
        }
        return ordiniEvasi;
    }

    @Override
    public void forzaRigheOrdine(List<Integer> righe) {
        rigaOrdineManager.forzaRigheOrdine(righe);
    }

    @Override
    public void generaRigheOrdine(List<RigaOrdineInserimento> righeInserimento, AreaOrdine areaOrdine) {
        righeInserimentoManager.generaRigheOrdine(righeInserimento, areaOrdine);
    }

    @Override
    @RolesAllowed("gestioneDocOrdine")
    public void inserisciRaggruppamentoArticoli(Integer idAreaOrdine, ProvenienzaPrezzo provenienzaPrezzo,
            Integer idRaggruppamentoArticoli, Date data, Integer idSedeEntita, Integer idListinoAlternativo,
            Integer idListino, Importo importo, CodiceIva codiceIvaAlternativo, Integer idTipoMezzo,
            Integer idZonaGeografica, boolean noteSuDestinazione, String codiceValuta, String codiceLingua,
            Date dataConsegna, Integer idAgente, ETipologiaCodiceIvaAlternativo tipologiaCodiceIvaAlternativo,
            BigDecimal percentualeScontoCommerciale) throws RimanenzaLottiNonValidaException {
        logger.debug("--> Enter inserisciRaggruppamentoArticoli");
        rigaOrdineManager.inserisciRaggruppamentoArticoli(idAreaOrdine, provenienzaPrezzo, idRaggruppamentoArticoli,
                data, idSedeEntita, idListinoAlternativo, idListino, importo, codiceIvaAlternativo, idTipoMezzo,
                idZonaGeografica, noteSuDestinazione, codiceValuta, codiceLingua, dataConsegna, idAgente,
                tipologiaCodiceIvaAlternativo, percentualeScontoCommerciale);
        logger.debug("--> Exit inserisciRaggruppamentoArticoli");
    }

    @Override
    public AreaOrdineFullDTO ricalcolaPrezziOrdine(Integer idAreaOrdine) {

        areaOrdineManager.ricalcolaPrezziOrdine(idAreaOrdine);

        AreaOrdine areaOrdine = new AreaOrdine();
        areaOrdine.setId(idAreaOrdine);
        AreaOrdineFullDTO areaOrdineFullDTO = caricaAreaOrdineFullDTO(areaOrdine);

        return areaOrdineFullDTO;
    }

    @Override
    @RolesAllowed("visualizzaDocOrdine")
    public List<AreaOrdineRicerca> ricercaAreeOrdine(ParametriRicercaAreaOrdine parametriRicercaAreaOrdine) {
        return areaOrdineManager.ricercaAreeOrdine(parametriRicercaAreaOrdine);
    }

    @Override
    @RolesAllowed("gestioneDocOrdine")
    public AreaOrdineFullDTO salvaAreaOrdine(AreaOrdine areaOrdine, AreaRate areaRate) {
        logger.debug("--> Enter salvaAreaOrdine");

        boolean cambioSconto = areaRateManager.checkCambioScontoCommerciale(areaRate);
        if (cambioSconto) {
            CodicePagamento codicePagamento = areaRate.getCodicePagamento();
            BigDecimal scontoComm = (codicePagamento != null) ? codicePagamento.getPercentualeScontoCommerciale()
                    : BigDecimal.ZERO;
            rigaOrdineManager.aggiornaScontoCommerciale(areaOrdine, scontoComm);
        }

        boolean cambioStato = areaOrdineVerificaManager.checkCambioStato(areaOrdine, areaRate);
        // se devo cambiare lo stato vado anche a svalidare le righe magazzino
        if (cambioStato) {
            areaOrdine.getDatiValidazioneRighe().invalida();
            areaOrdine.setStatoAreaOrdine(StatoAreaOrdine.PROVVISORIO);
        }

        AreaOrdine areaOrdineSave = areaOrdineManager.salvaAreaOrdine(areaOrdine);

        if (areaRate != null) {
            // aggiorna AreaPartite con il documento salvato
            areaRate.setDocumento(areaOrdineSave.getDocumento());
            areaRateManager.salvaAreaRate(areaRate);
        }

        AreaOrdineFullDTO areaOrdineFullDTO = caricaAreaOrdineFullDTO(areaOrdineSave);

        logger.debug("--> Exit salvaAreaOrdine");
        return areaOrdineFullDTO;
    }

    @Override
    @RolesAllowed("gestioneDocOrdine")
    public RigaOrdine salvaRigaOrdine(RigaOrdine rigaOrdine, boolean checkRiga) {
        if (checkRiga) {
            return rigaOrdineManager.getDao(rigaOrdine).salvaRigaOrdine(rigaOrdine);
        } else {
            return rigaOrdineManager.getDao(rigaOrdine).salvaRigaOrdineNoCheck(rigaOrdine);
        }
    }

    @Override
    @RolesAllowed("gestioneBackOrder")
    public List<RigaOrdineImportata> salvaRigaOrdineImportata(RigaOrdineImportata rigaOrdine) {
        return importazioneOrdiniManager.salvaRigaOrdineImportata(rigaOrdine);
    }

    @Override
    public void salvaRigheMagazzinoNoCheck(
            Map<AreaMagazzino, List<it.eurotn.panjea.magazzino.domain.RigaArticolo>> righePerArea) {
        Set<AreaMagazzino> keySet = righePerArea.keySet();

        for (AreaMagazzino areaMagazzino : keySet) {
            List<it.eurotn.panjea.magazzino.domain.RigaArticolo> righeDaSalvare = righePerArea.get(areaMagazzino);

            for (it.eurotn.panjea.magazzino.domain.RigaArticolo rigaDaSalvare : righeDaSalvare) {
                rigaMagazzinoManager.getDao().salvaRigaMagazzinoNoCheck(rigaDaSalvare);
            }
            // attiva l'interceptor per l'area magazzino e quindi aggiorna le
            // righe articolo nel datawarehouse, qui
            // l'area
            // magazzino arriva già confermata e visto il noCheck, lo stato non
            // viene alterato
            statiAreaMagazzinoManager.cambiaStatoDaProvvisorioInConfermato(areaMagazzino);
        }
    }

    @Override
    public TipoAreaOrdine salvaTipoAreaOrdine(TipoAreaOrdine tipoAreaOrdine) {
        return tipiAreaOrdineManager.salvaTipoAreaOrdine(tipoAreaOrdine);
    }

    @Override
    public void spostaRighe(Set<Integer> righeDaSpostare, Integer idDest) {
        areaOrdineManager.spostaRighe(righeDaSpostare, idDest);
    }

    @Override
    public AreaOrdine totalizzaDocumento(AreaOrdine areaOrdine, AreaPartite areaPartite) {
        return areaOrdineManager.totalizzaDocumento(areaOrdine, areaPartite);
    }

    @Override
    @RolesAllowed("gestioneDocOrdine")
    public AreaOrdineFullDTO validaRigheOrdine(AreaOrdine areaOrdine, AreaRate areaRate, boolean cambioStato) {
        logger.debug("--> Enter validaRigheOrdine");

        AreaOrdine areaOrdineConfermata = null;
        try {
            areaOrdineConfermata = areaOrdineManager.validaRigheOrdine(areaOrdine, areaRate, cambioStato);
        } catch (Exception e) {
            logger.error("--> errore nel validare le righe ordine ", e);
            throw new RuntimeException("errore nel validare le righe ordine", e);
        }

        AreaOrdineFullDTO areaOrdineFullDTO = caricaAreaOrdineFullDTO(areaOrdineConfermata);
        logger.debug("--> Exit validaRigheOrdine");
        return areaOrdineFullDTO;
    }

    @Override
    @RolesAllowed("evasione")
    public int verificaNumeroOrdiniDaEvadere() {
        return distintaCaricoManager.verificaNumeroOrdiniDaEvadere();
    }
}
