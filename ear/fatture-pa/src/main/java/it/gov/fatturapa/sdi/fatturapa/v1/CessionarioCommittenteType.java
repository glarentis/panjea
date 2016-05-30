package it.gov.fatturapa.sdi.fatturapa.v1;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * Blocco relativo ai dati del Cessionario / Committente
 *
 *
 * <p>
 * Java class for CessionarioCommittenteType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="CessionarioCommittenteType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DatiAnagrafici" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}DatiAnagraficiCessionarioType"/>
 *         &lt;element name="Sede" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}IndirizzoType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CessionarioCommittenteType", propOrder = { "datiAnagrafici", "sede" })
public class CessionarioCommittenteType implements Serializable {

	private static final long serialVersionUID = 1535317176962570156L;
	@XmlElement(name = "DatiAnagrafici", required = true)
	protected DatiAnagraficiCessionarioType datiAnagrafici;
	@XmlElement(name = "Sede", required = true)
	protected IndirizzoType sede;

	/**
	 * Gets the value of the datiAnagrafici property.
	 *
	 * @return possible object is {@link DatiAnagraficiCessionarioType }
	 *
	 */
	public DatiAnagraficiCessionarioType getDatiAnagrafici() {
		return datiAnagrafici;
	}

	/**
	 * Gets the value of the sede property.
	 *
	 * @return possible object is {@link IndirizzoType }
	 *
	 */
	public IndirizzoType getSede() {
		return sede;
	}

	/**
	 * Sets the value of the datiAnagrafici property.
	 *
	 * @param value
	 *            allowed object is {@link DatiAnagraficiCessionarioType }
	 *
	 */
	public void setDatiAnagrafici(DatiAnagraficiCessionarioType value) {
		this.datiAnagrafici = value;
	}

	/**
	 * Sets the value of the sede property.
	 *
	 * @param value
	 *            allowed object is {@link IndirizzoType }
	 *
	 */
	public void setSede(IndirizzoType value) {
		this.sede = value;
	}

}
