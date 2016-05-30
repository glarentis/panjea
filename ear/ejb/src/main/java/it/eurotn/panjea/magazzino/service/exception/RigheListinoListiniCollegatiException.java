/**
 * 
 */
package it.eurotn.panjea.magazzino.service.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fattazzo
 * 
 */
public class RigheListinoListiniCollegatiException extends Exception {

	private static final long serialVersionUID = -7233702332324872861L;

	private List<RigaListinoListiniCollegatiException> exceptions;

	{
		exceptions = new ArrayList<RigaListinoListiniCollegatiException>();
	}

	/**
	 * Costruttore.
	 */
	public RigheListinoListiniCollegatiException() {
		super();
	}

	/**
	 * Aggiunge una {@link RigaListinoListiniCollegatiException} alla lista delle eccezioni
	 * 
	 * @param exception
	 *            eccezione da aggiungere
	 */
	public void addException(RigaListinoListiniCollegatiException exception) {
		exceptions.add(exception);
	}

	/**
	 * @return the exceptions
	 */
	public List<RigaListinoListiniCollegatiException> getExceptions() {
		return exceptions;
	}

	/**
	 * Indica se contiene eccezioni di tipo {@link RigaListinoListiniCollegatiException}.
	 * 
	 * @return <code>true</code> se non ne contiene
	 */
	public boolean isEmpty() {
		return exceptions.isEmpty();
	}

}
