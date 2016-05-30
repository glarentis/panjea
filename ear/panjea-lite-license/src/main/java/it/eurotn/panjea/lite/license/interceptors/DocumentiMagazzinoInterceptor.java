package it.eurotn.panjea.lite.license.interceptors;

import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.sicurezza.license.exception.NumeroInserimentiSuperatiException;
import it.eurotn.security.JecPrincipal;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.persistence.Query;

public class DocumentiMagazzinoInterceptor {

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

		AreaMagazzino areaMagazzino = (AreaMagazzino) ctx.getParameters()[0];
		if (areaMagazzino.isNew()) {

			Query query = panjeaDAO.prepareNamedQuery("AreaMagazzino.countAree");
			query.setParameter("paramCodiceAzienda", getCodiceAzienda());

			Long numeroDoc = new Long(0);
			numeroDoc = (Long) panjeaDAO.getSingleResult(query);

			if (numeroDoc > NUMERO_DOCUMENTI_LITE) {
				throw new NumeroInserimentiSuperatiException(AreaMagazzino.class, NUMERO_DOCUMENTI_LITE);
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
