package it.gov.fatturapa.sdi.fatturapa.v1;

import it.gov.fatturapa.sdi.fatturapa.adapter.BigDecimal2Adapter;
import it.gov.fatturapa.sdi.fatturapa.adapter.BigDecimal8Adapter;
import it.gov.fatturapa.sdi.fatturapa.adapter.PercBigDecimal2Adapter;

import java.io.Serializable;
import java.math.BigDecimal;
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
 * Java class for DettaglioLineeType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="DettaglioLineeType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="NumeroLinea" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}NumeroLineaType"/>
 *         &lt;element name="TipoCessionePrestazione" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}TipoCessionePrestazioneType" minOccurs="0"/>
 *         &lt;element name="CodiceArticolo" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}CodiceArticoloType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Descrizione" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}String100LatinType"/>
 *         &lt;element name="Quantita" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}QuantitaType" minOccurs="0"/>
 *         &lt;element name="UnitaMisura" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}String10Type" minOccurs="0"/>
 *         &lt;element name="DataInizioPeriodo" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="DataFinePeriodo" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="PrezzoUnitario" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}Amount8DecimalType"/>
 *         &lt;element name="ScontoMaggiorazione" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}ScontoMaggiorazioneType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="PrezzoTotale" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}Amount8DecimalType"/>
 *         &lt;element name="AliquotaIVA" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}RateType"/>
 *         &lt;element name="Ritenuta" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}RitenutaType" minOccurs="0"/>
 *         &lt;element name="Natura" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}NaturaType" minOccurs="0"/>
 *         &lt;element name="RiferimentoAmministrazione" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}String20Type" minOccurs="0"/>
 *         &lt;element name="AltriDatiGestionali" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}AltriDatiGestionaliType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DettaglioLineeType", propOrder = { "numeroLinea", "tipoCessionePrestazione", "codiceArticolo",
		"descrizione", "quantita", "unitaMisura", "dataInizioPeriodo", "dataFinePeriodo", "prezzoUnitario",
		"scontoMaggiorazione", "prezzoTotale", "aliquotaIVA", "ritenuta", "natura", "riferimentoAmministrazione",
"altriDatiGestionali" })
public class DettaglioLineeType implements Serializable {

	private static final long serialVersionUID = -1677796086584634619L;
	@XmlElement(name = "NumeroLinea")
	protected int numeroLinea;
	@XmlElement(name = "TipoCessionePrestazione")
	protected TipoCessionePrestazioneType tipoCessionePrestazione;
	@XmlElement(name = "CodiceArticolo")
	protected List<CodiceArticoloType> codiceArticolo;
	@XmlElement(name = "Descrizione", required = true)
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	protected String descrizione;

	@XmlJavaTypeAdapter(BigDecimal2Adapter.class)
	@XmlSchemaType(name = "QuantitaType")
	@XmlElement(name = "Quantita")
	protected BigDecimal quantita;
	@XmlElement(name = "UnitaMisura")
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	protected String unitaMisura;
	@XmlElement(name = "DataInizioPeriodo")
	@XmlSchemaType(name = "date")
	protected XMLGregorianCalendar dataInizioPeriodo;
	@XmlElement(name = "DataFinePeriodo")
	@XmlSchemaType(name = "date")
	protected XMLGregorianCalendar dataFinePeriodo;

	@XmlJavaTypeAdapter(BigDecimal8Adapter.class)
	@XmlElement(name = "PrezzoUnitario", required = true)
	protected BigDecimal prezzoUnitario;
	@XmlElement(name = "ScontoMaggiorazione")
	protected List<ScontoMaggiorazioneType> scontoMaggiorazione;

	@XmlJavaTypeAdapter(BigDecimal8Adapter.class)
	@XmlElement(name = "PrezzoTotale", required = true)
	protected BigDecimal prezzoTotale;

