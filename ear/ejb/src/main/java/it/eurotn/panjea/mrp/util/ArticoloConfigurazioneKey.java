package it.eurotn.panjea.mrp.util;

import java.io.Serializable;

public class ArticoloConfigurazioneKey implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer idArticolo;
	private Integer idConfigurazione;

	public ArticoloConfigurazioneKey(Integer idArticolo, Integer idConfigurazione) {
		super();
		this.idArticolo = idArticolo;
		this.idConfigurazione = idConfigurazione;
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
		ArticoloConfigurazioneKey other = (ArticoloConfigurazioneKey) obj;
		if (idArticolo == null) {
			if (other.idArticolo != null) {
				return false;
			}
		} else if (!idArticolo.equals(other.idArticolo)) {
			return false;
		}
		if (idConfigurazione == null) {
			if (other.idConfigurazione != null) {
				return false;
			}
		} else if (!idConfigurazione.equals(other.idConfigurazione)) {
			return false;
		}
		return true;
	}

	/**
	 * @return Returns the idArticolo.
	 */
	public Integer getIdArticolo() {
		return idArticolo;
	}

	/**
	 * @return Returns the idConfigurazione.
	 */
	public Integer getIdConfigurazione() {
		return idConfigurazione;
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
		result = prime * result + ((idArticolo == null) ? 0 : idArticolo.hashCode());
		result = prime * result + ((idConfigurazione == null) ? 0 : idConfigurazione.hashCode());
		return result;
	}

	/**
	 * @param idArticolo
	 *            The idArticolo to set.
	 */
	public void setIdArticolo(Integer idArticolo) {
		this.idArticolo = idArticolo;
	}

	/**
	 * @param idConfigurazione
	 *            The idConfigurazione to set.
	 */
	public void setIdConfigurazione(Integer idConfigurazione) {
		this.idConfigurazione = idConfigurazione;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ArticoloConfigurazioneKey [idArticolo=" + idArticolo + ", idConfigurazione=" + idConfigurazione + "]";
	}
}
