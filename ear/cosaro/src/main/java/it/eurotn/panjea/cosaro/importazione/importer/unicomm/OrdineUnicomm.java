package it.eurotn.panjea.cosaro.importazione.importer.unicomm;

import it.eurotn.panjea.anagrafica.domain.Importo;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

public class OrdineUnicomm {
	private Integer codiceFornitore;
	private Integer codiceEnte;
	private Integer codiceInterlocutore;
	private Date data;
	private String numero = "";
	private String codiceArticolo;
	private String codiceArticoloFornitore;
	private String pezzi;
	private String colli;
	private double qta;

	/**
	 * @return Returns the codiceArticolo.
	 */
	public String getCodiceArticolo() {
		return codiceArticolo;
	}

	/**
	 * @return Returns the codiceArticoloFornitore.
	 */
	public String getCodiceArticoloFornitore() {
		return codiceArticoloFornitore;
	}

	/**
	 * @return Returns the codiceEnte.
	 */
	public Integer getCodiceEnte() {
		return codiceEnte;
	}

	/**
	 * @return Returns the codiceFornitore.
	 */
	public Integer getCodiceFornitore() {
		return codiceFornitore;
	}

	/**
	 * @return Returns the codiceInterlocutore.
	 */
	public Integer getCodiceInterlocutore() {
		return codiceInterlocutore;
	}

	/**
	 * @return Returns the colli.
	 */
	public String getColli() {
		return colli;
	}

	/**
	 * @return Returns the data.
	 */
	public Date getData() {
		return data;
	}

	/**
	 * @return Returns the numero.
	 */
	public String getNumero() {
		return numero;
	}

	/**
	 * @return Returns the pezzi.
	 */
	public String getPezzi() {
		return pezzi;
	}

	/**
	 * @return Returns the qta.
	 */
	public double getQta() {
		return qta;
	}

	/**
	 * @param codiceArticolo
	 *            The codiceArticolo to set.
	 */
	public void setCodiceArticolo(String codiceArticolo) {
		this.codiceArticolo = codiceArticolo;
	}

	/**
	 * @param codiceArticoloFornitore
	 *            The codiceArticoloFornitore to set.
	 */
	public void setCodiceArticoloFornitore(String codiceArticoloFornitore) {
		this.codiceArticoloFornitore = codiceArticoloFornitore;
	}

	/**
	 * @param codiceEnte
	 *            The codiceEnte to set.
	 */
	public void setCodiceEnte(Integer codiceEnte) {
		this.codiceEnte = codiceEnte;
	}

	/**
	 * @param codiceFornitore
	 *            The codiceFornitore to set.
	 */
	public void setCodiceFornitore(Integer codiceFornitore) {
		this.codiceFornitore = codiceFornitore;
	}

	/**
	 * @param codiceInterlocutore
	 *            The codiceInterlocutore to set.
	 */
	public void setCodiceInterlocutore(Integer codiceInterlocutore) {
		this.codiceInterlocutore = codiceInterlocutore;
	}

	/**
	 * @param colli
	 *            The colli to set.
	 */
	public void setColli(String colli) {
		this.colli = colli;
	}

	/**
	 * @param data
	 *            The data to set.
	 */
	public void setData(Date data) {
		this.data = data;
	}

	/**
	 * @param numero
	 *            The numero to set.
	 */
	public void setNumero(String numero) {
		try {
			Integer intNumero = Integer.parseInt(numero);
			this.numero = intNumero.toString();
		} catch (NumberFormatException e) {
			this.numero = numero;
		}
	}

	/**
	 * @param pezzi
	 *            The pezzi to set.
	 */
	public void setPezzi(String pezzi) {
		try {
			BigDecimal number = new BigDecimal(pezzi);
			// number.setScale(2);
			number = number.divide(Importo.HUNDRED);
			DecimalFormat df = new DecimalFormat("########.00");
			this.pezzi = df.format(number);
		} catch (Exception e) {
			this.pezzi = "";
		}
	}

	/**
	 * @param qta
	 *            The qta to set.
	 */
	public void setQta(double qta) {
		this.qta = qta;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "OrdineUnicomm [codiceFornitore=" + codiceFornitore + ", codiceEnte=" + codiceEnte
				+ ", codiceInterlocutore=" + codiceInterlocutore + ", data=" + data + ", numero=" + numero
				+ ", codiceArticolo=" + codiceArticolo + ", codiceArticoloFornitore=" + codiceArticoloFornitore
				+ ", qta=" + qta + "]";
	}
}
