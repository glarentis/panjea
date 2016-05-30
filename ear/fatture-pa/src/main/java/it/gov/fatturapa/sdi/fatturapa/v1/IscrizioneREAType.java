package it.gov.fatturapa.sdi.fatturapa.v1;

import it.eurotn.panjea.fatturepa.util.FatturazionePAUtils;
import it.gov.fatturapa.sdi.fatturapa.adapter.BigDecimal2Adapter;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * <p>
 * Java class for IscrizioneREAType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="IscrizioneREAType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Ufficio" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}ProvinciaType"/>
 *         &lt;element name="NumeroREA" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}String20Type"/>
 *         &lt;element name="CapitaleSociale" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}Amount2DecimalType" minOccurs="0"/>
 *         &lt;element name="SocioUnico" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}SocioUnicoType" minOccurs="0"/>
 *         &lt;element name="StatoLiquidazione" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}StatoLiquidazioneType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IscrizioneREAType", propOrder = { "ufficio", "numeroREA", "capitaleSociale", "socioUnico",
		"statoLiquidazione" })
public class IscrizioneREAType implements Serializable {

	private static final long serialVersionUID = -8468327367198847706L;
	@XmlElement(name = "Ufficio", required = true)
	protected String ufficio;
	@XmlElement(name = "NumeroREA", required = true)
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	protected String numeroREA;
	@XmlJavaTypeAdapter(BigDecimal2Adapter.class)
	@XmlElement(name = "CapitaleSociale")
	protected BigDecimal capitaleSociale;
	@XmlElement(name = "SocioUnico")
	protected SocioUnicoType socioUnico;
	@XmlElement(name = "StatoLiquidazione", required = true)
	protected StatoLiquidazioneType statoLiquidazione;

	/**
	 * Gets the value of the capitaleSociale property.
	 *
	 * @return possible object is {@link BigDecimal }
	 *
	 */
	public BigDecimal getCapitaleSociale() {
		return capitaleSociale;
	}

	/**
	 * Gets the value of the numeroREA property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getNumeroREA() {
		return numeroREA;
	}

	/**
	 * Gets the value of the socioUnico property.
	 *
	 * @return possible object is {@link SocioUnicoType }
	 *
	 */
	public SocioUnicoType getSocioUnico() {
		return socioUnico;
	}

	/**
	 * Gets the value of the statoLiquidazione property.
	 *
	 * @return possible object is {@link StatoLiquidazioneType }
	 *
	 */
	public StatoLiquidazioneType getStatoLiquidazione() {
		return statoLiquidazione;
	}

	/**
	 * Gets the value of the ufficio property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getUfficio() {
		return ufficio;
	}

	/**
	 * Sets the value of the capitaleSociale property.
	 *
	 * @param value
	 *            allowed object is {@link BigDecimal }
	 *
	 */
	public void setCapitaleSociale(BigDecimal value) {
		this.capitaleSociale = value;
	}

	/**
	 * Sets the value of the numeroREA property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setNumeroREA(String value) {
		this.numeroREA = FatturazionePAUtils.getString(value);
	}

	/**
	 * Sets the value of the socioUnico property.
	 *
	 * @param value
	 *            allowed object is {@link SocioUnicoType }
	 *
	 */
	public void setSocioUnico(SocioUnicoType value) {
		this.socioUnico = value;
	}

	/**
	 * Sets the value of the statoLiquidazione property.
	 *
	 * @param value
	 *            allowed object is {@link StatoLiquidazioneType }
	 *
	 */
	public void setStatoLiquidazione(StatoLiquidazioneType value) {
		this.statoLiquidazione = value;
	}

	/**
	 * Sets the value of the ufficio property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setUfficio(String value) {
		this.ufficio = FatturazionePAUtils.getString(value);
	}

}
