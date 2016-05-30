/**
 * 
 */
package it.eurotn.panjea.beniammortizzabili.util;

import java.math.BigDecimal;

/**
 * 
 * @author adriano
 * @version 1.0, 03/dic/07
 * 
 */
public class QuotaAmmortamentoFiscaleImporto {

	private Integer idBeneAmmortizzabile;

	private BigDecimal impQuotaAmmortamentoOrdinario;
	private BigDecimal impQuotaAmmortamentoAnticipato;

	/**
	 * 
	 */
	public QuotaAmmortamentoFiscaleImporto() {
		this(null, BigDecimal.ZERO, BigDecimal.ZERO);
	}

	/**
	 * 
	 * @param paramIdBeneAmmortizzabile
	 *            id del bene
	 * @param paramImpQuotaAmmortamentoOrdinario
	 *            importo ordinario quota
	 * @param paramImpQuotaAmmortamentoAnticipato
	 *            importo anticipato quota
	 */
	public QuotaAmmortamentoFiscaleImporto(final Integer paramIdBeneAmmortizzabile,
			final BigDecimal paramImpQuotaAmmortamentoOrdinario, final BigDecimal paramImpQuotaAmmortamentoAnticipato) {
		super();
		this.idBeneAmmortizzabile = paramIdBeneAmmortizzabile;
		this.impQuotaAmmortamentoOrdinario = paramImpQuotaAmmortamentoOrdinario;
		this.impQuotaAmmortamentoAnticipato = paramImpQuotaAmmortamentoAnticipato;
	}

	/**
	 * @return Returns the idBeneAmmortizzabile.
	 */
	public Integer getIdBeneAmmortizzabile() {
		return idBeneAmmortizzabile;
	}

	/**
	 * @return Returns the impQuotaAmmortamentoAnticipato.
	 */
	public BigDecimal getImpQuotaAmmortamentoAnticipato() {
		return impQuotaAmmortamentoAnticipato;
	}

	/**
	 * @return Returns the impQuotaAmmortamentoOrdinario.
	 */
	public BigDecimal getImpQuotaAmmortamentoOrdinario() {
		return impQuotaAmmortamentoOrdinario;
	}

	/**
	 * @param idBeneAmmortizzabile
	 *            The idBeneAmmortizzabile to set.
	 */
	public void setIdBeneAmmortizzabile(Integer idBeneAmmortizzabile) {
		this.idBeneAmmortizzabile = idBeneAmmortizzabile;
	}

	/**
	 * @param impQuotaAmmortamentoAnticipato
	 *            The impQuotaAmmortamentoAnticipato to set.
	 */
	public void setImpQuotaAmmortamentoAnticipato(BigDecimal impQuotaAmmortamentoAnticipato) {
		this.impQuotaAmmortamentoAnticipato = impQuotaAmmortamentoAnticipato;
	}

	/**
	 * @param impQuotaAmmortamentoOrdinario
	 *            The impQuotaAmmortamentoOrdinario to set.
	 */
	public void setImpQuotaAmmortamentoOrdinario(BigDecimal impQuotaAmmortamentoOrdinario) {
		this.impQuotaAmmortamentoOrdinario = impQuotaAmmortamentoOrdinario;
	}

}
