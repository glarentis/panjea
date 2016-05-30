package it.gov.fatturapa.sdi.fatturapa.v1;

import it.gov.fatturapa.sdi.fatturapa.adapter.BigDecimal2Adapter;

import java.io.Serializable;
import java.math.BigDecimal;

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
 * Java class for DatiTrasportoType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="DatiTrasportoType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DatiAnagraficiVettore" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}DatiAnagraficiVettoreType" minOccurs="0"/>
 *         &lt;element name="MezzoTrasporto" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}String80LatinType" minOccurs="0"/>
 *         &lt;element name="CausaleTrasporto" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}String100LatinType" minOccurs="0"/>
 *         &lt;element name="NumeroColli" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}NumeroColliType" minOccurs="0"/>
 *         &lt;element name="Descrizione" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}String100LatinType" minOccurs="0"/>
 *         &lt;element name="UnitaMisuraPeso" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}String10Type" minOccurs="0"/>
 *         &lt;element name="PesoLordo" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}PesoType" minOccurs="0"/>
 *         &lt;element name="PesoNetto" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}PesoType" minOccurs="0"/>
 *         &lt;element name="DataOraRitiro" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="DataInizioTrasporto" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="TipoResa" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}TipoResaType" minOccurs="0"/>
 *         &lt;element name="IndirizzoResa" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}IndirizzoType" minOccurs="0"/>
 *         &lt;element name="DataOraConsegna" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DatiTrasportoType", propOrder = { "datiAnagraficiVettore", "mezzoTrasporto", "causaleTrasporto",
		"numeroColli", "descrizione", "unitaMisuraPeso", "pesoLordo", "pesoNetto", "dataOraRitiro",
		"dataInizioTrasporto", "tipoResa", "indirizzoResa", "dataOraConsegna" })
public class DatiTrasportoType implements Serializable {

	private static final long serialVersionUID = -478921349199997066L;
	@XmlElement(name = "DatiAnagraficiVettore")
	protected DatiAnagraficiVettoreType datiAnagraficiVettore;
	@XmlElement(name = "MezzoTrasporto")
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	protected String mezzoTrasporto;
	@XmlElement(name = "CausaleTrasporto")
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	protected String causaleTrasporto;
	@XmlElement(name = "NumeroColli")
	protected Integer numeroColli;
	@XmlElement(name = "Descrizione")
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	protected String descrizione;
	@XmlElement(name = "UnitaMisuraPeso")
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	protected String unitaMisuraPeso;
	@XmlJavaTypeAdapter(BigDecimal2Adapter.class)
	@XmlElement(name = "PesoLordo")
	protected BigDecimal pesoLordo;
	@XmlJavaTypeAdapter(BigDecimal2Adapter.class)
	@XmlElement(name = "PesoNetto")
	protected BigDecimal pesoNetto;
	@XmlElement(name = "DataOraRitiro")
	@XmlSchemaType(name = "dateTime")
	protected XMLGregorianCalendar dataOraRitiro;
	@XmlElement(name = "DataInizioTrasporto")
	@XmlSchemaType(name = "date")
	protected XMLGregorianCalendar dataInizioTrasporto;
	@XmlElement(name = "TipoResa")
	protected String tipoResa;
	@XmlElement(name = "IndirizzoResa")
	protected IndirizzoType indirizzoResa;
	@XmlElement(name = "DataOraConsegna")
	@XmlSchemaType(name = "dateTime")
	protected XMLGregorianCalendar dataOraConsegna;

	/**
	 * Gets the value of the causaleTrasporto property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getCausaleTrasporto() {
		return causaleTrasporto;
	}

	/**
	 * Gets the value of the dataInizioTrasporto property.
	 *
	 * @return possible object is {@link XMLGregorianCalendar }
	 *
	 */
	public XMLGregorianCalendar getDataInizioTrasporto() {
		return dataInizioTrasporto;
	}

	/**
	 * Gets the value of the dataOraConsegna property.
	 *
	 * @return possible object is {@link XMLGregorianCalendar }
	 *
	 */
	public XMLGregorianCalendar getDataOraConsegna() {
		return dataOraConsegna;
	}

	/**
	 * Gets the value of the dataOraRitiro property.
	 *
	 * @return possible object is {@link XMLGregorianCalendar }
	 *
	 */
	public XMLGregorianCalendar getDataOraRitiro() {
		return dataOraRitiro;
	}

