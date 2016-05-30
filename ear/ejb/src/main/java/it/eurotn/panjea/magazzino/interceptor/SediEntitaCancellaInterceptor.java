package it.eurotn.panjea.magazzino.interceptor;

import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.magazzino.manager.interfaces.SediMagazzinoManager;

import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import org.apache.log4j.Logger;

/**
 * Interceptor che si occupa di cancellare la sedeMagazzino.
 * 
 * @author giangi
 */
public class SediEntitaCancellaInterceptor {
	private static Logger logger = Logger.getLogger(SediEntitaCancellaInterceptor.class);
	/**
	 * @uml.property name="sediMagazzinoManager"
	 * @uml.associationEnd
	 */
	@EJB
	protected SediMagazzinoManager sediMagazzinoManager;

	/**
	 * Metodo chiamato dagli interceptor ejb3 quando viene cancellata una SedeEntita.<br/>
	 * .
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
			sediMagazzinoManager.cancellaSedeMagazzino(sedeEntita);
		}
		Object obj = ctx.proceed();
		logger.debug("--> Exit cancellaSedeEntita");
		return obj;
	}
}
