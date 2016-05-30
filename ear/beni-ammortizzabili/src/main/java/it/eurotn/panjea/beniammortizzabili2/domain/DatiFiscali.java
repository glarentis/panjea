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
public class DatiFiscali implements Serializable {

	private static final long serialVersionUID = -6917355058251889003L;

	public static final String REF = "DatiFiscali";
	public static final String PROP_AMMORTAMENTO_IN_CORSO = "ammortamentoInCorso";
	public static final String PROP_PERCENTUALE_AMMORTAMENTO_ORDINARIO = "percentualeAmmortamentoOrdinario";
	public static final String PROP_ATTIVAZIONE_AMMORTAMENTO_ANTICIPATO = "attivazioneAmmortamentoAnticipato";
	public static final String PROP_PERCENTUALE_AMMORTAMENTO_ANTICIPATO = "percentualeAmmortamentoAnticipato";
	public static final String PROP_PERCENTUALE_AMMORTAMENTO_ACCELERATO = "percentualeAmmortamentoAccelerato";

	@Column(name = "ammortamento_in_corso_fisc")
	private boolean ammortamentoInCorso = false;
	private boolean attivazioneAmmortamentoAnticipato = false;

	@Column(name = "perc_ammortamento_ordinario_fisc")
	private Double percentualeAmmortamentoOrdinario;
	private Double percentualeAmmortamentoAnticipato;
	private Double percentualeAmmortamentoAccelerato;

	{
		percentualeAmmortamentoOrdinario = 0.0;
		percentualeAmmortamentoAnticipato = 0.0;
		percentualeAmmortamentoAccelerato = 0.0;
	}

	/**
	 * Costruttore di default.
	 */
	public DatiFiscali() {
	}

	/**
	 * @return the percentualeAmmortamentoAccelerato
	 */
	public Double getPercentualeAmmortamentoAccelerato() {
		return percentualeAmmortamentoAccelerato;
	}

	/**
	 * @return the percentualeAmmortamentoAnticipato
	 */
	public Double getPercentualeAmmortamentoAnticipato() {
		return percentualeAmmortamentoAnticipato;
	}

	/**
	 * @return the percentualeAmmortamentoOrdinario
	 */
	public Double getPercentualeAmmortamentoOrdinario() {
		return percentualeAmmortamentoOrdinario;
	}

	/**
	 * @return the ammortamentoInCorso
	 */
	public boolean isAmmortamentoInCorso() {
		return ammortamentoInCorso;
	}

	/**
	 * @return the attivazioneAmmortamentoAnticipato
	 */
	public boolean isAttivazioneAmmortamentoAnticipato() {
		return attivazioneAmmortamentoAnticipato;
	}

	/**
	 * @param ammortamentoInCorso
	 *            the ammortamentoInCorso to set
	 */
	public void setAmmortamentoInCorso(boolean ammortamentoInCorso) {
		this.ammortamentoInCorso = ammortamentoInCorso;
	}

	/**
	 * @param attivazioneAmmortamentoAnticipato
	 *            the attivazioneAmmortamentoAnticipato to set
	 */
	public void setAttivazioneAmmortamentoAnticipato(boolean attivazioneAmmortamentoAnticipato) {
		this.attivazioneAmmortamentoAnticipato = attivazioneAmmortamentoAnticipato;
	}

	/**
	 * @param percentualeAmmortamentoAccelerato
	 *            the percentualeAmmortamentoAccelerato to set
	 */
	public void setPercentualeAmmortamentoAccelerato(Double percentualeAmmortamentoAccelerato) {
		this.percentualeAmmortamentoAccelerato = percentualeAmmortamentoAccelerato;
	}

	/**
	 * @param percentualeAmmortamentoAnticipato
	 *            the percentualeAmmortamentoAnticipato to set
	 */
	public void setPercentualeAmmortamentoAnticipato(Double percentualeAmmortamentoAnticipato) {
		this.percentualeAmmortamentoAnticipato = percentualeAmmortamentoAnticipato;
	}

	/**
	 * @param percentualeAmmortamentoOrdinario
	 *            the percentualeAmmortamentoOrdinario to set
	 */
	public void setPercentualeAmmortamentoOrdinario(Double percentualeAmmortamentoOrdinario) {
		this.percentualeAmmortamentoOrdinario = percentualeAmmortamentoOrdinario;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("DatiFiscali[");
		buffer.append("ammortamentoInCorso = ").append(ammortamentoInCorso);
		buffer.append(" attivazioneAmmortamentoAnticipato = ").append(attivazioneAmmortamentoAnticipato);
		buffer.append(" percentualeAmmortamentoAccelerato = ").append(percentualeAmmortamentoAccelerato);
		buffer.append(" percentualeAmmortamentoAnticipato = ").append(percentualeAmmortamentoAnticipato);
		buffer.append(" percentualeAmmortamentoOrdinario = ").append(percentualeAmmortamentoOrdinario);
		buffer.append("]");
		return buffer.toString();
	}

}
