package it.gov.fatturapa.sdi.fatturapa.v1;

import it.eurotn.panjea.fatturepa.util.FatturazionePAUtils;

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
 * Java class for DatiDocumentiCorrelatiType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="DatiDocumentiCorrelatiType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RiferimentoNumeroLinea" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}RiferimentoNumeroLineaType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="IdDocumento" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}String20Type"/>
 *         &lt;element name="Data" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="NumItem" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}String20Type" minOccurs="0"/>
 *         &lt;element name="CodiceCommessaConvenzione" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}String100LatinType" minOccurs="0"/>
 *         &lt;element name="CodiceCUP" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}String15Type" minOccurs="0"/>
 *         &lt;element name="CodiceCIG" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}String15Type" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DatiDocumentiCorrelatiType", propOrder = { "riferimentoNumeroLinea", "idDocumento", "data", "numItem",
		"codiceCommessaConvenzione", "codiceCUP", "codiceCIG" })
public class DatiDocumentiCorrelatiType implements Serializable {

	private static final long serialVersionUID = -7678972213013793699L;
	@XmlElement(name = "RiferimentoNumeroLinea", type = Integer.class)
	protected List<Integer> riferimentoNumeroLinea;
	@XmlElement(name = "IdDocumento", required = true)
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	protected String idDocumento;
	@XmlElement(name = "Data")
	@XmlSchemaType(name = "date")
	protected XMLGregorianCalendar data;
	@XmlElement(name = "NumItem")
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	protected String numItem;
	@XmlElement(name = "CodiceCommessaConvenzione")
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	protected String codiceCommessaConvenzione;
	@XmlElement(name = "CodiceCUP")
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	protected String codiceCUP;
	@XmlElement(name = "CodiceCIG")
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	protected String codiceCIG;

	@XmlTransient
	private String id;

	/**
	 * Gets the value of the codiceCIG property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getCodiceCIG() {
		return codiceCIG;
	}

	/**
	 * Gets the value of the codiceCommessaConvenzione property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getCodiceCommessaConvenzione() {
		return codiceCommessaConvenzione;
	}

	/**
	 * Gets the value of the codiceCUP property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getCodiceCUP() {
		return codiceCUP;
	}

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
	 * @return the id
	 */
	public String getId() {
		if (id == null) {
			id = UUID.randomUUID().toString();
		}
		return id;
	}

	/**
	 * Gets the value of the idDocumento property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getIdDocumento() {
		return idDocumento;
	}

	/**
	 * Gets the value of the numItem property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getNumItem() {
		return numItem;
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
	 * Sets the value of the codiceCIG property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setCodiceCIG(String value) {
		this.codiceCIG = FatturazionePAUtils.getString(value);
	}

	/**
	 * Sets the value of the codiceCommessaConvenzione property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setCodiceCommessaConvenzione(String value) {
		this.codiceCommessaConvenzione = FatturazionePAUtils.getString(value);
	}

	/**
	 * Sets the value of the codiceCUP property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setCodiceCUP(String value) {
		this.codiceCUP = FatturazionePAUtils.getString(value);
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
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Sets the value of the idDocumento property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setIdDocumento(String value) {
		this.idDocumento = FatturazionePAUtils.getString(value);
	}

	/**
	 * Sets the value of the numItem property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setNumItem(String value) {
		this.numItem = FatturazionePAUtils.getString(value);
	}

	/**
	 * @param riferimentoNumeroLinea
	 *            the riferimentoNumeroLinea to set
	 */
	public void setRiferimentoNumeroLinea(List<Integer> riferimentoNumeroLinea) {
		this.riferimentoNumeroLinea = riferimentoNumeroLinea;
	}

}
