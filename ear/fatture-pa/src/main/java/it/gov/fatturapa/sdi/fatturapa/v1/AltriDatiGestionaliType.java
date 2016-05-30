package it.gov.fatturapa.sdi.fatturapa.v1;

import it.gov.fatturapa.sdi.fatturapa.adapter.BigDecimal8Adapter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <p>
 * Java class for AltriDatiGestionaliType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="AltriDatiGestionaliType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TipoDato" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}String10Type"/>
 *         &lt;element name="RiferimentoTesto" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}String60LatinType" minOccurs="0"/>
 *         &lt;element name="RiferimentoNumero" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}Amount8DecimalType" minOccurs="0"/>
 *         &lt;element name="RiferimentoData" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AltriDatiGestionaliType", propOrder = { "tipoDato", "riferimentoTesto", "riferimentoNumero",
		"riferimentoData" })
public class AltriDatiGestionaliType implements Serializable {

	private static final long serialVersionUID = 9082871783485069345L;
	@XmlElement(name = "TipoDato", required = true)
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	protected String tipoDato;
	@XmlElement(name = "RiferimentoTesto")
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	protected String riferimentoTesto;

	@XmlJavaTypeAdapter(BigDecimal8Adapter.class)
	@XmlElement(name = "RiferimentoNumero")
	protected BigDecimal riferimentoNumero;
	@XmlElement(name = "RiferimentoData")
	@XmlSchemaType(name = "date")
	protected XMLGregorianCalendar riferimentoData;

	@XmlTransient
	private String id;

	/**
	 * @return id
	 */
	public String getId() {
		if (id == null) {
			id = UUID.randomUUID().toString();
		}
		return id;
	}

	/**
	 * Gets the value of the riferimentoData property.
	 *
	 * @return possible object is {@link XMLGregorianCalendar }
	 *
	 */
	public XMLGregorianCalendar getRiferimentoData() {
		return riferimentoData;
	}

	/**
	 * Gets the value of the riferimentoNumero property.
	 *
	 * @return possible object is {@link BigDecimal }
	 *
	 */
	public BigDecimal getRiferimentoNumero() {
		return riferimentoNumero;
	}

	/**
	 * Gets the value of the riferimentoTesto property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getRiferimentoTesto() {
		return riferimentoTesto;
	}

	/**
	 * Gets the value of the tipoDato property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getTipoDato() {
		return tipoDato;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Sets the value of the riferimentoData property.
	 *
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 *
	 */
	public void setRiferimentoData(XMLGregorianCalendar value) {
		this.riferimentoData = value;
	}

	/**
	 * Sets the value of the riferimentoNumero property.
	 *
	 * @param value
	 *            allowed object is {@link BigDecimal }
	 *
	 */
	public void setRiferimentoNumero(BigDecimal value) {
		this.riferimentoNumero = value;
	}

	/**
	 * Sets the value of the riferimentoTesto property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setRiferimentoTesto(String value) {
		this.riferimentoTesto = value;
	}

	/**
	 * Sets the value of the tipoDato property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setTipoDato(String value) {
		this.tipoDato = value;
	}
}
