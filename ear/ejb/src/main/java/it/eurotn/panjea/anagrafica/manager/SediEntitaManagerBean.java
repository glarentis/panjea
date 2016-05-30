package it.eurotn.panjea.anagrafica.manager;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.jboss.annotation.IgnoreDependency;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.dao.exception.VincoloException;
import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.anagrafica.domain.CaricamentoSediEntita;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.RapportoBancarioSedeEntita;
import it.eurotn.panjea.anagrafica.domain.SedeAnagrafica;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.TipoSedeEntita;
import it.eurotn.panjea.anagrafica.domain.TipoSedeEntita.TipoSede;
import it.eurotn.panjea.anagrafica.domain.lite.SedeEntitaLite;
import it.eurotn.panjea.anagrafica.domain.lite.VettoreLite;
import it.eurotn.panjea.anagrafica.manager.interfaces.AnagraficheManager;
import it.eurotn.panjea.anagrafica.manager.interfaces.AnagraficheTipologieManager;
import it.eurotn.panjea.anagrafica.manager.interfaces.ContattiManager;
import it.eurotn.panjea.anagrafica.manager.interfaces.SediEntitaManager;
import it.eurotn.panjea.anagrafica.manager.rapportibancarisedeentita.interfaces.RapportiBancariSedeEntitaManager;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;
import it.eurotn.panjea.anagrafica.service.exception.SedeAnagraficaOrphanException;
import it.eurotn.panjea.anagrafica.service.exception.SedeEntitaPrincipaleAlreadyExistException;
import it.eurotn.panjea.anagrafica.service.exception.TipoSedeEntitaNonTrovataException;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino;
import it.eurotn.panjea.magazzino.manager.interfaces.SediMagazzinoManager;
import it.eurotn.panjea.riepilogo.util.RiepilogoSedeEntitaDTO;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

/**
 * SessionBean Manager per {@link SedeEntita} .
 *
 * @author adriano
 * @version 1.0, 17/dic/07
 */
@Stateless(name = "Panjea.SediEntitaManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.SediEntitaManager")
public class SediEntitaManagerBean implements SediEntitaManager {

