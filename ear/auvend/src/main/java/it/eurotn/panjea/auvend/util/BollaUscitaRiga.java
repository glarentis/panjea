package it.eurotn.panjea.auvend.util;

import java.util.Date;

/**
 * 
 * Classe di utilita' che rappresenta la riga di carico dei caricatori di AuVend.
 * 
 * @author adriano
 * @version 1.0, 29/dic/2008
 * 
 */
public class BollaUscitaRiga {

	private int progressivo;

	private String prodotto;

	private Double quantitaCaricata;

	private Double quantitaResidua;

	private Date dataUltimaModifica;

	/**
	 * @return Returns the dataUltimaModifica.
	 */
	public Date getDataUltimaModifica() {
		return dataUltimaModifica;
	}

	/**
	 * @return Returns the prodotto.
	 */
	public String getProdotto() {
		return prodotto;
	}

	/**
	 * @return Returns the progressivo.
	 */
	public int getProgressivo() {
		return progressivo;
	}

	/**
	 * @return Returns the quantitaCaricata.
	 */
	public Double getQuantitaCaricata() {
		return quantitaCaricata;
	}

	/**
	 * @return Returns the quantitaResidua.
	 */
	public Double getQuantitaResidua() {
		return quantitaResidua;
	}

	/**
	 * @param dataUltimaModifica
	 *            The dataUltimaModifica to set.
	 */
	public void setDataUltimaModifica(Date dataUltimaModifica) {
		this.dataUltimaModifica = dataUltimaModifica;
	}

	/**
	 * @param prodotto
	 *            The prodotto to set.
	 */
	public void setProdotto(String prodotto) {
		this.prodotto = prodotto;
	}

	/**
	 * @param progressivo
	 *            The progressivo to set.
	 */
	public void setProgressivo(int progressivo) {
		this.progressivo = progressivo;
	}

	/**
	 * @param quantitaCaricata
	 *            The quantitaCaricata to set.
	 */
	public void setQuantitaCaricata(Double quantitaCaricata) {
		this.quantitaCaricata = quantitaCaricata;
	}

	/**
	 * @param quantitaResidua
	 *            The quantitaResidua to set.
	 */
	public void setQuantitaResidua(Double quantitaResidua) {
		this.quantitaResidua = quantitaResidua;
	}

	/**
	 * Costruisce una <code>String</code> con tutti gli attributi accodati con questo formato nome attributo = valore.
	 * 
	 * @return a <code>String</code> come risultato di questo oggetto
	 */
	@Override
	public String toString() {

		StringBuffer retValue = new StringBuffer();

		retValue.append("BollaUscitaRiga[ ").append(super.toString()).append(" progressivo = ")
				.append(this.progressivo).append(" prodotto = ").append(this.prodotto).append(" quantitaCaricata = ")
				.append(this.quantitaCaricata).append(" quantitaResidua = ").append(this.quantitaResidua)
				.append(" dataUltimaModifica = ").append(this.dataUltimaModifica).append(" ]");

		return retValue.toString();
	}

}
