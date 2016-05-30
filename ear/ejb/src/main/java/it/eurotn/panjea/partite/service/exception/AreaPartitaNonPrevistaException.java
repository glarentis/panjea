/**
 * 
 */
package it.eurotn.panjea.partite.service.exception;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.partite.domain.AreaPartite;

/**
 * Eccezione rilanciata se si cerca di salvare un {@link AreaPartite} per la quale il {@link TipoDocumento} <br>
 * non è prevista.<br>
 * 
 * @author adriano
 * @version 1.0, 03/set/2008
 * 
 */
public class AreaPartitaNonPrevistaException extends Exception {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param message
	 */
	public AreaPartitaNonPrevistaException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param throwable
	 */
	public AreaPartitaNonPrevistaException(String message, Throwable throwable) {
		super(message, throwable);
	}

	/**
	 * @param throwable
	 */
	public AreaPartitaNonPrevistaException(Throwable throwable) {
		super(throwable);
	}

}