	@XmlJavaTypeAdapter(PercBigDecimal2Adapter.class)
	@XmlElement(name = "AliquotaIVA", required = true)
	protected BigDecimal aliquotaIVA;
	@XmlElement(name = "Ritenuta")
	protected RitenutaType ritenuta;
	@XmlElement(name = "Natura")
	protected NaturaType natura;
	@XmlElement(name = "RiferimentoAmministrazione")
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	protected String riferimentoAmministrazione;
	@XmlElement(name = "AltriDatiGestionali")
	protected List<AltriDatiGestionaliType> altriDatiGestionali;

	@XmlTransient
	private String id;

	/**
	 * Gets the value of the aliquotaIVA property.
	 *
	 * @return possible object is {@link BigDecimal }
	 *
	 */
	public BigDecimal getAliquotaIVA() {
		return aliquotaIVA;
	}

	/**
	 * Gets the value of the altriDatiGestionali property.
	 *
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
	 * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
	 * the altriDatiGestionali property.
	 *
	 * <p>
	 * For example, to add a new item, do as follows:
	 *
	 * <pre>
	 * getAltriDatiGestionali().add(newItem);
	 * </pre>
	 *
	 *
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link AltriDatiGestionaliType }
	 *
	 * @return altri dati
	 */
	public List<AltriDatiGestionaliType> getAltriDatiGestionali() {
		if (altriDatiGestionali == null) {
			altriDatiGestionali = new ArrayList<AltriDatiGestionaliType>();
		}
		return this.altriDatiGestionali;
	}

	/**
	 * Gets the value of the codiceArticolo property.
	 *
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
	 * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
	 * the codiceArticolo property.
	 *
	 * <p>
	 * For example, to add a new item, do as follows:
	 *
	 * <pre>
	 * getCodiceArticolo().add(newItem);
	 * </pre>
	 *
	 *
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link CodiceArticoloType }
	 *
	 * @return codice articolo
	 */
	public List<CodiceArticoloType> getCodiceArticolo() {
		if (codiceArticolo == null) {
			codiceArticolo = new ArrayList<CodiceArticoloType>();
		}
		return this.codiceArticolo;
	}

	/**
	 * Gets the value of the dataFinePeriodo property.
	 *
	 * @return possible object is {@link XMLGregorianCalendar }
	 *
	 */
	public XMLGregorianCalendar getDataFinePeriodo() {
		return dataFinePeriodo;
	}

