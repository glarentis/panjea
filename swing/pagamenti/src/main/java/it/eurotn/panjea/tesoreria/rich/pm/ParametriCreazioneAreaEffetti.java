/**
 *
 */
package it.eurotn.panjea.tesoreria.rich.pm;

import java.math.BigDecimal;
import java.util.Date;

/**
 * PM per la testata di una areaEffetto di un documento di pagamento che contiene numero documento,data documento e
 * spese per la registrazione della stessa.
 *
 * @author Leonardo
 */
public class ParametriCreazioneAreaEffetti {

	private String numeroDocumento = null;
	private Date dataDocumento = null;
	private BigDecimal spese = null;
	private BigDecimal speseDistinta = null;
	private Boolean raggruppaBanche = true;

	/**
	 *
	 */
	public ParametriCreazioneAreaEffetti() {
		super();
		initialize();
	}

	/**
	 * @return the dataDocumento
	 */
	public Date getDataDocumento() {
		return dataDocumento;
	}

	/**
	 * @return the numeroDocumento
	 */
	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	/**
	 * @return Returns the raggruppaBanche.
	 */
	public Boolean getRaggruppaBanche() {
		return raggruppaBanche;
	}

	/**
	 * @return the spese
	 */
	public BigDecimal getSpese() {
		return spese;
	}

	/**
	 * @return the speseDistinta
	 */
	public BigDecimal getSpeseDistinta() {
		return speseDistinta;
	}

	/**
	 * Inizializza i valori di default.
	 */
	private void initialize() {
		this.spese = BigDecimal.ZERO;
		this.speseDistinta = BigDecimal.ZERO;
	}

	/**
	 * @param dataDocumento
	 *            the dataDocumento to set
	 */
	public void setDataDocumento(Date dataDocumento) {
		this.dataDocumento = dataDocumento;
	}

	/**
	 * @param numeroDocumento
	 *            the numeroDocumento to set
	 */
	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	/**
	 * @param raggruppaBanche
	 *            The raggruppaBanche to set.
	 */
	public void setRaggruppaBanche(Boolean raggruppaBanche) {
		this.raggruppaBanche = raggruppaBanche;
	}

	/**
	 * @param spese
	 *            the spese to set
	 */
	public void setSpese(BigDecimal spese) {
		this.spese = spese;
	}

	/**
	 * @param speseDistinta
	 *            the speseDistinta to set
	 */
	public void setSpeseDistinta(BigDecimal speseDistinta) {
		this.speseDistinta = speseDistinta;
	}

}
