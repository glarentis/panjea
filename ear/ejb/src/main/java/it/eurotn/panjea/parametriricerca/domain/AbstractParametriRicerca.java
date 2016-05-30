package it.eurotn.panjea.parametriricerca.domain;

import it.eurotn.entity.EntityBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "para_ricerca")
public abstract class AbstractParametriRicerca extends EntityBase {

	private static final long serialVersionUID = -7052321829820277996L;

	private String nome;
	private boolean global;

	@Column(length = 255)
	private String nomeLayoutTabella;

	@Transient
	private boolean effettuaRicerca;

	/**
	 * @return Returns the nome.
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @return the nomeLayoutTabella
	 */
	public String getNomeLayoutTabella() {
		return nomeLayoutTabella;
	}

	/**
	 * @return Returns the effettuaRicerca.
	 */
	public boolean isEffettuaRicerca() {
		return effettuaRicerca;
	}

	/**
	 * @return Returns the global.
	 */
	public boolean isGlobal() {
		return global;
	}

	/**
	 * @param effettuaRicerca
	 *            The effettuaRicerca to set.
	 */
	public void setEffettuaRicerca(boolean effettuaRicerca) {
		this.effettuaRicerca = effettuaRicerca;
	}

	/**
	 * @param global
	 *            The global to set.
	 */
	public void setGlobal(boolean global) {
		this.global = global;
	}

	/**
	 * @param nome
	 *            The nome to set.
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * @param nomeLayoutTabella
	 *            the nomeLayoutTabella to set
	 */
	public void setNomeLayoutTabella(String nomeLayoutTabella) {
		this.nomeLayoutTabella = nomeLayoutTabella;
	}

}
