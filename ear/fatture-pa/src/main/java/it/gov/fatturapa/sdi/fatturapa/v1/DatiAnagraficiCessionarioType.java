package it.gov.fatturapa.sdi.fatturapa.v1;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import it.eurotn.panjea.fatturepa.util.FatturazionePAUtils;

/**
 * <p>
 * Java class for DatiAnagraficiCessionarioType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="DatiAnagraficiCessionarioType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="IdFiscaleIVA" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}IdFiscaleType" minOccurs="0"/>
 *         &lt;element name="CodiceFiscale" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}CodiceFiscaleType" minOccurs="0"/>
 *         &lt;element name="Anagrafica" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}AnagraficaType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DatiAnagraficiCessionarioType", propOrder = { "idFiscaleIVA", "codiceFiscale", "anagrafica" })
public class DatiAnagraficiCessionarioType implements Serializable {

	private static final long serialVersionUID = -261030746078357836L;
	@XmlElement(name = "IdFiscaleIVA")
	protected IdFiscaleType idFiscaleIVA;
	@XmlElement(name = "CodiceFiscale")
	protected String codiceFiscale;
	@XmlElement(name = "Anagrafica", required = true)
	protected AnagraficaType anagrafica;

	{
		idFiscaleIVA = new IdFiscaleType();
	}

	/**
	 * Gets the value of the anagrafica property.
	 *
	 * @return possible object is {@link AnagraficaType }
	 *
	 */
	public AnagraficaType getAnagrafica() {
		return anagrafica;
	}

	/**
	 * Gets the value of the codiceFiscale property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	/**
	 * Gets the value of the idFiscaleIVA property.
	 *
	 * @return possible object is {@link IdFiscaleType }
	 *
	 */
	public IdFiscaleType getIdFiscaleIVA() {
		return idFiscaleIVA;
	}

	/**
	 * Sets the value of the anagrafica property.
	 *
	 * @param value
	 *            allowed object is {@link AnagraficaType }
	 *
	 */
	public void setAnagrafica(AnagraficaType value) {
		this.anagrafica = value;
	}

	/**
	 * Sets the value of the codiceFiscale property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setCodiceFiscale(String value) {
		this.codiceFiscale = FatturazionePAUtils.getString(value);
	}

	/**
	 * Sets the value of the idFiscaleIVA property.
	 *
	 * @param value
	 *            allowed object is {@link IdFiscaleType }
	 *
	 */
	public void setIdFiscaleIVA(IdFiscaleType value) {
		this.idFiscaleIVA = value;
	}

}
