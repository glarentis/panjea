package it.gov.fatturapa.sdi.fatturapa.v1;

import it.eurotn.panjea.fatturepa.util.FatturazionePAUtils;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * <p>
 * Java class for IndirizzoType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="IndirizzoType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Indirizzo" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}String60LatinType"/>
 *         &lt;element name="NumeroCivico" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}NumeroCivicoType" minOccurs="0"/>
 *         &lt;element name="CAP" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}CAPType"/>
 *         &lt;element name="Comune" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}String60LatinType"/>
 *         &lt;element name="Provincia" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}ProvinciaType" minOccurs="0"/>
 *         &lt;element name="Nazione" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}NazioneType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IndirizzoType", propOrder = { "indirizzo", "numeroCivico", "cap", "comune", "provincia", "nazione" })
public class IndirizzoType implements Serializable {

	private static final long serialVersionUID = 7007863791214702574L;
	@XmlElement(name = "Indirizzo", required = true)
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	protected String indirizzo;
	@XmlElement(name = "NumeroCivico")
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	protected String numeroCivico;
	@XmlElement(name = "CAP", required = true)
	protected String cap;
	@XmlElement(name = "Comune", required = true)
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	protected String comune;
	@XmlElement(name = "Provincia")
	protected String provincia;
	@XmlElement(name = "Nazione", required = true, defaultValue = "IT")
	protected String nazione;

	/**
	 * Gets the value of the cap property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getCap() {
		return cap;
	}

	/**
	 * Gets the value of the comune property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getComune() {
		return comune;
	}

	/**
	 * Gets the value of the indirizzo property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getIndirizzo() {
		return indirizzo;
	}

	/**
	 * Gets the value of the nazione property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getNazione() {
		return nazione;
	}

	/**
	 * Gets the value of the numeroCivico property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getNumeroCivico() {
		return numeroCivico;
	}

	/**
	 * Gets the value of the provincia property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getProvincia() {
		return provincia;
	}

	/**
	 * Sets the value of the cap property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setCap(String value) {
		this.cap = FatturazionePAUtils.getString(value);
	}

	/**
	 * Sets the value of the comune property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setComune(String value) {
		this.comune = FatturazionePAUtils.getString(value);
	}

	/**
	 * Sets the value of the indirizzo property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setIndirizzo(String value) {
		this.indirizzo = FatturazionePAUtils.getString(value);
	}

	/**
	 * Sets the value of the nazione property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setNazione(String value) {
		this.nazione = FatturazionePAUtils.getString(value);
	}

	/**
	 * Sets the value of the numeroCivico property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setNumeroCivico(String value) {
		this.numeroCivico = FatturazionePAUtils.getString(value);
	}

	/**
	 * Sets the value of the provincia property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setProvincia(String value) {
		this.provincia = FatturazionePAUtils.getString(value);
	}

}
