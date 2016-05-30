/**
 * 
 */
package it.eurotn.panjea.magazzino.service.exception;

import it.eurotn.panjea.magazzino.domain.Listino;
import it.eurotn.panjea.magazzino.domain.RigaListino;

import java.util.List;

/**
 * @author fattazzo
 * 
 */
public class RigaListinoListiniCollegatiException extends Exception {

	private static final long serialVersionUID = 4790289032524311209L;

	private RigaListino rigaListino;
	private List<Listino> listiniCollegati;

	/**
	 * Costruttore.
	 * 
	 * @param rigaListino
	 *            riga listino di riferimento
	 * @param listiniCollegati
	 *            listini collegati alle riga listino
	 */
	public RigaListinoListiniCollegatiException(final RigaListino rigaListino, final List<Listino> listiniCollegati) {
		super();
		this.rigaListino = rigaListino;
		this.listiniCollegati = listiniCollegati;
	}

	/**
	 * @return the listiniCollegati
	 */
	public List<Listino> getListiniCollegati() {
		return listiniCollegati;
	}

	/**
	 * @return the rigaListino
	 */
	public RigaListino getRigaListino() {
		return rigaListino;
	}

}
