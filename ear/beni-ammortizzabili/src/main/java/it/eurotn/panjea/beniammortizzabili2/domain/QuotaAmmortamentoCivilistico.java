/**
 * 
 */
package it.eurotn.panjea.beniammortizzabili2.domain;

import java.math.BigDecimal;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author Leonardo
 * 
 */
@Entity
@DiscriminatorValue("C")
public class QuotaAmmortamentoCivilistico extends QuotaAmmortamento {

	private static final long serialVersionUID = 4854725140966091819L;

	public static final String PROP_PERC_MAGGIOR_UTILIZZO_BENE = "percMaggiorUtilizzoBene";
	public static final String PROP_IMP_MAGGIOR_UTILIZZO_BENE = "impMaggiorUtilizzoBene";
	public static final String PROP_PERC_MINORE_UTILIZZO_BENE = "percMinoreUtilizzoBene";
	public static final String PROP_IMP_MINORE_UTILIZZO_BENE = "impMinoreUtilizzoBene";

	private double percMaggiorUtilizzoBene;
	private double percMinoreUtilizzoBene;

	private BigDecimal impMaggiorUtilizzoBene;
	private BigDecimal impMinoreUtilizzoBene;

	/**
	 * Costruttore di default.
	 */
	public QuotaAmmortamentoCivilistico() {
		super();
		initialize();
	}

	/**
	 * @return the impMaggiorUtilizzoBene
	 */
	public BigDecimal getImpMaggiorUtilizzoBene() {
		return impMaggiorUtilizzoBene;
	}

	/**
	 * @return the impMinoreUtilizzoBene
	 */
	public BigDecimal getImpMinoreUtilizzoBene() {
		return impMinoreUtilizzoBene;
	}

	/**
	 * @return the percMaggiorUtilizzoBene
	 */
	public double getPercMaggiorUtilizzoBene() {
		return percMaggiorUtilizzoBene;
	}

	/**
	 * @return the percMinoreUtilizzoBene
	 */
	public double getPercMinoreUtilizzoBene() {
		return percMinoreUtilizzoBene;
	}

	@Override
	public BigDecimal getTotaleAnno() {
		return BigDecimal.ZERO;
	}

	/**
	 * Inizializza i valori delle variabili dell'istanza.
	 */
	private void initialize() {
		percMaggiorUtilizzoBene = 0;
		percMinoreUtilizzoBene = 0;
		impMaggiorUtilizzoBene = BigDecimal.ZERO;
		impMinoreUtilizzoBene = BigDecimal.ZERO;
	}

	/**
	 * @param impMaggiorUtilizzoBene
	 *            the impMaggiorUtilizzoBene to set
	 */
	public void setImpMaggiorUtilizzoBene(BigDecimal impMaggiorUtilizzoBene) {
		this.impMaggiorUtilizzoBene = impMaggiorUtilizzoBene;
	}

	/**
	 * @param impMinoreUtilizzoBene
	 *            the impMinoreUtilizzoBene to set
	 */
	public void setImpMinoreUtilizzoBene(BigDecimal impMinoreUtilizzoBene) {
		this.impMinoreUtilizzoBene = impMinoreUtilizzoBene;
	}

	/**
	 * @param percMaggiorUtilizzoBene
	 *            the percMaggiorUtilizzoBene to set
	 */
	public void setPercMaggiorUtilizzoBene(double percMaggiorUtilizzoBene) {
		this.percMaggiorUtilizzoBene = percMaggiorUtilizzoBene;
	}

	/**
	 * @param percMinoreUtilizzoBene
	 *            the percMinoreUtilizzoBene to set
	 */
	public void setPercMinoreUtilizzoBene(double percMinoreUtilizzoBene) {
		this.percMinoreUtilizzoBene = percMinoreUtilizzoBene;
	}

	@Override
	public String toString() {
		String superString = super.toString();
		StringBuffer buffer = new StringBuffer();
		buffer.append("QuotaAmmortamentoCivilistico[");
		buffer.append("impMaggiorUtilizzoBene = ").append(impMaggiorUtilizzoBene);
		buffer.append(" impMinoreUtilizzoBene = ").append(impMinoreUtilizzoBene);
		buffer.append(" percMaggiorUtilizzoBene = ").append(percMaggiorUtilizzoBene);
		buffer.append(" percMinoreUtilizzoBene = ").append(percMinoreUtilizzoBene);
		buffer.append("]");
		return superString + buffer.toString();
	}

}
