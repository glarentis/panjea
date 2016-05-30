package it.eurotn.panjea.contabilita.interceptor;

import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.contabilita.domain.Conto;
import it.eurotn.panjea.contabilita.domain.Conto.SottotipoConto;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.manager.interfaces.PianoContiManager;
import it.eurotn.panjea.contabilita.service.exception.MovimentiContabiliPresenti;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.persistence.Query;

public class EntitaVerificaInterceptor {

	@EJB
	protected PanjeaDAO panjeaDAO;

	@EJB
	protected PianoContiManager pianoContiManager;

	/**
	 * 
	 * 
	 * @param ctx
	 *            il context
	 * @return null
	 * @throws Exception
	 *             Exception
	 */
	@AroundInvoke
	public Object verificaEntita(InvocationContext ctx) throws Exception {

		Entita entita = (Entita) ctx.getParameters()[0];

		// se l'entità non è nuova e si sta cercando di cambiare il codice
		// controllo se ci sono movimenti contabili
		SottotipoConto sottotipoConto = Conto.getSottoTipoConto(entita);

		if (sottotipoConto != null) {
			if (entita != null && entita.getId() != null && entita.getCodicePrecedente() != null
					&& entita.getCodicePrecedente().compareTo(entita.getCodice()) != 0) {

				EntitaLite entitaLite = entita.getEntitaLite();
				entitaLite.setCodice(entita.getCodicePrecedente());
				SottoConto sottoConto = pianoContiManager.caricaSottoContoPerEntita(entitaLite);

				Query query = panjeaDAO
						.prepareQuery("select count(rc.id) from RigaContabile rc where rc.conto.id=:paramSottoContoId");
				query.setParameter("paramSottoContoId", sottoConto.getId());

				Long numMovimenti = null;
				numMovimenti = (Long) panjeaDAO.getSingleResult(query);

				if (numMovimenti > 0) {
					throw new MovimentiContabiliPresenti("Esistono dei movimenti contabili per l'entità "
							+ entita.getCodicePrecedente());
				}
			}
		}
		entita = (Entita) ctx.proceed();
		return entita;
	}
}
