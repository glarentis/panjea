package it.eurotn.panjea.ordini.manager.documento.evasione;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.transform.Transformers;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.ejb.TransactionTimeout;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.manager.interfaces.EntitaManager;
import it.eurotn.panjea.anagrafica.manager.interfaces.SediEntitaManager;
import it.eurotn.panjea.contabilita.service.exception.ContiBaseException;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.lotti.exception.EvasioneLottiException;
import it.eurotn.panjea.lotti.exception.LottiException;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.magazzino.manager.interfaces.GiacenzaManager;
import it.eurotn.panjea.magazzino.service.exception.ContabilizzazioneException;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.panjea.ordini.domain.RigaArticolo;
import it.eurotn.panjea.ordini.domain.SedeOrdine;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.domain.documento.evasione.DatiEvasioneDocumento;
import it.eurotn.panjea.ordini.domain.documento.evasione.DistintaCarico;
import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCarico;
import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCaricoLotto;
import it.eurotn.panjea.ordini.exception.AgenteFatturazioneSenzaClienteException;
import it.eurotn.panjea.ordini.exception.CodicePagamentoAssenteException;
import it.eurotn.panjea.ordini.exception.CodicePagamentoEvasioneAssenteException;
import it.eurotn.panjea.ordini.exception.EntitaSenzaTipoDocumentoEvasioneException;
import it.eurotn.panjea.ordini.exception.TipoAreaPartitaDestinazioneRichiestaException;
import it.eurotn.panjea.ordini.manager.documento.evasione.interfaces.DatiDistintaCaricoEvasioneManager;
import it.eurotn.panjea.ordini.manager.documento.evasione.interfaces.DistintaCaricoManager;
import it.eurotn.panjea.ordini.manager.documento.evasione.interfaces.DocumentoEvasioneManager;
import it.eurotn.panjea.ordini.manager.documento.evasione.interfaces.EvasioneGenerator;
import it.eurotn.panjea.ordini.manager.interfaces.OrdiniSettingsManager;
import it.eurotn.panjea.ordini.manager.interfaces.SediOrdineManager;
import it.eurotn.panjea.ordini.util.parametriricerca.ParametriRicercaEvasione;
import it.eurotn.panjea.partite.manager.interfaces.TipiAreaPartitaManager;
import it.eurotn.panjea.rate.manager.interfaces.AreaRateManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.util.KeyFromValueProvider;
import it.eurotn.util.PanjeaEJBUtil;

/**
 * @author fattazzo
 */
@Stateless(name = "Panjea.DistintaCaricoManager")
@SecurityDomain("PanjeaLoginModule")
@LocalBinding(jndiBinding = "Panjea.DistintaCaricoManager")
public class DistintaCaricoManagerBean implements DistintaCaricoManager {

    public enum ECaricamentoRigaDistinta {
        SOLO_NON_FORZATE, SOLO_FORZATE, TUTTE
    }

    private static final Logger LOGGER = Logger.getLogger(DistintaCaricoManagerBean.class);

    public static final String EVASIONE_MESSAGE_SELECTOR = "evasioneMessageSelector";

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    private GiacenzaManager giacenzaManager;

    @EJB
    private DatiDistintaCaricoEvasioneManager datiDistintaCaricoEvasioneManager;

    @EJB
    private DocumentoEvasioneManager documentoEvasioneManager;

    @EJB
    private SediOrdineManager sediOrdineManager;

    @EJB
    private EvasioneGenerator evasioneGenerator;

    @EJB
    private EntitaManager entitaManager;

    @EJB
    private SediEntitaManager sediEntitaManager;

    @EJB
    private TipiAreaPartitaManager tipiAreaPartitaManager;

    @EJB
    private AreaRateManager areaRateManager;

    @EJB
    private OrdiniSettingsManager ordiniSettings;

