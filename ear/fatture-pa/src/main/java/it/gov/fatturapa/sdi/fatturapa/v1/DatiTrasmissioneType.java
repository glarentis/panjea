package it.gov.fatturapa.sdi.fatturapa.v1;

import it.gov.fatturapa.sdi.fatturapa.FormatoTrasmissioneType;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * Blocco relativo ai dati di trasmissione della Fattura Elettronica
 *
 *
 * <p>
 * Java class for DatiTrasmissioneType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="DatiTrasmissioneType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="IdTrasmittente" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}IdFiscaleType"/>
 *         &lt;element name="ProgressivoInvio" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}String10Type"/>
 *         &lt;element name="FormatoTrasmissione" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}FormatoTrasmissioneType"/>
 *         &lt;element name="CodiceDestinatario" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}CodiceDestinatarioType"/>
 *         &lt;element name="ContattiTrasmittente" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}ContattiTrasmittenteType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DatiTrasmissioneType", propOrder = { "idTrasmittente", "progressivoInvio", "formatoTrasmissione",
		"codiceDestinatario", "contattiTrasmittente" })
public class DatiTrasmissioneType implements Serializable {

	private static final long serialVersionUID = 4317629074973427994L;
	@XmlElement(name = "IdTrasmittente", required = true)
	protected IdFiscaleType idTrasmittente;
	@XmlElement(name = "ProgressivoInvio", required = true)
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	protected String progressivoInvio;
	@XmlElement(name = "FormatoTrasmissione", required = true)
	protected FormatoTrasmissioneType formatoTrasmissione;
	@XmlElement(name = "CodiceDestinatario", required = true)
	protected String codiceDestinatario;
	@XmlElement(name = "ContattiTrasmittente")
	protected ContattiTrasmittenteType contattiTrasmittente;

	{
		contattiTrasmittente = new ContattiTrasmittenteType();
	}

	/**
	 * Gets the value of the codiceDestinatario property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getCodiceDestinatario() {
		return codiceDestinatario;
	}

	/**
	 * Gets the value of the contattiTrasmittente property.
	 *
	 * @return possible object is {@link ContattiTrasmittenteType }
	 *
	 */
	public ContattiTrasmittenteType getContattiTrasmittente() {
		return contattiTrasmittente;
	}

	/**
	 * Gets the value of the formatoTrasmissione property.
	 *
	 * @return possible object is {@link FormatoTrasmissioneType }
	 *
	 */
	public FormatoTrasmissioneType getFormatoTrasmissione() {
		return formatoTrasmissione;
	}

	/**
	 * Gets the value of the idTrasmittente property.
	 *
	 * @return possible object is {@link IdFiscaleType }
	 *
	 */
	public IdFiscaleType getIdTrasmittente() {
		return idTrasmittente;
	}

	/**
	 * Gets the value of the progressivoInvio property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getProgressivoInvio() {
		return progressivoInvio;
	}

	/**
	 * Sets the value of the codiceDestinatario property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setCodiceDestinatario(String value) {
		this.codiceDestinatario = value;
	}

	/**
	 * Sets the value of the contattiTrasmittente property.
	 *
	 * @param value
	 *            allowed object is {@link ContattiTrasmittenteType }
	 *
	 */
	public void setContattiTrasmittente(ContattiTrasmittenteType value) {
		this.contattiTrasmittente = value;
	}

	/**
	 * Sets the value of the formatoTrasmissione property.
	 *
	 * @param value
	 *            allowed object is {@link FormatoTrasmissioneType }
	 *
	 */
	public void setFormatoTrasmissione(FormatoTrasmissioneType value) {
		this.formatoTrasmissione = value;
	}

	/**
	 * Sets the value of the idTrasmittente property.
	 *
	 * @param value
	 *            allowed object is {@link IdFiscaleType }
	 *
	 */
	public void setIdTrasmittente(IdFiscaleType value) {
		this.idTrasmittente = value;
	}

	/**
	 * Sets the value of the progressivoInvio property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setProgressivoInvio(String value) {
		this.progressivoInvio = value;
	}

}
