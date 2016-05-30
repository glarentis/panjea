package it.eurotn.panjea.mrp.util;

import java.io.Serializable;

/**
 * Classe utilizzata come chiave per articolo e deposito da usare nelle mappe.
 *
 * @author giangi
 * @version 1.0, 17/dic/2013
 */
public class ArticoloDepositoConfigurazioneKey implements Serializable {

	private static final long serialVersionUID = -5106589471812787072L;

	private int idArticolo;

	private int idDeposito;

	private Integer idConfigurazione;

	/**
	 * costruttore.
	 *
	 * @param idArticolo
	 *            articolo
	 * @param idDeposito
	 *            deposito
	 * @param idConfigurazione
	 *            condifurazione utilizzata dalla riga.
	 */
	public ArticoloDepositoConfigurazioneKey(final int idArticolo, final int idDeposito, final Integer idConfigurazione) {
		super();
		this.idArticolo = idArticolo;
		this.idDeposito = idDeposito;
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
		ArticoloDepositoConfigurazioneKey other = (ArticoloDepositoConfigurazioneKey) obj;
		if (idArticolo != other.idArticolo) {
			return false;
		}
		if (idConfigurazione == null) {
			if (other.idConfigurazione != null) {
				return false;
			}
		} else if (!idConfigurazione.equals(other.idConfigurazione)) {
			return false;
		}
		if (idDeposito != other.idDeposito) {
			return false;
		}
		return true;
	}

	/**
	 * @return Returns the idArticolo.
	 */
	public int getIdArticolo() {
		return idArticolo;
	}

	/**
	 * @return Returns the idConfigurazione.
	 */
	public Integer getIdConfigurazione() {
		return idConfigurazione;
	}

	/**
	 * @return Returns the idDeposito.
	 */
	public int getIdDeposito() {
		return idDeposito;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idArticolo;
		result = prime * result + ((idConfigurazione == null) ? 0 : idConfigurazione.hashCode());
		result = prime * result + idDeposito;
		return result;
	}

	/**
	 * @param idArticolo
	 *            The idArticolo to set.
	 */
	public void setIdArticolo(int idArticolo) {
		this.idArticolo = idArticolo;
	}

	/**
	 * @param idConfigurazione
	 *            The idConfigurazione to set.
	 */
	public void setIdConfigurazione(Integer idConfigurazione) {
		this.idConfigurazione = idConfigurazione;
	}

	/**
	 * @param idDeposito
	 *            The idDeposito to set.
	 */
	public void setIdDeposito(int idDeposito) {
		this.idDeposito = idDeposito;
	}

	@Override
	public String toString() {
		return "ArticoloDepositoConfigurazioneKey [idArticolo=" + idArticolo + ", idDeposito=" + idDeposito
				+ ", idConfigurazione=" + idConfigurazione + "]";
	}

}
