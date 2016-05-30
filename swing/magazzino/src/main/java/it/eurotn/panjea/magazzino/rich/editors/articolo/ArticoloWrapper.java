package it.eurotn.panjea.magazzino.rich.editors.articolo;

import it.eurotn.panjea.magazzino.domain.ArticoloLite;

import java.io.Serializable;

public class ArticoloWrapper implements Serializable {

	private static final long serialVersionUID = 6488168942290072075L;
	private ArticoloLite articoloOriginale;
	private ArticoloLite articolo;
	private Integer id;

	/**
	 * Costruttore.
	 */
	public ArticoloWrapper() {
		super();
	}

	/**
	 * Costruttore.
	 * 
	 * @param articoloLite
	 *            l'articolo wrappato
	 */
	public ArticoloWrapper(final ArticoloLite articoloLite) {
		super();
		setArticolo(articoloLite);
		setId(articoloLite.getId());
		this.articoloOriginale = articoloLite;
	}

	/**
	 * @return the articoloLite
	 */
	public ArticoloLite getArticolo() {
		return articolo;
	}

	/**
	 * @return the articoloOriginale
	 */
	public ArticoloLite getArticoloOriginale() {
		return articoloOriginale;
	}

	/**
	 * @return the id to get
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * 
	 * @return true se è stato cambiato l'articolo
	 */
	public boolean isArticoloChanged() {
		return articoloOriginale != null && !articoloOriginale.equals(articolo);
	}

	/**
	 * @param articolo
	 *            the articolo to set
	 */
	public void setArticolo(ArticoloLite articolo) {
		this.articolo = articolo;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

}