    @Override
    public List<RigaDistintaCarico> aggiornaRigheCaricoConDatiEvasione(
            List<RigaDistintaCarico> righeDistintaDaAggiornare) {
        DatiDistintaCaricoEvasione datiDistintaCaricoEvasione = datiDistintaCaricoEvasioneManager
                .getDatiDistintaCaricoEvasione();
        for (RigaDistintaCarico rigaDistintaCarico : righeDistintaDaAggiornare) {
            AreaOrdine areaOrdine = new AreaOrdine();
            areaOrdine.setId(rigaDistintaCarico.getIdAreaOrdine());

            DatiEvasioneDocumento datiEvasioneDocumento = new DatiEvasioneDocumento();
            datiEvasioneDocumento.setCodicePagamento(
                    datiDistintaCaricoEvasione.getCodicePagamento(areaOrdine, rigaDistintaCarico.getSedeEntita()));
            datiEvasioneDocumento.setContoTerzi(datiDistintaCaricoEvasione
                    .isContoTerzi(rigaDistintaCarico.getSedeEntita(), rigaDistintaCarico.getArticolo()));
            datiEvasioneDocumento.setTipoAreaEvasione(datiDistintaCaricoEvasione.getTipoDocumentoDestinazione(
                    rigaDistintaCarico.getSedeEntita(), rigaDistintaCarico.getArticolo()));

            rigaDistintaCarico.setDatiEvasioneDocumento(datiEvasioneDocumento);
        }
        return righeDistintaDaAggiornare;
    }

    @Override
    public void associaTipoAreaEvasione(Map<TipoAreaMagazzino, Set<EntitaLite>> map) {
        LOGGER.debug("--> Enter associaTipoAreaEvasione");

        for (Entry<TipoAreaMagazzino, Set<EntitaLite>> entry : map.entrySet()) {

            for (EntitaLite entitaLite : entry.getValue()) {
                SedeOrdine sedeOrdine = sediOrdineManager.caricaSedeOrdinePrincipaleEntita(entitaLite);
                sedeOrdine.setTipoAreaEvasione(entry.getKey());

                try {
                    panjeaDAO.saveWithoutFlush(sedeOrdine);
                } catch (DAOException e) {
                    LOGGER.error(
                            "-->errore durante il salvataggio della sede ordine per l'entità " + entitaLite.getId(), e);
                    throw new GenericException(
                            "errore durante il salvataggio della sede ordine per l'entità " + entitaLite.getId(), e);
                }
            }

            panjeaDAO.getEntityManager().flush();
        }

        LOGGER.debug("--> Exit associaTipoAreaEvasione");
    }

    @Override
    public Map<ArticoloLite, Double> calcolaGiacenze(DepositoLite depositoLite, Date data) {
        // Recupero le giacenze di magazzino
        Map<ArticoloLite, Double> giacenze = giacenzaManager.calcolaGiacenze(depositoLite, data);

        // Recupera le qta sulla distinta base
        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        sb.append("new map(riga.rigaArticolo.articolo as articolo,sum(riga.qtaDaEvadere)  as qta) ");
        sb.append("from RigaDistintaCarico riga ");
        sb.append("group by riga.rigaArticolo.articolo ");
        Query query = panjeaDAO.prepareQuery(sb.toString());
        @SuppressWarnings("unchecked")
        List<Object> righeDistinteRaggruppate = query.getResultList();

        // Aggiorno le giacenze in modo da ridurre la giacenza in relazione alla quantità da evadere
        // sulle missioni
        // presenti
        for (Object riga : righeDistinteRaggruppate) {
            @SuppressWarnings("unchecked")
            Map<String, Object> valore = (Map<String, Object>) riga;
            ArticoloLite articolo = (ArticoloLite) valore.get("articolo");
            Double qtaGiacenza = giacenze.get(articolo);
            if (qtaGiacenza == null) {
                qtaGiacenza = 0.0;
            }
            qtaGiacenza -= (Double) valore.get("qta");
            giacenze.put(articolo, qtaGiacenza);
        }
        return giacenze;
    }

    @Override
    public void cancellaRigheDistintaCarico(Set<RigaDistintaCarico> righeDistintaCarico) {
        LOGGER.debug("--> Enter rimuoviRigheDistintaCarico");
        for (RigaDistintaCarico rigaDistintaCarico : righeDistintaCarico) {
            try {
                panjeaDAO.delete(rigaDistintaCarico);
            } catch (DAOException e) {
                throw new GenericException(e);
            }
        }
        // Cancello tutte le distinte che non hanno righe
        Query queryDeleteDistinte = panjeaDAO.prepareQuery("delete from DistintaCarico d where d.righeEvasione.size=0");
        try {
            panjeaDAO.executeQuery(queryDeleteDistinte);
        } catch (DAOException e) {
            throw new GenericException(e);
        }
        LOGGER.debug("--> Exit enclosing_method");
    }

