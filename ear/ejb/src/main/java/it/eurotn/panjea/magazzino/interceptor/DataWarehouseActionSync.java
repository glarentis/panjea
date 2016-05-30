package it.eurotn.panjea.magazzino.interceptor;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.DuplicateKeyObjectException;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.interceptor.sqlbuilder.AbstractDataWarehouseMovimentiMagazzinoSqlBuilder;

import java.util.Set;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.persistence.Query;

/**
 * Rimuove (se esistono) e inserisce le righe articolo nel datawarehouse <br>
 * dell'area magazzino passata come parametro al metodo intercettato.<br/>
 * <B>NB.</B>Gli interceptor vengono configurati nell'ejb-jar.xml.
 * 
 * @author giangi
 * 
 */
public class DataWarehouseActionSync extends AbstractDataWarehouseSync {

	/**
	 * PointCut.
	 * 
	 * @param ctx
	 *            contesto della chiamata
	 * @return return per il metodo
	 * @throws Exception
	 *             eventuale exception
	 */
	@AroundInvoke
	public Object execute(InvocationContext ctx) throws Exception {
		// chiamo il metodo per essere sicuro che non dia errore.
		// Essendo il warehouse di tipo MyIsam (per prestazioni) e quindi senza
		// transazione mi
		// assicuro che il metodo intercettato non faccia degli errori quindi lo
		// eseguo subito
		Object result = ctx.proceed();
		AreaMagazzino areaMagazzino = getParameterAreaMagazzino(ctx);
		if (movimentaDatawarehouse(areaMagazzino)) {
			Set<AbstractDataWarehouseMovimentiMagazzinoSqlBuilder> builders = AbstractDataWarehouseMovimentiMagazzinoSqlBuilder
					.createBuilder(areaMagazzino.getTipoAreaMagazzino().getTipoMovimento());
			Query queryDelete = panjeaDAO.getEntityManager().createNativeQuery(
					DataWarehouseActionRemoveInterceptor.SQL_DELETE);
			queryDelete.setParameter("areaMagazzino_id", areaMagazzino.getId());
			panjeaDAO.executeQuery(queryDelete);
			for (AbstractDataWarehouseMovimentiMagazzinoSqlBuilder builder : builders) {
				javax.persistence.Query query = panjeaDAO.getEntityManager().createNativeQuery(builder.getSql());
				query.setParameter("areaMagazzino_id", areaMagazzino.getId());
				try {
					panjeaDAO.executeQuery(query);
				} catch (DuplicateKeyObjectException dex) {
					try {
						panjeaDAO.executeQuery(query);
					} catch (DAOException e) {
						throw new RuntimeException(e);
					}
				} catch (DAOException e) {
					throw new RuntimeException(e);
				}
			}
		}
		return result;
	}

}
