package it.gov.fatturapa.sdi.fatturapa.v1_1;

import it.gov.fatturapa.sdi.fatturapa.IFatturaElettronicaHeaderType;
import it.gov.fatturapa.sdi.fatturapa.v1.CessionarioCommittenteType;
import it.gov.fatturapa.sdi.fatturapa.v1.DatiTrasmissioneType;
import it.gov.fatturapa.sdi.fatturapa.v1.RappresentanteFiscaleType;
import it.gov.fatturapa.sdi.fatturapa.v1.SoggettoEmittenteType;
import it.gov.fatturapa.sdi.fatturapa.v1.TerzoIntermediarioSoggettoEmittenteType;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for FatturaElettronicaHeaderType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="FatturaElettronicaHeaderType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DatiTrasmissione" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}DatiTrasmissioneType"/>
 *         &lt;element name="CedentePrestatore" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}CedentePrestatoreType"/>
 *         &lt;element name="RappresentanteFiscale" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}RappresentanteFiscaleType" minOccurs="0"/>
 *         &lt;element name="CessionarioCommittente" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}CessionarioCommittenteType"/>
 *         &lt;element name="TerzoIntermediarioOSoggettoEmittente" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}TerzoIntermediarioSoggettoEmittenteType" minOccurs="0"/>
 *         &lt;element name="SoggettoEmittente" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}SoggettoEmittenteType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FatturaElettronicaHeaderType", propOrder = { "datiTrasmissione", "cedentePrestatore",
		"rappresentanteFiscale", "cessionarioCommittente", "terzoIntermediarioOSoggettoEmittente", "soggettoEmittente" })
public class FatturaElettronicaHeaderType implements IFatturaElettronicaHeaderType, Serializable {

	private static final long serialVersionUID = 1589808610009721481L;
	@XmlElement(name = "DatiTrasmissione", required = true)
	protected DatiTrasmissioneType datiTrasmissione;
	@XmlElement(name = "CedentePrestatore", required = true)
	protected CedentePrestatoreType cedentePrestatore;
	@XmlElement(name = "RappresentanteFiscale")
	protected RappresentanteFiscaleType rappresentanteFiscale;
	@XmlElement(name = "CessionarioCommittente", required = true)
	protected CessionarioCommittenteType cessionarioCommittente;
	@XmlElement(name = "TerzoIntermediarioOSoggettoEmittente")
	protected TerzoIntermediarioSoggettoEmittenteType terzoIntermediarioOSoggettoEmittente;
	@XmlElement(name = "SoggettoEmittente")
	protected SoggettoEmittenteType soggettoEmittente;

	/**
	 * Gets the value of the cedentePrestatore property.
	 *
	 * @return possible object is {@link CedentePrestatoreType }
	 *
	 */
	public CedentePrestatoreType getCedentePrestatore() {
		return cedentePrestatore;
	}

	/**
	 * Gets the value of the cessionarioCommittente property.
	 *
	 * @return possible object is {@link CessionarioCommittenteType }
	 *
	 */
	public CessionarioCommittenteType getCessionarioCommittente() {
		return cessionarioCommittente;
	}

	/**
	 * Gets the value of the datiTrasmissione property.
	 *
	 * @return possible object is {@link DatiTrasmissioneType }
	 *
	 */
	public DatiTrasmissioneType getDatiTrasmissione() {
		return datiTrasmissione;
	}

	/**
	 * Gets the value of the rappresentanteFiscale property.
	 *
	 * @return possible object is {@link RappresentanteFiscaleType }
	 *
	 */
	public RappresentanteFiscaleType getRappresentanteFiscale() {
		return rappresentanteFiscale;
	}

	/**
	 * Gets the value of the soggettoEmittente property.
	 *
	 * @return possible object is {@link SoggettoEmittenteType }
	 *
	 */
	public SoggettoEmittenteType getSoggettoEmittente() {
		return soggettoEmittente;
	}

	/**
	 * Gets the value of the terzoIntermediarioOSoggettoEmittente property.
	 *
	 * @return possible object is {@link TerzoIntermediarioSoggettoEmittenteType }
	 *
	 */
	public TerzoIntermediarioSoggettoEmittenteType getTerzoIntermediarioOSoggettoEmittente() {
		return terzoIntermediarioOSoggettoEmittente;
	}

	/**
	 * Sets the value of the cedentePrestatore property.
	 *
	 * @param value
	 *            allowed object is {@link CedentePrestatoreType }
	 *
	 */
	public void setCedentePrestatore(CedentePrestatoreType value) {
		this.cedentePrestatore = value;
	}

	/**
	 * Sets the value of the cessionarioCommittente property.
	 *
	 * @param value
	 *            allowed object is {@link CessionarioCommittenteType }
	 *
	 */
	public void setCessionarioCommittente(CessionarioCommittenteType value) {
		this.cessionarioCommittente = value;
	}

	/**
	 * Sets the value of the datiTrasmissione property.
	 *
	 * @param value
	 *            allowed object is {@link DatiTrasmissioneType }
	 *
	 */
	public void setDatiTrasmissione(DatiTrasmissioneType value) {
		this.datiTrasmissione = value;
	}

	/**
	 * Sets the value of the rappresentanteFiscale property.
	 *
	 * @param value
	 *            allowed object is {@link RappresentanteFiscaleType }
	 *
	 */
	public void setRappresentanteFiscale(RappresentanteFiscaleType value) {
		this.rappresentanteFiscale = value;
	}

	/**
	 * Sets the value of the soggettoEmittente property.
	 *
	 * @param value
	 *            allowed object is {@link SoggettoEmittenteType }
	 *
	 */
	public void setSoggettoEmittente(SoggettoEmittenteType value) {
		this.soggettoEmittente = value;
	}

	/**
	 * Sets the value of the terzoIntermediarioOSoggettoEmittente property.
	 *
	 * @param value
	 *            allowed object is {@link TerzoIntermediarioSoggettoEmittenteType }
	 *
	 */
	public void setTerzoIntermediarioOSoggettoEmittente(TerzoIntermediarioSoggettoEmittenteType value) {
		this.terzoIntermediarioOSoggettoEmittente = value;
	}

}
