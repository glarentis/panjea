package it.eurotn.panjea.tesoreria.solleciti.editors;

import it.eurotn.panjea.rate.domain.Rata;
import it.eurotn.panjea.tesoreria.solleciti.Sollecito;

public class RataSollecitoPM {

	private Sollecito sollecito;

	private Rata rata;

	/**
	 * Costruttore.
	 * 
	 */
	public RataSollecitoPM() {
		super();
		this.rata = new Rata();
		this.sollecito = new Sollecito();
	}

	/**
	 * Costruttore.
	 * 
	 * @param sollecito
	 *            sollecito
	 * @param rata
	 *            rata
	 */
	public RataSollecitoPM(final Sollecito sollecito, final Rata rata) {
		super();
		this.sollecito = sollecito;
		this.rata = rata;
	}

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
		RataSollecitoPM other = (RataSollecitoPM) obj;
		if (rata == null) {
			if (other.rata != null) {
				return false;
			}
		} else if (!rata.equals(other.rata)) {
			return false;
		}
		if (sollecito == null) {
			if (other.sollecito != null) {
				return false;
			}
		} else if (!sollecito.equals(other.sollecito)) {
			return false;
		}
		return true;
	}

	/**
	 * @return the rata
	 */
	public Rata getRata() {
		return rata;
	}

	/**
	 * @return the sollecito
	 */
	public Sollecito getSollecito() {
		return sollecito;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((rata == null) ? 0 : rata.hashCode());
		result = prime * result + ((sollecito == null) ? 0 : sollecito.hashCode());
		return result;
	}

	/**
	 * @param rata
	 *            the rata to set
	 */
	public void setRata(Rata rata) {
		this.rata = rata;
	}

	/**
	 * @param sollecito
	 *            the sollecito to set
	 */
	public void setSollecito(Sollecito sollecito) {
		this.sollecito = sollecito;
	}

}
