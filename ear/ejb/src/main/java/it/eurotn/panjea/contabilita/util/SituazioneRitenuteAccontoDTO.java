/**
 *
 */
package it.eurotn.panjea.contabilita.util;

import it.eurotn.panjea.anagrafica.documenti.domain.CodiceDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.util.EntitaDocumento;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author fattazzo
 *
 */
public class SituazioneRitenuteAccontoDTO implements Serializable {

	private static final long serialVersionUID = 9010837073271123969L;

	private Integer idAreaContabile;
	private Date dataRegistrazione;
	private String tributo;
	private Double percentualeAliquota;

	private TipoDocumento tipoDocumento;

	private Integer idDocumento;
	private BigDecimal totaleDocumento;
	private CodiceDocumento numeroDocumento;

	private CodiceDocumento protocollo;

	private EntitaDocumento fornitore;

	private Integer idRata;

	private BigDecimal residuo;
	private BigDecimal totalePagato;
	private Date dataPagamento;

	private BigDecimal totaleRitenute;
	private BigDecimal totaleRitenuteAperte;
	private BigDecimal totaleRitenutePagate;
	private Date ultimaDataPagamentoRitenuta;

	{
		protocollo = new CodiceDocumento();
		tipoDocumento = new TipoDocumento();
		numeroDocumento = new CodiceDocumento();
		fornitore = new EntitaDocumento();
		fornitore.setTipoEntita(TipoEntita.FORNITORE);
	}

	/**
	 * @return the dataPagamento
	 */
	public Date getDataPagamento() {
		return dataPagamento;
	}

	/**
	 * @return the dataRegistrazione
	 */
	public Date getDataRegistrazione() {
		return dataRegistrazione;
	}

	/**
	 * @return the entita
	 */
	public EntitaDocumento getFornitore() {
		return fornitore;
	}

	/**
	 * @return the idAreaContabile
	 */
	public Integer getIdAreaContabile() {
		return idAreaContabile;
	}

	/**
	 * @return the idDocumento
	 */
	public Integer getIdDocumento() {
		return idDocumento;
	}

	/**
	 * @return the idRata
	 */
	public Integer getIdRata() {
		return idRata;
	}

	/**
	 * @return the numeroDocumento
	 */
	public CodiceDocumento getNumeroDocumento() {
		return numeroDocumento;
	}

	/**
	 * @return the percentualeAliquota
	 */
	public Double getPercentualeAliquota() {
		return percentualeAliquota;
	}

	/**
	 * @return the protocollo
	 */
	public CodiceDocumento getProtocollo() {
		return protocollo;
	}

	/**
	 * @return the residuo
	 */
	public BigDecimal getResiduo() {
		return residuo;
	}

