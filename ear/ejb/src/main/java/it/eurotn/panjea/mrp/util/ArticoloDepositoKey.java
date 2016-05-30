package it.eurotn.panjea.mrp.util;

/**
 * Classe utilizzata come chiave per articolo e deposito da usare nelle mappe.
 * 
 * @author giangi
 * @version 1.0, 17/dic/2013
 */
public class ArticoloDepositoKey {

	private int idArticolo;

	private int idDeposito;

	/**
	 * costruttore.
	 * 
	 * @param idArticolo
	 *            articolo
	 * @param idDeposito
	 *            deposito
	 */
	public ArticoloDepositoKey(final int idArticolo, final int idDeposito) {
		this.idArticolo = idArticolo;
		this.idDeposito = idDeposito;
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
		ArticoloDepositoKey other = (ArticoloDepositoKey) obj;
		if (idArticolo != other.idArticolo) {
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
	 * @return Returns the idDeposito.
	 */
	public int getIdDeposito() {
		return idDeposito;
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
		result = prime * result + idArticolo;
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
	 * @param idDeposito
	 *            The idDeposito to set.
	 */
	public void setIdDeposito(int idDeposito) {
		this.idDeposito = idDeposito;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ArticoloDepositoKey [idArticolo=" + idArticolo + ", idDeposito=" + idDeposito + "]";
	}

}
