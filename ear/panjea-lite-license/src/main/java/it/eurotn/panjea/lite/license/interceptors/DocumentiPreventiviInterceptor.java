package it.eurotn.panjea.lite.license.interceptors;

import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.sicurezza.license.exception.NumeroInserimentiSuperatiException;
import it.eurotn.security.JecPrincipal;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.persistence.Query;

public class DocumentiPreventiviInterceptor {

	public static final int NUMERO_DOCUMENTI_LITE = 20;

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

		AreaPreventivo areaPreventivo = (AreaPreventivo) ctx.getParameters()[0];
		if (areaPreventivo.isNew()) {

			Query query = panjeaDAO.prepareNamedQuery("AreaPreventivo.countAree");
			query.setParameter("paramCodiceAzienda", getCodiceAzienda());

			Long numeroDoc = new Long(0);
			numeroDoc = (Long) panjeaDAO.getSingleResult(query);

			if (numeroDoc > NUMERO_DOCUMENTI_LITE) {
				throw new NumeroInserimentiSuperatiException(AreaPreventivo.class, NUMERO_DOCUMENTI_LITE);
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
