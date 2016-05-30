package it.eurotn.panjea.exporter.exception;

/**
 * Lanciata se non è possibile creare un file.<br/>
 * Nella descrizione è presente il motivo ell'errore.
 * 
 * @author giangi
 * @version 1.0, 07/ott/2011
 * 
 */
public class FileCreationException extends Exception {

	private static final long serialVersionUID = 80006072986071884L;

	/**
	 * 
	 * Costruttore.
	 * 
	 * @param message
	 *            messaggio che contiene la spiegazione dell'errore
	 */
	public FileCreationException(final String message) {
		super(message);
	}

}
