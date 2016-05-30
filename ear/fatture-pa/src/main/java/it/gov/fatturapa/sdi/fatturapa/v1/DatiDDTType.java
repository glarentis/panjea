package it.gov.fatturapa.sdi.fatturapa.v1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
 * Java class for DatiDDTType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="DatiDDTType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="NumeroDDT" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}String20Type"/>
 *         &lt;element name="DataDDT" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="RiferimentoNumeroLinea" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}RiferimentoNumeroLineaType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DatiDDTType", propOrder = { "numeroDDT", "dataDDT", "riferimentoNumeroLinea" })
public class DatiDDTType implements Serializable {

	private static final long serialVersionUID = -5186621950529229942L;
	@XmlElement(name = "NumeroDDT", required = true)
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	protected String numeroDDT;
	@XmlElement(name = "DataDDT", required = true)
	@XmlSchemaType(name = "date")
	protected XMLGregorianCalendar dataDDT;
	@XmlElement(name = "RiferimentoNumeroLinea", type = Integer.class)
	protected List<Integer> riferimentoNumeroLinea;

	@XmlTransient
	private String id;

	/**
	 * Gets the value of the dataDDT property.
	 *
	 * @return possible object is {@link XMLGregorianCalendar }
	 *
	 */
	public XMLGregorianCalendar getDataDDT() {
		return dataDDT;
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
	 * Gets the value of the numeroDDT property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getNumeroDDT() {
		return numeroDDT;
	}

	/**
	 * Gets the value of the riferimentoNumeroLinea property.
	 *
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
	 * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
	 * the riferimentoNumeroLinea property.
	 *
	 * <p>
	 * For example, to add a new item, do as follows:
	 *
	 * <pre>
	 * getRiferimentoNumeroLinea().add(newItem);
	 * </pre>
	 *
	 *
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link Integer }
	 *
	 * @return riferimento numeri
	 */
	public List<Integer> getRiferimentoNumeroLinea() {
		if (riferimentoNumeroLinea == null) {
			riferimentoNumeroLinea = new ArrayList<Integer>();
		}
		return this.riferimentoNumeroLinea;
	}

	/**
	 * Sets the value of the dataDDT property.
	 *
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 *
	 */
	public void setDataDDT(XMLGregorianCalendar value) {
		this.dataDDT = value;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Sets the value of the numeroDDT property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setNumeroDDT(String value) {
		this.numeroDDT = value;
	}

}