    @Override
    public void cancellaRigheDistintaCaricoLotti(RigaDistintaCarico rigaDistintaCarico) {
        LOGGER.debug("--> Enter cancellaRigheDistintaCaricoLotti");

        String queryString = "delete from RigaDistintaCaricoLotto rdcl where rdcl.rigaDistintaCarico = :paramRigaDistinta";
        Query query = panjeaDAO.prepareQuery(queryString);
        query.setParameter("paramRigaDistinta", rigaDistintaCarico);

        try {
            panjeaDAO.executeQuery(query);
        } catch (Exception e) {
            LOGGER.error("--> errore durante la cancellazione delle righe lotto della riga distinta"
                    + rigaDistintaCarico.getId(), e);
            throw new GenericException("errore durante la cancellazione delle righe lotto della riga distinta"
                    + rigaDistintaCarico.getId(), e);
        }

        LOGGER.debug("--> Exit cancellaRigheDistintaCaricoLotti");
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<RigaDistintaCarico> caricaRigheDistintaCarico(List<DistintaCarico> distinte) {
        LOGGER.debug("--> Enter caricaRigheDistintaCarico");

        StringBuilder sb = new StringBuilder();
        sb.append("select DISTINCT rigaEv from RigaDistintaCarico rigaEv ");
        sb.append("join fetch rigaEv.rigaArticolo rigaOrdine ");
        sb.append("left join fetch rigaOrdine.attributi ");
        sb.append("join fetch rigaOrdine.areaOrdine ");
        sb.append("join fetch rigaEv.distintaCarico ");
        if (!CollectionUtils.isEmpty(distinte)) {
            sb.append("where rigaEv.distintaCarico in (:paramDistinte) ");
        }
        Query query = panjeaDAO.prepareQuery(sb.toString());
        if (!CollectionUtils.isEmpty(distinte)) {
            query.setParameter("paramDistinte", distinte);
        }
        List<RigaDistintaCarico> righeEvasione = new ArrayList<RigaDistintaCarico>();
        try {
            righeEvasione = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            LOGGER.error("-->errore durante il caricamento delle righe evasione.", e);
            throw new GenericException("errore durante il caricamento delle righe evasione.", e);
        }

        LOGGER.debug("--> Exit caricaRigheDistintaCarico");
        return righeEvasione;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<RigaDistintaCaricoLotto> caricaRigheDistintaCaricoLotto(RigaDistintaCarico rigaDistintaCarico) {
        LOGGER.debug("--> Enter caricaRigheDistintaCaricoLotto");

        List<RigaDistintaCaricoLotto> righeDistintaCaricoLotto = new ArrayList<RigaDistintaCaricoLotto>();
        Query query = panjeaDAO.prepareNamedQuery("RigaDistintaCaricoLotto.caricaByRigaDistinta");
        query.setParameter("idRigaDistinta", rigaDistintaCarico.getId());
        try {
            righeDistintaCaricoLotto = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            LOGGER.error("-->errore durante il caricamento delle righe evasione lotto.", e);
            throw new GenericException("errore durante il caricamento delle righe evasione lotto.", e);
        }

        LOGGER.debug("--> Exit caricaRigheDistintaCaricoLotto");
        return righeDistintaCaricoLotto;
    }

    @Override
    public List<RigaDistintaCarico> caricaRigheInMagazzino() {
        LOGGER.debug("--> Enter caricaRigheEvasione");
        List<RigaDistintaCarico> righeEvasione = caricaRigheDistintaCarico(null);
        LOGGER.debug("--> Exit caricaRigheEvasione con num righe " + righeEvasione.size());
        return righeEvasione;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<RigaDistintaCarico> caricaRighePerEvasione(ParametriRicercaEvasione parametriRicercaEvasione) {

        String hqlStringQuery = EvasioneOrdiniQueryBuilder.getHQL(parametriRicercaEvasione);

        org.hibernate.ejb.HibernateQuery query = (org.hibernate.ejb.HibernateQuery) panjeaDAO.getEntityManager()
                .createQuery(hqlStringQuery);
        EvasioneOrdiniQueryBuilder.setParameters(query, parametriRicercaEvasione);
        query.getHibernateQuery().setResultTransformer(
                Transformers.aliasToBean(ordiniSettings.caricaOrdiniSettings().getClassForRigaDistintaCarico()));

        List<RigaDistintaCarico> righeEvasione = null;
        try {
            righeEvasione = panjeaDAO.getResultList(query);
            // se il massimo dei livelli>1 allora devo inserire le colonne per le testate
            String[] descrizioneLivelli = new String[10];
            Arrays.fill(descrizioneLivelli, null);
            for (RigaDistintaCarico rigaDistintaCarico : righeEvasione) {
                descrizioneLivelli[rigaDistintaCarico.getLivello()] = rigaDistintaCarico.getDescrizioneTestata();
                StringBuilder descrizioneTestataBuilder = new StringBuilder(100);
                for (int i = 1; i < rigaDistintaCarico.getLivello() + 1; i++) {
                    descrizioneTestataBuilder.append("-").append(descrizioneLivelli[i]);
                }
                rigaDistintaCarico.setDescrizioneTestata(StringUtils.right(descrizioneTestataBuilder.toString(),
                        descrizioneTestataBuilder.length() - 1));
            }
        } catch (Exception e) {
            LOGGER.error("--> errore in caricaRigheEvasione", e);
            throw new GenericException(e);
        }
        return righeEvasione;
    }

    @Override
    public List<DistintaCarico> creaDistintadiCarico(List<RigaDistintaCarico> righeEvasione) {
        List<DistintaCarico> distinte = new ArrayList<DistintaCarico>();
        // Raggruppo per deposito
        Map<DepositoLite, List<RigaDistintaCarico>> righeraggruppate = PanjeaEJBUtil.listToMap(righeEvasione,
                new KeyFromValueProvider<RigaDistintaCarico, DepositoLite>() {
                    @Override
                    public DepositoLite keyFromValue(RigaDistintaCarico elem) {
                        return elem.getDeposito();
                    }
                });

        for (Entry<DepositoLite, List<RigaDistintaCarico>> depositoConRighe : righeraggruppate.entrySet()) {
            try {
                DistintaCarico distintaCarico = new DistintaCarico();
                distintaCarico.setRigheEvasione(new ArrayList<RigaDistintaCarico>());
                for (RigaDistintaCarico rigaEvasione : depositoConRighe.getValue()) {
                    // RigaEvasione ha una rigaArticolo proveniente da una query
                    // ad hoc. La ricarico.
                    RigaArticolo rigaArticolo = panjeaDAO.load(RigaArticolo.class,
                            rigaEvasione.getRigaArticolo().getId());
                    distintaCarico.addRigaMissione(rigaEvasione, rigaArticolo);
                }
                distintaCarico = panjeaDAO.save(distintaCarico);
                distinte.add(distintaCarico);
            } catch (Exception e) {
                LOGGER.error("-->errore nel salvare la distinta di carico", e);
                throw new GenericException(e);
            }
        }
        return distinte;

    }

    @Override
    public void evadiOrdini(List<RigaDistintaCarico> righeEvasione, AreaMagazzino documentoEvasione)
            throws EvasioneLottiException {
        LOGGER.debug("--> Enter evadiOrdini");
        evasioneGenerator.evadiOrdini(righeEvasione, documentoEvasione);
        LOGGER.debug("--> Exit evadiOrdini");
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @TransactionTimeout(value = 7200)
    @Override
    public List<AreaMagazzino> evadiOrdini(List<RigaDistintaCarico> righeEvasione, Date dataEvasione)
            throws EntitaSenzaTipoDocumentoEvasioneException, ContabilizzazioneException, ContiBaseException,
            TipoAreaPartitaDestinazioneRichiestaException, CodicePagamentoEvasioneAssenteException,
            CodicePagamentoAssenteException, LottiException {

        List<AreaMagazzino> areeSalvate = new ArrayList<AreaMagazzino>();
        // tolgo le righe non valide per l'evasione
        List<RigaDistintaCarico> righeValide = new ArrayList<RigaDistintaCarico>();
        for (RigaDistintaCarico rigaDistintaCarico : righeEvasione) {
            if (rigaDistintaCarico.isMissioneAllowed()) {
                righeValide.add(rigaDistintaCarico);
            }
        }

        if (righeValide.isEmpty()) {
            return areeSalvate;
        }

        // Se la prima riga di evasione non ha dati di evasione risetto i dati
        // di evasione
        if (!righeValide.isEmpty() && righeValide.get(0).getDatiEvasioneDocumento() == null) {
            righeValide = aggiornaRigheCaricoConDatiEvasione(righeValide);
        }

        verificaRigheEvasione(righeValide);

        Set<Integer> righeOrdineDaforzare = new HashSet<Integer>();
        // Ciclo sulle righe e ricaco righe da forzare
        for (RigaDistintaCarico rigaDistintaCarico : righeEvasione) {
            if (rigaDistintaCarico.getForzata()) {
                righeOrdineDaforzare.add(rigaDistintaCarico.getRigaArticolo().getId());
            }
        }

        // sostituisco agli agenti da fatturare il relativo cliente
        try {
            righeValide = transforOrdiniPerFatturazioneAgente(righeValide);
        } catch (AgenteFatturazioneSenzaClienteException e) {
            throw new GenericException(e);
        }

        List<AreaMagazzinoFullDTO> areeMagazzinoFullDTO = evasioneGenerator.evadiOrdini(righeValide, dataEvasione);

        // creo il documento di evasione
        for (AreaMagazzinoFullDTO areaMagazzinoFullDTO : areeMagazzinoFullDTO) {
            AreaMagazzino areaMagazzinoSalvata = documentoEvasioneManager.salvaDocumentoEvasione(areaMagazzinoFullDTO);
            areeSalvate.add(areaMagazzinoSalvata);
        }
        // Cancello dalle distinte di carico le righe che sono state evase
        // passando attraverso il UUIDContabilizzazione.
        String uuid = areeMagazzinoFullDTO.get(0).getAreaMagazzino().getUUIDContabilizzazione();
        documentoEvasioneManager.aggiornaDistinteCaricoEvase(righeOrdineDaforzare, uuid);

        return areeSalvate;
    }

    /**
     * Sostituisce l'entita e sede dell'ordine con il corrispondente cliente dell'agente.
     *
     * @param righeDistinta
     *            righe da sostituire
     * @return righe sostituite
     * @throws AgenteFatturazioneSenzaClienteException
     *             rilanciata se non esiste il cliente associato all'agente
     */
    private List<RigaDistintaCarico> transforOrdiniPerFatturazioneAgente(List<RigaDistintaCarico> righeDistinta)
            throws AgenteFatturazioneSenzaClienteException {

        Map<AgenteLite, SedeEntita> mapClientiAgente = new HashMap<AgenteLite, SedeEntita>();

        for (RigaDistintaCarico rigaDistintaCarico : righeDistinta) {

            AgenteLite agenteLite = rigaDistintaCarico.getRigaArticolo().getAreaOrdine().getAgente();

            if (agenteLite != null && agenteLite.isFatturazioneAgente()) {

                if (!mapClientiAgente.containsKey(agenteLite)) {
                    List<EntitaLite> listEntita = entitaManager.caricaEntita(agenteLite.getAnagrafica());

                    ClienteLite clienteAgente = null;
                    for (EntitaLite entitaLite : listEntita) {
                        if (entitaLite instanceof ClienteLite) {
                            clienteAgente = (ClienteLite) entitaLite;
                            break;
                        }
                    }

                    if (clienteAgente == null) {
                        throw new AgenteFatturazioneSenzaClienteException(agenteLite);
                    }

                    SedeEntita sedeEntitaAgente;
                    try {
                        sedeEntitaAgente = sediEntitaManager
                                .caricaSedePrincipaleEntita(clienteAgente.creaProxyEntita());
                    } catch (Exception e) {
                        LOGGER.error("--> errore durante il caricamento della sede principale dell'entità", e);
                        throw new GenericException("errore durante il caricamento della sede principale dell'entità",
                                e);
                    }
                    mapClientiAgente.put(agenteLite, sedeEntitaAgente);
                }

                rigaDistintaCarico.setSedeEntitaEvasione(mapClientiAgente.get(agenteLite));
            }
        }

        return righeDistinta;
    }

    @Override
    public int verificaNumeroOrdiniDaEvadere() {
        LOGGER.debug("--> Enter verificaNumeroOrdiniDaEvadere");
        StringBuilder sb = new StringBuilder();
        sb.append(
                "select distinct ao.id from RigaDistintaCarico r join r.rigaArticolo.areaOrdine ao where r.qtaDaEvadere=r.qtaEvasa or forzata=1");
        Query query = panjeaDAO.prepareQuery(sb.toString());
        int numDocumenti = 0;
        try {
            numDocumenti = query.getResultList().size();
        } catch (NoResultException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.trace(e);
                LOGGER.debug("--> Nessuna riga nelle distinte di carico");
            }
        }
        LOGGER.debug("--> Exit verificaNumeroOrdiniDaEvadere");
        return numDocumenti;
    }

    /**
     *
     * @param righeEvasione
     *            .
     * @throws EntitaSenzaTipoDocumentoEvasioneException
     *             .
     * @throws CodicePagamentoEvasioneAssenteException
     *             .
     * @throws CodicePagamentoAssenteException
     *             .
     */
    private void verificaRigheEvasione(List<RigaDistintaCarico> righeEvasione)
            throws EntitaSenzaTipoDocumentoEvasioneException, CodicePagamentoEvasioneAssenteException,
            CodicePagamentoAssenteException {
        Set<EntitaLite> entitaSenzaTipoAreaOrdineEvasione = new HashSet<EntitaLite>();

        // verifico che non ci siano entità senza tipo area ordine di evasione
        for (RigaDistintaCarico rigaDistintaCarico : righeEvasione) {
            if (rigaDistintaCarico.getDatiEvasioneDocumento().getTipoAreaEvasione() == null) {
                rigaDistintaCarico = panjeaDAO.getEntityManager().merge(rigaDistintaCarico);
                Hibernate.initialize(rigaDistintaCarico.getSedeEntita().getEntita().getAnagrafica());
                entitaSenzaTipoAreaOrdineEvasione.add(rigaDistintaCarico.getSedeEntita().getEntita().getEntitaLite());
            }
        }
        if (!entitaSenzaTipoAreaOrdineEvasione.isEmpty()) {
            throw new EntitaSenzaTipoDocumentoEvasioneException(entitaSenzaTipoAreaOrdineEvasione);
        }

        Set<AreaOrdine> areeOrdineSenzaCodicePagamento = new TreeSet<AreaOrdine>();
        Set<AreaOrdine> areeOrdine = new TreeSet<AreaOrdine>();
        Map<Integer, Boolean> tipiAreePartita = new HashMap<Integer, Boolean>();
        for (RigaDistintaCarico rigaDistintaCarico : righeEvasione) {
            // metto in cache i tipi area partita per non continuare a ricaricarli
            TipoDocumento tipoDocumento = rigaDistintaCarico.getRigaArticolo().getAreaOrdine().getDocumento()
                    .getTipoDocumento();
            TipoDocumento tipoDocumentoEvasione = rigaDistintaCarico.getDatiEvasioneDocumento().getTipoAreaEvasione()
                    .getTipoDocumento();
            if (!tipiAreePartita.containsKey(tipoDocumento.getId())) {
                tipiAreePartita.put(tipoDocumento.getId(),
                        tipiAreaPartitaManager.caricaTipoAreaPartitaPerTipoDocumento(tipoDocumento).getId() != null);
            }
            if (!tipiAreePartita.containsKey(tipoDocumentoEvasione.getId())) {
                tipiAreePartita.put(tipoDocumentoEvasione.getId(), tipiAreaPartitaManager
                        .caricaTipoAreaPartitaPerTipoDocumento(tipoDocumentoEvasione).getId() != null);
            }
            boolean tipoAreaPartitaPresente = tipiAreePartita.get(tipoDocumento.getId());

            if (tipoAreaPartitaPresente) {
                // controllo che se l'ordine ha un tipo area partite deve avere anche un codice
                // pagamento
                if (areaRateManager.caricaAreaRate(rigaDistintaCarico.getRigaArticolo().getAreaOrdine().getDocumento())
                        .getCodicePagamento() == null) {
                    areeOrdineSenzaCodicePagamento.add(rigaDistintaCarico.getRigaArticolo().getAreaOrdine());
                }
            } else {
                // l'ordine non ha un tipo area partite. Se il documento di destinazione ce l'ha,
                // controllo se riesco a
                // recuperare il codice pagamento dalla sede
                if (tipiAreePartita.get(tipoDocumentoEvasione.getId())
                        && rigaDistintaCarico.getDatiEvasioneDocumento().getCodicePagamento() == null) {
                    areeOrdine.add(rigaDistintaCarico.getRigaArticolo().getAreaOrdine());
                }
            }
        }
        if (!areeOrdineSenzaCodicePagamento.isEmpty()) {
            throw new CodicePagamentoAssenteException(areeOrdineSenzaCodicePagamento);
        }

        if (!areeOrdine.isEmpty()) {
            throw new CodicePagamentoEvasioneAssenteException(areeOrdine);
        }
    }
}