    private static Logger logger = Logger.getLogger(SediEntitaManagerBean.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    private AnagraficheManager anagraficaManager;

    @EJB
    private AnagraficheTipologieManager anagraficheTipologieManager;

    @EJB
    private ContattiManager contattiManager;

    @EJB
    @IgnoreDependency
    protected SediMagazzinoManager sediMagazzinoManager;

    @Resource
    private SessionContext context;

    @EJB
    @IgnoreDependency
    private RapportiBancariSedeEntitaManager rapportiBancariSedeEntitaManager;

    @Override
    public void cancellaSedeEntita(it.eurotn.panjea.anagrafica.domain.SedeEntita sedeEntita, boolean deleteOrphan)
            throws AnagraficaServiceException, SedeAnagraficaOrphanException {
        logger.debug("--> Enter cancellaSedeEntita");

        if (sedeEntita.isNew()) {
            logger.error("--> Impossibile cancellare una sedeEntita con id nullo.");
            throw new AnagraficaServiceException("Impossibile cancellare una sedeEntita con id nullo.");
        }

        // Verifico se la sede anagrafica
        // della sede entit� cancellata � orfana o no. Nel caso in cui risulti
        // esserlo e non � richiesta la
        // cancellazione delle sedi orfane rilancio un'eccezione
        boolean isOrphan = anagraficaManager.isSedeOrphan(sedeEntita.getSede());
        if (isOrphan && (!deleteOrphan)) {
            logger.warn("--> La sede risulta essere orfana. Forzare la cancellazione per poterla cancellare.");
            throw new SedeAnagraficaOrphanException(
                    "La sede risulta essere orfana. Forzare la cancellazione per poterla cancellare.",
                    sedeEntita.getSede());
        }

        contattiManager.cancellaContattiPerSedeEntita(sedeEntita);

        // Cancello i rapporti bancari
        List<RapportoBancarioSedeEntita> rapportiBancari = rapportiBancariSedeEntitaManager
                .caricaRapportiBancariSedeEntita(sedeEntita.getId());
        for (RapportoBancarioSedeEntita rapportoBancarioSedeEntita : rapportiBancari) {
            rapportiBancariSedeEntitaManager.cancellaRapportoBancarioSedeEntita(rapportoBancarioSedeEntita);
        }

        try {
            panjeaDAO.delete(sedeEntita);
        } catch (VincoloException e) {
            logger.error("--> Errore durante la cancellazione della sedeEntita " + sedeEntita.getId(), e);
            throw new RuntimeException(e);
        } catch (DAOException e) {
            logger.error("--> Errore durante la cancellazione della sedeEntita " + sedeEntita.getId(), e);
            throw new RuntimeException(e);
        }

        if (isOrphan) {
            logger.debug("--> cancellazione sede anagrafica orfana " + sedeEntita.getSede().getId());
            anagraficaManager.cancellaSedeAnagrafica(sedeEntita.getSede());
        }
        logger.debug("--> Exit cancellaSedeEntita");

    }

    @Override
    public String caricaNoteStampa(Integer idSedeEntita) {
        Query query = panjeaDAO.prepareNamedQuery("SedeEntita.caricaNoteStampa");
        query.setParameter("paramIdSedeEntita", idSedeEntita);
        String note = null;
        try {
            note = (String) panjeaDAO.getSingleResult(query);
        } catch (NoResultException e) {
            logger.warn("--> nessuna nota entita di stampa per la sede entita con id " + idSedeEntita);
        } catch (Exception e) {
            logger.error("--> errore durante il caricamento delle note stampa della sede entità", e);
            throw new RuntimeException("errore durante il caricamento delle note stampa della sede entità", e);
        }
        logger.debug("--> Exit caricaNoteStampa");
        return note;
    }

    @Override
    public List<RiepilogoSedeEntitaDTO> caricaRiepilogoSediEntita() {
        RiepilogoSediEntitaFactoryQueryBuilder queryBuilder = new RiepilogoSediEntitaFactoryQueryBuilder();
        return caricaRiepilogoSediEntita(queryBuilder);
    }

    /**
     * Carica una lista di riepilogoSedeEntitaDTO filtrate per la stringa passata come parametro.
     *
     * @param queryBuilder
     *            query builder da usare per la ricerca
     * @return lista Sedi entità
     */
    @SuppressWarnings("unchecked")
    private List<RiepilogoSedeEntitaDTO> caricaRiepilogoSediEntita(
            RiepilogoSediEntitaFactoryQueryBuilder queryBuilder) {
        logger.debug("--> Enter caricaRiepilogoSediEntita");

        // carico le sedi che non ereditano i dati commerciali
        String hqlSedi = queryBuilder.getHqlSedi();
        org.hibernate.Query query = ((Session) panjeaDAO.getEntityManager().getDelegate()).createQuery(hqlSedi);
        query.setResultTransformer(Transformers.aliasToBean(RiepilogoSedeEntitaDTO.class));
        query.setParameter("codiceAzienda", getCodiceAzienda());

        List<RiepilogoSedeEntitaDTO> riepilogo = new ArrayList<RiepilogoSedeEntitaDTO>();
        riepilogo = query.list();

        // carico le sedi che ereditano i dati commerciali
        String hqlSediEredita = queryBuilder.getHqlSediEreditarie();
        query = ((Session) panjeaDAO.getEntityManager().getDelegate()).createQuery(hqlSediEredita);
        query.setResultTransformer(Transformers.aliasToBean(RiepilogoSedeEntitaDTO.class));
        query.setParameter("codiceAzienda", getCodiceAzienda());

        List<RiepilogoSedeEntitaDTO> riepilogoNonEredita = new ArrayList<RiepilogoSedeEntitaDTO>();
        riepilogoNonEredita = query.list();

        riepilogo.addAll(riepilogoNonEredita);

        logger.debug("--> Exit caricaRiepilogoSediEntita");
        return riepilogo;
    }

    @Override
    public List<RiepilogoSedeEntitaDTO> caricaRiepilogoSediEntitaByAgente(AgenteLite agente) {
        RiepilogoSediEntitaPerAgenteQueryBuilder queryBuilder = new RiepilogoSediEntitaPerAgenteQueryBuilder(
                agente.getId());
        return caricaRiepilogoSediEntita(queryBuilder);
    }

    @Override
    public List<RiepilogoSedeEntitaDTO> caricaRiepilogoSediEntitaByListino(Integer idListino) {
        RiepilogoSediEntitaPerListinoQueryBuilder queryBuilder = new RiepilogoSediEntitaPerListinoQueryBuilder(
                idListino);
        return caricaRiepilogoSediEntita(queryBuilder);
    }

    @Override
    public List<RiepilogoSedeEntitaDTO> caricaRiepilogoSediEntitaByVettore(VettoreLite vettore) {
        RiepilogoSediEntitaPerVettoreQueryBuilder queryBuilder = new RiepilogoSediEntitaPerVettoreQueryBuilder(
                vettore.getId());
        return caricaRiepilogoSediEntita(queryBuilder);
    }

    @Override
    public SedeEntita caricaSedeEntita(Entita entita, String codiceSedeEntita) {
        logger.debug("--> Enter caricaSedePredefinitaEntita");
        if (entita.isNew()) {
            logger.error("--> Impossibile caricare la sede. Entita o id entita nullo.");
            throw new RuntimeException("Impossibile caricare la sede. Entita o id entita nullo.");
        }
        SedeEntita sedeEntita = null;
        try {
            Query query = panjeaDAO.prepareNamedQuery("SedeEntita.caricaByCodice");
            query.setParameter("idEntitaParam", entita.getId());
            query.setParameter("paramCodice", codiceSedeEntita);

            sedeEntita = (SedeEntita) panjeaDAO.getSingleResult(query);
        } catch (Exception e1) {
            logger.error("--> errore, impossibile recuperare SedeEntita con codice  " + codiceSedeEntita, e1);
            sedeEntita = null;
        }
        logger.debug("--> Exit caricaSedePredefinitaEntita");
        return sedeEntita;
    }

    @Override
    public SedeEntita caricaSedeEntita(Integer idSedeEntita)
            throws it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException {
        return caricaSedeEntita(idSedeEntita, true);
    }

    @Override
    public SedeEntita caricaSedeEntita(Integer idSedeEntita, boolean caricaLazy) throws AnagraficaServiceException {
        logger.debug("--> Enter caricaSedeEntita");
        SedeEntita sedeEntita;
        try {
            sedeEntita = panjeaDAO.load(SedeEntita.class, idSedeEntita);
            Hibernate.initialize(sedeEntita.getSedeCollegata());
            Hibernate.initialize(sedeEntita.getSedeSpedizione());
            Hibernate.initialize(sedeEntita.getSedeCollegata());
            Hibernate.initialize(sedeEntita.getEntita().getAnagrafica());
            if (!caricaLazy) {
                sedeEntita.getCategoriaEntita().size();
            }
        } catch (ObjectNotFoundException e) {
            logger.error("--> errore, impossibile recuperare SedeEntita con id " + idSedeEntita, e);
            throw new RuntimeException("--> errore, impossibile recuperare SedeEntita con id " + idSedeEntita, e);
        }
        logger.debug("--> Exit caricaSedeEntita");
        return sedeEntita;
    }

    @Override
    public SedeEntita caricaSedePredefinitaEntita(Entita entita) throws AnagraficaServiceException {
        logger.debug("--> Enter caricaSedePredefinitaEntita");
        if (entita.isNew()) {
            logger.error("--> Impossibile caricare la sede. Entita o id entita nullo.");
            throw new AnagraficaServiceException("Impossibile caricare la sede. Entita o id entita nullo.");
        }
        SedeEntita sedeEntita = null;
        try {
            Query query = panjeaDAO.prepareNamedQuery("SedeEntita.caricaPredefinita");
            query.setParameter("entitaParam", entita);
            @SuppressWarnings("unchecked")
            List<SedeEntita> sedi = panjeaDAO.getResultList(query);
            // La sede predefinita DEVE essere solamente 1.
            // Se ne trovo 2 seleziono la prima.
            // Se non ne trovo carico la sedePrincipale e aggiorno la principale come la predefinita
            if (sedi.size() == 0) {
                sedeEntita = caricaSedePrincipaleEntita(entita);
                sedeEntita.setPredefinita(true);
                sedeEntita = salvaSedeEntita(sedeEntita);
            } else {
                sedeEntita = sedi.get(0);
            }
            if (sedeEntita != null && sedeEntita.getSedeCollegata() != null) {
                sedeEntita.getSedeCollegata().getId();
            }
        } catch (Exception e1) {
            logger.error("--> errore, impossibile recuperare SedeEntita principale per id  " + entita.getId(), e1);
            throw new AnagraficaServiceException(e1);
        }
        logger.debug("--> Exit caricaSedePredefinitaEntita");
        return sedeEntita;
    }

    @Override
    public SedeEntita caricaSedePrincipaleEntita(Entita entita) throws AnagraficaServiceException {
        if (entita.isNew()) {
            logger.error("--> Impossibile caricare la sede. Entita o id entita nullo.");
            throw new AnagraficaServiceException("Impossibile caricare la sede. Entita o id entita nullo.");
        }
        return caricaSedePrincipaleEntita(entita.getId());
    }

    @Override
    public SedeEntita caricaSedePrincipaleEntita(Integer idEntita) throws AnagraficaServiceException {
        logger.debug("--> Enter caricaSedePrincipaleEntita");
        if (idEntita == null) {
            logger.error("--> Impossibile caricare la sede. Entita o id entita nullo.");
            throw new AnagraficaServiceException("Impossibile caricare la sede. Entita o id entita nullo.");
        }
        try {
            Session hibernateSession = (Session) panjeaDAO.getEntityManager().getDelegate();
            Criteria criteria = hibernateSession.createCriteria(SedeEntita.class)
                    .createAlias(SedeEntita.PROP_ENTITA, "entita").createAlias("entita.anagrafica", "anag")
                    .createAlias(SedeEntita.PROP_TIPO_SEDE, "tipoSede").add(Restrictions.eq("entita.id", idEntita))
                    .add(Restrictions.eq("tipoSede." + TipoSedeEntita.PROP_SEDE_PRINCIPALE, true));
            logger.debug("--> Exit caricaSedePrincipale");
            SedeEntita sedeEntita = (SedeEntita) criteria.uniqueResult();
            if (sedeEntita != null) {
                if (sedeEntita.getSedeCollegata() != null) {
                    sedeEntita.getSedeCollegata().getId();
                }
                sedeEntita.getCategoriaEntita().size();
            }
            return sedeEntita;
        } catch (HibernateException e1) {
            logger.error("--> errore, impossibile recuperare SedeEntita principale per id  " + idEntita, e1);
            throw new AnagraficaServiceException(e1);
        }

    }

    @SuppressWarnings("unchecked")
    @Override
    public List<SedeEntitaLite> caricaSediEntita(Integer idEntita) {
        logger.debug("--> Enter caricaSediEntita");

        javax.persistence.Query query = panjeaDAO.prepareNamedQuery("SedeEntitaLite.caricaByEntita");
        query.setParameter("paramIdEntita", idEntita);
        List<SedeEntitaLite> sediEntita;
        try {
            sediEntita = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            logger.error("--> errore durante il caricamento delle sedi dell'entità", e);
            throw new RuntimeException("errore durante il caricamento delle sedi dell'entità", e);
        }

        logger.debug("--> Exit caricaSediEntita");
        return sediEntita;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<SedeEntita> caricaSediEntita(String filtro, Integer idEntita,
            CaricamentoSediEntita caricamentoSediEntita, boolean caricaSedeDisabilitate)
                    throws AnagraficaServiceException {
        logger.debug("--> Enter caricaSediEntita");

        List<SedeEntita> sediEntita;
        StringBuilder sb = new StringBuilder(
                "from SedeEntita s join fetch s.sede sede left join fetch s.sedeCollegata left join fetch sede.datiGeografici.localita loc where s.entita.id = :paramIdEntita ");
        if (filtro != null) {
            sb.append("and ");
            sb.append("(sede.descrizione like :codice ");
            sb.append("or s.codice like :codice ");
            sb.append("or loc.descrizione like :codice ");
            sb.append("or sede.indirizzo like :codice )");
        }
        sb.append(" order by sede.descrizione");
        try {
            javax.persistence.Query query = panjeaDAO.prepareQuery(sb.toString());
            query.setParameter("paramIdEntita", idEntita);
            if (filtro != null) {
                query.setParameter("codice", filtro);
            }
            sediEntita = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            logger.error("--> errore, impossibile recuperare le sedi entita di entita " + idEntita, e);
            throw new it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException(e);
        }

        List<SedeEntita> sediResult = new ArrayList<SedeEntita>();

        // tolgo le sedi in basi ai filtri
        for (SedeEntita sedeEntita : sediEntita) {

            if (sedeEntita.isAbilitato() || caricaSedeDisabilitate) {

                switch (caricamentoSediEntita) {
                case ESCLUSE_SEDI_SPEDIZIONE_SERVIZI:
                    if (sedeEntita.getTipoSede().getTipoSede() != TipoSede.INDIRIZZO_SPEDIZIONE
                            && sedeEntita.getTipoSede().getTipoSede() != TipoSede.SERVIZIO) {
                        sediResult.add(sedeEntita);
                    }
                    break;
                case SOLO_SEDI_SPEDIZIONE_SERVIZI:
                    if (sedeEntita.getTipoSede().getTipoSede() == TipoSede.INDIRIZZO_SPEDIZIONE
                            || sedeEntita.getTipoSede().getTipoSede() == TipoSede.SERVIZIO) {
                        sediResult.add(sedeEntita);
                    }
                    break;
                default:
                    sediResult.add(sedeEntita);
                    break;
                }
            }
        }

        logger.debug("--> Exit caricaSediEntita");
        return sediResult;
    }

    @Override
    public Set<SedeEntita> caricaSediEntitaAssociate(VettoreLite vettore) {
        logger.debug("--> Enter caricaSediAssociate");

        StringBuilder sb = new StringBuilder();

        sb.append(
                "select s from SedeEntita s join fetch s.entita e join fetch e.anagrafica ana join fetch s.sede sedeAnag left join fetch s.agente ag where s.abilitato=true and s.vettore.id=:vettore");
        Query hql = panjeaDAO.prepareQuery(sb.toString());
        hql.setParameter("vettore", vettore.getId());
        @SuppressWarnings("unchecked")
        List<SedeEntita> sedi = hql.getResultList();

        Set<SedeEntita> sediResult = new HashSet<SedeEntita>();
        sediResult.addAll(sedi);
        logger.debug("--> Exit caricaSediAssociate");
        return sediResult;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<SedeEntita> caricaSediEntitaNonEreditaDatiComerciali(Integer idEntita)
            throws AnagraficaServiceException {
        logger.debug("--> Enter caricaSediEntita");
        javax.persistence.Query query = panjeaDAO
                .prepareNamedQuery("SedeEntita.caricaSediEntitaNonEreditaDatiComerciali");
        query.setParameter("paramIdEntita", idEntita);
        List<SedeEntita> sediEntita;
        try {
            sediEntita = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            logger.error("--> errore, impossibile recuperare le sedi entita di entita " + idEntita, e);
            throw new it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException(e);
        }
        logger.debug("--> Exit caricaSediEntita");
        return sediEntita;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<it.eurotn.panjea.anagrafica.domain.SedeEntita> caricaSediSecondarieEntita(Entita entita)
            throws it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException {
        logger.debug("--> Enter caricaSediSecondarieEntita");
        List<SedeEntita> sediEntita = new ArrayList<SedeEntita>();

        try {
            Session hibernateSession = (Session) panjeaDAO.getEntityManager().getDelegate();
            Criteria crit = hibernateSession.createCriteria(SedeEntita.class);
            sediEntita = crit.add(Restrictions.eq(SedeEntita.PROP_ENTITA + ".id", entita.getId()))
                    .createAlias(SedeEntita.PROP_TIPO_SEDE, "ts").add(Restrictions.eq("ts.sedePrincipale", false))
                    .list();
        } catch (HibernateException e) {
            logger.error("--> Errore durante il caricamento delle sedi dell'entit� " + entita.getId(), e);
            throw new it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException(e);
        }

        logger.debug("--> Exit caricaSediSecondarieEntita");
        return sediEntita;
    }

    @Override
    public SedeEntita creaSedeEntitaGenerica(Integer idEntita) throws TipoSedeEntitaNonTrovataException {
        logger.debug("--> Enter creaSedeEntitaGenerica");
        SedeAnagrafica sedeAnagrafica = new SedeAnagrafica();
        sedeAnagrafica.setAbilitato(true);

        Entita entita = null;
        try {
            entita = panjeaDAO.load(Entita.class, idEntita);
            Hibernate.initialize(entita.getAnagrafica());
        } catch (ObjectNotFoundException e1) {
            throw new RuntimeException(e1);
        }
        SedeEntita sedeEntita = new SedeEntita();
        sedeEntita.setAbilitato(true);
        sedeEntita.setEntita(entita);
        sedeEntita.setSede(sedeAnagrafica);
        sedeEntita.setEreditaDatiCommerciali(true);
        sedeEntita.setEreditaRapportiBancari(true);
        // ricerca e inizializza sedeEntita con il tipo sedeentita principale
        TipoSedeEntita tipoSedeEntita;
        try {
            tipoSedeEntita = anagraficheTipologieManager.caricaTipoSedeEntitaByTipoSede(TipoSede.DA_DOCUMENTO);
        } catch (TipoSedeEntitaNonTrovataException e) {
            throw e;
        }
        sedeEntita.setTipoSede(tipoSedeEntita);
        logger.debug("--> Exit creaSedeEntitaGenerica");
        return sedeEntita;
    }

    @Override
    public void generaSedePrincipaleInEntita(Entita entita) {
        logger.debug("--> Enter generaSedePrincipaleInEntita");
        // genera una SedeEntita per Entita e la definisce come sede principale
        SedeEntita sedeEntita = new SedeEntita();
        sedeEntita.setAbilitato(true);
        sedeEntita.setEntita(entita);
        entita.getAnagrafica().getSedeAnagrafica().setDescrizione("Sede principale");
        sedeEntita.setSede(entita.getAnagrafica().getSedeAnagrafica());
        // ricerca e inizializza sedeEntita con il tipo sedeentita principale
        TipoSedeEntita tipoSedeEntita;
        try {
            tipoSedeEntita = anagraficheTipologieManager.caricaTipoSedeEntitaPrincipale();
        } catch (AnagraficaServiceException e) {
            logger.error("--> errore, impossibile determinare TipoSedeEntita principale ", e);
            throw new RuntimeException(e);
        }
        sedeEntita.setTipoSede(tipoSedeEntita);
        // inizializzo i dati dell'embedded blocco sede
        sedeEntita.getBloccoSede();
        try {
            sedeEntita = salvaSedeEntita(sedeEntita);
        } catch (AnagraficaServiceException e) {
            logger.error("--> errore, impossibile salvare la nuova SedeEntita principale ", e);
            throw new RuntimeException(e);
        } catch (SedeEntitaPrincipaleAlreadyExistException e) {
            logger.error("--> errore, impossibile salvare la nuova SedeEntita principale ", e);
            throw new RuntimeException(e);
        }

        logger.debug("--> Exit generaSedePrincipaleInEntita");
    }

    /**
     *
     * @return codiceAzienda
     */
    private String getCodiceAzienda() {
        return ((JecPrincipal) context.getCallerPrincipal()).getCodiceAzienda();
    }

    /**
     * Carica il codice disponibile per una sede data l'entità. Il codice viene calcolato trovanto quello più alto tra
     * le sedi dell'entità e aggiungendo 1.
     *
     * @param idEntita
     *            id dell'entità
     * @return codice disponibile per la sede
     */
    private Integer getNuovoCodiceSede(Integer idEntita) {
        logger.debug("--> Enter getNuovoCodiceSede");

        Integer nuovoCodice = 1;

        StringBuilder sb = new StringBuilder();
        sb.append("select max(cast(codice as SIGNED)) ");
        sb.append("from anag_sedi_entita ");
        sb.append("where entita_id = " + idEntita);

        SQLQuery query = ((Session) panjeaDAO.getEntityManager().getDelegate()).createSQLQuery(sb.toString());

        BigInteger ultimoCodice = (BigInteger) query.uniqueResult();
        if (ultimoCodice != null) {
            nuovoCodice = ultimoCodice.intValue() + 1;
        }

        logger.debug("--> Exit getNuovoCodiceSede");
        return nuovoCodice;
    }

    @Override
    public SedeEntita salvaSedeEntita(SedeEntita sedeEntita)
            throws AnagraficaServiceException, SedeEntitaPrincipaleAlreadyExistException {
        /*
         * se sedeEntita risulta sede principale va controllata l'eventuale variazione della sede anagrafica questa
         * modifica incide sulla attributo sedeAnagrafica di Anagrafica
         */
        SedeEntita sedeEntitaPrincipaleCorrente = caricaSedePrincipaleEntita(sedeEntita.getEntita());
        if ((sedeEntitaPrincipaleCorrente != null && sedeEntita.getTipoSede().isSedePrincipale()
                && !sedeEntita.equals(sedeEntitaPrincipaleCorrente))) {
            throw new SedeEntitaPrincipaleAlreadyExistException("Sede Principale gia' esistente",
                    sedeEntitaPrincipaleCorrente);
        }

        // se la sede non ha un codice setto il primo valido
        if (sedeEntita.getCodice() == null || sedeEntita.getCodice().isEmpty()) {
            sedeEntita.setCodice(getNuovoCodiceSede(sedeEntita.getEntita().getId()).toString());
        }

        SedeEntita sedeEntitaSave = null;
        try {
            logger.debug("--> entita " + sedeEntita.getEntita());
            sedeEntitaSave = panjeaDAO.save(sedeEntita);

            // Se la sede entità è settata come predefinita vado a togliere il flag alle altre.
            // Questo perchè la sede
            // prima del salvataggio poteva non esserlo.
            if (sedeEntitaSave.isPredefinita()) {
                StringBuilder sb = new StringBuilder();
                sb.append("update SedeEntita se ");
                sb.append("set se.predefinita = false ");
                sb.append("where se.entita.id = ");
                sb.append(sedeEntitaSave.getEntita().getId());
                sb.append(" and se.id != ");
                sb.append(sedeEntitaSave.getId());
                Query query = panjeaDAO.prepareQuery(sb.toString());
                query.executeUpdate();
            }

            SedeMagazzino sedeMagazzino = sediMagazzinoManager.caricaSedeMagazzinoBySedeEntita(sedeEntitaSave, true);

            if (sedeMagazzino == null || sedeMagazzino.isNew()) {
                SedeMagazzino sedeMagazzinoNuova = new SedeMagazzino();
                sedeMagazzinoNuova.setSedeEntita(sedeEntitaSave);
                sediMagazzinoManager.salvaSedeMagazzino(sedeMagazzinoNuova);
            }

            if (sedeEntitaSave.getSedeCollegata() != null) {
                sedeEntitaSave.getSedeCollegata().getId();
            }
            logger.debug("--> Salvata la sedeEntita: " + sedeEntita.getId());
            sedeEntitaPrincipaleCorrente = caricaSedePrincipaleEntita(sedeEntita.getEntita());
            if (sedeEntitaPrincipaleCorrente == null) {
                throw new IllegalArgumentException("Nessuna sede principale definita");
            }
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
        logger.debug("--> Exit salvaSedeEntita");
        return sedeEntitaSave;
    }
}
