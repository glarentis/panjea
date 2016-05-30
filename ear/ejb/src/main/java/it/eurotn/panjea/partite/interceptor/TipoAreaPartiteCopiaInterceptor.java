package it.eurotn.panjea.partite.interceptor;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.partite.domain.TipoAreaPartita;
import it.eurotn.panjea.partite.manager.interfaces.TipiAreaPartitaManager;

import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import org.apache.log4j.Logger;
import org.jboss.annotation.IgnoreDependency;

public class TipoAreaPartiteCopiaInterceptor {

	private static Logger logger = Logger.getLogger(TipoAreaPartiteCopiaInterceptor.class);

	@IgnoreDependency
	@EJB
	private TipiAreaPartitaManager tipiAreaPartitaManager;

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
	public Object copiaTipoAreaPartita(InvocationContext ctx) throws Exception {
		logger.debug("--> Enter copiaTipoAreaPartita");

		TipoDocumento tipoDocumentoCopia = (TipoDocumento) ctx.proceed();
		TipoDocumento tipoDocumentoOriginale = (TipoDocumento) ctx.getParameters()[2];

		TipoAreaPartita tipoAreaPartitaOriginale = tipiAreaPartitaManager
				.caricaTipoAreaPartitaPerTipoDocumento(tipoDocumentoOriginale);

		if (tipoAreaPartitaOriginale != null && tipoAreaPartitaOriginale.getId() != null) {
			TipoAreaPartita tipoAreaPartitaCopia = (TipoAreaPartita) tipoAreaPartitaOriginale.clone();
			tipoAreaPartitaCopia.setTipoDocumento(tipoDocumentoCopia);

			tipiAreaPartitaManager.salvaTipoAreaPartita(tipoAreaPartitaCopia);
		}

		logger.debug("--> Exit copiaTipoAreaPartita");
		return tipoDocumentoCopia;
	}
}
