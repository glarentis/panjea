/**
 * 
 */
package it.eurotn.panjea.magazzino.service.exception;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;

/**
 * Eccezione rilanciata durante il salvataggio di {@link AreaMagazzino} se il {@link Documento} non esiste <br>
 * e se l'attributo operazioneAreaContabileNonTrovata in {@link TipoAreaMagazzino} e' settata a BLOCCA.
 * 
 * @author adriano
 * @version 1.0, 01/set/2008
 * 
 */
public class DocumentoAssenteBloccaException extends Exception {

	private static final long serialVersionUID = 6466384227134815782L;

	/**
	 * Costruttore.
	 * 
	 * @param message
	 *            messaggio
	 */
	public DocumentoAssenteBloccaException(final String message) {
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
	public DocumentoAssenteBloccaException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Costruttore.
	 * 
	 * @param cause
	 *            eccezione
	 */
	public DocumentoAssenteBloccaException(final Throwable cause) {
		super(cause);
	}

}
