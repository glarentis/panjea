/**
 * 
 */
package it.eurotn.panjea.beniammortizzabili.util;

import java.math.BigDecimal;

/**
 * 
 * 
 * @author adriano
 * @version 1.0, 03/dic/07
 * 
 */
public class BeneImportoSoggettoAdAmmortamento {

	private Integer idBeneAmmortizzabile;
	private BigDecimal importoSoggettoAdAmmortamento;

	/**
	 * @param paramIdBeneAmmortizzabile
	 *            id del bene
	 * @param paramImportoSoggettoAdAmmortamento
	 *            importo soggetto ad ammortamento
	 */
	public BeneImportoSoggettoAdAmmortamento(final Integer paramIdBeneAmmortizzabile,
			final BigDecimal paramImportoSoggettoAdAmmortamento) {
		super();
		initialize(paramIdBeneAmmortizzabile, paramImportoSoggettoAdAmmortamento);
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
		BeneImportoSoggettoAdAmmortamento other = (BeneImportoSoggettoAdAmmortamento) obj;
		if (idBeneAmmortizzabile == null) {
			if (other.idBeneAmmortizzabile != null) {
				return false;
			}
		} else if (!idBeneAmmortizzabile.equals(other.idBeneAmmortizzabile)) {
			return false;
		}
		return true;
	}

	/**
	 * @return Returns the idBeneAmmortizzabile.
	 */
	public Integer getIdBeneAmmortizzabile() {
		return idBeneAmmortizzabile;
	}

	/**
	 * @return Returns the importoSoggettoAdAmmortamento.
	 */
	public BigDecimal getImportoSoggettoAdAmmortamento() {
		return importoSoggettoAdAmmortamento;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idBeneAmmortizzabile == null) ? 0 : idBeneAmmortizzabile.hashCode());
		return result;
	}

	/**
	 * 
	 * @param paramIdBeneAmmortizzabile
	 *            id del bene
	 * @param paramImportoSoggettoAdAmmortamento
	 *            importo soggetto ad ammortamento
	 */
	private void initialize(Integer paramIdBeneAmmortizzabile, BigDecimal paramImportoSoggettoAdAmmortamento) {
		this.idBeneAmmortizzabile = paramIdBeneAmmortizzabile;
		this.importoSoggettoAdAmmortamento = paramImportoSoggettoAdAmmortamento;
	}

}
