package it.eurotn.panjea.lite.license.interceptors;

import it.eurotn.panjea.magazzino.domain.Listino;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.sicurezza.license.exception.NumeroInserimentiSuperatiException;
import it.eurotn.security.JecPrincipal;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.persistence.Query;

public class ListiniInterceptor {

	public static final int NUMERO_LISTINI_LITE = 2;

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

		Listino listino = (Listino) ctx.getParameters()[0];
		if (listino.isNew()) {

			Query query = panjeaDAO.prepareNamedQuery("Listino.countListini");
			query.setParameter("paramCodiceAzienda", getCodiceAzienda());

			Long numeroDoc = new Long(0);
			numeroDoc = (Long) panjeaDAO.getSingleResult(query);

			if (numeroDoc > NUMERO_LISTINI_LITE) {
				throw new NumeroInserimentiSuperatiException(Listino.class, NUMERO_LISTINI_LITE);
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
