package it.eurotn.panjea.contabilita.service.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
public class ContiEntitaAssentiException extends Exception {

	private static final long serialVersionUID = -4769105688777590933L;

	private final List<ContoEntitaAssenteException> contiEntitaExceptions;

	/**
	 * Costruttore.
	 */
	public ContiEntitaAssentiException() {
		super();
		this.contiEntitaExceptions = new ArrayList<ContoEntitaAssenteException>();
	}

	/**
	 * Permette di aggiungere i risultati di eventuali exception ricevuto da più documenti.
	 * 
	 * @param contiEntitaAssentiException
	 *            contiEntitaAssentiException
	 */
	public void add(ContiEntitaAssentiException contiEntitaAssentiException) {
		contiEntitaExceptions.addAll(contiEntitaAssentiException.getContiEntitaExceptions());
	}

	/**
	 * Aggiunge una eccezione alla lista.
	 * 
	 * @param contoEntitaAssenteException
	 *            eccezione da aggiungere
	 */
	public void add(ContoEntitaAssenteException contoEntitaAssenteException) {
		this.contiEntitaExceptions.add(contoEntitaAssenteException);
	}

	/**
	 * @return the contiEntitaExceptions
	 */
	public List<ContoEntitaAssenteException> getContiEntitaExceptions() {
		return contiEntitaExceptions;
	}
}
