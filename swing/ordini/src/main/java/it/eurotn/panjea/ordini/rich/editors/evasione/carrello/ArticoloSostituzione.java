package it.eurotn.panjea.ordini.rich.editors.evasione.carrello;

import it.eurotn.panjea.magazzino.domain.ArticoloLite;

/**
 * Wrapper per il formModel utilizzato nella scelta dell'articolo in sostituzione.
 * 
 * @author giangi
 * @version 1.0, 05/feb/2011
 * 
 */
public class ArticoloSostituzione {

	private ArticoloLite articolo;

	/**
	 * Costruttore.
	 */
	public ArticoloSostituzione() {
		super();
		this.articolo = new ArticoloLite();
	}

	/**
	 * @return the articolo
	 */
	public ArticoloLite getArticolo() {
		return articolo;
	}

	/**
	 * @param articolo
	 *            the articolo to set
	 */
	public void setArticolo(ArticoloLite articolo) {
		this.articolo = articolo;
	}
}