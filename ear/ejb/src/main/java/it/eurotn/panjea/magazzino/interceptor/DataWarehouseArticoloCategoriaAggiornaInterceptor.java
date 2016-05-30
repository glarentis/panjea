package it.eurotn.panjea.magazzino.interceptor;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.magazzino.domain.Categoria;
import it.eurotn.panjea.magazzino.interceptor.sqlbuilder.DataWarehouseArticoliSqlBuilder;
import it.eurotn.panjea.magazzino.interceptor.sqlbuilder.DataWarehouseArticoliSqlBuilder.TipoFiltro;
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
public class DataWarehouseArticoloCategoriaAggiornaInterceptor {

	private static Logger logger = Logger.getLogger(DataWarehouseArticoloCategoriaAggiornaInterceptor.class);

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
	public Object aggiornaArticoloCategoria(InvocationContext ctx) throws Exception {
		logger.debug("--> Enter aggiornaArticoloCategoria");
		Categoria categoria = (Categoria) ctx.proceed();
		DataWarehouseArticoliSqlBuilder sqlBuilder = new DataWarehouseArticoliSqlBuilder(TipoFiltro.CATEGORIA_ID);
		javax.persistence.Query query = panjeaDAO.getEntityManager().createNativeQuery(sqlBuilder.getSqlUpdate());
		query.setParameter(DataWarehouseArticoliSqlBuilder.PARAM_DESCRIZIONE_CATEGORIA, categoria.getDescrizione());
		query.setParameter(DataWarehouseArticoliSqlBuilder.PARAM_CODICE_CATEGORIA, categoria.getCodice());
		query.setParameter(DataWarehouseArticoliSqlBuilder.PARAM_FILTRO_CATEGORIA_ID, categoria.getId());
		try {
			panjeaDAO.executeQuery(query);
		} catch (DAOException e) {
			throw new RuntimeException(e);
		}

		logger.debug("--> Exit aggiornaArticoloCategoria");
		return categoria;
	}

}
