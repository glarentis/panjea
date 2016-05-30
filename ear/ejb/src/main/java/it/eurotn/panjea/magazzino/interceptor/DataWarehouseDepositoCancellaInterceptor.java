package it.eurotn.panjea.magazzino.interceptor;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.magazzino.interceptor.sqlbuilder.DataWarehouseDepositiSqlBuilder;
import it.eurotn.panjea.magazzino.interceptor.sqlbuilder.DataWarehouseDepositiSqlBuilder.TipoFiltro;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import org.apache.log4j.Logger;

/**
 * 
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
public class DataWarehouseDepositoCancellaInterceptor {

	private static Logger logger = Logger.getLogger(DataWarehouseDepositoCancellaInterceptor.class);

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
	public Object cancellaDeposito(InvocationContext ctx) throws Exception {
		logger.debug("--> Enter aggiornaDeposito");
		Object result = ctx.proceed();
		DataWarehouseDepositiSqlBuilder sqlBuilder = new DataWarehouseDepositiSqlBuilder(TipoFiltro.DEPOSITO_ID);
		javax.persistence.Query query = panjeaDAO.getEntityManager().createNativeQuery(sqlBuilder.getSqlDelete());
		query.setParameter(DataWarehouseDepositiSqlBuilder.PARAM_FILTRO_DEPOSITO_ID,
				((Deposito) ctx.getParameters()[0]).getId());
		try {
			panjeaDAO.executeQuery(query);
		} catch (DAOException e) {
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit aggiornaDeposito");
		return result;
	}

}
