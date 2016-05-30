package it.eurotn.panjea.onroad.domain;

import java.math.BigDecimal;
import java.util.Date;

public class RigaIvaOnRoad {

	private String codiceUtente;
	private String codiceCliente;
	private String codiceDestinatario;
	private String tipoDocumento;
	private String codiceCessionario;
	private String identificativoDocumento;
	private Date dataDocumento;
	private String oraDocumento;
	private String codiceIva;
	private BigDecimal imponibile;
	private BigDecimal imposta;
	private BigDecimal imponibileScontoNatura;
	private String statoDocumento;

	/**
	 * Costruttore.
	 */
	public RigaIvaOnRoad() {
		super();
	}

	/**
	 * @return l'identificativo della testata documento, uguale per DocumentoOnRoad,RigaDocumentoOnRoad e RigaIvaOnRoad.
	 */
	public String getChiaveDocumento() {
		StringBuilder sb = new StringBuilder();
		sb.append(getCodiceUtente());
		sb.append("#");
		sb.append(getCodiceCliente());
		sb.append("#");
		sb.append(getCodiceDestinatario());
		sb.append("#");
		sb.append(getTipoDocumento());
		sb.append("#");
		sb.append(getCodiceCessionario());
		sb.append("#");
		sb.append(getIdentificativoDocumento());
		sb.append("#");
		sb.append(getDataDocumento().getTime());
		return sb.toString();
	}

	/**
	 * @return the codiceCessionario
	 */
	public String getCodiceCessionario() {
		return codiceCessionario;
	}

	/**
	 * @return the codiceCliente
	 */
	public String getCodiceCliente() {
		return codiceCliente;
	}

	/**
	 * @return the codiceDestinatario
	 */
	public String getCodiceDestinatario() {
		return codiceDestinatario;
	}

	/**
	 * @return the codiceIva
	 */
	public String getCodiceIva() {
		return codiceIva;
	}

	/**
	 * @return the codiceUtente
	 */
	public String getCodiceUtente() {
		return codiceUtente;
	}

	/**
	 * @return the dataDocumento
	 */
	public Date getDataDocumento() {
		return dataDocumento;
	}

	/**
	 * @return the identificativoDocumento
	 */
	public String getIdentificativoDocumento() {
		return identificativoDocumento;
	}

	/**
	 * @return the imponibile
	 */
	public BigDecimal getImponibile() {
		return imponibile;
	}

	/**
	 * @return the imponibileScontoNatura
	 */
	public BigDecimal getImponibileScontoNatura() {
		return imponibileScontoNatura;
	}

	/**
	 * @return the imposta
	 */
	public BigDecimal getImposta() {
		return imposta;
	}

	/**
	 * @return the oraDocumento
	 */
	public String getOraDocumento() {
		return oraDocumento;
	}

	/**
	 * @return the statoDocumento
	 */
	public String getStatoDocumento() {
		return statoDocumento;
	}

	/**
	 * @return the tipoDocumento
	 */
	public String getTipoDocumento() {
		return tipoDocumento;
	}

	/**
	 * @param codiceCessionario
	 *            the codiceCessionario to set
	 */
	public void setCodiceCessionario(String codiceCessionario) {
		this.codiceCessionario = codiceCessionario;
	}

	/**
	 * @param codiceCliente
	 *            the codiceCliente to set
	 */
	public void setCodiceCliente(String codiceCliente) {
		this.codiceCliente = codiceCliente;
	}

	/**
	 * @param codiceDestinatario
	 *            the codiceDestinatario to set
	 */
	public void setCodiceDestinatario(String codiceDestinatario) {
		this.codiceDestinatario = codiceDestinatario;
	}

	/**
	 * @param codiceIva
	 *            the codiceIva to set
	 */
	public void setCodiceIva(String codiceIva) {
		this.codiceIva = codiceIva;
	}

	/**
	 * @param codiceUtente
	 *            the codiceUtente to set
	 */
	public void setCodiceUtente(String codiceUtente) {
		this.codiceUtente = codiceUtente;
	}

	/**
	 * @param dataDocumento
	 *            the dataDocumento to set
	 */
	public void setDataDocumento(Date dataDocumento) {
		this.dataDocumento = dataDocumento;
	}

	/**
	 * @param identificativoDocumento
	 *            the identificativoDocumento to set
	 */
	public void setIdentificativoDocumento(String identificativoDocumento) {
		this.identificativoDocumento = identificativoDocumento;
	}

	/**
	 * @param imponibile
	 *            the imponibile to set
	 */
	public void setImponibile(BigDecimal imponibile) {
		this.imponibile = imponibile;
	}

	/**
	 * @param imponibileScontoNatura
	 *            the imponibileScontoNatura to set
	 */
	public void setImponibileScontoNatura(BigDecimal imponibileScontoNatura) {
		this.imponibileScontoNatura = imponibileScontoNatura;
	}

	/**
	 * @param imposta
	 *            the imposta to set
	 */
	public void setImposta(BigDecimal imposta) {
		this.imposta = imposta;
	}

	/**
	 * @param oraDocumento
	 *            the oraDocumento to set
	 */
	public void setOraDocumento(String oraDocumento) {
		this.oraDocumento = oraDocumento;
	}

	/**
	 * @param statoDocumento
	 *            the statoDocumento to set
	 */
	public void setStatoDocumento(String statoDocumento) {
		this.statoDocumento = statoDocumento;
	}

	/**
	 * @param tipoDocumento
	 *            the tipoDocumento to set
	 */
	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

}
