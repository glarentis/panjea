package it.eurotn.panjea.ordini.util.parametriricerca;

import it.eurotn.panjea.ordini.domain.OrdineImportato.EProvenienza;

import java.io.Serializable;

public class ParametriRicercaOrdiniImportati implements Serializable {

	private static final long serialVersionUID = 3235219885446177134L;

	private EProvenienza provenienza;

	private Integer numeroOrdine;

	/**
	 * Costruttore.
	 * 
	 */
	public ParametriRicercaOrdiniImportati() {
		super();
	}

	/**
	 * @return Returns the numeroOrdine.
	 */
	public Integer getNumeroOrdine() {
		return numeroOrdine;
	}

	/**
	 * @return the provenienza
	 */
	public EProvenienza getProvenienza() {
		return provenienza;
	}

	/**
	 * @param numeroOrdine
	 *            The numeroOrdine to set.
	 */
	public void setNumeroOrdine(Integer numeroOrdine) {
		this.numeroOrdine = numeroOrdine;
	}

	/**
	 * @param provenienza
	 *            the provenienza to set
	 */
	public void setProvenienza(EProvenienza provenienza) {
		this.provenienza = provenienza;
	}

}
