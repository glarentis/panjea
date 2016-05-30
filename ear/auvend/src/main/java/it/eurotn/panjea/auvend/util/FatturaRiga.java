/**
 * 
 */
package it.eurotn.panjea.auvend.util;

import java.util.Date;

/**
 * Classe di utilita' che rappresenta la riga dei documenti fattura di AuVend.
 * 
 * @author adriano
 * @version 1.0, 27/gen/2009
 * 
 */
public class FatturaRiga {

	private Date dataFattura;

	private int numeroFattura;

	private String prodotto;

	private int quantita;

	private Double prezzo;

	private Double percentualeIva;

	private boolean omaggio;

	/**
	 * 
	 */
	public FatturaRiga() {
		super();
	}

	/**
	 * @return Returns the dataFattura.
	 */
	public Date getDataFattura() {
		return dataFattura;
	}

	/**
	 * @return Returns the numeroFattura.
	 */
	public int getNumeroFattura() {
		return numeroFattura;
	}

	/**
	 * @return Returns the percentualeIva.
	 */
	public Double getPercentualeIva() {
		return percentualeIva;
	}

	/**
	 * @return Returns the prezzo.
	 */
	public Double getPrezzo() {
		return prezzo;
	}

	/**
	 * @return Returns the prodotto.
	 */
	public String getProdotto() {
		return prodotto;
	}

	/**
	 * @return Returns the quantita.
	 */
	public int getQuantita() {
		return quantita;
	}

	/**
	 * @return Returns the omaggio.
	 */
	public boolean isOmaggio() {
		return omaggio;
	}

	/**
	 * @param dataFattura
	 *            The dataFattura to set.
	 */
	public void setDataFattura(Date dataFattura) {
		this.dataFattura = dataFattura;
	}

	/**
	 * @param numeroFattura
	 *            The numeroFattura to set.
	 */
	public void setNumeroFattura(int numeroFattura) {
		this.numeroFattura = numeroFattura;
	}

	/**
	 * @param omaggio
	 *            The omaggio to set.
	 */
	public void setOmaggio(boolean omaggio) {
		this.omaggio = omaggio;
	}

	/**
	 * @param percentualeIva
	 *            The percentualeIva to set.
	 */
	public void setPercentualeIva(Double percentualeIva) {
		this.percentualeIva = percentualeIva;
	}

	/**
	 * @param prezzo
	 *            The prezzo to set.
	 */
	public void setPrezzo(Double prezzo) {
		this.prezzo = prezzo;
	}

	/**
	 * @param prodotto
	 *            The prodotto to set.
	 */
	public void setProdotto(String prodotto) {
		this.prodotto = prodotto;
	}

	/**
	 * @param quantita
	 *            The quantita to set.
	 */
	public void setQuantita(int quantita) {
		this.quantita = quantita;
	}

	/**
	 * Constructs a <code>String</code> with all attributes in name = value format.
	 * 
	 * @return a <code>String</code> representation of this object.
	 */
	@Override
	public String toString() {

		StringBuffer retValue = new StringBuffer();

		retValue.append("FatturaRiga[ ").append(super.toString());
		retValue.append(" dataFattura = ").append(this.dataFattura);
		retValue.append(" numeroFattura = ").append(this.numeroFattura);
		retValue.append(" omaggio = ").append(this.omaggio);
		retValue.append(" percentualeIva = ").append(this.percentualeIva);
		retValue.append(" prezzo = ").append(this.prezzo);
		retValue.append(" prodotto = ").append(this.prodotto);
		retValue.append(" quantita = ").append(this.quantita);
		retValue.append(" ]");

		return retValue.toString();
	}

}
