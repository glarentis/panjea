/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.editors.listino.exception;

import it.eurotn.panjea.magazzino.domain.Listino;
import it.eurotn.panjea.magazzino.domain.RigaListino;
import it.eurotn.panjea.magazzino.service.exception.RigaListinoListiniCollegatiException;

import java.util.List;

/**
 * @author fattazzo
 * 
 */
public class RigaListinoCollegataPM {

	private RigaListino rigaListino;

	private List<Listino> listiniCollegati;

	private Boolean aggiornaListiniCollegati;

	{
		aggiornaListiniCollegati = false;
	}

	/**
	 * Costruttore.
	 */
	public RigaListinoCollegataPM() {
		super();
	}

	/**
	 * Costruttore.
	 * 
	 * @param exception
	 *            exception
	 */
	public RigaListinoCollegataPM(final RigaListinoListiniCollegatiException exception) {
		super();
		this.rigaListino = exception.getRigaListino();
		this.listiniCollegati = exception.getListiniCollegati();
	}

	/**
	 * @return the aggiornaListiniCollegati
	 */
	public Boolean getAggiornaListiniCollegati() {
		return aggiornaListiniCollegati;
	}

	/**
	 * @return the listiniCollegati
	 */
	public List<Listino> getListiniCollegati() {
		return listiniCollegati;
	}

	/**
	 * @return numero di listini collegati
	 */
	public int getNumeroListiniCollegati() {
		return this.listiniCollegati != null ? this.listiniCollegati.size() : 0;
	}

	/**
	 * @return the rigaListino
	 */
	public RigaListino getRigaListino() {
		return rigaListino;
	}

	/**
	 * @param aggiornaListiniCollegati
	 *            the aggiornaListiniCollegati to set
	 */
	public void setAggiornaListiniCollegati(Boolean aggiornaListiniCollegati) {
		this.aggiornaListiniCollegati = aggiornaListiniCollegati;
	}

}
