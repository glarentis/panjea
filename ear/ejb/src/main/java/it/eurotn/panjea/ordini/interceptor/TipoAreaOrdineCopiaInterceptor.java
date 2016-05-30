package it.eurotn.panjea.ordini.interceptor;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.ordini.domain.documento.TipoAreaOrdine;
import it.eurotn.panjea.ordini.manager.documento.interfaces.TipiAreaOrdineManager;
import it.eurotn.panjea.stampe.manager.interfaces.LayoutStampeManager;

import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import org.apache.log4j.Logger;
import org.jboss.annotation.IgnoreDependency;

public class TipoAreaOrdineCopiaInterceptor {

	private static Logger logger = Logger.getLogger(TipoAreaOrdineCopiaInterceptor.class);

	@IgnoreDependency
	@EJB
	private TipiAreaOrdineManager tipiAreaOrdineManager;

	@EJB
	private LayoutStampeManager layoutStampeManager;

	/**
	 * Metodo chiamato dagli interceptor ejb3 quando viene copiato un tipo documento.<br/>
	 *
	 * @param ctx
	 *            InvocationContext
	 * @return null
	 * @throws Exception
	 *             mai lanciata
	 */
	@AroundInvoke
	public Object copiaTipoAreaOrdine(InvocationContext ctx) throws Exception {
		logger.debug("--> Exit copiaTipoAreaOrdine");

		TipoDocumento tipoDocumentoCopia = (TipoDocumento) ctx.proceed();
		TipoDocumento tipoDocumentoOriginale = (TipoDocumento) ctx.getParameters()[2];

		TipoAreaOrdine tipoAreaOrdineOriginale = tipiAreaOrdineManager
				.caricaTipoAreaOrdinePerTipoDocumento(tipoDocumentoOriginale.getId());

		if (tipoAreaOrdineOriginale != null && tipoAreaOrdineOriginale.getId() != null) {
			TipoAreaOrdine tipoAreaOrdineCopia = (TipoAreaOrdine) tipoAreaOrdineOriginale.clone();
			tipoAreaOrdineCopia.setTipoDocumento(tipoDocumentoCopia);
			tipoAreaOrdineCopia = tipiAreaOrdineManager.salvaTipoAreaOrdine(tipoAreaOrdineCopia);
			layoutStampeManager.cloneLayoutStampa(tipoAreaOrdineCopia, tipoAreaOrdineOriginale);
		}

		logger.debug("--> Exit copiaTipoAreaOrdine");
		return tipoDocumentoCopia;
	}
}
