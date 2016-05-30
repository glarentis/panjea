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
public class BeneAmmortizzabileCalcoloAmmortamento {

	private Integer idBeneAmmortizzabile;

	private BigDecimal importoSoggettoAmmortamento;
	private BigDecimal importoValutazioniBene;
	private BigDecimal importoStoricoVendite;
	private BigDecimal importoSoggettoAmmortamentoFigli;
	private BigDecimal importoOrdinarioFiscale;
	private BigDecimal importoAnticipatoFiscale;
	private BigDecimal importoValutazioniFondo;
	private BigDecimal importoStornoFondoVendite;

	// private BigDecimal valoreBene;
	// private BigDecimal valoreFondo;
	// private BigDecimal totaleAmmortamentoFiscale;

	/**
	 * 
	 */
	public BeneAmmortizzabileCalcoloAmmortamento() {
		super();
		initialize();
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
		BeneAmmortizzabileCalcoloAmmortamento other = (BeneAmmortizzabileCalcoloAmmortamento) obj;
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
	 * @return Returns the importoAnticipatoFiscale.
	 */
	public BigDecimal getImportoAnticipatoFiscale() {
		return importoAnticipatoFiscale;
	}

	/**
	 * @return Returns the importoOrdinarioFiscale.
	 */
	public BigDecimal getImportoOrdinarioFiscale() {
		return importoOrdinarioFiscale;
	}

	/**
	 * @return Returns the importoSoggettoAmmortamento.
	 */
	public BigDecimal getImportoSoggettoAmmortamento() {
		return importoSoggettoAmmortamento;
	}

	/**
	 * @return Returns the importoSoggettoAmmortamentoFigli.
	 */
	public BigDecimal getImportoSoggettoAmmortamentoFigli() {
		return importoSoggettoAmmortamentoFigli;
	}

	/**
	 * @return Returns the importoStoricoVendite.
	 */
	public BigDecimal getImportoStoricoVendite() {
		return importoStoricoVendite;
	}

	/**
	 * @return Returns the importoStornoFondoVendite.
	 */
	public BigDecimal getImportoStornoFondoVendite() {
		return importoStornoFondoVendite;
	}

	/**
	 * @return Returns the importoValutazioniBene.
	 */
	public BigDecimal getImportoValutazioniBene() {
		return importoValutazioniBene;
	}

	/**
	 * @return Returns the importoValutazioniFondo.
	 */
	public BigDecimal getImportoValutazioniFondo() {
		return importoValutazioniFondo;
	}

	/**
	 * Calcolo del valore totale ammortamento fiscale.
	 * 
	 * totale ammortamento fiscale = (importo amm.ordinario fiscale) + (importo amm.anticipato) + (rialutazione fondo)
	 * 
	 * @return Returns the totaleAmmortamentoFiscale.
	 */
	public BigDecimal getTotaleAmmortamentoFiscale() {
		return importoOrdinarioFiscale.add(importoAnticipatoFiscale).add(importoValutazioniFondo).subtract(
				importoStornoFondoVendite);
		// sumQuotaAmmortamentoImportoOrdinarioFiscale.add(sumQuotaAmmortamentoImportoAnticipatoFiscale).add(sommaImportoValutazioniFondo);
		// return totaleAmmortamentoFiscale;
	}

	/**
	 * Calcolo del valore corrente del bene soggetto ad ammortamento + rivalutazioni - svalutazioni - importo storico di
	 * riferimento di ogni vendita + ( per ogni figlio ) valore soggetto ad ammortamento.
	 * 
	 * @return Returns the valoreBene.
	 */
	public BigDecimal getValoreBene() {
		return importoSoggettoAmmortamento.add(importoValutazioniBene).subtract(importoStoricoVendite).add(
				importoSoggettoAmmortamentoFigli);
		// valoreBene =
		// valoreSoggettoAmmortamento.add(sommaImportoValutazioniBene).subtract(sommaImportoStoricoVendite).add(valoreSoggettoAmmortamentoFigli);
		// return valoreBene;
	}

	/**
	 * Calcolo del valore del fondo del bene totale importo amm. ord + totale importo amm.antic. + rivalutazioni fondo -
	 * svalutazioni fondo - ( per ogni vendita ) importo storno fondo ammortamento
	 * 
	 * @return Returns the valoreFondo.
	 */
	public BigDecimal getValoreFondo() {
		return importoOrdinarioFiscale.add(importoAnticipatoFiscale).add(importoValutazioniFondo).subtract(
				importoStornoFondoVendite);
		// valoreFondo =
		// sumQuotaAmmortamentoImportoOrdinarioFiscale.add(sumQuotaAmmortamentoImportoAnticipatoFiscale)
		// .add(sommaImportoValutazioniFondo).subtract(sommaImportoStornoFondoVendite);
		// return valoreFondo;
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
	 */
	private void initialize() {
		this.idBeneAmmortizzabile = null;
		this.importoSoggettoAmmortamento = BigDecimal.ZERO;
		this.importoValutazioniBene = BigDecimal.ZERO;
		this.importoStoricoVendite = BigDecimal.ZERO;
		this.importoSoggettoAmmortamentoFigli = BigDecimal.ZERO;
		this.importoAnticipatoFiscale = BigDecimal.ZERO;
		this.importoOrdinarioFiscale = BigDecimal.ZERO;
		this.importoValutazioniFondo = BigDecimal.ZERO;
		this.importoStornoFondoVendite = BigDecimal.ZERO;

	}

	/**
	 * @param idBeneAmmortizzabile
	 *            The idBeneAmmortizzabile to set.
	 */
	public void setIdBeneAmmortizzabile(Integer idBeneAmmortizzabile) {
		this.idBeneAmmortizzabile = idBeneAmmortizzabile;
	}

	/**
	 * @param importoAnticipatoFiscale
	 *            The importoAnticipatoFiscale to set.
	 */
	public void setImportoAnticipatoFiscale(BigDecimal importoAnticipatoFiscale) {
		this.importoAnticipatoFiscale = importoAnticipatoFiscale;
	}

	/**
	 * @param importoOrdinarioFiscale
	 *            The importoOrdinarioFiscale to set.
	 */
	public void setImportoOrdinarioFiscale(BigDecimal importoOrdinarioFiscale) {
		this.importoOrdinarioFiscale = importoOrdinarioFiscale;
	}

	/**
	 * @param importoSoggettoAmmortamento
	 *            The importoSoggettoAmmortamento to set.
	 */
	public void setImportoSoggettoAmmortamento(BigDecimal importoSoggettoAmmortamento) {
		this.importoSoggettoAmmortamento = importoSoggettoAmmortamento;
	}

	/**
	 * @param importoSoggettoAmmortamentoFigli
	 *            The importoSoggettoAmmortamentoFigli to set.
	 */
	public void setImportoSoggettoAmmortamentoFigli(BigDecimal importoSoggettoAmmortamentoFigli) {
		this.importoSoggettoAmmortamentoFigli = importoSoggettoAmmortamentoFigli;
	}

	/**
	 * @param importoStoricoVendite
	 *            The importoStoricoVendite to set.
	 */
	public void setImportoStoricoVendite(BigDecimal importoStoricoVendite) {
		this.importoStoricoVendite = importoStoricoVendite;
	}

	/**
	 * @param importoStornoFondoVendite
	 *            The importoStornoFondoVendite to set.
	 */
	public void setImportoStornoFondoVendite(BigDecimal importoStornoFondoVendite) {
		this.importoStornoFondoVendite = importoStornoFondoVendite;
	}

	/**
	 * @param importoValutazioniBene
	 *            The importoValutazioniBene to set.
	 */
	public void setImportoValutazioniBene(BigDecimal importoValutazioniBene) {
		this.importoValutazioniBene = importoValutazioniBene;
	}

	/**
	 * @param importoValutazioniFondo
	 *            The importoValutazioniFondo to set.
	 */
	public void setImportoValutazioniFondo(BigDecimal importoValutazioniFondo) {
		this.importoValutazioniFondo = importoValutazioniFondo;
	}

}
