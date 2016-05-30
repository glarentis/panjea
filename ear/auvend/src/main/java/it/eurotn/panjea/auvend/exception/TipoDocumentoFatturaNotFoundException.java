/**
 * 
 */
package it.eurotn.panjea.auvend.exception;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;

/**
 * Eccezione per gestire la mancanza del {@link TipoDocumento} per l'operazione di recupero delle fatture.
 * 
 * @author adriano
 * @version 1.0, 27/gen/2009
 * 
 */
public class TipoDocumentoFatturaNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Costruttore.
	 * 
	 * @param message
	 *            messaggio
	 */
	public TipoDocumentoFatturaNotFoundException(final String message) {
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
	public TipoDocumentoFatturaNotFoundException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Costruttore.
	 * 
	 * @param cause
	 *            causa
	 */
	public TipoDocumentoFatturaNotFoundException(final Throwable cause) {
		super(cause);
	}

}
