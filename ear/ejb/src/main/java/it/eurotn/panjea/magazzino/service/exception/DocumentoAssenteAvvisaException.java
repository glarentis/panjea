/**
 * 
 */
package it.eurotn.panjea.magazzino.service.exception;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;

/**
 * Eccezione rilanciata durante il salvataggio di {@link AreaMagazzino} se il {@link Documento} non esiste <br>
 * e se l'attributo operazioneAreaContabileNonTrovata in {@link TipoAreaMagazzino} e' settata a AVVISA.
 * 
 * @author adriano
 * @version 1.0, 01/set/2008
 * 
 */
public class DocumentoAssenteAvvisaException extends Exception {

	private static final long serialVersionUID = -3535734953729005674L;

	/**
	 * Costruttore.
	 * 
	 * @param message
	 *            messaggio
	 */
	public DocumentoAssenteAvvisaException(final String message) {
		super(message);
	}

	/**
	 * Costruttore.
	 * 
	 * @param message
	 *            messaggio
	 * @param cause
	 *            eccezione
	 */
	public DocumentoAssenteAvvisaException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Costruttore.
	 * 
	 * @param cause
	 *            eccezione
	 */
	public DocumentoAssenteAvvisaException(final Throwable cause) {
		super(cause);
	}

}
