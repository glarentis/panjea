package it.eurotn.panjea.lite.license.interceptors;

import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.sicurezza.license.exception.NumeroInserimentiSuperatiException;
import it.eurotn.security.JecPrincipal;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.persistence.Query;

public class EntitaInterceptor {

	public static final int NUMERO_ENTITA_LITE = 20;

	@EJB
	private PanjeaDAO panjeaDAO;

	@Resource
	private SessionContext context;

	/**
	 * Eseguito durante il salvataggio di un documento di magazzino.
	 *
	 * @param ctx
	 *            context
	 * @return .
	 * @throws Exception .
	 */
	@AroundInvoke
	public Object execute(InvocationContext ctx) throws Exception {

		Entita entita = (Entita) ctx.getParameters()[0];
		if (entita.isNew()) {

			Query query = panjeaDAO.prepareNamedQuery("Entita.countEntita");
			query.setParameter("paramCodiceAzienda", getCodiceAzienda());

			Long numeroDoc = new Long(0);
			numeroDoc = (Long) panjeaDAO.getSingleResult(query);

			if (numeroDoc > NUMERO_ENTITA_LITE) {
				throw new NumeroInserimentiSuperatiException(entita.getClass(), NUMERO_ENTITA_LITE);
			}
		}

		return ctx.proceed();
	}

	/**
	 *
	 * @return codiceAzienda
	 */
	private String getCodiceAzienda() {
		return ((JecPrincipal) context.getCallerPrincipal()).getCodiceAzienda();
	}
}
