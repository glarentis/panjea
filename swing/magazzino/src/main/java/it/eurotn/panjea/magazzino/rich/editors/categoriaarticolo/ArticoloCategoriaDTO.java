package it.eurotn.panjea.magazzino.rich.editors.categoriaarticolo;

import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.Categoria;

import java.io.Serializable;

/**
 * 
 * Utilizzata come oggetto dell'editor dell'articolo e delle sue categorie.
 * 
 * @author giangi
 * @version 1.0, 24/ago/2010
 * 
 */
public class ArticoloCategoriaDTO implements Serializable {

	private static final long serialVersionUID = 1164013214745643234L;

	private Articolo articolo;

	private Categoria categoria;

	/**
	 * Costruttore.
	 */
	public ArticoloCategoriaDTO() {
		super();
		this.articolo = null;
		this.categoria = null;
	}

	/**
	 * 
	 * @param articolo
	 *            articolo nell'editor
	 * @param categoria
	 *            categoria nell'editor
	 */
	public ArticoloCategoriaDTO(final Articolo articolo, final Categoria categoria) {
		super();
		this.articolo = articolo;
		this.categoria = categoria;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ArticoloCategoriaDTO other = (ArticoloCategoriaDTO) obj;
		if (articolo == null) {
			if (other.articolo != null) {
				return false;
			}
		} else if (!articolo.equals(other.articolo)) {
			return false;
		}
		if (categoria == null) {
			if (other.categoria != null) {
				return false;
			}
		} else if (!categoria.equals(other.categoria)) {
			return false;
		}
		return true;
	}

	/**
	 * @return Returns the articolo.
	 */
	public Articolo getArticolo() {
		return articolo;
	}

	/**
	 * @return Returns the categoria.
	 */
	public Categoria getCategoria() {
		return categoria;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((articolo == null) ? 0 : articolo.hashCode());
		result = prime * result + ((categoria == null) ? 0 : categoria.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ArticoloCategoriaDTO [articolo=" + articolo + ", categoria=" + categoria + "]";
	}

}
