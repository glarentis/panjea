/**
 * 
 */
package it.eurotn.panjea.anagrafica.documenti.service.exception;

import it.eurotn.panjea.anagrafica.documenti.domain.IAreaDocumento;

import java.util.ArrayList;
import java.util.List;

/**
 * Exception usata per segnalare la presenza di diversi aree sotto lo stesso documento
 * 
 * @author Leonardo
 */
public class AreeCollegatePresentiException extends Exception {

	private static final long serialVersionUID = -3691660500150682742L;

	/**
	 * @uml.property name="aree"
	 */
	private List<IAreaDocumento> aree = null;

	/**
	 * 
	 */
	public AreeCollegatePresentiException() {
		super();
		init();
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public AreeCollegatePresentiException(final String message) {
		super(message);
		init();
	}

	/**
	 * @param message
	 *            the message to set
	 * @param cause
	 *            the cause to set
	 */
	public AreeCollegatePresentiException(final String message, final Throwable cause) {
		super(message, cause);
		init();
	}

	/**
	 * @param cause
	 *            the cause to set
	 */
	public AreeCollegatePresentiException(final Throwable cause) {
		super(cause);
		init();
	}

	/**
	 * @param areaDocumento
	 *            area da aggiungere
	 */
	public void addAreaCollegata(IAreaDocumento areaDocumento) {
		getAree().add(areaDocumento);
	}

	/**
	 * @return the aree
	 * @uml.property name="aree"
	 */
	public List<IAreaDocumento> getAree() {
		return aree;
	}

	private void init() {
		setAree(new ArrayList<IAreaDocumento>());
	}

	/**
	 * @param aree
	 *            the aree to set
	 * @uml.property name="aree"
	 */
	private void setAree(List<IAreaDocumento> aree) {
		this.aree = aree;
	}

}
