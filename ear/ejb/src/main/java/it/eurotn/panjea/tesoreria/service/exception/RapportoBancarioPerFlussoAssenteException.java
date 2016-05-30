package it.eurotn.panjea.tesoreria.service.exception;

import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;

import java.util.List;

/**
 * 
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
public class RapportoBancarioPerFlussoAssenteException extends Exception {

	private static final long serialVersionUID = -7534253075543133446L;
	/**
	 * @uml.property name="errors"
	 */
	private List<EntitaLite> errors;

	/**
	 * Costruttore.
	 * 
	 */
	public RapportoBancarioPerFlussoAssenteException() {
		super();
	}

	/**
	 * Costruttore.
	 * 
	 * @param errors
	 *            errori
	 */
	public RapportoBancarioPerFlussoAssenteException(final List<EntitaLite> errors) {
		super();
		this.errors = errors;
	}

	/**
	 * 
	 * Costruttore.
	 * 
	 * @param message
	 *            messaggio
	 */
	public RapportoBancarioPerFlussoAssenteException(final String message) {
		super(message);
	}

	/**
	 * @return errors
	 * @uml.property name="errors"
	 */
	public List<EntitaLite> getErrors() {
		return errors;
	}

}
