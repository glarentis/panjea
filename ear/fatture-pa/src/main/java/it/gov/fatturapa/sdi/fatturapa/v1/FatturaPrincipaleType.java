package it.gov.fatturapa.sdi.fatturapa.v1;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <p>
 * Java class for FatturaPrincipaleType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="FatturaPrincipaleType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="NumeroFatturaPrincipale" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}String20Type"/>
 *         &lt;element name="DataFatturaPrincipale" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FatturaPrincipaleType", propOrder = { "numeroFatturaPrincipale", "dataFatturaPrincipale" })
public class FatturaPrincipaleType implements Serializable {

	private static final long serialVersionUID = -4424910877386334204L;
	@XmlElement(name = "NumeroFatturaPrincipale", required = true)
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	protected String numeroFatturaPrincipale;
	@XmlElement(name = "DataFatturaPrincipale", required = true)
	@XmlSchemaType(name = "date")
	protected XMLGregorianCalendar dataFatturaPrincipale;

	/**
	 * Gets the value of the dataFatturaPrincipale property.
	 *
	 * @return possible object is {@link XMLGregorianCalendar }
	 *
	 */
	public XMLGregorianCalendar getDataFatturaPrincipale() {
		return dataFatturaPrincipale;
	}

	/**
	 * Gets the value of the numeroFatturaPrincipale property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getNumeroFatturaPrincipale() {
		return numeroFatturaPrincipale;
	}

	/**
	 * Sets the value of the dataFatturaPrincipale property.
	 *
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 *
	 */
	public void setDataFatturaPrincipale(XMLGregorianCalendar value) {
		this.dataFatturaPrincipale = value;
	}

	/**
	 * Sets the value of the numeroFatturaPrincipale property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setNumeroFatturaPrincipale(String value) {
		this.numeroFatturaPrincipale = value;
	}

}
