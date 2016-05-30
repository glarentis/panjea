package it.gov.fatturapa.sdi.fatturapa.v1;

import it.eurotn.panjea.fatturepa.util.FatturazionePAUtils;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * Il campo Denominazione è in alternativa ai campi Nome e Cognome
 *
 *
 * <p>
 * Java class for AnagraficaType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="AnagraficaType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;sequence>
 *             &lt;element name="Denominazione" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}String80LatinType"/>
 *           &lt;/sequence>
 *           &lt;sequence>
 *             &lt;element name="Nome" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}String60LatinType"/>
 *             &lt;element name="Cognome" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}String60LatinType"/>
 *           &lt;/sequence>
 *         &lt;/choice>
 *         &lt;element name="Titolo" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}TitoloType" minOccurs="0"/>
 *         &lt;element name="CodEORI" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}CodEORIType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AnagraficaType", propOrder = { "denominazione", "nome", "cognome", "titolo", "codEORI" })
public class AnagraficaType implements Serializable {

	private static final long serialVersionUID = 5440019149212395908L;
	@XmlElement(name = "Denominazione")
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	protected String denominazione;
	@XmlElement(name = "Nome")
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	protected String nome;
	@XmlElement(name = "Cognome")
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	protected String cognome;
	@XmlElement(name = "Titolo")
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	protected String titolo;
	@XmlElement(name = "CodEORI")
	protected String codEORI;

	/**
	 * Gets the value of the codEORI property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getCodEORI() {
		return codEORI;
	}

	/**
	 * Gets the value of the cognome property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getCognome() {
		return cognome;
	}

	/**
	 * Gets the value of the denominazione property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getDenominazione() {
		return denominazione;
	}

	/**
	 * Gets the value of the nome property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * Gets the value of the titolo property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getTitolo() {
		return titolo;
	}

	/**
	 * Sets the value of the codEORI property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setCodEORI(String value) {
		this.codEORI = FatturazionePAUtils.getString(value);
	}

	/**
	 * Sets the value of the cognome property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setCognome(String value) {
		this.cognome = FatturazionePAUtils.getString(value);
	}

	/**
	 * Sets the value of the denominazione property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setDenominazione(String value) {
		this.denominazione = FatturazionePAUtils.getString(value);
	}

	/**
	 * Sets the value of the nome property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setNome(String value) {
		this.nome = FatturazionePAUtils.getString(value);
	}

	/**
	 * Sets the value of the titolo property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setTitolo(String value) {
		this.titolo = FatturazionePAUtils.getString(value);
	}

}
