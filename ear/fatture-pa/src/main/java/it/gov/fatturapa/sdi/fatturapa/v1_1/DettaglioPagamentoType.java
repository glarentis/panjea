package it.gov.fatturapa.sdi.fatturapa.v1_1;

import it.gov.fatturapa.sdi.fatturapa.adapter.BigDecimal2Adapter;

import java.io.Serializable;
import java.math.BigDecimal;
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
 * Java class for DettaglioPagamentoType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="DettaglioPagamentoType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Beneficiario" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}String200LatinType" minOccurs="0"/>
 *         &lt;element name="ModalitaPagamento" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}ModalitaPagamentoType"/>
 *         &lt;element name="DataRiferimentoTerminiPagamento" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="GiorniTerminiPagamento" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}GiorniTerminePagamentoType" minOccurs="0"/>
 *         &lt;element name="DataScadenzaPagamento" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="ImportoPagamento" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}Amount2DecimalType"/>
 *         &lt;element name="CodUfficioPostale" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}String20Type" minOccurs="0"/>
 *         &lt;element name="CognomeQuietanzante" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}String60LatinType" minOccurs="0"/>
 *         &lt;element name="NomeQuietanzante" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}String60LatinType" minOccurs="0"/>
 *         &lt;element name="CFQuietanzante" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}CodiceFiscalePFType" minOccurs="0"/>
 *         &lt;element name="TitoloQuietanzante" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}TitoloType" minOccurs="0"/>
 *         &lt;element name="IstitutoFinanziario" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}String80LatinType" minOccurs="0"/>
 *         &lt;element name="IBAN" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}IBANType" minOccurs="0"/>
 *         &lt;element name="ABI" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}ABIType" minOccurs="0"/>
 *         &lt;element name="CAB" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}CABType" minOccurs="0"/>
 *         &lt;element name="BIC" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}BICType" minOccurs="0"/>
 *         &lt;element name="ScontoPagamentoAnticipato" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}Amount2DecimalType" minOccurs="0"/>
 *         &lt;element name="DataLimitePagamentoAnticipato" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="PenalitaPagamentiRitardati" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}Amount2DecimalType" minOccurs="0"/>
 *         &lt;element name="DataDecorrenzaPenale" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="CodicePagamento" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}String15Type" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DettaglioPagamentoType", propOrder = { "beneficiario", "modalitaPagamento",
		"dataRiferimentoTerminiPagamento", "giorniTerminiPagamento", "dataScadenzaPagamento", "importoPagamento",
		"codUfficioPostale", "cognomeQuietanzante", "nomeQuietanzante", "cfQuietanzante", "titoloQuietanzante",
		"istitutoFinanziario", "iban", "abi", "cab", "bic", "scontoPagamentoAnticipato",
		"dataLimitePagamentoAnticipato", "penalitaPagamentiRitardati", "dataDecorrenzaPenale", "codicePagamento" })
public class DettaglioPagamentoType implements Serializable {

	private static final long serialVersionUID = -2766345015927187765L;
	@XmlElement(name = "Beneficiario")
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	protected String beneficiario;
	@XmlElement(name = "ModalitaPagamento", required = true)
	protected ModalitaPagamentoType modalitaPagamento;
	@XmlElement(name = "DataRiferimentoTerminiPagamento")
	@XmlSchemaType(name = "date")
	protected XMLGregorianCalendar dataRiferimentoTerminiPagamento;
	@XmlElement(name = "GiorniTerminiPagamento")
	protected Integer giorniTerminiPagamento;
	@XmlElement(name = "DataScadenzaPagamento")
	@XmlSchemaType(name = "date")
	protected XMLGregorianCalendar dataScadenzaPagamento;
	@XmlJavaTypeAdapter(BigDecimal2Adapter.class)
	@XmlElement(name = "ImportoPagamento", required = true)
	protected BigDecimal importoPagamento;
	@XmlElement(name = "CodUfficioPostale")
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	protected String codUfficioPostale;
	@XmlElement(name = "CognomeQuietanzante")
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	protected String cognomeQuietanzante;
	@XmlElement(name = "NomeQuietanzante")
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	protected String nomeQuietanzante;
	@XmlElement(name = "CFQuietanzante")
	protected String cfQuietanzante;
	@XmlElement(name = "TitoloQuietanzante")
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	protected String titoloQuietanzante;
	@XmlElement(name = "IstitutoFinanziario")
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	protected String istitutoFinanziario;
	@XmlElement(name = "IBAN")
	protected String iban;
	@XmlElement(name = "ABI")
	protected String abi;
	@XmlElement(name = "CAB")
	protected String cab;
	@XmlElement(name = "BIC")
	protected String bic;
	@XmlJavaTypeAdapter(BigDecimal2Adapter.class)
	@XmlElement(name = "ScontoPagamentoAnticipato")
	protected BigDecimal scontoPagamentoAnticipato;
	@XmlElement(name = "DataLimitePagamentoAnticipato")
	@XmlSchemaType(name = "date")
	protected XMLGregorianCalendar dataLimitePagamentoAnticipato;
	@XmlJavaTypeAdapter(BigDecimal2Adapter.class)
	@XmlElement(name = "PenalitaPagamentiRitardati")
	protected BigDecimal penalitaPagamentiRitardati;
	@XmlElement(name = "DataDecorrenzaPenale")
	@XmlSchemaType(name = "date")
	protected XMLGregorianCalendar dataDecorrenzaPenale;
	@XmlElement(name = "CodicePagamento")
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	protected String codicePagamento;

