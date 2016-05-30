/**
 * 
 */
package it.eurotn.panjea.magazzino.service.exception;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;

/**
 * Eccezione rilanciata durante il salvataggio di {@link AreaMagazzino} se il {@link Documento} non esiste <br>
 * e se l'attributo operazioneAreaContabileNonTrovata in {@link TipoAreaMagazzino} e' settata a NONAVVISARE
 * 
 * @author adriano
 * @version 1.0, 01/set/2008
 * 
 */
public class DocumentoAssenteNonAvvisareException extends Exception {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -7719386750259348627L;

	/**
	 * @param message
	 */
	public DocumentoAssenteNonAvvisareException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param throwable
	 */
	public DocumentoAssenteNonAvvisareException(String message, Throwable throwable) {
		super(message, throwable);
	}

	/**
	 * @param throwable
	 */
	public DocumentoAssenteNonAvvisareException(Throwable throwable) {
		super(throwable);
	}

}
