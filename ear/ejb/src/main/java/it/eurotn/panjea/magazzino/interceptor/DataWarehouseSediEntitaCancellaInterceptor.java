package it.eurotn.panjea.magazzino.interceptor;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.anagrafica.domain.ClientePotenziale;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
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
public class DataWarehouseSediEntitaCancellaInterceptor {

	private static Logger logger = Logger.getLogger(DataWarehouseSediEntitaCancellaInterceptor.class);

	/**
	 * @uml.property name="panjeaDAO"
	 * @uml.associationEnd
	 */
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
	public Object cancellaSedeAnagrafica(InvocationContext ctx) throws Exception {
		logger.debug("--> Enter cancellaSedeAnagrafica");
		SedeEntita sedeEntita = (SedeEntita) ctx.getParameters()[0];
		ctx.proceed();

		if (sedeEntita.getEntita() instanceof ClientePotenziale) {
			if (logger.isDebugEnabled()) {
				logger.debug("--> non cancello la sede del cliente potenziale nel DW dw_sedientita");
			}
			return null;
		}

		javax.persistence.Query query = panjeaDAO.getEntityManager().createNativeQuery(getSqlDelete());
		query.setParameter("SedeEntita_id", sedeEntita.getId());
		try {
			panjeaDAO.executeQuery(query);
		} catch (DAOException e) {
			throw new RuntimeException(e);
		}

		logger.debug("--> Exit cancellaSedeAnagrafica");
		return null;
	}

	/**
	 * 
	 * @return sql per la cancellazione
	 */
	private String getSqlDelete() {
		StringBuffer sb = new StringBuffer();
		sb.append("delete from dw_sedientita where sede_entita_id=:SedeEntita_id");
		return sb.toString();
	}
}