	/**
	 * @return the tipoDocumento
	 */
	public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}

	/**
	 * @return the totaleDocumento
	 */
	public BigDecimal getTotaleDocumento() {
		return totaleDocumento;
	}

	/**
	 * @return the totalePagato
	 */
	public BigDecimal getTotalePagato() {
		return totalePagato;
	}

	/**
	 * @return the totaleRitenute
	 */
	public BigDecimal getTotaleRitenute() {
		return totaleRitenute;
	}

	/**
	 * @return the totaleRitenuteAperte
	 */
	public BigDecimal getTotaleRitenuteAperte() {
		return totaleRitenuteAperte;
	}

	/**
	 * @return the totaleRitenutePagate
	 */
	public BigDecimal getTotaleRitenutePagate() {
		return totaleRitenutePagate;
	}

	/**
	 * @return the tributo
	 */
	public String getTributo() {
		return tributo;
	}

	/**
	 * @return the ultimaDataPagamentoRitenuta
	 */
	public Date getUltimaDataPagamentoRitenuta() {
		return ultimaDataPagamentoRitenuta;
	}

	/**
	 * @param codiceFornitore
	 *            the codiceFornitore to set
	 */
	public void setCodiceFornitore(Integer codiceFornitore) {
		this.fornitore.setCodice(codiceFornitore);
	}

	/**
	 * @param codiceTipoDocumento
	 *            the codiceTipoDocumento to set
	 */
	public void setCodiceTipoDocumento(String codiceTipoDocumento) {
		this.tipoDocumento.setCodice(codiceTipoDocumento);
	}

	/**
	 * @param dataPagamento
	 *            the dataPagamento to set
	 */
	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	/**
	 * @param dataRegistrazione
	 *            the dataRegistrazione to set
	 */
	public void setDataRegistrazione(Date dataRegistrazione) {
		this.dataRegistrazione = dataRegistrazione;
	}

	/**
	 * @param denominazioneFornitore
	 *            the denominazioneFornitore to set
	 */
	public void setDenominazioneFornitore(String denominazioneFornitore) {
		this.fornitore.setDescrizione(denominazioneFornitore);
	}

	/**
	 * @param descrizioneTipoDocumento
	 *            the descrizioneTipoDocumento to set
	 */
	public void setDescrizioneTipoDocumento(String descrizioneTipoDocumento) {
		this.tipoDocumento.setDescrizione(descrizioneTipoDocumento);
	}

	/**
	 * @param idAreaContabile
	 *            the idAreaContabile to set
	 */
	public void setIdAreaContabile(Integer idAreaContabile) {
		this.idAreaContabile = idAreaContabile;
	}

	/**
	 * @param idDocumento
	 *            the idDocumento to set
	 */
	public void setIdDocumento(Integer idDocumento) {
		this.idDocumento = idDocumento;
	}

	/**
	 * @param idFornitore
	 *            the idFornitore to set
	 */
	public void setIdFornitore(Integer idFornitore) {
		this.fornitore.setId(idFornitore);
	}

	/**
	 * @param idRata
	 *            the idRata to set
	 */
	public void setIdRata(Integer idRata) {
		this.idRata = idRata;
	}

	/**
	 * @param idTipoDocumento
	 *            the idTipoDocumento to set
	 */
	public void setIdTipoDocumento(Integer idTipoDocumento) {
		this.tipoDocumento.setId(idTipoDocumento);
	}

	/**
	 * @param numeroDocumento
	 *            the numeroDocumento to set
	 */
	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento.setCodice(numeroDocumento);
	}

	/**
	 * @param numeroDocumentoOrder
	 *            the numeroDocumentoOrder to set
	 */
	public void setNumeroDocumentoOrder(String numeroDocumentoOrder) {
		this.numeroDocumento.setCodiceOrder(numeroDocumentoOrder);
	}

	/**
	 * @param percentualeAliquota
	 *            the percentualeAliquota to set
	 */
	public void setPercentualeAliquota(Double percentualeAliquota) {
		this.percentualeAliquota = percentualeAliquota;
	}

	/**
	 * @param protocollo
	 *            the protocollo to set
	 */
	public void setProtocollo(String protocollo) {
		this.protocollo.setCodice(protocollo);
	}

	/**
	 * @param protocolloOrder
	 *            the protocolloOrder to set
	 */
	public void setProtocolloOrder(String protocolloOrder) {
		this.protocollo.setCodiceOrder(protocolloOrder);
	}

	/**
	 * @param residuo
	 *            the residuo to set
	 */
	public void setResiduo(BigDecimal residuo) {
		this.residuo = residuo;
	}

	/**
	 * @param totaleDocumento
	 *            the totaleDocumento to set
	 */
	public void setTotaleDocumento(BigDecimal totaleDocumento) {
		this.totaleDocumento = totaleDocumento;
	}

	/**
	 * @param totalePagato
	 *            the totalePagato to set
	 */
	public void setTotalePagato(BigDecimal totalePagato) {
		this.totalePagato = totalePagato;
	}

	/**
	 * @param totaleRitenute
	 *            the totaleRitenute to set
	 */
	public void setTotaleRitenute(BigDecimal totaleRitenute) {
		this.totaleRitenute = totaleRitenute;
	}

	/**
	 * @param totaleRitenuteAperte
	 *            the totaleRitenuteAperte to set
	 */
	public void setTotaleRitenuteAperte(BigDecimal totaleRitenuteAperte) {
		this.totaleRitenuteAperte = totaleRitenuteAperte;
	}

	/**
	 * @param totaleRitenutePagate
	 *            the totaleRitenutePagate to set
	 */
	public void setTotaleRitenutePagate(BigDecimal totaleRitenutePagate) {
		this.totaleRitenutePagate = totaleRitenutePagate;
	}

	/**
	 * @param tributo
	 *            the tributo to set
	 */
	public void setTributo(String tributo) {
		this.tributo = tributo;
	}

	/**
	 * @param ultimaDataPagamentoRitenuta
	 *            the ultimaDataPagamentoRitenuta to set
	 */
	public void setUltimaDataPagamentoRitenuta(Date ultimaDataPagamentoRitenuta) {
		this.ultimaDataPagamentoRitenuta = ultimaDataPagamentoRitenuta;
	}

}
