/**
 * 
 */
package it.eurotn.panjea.auvend.exception;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;

/**
 * Eccezione per gestire l'assenza del {@link TipoDocumento} per i movimenti di rifornimento dei caricatori di AuVend.
 * 
 * @author adriano
 * @version 1.0, 23/dic/2008
 * 
 */
public class TipoDocumentoRifornimentoNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public TipoDocumentoRifornimentoNotFoundException() {
		super();
	}

	/**
	 * Costruttore.
	 * 
	 * @param message
	 *            messaggio
	 */
	public TipoDocumentoRifornimentoNotFoundException(final String message) {
		super(message);
	}

	/**
	 * Costruttore.
	 * 
	 * @param message
	 *            messaggio
	 * @param cause
	 *            causa
	 */
	public TipoDocumentoRifornimentoNotFoundException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Costruttore.
	 * 
	 * @param cause
	 *            causa
	 */
	public TipoDocumentoRifornimentoNotFoundException(final Throwable cause) {
		super(cause);
	}

}
