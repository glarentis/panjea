package it.gov.fatturapa.sdi.fatturapa.v1;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * Blocco relativo ai dati del Terzo Intermediario che emette fattura elettronica per conto del Cedente/Prestatore
 *
 *
 * <p>
 * Java class for TerzoIntermediarioSoggettoEmittenteType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="TerzoIntermediarioSoggettoEmittenteType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DatiAnagrafici" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}DatiAnagraficiTerzoIntermediarioType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TerzoIntermediarioSoggettoEmittenteType", propOrder = { "datiAnagrafici" })
public class TerzoIntermediarioSoggettoEmittenteType implements Serializable {

	private static final long serialVersionUID = -2748060738467477437L;
	@XmlElement(name = "DatiAnagrafici", required = true)
	protected DatiAnagraficiTerzoIntermediarioType datiAnagrafici;

	/**
	 * Gets the value of the datiAnagrafici property.
	 *
	 * @return possible object is {@link DatiAnagraficiTerzoIntermediarioType }
	 *
	 */
	public DatiAnagraficiTerzoIntermediarioType getDatiAnagrafici() {
		return datiAnagrafici;
	}

	/**
	 * Sets the value of the datiAnagrafici property.
	 *
	 * @param value
	 *            allowed object is {@link DatiAnagraficiTerzoIntermediarioType }
	 *
	 */
	public void setDatiAnagrafici(DatiAnagraficiTerzoIntermediarioType value) {
		this.datiAnagrafici = value;
	}

}
