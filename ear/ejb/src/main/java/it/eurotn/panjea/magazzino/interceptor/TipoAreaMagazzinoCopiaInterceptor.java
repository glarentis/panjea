package it.eurotn.panjea.magazzino.interceptor;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.TipiAreaMagazzinoManager;
import it.eurotn.panjea.stampe.manager.interfaces.LayoutStampeManager;

import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import org.apache.log4j.Logger;
import org.jboss.annotation.IgnoreDependency;

public class TipoAreaMagazzinoCopiaInterceptor {

	private static Logger logger = Logger.getLogger(TipoAreaMagazzinoCopiaInterceptor.class);

	@IgnoreDependency
	@EJB
	private TipiAreaMagazzinoManager tipiAreaMagazzinoManager;

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
	public Object copiaTipoAreaMagazzino(InvocationContext ctx) throws Exception {
		logger.debug("--> Enter copiaTipoAreaMagazzino");

		TipoDocumento tipoDocumentoCopia = (TipoDocumento) ctx.proceed();
		TipoDocumento tipoDocumentoOriginale = (TipoDocumento) ctx.getParameters()[2];

		TipoAreaMagazzino tipoAreaMagazzinoOriginale = tipiAreaMagazzinoManager
				.caricaTipoAreaMagazzinoPerTipoDocumento(tipoDocumentoOriginale.getId());

		if (tipoAreaMagazzinoOriginale != null && tipoAreaMagazzinoOriginale.getId() != null) {
			TipoAreaMagazzino tipoAreaMagazzinoCopia = (TipoAreaMagazzino) tipoAreaMagazzinoOriginale.clone();
			tipoAreaMagazzinoCopia.setTipoDocumento(tipoDocumentoCopia);
			tipoAreaMagazzinoCopia = tipiAreaMagazzinoManager.salvaTipoAreaMagazzino(tipoAreaMagazzinoCopia);
			layoutStampeManager.cloneLayoutStampa(tipoAreaMagazzinoCopia, tipoAreaMagazzinoOriginale);
		}

		logger.debug("--> Exit copiaTipoAreaMagazzino");
		return tipoDocumentoCopia;
	}
}
