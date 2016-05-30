package it.eurotn.panjea.auvend.domain;

import java.io.Serializable;

/**
 * Articolo di Auvend con le proprietà base<br/>
 * .
 * 
 * @author giangi
 * 
 */
public class Articolo implements Serializable, Comparable<Articolo> {
	private static final long serialVersionUID = 2227492722676983000L;
	private String codice;
	private String descrizione;

	@Override
	public int compareTo(Articolo o) {
		return this.getCodice().compareTo(o.getCodice());
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
		Articolo other = (Articolo) obj;
		if (codice == null) {
			if (other.codice != null) {
				return false;
			}
		} else if (!codice.equals(other.codice)) {
			return false;
		}
		return true;
	}

	/**
	 * @return Returns the codice.
	 */
	public String getCodice() {
		return codice;
	}

	/**
	 * @return Returns the descrizione.
	 */
	public String getDescrizione() {
		return descrizione;
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
		result = prime * result + ((codice == null) ? 0 : codice.hashCode());
		return result;
	}

	/**
	 * @param codice
	 *            The codice to set.
	 */
	public void setCodice(String codice) {
		this.codice = codice;
	}

	/**
	 * @param descrizione
	 *            The descrizione to set.
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	@Override
	public String toString() {
		return getCodice() + " - " + getDescrizione();
	}

}
