/**
 * 
 */
package it.eurotn.panjea.magazzino.exception;

import it.eurotn.panjea.magazzino.domain.Listino;

/**
 * @author fattazzo
 * 
 */
public class ListinoManutenzioneNonValidoException extends Exception {

	private static final long serialVersionUID = -2169536498718887118L;

	private Listino listinoValido;

	/**
	 * Costruttore.
	 * 
	 * @param listinoValido
	 *            listino valido da utilizzare
	 */
	public ListinoManutenzioneNonValidoException(final Listino listinoValido) {
		super();
		this.listinoValido = listinoValido;
	}

	/**
	 * @return the listinoValido
	 */
	public Listino getListinoValido() {
		return listinoValido;
	}

}
