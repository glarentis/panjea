package it.eurotn.panjea.magazzino.interceptor;

import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import org.apache.log4j.Logger;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.anagrafica.domain.ClientePotenziale;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.magazzino.interceptor.sqlbuilder.DataWarehouseSediEntitaSqlBuilder;
import it.eurotn.panjea.magazzino.interceptor.sqlbuilder.DataWarehouseSediEntitaSqlBuilder.TipoFiltro;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

/**
 *
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
public class DataWarehouseSediEntitaAggiornaInterceptor {

    private static final Logger LOGGER = Logger.getLogger(DataWarehouseSediEntitaAggiornaInterceptor.class);
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
    public Object aggiornaSedeAnagrafica(InvocationContext ctx) throws Exception {
        LOGGER.debug("--> Enter aggiornaSedeAnagrafica");
        SedeEntita sedeEntita = (SedeEntita) ctx.proceed();

        if (sedeEntita.getEntita() instanceof ClientePotenziale) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("--> non aggiorno la sede del cliente potenziale nel DW dw_sedientita");
            }
            return sedeEntita;
        }

        DataWarehouseSediEntitaSqlBuilder sqlBuilder = new DataWarehouseSediEntitaSqlBuilder(
                TipoFiltro.SEDE_ANAGRAFICA_ID);

        javax.persistence.Query query = panjeaDAO.getEntityManager().createNativeQuery(sqlBuilder.getSqlDelete());
        query.setParameter(DataWarehouseSediEntitaSqlBuilder.PARAM_FILTRO_SEDE_ANAGRAFICA_ID,
                sedeEntita.getSede().getId());
        try {
            panjeaDAO.executeQuery(query);
        } catch (DAOException e) {
            throw new GenericException("", e);
        }

        query = panjeaDAO.getEntityManager().createNativeQuery(sqlBuilder.getSql());
        query.setParameter(DataWarehouseSediEntitaSqlBuilder.PARAM_FILTRO_SEDE_ANAGRAFICA_ID,
                sedeEntita.getSede().getId());
        try {
            panjeaDAO.executeQuery(query);
        } catch (DAOException e) {
            throw new GenericException("", e);
        }

        LOGGER.debug("--> Exit aggiornaSedeAnagrafica");
        return sedeEntita;
    }

}