	/**
	 * Gets the value of the dataInizioPeriodo property.
	 *
	 * @return possible object is {@link XMLGregorianCalendar }
	 *
	 */
	public XMLGregorianCalendar getDataInizioPeriodo() {
		return dataInizioPeriodo;
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
	 * @return the id
	 */
	public String getId() {
		if (id == null) {
			id = UUID.randomUUID().toString();
		}
		return id;
	}

	/**
	 * Gets the value of the natura property.
	 *
	 * @return possible object is {@link NaturaType }
	 *
	 */
	public NaturaType getNatura() {
		return natura;
	}

	/**
	 * @return Gets the value of the numeroLinea property.
	 *
	 */
	public int getNumeroLinea() {
		return numeroLinea;
	}

	/**
	 * Gets the value of the prezzoTotale property.
	 *
	 * @return possible object is {@link BigDecimal }
	 *
	 */
	public BigDecimal getPrezzoTotale() {
		return prezzoTotale;
	}

	/**
	 * Gets the value of the prezzoUnitario property.
	 *
	 * @return possible object is {@link BigDecimal }
	 *
	 */
	public BigDecimal getPrezzoUnitario() {
		return prezzoUnitario;
	}

	/**
	 * Gets the value of the quantita property.
	 *
	 * @return possible object is {@link BigDecimal }
	 *
	 */
	public BigDecimal getQuantita() {
		return quantita;
	}

	/**
	 * Gets the value of the riferimentoAmministrazione property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getRiferimentoAmministrazione() {
		return riferimentoAmministrazione;
	}

	/**
	 * Gets the value of the ritenuta property.
	 *
	 * @return possible object is {@link RitenutaType }
	 *
	 */
	public RitenutaType getRitenuta() {
		return ritenuta;
	}

	/**
	 * Gets the value of the scontoMaggiorazione property.
	 *
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
	 * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
	 * the scontoMaggiorazione property.
	 *
	 * <p>
	 * For example, to add a new item, do as follows:
	 *
	 * <pre>
	 * getScontoMaggiorazione().add(newItem);
	 * </pre>
	 *
	 *
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link ScontoMaggiorazioneType }
	 *
	 * @return sconto maggiorazione
	 */
	public List<ScontoMaggiorazioneType> getScontoMaggiorazione() {
		if (scontoMaggiorazione == null) {
			scontoMaggiorazione = new ArrayList<ScontoMaggiorazioneType>();
		}
		return this.scontoMaggiorazione;
	}

	/**
	 * Gets the value of the tipoCessionePrestazione property.
	 *
	 * @return possible object is {@link TipoCessionePrestazioneType }
	 *
	 */
	public TipoCessionePrestazioneType getTipoCessionePrestazione() {
		return tipoCessionePrestazione;
	}

	/**
	 * Gets the value of the unitaMisura property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getUnitaMisura() {
		return unitaMisura;
	}

	/**
	 * Sets the value of the aliquotaIVA property.
	 *
	 * @param value
	 *            allowed object is {@link BigDecimal }
	 *
	 */
	public void setAliquotaIVA(BigDecimal value) {
		this.aliquotaIVA = value;
	}

	/**
	 * Sets the value of the dataFinePeriodo property.
	 *
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 *
	 */
	public void setDataFinePeriodo(XMLGregorianCalendar value) {
		this.dataFinePeriodo = value;
	}

	/**
	 * Sets the value of the dataInizioPeriodo property.
	 *
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 *
	 */
	public void setDataInizioPeriodo(XMLGregorianCalendar value) {
		this.dataInizioPeriodo = value;
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
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Sets the value of the natura property.
	 *
	 * @param value
	 *            allowed object is {@link NaturaType }
	 *
	 */
	public void setNatura(NaturaType value) {
		this.natura = value;
	}

	/**
	 * @param value
	 *            Sets the value of the numeroLinea property.
	 *
	 */
	public void setNumeroLinea(int value) {
		this.numeroLinea = value;
	}

	/**
	 * Sets the value of the prezzoTotale property.
	 *
	 * @param value
	 *            allowed object is {@link BigDecimal }
	 *
	 */
	public void setPrezzoTotale(BigDecimal value) {
		this.prezzoTotale = value;
	}

	/**
	 * Sets the value of the prezzoUnitario property.
	 *
	 * @param value
	 *            allowed object is {@link BigDecimal }
	 *
	 */
	public void setPrezzoUnitario(BigDecimal value) {
		this.prezzoUnitario = value;
	}

	/**
	 * Sets the value of the quantita property.
	 *
	 * @param value
	 *            allowed object is {@link BigDecimal }
	 *
	 */
	public void setQuantita(BigDecimal value) {
		this.quantita = value;
	}

	/**
	 * Sets the value of the riferimentoAmministrazione property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setRiferimentoAmministrazione(String value) {
		this.riferimentoAmministrazione = value;
	}

	/**
	 * Sets the value of the ritenuta property.
	 *
	 * @param value
	 *            allowed object is {@link RitenutaType }
	 *
	 */
	public void setRitenuta(RitenutaType value) {
		this.ritenuta = value;
	}

	/**
	 * Sets the value of the tipoCessionePrestazione property.
	 *
	 * @param value
	 *            allowed object is {@link TipoCessionePrestazioneType }
	 *
	 */
	public void setTipoCessionePrestazione(TipoCessionePrestazioneType value) {
		this.tipoCessionePrestazione = value;
	}

	/**
	 * Sets the value of the unitaMisura property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setUnitaMisura(String value) {
		this.unitaMisura = value;
	}

}
