/**
 * 
 */
package it.eurotn.panjea.beniammortizzabili2.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

/**
 * @author Leonardo
 * 
 */
@Embeddable
public class PoliticaCalcoloFiscale implements Serializable {

	private static final long serialVersionUID = -3985817334110283363L;

	public static final String REF = "PoliticaCalcoloFiscale";
	public static final String PROP_AMMORTAMENTO_ORDINARIO = "ammortamentoOrdinario";
	public static final String PROP_PERC_AMMORTAMENTO_ORDINARIO = "percAmmortamentoOrdinario";
	public static final String PROP_AMMORTAMENTO_ANTICIPATO = "ammortamentoAnticipato";
	public static final String PROP_PERC_AMMORTAMENTO_ANTICIPATO = "percAmmortamentoAnticipato";
	public static final String PROP_AMMORTAMENTO_ACCELERATO = "ammortamentoAccelerato";
	public static final String PROP_PERC_AMMORTAMENTO_ACCELERATO = "percAmmortamentoAccelerato";
	public static final String PROP_AMMORTAMENTO_RIDOTTO = "ammortamentoRidotto";
	public static final String PROP_PERC_AMMORTAMENTO_RIDOTTO = "percAmmortamentoRidotto";
	public static final String PROP_TOTALE_ORDINARIO = "totaleOrdinario";
	public static final String PROP_TOTALE_ANTICIPATO = "totaleAnticipato";

	@Column(name = "ammortamento_ordinario_fisc")
	private boolean ammortamentoOrdinario = false;
	@Column(name = "ammortamento_anticipato_fisc")
	private boolean ammortamentoAnticipato = false;
	@Column(name = "ammortamento_accelerato_fisc")
	private boolean ammortamentoAccelerato = false;
	@Column(name = "ammortamento_ridotto_fisc")
	private boolean ammortamentoRidotto = false;

	@Column(name = "perc_ammortamento_ordinario_fisc")
	private double percAmmortamentoOrdinario;
	@Column(name = "perc_ammortamento_anticipato_fisc")
	private double percAmmortamentoAnticipato;
	@Column(name = "perc_ammortamento_accelerato_fisc")
	private double percAmmortamentoAccelerato;
	@Column(name = "perc_ammortamento_ridotto_fisc")
	private double percAmmortamentoRidotto;

	@Transient
	private BigDecimal totaleOrdinario;
	@Transient
	private BigDecimal totaleAnticipato;

	/**
	 * Costruttore di default.
	 */
	public PoliticaCalcoloFiscale() {
		initialize();
	}

	/**
	 * @return the percAmmortamentoAccelerato
	 */
	public double getPercAmmortamentoAccelerato() {
		return percAmmortamentoAccelerato;
	}

	/**
	 * @return the percAmmortamentoAnticipato
	 */
	public double getPercAmmortamentoAnticipato() {
		return percAmmortamentoAnticipato;
	}

	/**
	 * @return the percAmmortamentoOrdinario
	 */
	public double getPercAmmortamentoOrdinario() {
		return percAmmortamentoOrdinario;
	}

	/**
	 * @return the percAmmortamentoRidotto
	 */
	public double getPercAmmortamentoRidotto() {
		return percAmmortamentoRidotto;
	}

	/**
	 * @return the totaleAnticipato
	 */
	public BigDecimal getTotaleAnticipato() {
		return totaleAnticipato;
	}

	/**
	 * @return the totaleOrdinario
	 */
	public BigDecimal getTotaleOrdinario() {
		return totaleOrdinario;
	}

	/**
	 * Inizializza i valori delle variabili dell'istanza.
	 */
	private void initialize() {
		percAmmortamentoOrdinario = 0;
		percAmmortamentoAnticipato = 0;
		percAmmortamentoAccelerato = 0;
		percAmmortamentoRidotto = 0;
		totaleOrdinario = BigDecimal.ZERO;
		totaleAnticipato = BigDecimal.ZERO;
	}

	/**
	 * @return the ammortamentoAccelerato
	 */
	public boolean isAmmortamentoAccelerato() {
		return ammortamentoAccelerato;
	}

