package it.eurotn.panjea.cosaro.rendicontazione;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.ObjectUtils;

public class CoopTracciabilitaRigaRendicontazioneBeanExporter {

	private String codiceFornitoreCoop;

	private String numeroBolla;
	private String dataBolla;

	private String codiceArticoloCoop;
	private String barCodeCoop;
	private String descrizioneArticolo;

	private Integer colli;
	private Integer pezzi;
	private Double quantita;

	private String lotto;

	/**
	 * Costruttore.
	 */
	public CoopTracciabilitaRigaRendicontazioneBeanExporter() {
		super();
	}

	/**
	 * @return the barCodeCoop
	 */
	public String getBarCodeCoop() {
		return barCodeCoop;
	}

	/**
	 * @return the codiceArticoloCoop
	 */
	public String getCodiceArticoloCoop() {
		return codiceArticoloCoop;
	}

	/**
	 * @return the codiceFornitoreCoop
	 */
	public String getCodiceFornitoreCoop() {
		return codiceFornitoreCoop;
	}

	/**
	 * @return the colli
	 */
	public Integer getColli() {
		return colli;
	}

	/**
	 * @return the dataBolla
	 */
	public String getDataBolla() {
		return dataBolla;
	}

	/**
	 * @return the dataBolla
	 */
	public Date getDataBollaFormatted() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
		try {
			return dateFormat.parse(dataBolla);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * @return the descrizioneArticolo
	 */
	public String getDescrizioneArticolo() {
		return descrizioneArticolo;
	}

	/**
	 * @return the lotto
	 */
	public String getLotto() {
		return lotto;
	}

	/**
	 * @return the numeroBolla
	 */
	public String getNumeroBolla() {
		return numeroBolla;
	}

	/**
	 * @return the pezzi
	 */
	public Integer getPezzi() {
		return pezzi;
	}

	/**
	 * @return the quantita
	 */
	public Double getQuantita() {
		return quantita;
	}

	/**
	 * @return the quantita
	 */
	public Integer getQuantitaFormatted() {
		BigDecimal qtaDecimal = BigDecimal.valueOf(ObjectUtils.defaultIfNull(quantita, 0.0));
		qtaDecimal = qtaDecimal.multiply(new BigDecimal(1000));
		return qtaDecimal.intValue();
	}

	/**
	 * @param barCodeCoop
	 *            the barCodeCoop to set
	 */
	public void setBarCodeCoop(String barCodeCoop) {
		this.barCodeCoop = barCodeCoop;
	}

	/**
	 * @param codiceArticoloCoop
	 *            the codiceArticoloCoop to set
	 */
	public void setCodiceArticoloCoop(String codiceArticoloCoop) {
		this.codiceArticoloCoop = codiceArticoloCoop;
	}

	/**
	 * @param codiceFornitoreCoop
	 *            the codiceFornitoreCoop to set
	 */
	public void setCodiceFornitoreCoop(String codiceFornitoreCoop) {
		this.codiceFornitoreCoop = codiceFornitoreCoop;
	}

	/**
	 * @param colli
	 *            the colli to set
	 */
	public void setColli(Integer colli) {
		this.colli = colli;
	}

	/**
	 * @param dataBolla
	 *            the dataBolla to set
	 */
	public void setDataBolla(String dataBolla) {
		this.dataBolla = dataBolla;
	}

	/**
	 * @param dataBollaFormatted
	 *            the dataBollaFormatted to set
	 */
	public void setDataBollaFormatted(Date dataBollaFormatted) {
		// creato perchè il writer del BeanIO richiede il setter
	}

	/**
	 * @param descrizioneArticolo
	 *            the descrizioneArticolo to set
	 */
	public void setDescrizioneArticolo(String descrizioneArticolo) {
		this.descrizioneArticolo = descrizioneArticolo;
	}

	/**
	 * @param lotto
	 *            the lotto to set
	 */
	public void setLotto(String lotto) {
		this.lotto = lotto;
	}

	/**
	 * @param numeroBolla
	 *            the numeroBolla to set
	 */
	public void setNumeroBolla(String numeroBolla) {
		this.numeroBolla = numeroBolla;
	}

	/**
	 * @param pezzi
	 *            the pezzi to set
	 */
	public void setPezzi(Integer pezzi) {
		this.pezzi = pezzi;
	}

	/**
	 * @param quantita
	 *            the quantita to set
	 */
	public void setQuantita(Double quantita) {
		this.quantita = quantita;
	}

	/**
	 * @param quantitaFormatted
	 *            the quantita to set
	 */
	public void setQuantitaFormatted(Integer quantitaFormatted) {
		// creato perchè il writer del BeanIO richiede il setter
	}

}
