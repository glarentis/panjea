package it.eurotn.panjea.pagamenti.interceptor;

import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.pagamenti.manager.interfaces.SediPagamentoManager;

import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import org.apache.log4j.Logger;

/**
 * 
 * @author  giangi
 * @version  1.0, 10/nov/2010
 */
public class SediEntitaCancellaInterceptor {
	private static Logger logger = Logger.getLogger(SediEntitaCancellaInterceptor.class);
	/**
	 * @uml.property  name="sediPagamentoManager"
	 * @uml.associationEnd  
	 */
	@EJB
	protected SediPagamentoManager sediPagamentoManager;

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
			sediPagamentoManager.cancellaSedePagamento(sedeEntita);
		}
		Object obj = ctx.proceed();
		logger.debug("--> Exit cancellaSedeEntita");
		return obj;
	}
}
