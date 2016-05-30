package it.eurotn.panjea.parametriricerca.domain;

import java.io.Serializable;

public class ParametriRicercaDTO implements Serializable {
	private static final long serialVersionUID = 7957373529312774883L;
	private String nome;
	private boolean isGlobal;

	/**
	 * 
	 * Costruttore.
	 * 
	 * @param nome
	 *            nome della ricerca
	 * @param isGlobal
	 *            indica se tutti gli utenti devono vedere la ricerca
	 */
	public ParametriRicercaDTO(final String nome, final boolean isGlobal) {
		super();
		this.nome = nome;
		this.isGlobal = isGlobal;
	}

	/**
	 * @return Returns the nome.
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @return Returns the isGlobal.
	 */
	public boolean isGlobal() {
		return isGlobal;
	}

}
