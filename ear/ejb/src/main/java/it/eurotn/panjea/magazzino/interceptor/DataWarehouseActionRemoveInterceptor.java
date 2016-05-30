package it.eurotn.panjea.magazzino.interceptor;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino.StatoAreaMagazzino;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import java.util.List;

import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

/**
 * Rimuove dal datawarehouse il dato passato come parametro al metodo.<br/>
 * Per ora il parametro che si aspetta è un areaMagazzino<br/>
 * <B>NB.</B>Gli interceptor vengono configurati nell'ejb-jar.xml.
 * 
 * @author giangi
 */

public class DataWarehouseActionRemoveInterceptor {
	public static final String SQL_DELETE = "delete from dw_movimentimagazzino where areaMagazzino_id=:areaMagazzino_id";

	@EJB
	protected PanjeaDAO panjeaDAO;

	/**
	 * cancella una lista di aree Magazzino.
	 * 
	 * @param areeMagazzino
	 *            lista da cancellare
	 */
	private void deleteMovimenti(List<? extends AreaMagazzino> areeMagazzino) {
		for (AreaMagazzino areaMagazzino : areeMagazzino) {
			deleteMovimento(areaMagazzino, areaMagazzino.getStatoAreaMagazzino());
		}
	}

	/**
	 * Cancella un'area magazzino.
	 * 
	 * @param areaMagazzino
	 *            area Magazzino da cancellare
	 * @param statoPrecedente
	 *            stato dell'area prima di essere salvata (quindi prima di chiamare ctx.proceed())
	 */
	private void deleteMovimento(AreaMagazzino areaMagazzino, StatoAreaMagazzino statoPrecedente) {
		if (areaMagazzino.getTipoAreaMagazzino().isMovimentaDatawarehouse()) {
			javax.persistence.Query query = panjeaDAO.getEntityManager().createNativeQuery(SQL_DELETE);
			query.setParameter("areaMagazzino_id", areaMagazzino.getId());
			try {
				panjeaDAO.executeQuery(query);
			} catch (DAOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * PointCut.
	 * 
	 * @param ctx
	 *            contesto della chiamata
	 * @return return per il metodo
	 * @throws Exception
	 *             eventuale exception
	 */
	@SuppressWarnings("unchecked")
	@AroundInvoke
	public Object execute(InvocationContext ctx) throws Exception {
		Object result = ctx.proceed();

		for (Object parametro : ctx.getParameters()) {
			if (parametro != null && parametro instanceof AreaMagazzino) {
				deleteMovimento((AreaMagazzino) parametro, StatoAreaMagazzino.CONFERMATO);
			} else if (parametro != null && parametro instanceof List) {
				deleteMovimenti((List<AreaMagazzino>) parametro);
			}
		}
		return result;
	}

}
