/**
 * 
 */
package it.eurotn.panjea.auvend.exception;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;

/**
 * Eccezione per gestire l'assenza del {@link TipoDocumento} per i movimenti di carico dei caricatori di AuVend.
 * 
 * @author adriano
 * @version 1.0, 23/dic/2008
 * 
 */
public class TipoDocumentoBaseAuvendNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public TipoDocumentoBaseAuvendNotFoundException() {
		super();
	}

	/**
	 * Costruttore.
	 * 
	 * @param message
	 *            messaggio
	 */
	public TipoDocumentoBaseAuvendNotFoundException(final String message) {
		super(message);
	}
}
