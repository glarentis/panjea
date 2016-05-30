package it.eurotn.panjea.fornodoro;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RigaProduzione {

	private String codice;
	private String descrizione;
	private String um;
	private double qta;
	private int confezioni;
	private String lotto;
	private Date dataScadenza;

	/**
	 * Costruttore.
	 */
	public RigaProduzione() {
		super();
	}

	/**
	 * @return the codice
	 */
	public String getCodice() {
		return codice;
	}

	/**
	 * @return the confezioni
	 */
	public int getConfezioni() {
		return confezioni;
	}

	/**
	 * @return the dataScadenza
	 */
	public Date getDataScadenza() {
		return dataScadenza;
	}

	/**
	 * @return the descrizione
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * @return the lotto
	 */
	public String getLotto() {
		return lotto;
	}

	/**
	 * @return the qta
	 */
	public double getQta() {
		return qta;
	}

	/**
	 * @return the um
	 */
	public String getUm() {
		return um;
	}

	/**
	 * @param codice
	 *            the codice to set
	 */
	public void setCodice(String codice) {
		this.codice = codice;
	}

	/**
	 * @param confezioni
	 *            the confezioni to set
	 */
	public void setConfezioni(int confezioni) {
		this.confezioni = confezioni;
	}

	/**
	 * @param dataScadenza
	 *            the dataScadenza to set
	 */
	public void setDataScadenza(Date dataScadenza) {
		this.dataScadenza = dataScadenza;
	}

	/**
	 * @param descrizione
	 *            the descrizione to set
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	/**
	 * @param lotto
	 *            the lotto to set
	 */
	public void setLotto(String lotto) {
		this.lotto = lotto;
	}

	/**
	 * @param qta
	 *            the qta to set
	 */
	public void setQta(double qta) {
		this.qta = qta;
	}

	/**
	 * @param um
	 *            the um to set
	 */
	public void setUm(String um) {
		this.um = um;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder = stringBuilder.append(codice);
		stringBuilder = stringBuilder.append("#");
		stringBuilder = stringBuilder.append(descrizione);
		stringBuilder = stringBuilder.append("#");
		stringBuilder = stringBuilder.append(um);
		stringBuilder = stringBuilder.append("#");
		stringBuilder = stringBuilder.append(qta);
		stringBuilder = stringBuilder.append("#");
		stringBuilder = stringBuilder.append(confezioni);
		stringBuilder = stringBuilder.append("#");
		stringBuilder = stringBuilder.append(lotto);
		stringBuilder = stringBuilder.append("#");
		stringBuilder = stringBuilder.append(new SimpleDateFormat("yyyyMMdd").format(dataScadenza));
		return stringBuilder.toString();
	}

}
