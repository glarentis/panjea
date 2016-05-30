package it.eurotn.panjea.magazzino.interceptor;

import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.magazzino.domain.DepositoMagazzino;
import it.eurotn.panjea.magazzino.manager.interfaces.DepositiMagazzinoManager;

import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import org.apache.log4j.Logger;

/**
 * Interceptor che si occupa di cancellare il deposito.
 * 
 * @author Leonardo
 */
public class DepositoAziendaCancellaInterceptor {

	private static Logger logger = Logger.getLogger(DepositoAziendaCancellaInterceptor.class);
	/**
	 * @uml.property name="depositiMagazzinoManager"
	 * @uml.associationEnd
	 */
	@EJB
	protected DepositiMagazzinoManager depositiMagazzinoManager;

	/**
	 * Metodo chiamato dagli interceptor ejb3 quando viene cancellato un DepositoAzienda.<br/>
	 * .
	 * 
	 * @param ctx
	 *            InvocationContext
	 * @return null
	 * @throws Exception
	 *             mai lanciata
	 */
	@AroundInvoke
	public Object cancellaDeposito(InvocationContext ctx) throws Exception {
		logger.debug("--> Enter cancellaSedeEntita");
		Deposito deposito = (Deposito) ctx.getParameters()[0];
		if (deposito != null && deposito.getId() != null) {
			DepositoMagazzino depositoMagazzino = depositiMagazzinoManager.caricaDepositoMagazzinoByDeposito(deposito);
			if (depositoMagazzino != null && depositoMagazzino.getId() != null) {
				depositiMagazzinoManager.cancellaDepositoMagazzino(depositoMagazzino.getId());
			}
		}
		ctx.proceed();
		logger.debug("--> Exit cancellaSedeEntita");
		return null;
	}
}
