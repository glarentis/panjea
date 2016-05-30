package it.eurotn.panjea.documenti.graph.node;

import java.io.Serializable;

public abstract class AreaDocumentoNode implements Serializable {

	private static final long serialVersionUID = -1206761614881863665L;

	/**
	 * Costruttore.
	 * 
	 */
	public AreaDocumentoNode() {
		super();
	}

	/**
	 * Restituisce la descrizione HTML del nodo.
	 * 
	 * @return descrizione
	 */
	public abstract String getHTMLDescription();

	/**
	 * @return Returns the tipoArea.
	 */
	public abstract String getTipoArea();

	@Override
	public String toString() {

		return getHTMLDescription();
	}
}
