package it.eurotn.panjea.onroad.domain.wrapper;

import it.eurotn.panjea.magazzino.domain.Articolo;

public class ArticoloOnRoad {

	private Articolo articolo;

	private Double giacenza;

	/**
	 * Costruttore.
	 * 
	 * @param articolo
	 *            articolo
	 * @param giacenza
	 *            giacenza
	 */
	public ArticoloOnRoad(final Articolo articolo, final Double giacenza) {
		super();
		this.articolo = articolo;
		this.giacenza = giacenza;
	}

	/**
	 * @return Returns the articolo.
	 */
	public Articolo getArticolo() {
		return articolo;
	}

	/**
	 * @return Returns the giacenza.
	 */
	public Double getGiacenza() {
		return giacenza;
	}

	/**
	 * Formato del campo di input.<br>
	 * 0 = solo interi 1 = presenta i decimali
	 * 
	 * @return 0 o 1
	 */
	public String getTipoUnitaMisura() {
		return articolo.getNumeroDecimaliQta() > 0 ? "1" : "0";
	}

	/**
	 * @param articolo
	 *            The articolo to set.
	 */
	public void setArticolo(Articolo articolo) {
		this.articolo = articolo;
	}

	/**
	 * @param giacenza
	 *            The giacenza to set.
	 */
	public void setGiacenza(Double giacenza) {
		this.giacenza = giacenza;
	}

	/**
	 * @param tipoUnitaMisura
	 *            fake per beanio
	 */
	public void setTipoUnitaMisura(String tipoUnitaMisura) {
		throw new UnsupportedOperationException("metodo fake per esportare con BEANIO");
	}

}
