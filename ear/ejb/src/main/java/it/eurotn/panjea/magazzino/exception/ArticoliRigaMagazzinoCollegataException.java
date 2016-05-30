/**
 *
 */
package it.eurotn.panjea.magazzino.exception;

import it.eurotn.panjea.magazzino.util.ArticoloRicerca;

import java.util.HashSet;
import java.util.Set;

/**
 * @author leonardo
 * 
 */
public class ArticoliRigaMagazzinoCollegataException extends Exception {

	private static final long serialVersionUID = 5470453549426161121L;

	private Set<ArticoloRicerca> articoli = null;

	/**
	 * Costruttore.
	 */
	public ArticoliRigaMagazzinoCollegataException() {
		super();
	}

	/**
	 * @param articolo
	 *            the articolo to add
	 */
	public void addArticolo(ArticoloRicerca articolo) {
		getArticoli().add(articolo);
	}

	/**
	 * @return the articoli
	 */
	public Set<ArticoloRicerca> getArticoli() {
		if (articoli == null) {
			articoli = new HashSet<ArticoloRicerca>();
		}
		return articoli;
	}

	/**
	 * @param articoli
	 *            the articoli to set
	 */
	public void setArticoli(Set<ArticoloRicerca> articoli) {
		this.articoli = articoli;
	}

}
