/**
 * 
 */
package it.eurotn.panjea.contabilita.service.exception;

/**
 * Exception per segnalare all'utente che il codice iva collegato richiesto per la gestione intra/art17 non e' impostato
 * nell'anagrafica di codiceIva
 * 
 * @author Leonardo
 */
public class CodiceIvaCollegatoAssenteException extends Exception {

	private static final long serialVersionUID = -4917274091717337381L;

	public CodiceIvaCollegatoAssenteException() {
		super();
	}

	public CodiceIvaCollegatoAssenteException(String message, Throwable cause) {
		super(message, cause);
	}

	public CodiceIvaCollegatoAssenteException(String message) {
		super(message);
	}

	public CodiceIvaCollegatoAssenteException(Throwable cause) {
		super(cause);
	}

}