	/**
	 * @return the ammortamentoAnticipato
	 */
	public boolean isAmmortamentoAnticipato() {
		return ammortamentoAnticipato;
	}

	/**
	 * @return the ammortamentoOrdinario
	 */
	public boolean isAmmortamentoOrdinario() {
		return ammortamentoOrdinario;
	}

	/**
	 * @return the ammortamentoRidotto
	 */
	public boolean isAmmortamentoRidotto() {
		return ammortamentoRidotto;
	}

	/**
	 * @param ammortamentoAccelerato
	 *            the ammortamentoAccelerato to set
	 */
	public void setAmmortamentoAccelerato(boolean ammortamentoAccelerato) {
		this.ammortamentoAccelerato = ammortamentoAccelerato;
	}

	/**
	 * @param ammortamentoAnticipato
	 *            the ammortamentoAnticipato to set
	 */
	public void setAmmortamentoAnticipato(boolean ammortamentoAnticipato) {
		this.ammortamentoAnticipato = ammortamentoAnticipato;
	}

	/**
	 * @param ammortamentoOrdinario
	 *            the ammortamentoOrdinario to set
	 */
	public void setAmmortamentoOrdinario(boolean ammortamentoOrdinario) {
		this.ammortamentoOrdinario = ammortamentoOrdinario;
	}

	/**
	 * @param ammortamentoRidotto
	 *            the ammortamentoRidotto to set
	 */
	public void setAmmortamentoRidotto(boolean ammortamentoRidotto) {
		this.ammortamentoRidotto = ammortamentoRidotto;
	}

	/**
	 * @param percAmmortamentoAccelerato
	 *            the percAmmortamentoAccelerato to set
	 */
	public void setPercAmmortamentoAccelerato(double percAmmortamentoAccelerato) {
		this.percAmmortamentoAccelerato = percAmmortamentoAccelerato;
	}

	/**
	 * @param percAmmortamentoAnticipato
	 *            the percAmmortamentoAnticipato to set
	 */
	public void setPercAmmortamentoAnticipato(double percAmmortamentoAnticipato) {
		this.percAmmortamentoAnticipato = percAmmortamentoAnticipato;
	}

	/**
	 * @param percAmmortamentoOrdinario
	 *            the percAmmortamentoOrdinario to set
	 */
	public void setPercAmmortamentoOrdinario(double percAmmortamentoOrdinario) {
		this.percAmmortamentoOrdinario = percAmmortamentoOrdinario;
	}

	/**
	 * @param percAmmortamentoRidotto
	 *            the percAmmortamentoRidotto to set
	 */
	public void setPercAmmortamentoRidotto(double percAmmortamentoRidotto) {
		this.percAmmortamentoRidotto = percAmmortamentoRidotto;
	}

	/**
	 * @param totaleAnticipato
	 *            the totaleAnticipato to set
	 */
	public void setTotaleAnticipato(BigDecimal totaleAnticipato) {
		this.totaleAnticipato = totaleAnticipato;
	}

	/**
	 * @param totaleOrdinario
	 *            the totaleOrdinario to set
	 */
	public void setTotaleOrdinario(BigDecimal totaleOrdinario) {
		this.totaleOrdinario = totaleOrdinario;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("PoliticaCalcoloFiscale[");
		buffer.append("ammortamentoAccelerato = ").append(ammortamentoAccelerato);
		buffer.append(" ammortamentoAnticipato = ").append(ammortamentoAnticipato);
		buffer.append(" ammortamentoOrdinario = ").append(ammortamentoOrdinario);
		buffer.append(" ammortamentoRidotto = ").append(ammortamentoRidotto);
		buffer.append(" percAmmortamentoAccelerato = ").append(percAmmortamentoAccelerato);
		buffer.append(" percAmmortamentoAnticipato = ").append(percAmmortamentoAnticipato);
		buffer.append(" percAmmortamentoOrdinario = ").append(percAmmortamentoOrdinario);
		buffer.append(" percAmmortamentoRidotto = ").append(percAmmortamentoRidotto);
		buffer.append("]");
		return buffer.toString();
	}

}
