package it.eurotn.panjea.contabilita.service.exception;

/**
 * 
 * Rilanciata se il documento ha delle righe contabili non valide.<br/>
 * Nel messaggio viene indicata la causa. (ad esempio Centri di costo non completi).
 * 
 * 
 * @author giangi
 * @version 1.0, 30/dic/2010
 * 
 */
public class RigheContabiliNonValidiException extends Exception {

	private static final long serialVersionUID = -384039707284164842L;

	/**
	 * 
	 * @param message
	 *            messaggio dell'eccezione
	 */
	public RigheContabiliNonValidiException(final String message) {
		super(message);
	}

}