	/**
	 * Gets the value of the datiAnagraficiVettore property.
	 *
	 * @return possible object is {@link DatiAnagraficiVettoreType }
	 *
	 */
	public DatiAnagraficiVettoreType getDatiAnagraficiVettore() {
		return datiAnagraficiVettore;
	}

	/**
	 * Gets the value of the descrizione property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * Gets the value of the indirizzoResa property.
	 *
	 * @return possible object is {@link IndirizzoType }
	 *
	 */
	public IndirizzoType getIndirizzoResa() {
		return indirizzoResa;
	}

	/**
	 * Gets the value of the mezzoTrasporto property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getMezzoTrasporto() {
		return mezzoTrasporto;
	}

	/**
	 * Gets the value of the numeroColli property.
	 *
	 * @return possible object is {@link Integer }
	 *
	 */
	public Integer getNumeroColli() {
		return numeroColli;
	}

	/**
	 * Gets the value of the pesoLordo property.
	 *
	 * @return possible object is {@link BigDecimal }
	 *
	 */
	public BigDecimal getPesoLordo() {
		return pesoLordo;
	}

	/**
	 * Gets the value of the pesoNetto property.
	 *
	 * @return possible object is {@link BigDecimal }
	 *
	 */
	public BigDecimal getPesoNetto() {
		return pesoNetto;
	}

	/**
	 * Gets the value of the tipoResa property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getTipoResa() {
		return tipoResa;
	}

	/**
	 * Gets the value of the unitaMisuraPeso property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getUnitaMisuraPeso() {
		return unitaMisuraPeso;
	}

	/**
	 * Sets the value of the causaleTrasporto property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setCausaleTrasporto(String value) {
		this.causaleTrasporto = value;
	}

	/**
	 * Sets the value of the dataInizioTrasporto property.
	 *
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 *
	 */
	public void setDataInizioTrasporto(XMLGregorianCalendar value) {
		this.dataInizioTrasporto = value;
	}

	/**
	 * Sets the value of the dataOraConsegna property.
	 *
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 *
	 */
	public void setDataOraConsegna(XMLGregorianCalendar value) {
		this.dataOraConsegna = value;
	}

	/**
	 * Sets the value of the dataOraRitiro property.
	 *
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 *
	 */
	public void setDataOraRitiro(XMLGregorianCalendar value) {
		this.dataOraRitiro = value;
	}

	/**
	 * Sets the value of the datiAnagraficiVettore property.
	 *
	 * @param value
	 *            allowed object is {@link DatiAnagraficiVettoreType }
	 *
	 */
	public void setDatiAnagraficiVettore(DatiAnagraficiVettoreType value) {
		this.datiAnagraficiVettore = value;
	}

	/**
	 * Sets the value of the descrizione property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setDescrizione(String value) {
		this.descrizione = value;
	}

	/**
	 * Sets the value of the indirizzoResa property.
	 *
	 * @param value
	 *            allowed object is {@link IndirizzoType }
	 *
	 */
	public void setIndirizzoResa(IndirizzoType value) {
		this.indirizzoResa = value;
	}

	/**
	 * Sets the value of the mezzoTrasporto property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setMezzoTrasporto(String value) {
		this.mezzoTrasporto = value;
	}

	/**
	 * Sets the value of the numeroColli property.
	 *
	 * @param value
	 *            allowed object is {@link Integer }
	 *
	 */
	public void setNumeroColli(Integer value) {
		this.numeroColli = value;
	}

	/**
	 * Sets the value of the pesoLordo property.
	 *
	 * @param value
	 *            allowed object is {@link BigDecimal }
	 *
	 */
	public void setPesoLordo(BigDecimal value) {
		this.pesoLordo = value;
	}

	/**
	 * Sets the value of the pesoNetto property.
	 *
	 * @param value
	 *            allowed object is {@link BigDecimal }
	 *
	 */
	public void setPesoNetto(BigDecimal value) {
		this.pesoNetto = value;
	}

	/**
	 * Sets the value of the tipoResa property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setTipoResa(String value) {
		this.tipoResa = value;
	}

	/**
	 * Sets the value of the unitaMisuraPeso property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setUnitaMisuraPeso(String value) {
		this.unitaMisuraPeso = value;
	}

}
