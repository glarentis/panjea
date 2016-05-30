package it.eurotn.panjea.tesoreria.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
public class ParametriCreazioneInsoluti implements Serializable {

	private static final long serialVersionUID = 8262635291237368869L;

	private Date dataDocumento;

	private String numeroDocumento;
	private boolean numeroDocumentoRichiesto = true;

	private BigDecimal speseInsoluto;

	/**
	 * Costruttore.
	 */
	public ParametriCreazioneInsoluti() {
		super();
		speseInsoluto = BigDecimal.ZERO;
	}

	/**
	 * @return the dataDocumento
	 * @uml.property name="dataDocumento"
	 */
	public Date getDataDocumento() {
		return dataDocumento;
	}

	/**
	 * @return the numeroDocumento
	 * @uml.property name="numeroDocumento"
	 */
	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	/**
	 * @return the speseInsoluto
	 * @uml.property name="speseInsoluto"
	 */
	public BigDecimal getSpeseInsoluto() {
		return speseInsoluto;
	}

	/**
	 * @return the numeroDocumentoRichiesto
	 * @uml.property name="numeroDocumentoRichiesto"
	 */
	public boolean isNumeroDocumentoRichiesto() {
		return numeroDocumentoRichiesto;
	}

	/**
	 * @param dataDocumento
	 *            the dataDocumento to set
	 * @uml.property name="dataDocumento"
	 */
	public void setDataDocumento(Date dataDocumento) {
		this.dataDocumento = dataDocumento;
	}

	/**
	 * @param numeroDocumento
	 *            the numeroDocumento to set
	 * @uml.property name="numeroDocumento"
	 */
	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	/**
	 * @param numeroDocumentoRichiesto
	 *            the numeroDocumentoRichiesto to set
	 * @uml.property name="numeroDocumentoRichiesto"
	 */
	public void setNumeroDocumentoRichiesto(boolean numeroDocumentoRichiesto) {
		this.numeroDocumentoRichiesto = numeroDocumentoRichiesto;
	}

	/**
	 * @param speseInsoluto
	 *            the speseInsoluto to set
	 * @uml.property name="speseInsoluto"
	 */
	public void setSpeseInsoluto(BigDecimal speseInsoluto) {
		this.speseInsoluto = speseInsoluto;
	}

}
