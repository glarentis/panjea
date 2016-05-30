package it.eurotn.panjea.anagrafica.domain;

import java.io.Serializable;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Entity
@Table(name = "preference_layout")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO_LAYOUT", discriminatorType = DiscriminatorType.STRING, length = 1)
@DiscriminatorValue("A")
public abstract class AbstractLayout implements Serializable {

	private static final long serialVersionUID = -8127315748821484635L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String utente;

	private String chiave;

	private boolean global;

	private String name;

	@Lob
	private String data;

	/**
	 * Se contiene il nome dell'utente indica che rappresenta l'ultimo layout
	 * caricato per l'utente. Se il layout è globale conterrà una lista di
	 * utenti separati da ",".
	 */
	private String ultimoCaricato;

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
		AbstractLayout other = (AbstractLayout) obj;
		if (chiave == null) {
			if (other.chiave != null) {
				return false;
			}
		} else if (!chiave.equals(other.chiave)) {
			return false;
		}
		if (global != other.global) {
			return false;
		}
		if (utente == null) {
			if (other.utente != null) {
				return false;
			}
		} else if (!utente.equals(other.utente)) {
			return false;
		}
		return true;
	}

	/**
	 * @return the chiave
	 * @uml.property name="chiave"
	 */
	public String getChiave() {
		return chiave;
	}

	/**
	 * @return the data
	 * @uml.property name="data"
	 */
	public String getData() {
		return data;
	}

	/**
	 * @return the id
	 * @uml.property name="id"
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @return the name
	 * @uml.property name="name"
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the ultimoCaricato
	 */
	public String getUltimoCaricato() {
		return ultimoCaricato;
	}

	/**
	 * @return the utente
	 * @uml.property name="utente"
	 */
	public String getUtente() {
		return utente;
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
		result = prime * result + ((chiave == null) ? 0 : chiave.hashCode());
		result = prime * result + (global ? 1231 : 1237);
		result = prime * result + ((utente == null) ? 0 : utente.hashCode());
		return result;
	}

	/**
	 * @return the global
	 * @uml.property name="global"
	 */
	public boolean isGlobal() {
		return global;
	}

	/**
	 * @param chiave
	 *            the chiave to set
	 * @uml.property name="chiave"
	 */
	public void setChiave(String chiave) {
		this.chiave = chiave;
	}

	/**
	 * @param data
	 *            the data to set
	 * @uml.property name="data"
	 */
	public void setData(String data) {
		this.data = data;
	}

	/**
	 * @param global
	 *            the global to set
	 * @uml.property name="global"
	 */
	public void setGlobal(boolean global) {
		this.global = global;
	}

	/**
	 * @param id
	 *            the id to set
	 * @uml.property name="id"
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @param name
	 *            the name to set
	 * @uml.property name="name"
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param ultimoCaricato
	 *            the ultimoCaricato to set
	 */
	public void setUltimoCaricato(String ultimoCaricato) {
		this.ultimoCaricato = ultimoCaricato;
	}

	/**
	 * @param utente
	 *            the utente to set
	 * @uml.property name="utente"
	 */
	public void setUtente(String utente) {
		this.utente = utente;
	}
}
