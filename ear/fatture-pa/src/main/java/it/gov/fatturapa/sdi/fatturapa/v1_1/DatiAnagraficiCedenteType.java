package it.gov.fatturapa.sdi.fatturapa.v1_1;

import it.eurotn.panjea.fatturepa.util.FatturazionePAUtils;
import it.gov.fatturapa.sdi.fatturapa.v1.AnagraficaType;
import it.gov.fatturapa.sdi.fatturapa.v1.IdFiscaleType;

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
 * Java class for DatiAnagraficiCedenteType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="DatiAnagraficiCedenteType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="IdFiscaleIVA" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}IdFiscaleType"/>
 *         &lt;element name="CodiceFiscale" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}CodiceFiscaleType" minOccurs="0"/>
 *         &lt;element name="Anagrafica" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}AnagraficaType"/>
 *         &lt;element name="AlboProfessionale" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}String60LatinType" minOccurs="0"/>
 *         &lt;element name="ProvinciaAlbo" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}ProvinciaType" minOccurs="0"/>
 *         &lt;element name="NumeroIscrizioneAlbo" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}String60Type" minOccurs="0"/>
 *         &lt;element name="DataIscrizioneAlbo" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="RegimeFiscale" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}RegimeFiscaleType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DatiAnagraficiCedenteType", propOrder = { "idFiscaleIVA", "codiceFiscale", "anagrafica",
		"alboProfessionale", "provinciaAlbo", "numeroIscrizioneAlbo", "dataIscrizioneAlbo", "regimeFiscale" })
public class DatiAnagraficiCedenteType implements Serializable {

	private static final long serialVersionUID = 5312133388241078149L;
	@XmlElement(name = "IdFiscaleIVA", required = true)
	protected IdFiscaleType idFiscaleIVA;
	@XmlElement(name = "CodiceFiscale")
	protected String codiceFiscale;
	@XmlElement(name = "Anagrafica", required = true)
	protected AnagraficaType anagrafica;
	@XmlElement(name = "AlboProfessionale")
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	protected String alboProfessionale;
	@XmlElement(name = "ProvinciaAlbo")
	protected String provinciaAlbo;
	@XmlElement(name = "NumeroIscrizioneAlbo")
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	protected String numeroIscrizioneAlbo;
	@XmlElement(name = "DataIscrizioneAlbo")
	@XmlSchemaType(name = "date")
	protected XMLGregorianCalendar dataIscrizioneAlbo;
	@XmlElement(name = "RegimeFiscale", required = true)
	protected RegimeFiscaleType regimeFiscale;

	/**
	 * Gets the value of the alboProfessionale property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getAlboProfessionale() {
		return alboProfessionale;
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
	 * Gets the value of the dataIscrizioneAlbo property.
	 *
	 * @return possible object is {@link XMLGregorianCalendar }
	 *
	 */
	public XMLGregorianCalendar getDataIscrizioneAlbo() {
		return dataIscrizioneAlbo;
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
	 * Gets the value of the numeroIscrizioneAlbo property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getNumeroIscrizioneAlbo() {
		return numeroIscrizioneAlbo;
	}

	/**
	 * Gets the value of the provinciaAlbo property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getProvinciaAlbo() {
		return provinciaAlbo;
	}

	/**
	 * Gets the value of the regimeFiscale property.
	 *
	 * @return possible object is {@link RegimeFiscaleType }
	 *
	 */
	public RegimeFiscaleType getRegimeFiscale() {
		return regimeFiscale;
	}

	/**
	 * Sets the value of the alboProfessionale property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setAlboProfessionale(String value) {
		this.alboProfessionale = FatturazionePAUtils.getString(value);
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
	 * Sets the value of the dataIscrizioneAlbo property.
	 *
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 *
	 */
	public void setDataIscrizioneAlbo(XMLGregorianCalendar value) {
		this.dataIscrizioneAlbo = value;
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

	/**
	 * Sets the value of the numeroIscrizioneAlbo property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setNumeroIscrizioneAlbo(String value) {
		this.numeroIscrizioneAlbo = FatturazionePAUtils.getString(value);
	}

	/**
	 * Sets the value of the provinciaAlbo property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setProvinciaAlbo(String value) {
		this.provinciaAlbo = FatturazionePAUtils.getString(value);
	}

	/**
	 * Sets the value of the regimeFiscale property.
	 *
	 * @param value
	 *            allowed object is {@link RegimeFiscaleType }
	 *
	 */
	public void setRegimeFiscale(RegimeFiscaleType value) {
		this.regimeFiscale = value;
	}

}
