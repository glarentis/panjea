package it.gov.fatturapa.sdi.fatturapa.v1;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * Blocco relativo ai dati del Rappresentante Fiscale
 *
 *
 * <p>
 * Java class for RappresentanteFiscaleType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="RappresentanteFiscaleType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DatiAnagrafici" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}DatiAnagraficiRappresentanteType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RappresentanteFiscaleType", propOrder = { "datiAnagrafici" })
public class RappresentanteFiscaleType implements Serializable {

	private static final long serialVersionUID = 8296863916959331760L;
	@XmlElement(name = "DatiAnagrafici", required = true)
	protected DatiAnagraficiRappresentanteType datiAnagrafici;

	/**
	 * Gets the value of the datiAnagrafici property.
	 *
	 * @return possible object is {@link DatiAnagraficiRappresentanteType }
	 *
	 */
	public DatiAnagraficiRappresentanteType getDatiAnagrafici() {
		return datiAnagrafici;
	}

	/**
	 * Sets the value of the datiAnagrafici property.
	 *
	 * @param value
	 *            allowed object is {@link DatiAnagraficiRappresentanteType }
	 *
	 */
	public void setDatiAnagrafici(DatiAnagraficiRappresentanteType value) {
		this.datiAnagrafici = value;
	}

}
