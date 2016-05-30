package it.eurotn.panjea.tesoreria.manager;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile;
import it.eurotn.panjea.contabilita.manager.interfaces.TipiAreaContabileManager;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.security.JecPrincipal;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;

import org.apache.log4j.Logger;

public abstract class AbstractAreaTesoreriaContabilitaManagerBean {

	private static Logger logger = Logger.getLogger(AbstractAreaTesoreriaContabilitaManagerBean.class);

	@EJB
	private TipiAreaContabileManager tipiAreaContabileManager;

	@Resource
	protected SessionContext context;

	/**
	 * recupera {@link JecPrincipal} dal {@link SessionContext}.
	 * 
	 * @return utente loggato
	 */
	public JecPrincipal getPrincipal() {
		logger.debug("--> Enter getJecPrincipal");
		return (JecPrincipal) context.getCallerPrincipal();
	}

	/**
	 * Carica il tipo area contabile legato al tipo documento. Se non è definita ritorna <code>null</code>.
	 * 
	 * @param tipoDocumento
	 *            tipoDocumento
	 * @return {@link TipoAreaContabile} legata al {@link TipoDocumento}
	 */
	public TipoAreaContabile getTipoAreaContabileByTipoDocumento(TipoDocumento tipoDocumento) {
		TipoAreaContabile tipoAreaContabile;

		try {
			tipoAreaContabile = tipiAreaContabileManager.caricaTipoAreaContabilePerTipoDocumento(tipoDocumento.getId());
		} catch (ContabilitaException e) {
			logger.error("--> errore ContabilitaException in creaAreaContabile", e);
			throw new RuntimeException(e);
		}
		if (tipoAreaContabile.isNew()) {
			tipoAreaContabile = null;
		}

		return tipoAreaContabile;
	}
}
