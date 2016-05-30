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
 * Java class for ContattiType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="ContattiType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Telefono" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}TelFaxType" minOccurs="0"/>
 *         &lt;element name="Fax" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}TelFaxType" minOccurs="0"/>
 *         &lt;element name="Email" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}EmailType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ContattiType", propOrder = { "telefono", "fax", "email" })
public class ContattiType implements Serializable {

	private static final long serialVersionUID = 2677934167204217503L;
	@XmlElement(name = "Telefono")
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	protected String telefono;
	@XmlElement(name = "Fax")
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	protected String fax;
	@XmlElement(name = "Email")
	protected String email;

	/**
	 * Gets the value of the email property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Gets the value of the fax property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getFax() {
		return fax;
	}

	/**
	 * Gets the value of the telefono property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getTelefono() {
		return telefono;
	}

	/**
	 * Sets the value of the email property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setEmail(String value) {
		this.email = FatturazionePAUtils.getString(value);
	}

	/**
	 * Sets the value of the fax property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setFax(String value) {
		this.fax = FatturazionePAUtils.getString(value);
	}

	/**
	 * Sets the value of the telefono property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setTelefono(String value) {
		this.telefono = FatturazionePAUtils.getString(value);
	}

}
