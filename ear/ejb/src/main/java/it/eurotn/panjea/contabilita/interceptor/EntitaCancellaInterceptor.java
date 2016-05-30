package it.eurotn.panjea.contabilita.interceptor;

import it.eurotn.panjea.anagrafica.domain.Cliente;
import it.eurotn.panjea.anagrafica.domain.ClientePotenziale;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.Fornitore;
import it.eurotn.panjea.contabilita.domain.Conto.SottotipoConto;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.manager.interfaces.PianoContiManager;

import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import org.apache.log4j.Logger;

/**
 * 
 * 
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
public class EntitaCancellaInterceptor {

	private static Logger logger = Logger.getLogger(EntitaCancellaInterceptor.class);
	/**
	 * @uml.property name="pianoContiManager"
	 * @uml.associationEnd
	 */
	@EJB
	protected PianoContiManager pianoContiManager;

	/**
	 * Cancella il conto dell'entita passata.
	 * 
	 * @param ctx
	 *            il context
	 * @return null
	 * @throws Exception
	 *             Exception
	 */
	@AroundInvoke
	public Object cancellaContoEntita(InvocationContext ctx) throws Exception {
		Entita entita = (Entita) ctx.getParameters()[0];
		if (!(entita instanceof ClientePotenziale)) {
			SottotipoConto sottotipoConto = getSottoTipoConto(entita);
			if (sottotipoConto != null) {
				SottoConto sottoConto = pianoContiManager.caricaSottoContoPerEntita(getSottoTipoConto(entita),
						entita.getCodice());
				// il sottoconto potrebbe non esistere e quindi viene restituito un sottoconto vuoto (ad esempio se non
				// ho il conto cliente e/o fornitore definito)
				if (sottoConto.getId() != null) {
					pianoContiManager.cancellaSottoConto(sottoConto);
				}
			}
		}
		Object obj = ctx.proceed();
		return obj;
	}

	/**
	 * Restituisce il sottotipoconto dell'entita.
	 * 
	 * @param entita
	 *            entita
	 * @return SottotipoConto
	 */
	private SottotipoConto getSottoTipoConto(Entita entita) {
		logger.debug("--> Enter getSottoTipoConto");
		SottotipoConto sottotipoConto = null;
		if (entita instanceof Cliente) {
			logger.debug("--> entita' di classe cliente");
			sottotipoConto = SottotipoConto.CLIENTE;
		} else if (entita instanceof Fornitore) {
			logger.debug("--> entita' di classe fornitore");
			sottotipoConto = SottotipoConto.FORNITORE;
		} else {
			// non ho un cliente o fornitore per cui cercare il rispettivo conto nel piano dei conti
			logger.warn("--> non ho una entita' valida per cui ricercare il conto");
		}
		logger.debug("--> Exit getSottoTipoConto");
		return sottotipoConto;
	}

}
