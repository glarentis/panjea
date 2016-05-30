package it.eurotn.panjea.ordini.interceptor;

import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.ordini.manager.interfaces.SediOrdineManager;

import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import org.apache.log4j.Logger;
import org.jboss.annotation.IgnoreDependency;

/**
 * Interceptor che si occupa di cancellare la sedeOrdine.
 * 
 * @author leonardo
 */
public class SediEntitaCancellaInterceptor {
	
	private static Logger logger = Logger.getLogger(SediEntitaCancellaInterceptor.class);
	
	@IgnoreDependency
	@EJB
	protected SediOrdineManager sediOrdineManager;

	/**
	 * Metodo chiamato dagli interceptor ejb3 quando viene cancellata una SedeEntita.<br/>
	 * 
	 * @param ctx
	 *            InvocationContext
	 * @return null
	 * @throws Exception
	 *             mai lanciata
	 */
	@AroundInvoke
	public Object cancellaSedeEntita(InvocationContext ctx) throws Exception {
		logger.debug("--> Enter cancellaSedeEntita");
		SedeEntita sedeEntita = (SedeEntita) ctx.getParameters()[0];
		if (sedeEntita != null && sedeEntita.getId() != null) {
			sediOrdineManager.cancellaSedeOrdine(sedeEntita);
		}
		Object obj = ctx.proceed();
		logger.debug("--> Exit cancellaSedeEntita");
		return obj;
	}
}
