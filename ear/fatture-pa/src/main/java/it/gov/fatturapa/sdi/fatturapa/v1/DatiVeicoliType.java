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
 *
 * Blocco relativo ai dati dei Veicoli della Fattura Elettronica (da indicare nei casi di cessioni tra Paesi membri di
 * mezzi di trasporto nuovi, in base all'art. 38, comma 4 del dl 331 del 1993)
 *
 *
 * <p>
 * Java class for DatiVeicoliType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="DatiVeicoliType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Data" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="TotalePercorso" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}String15Type"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DatiVeicoliType", propOrder = { "data", "totalePercorso" })
public class DatiVeicoliType implements Serializable {

	private static final long serialVersionUID = -4855961631533338921L;
	@XmlElement(name = "Data", required = true)
	@XmlSchemaType(name = "date")
	protected XMLGregorianCalendar data;
	@XmlElement(name = "TotalePercorso", required = true)
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	protected String totalePercorso;

	/**
	 * Gets the value of the data property.
	 *
	 * @return possible object is {@link XMLGregorianCalendar }
	 *
	 */
	public XMLGregorianCalendar getData() {
		return data;
	}

	/**
	 * Gets the value of the totalePercorso property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getTotalePercorso() {
		return totalePercorso;
	}

	/**
	 * Sets the value of the data property.
	 *
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 *
	 */
	public void setData(XMLGregorianCalendar value) {
		this.data = value;
	}

	/**
	 * Sets the value of the totalePercorso property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setTotalePercorso(String value) {
		this.totalePercorso = value;
	}

}
