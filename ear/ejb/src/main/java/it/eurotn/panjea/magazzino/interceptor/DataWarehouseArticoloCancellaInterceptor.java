package it.eurotn.panjea.magazzino.interceptor;

import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import org.apache.log4j.Logger;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.interceptor.sqlbuilder.DataWarehouseArticoliSqlBuilder;
import it.eurotn.panjea.magazzino.interceptor.sqlbuilder.DataWarehouseArticoliSqlBuilder.TipoFiltro;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

/**
 *
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
public class DataWarehouseArticoloCancellaInterceptor {

    private static Logger logger = Logger.getLogger(DataWarehouseArticoloCancellaInterceptor.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    /**
     * 
     * @param ctx
     *            context della chiamata
     * @return risultato del metodo intecettato
     * @throws Exception
     *             eccezione generica
     */
    @AroundInvoke
    public Object cancellaArticolo(InvocationContext ctx) throws Exception {
        logger.debug("--> Enter cancellaArticolo");
        Object result = ctx.proceed();
        Integer idArticolo = null;
        if ((ctx.getParameters()[0]) instanceof Articolo) {
            idArticolo = ((Articolo) ctx.getParameters()[0]).getId();
        } else {
            idArticolo = (Integer) ctx.getParameters()[0];
        }

        DataWarehouseArticoliSqlBuilder sqlBuilder = new DataWarehouseArticoliSqlBuilder(TipoFiltro.ARTICOLO_ID);
        javax.persistence.Query query = panjeaDAO.getEntityManager().createNativeQuery(sqlBuilder.getSqlDelete());
        query.setParameter(DataWarehouseArticoliSqlBuilder.PARAM_FILTRO_ARTICOLO_ID, idArticolo);
        try {
            panjeaDAO.executeQuery(query);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }

        logger.debug("--> Exit cancellaArticolo");
        return result;
    }

}
