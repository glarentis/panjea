package it.eurotn.panjea.magazzino.manager;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.magazzino.manager.articolo.statistiche.StatisticheArticoloQueryBuilder;
import it.eurotn.panjea.magazzino.manager.interfaces.StatisticaArticoloManager;
import it.eurotn.panjea.magazzino.util.StatisticaArticolo;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

@Stateless(name = "Panjea.StatisticaArticoloManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.StatisticaArticoloManager")
public class StatisticaArticoloManagerBean implements StatisticaArticoloManager {

    private static Logger logger = Logger.getLogger(StatisticaArticoloManagerBean.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @Override
    public StatisticaArticolo caricaStatisticaArticolo(Integer idArticolo, Integer anno, Integer idDeposito) {

        String query = StatisticheArticoloQueryBuilder.getSqlQuery(idArticolo, anno, idDeposito);
        Query sqlQuery = panjeaDAO.prepareSQLQuery(query, StatisticaArticolo.class, null);

        sqlQuery = StatisticheArticoloQueryBuilder.addScalar(sqlQuery);

        StatisticaArticolo statisticaArticolo = null;
        try {
            statisticaArticolo = (StatisticaArticolo) sqlQuery.getSingleResult();
        } catch (NoResultException e) {
            logger.trace("nessuna statistica per l'articolo", e);
            statisticaArticolo = new StatisticaArticolo();
        } catch (Exception e) {
            logger.error("--> errore durante il caricamento della statistica articolo", e);
            throw new GenericException("errore durante il caricamento della statistica articolo", e);
        }

        return statisticaArticolo;

    }

}
