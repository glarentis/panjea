package it.eurotn.panjea.contabilita.interceptor;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.contabilita.domain.ControPartita;
import it.eurotn.panjea.contabilita.domain.StrutturaContabile;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile;
import it.eurotn.panjea.contabilita.manager.interfaces.StrutturaContabileManager;
import it.eurotn.panjea.contabilita.manager.interfaces.TipiAreaContabileManager;

import java.util.List;

import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import org.apache.log4j.Logger;
import org.jboss.annotation.IgnoreDependency;

public class TipoAreaContabileCopiaInterceptor {

	private static Logger logger = Logger.getLogger(TipoAreaContabileCopiaInterceptor.class);

	@IgnoreDependency
	@EJB
	private TipiAreaContabileManager tipiAreaContabileManager;

	@IgnoreDependency
	@EJB
	private StrutturaContabileManager strutturaContabileManager;

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
	public Object copiaTipoAreaContabile(InvocationContext ctx) throws Exception {
		logger.debug("--> Enter copiaTipoAreaContabile");

		TipoDocumento tipoDocumentoCopia = (TipoDocumento) ctx.proceed();
		TipoDocumento tipoDocumentoOriginale = (TipoDocumento) ctx.getParameters()[2];

		TipoAreaContabile tipoAreaContabileOriginale = tipiAreaContabileManager
				.caricaTipoAreaContabilePerTipoDocumento(tipoDocumentoOriginale.getId());

		if (tipoAreaContabileOriginale != null && tipoAreaContabileOriginale.getId() != null) {
			TipoAreaContabile tipoAreaContabileCopia = (TipoAreaContabile) tipoAreaContabileOriginale.clone();
			tipoAreaContabileCopia.setTipoDocumento(tipoDocumentoCopia);

			tipiAreaContabileManager.salvaTipoAreaContabile(tipoAreaContabileCopia);

			// carico le strutture contabili
			List<StrutturaContabile> strutture = strutturaContabileManager
					.caricaStrutturaContabile(tipoDocumentoOriginale);
			for (StrutturaContabile strutturaContabile : strutture) {

				StrutturaContabile strutturaContabileCopia = (StrutturaContabile) strutturaContabile.clone();
				strutturaContabileCopia.setTipoDocumento(tipoDocumentoCopia);
				strutturaContabileManager.salvaStrutturaContabile(strutturaContabileCopia);
			}

			// carico le contro partite
			List<ControPartita> controPartite = strutturaContabileManager.caricaControPartite(tipoDocumentoOriginale);
			for (ControPartita controPartita : controPartite) {

				ControPartita controPartitaCopia = (ControPartita) controPartita.clone();
				controPartitaCopia.setTipoDocumento(tipoDocumentoCopia);
				strutturaContabileManager.salvaControPartita(controPartitaCopia);
			}
		}

		logger.debug("--> Exit copiaTipoAreaContabile");
		return tipoDocumentoCopia;
	}
}
