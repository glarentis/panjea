package it.eurotn.panjea.ordini.rich.editors.evasione.distintacarico.exception;

import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;

public class EntitaEvasione {

	private EntitaLite entita;

	private Boolean selezionata;

	private TipoAreaMagazzino tipoAreaEvasione;

	/**
	 * Costruttore.
	 * 
	 */
	public EntitaEvasione() {
		super();
		this.selezionata = Boolean.FALSE;
		this.tipoAreaEvasione = new TipoAreaMagazzino();
	}

	/**
	 * Costruttore.
	 * 
	 * @param entitaLite
	 *            entita
	 * 
	 */
	public EntitaEvasione(final EntitaLite entitaLite) {
		this();
		this.entita = entitaLite;
	}

	/**
	 * @return the entita
	 */
	public EntitaLite getEntita() {
		return entita;
	}

	/**
	 * @return the selezionata
	 */
	public Boolean getSelezionata() {
		return selezionata;
	}

	/**
	 * @return the tipoAreaEvasione
	 */
	public TipoAreaMagazzino getTipoAreaEvasione() {
		return tipoAreaEvasione;
	}

	/**
	 * @param selezionata
	 *            the selezionata to set
	 */
	public void setSelezionata(Boolean selezionata) {
		this.selezionata = selezionata;
	}

	/**
	 * @param tipoAreaEvasione
	 *            the tipoAreaEvasione to set
	 */
	public void setTipoAreaEvasione(TipoAreaMagazzino tipoAreaEvasione) {
		this.tipoAreaEvasione = tipoAreaEvasione;
	}

}
