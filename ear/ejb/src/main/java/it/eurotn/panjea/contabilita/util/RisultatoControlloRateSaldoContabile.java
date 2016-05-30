package it.eurotn.panjea.contabilita.util;

import it.eurotn.panjea.contabilita.domain.Conto.SottotipoConto;
import it.eurotn.panjea.tesoreria.util.SituazioneRata;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Contiene i risultato del controllo fra il saldo rate e quello contabile.
 * 
 * @author giangi
 * @version 1.0, 29/ott/2012
 * 
 */
public class RisultatoControlloRateSaldoContabile implements Serializable {
	private static final long serialVersionUID = 1311441320888018830L;
	private String codiceConto;
	private Integer codiceEntita;

	private String descrizioneEntita;

	private BigDecimal totaleRate;
	private BigDecimal saldo;

	{
		saldo = BigDecimal.ZERO;
		totaleRate = BigDecimal.ZERO;
	}

	/**
	 * 
	 * Costruttore.
	 * 
	 * @param saldo
	 *            saldo dal quale creare il risultatoControllo
	 */
	public RisultatoControlloRateSaldoContabile(final SaldoConto saldo) {
		setCodiceConto(saldo.getSottoContoCodiceCompleto());
		setCodiceEntita(Integer.parseInt(saldo.getSottoContoCodice()));
		setDescrizioneEntita(saldo.getSottoContoDescrizione());
		if (saldo.getSottoTipoConto() == SottotipoConto.CLIENTE) {
			setSaldo(saldo.getImportoDare().subtract(saldo.getImportoAvere()));
		} else {
			setSaldo(saldo.getImportoAvere().subtract(saldo.getImportoDare()));
		}
	}

	/**
	 * 
	 * Costruttore.
	 * 
	 * @param situazioneRata
	 *            situazioneRata dal quale creare il risultatoControllo. <b>NB:</b>il residuo della situazionerata
	 *            passata NON viene considerato.
	 */
	public RisultatoControlloRateSaldoContabile(final SituazioneRata situazioneRata) {
		setCodiceEntita(situazioneRata.getEntita().getCodice());
		setDescrizioneEntita(situazioneRata.getEntita().getDescrizione());
		setTotaleRate(BigDecimal.ZERO);
	}

	/**
	 * @return Returns the codiceConto.
	 */
	public String getCodiceConto() {
		return codiceConto;
	}

	/**
	 * @return Returns the codiceEntita.
	 */
	public Integer getCodiceEntita() {
		return codiceEntita;
	}

	/**
	 * @return Returns the descrizioneEntita.
	 */
	public String getDescrizioneEntita() {
		return descrizioneEntita;
	}

	/**
	 * @return Returns the differenza.
	 */
	public BigDecimal getDifferenza() {
		return saldo.subtract(totaleRate);
	}

	/**
	 * @return Returns the saldo.
	 */
	public BigDecimal getSaldo() {
		return saldo;
	}

	/**
	 * @return Returns the totaleRate.
	 */
	public BigDecimal getTotaleRate() {
		return totaleRate;
	}

	/**
	 * @param codiceConto
	 *            The codiceConto to set.
	 */
	public void setCodiceConto(String codiceConto) {
		this.codiceConto = codiceConto;
	}

	/**
	 * @param codiceEntita
	 *            The codiceEntita to set.
	 */
	public void setCodiceEntita(Integer codiceEntita) {
		this.codiceEntita = codiceEntita;
	}

	/**
	 * @param descrizioneEntita
	 *            The descrizioneEntita to set.
	 */
	public void setDescrizioneEntita(String descrizioneEntita) {
		this.descrizioneEntita = descrizioneEntita;
	}

	/**
	 * @param saldo
	 *            The saldo to set.
	 */
	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	/**
	 * @param totaleRate
	 *            The totaleRate to set.
	 */
	public void setTotaleRate(BigDecimal totaleRate) {
		this.totaleRate = totaleRate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RisultatoControlloRateSaldoContabile [codiceConto=" + codiceConto + ", codiceEntita=" + codiceEntita
				+ ", descrizioneEntita=" + descrizioneEntita + ", totaleRate=" + totaleRate + ", saldo=" + saldo
				+ ", differenza=" + getDifferenza() + "]";
	}

}
