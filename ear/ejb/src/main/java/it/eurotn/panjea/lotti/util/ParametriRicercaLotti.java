/**
 * 
 */
package it.eurotn.panjea.lotti.util;

import it.eurotn.panjea.lotti.domain.Lotto;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;

import java.io.Serializable;

/**
 * @author fattazzo
 * 
 */
public class ParametriRicercaLotti implements Serializable {

	private static final long serialVersionUID = 6308513955858610274L;

	private Lotto lotto;

	private ArticoloLite articolo;

	private boolean effettuaRicerca;

	{
		effettuaRicerca = false;
	}

	/**
	 * Costruttore.
	 */
	public ParametriRicercaLotti() {
		super();
	}

	/**
	 * @return the articolo
	 */
	public ArticoloLite getArticolo() {
		return articolo;
	}

	/**
	 * @return the lotto
	 */
	public Lotto getLotto() {
		return lotto;
	}

	/**
	 * @return the effettuaRicerca
	 */
	public boolean isEffettuaRicerca() {
		return effettuaRicerca;
	}

	/**
	 * @param articolo
	 *            the articolo to set
	 */
	public void setArticolo(ArticoloLite articolo) {
		this.articolo = articolo;
	}

	/**
	 * @param effettuaRicerca
	 *            the effettuaRicerca to set
	 */
	public void setEffettuaRicerca(boolean effettuaRicerca) {
		this.effettuaRicerca = effettuaRicerca;
	}

	/**
	 * @param lotto
	 *            the lotto to set
	 */
	public void setLotto(Lotto lotto) {
		this.lotto = lotto;
	}

}
