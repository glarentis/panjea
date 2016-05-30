/**
 * 
 */
package it.eurotn.panjea.beniammortizzabili2.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author Leonardo
 * 
 */
@Embeddable
public class DatiCivilistici implements Serializable {

	private static final long serialVersionUID = -6236341850819175032L;

	public static final String REF = "DatiCivilistici";
	public static final String PROP_AMMORTAMENTO_IN_CORSO = "ammortamentoInCorso";
	public static final String PROP_PERCENTUALE_AMMORTAMENTO_ORDINARIO = "percentualeAmmortamentoOrdinario";
	public static final String PROP_PERCENTUALE_MINORE_UTILIZZO_BENE = "percentualeMinoreUtilizzoBene";
	public static final String PROP_PERCENTUALE_MAGGIORE_UTILIZZO_BENE = "percentualeMaggioreUtilizzoBene";

	@Column(name = "ammortamento_in_corso_civ")
	private boolean ammortamentoInCorso = false;

	@Column(name = "perc_ammortamento_ordinario_civ")
	private Double percentualeAmmortamentoOrdinario;
	private Double percentualeMinoreUtilizzoBene;
	private Double percentualeMaggioreUtilizzoBene;

	{
		percentualeAmmortamentoOrdinario = 0.0;
		percentualeMinoreUtilizzoBene = 0.0;
		percentualeMaggioreUtilizzoBene = 0.0;
	}

	/**
	 * Costruttore di default.
	 */
	public DatiCivilistici() {
	}

	/**
	 * @return the percentualeAmmortamentoOrdinario
	 */
	public Double getPercentualeAmmortamentoOrdinario() {
		return percentualeAmmortamentoOrdinario;
	}

	/**
	 * @return the percentualeMaggioreUtilizzoBene
	 */
	public Double getPercentualeMaggioreUtilizzoBene() {
		return percentualeMaggioreUtilizzoBene;
	}

	/**
	 * @return the percentualeMinoreUtilizzoBene
	 */
	public Double getPercentualeMinoreUtilizzoBene() {
		return percentualeMinoreUtilizzoBene;
	}

	/**
	 * @return the ammortamentoInCorso
	 */
	public boolean isAmmortamentoInCorso() {
		return ammortamentoInCorso;
	}

	/**
	 * @param ammortamentoInCorso
	 *            the ammortamentoInCorso to set
	 */
	public void setAmmortamentoInCorso(boolean ammortamentoInCorso) {
		this.ammortamentoInCorso = ammortamentoInCorso;
	}

	/**
	 * @param percentualeAmmortamentoOrdinario
	 *            the percentualeAmmortamentoOrdinario to set
	 */
	public void setPercentualeAmmortamentoOrdinario(Double percentualeAmmortamentoOrdinario) {
		this.percentualeAmmortamentoOrdinario = percentualeAmmortamentoOrdinario;
	}

	/**
	 * @param percentualeMaggioreUtilizzoBene
	 *            the percentualeMaggioreUtilizzoBene to set
	 */
	public void setPercentualeMaggioreUtilizzoBene(Double percentualeMaggioreUtilizzoBene) {
		this.percentualeMaggioreUtilizzoBene = percentualeMaggioreUtilizzoBene;
	}

	/**
	 * @param percentualeMinoreUtilizzoBene
	 *            the percentualeMinoreUtilizzoBene to set
	 */
	public void setPercentualeMinoreUtilizzoBene(Double percentualeMinoreUtilizzoBene) {
		this.percentualeMinoreUtilizzoBene = percentualeMinoreUtilizzoBene;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("DatiCivilistici[");
		buffer.append("ammortamentoInCorso = ").append(ammortamentoInCorso);
		buffer.append(" percentualeAmmortamentoOrdinario = ").append(percentualeAmmortamentoOrdinario);
		buffer.append(" percentualeMaggioreUtilizzoBene = ").append(percentualeMaggioreUtilizzoBene);
		buffer.append(" percentualeMinoreUtilizzoBene = ").append(percentualeMinoreUtilizzoBene);
		buffer.append("]");
		return buffer.toString();
	}

}