	@XmlTransient
	private String id;

	/**
	 * Gets the value of the abi property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getABI() {
		return abi;
	}

	/**
	 * Gets the value of the beneficiario property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getBeneficiario() {
		return beneficiario;
	}

	/**
	 * Gets the value of the bic property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getBIC() {
		return bic;
	}

	/**
	 * Gets the value of the cab property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getCAB() {
		return cab;
	}

	/**
	 * Gets the value of the cfQuietanzante property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getCFQuietanzante() {
		return cfQuietanzante;
	}

	/**
	 * Gets the value of the codicePagamento property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getCodicePagamento() {
		return codicePagamento;
	}

	/**
	 * Gets the value of the codUfficioPostale property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getCodUfficioPostale() {
		return codUfficioPostale;
	}

	/**
	 * Gets the value of the cognomeQuietanzante property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getCognomeQuietanzante() {
		return cognomeQuietanzante;
	}

	/**
	 * Gets the value of the dataDecorrenzaPenale property.
	 *
	 * @return possible object is {@link XMLGregorianCalendar }
	 *
	 */
	public XMLGregorianCalendar getDataDecorrenzaPenale() {
		return dataDecorrenzaPenale;
	}

	/**
	 * Gets the value of the dataLimitePagamentoAnticipato property.
	 *
	 * @return possible object is {@link XMLGregorianCalendar }
	 *
	 */
	public XMLGregorianCalendar getDataLimitePagamentoAnticipato() {
		return dataLimitePagamentoAnticipato;
	}

	/**
	 * Gets the value of the dataRiferimentoTerminiPagamento property.
	 *
	 * @return possible object is {@link XMLGregorianCalendar }
	 *
	 */
	public XMLGregorianCalendar getDataRiferimentoTerminiPagamento() {
		return dataRiferimentoTerminiPagamento;
	}

	/**
	 * Gets the value of the dataScadenzaPagamento property.
	 *
	 * @return possible object is {@link XMLGregorianCalendar }
	 *
	 */
	public XMLGregorianCalendar getDataScadenzaPagamento() {
		return dataScadenzaPagamento;
	}

	/**
	 * Gets the value of the giorniTerminiPagamento property.
	 *
	 * @return possible object is {@link Integer }
	 *
	 */
	public Integer getGiorniTerminiPagamento() {
		return giorniTerminiPagamento;
	}

	/**
	 * Gets the value of the iban property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getIBAN() {
		return iban;
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
	 * Gets the value of the importoPagamento property.
	 *
	 * @return possible object is {@link BigDecimal }
	 *
	 */
	public BigDecimal getImportoPagamento() {
		return importoPagamento;
	}

	/**
	 * Gets the value of the istitutoFinanziario property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getIstitutoFinanziario() {
		return istitutoFinanziario;
	}

	/**
	 * Gets the value of the modalitaPagamento property.
	 *
	 * @return possible object is {@link ModalitaPagamentoType }
	 *
	 */
	public ModalitaPagamentoType getModalitaPagamento() {
		return modalitaPagamento;
	}

	/**
	 * Gets the value of the nomeQuietanzante property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getNomeQuietanzante() {
		return nomeQuietanzante;
	}

	/**
	 * Gets the value of the penalitaPagamentiRitardati property.
	 *
	 * @return possible object is {@link BigDecimal }
	 *
	 */
	public BigDecimal getPenalitaPagamentiRitardati() {
		return penalitaPagamentiRitardati;
	}

