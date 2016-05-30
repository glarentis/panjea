package it.gov.fatturapa.sdi.fatturapa.v1;

import java.io.Serializable;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * <p>
 * Java class for CodiceArticoloType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="CodiceArticoloType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CodiceTipo" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}String35Type"/>
 *         &lt;element name="CodiceValore" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}String35Type"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CodiceArticoloType", propOrder = { "codiceTipo", "codiceValore" })
public class CodiceArticoloType implements Serializable {

	private static final long serialVersionUID = -2789387394131561316L;
	@XmlElement(name = "CodiceTipo", required = true)
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	protected String codiceTipo;
	@XmlElement(name = "CodiceValore", required = true)
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	protected String codiceValore;

	@XmlTransient
	private String id;

	/**
	 * Gets the value of the codiceTipo property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getCodiceTipo() {
		return codiceTipo;
	}

	/**
	 * Gets the value of the codiceValore property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getCodiceValore() {
		return codiceValore;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		if (id == null) {
			id = UUID.randomUUID().toString();
		}
		return id;
	}

	/**
	 * Sets the value of the codiceTipo property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setCodiceTipo(String value) {
		this.codiceTipo = value;
	}

	/**
	 * Sets the value of the codiceValore property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setCodiceValore(String value) {
		this.codiceValore = value;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

}
