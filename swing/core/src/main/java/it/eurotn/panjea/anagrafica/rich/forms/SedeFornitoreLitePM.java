package it.eurotn.panjea.anagrafica.rich.forms;

import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.FornitoreLite;

public class SedeFornitoreLitePM extends Object {

	private FornitoreLite fornitore;
	private SedeEntita sedeEntita;

	/**
	 * Costruttore.
	 *
	 */
	public SedeFornitoreLitePM() {
		super();
		this.fornitore = new FornitoreLite();
		this.sedeEntita = new SedeEntita();
	}

	/**
	 * @return Returns the fornitore.
	 */
	public FornitoreLite getFornitore() {
		return fornitore;
	}

	/**
	 * @return the sedeEntita
	 */
	public SedeEntita getSedeEntita() {
		return sedeEntita;
	}

	/**
	 * @param fornitore
	 *            The fornitore to set.
	 */
	public void setFornitore(FornitoreLite fornitore) {
		this.fornitore = fornitore;
	}

	/**
	 * @param sedeEntita
	 *            the sedeEntita to set
	 */
	public void setSedeEntita(SedeEntita sedeEntita) {
		this.sedeEntita = sedeEntita;
	}
}