	/**
	 * Gets the value of the scontoPagamentoAnticipato property.
	 *
	 * @return possible object is {@link BigDecimal }
	 *
	 */
	public BigDecimal getScontoPagamentoAnticipato() {
		return scontoPagamentoAnticipato;
	}

	/**
	 * Gets the value of the titoloQuietanzante property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getTitoloQuietanzante() {
		return titoloQuietanzante;
	}

	/**
	 * Sets the value of the abi property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setABI(String value) {
		this.abi = value;
	}

	/**
	 * Sets the value of the beneficiario property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setBeneficiario(String value) {
		this.beneficiario = value;
	}

	/**
	 * Sets the value of the bic property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setBIC(String value) {
		this.bic = value;
	}

	/**
	 * Sets the value of the cab property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setCAB(String value) {
		this.cab = value;
	}

	/**
	 * Sets the value of the cfQuietanzante property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setCFQuietanzante(String value) {
		this.cfQuietanzante = value;
	}

	/**
	 * Sets the value of the codicePagamento property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setCodicePagamento(String value) {
		this.codicePagamento = value;
	}

	/**
	 * Sets the value of the codUfficioPostale property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setCodUfficioPostale(String value) {
		this.codUfficioPostale = value;
	}

	/**
	 * Sets the value of the cognomeQuietanzante property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setCognomeQuietanzante(String value) {
		this.cognomeQuietanzante = value;
	}

	/**
	 * Sets the value of the dataDecorrenzaPenale property.
	 *
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 *
	 */
	public void setDataDecorrenzaPenale(XMLGregorianCalendar value) {
		this.dataDecorrenzaPenale = value;
	}

	/**
	 * Sets the value of the dataLimitePagamentoAnticipato property.
	 *
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 *
	 */
	public void setDataLimitePagamentoAnticipato(XMLGregorianCalendar value) {
		this.dataLimitePagamentoAnticipato = value;
	}

	/**
	 * Sets the value of the dataRiferimentoTerminiPagamento property.
	 *
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 *
	 */
	public void setDataRiferimentoTerminiPagamento(XMLGregorianCalendar value) {
		this.dataRiferimentoTerminiPagamento = value;
	}

	/**
	 * Sets the value of the dataScadenzaPagamento property.
	 *
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 *
	 */
	public void setDataScadenzaPagamento(XMLGregorianCalendar value) {
		this.dataScadenzaPagamento = value;
	}

	/**
	 * Sets the value of the giorniTerminiPagamento property.
	 *
	 * @param value
	 *            allowed object is {@link Integer }
	 *
	 */
	public void setGiorniTerminiPagamento(Integer value) {
		this.giorniTerminiPagamento = value;
	}

	/**
	 * Sets the value of the iban property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setIBAN(String value) {
		this.iban = value;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Sets the value of the importoPagamento property.
	 *
	 * @param value
	 *            allowed object is {@link BigDecimal }
	 *
	 */
	public void setImportoPagamento(BigDecimal value) {
		this.importoPagamento = value;
	}

	/**
	 * Sets the value of the istitutoFinanziario property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setIstitutoFinanziario(String value) {
		this.istitutoFinanziario = value;
	}

	/**
	 * Sets the value of the modalitaPagamento property.
	 *
	 * @param value
	 *            allowed object is {@link ModalitaPagamentoType }
	 *
	 */
	public void setModalitaPagamento(ModalitaPagamentoType value) {
		this.modalitaPagamento = value;
	}

	/**
	 * Sets the value of the nomeQuietanzante property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setNomeQuietanzante(String value) {
		this.nomeQuietanzante = value;
	}

	/**
	 * Sets the value of the penalitaPagamentiRitardati property.
	 *
	 * @param value
	 *            allowed object is {@link BigDecimal }
	 *
	 */
	public void setPenalitaPagamentiRitardati(BigDecimal value) {
		this.penalitaPagamentiRitardati = value;
	}

	/**
	 * Sets the value of the scontoPagamentoAnticipato property.
	 *
	 * @param value
	 *            allowed object is {@link BigDecimal }
	 *
	 */
	public void setScontoPagamentoAnticipato(BigDecimal value) {
		this.scontoPagamentoAnticipato = value;
	}

	/**
	 * Sets the value of the titoloQuietanzante property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setTitoloQuietanzante(String value) {
		this.titoloQuietanzante = value;
	}
}
