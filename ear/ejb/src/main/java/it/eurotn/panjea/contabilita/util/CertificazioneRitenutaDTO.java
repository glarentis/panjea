/**
 * 
 */
package it.eurotn.panjea.contabilita.util;

import it.eurotn.panjea.anagrafica.domain.Anagrafica;
import it.eurotn.panjea.anagrafica.domain.lite.AziendaLite;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author fattazzo
 * 
 */
public class CertificazioneRitenutaDTO implements Serializable {

	private static final long serialVersionUID = 6706022434224980153L;

	private AziendaLite aziendaLite;

	private Integer idEntita;

	private Anagrafica anagrafica;

	private Integer idAreaContabile;

	private String prestazione;

	private String numeroDocumento;

	private Date dataDocumento;

	private BigDecimal totaleLordo;

	private BigDecimal imponibileNonSoggetto;

	private BigDecimal imponibileSoggetto;

	private Date dataPagamento;

	private Double percentualeAliquota;

	private BigDecimal importoRitenutaAcconto;

	private BigDecimal importoRimborsoSpese;

	private BigDecimal totaleImportoRitenute;

	/**
	 * @return the anagrafica
	 */
	public Anagrafica getAnagrafica() {
		return anagrafica;
	}

	/**
	 * @return the aziendaLite
	 */
	public AziendaLite getAziendaLite() {
		return aziendaLite;
	}

	/**
	 * @return the dataDocumento
	 */
	public Date getDataDocumento() {
		return dataDocumento;
	}

	/**
	 * @return the dataPagamento
	 */
	public Date getDataPagamento() {
		return dataPagamento;
	}

	/**
	 * @return the idAreaContabile
	 */
	public Integer getIdAreaContabile() {
		return idAreaContabile;
	}

	/**
	 * @return the idEntita
	 */
	public Integer getIdEntita() {
		return idEntita;
	}

	/**
	 * @return the imponibileNonSoggetto
	 */
	public BigDecimal getImponibileNonSoggetto() {
		return imponibileNonSoggetto;
	}

	/**
	 * @return the imponibileSoggetto
	 */
	public BigDecimal getImponibileSoggetto() {
		return imponibileSoggetto;
	}

	/**
	 * @return the importoRimborsoSpese
	 */
	public BigDecimal getImportoRimborsoSpese() {
		return importoRimborsoSpese;
	}

	/**
	 * @return the importoRitenutaAcconto
	 */
	public BigDecimal getImportoRitenutaAcconto() {
		return importoRitenutaAcconto;
	}

	/**
	 * @return the numeroDocumento
	 */
	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	/**
	 * @return the percentualeAliquota
	 */
	public Double getPercentualeAliquota() {
		return percentualeAliquota;
	}

	/**
	 * @return the prestazione
	 */
	public String getPrestazione() {
		return prestazione;
	}

	/**
	 * @return the totaleImportoRitenute
	 */
	public BigDecimal getTotaleImportoRitenute() {
		return totaleImportoRitenute;
	}

	/**
	 * @return the totaleLordo
	 */
	public BigDecimal getTotaleLordo() {
		return totaleLordo;
	}

	/**
	 * @param anagrafica
	 *            the anagrafica to set
	 */
	public void setAnagrafica(Anagrafica anagrafica) {
		this.anagrafica = anagrafica;
	}

	/**
	 * @param aziendaLite
	 *            the aziendaLite to set
	 */
	public void setAziendaLite(AziendaLite aziendaLite) {
		this.aziendaLite = aziendaLite;
	}

	/**
	 * @param dataDocumento
	 *            the dataDocumento to set
	 */
	public void setDataDocumento(Date dataDocumento) {
		this.dataDocumento = dataDocumento;
	}

	/**
	 * @param dataPagamento
	 *            the dataPagamento to set
	 */
	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	/**
	 * @param idAreaContabile
	 *            the idAreaContabile to set
	 */
	public void setIdAreaContabile(Integer idAreaContabile) {
		this.idAreaContabile = idAreaContabile;
	}

	/**
	 * @param idEntita
	 *            the idEntita to set
	 */
	public void setIdEntita(Integer idEntita) {
		this.idEntita = idEntita;
	}

	/**
	 * @param imponibileNonSoggetto
	 *            the imponibileNonSoggetto to set
	 */
	public void setImponibileNonSoggetto(BigDecimal imponibileNonSoggetto) {
		this.imponibileNonSoggetto = imponibileNonSoggetto;
	}

	/**
	 * @param imponibileSoggetto
	 *            the imponibileSoggetto to set
	 */
	public void setImponibileSoggetto(BigDecimal imponibileSoggetto) {
		this.imponibileSoggetto = imponibileSoggetto;
	}

	/**
	 * @param importoRimborsoSpese
	 *            the importoRimborsoSpese to set
	 */
	public void setImportoRimborsoSpese(BigDecimal importoRimborsoSpese) {
		this.importoRimborsoSpese = importoRimborsoSpese;
	}

	/**
	 * @param importoRitenutaAcconto
	 *            the importoRitenutaAcconto to set
	 */
	public void setImportoRitenutaAcconto(BigDecimal importoRitenutaAcconto) {
		this.importoRitenutaAcconto = importoRitenutaAcconto;
	}

	/**
	 * @param numeroDocumento
	 *            the numeroDocumento to set
	 */
	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	/**
	 * @param percentualeAliquota
	 *            the percentualeAliquota to set
	 */
	public void setPercentualeAliquota(Double percentualeAliquota) {
		this.percentualeAliquota = percentualeAliquota;
	}

	/**
	 * @param prestazione
	 *            the prestazione to set
	 */
	public void setPrestazione(String prestazione) {
		this.prestazione = prestazione;
	}

	/**
	 * @param totaleImportoRitenute
	 *            the totaleImportoRitenute to set
	 */
	public void setTotaleImportoRitenute(BigDecimal totaleImportoRitenute) {
		this.totaleImportoRitenute = totaleImportoRitenute;
	}

	/**
	 * @param totaleLordo
	 *            the totaleLordo to set
	 */
	public void setTotaleLordo(BigDecimal totaleLordo) {
		this.totaleLordo = totaleLordo;
	}
}
