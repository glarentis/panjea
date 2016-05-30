package it.gov.fatturapa.sdi.fatturapa.v1;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for IdFiscaleType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="IdFiscaleType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="IdPaese" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}NazioneType"/>
 *         &lt;element name="IdCodice" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}CodiceType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IdFiscaleType", propOrder = { "idPaese", "idCodice" })
public class IdFiscaleType implements Serializable {

	private static final long serialVersionUID = -3378669037181113438L;
	@XmlElement(name = "IdPaese", required = true)
	protected String idPaese;
	@XmlElement(name = "IdCodice", required = true)
	protected String idCodice;

	/**
	 * Gets the value of the idCodice property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getIdCodice() {
		return idCodice;
	}

	/**
	 * Gets the value of the idPaese property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getIdPaese() {
		return idPaese;
	}

	/**
	 * Sets the value of the idCodice property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setIdCodice(String value) {
		this.idCodice = value;
	}

	/**
	 * Sets the value of the idPaese property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setIdPaese(String value) {
		this.idPaese = value;
	}

}
