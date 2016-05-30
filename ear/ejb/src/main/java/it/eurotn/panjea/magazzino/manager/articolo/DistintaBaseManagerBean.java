package it.eurotn.panjea.magazzino.manager.articolo;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.domain.FaseLavorazioneArticolo;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.Componente;
import it.eurotn.panjea.magazzino.domain.ConfigurazioneDistinta;
import it.eurotn.panjea.magazzino.domain.ConfigurazioneDistintaBase;
import it.eurotn.panjea.magazzino.exception.DistintaCircolareException;
import it.eurotn.panjea.magazzino.exception.DistintaComponenteRepeat;
import it.eurotn.panjea.magazzino.exception.DistintaComponenteSame;
import it.eurotn.panjea.magazzino.exception.FormulaAssenteException;
import it.eurotn.panjea.magazzino.manager.interfaces.articolo.DistintaBaseManager;
import it.eurotn.panjea.magazzino.util.ArticoloConfigurazioneDistinta;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.util.PanjeaEJBUtil;

@Stateless(name = "Panjea.DistintaBaseManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.DistintaBaseManager")
public class DistintaBaseManagerBean implements DistintaBaseManager {
    private static Logger logger = Logger.getLogger(DistintaBaseManagerBean.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @Resource
    private SessionContext sessionContext;

    @Override
    public Componente aggiungiComponenteAConfigurazione(ConfigurazioneDistinta configurazioneDistinta,
            Componente componentePadre, Articolo articoloDaAggiungere) throws DistintaCircolareException {
        logger.debug("--> Enter aggiungiComponenteAConfigurazione");
        if (configurazioneDistinta.isNew()) {
            throw new IllegalStateException("La configurazione deve essere salvata");
        }
        ((Session) panjeaDAO.getEntityManager().getDelegate()).enableFilter("configurazioneDistintaBase");

        Componente nuovoComponente = new Componente();
        nuovoComponente.setDistinta(componentePadre == null ? configurazioneDistinta.getDistinta().getArticoloLite()
                : componentePadre.getArticolo());
        nuovoComponente.setArticolo(articoloDaAggiungere.getArticoloLite());
        if (!(configurazioneDistinta.isConfigurazioneBase())) {
            nuovoComponente.setConfigurazioneDistinta(configurazioneDistinta);
            nuovoComponente.setComponenteConfigurazione(componentePadre);
        }
        try {
            nuovoComponente = panjeaDAO.save(nuovoComponente);
            for (FaseLavorazioneArticolo faseComponente : nuovoComponente.getArticolo().getFasiLavorazioneArticolo()) {
                FaseLavorazioneArticolo fase = new FaseLavorazioneArticolo();
                fase.setFaseLavorazione(faseComponente.getFaseLavorazione());
                fase.setDescrizione(faseComponente.getDescrizione());
                fase.setComponente(nuovoComponente);
                fase.setOrdinamento(faseComponente.getOrdinamento());
                fase.setQtaAttrezzaggio(0.0);
                panjeaDAO.save(fase);
            }
        } catch (DAOException e) {
            logger.error("-->errore nel salvare salvaComponentiConfigurazione ", e);
            throw new RuntimeException(e);
        }
        if (configurazioneDistinta.isConfigurazioneBase()) {
            if (!nuovoComponente.getDistinta().isDistinta()) {
                nuovoComponente.getDistinta().setDistinta(true);
                panjeaDAO.getEntityManager().merge(nuovoComponente.getDistinta());
            }
        } else {
            salvaComponentiConfigurazione(articoloDaAggiungere, configurazioneDistinta, nuovoComponente);
        }

        List<ArticoloLite> componentiElaborati = new ArrayList<ArticoloLite>();
        componentiElaborati.add(nuovoComponente.getDistinta());
        fillComponenti(nuovoComponente.getArticolo(), componentiElaborati, true);
        if (configurazioneDistinta.isConfigurazioneBase()) {
            nuovoComponente.getArticolo().getFasiLavorazioneArticolo().size();
            nuovoComponente = PanjeaEJBUtil.cloneObject(nuovoComponente);
            nuovoComponente.setFasiLavorazioneArticolo(nuovoComponente.getArticolo().getFasiLavorazioneArticolo());
        }
        logger.debug("--> Exit aggiungiComponenteAConfigurazione");
        return nuovoComponente;
    }

    @Override
    public Componente aggiungiComponenteAConfigurazionePerImportazione(ConfigurazioneDistinta configurazioneDistinta,
            Componente componentePadre, Articolo articoloDaAggiungere) {
        logger.debug("--> Enter aggiungiComponenteAConfigurazionePerImportazione");
        if (configurazioneDistinta.isNew()) {
            throw new IllegalStateException("La configurazione deve essere salvata");
        }
        ((Session) panjeaDAO.getEntityManager().getDelegate()).enableFilter("configurazioneDistintaBase");

        Componente nuovoComponente = new Componente();
        nuovoComponente.setDistinta(componentePadre == null ? configurazioneDistinta.getDistinta().getArticoloLite()
                : componentePadre.getArticolo());
        nuovoComponente.setArticolo(articoloDaAggiungere.getArticoloLite());
        if (!(configurazioneDistinta.isConfigurazioneBase())) {
            nuovoComponente.setConfigurazioneDistinta(configurazioneDistinta);
            nuovoComponente.setComponenteConfigurazione(componentePadre);
        }
        try {
            nuovoComponente = panjeaDAO.save(nuovoComponente);
            for (FaseLavorazioneArticolo faseComponente : nuovoComponente.getArticolo().getFasiLavorazioneArticolo()) {
                FaseLavorazioneArticolo fase = new FaseLavorazioneArticolo();
                fase.setFaseLavorazione(faseComponente.getFaseLavorazione());
                fase.setDescrizione(faseComponente.getDescrizione());
                fase.setComponente(nuovoComponente);
                fase.setOrdinamento(faseComponente.getOrdinamento());
                fase.setQtaAttrezzaggio(0.0);
                panjeaDAO.save(fase);
            }
        } catch (DAOException e) {
            logger.error("-->errore nel salvare salvaComponentiConfigurazione ", e);
            throw new RuntimeException(e);
        }
        if (configurazioneDistinta.isConfigurazioneBase()) {
            if (!nuovoComponente.getDistinta().isDistinta()) {
                nuovoComponente.getDistinta().setDistinta(true);
                panjeaDAO.getEntityManager().merge(nuovoComponente.getDistinta());
            }
        } else {
            // salvaComponentiConfigurazione(articoloDaAggiungere,
            // configurazioneDistinta, nuovoComponente);
        }

        List<ArticoloLite> componentiElaborati = new ArrayList<ArticoloLite>();
        componentiElaborati.add(nuovoComponente.getDistinta());
        try {
            fillComponenti(nuovoComponente.getArticolo(), componentiElaborati, true);
        } catch (DistintaCircolareException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (configurazioneDistinta.isConfigurazioneBase()) {
            nuovoComponente.getArticolo().getFasiLavorazioneArticolo().size();
            nuovoComponente = PanjeaEJBUtil.cloneObject(nuovoComponente);
            nuovoComponente.setFasiLavorazioneArticolo(nuovoComponente.getArticolo().getFasiLavorazioneArticolo());
        }
        logger.debug("--> Exit aggiungiComponenteAConfigurazionePerImportazione");
        return nuovoComponente;
    }

    @Override
    public Set<FaseLavorazioneArticolo> aggiungiFasiLavorazione(ConfigurazioneDistinta configurazione,
            ArticoloLite articoloLite, Set<FaseLavorazioneArticolo> fasiLavorazioni) {
        Set<FaseLavorazioneArticolo> result = new HashSet<FaseLavorazioneArticolo>();
        articoloLite = panjeaDAO.loadLazy(ArticoloLite.class, articoloLite.getId());

        for (FaseLavorazioneArticolo faseLavorazioneArticolo : fasiLavorazioni) {
            try {
                faseLavorazioneArticolo.setArticolo(articoloLite);
                if (!(configurazione.isConfigurazioneBase())) {
                    faseLavorazioneArticolo.setConfigurazioneDistinta(configurazione);
                }
                result.add(panjeaDAO.save(faseLavorazioneArticolo));
            } catch (DAOException e) {
                logger.error("-->errore nel salvare la fase di lavorazione ", e);
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    @Override
    public Set<FaseLavorazioneArticolo> aggiungiFasiLavorazione(ConfigurazioneDistinta configurazione,
            Componente componente, Set<FaseLavorazioneArticolo> fasiLavorazioni) {

        Set<FaseLavorazioneArticolo> result = new HashSet<FaseLavorazioneArticolo>();

        for (FaseLavorazioneArticolo faseLavorazioneArticolo : fasiLavorazioni) {
            try {
                faseLavorazioneArticolo.setComponente(componente);
                if (!(configurazione.isConfigurazioneBase())) {
                    faseLavorazioneArticolo.setConfigurazioneDistinta(configurazione);
                }
                result.add(panjeaDAO.save(faseLavorazioneArticolo));
            } catch (DAOException e) {
                logger.error("-->errore nel salvare la fase di lavorazione ", e);
                throw new RuntimeException(e);

            }
        }
        return result;
    }

    @Override
    public void cancellaComponentiConfigurazioneDistinta(List<Componente> componenti) {
        logger.debug("--> Enter cancellaComponenteConfigurazioneDistinta");
        for (Componente componente : componenti) {
            // se la distinta alla quale è legato il componente non ha più
            // componenti devo rimuovere il flag distinta.
            // size==1 perchè il componente devo ancora rimuoverlo.
            if (componente.getConfigurazioneComponente() == null
                    || componente.getConfigurazioneComponente().isConfigurazioneBase()) {
                ArticoloLite articolo = componente.getDistinta();
                articolo = panjeaDAO.loadLazy(ArticoloLite.class, articolo.getId());
                articolo.getComponenti().remove(componente);
            } else {
                // rimuovo le fasi del componente sulla configurazione prima di
                // cancellarlo
                // altimenti viene generata una vincoloException
                StringBuilder sb = new StringBuilder(500);
                sb.append("delete faseLavArt ");
                sb.append("from maga_fasi_lavorazione_articolo faseLavArt ");
                sb.append("inner join maga_componente c on c.id=faseLavArt.componente_id ");
                sb.append("where c.configurazioneDistinta_id=:idConfigurazione ");
                sb.append("and faseLavArt.componente_id=:idComponente ");
                SQLQuery query = panjeaDAO.prepareNativeQuery(sb.toString());
                int idConfigurazione = PanjeaEJBUtil.getLazyId(componente.getConfigurazioneDistinta());
                query.setParameter("idConfigurazione", idConfigurazione);
                query.setParameter("idComponente", componente.getId());
                try {
                    query.executeUpdate();
                } catch (Exception e) {
                    logger.error("--> errore nel cancellare le fasi lavorazione per la configurazione con id "
                            + componente.getComponenteConfigurazione().getId(), e);
                    throw new RuntimeException(
                            "--> errore nel cancellare le fasi lavorazione per la configurazione con id "
                                    + componente.getComponenteConfigurazione().getId(),
                            e);
                }
            }
            try {
                panjeaDAO.delete(componente);
            } catch (DAOException e) {
                logger.error(
                        "-->errore nel cancellare il componente dalla distinta. ID componente " + componente.getId(),
                        e);
                throw new RuntimeException(e);
            }
        }
        logger.debug("--> Exit cancellaComponenteConfigurazioneDistinta");
    }

    @Override
    public void cancellaConfigurazioneDistinta(ConfigurazioneDistinta configurazioneDistinta) {
        logger.debug("--> Enter cancellaConfigurazioneDistinta");

        SQLQuery query = panjeaDAO.prepareNativeQuery(
                "delete faseLavArt from maga_fasi_lavorazione_articolo faseLavArt inner join maga_componente c on c.id=faseLavArt.componente_id where c.configurazioneDistinta_id=:idConfigurazione");
        query.setParameter("idConfigurazione", configurazioneDistinta.getId());
        try {
            query.executeUpdate();
        } catch (Exception e) {
            logger.error("--> errore nel cancellare le fasi lavorazione per la configurazione con id "
                    + configurazioneDistinta.getId(), e);
            throw new RuntimeException("--> errore nel cancellare le fasi lavorazione per la configurazione con id "
                    + configurazioneDistinta.getId(), e);
        }
        try {
            panjeaDAO.delete(configurazioneDistinta);
        } catch (DAOException e) {
            logger.error("-->errore nel cancellare la configurazione distinta " + configurazioneDistinta, e);
            throw new RuntimeException(e);
        }
        logger.debug("--> Exit cancellaConfigurazioneDistinta");
    }

    @Override
    public void cancellaFasiLavorazioneArticolo(ConfigurazioneDistinta configurazioneDistinta,
            List<FaseLavorazioneArticolo> fasiArticoloDaCancellare) {
        logger.debug("--> Enter cancellaFasiLavorazioneArticolo");
        for (FaseLavorazioneArticolo faseLavorazioneArticolo : fasiArticoloDaCancellare) {
            if (configurazioneDistinta.isConfigurazioneBase()) {
                ArticoloLite articolo = faseLavorazioneArticolo.getArticolo();
                articolo = panjeaDAO.loadLazy(ArticoloLite.class, articolo.getId());
                articolo.getFasiLavorazioneArticolo().remove(faseLavorazioneArticolo);
            }
            try {
                panjeaDAO.delete(faseLavorazioneArticolo);
            } catch (DAOException e) {
                logger.error("-->errore nel cancellare una faseLavorazioneArticolo con id "
                        + faseLavorazioneArticolo.getId(), e);
                throw new RuntimeException(e);
            }
        }
        logger.debug("--> Exit cancellaFasiLavorazioneArticolo");
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<Integer, ArticoloLite> caricaArticoliComponenti() {
        Map<Integer, ArticoloLite> articoliMap;
        try {
            Query query = panjeaDAO.prepareQuery(
                    "select c.distinta.codice as codice,c.distinta.id as id ,c.distinta.descrizioneLinguaAziendale as descrizione from Componente c",
                    ArticoloLite.class, null);
            List<ArticoloLite> result = panjeaDAO.getResultList(query);
            query = panjeaDAO.prepareQuery(
                    "select c.articolo.codice  as codice ,c.articolo.id as id ,c.articolo.descrizioneLinguaAziendale as descrizione from Componente c",
                    ArticoloLite.class, null);
            result.addAll(panjeaDAO.getResultList(query));
            articoliMap = new HashMap<>();
            for (ArticoloLite articoloLite : result) {
                articoliMap.put(articoloLite.getId(), articoloLite);
            }
        } catch (Exception e) {
            logger.error("-->errore nel caricare gli articoli componenti", e);
            throw new RuntimeException(e);
        }
        return articoliMap;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ArticoloValorizzazioneBOM> caricaArticoliValorizzazioneBOM() {
        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        sb.append("art.id as idArticolo, ");
        sb.append("art.codice as codice, ");
        sb.append("art.descrizioneLinguaAziendale as descrizione, ");
        sb.append("art.numeroDecimaliPrezzo as numeroDecimaliPrezzo, ");
        sb.append("GROUP_CONCAT(COALESCE(ta.codice,0)) as codiciAttributo, ");
        sb.append("GROUP_CONCAT(COALESCE(att.valore,0)) as valoriAttributo ");
        sb.append("from maga_articoli art ");
        sb.append("left join maga_attributi_articoli att on att.articolo_id=art.id ");
        sb.append("left join maga_tipo_attributo ta on att.tipoAttributo_id=ta.id ");
        sb.append("where art.distinta=false ");
        sb.append("and art.id in ");
        sb.append("( ");
        sb.append("   select ");
        sb.append("   articolo_id ");
        sb.append("   from maga_componente ");
        sb.append(") ");
        sb.append("OR art.id in (select distinta_id from maga_componente) ");
        sb.append("group by art.id, ");
        sb.append("art.numeroDecimaliPrezzo, ");
        sb.append("art.codice, ");
        sb.append("art.descrizioneLinguaAziendale ");
        SQLQuery query = panjeaDAO.prepareNativeQuery(sb.toString(), ArticoloValorizzazioneBOM.class);
        query.addScalar("idArticolo", Hibernate.INTEGER);
        return query.list();
    }

    @Override
    public ArticoloConfigurazioneDistinta caricaArticoloConfigurazioneDistinta(
            ConfigurazioneDistinta configurazioneDistinta) throws DistintaCircolareException {
        Set<Componente> componenti = caricaComponenti(configurazioneDistinta);

        Set<Componente> distinte = new HashSet<>();
        if (configurazioneDistinta.isConfigurazioneBase()) {
            componenti = copyFasiArticoloSuComponente(componenti);
            distinte = caricaDistinteComponente(configurazioneDistinta.getDistinta().getId());
        } else {
            try {
                configurazioneDistinta = panjeaDAO.load(ConfigurazioneDistinta.class, configurazioneDistinta.getId());
            } catch (ObjectNotFoundException e) {
                logger.error("-->errore nel caricare la configurazione distinta ", e);
                throw new RuntimeException(e);
            }
        }
        Set<FaseLavorazioneArticolo> fasi = caricaFasiLavorazione(configurazioneDistinta, null);

        // nell'articoloLite il metodo createProxy deve impostare il
        // numerodecimaliqta
        ArticoloConfigurazioneDistinta articoloConfigurazioneDistinta = new ArticoloConfigurazioneDistinta(componenti,
                distinte, fasi, configurazioneDistinta.getDistinta().getId(),
                configurazioneDistinta.getDistinta().getCodice(), configurazioneDistinta.getDistinta().getDescrizione(),
                configurazioneDistinta.getDistinta().getNumeroDecimaliQta(), configurazioneDistinta);
        return articoloConfigurazioneDistinta;
    }

    @Override
    public Set<Componente> caricaComponenti(Articolo articolo) throws DistintaCircolareException {
        ((Session) panjeaDAO.getEntityManager().getDelegate()).enableFilter("configurazioneDistintaBase");
        ArticoloLite articoloLite = null;
        try {
            articoloLite = panjeaDAO.load(ArticoloLite.class, articolo.getId());
            List<ArticoloLite> componentiElaborati = new ArrayList<ArticoloLite>();
            componentiElaborati.add(articoloLite);
            fillComponenti(articoloLite, componentiElaborati, true);
        } catch (ObjectNotFoundException e) {
            logger.error("-->errore nel caricare l'articolo " + articolo.getId(), e);
            throw new RuntimeException(e);
        }
        return articoloLite.getComponenti();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<Componente> caricaComponenti(ConfigurazioneDistinta configurazioneDistinta)
            throws DistintaCircolareException {

        if (configurazioneDistinta.isConfigurazioneBase()) {
            return caricaComponenti(configurazioneDistinta.getDistinta());
        }

        Query query = panjeaDAO.prepareQuery(
                "select distinct c from Componente c join fetch c.articolo join fetch c.distinta left join fetch c.fasiLavorazioneArticolo where c.configurazioneDistinta=:configurazione order by c.componenteConfigurazione,c.ordinamento");
        query.setParameter("configurazione", configurazioneDistinta);

        List<Componente> componenti = query.getResultList();
        Set<Componente> componentiRoot = new LinkedHashSet<Componente>();
        Map<Integer, Componente> componentiElaborati = new HashMap<Integer, Componente>();

        // Tutto per riferimento. E' il male ma è veloce.
        for (Componente componente : componenti) {
            // Clono tutto il componente altrimenti hibernate crea una sola
            // istanza di componente.getArticolo e se ho
            // lo stesso articolo nell'albero facendo
            // componente.getArticolo().getComponenti() recupererei sempre la
            // stessa collection
            // Con un clone creo istanze nuove di componente e anche di
            // articolo, istanze che non sono in sessione
            componente = PanjeaEJBUtil.cloneObject(componente);
            componente.getArticolo().setComponenti(new LinkedHashSet<Componente>());
            if (componente.getComponenteConfigurazione() == null) {
                componentiRoot.add(componente);
            } else {
                ArticoloLite comp = componentiElaborati.get(componente.getComponenteConfigurazione().getId())
                        .getArticolo();
                if (comp.getComponenti() != null) {
                    comp.getComponenti().add(componente);
                }
            }
            componentiElaborati.put(componente.getId(), componente);
        }
        return componentiRoot;
    }

    @Override
    public ConfigurazioneDistinta caricaConfigurazioneDistinta(int idConfigurazione) {
        logger.debug("--> Enter caricaConfigurazioniDistinta");
        Query query = panjeaDAO.prepareQuery(
                "select c from ConfigurazioneDistinta c join fetch c.distinta d where c.id=:idConfigurazione");
        query.setParameter("idConfigurazione", idConfigurazione);
        ConfigurazioneDistinta result = null;
        try {
            result = (ConfigurazioneDistinta) panjeaDAO.getSingleResult(query);
        } catch (DAOException e) {
            logger.error("-->errore nel caricare la distinta  " + idConfigurazione, e);
            throw new RuntimeException(e);
        }
        logger.debug("--> Exit caricaConfigurazioniDistinta");
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ConfigurazioneDistinta> caricaConfigurazioniDistinta(ArticoloLite distinta) {
        logger.debug("--> Enter caricaConfigurazioniDistinta");
        distinta.setVersion(0);
        Query query = panjeaDAO.prepareQuery(
                "select c from ConfigurazioneDistinta c where c.distinta=:distinta and c.class=ConfigurazioneDistinta");
        query.setParameter("distinta", distinta);
        List<ConfigurazioneDistinta> result = null;
        try {
            result = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            logger.error("-->errore nel caricare le configurazioni per la distinta " + distinta.getId(), e);
            throw new RuntimeException(e);
        }
        result.add(0, new ConfigurazioneDistintaBase(distinta.creaProxyArticolo()));
        logger.debug("--> Exit caricaConfigurazioniDistinta");
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<Componente> caricaDistinteComponente(Integer idArticolo) {
        Query query = panjeaDAO.prepareQuery(
                "select c from Componente c where c.articolo.id=:paramIdArtComp and c.configurazioneDistinta is null");
        query.setParameter("paramIdArtComp", idArticolo);
        Set<Componente> results;
        try {
            results = new HashSet<Componente>(panjeaDAO.getResultList(query));
        } catch (DAOException e) {
            logger.error("-->errore nel caricare le distinte per il componente " + idArticolo, e);
            throw new RuntimeException(e);
        }
        return results;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<FaseLavorazioneArticolo> caricaFasiLavorazione(ConfigurazioneDistinta configurazioneDistinta,
            Componente componente) {
        Query query = null;
        if (configurazioneDistinta.isConfigurazioneBase()) {
            query = panjeaDAO.prepareNamedQuery("FaseLavorazioneArticolo.loadByConfigurazioneBase");
            ArticoloLite articolo = panjeaDAO.loadLazy(ArticoloLite.class,
                    configurazioneDistinta.getDistinta().getId());
            query.setParameter("articolo", articolo);
        } else {
            if (componente != null) {
                query = panjeaDAO.prepareNamedQuery("FaseLavorazioneArticolo.loadByConfigurazioneComponente");
                query.setParameter("componente", componente);
            } else {
                query = panjeaDAO.prepareNamedQuery("FaseLavorazioneArticolo.loadByConfigurazione");
                query.setParameter("configurazione", configurazioneDistinta);
            }
        }
        List<FaseLavorazioneArticolo> result = null;
        try {
            result = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            logger.error("-->errore nel caricare le fasi lavorazione per l'articolo con id "
                    + configurazioneDistinta.getDistinta().getId(), e);
            throw new RuntimeException("-->errore nel caricare le fasi lavorazione per l'articolo con id "
                    + configurazioneDistinta.getDistinta().getId(), e);
        }
        return new LinkedHashSet<FaseLavorazioneArticolo>(result);
    }

    @SuppressWarnings("unchecked")
    @Override
    public double caricaQtaAttrezzaggioComponenti(ArticoloLite distinta, ArticoloLite articolo,
            ConfigurazioneDistinta configurazioneDistinta) {
        logger.debug("--> Enter caricaQtaAttrezzaggio");
        StringBuilder sb = new StringBuilder();

        if (configurazioneDistinta != null) {
            if (articolo.equals(distinta)) {
                // per le fasi collegate alla configurazione
                sb.append("select sum(ifnull(fla.qtaAttrezzaggio,0)) as qtaAttrezzaggio ");
                sb.append("from maga_fasi_lavorazione_articolo fla ");
                sb.append("where fla.configurazioneDistinta_id=");
                sb.append(configurazioneDistinta.getId());
                sb.append(" and fla.articolo_id is null ");
                sb.append("and fla.componente_id is null ");
            } else {
                // la prima parte è per le qtaAttrezzaggio dei componenti + fase
                // collegata ad esso
                sb.append("select ");
                sb.append("ifnull(c.qtaAttrezzaggio,0)+sum(ifnull(fla.qtaAttrezzaggio,0)) as qtaAttrezzaggio ");
                sb.append("from maga_componente c ");
                sb.append(
                        "left join maga_fasi_lavorazione_articolo fla on c.configurazioneDistinta_id=fla.configurazioneDistinta_id ");
                sb.append("and c.id=fla.componente_id ");
                sb.append("where ");
                sb.append("c.articolo_id=");
                sb.append(articolo.getId());
                sb.append(" and c.configurazioneDistinta_id=");
                sb.append(configurazioneDistinta.getId());
                sb.append(" group by c.articolo_id ");
            }
        } else {
            if (articolo.equals(distinta)) {
                sb.append("select 0.0 + '0' ");
                sb.append("from maga_fasi_lavorazione_articolo ");
                sb.append("where articolo_id=");
                sb.append(articolo.getId());
                sb.append(" and componente_id is null ");
                sb.append("and configurazioneDistinta_id is null");
            } else {
                sb.append("select ");
                sb.append("ifnull(c.qtaAttrezzaggio,0)+sum(ifnull(fla.qtaAttrezzaggio,0)) ");
                sb.append("from maga_componente c ");
                sb.append("left join maga_fasi_lavorazione_articolo fla on c.articolo_id=fla.articolo_id ");
                sb.append("where ");
                sb.append("c.articolo_id=");
                sb.append(articolo.getId());
                sb.append(" and c.distinta_id=");
                sb.append(distinta.getId());
                sb.append(" and c.configurazioneDistinta_id is null ");
                sb.append("and fla.configurazioneDistinta_id is null ");
                sb.append("group by c.articolo_id ");
            }
        }

        SQLQuery query = panjeaDAO.prepareNativeQuery(sb.toString());
        List<Double> list = query.list();
        Double result = null;
        if (!list.isEmpty()) {
            result = list.get(0);
        }
        if (result == null) {
            result = 0.0;
        }
        logger.debug("--> Exit caricaQtaAttrezzaggio");
        return result;
    }

    /**
     * Verifico che per i componenti senza formula, ci sia la formula predefinita sulla categoria.
     *
     * @param idArticoli
     *            idArticoli
     */
    private void checkFormulaCategoriaAssente(Set<Integer> idArticoli) {
        StringBuilder sb = new StringBuilder(500);
        sb.append("select ");
        sb.append("count(a.id) ");
        sb.append("from maga_articoli a ");
        sb.append("inner join maga_categorie cat on cat.id=a.categoria_id ");
        sb.append("where a.id in (");
        sb.append(StringUtils.join(idArticoli.iterator(), ","));
        sb.append(") ");
        sb.append("and cat.formulaPredefinitaComponente is null;");
        SQLQuery query = panjeaDAO.prepareNativeQuery(sb.toString());
        BigInteger result = (BigInteger) query.list().get(0);
        if (result.intValue() > 0) {
            throw new FormulaAssenteException();
        }
    }

    /**
     * Copia le fasi dell' articolo sul componente, viene usato il clone del componente per evitare
     * di aggiornare l'oggetto in sessione. Sposto le fasi dell'articolo sul componente per evitare
     * di leggere l'articoloConfigurazioneDistinta base in modo diverso rispetto ad una
     * configurazione alternativa.
     *
     * @param componenti
     *            componenti
     * @return Set<Componente>
     */
    private Set<Componente> copyFasiArticoloSuComponente(Set<Componente> componenti) {
        if (componenti == null) {
            return componenti;
        }
        Set<Componente> comps = new LinkedHashSet<>();
        for (Componente componente : componenti) {
            componente = PanjeaEJBUtil.cloneObject(componente);
            Set<FaseLavorazioneArticolo> fasiLavorazioneArticolo = componente.getArticolo()
                    .getFasiLavorazioneArticolo();
            componente.setFasiLavorazioneArticolo(fasiLavorazioneArticolo);
            Set<Componente> componentiComponente = componente.getArticolo().getComponenti();
            componentiComponente = copyFasiArticoloSuComponente(componentiComponente);
            componente.getArticolo().setComponenti(componentiComponente);
            comps.add(componente);
        }
        return comps;
    }

    private void fillComponenti(ArticoloLite articolo, List<ArticoloLite> componentiElaborati,
            boolean rollbackOnCycleReference) throws DistintaCircolareException {
        for (Componente componente : articolo.getComponenti()) {
            if (componentiElaborati.contains(componente.getArticolo())) {
                if (rollbackOnCycleReference) {
                    sessionContext.setRollbackOnly();
                }
                componentiElaborati.add(componente.getArticolo());
                throw new DistintaCircolareException(articolo, componentiElaborati);
            }
            ArrayList<ArticoloLite> componenti = new ArrayList<ArticoloLite>(componentiElaborati);
            componente.getFasiLavorazioneArticolo().size();
            componente.getArticolo().getFasiLavorazioneArticolo().size();
            componenti.add(componente.getArticolo());
            fillComponenti(componente.getArticolo(), componenti, rollbackOnCycleReference);
        }
    }

    @Override
    public void rimuoviReferenzaCircolare(ArticoloLite articolo) {
        boolean ok = false;
        int repeat = 0;
        List<ArticoloLite> componentiElaborati = new ArrayList<ArticoloLite>();
        componentiElaborati.add(articolo);
        while (!ok && repeat < 10) {
            try {
                repeat++;
                fillComponenti(articolo, componentiElaborati, false);
                ok = true;
            } catch (DistintaCircolareException e) {
                // Devo togliere dall'ultimo articolo il primo articolo inserito
                // come componente
                ArticoloLite articoloCircolare = e.getComponentiElaborati().get(e.getComponentiElaborati().size() - 2);
                ArticoloLite componenteDaRimuovere = e.getComponentiElaborati()
                        .get(e.getComponentiElaborati().size() - 1);
                Iterator<Componente> componentiIterator = articoloCircolare.getComponenti().iterator();
                while (componentiIterator.hasNext()) {
                    Componente componente = componentiIterator.next();
                    if (componente.getArticolo().getId().equals(componenteDaRimuovere.getId())) {
                        try {
                            panjeaDAO.delete(componente);
                        } catch (DAOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    @Override
    public Componente salvaComponente(Componente componente) {
        try {
            if (componente.getConfigurazioneComponente() == null
                    || componente.getConfigurazioneComponente().isConfigurazioneBase()) {
                componente.setConfigurazioneComponente(null);
            }
            componente = panjeaDAO.save(componente);
        } catch (DAOException e) {
            logger.error("--> errore nel salvare salvaComponentiConfigurazione ", e);
            throw new RuntimeException(e);
        }
        return componente;
    }

    /**
     * Salva ricorsivamente i componenti dell'articolo distinta per la nuova configurazione.
     *
     * @param distinta
     *            distinta
     * @param configurazioneDistinta
     *            configurazioneDistinta
     * @param componentePadre
     *            componentePadre
     */
    private void salvaComponentiConfigurazione(Articolo distinta, ConfigurazioneDistinta configurazioneDistinta,
            Componente componentePadre) {
        // esplodo la configurazione base della distinta e la salvo

        try {
            distinta = panjeaDAO.load(Articolo.class, distinta.getId());
        } catch (ObjectNotFoundException e1) {
            logger.error("-->articolo non trovato. id = " + distinta.getId(), e1);
            throw new RuntimeException("-->articolo non trovato. id = " + distinta.getId(), e1);
        }
        if (!distinta.isDistinta()) {
            return;
        }
        for (Componente componenteConfigurazioneBase : distinta.getComponenti()) {
            Componente componenteNuovaConfigurazione = new Componente();
            PanjeaEJBUtil.copyProperties(componenteNuovaConfigurazione, componenteConfigurazioneBase);
            componenteNuovaConfigurazione.setId(null);
            componenteNuovaConfigurazione.setVersion(null);
            componenteNuovaConfigurazione.setDistinta(distinta.getArticoloLite());
            componenteNuovaConfigurazione.setConfigurazioneDistinta(configurazioneDistinta);
            componenteNuovaConfigurazione.setComponenteConfigurazione(componentePadre);
            try {
                componenteNuovaConfigurazione = panjeaDAO.save(componenteNuovaConfigurazione);
                for (FaseLavorazioneArticolo faseComponente : componenteConfigurazioneBase.getArticolo()
                        .getFasiLavorazioneArticolo()) {
                    FaseLavorazioneArticolo fase = new FaseLavorazioneArticolo();
                    fase.setFaseLavorazione(faseComponente.getFaseLavorazione());
                    fase.setDescrizione(faseComponente.getDescrizione());
                    fase.setComponente(componenteNuovaConfigurazione);
                    fase.setOrdinamento(faseComponente.getOrdinamento());
                    fase.setQtaAttrezzaggio(0.0);
                    panjeaDAO.save(fase);
                }
            } catch (DAOException e) {
                logger.error("--> errore nel salvare le fasi per i componenti", e);
                throw new RuntimeException(e);
            }
            salvaComponentiConfigurazione(componenteConfigurazioneBase.getArticolo().creaProxyArticolo(),
                    configurazioneDistinta, componenteNuovaConfigurazione);
        }
    }

    @Override
    public ConfigurazioneDistinta salvaConfigurazioneDistinta(ConfigurazioneDistinta configurazioneDistinta) {
        logger.debug("--> Enter salvaConfigurazioneDistinta");
        ((Session) panjeaDAO.getEntityManager().getDelegate()).enableFilter("configurazioneDistintaBase");
        boolean isNew = configurazioneDistinta.isNew();
        try {
            configurazioneDistinta = panjeaDAO.save(configurazioneDistinta);
            if (isNew) {
                salvaComponentiConfigurazione(configurazioneDistinta.getDistinta(), configurazioneDistinta, null);
                salvaFasiLavorazioneConfigurazione(configurazioneDistinta.getDistinta(), configurazioneDistinta);
            }
        } catch (DAOException e) {
            logger.error("-->errore nel salvare la configurazione distinta", e);
            e.printStackTrace();
        }

        logger.debug("--> Exit salvaConfigurazioneDistinta");
        return configurazioneDistinta;
    }

    @Override
    public void salvaDistintaArticolo(ArticoloLite articoloLite, Set<Componente> distinte, Set<Componente> componenti,
            Set<FaseLavorazioneArticolo> fasiLavorazioni) throws DistintaCircolareException {
        logger.debug("--> Enter salvaComponentiArticolo");

        Set<Integer> idArticoliSenzaFormula = new HashSet<>();

        // Verifico che tra i componenti non ci sia l'articolo stesso.
        // e che non ci siano due componenti uguali
        Set<ArticoloLite> checkDuplicate = new HashSet<ArticoloLite>();
        for (Componente componenteCorrente : componenti) {
            if (componenteCorrente.getFormula().isEmpty()) {
                idArticoliSenzaFormula.add(componenteCorrente.getArticolo().getId());
            }
            if (componenteCorrente.getArticolo().equals(articoloLite)) {
                throw new DistintaComponenteSame();
            }
            if (!checkDuplicate.add(componenteCorrente.getArticolo())) {
                throw new DistintaComponenteRepeat(componenteCorrente.getArticolo());
            }
        }

        // verifico che tra le distinte non ci sia l'articolo stesso
        checkDuplicate = new HashSet<ArticoloLite>();
        for (Componente distintaCorrente : distinte) {
            if (distintaCorrente.getFormula().isEmpty()) {
                idArticoliSenzaFormula.add(distintaCorrente.getArticolo().getId());
            }
            if (distintaCorrente.getDistinta().equals(articoloLite)) {
                throw new DistintaComponenteSame();
            }
            if (!checkDuplicate.add(distintaCorrente.getDistinta())) {
                throw new DistintaComponenteRepeat(distintaCorrente.getDistinta());
            }
        }

        // verifico che per i componenti senza formula, ci sia la formula
        // predefinita sulla categoria
        if (idArticoliSenzaFormula.size() > 0) {
            checkFormulaCategoriaAssente(idArticoliSenzaFormula);
        }

        ((Session) panjeaDAO.getEntityManager().getDelegate()).enableFilter("configurazioneDistintaBase");
        try {
            articoloLite = panjeaDAO.load(ArticoloLite.class, articoloLite.getId());
        } catch (ObjectNotFoundException e1) {
            throw new RuntimeException(e1);
        }

        Set<Componente> componentiCorrenti = articoloLite.getComponenti();

        // Cancello i componenti che sono stati eliminati
        for (Componente componenteCorrente : componentiCorrenti) {
            if (!componenti.contains(componenteCorrente)) {
                try {
                    panjeaDAO.delete(componenteCorrente);
                } catch (DAOException e) {
                    logger.error("-->errore nel cancellare il componente " + componenteCorrente.getId(), e);
                    throw new RuntimeException("-->errore nel cancellare il componente " + componenteCorrente.getId(),
                            e);
                }
            }
        }
        // Aggiungo o modifico i componenti.
        componentiCorrenti = new TreeSet<Componente>();
        for (Componente componente : componenti) {
            componente.setDistinta(articoloLite);
            try {
                componente = panjeaDAO.save(componente);
                componentiCorrenti.add(componente);
            } catch (DAOException e) {
                logger.error("-->errore nel salvare il componente " + componente, e);
                throw new RuntimeException("-->errore nel salvare il componente " + componente, e);
            }
        }

        // Gestisco il flag distinta
        articoloLite.setComponenti(componentiCorrenti);
        articoloLite.setDistinta(componenti.size() > 0);
        try {
            panjeaDAO.save(articoloLite);
            panjeaDAO.getEntityManager().flush();
        } catch (DAOException e) {
            logger.error("-->errore nel salvare il flag distinta dell'articolo " + articoloLite.getId(), e);
            throw new RuntimeException("-->errore nel salvare il flag distinta dell'articolo " + articoloLite.getId(),
                    e);
        }

        // Cancello le distinte
        Set<Componente> distinteCorrenti = caricaDistinteComponente(articoloLite.getId());
        Query queryComponenti = panjeaDAO.prepareNamedQuery("Articolo.numeroComponenti");
        for (Componente componenteDistinta : distinteCorrenti) {
            if (!distinte.contains(componenteDistinta)) {
                try {
                    panjeaDAO.delete(componenteDistinta);
                    queryComponenti.setParameter("articolo", componenteDistinta.getDistinta());
                    long numComponenti = (Long) panjeaDAO.getSingleResult(queryComponenti);
                    if (numComponenti == 0) {
                        ArticoloLite distinta = panjeaDAO.load(ArticoloLite.class,
                                componenteDistinta.getDistinta().getId());
                        distinta.setDistinta(false);
                        panjeaDAO.save(distinta);
                    }
                } catch (DAOException e) {
                    logger.error("-->errore nel cancellare il componente distinta " + componenteDistinta.getId(), e);
                    throw new RuntimeException(
                            "-->errore nel cancellare il componente distinta " + componenteDistinta.getId(), e);
                }
            }
        }
        for (Componente componente : distinte) {
            componente.setArticolo(articoloLite);
            try {
                panjeaDAO.saveWithoutFlush(componente);
                // Se mi collego ad un articolo che non è distinta devo farlo
                // diventare distinta.
                if (!componente.getDistinta().isDistinta()) {
                    ArticoloLite nuovaDistinta = panjeaDAO.loadLazy(ArticoloLite.class,
                            componente.getDistinta().getId());
                    nuovaDistinta.setDistinta(true);
                    panjeaDAO.save(nuovaDistinta);
                }
            } catch (DAOException e) {
                logger.error("-->errore nel salvare il componente " + componente, e);
                throw new RuntimeException("-->errore nel salvare il componente " + componente, e);
            }
        }

        // salvo le fasi.
        // salvaFasiLavorazione(articoloLite, fasiLavorazioni);

        // ricarico i componenti in modo da verificare se ho dipendenze
        // circolari
        List<ArticoloLite> componentiElaborati = new ArrayList<ArticoloLite>();
        componentiElaborati.add(articoloLite);
        panjeaDAO.getEntityManager().flush();
        fillComponenti(articoloLite, componentiElaborati, true);
        logger.debug("--> Exit salvaComponentiArticolo");
    }

    @Override
    public FaseLavorazioneArticolo salvaFaseLavorazioneArticolo(FaseLavorazioneArticolo faseLavorazioneArticolo) {
        try {
            faseLavorazioneArticolo = panjeaDAO.save(faseLavorazioneArticolo);
        } catch (DAOException e) {
            logger.error("--> errore nel salvare la faseLavorazioneArticolo", e);
            throw new RuntimeException(e);
        }
        return faseLavorazioneArticolo;
    }

    private void salvaFasiLavorazioneConfigurazione(Articolo distinta, ConfigurazioneDistinta configurazioneDistinta) {
        try {
            for (FaseLavorazioneArticolo faseLavorazioneDistinta : distinta.getFasiLavorazioneArticolo()) {
                FaseLavorazioneArticolo fase = new FaseLavorazioneArticolo();
                fase.setFaseLavorazione(faseLavorazioneDistinta.getFaseLavorazione());
                fase.setConfigurazioneDistinta(configurazioneDistinta);
                fase.setOrdinamento(faseLavorazioneDistinta.getOrdinamento());
                fase.setQtaAttrezzaggio(0.0);
                panjeaDAO.save(fase);
            }
        } catch (DAOException e) {
            logger.error("--> errore nel salvare le fasi per la configurazione", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Componente sostituisciComponenteAConfigurazione(Integer idConfigurazioneDistinta, Integer idComponentePadre,
            Integer idComponenteSelezionato, Integer idArticoloSostitutivo) throws DistintaCircolareException {
        logger.debug("--> Enter sostituisciComponenteAConfigurazione");
        ArrayList<Componente> compDaSostituire = new ArrayList<>();
        Componente cs = panjeaDAO.loadLazy(Componente.class, idComponenteSelezionato);
        compDaSostituire.add(cs);
        cancellaComponentiConfigurazioneDistinta(compDaSostituire);

        ConfigurazioneDistinta configurazioneDistinta = new ConfigurazioneDistintaBase(
                cs.getDistinta().creaProxyArticolo());

        if (idConfigurazioneDistinta != null) {
            try {
                configurazioneDistinta = panjeaDAO.load(ConfigurazioneDistinta.class, idConfigurazioneDistinta);
            } catch (ObjectNotFoundException e) {
                logger.error("-->errore nel caricare la configurazione distinta con id " + idConfigurazioneDistinta, e);
                throw new RuntimeException(e);
            }
        }

        Componente compPadre = null;
        if (idComponentePadre != null) {
            compPadre = panjeaDAO.loadLazy(Componente.class, idComponentePadre);
        }

        Articolo articolo = panjeaDAO.loadLazy(Articolo.class, idArticoloSostitutivo);
        Componente nuovoComponente = aggiungiComponenteAConfigurazione(configurazioneDistinta, compPadre, articolo);
        nuovoComponente.setFormula(cs.getFormula());
        try {
            nuovoComponente = panjeaDAO.save(nuovoComponente);
        } catch (DAOException e) {
            logger.error("-->errore nel salvare il nuovo componente sostituito", e);
            throw new RuntimeException("-->errore nel salvare il nuovo componente sostituito");
        }
        logger.debug("--> Exit sostituisciComponenteAConfigurazione");
        return nuovoComponente;
    }
}
