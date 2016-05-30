package it.eurotn.panjea.magazzino.interceptor;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.anagrafica.domain.SedeAzienda;
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
public class DataWarehouseDepositoSedeAggiornaInterceptor {

	private static Logger logger = Logger.getLogger(DataWarehouseDepositoSedeAggiornaInterceptor.class);

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
	public Object aggiornaDepositoSede(InvocationContext ctx) throws Exception {
		logger.debug("--> Enter aggiornaDeposito");
		SedeAzienda sedeAziendaAggiornata = (SedeAzienda) ctx.proceed();
		DataWarehouseDepositiSqlBuilder sqlBuilder = new DataWarehouseDepositiSqlBuilder(TipoFiltro.SEDE_ID);
		javax.persistence.Query query = panjeaDAO.getEntityManager().createNativeQuery(sqlBuilder.getSqlUpdate());
		query.setParameter(DataWarehouseDepositiSqlBuilder.PARAM_DESCRIZIONE_SEDE, sedeAziendaAggiornata.getSede()
				.getDescrizione());
		query.setParameter(DataWarehouseDepositiSqlBuilder.PARAM_FILTRO_SEDE_ID, sedeAziendaAggiornata.getSede()
				.getId());
		try {
			panjeaDAO.executeQuery(query);
		} catch (DAOException e) {
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit aggiornaDeposito");
		return sedeAziendaAggiornata;
	}

}
