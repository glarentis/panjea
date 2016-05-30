package it.gov.fatturapa.sdi.fatturapa.v1;

import it.gov.fatturapa.sdi.fatturapa.adapter.BigDecimal2Adapter;
import it.gov.fatturapa.sdi.fatturapa.adapter.BigDecimal8Adapter;
import it.gov.fatturapa.sdi.fatturapa.adapter.PercBigDecimal2Adapter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * <p>
 * Java class for DatiRiepilogoType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="DatiRiepilogoType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AliquotaIVA" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}RateType"/>
 *         &lt;element name="Natura" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}NaturaType" minOccurs="0"/>
 *         &lt;element name="SpeseAccessorie" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}Amount2DecimalType" minOccurs="0"/>
 *         &lt;element name="Arrotondamento" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}Amount8DecimalType" minOccurs="0"/>
 *         &lt;element name="ImponibileImporto" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}Amount2DecimalType"/>
 *         &lt;element name="Imposta" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}Amount2DecimalType"/>
 *         &lt;element name="EsigibilitaIVA" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}EsigibilitaIVAType" minOccurs="0"/>
 *         &lt;element name="RiferimentoNormativo" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}String100LatinType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DatiRiepilogoType", propOrder = { "aliquotaIVA", "natura", "speseAccessorie", "arrotondamento",
		"imponibileImporto", "imposta", "esigibilitaIVA", "riferimentoNormativo" })
public class DatiRiepilogoType implements Serializable {

	private static final long serialVersionUID = 9187286667739946517L;
	@XmlJavaTypeAdapter(PercBigDecimal2Adapter.class)
	@XmlElement(name = "AliquotaIVA", required = true)
	protected BigDecimal aliquotaIVA;
	@XmlElement(name = "Natura")
	protected NaturaType natura;
	@XmlJavaTypeAdapter(BigDecimal2Adapter.class)
	@XmlElement(name = "SpeseAccessorie")
	protected BigDecimal speseAccessorie;
	@XmlJavaTypeAdapter(BigDecimal8Adapter.class)
	@XmlElement(name = "Arrotondamento")
	protected BigDecimal arrotondamento;
	@XmlJavaTypeAdapter(BigDecimal2Adapter.class)
	@XmlElement(name = "ImponibileImporto", required = true)
	protected BigDecimal imponibileImporto;
	@XmlJavaTypeAdapter(BigDecimal2Adapter.class)
	@XmlElement(name = "Imposta", required = true)
	protected BigDecimal imposta;
	@XmlElement(name = "EsigibilitaIVA")
	protected EsigibilitaIVAType esigibilitaIVA;
	@XmlElement(name = "RiferimentoNormativo")
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	protected String riferimentoNormativo;

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
	 * Gets the value of the arrotondamento property.
	 *
	 * @return possible object is {@link BigDecimal }
	 *
	 */
	public BigDecimal getArrotondamento() {
		return arrotondamento;
	}

	/**
	 * Gets the value of the esigibilitaIVA property.
	 *
	 * @return possible object is {@link EsigibilitaIVAType }
	 *
	 */
	public EsigibilitaIVAType getEsigibilitaIVA() {
		return esigibilitaIVA;
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
	 * Gets the value of the imponibileImporto property.
	 *
	 * @return possible object is {@link BigDecimal }
	 *
	 */
	public BigDecimal getImponibileImporto() {
		return imponibileImporto;
	}

	/**
	 * Gets the value of the imposta property.
	 *
	 * @return possible object is {@link BigDecimal }
	 *
	 */
	public BigDecimal getImposta() {
		return imposta;
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
	 * Gets the value of the riferimentoNormativo property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getRiferimentoNormativo() {
		return riferimentoNormativo;
	}

	/**
	 * Gets the value of the speseAccessorie property.
	 *
	 * @return possible object is {@link BigDecimal }
	 *
	 */
	public BigDecimal getSpeseAccessorie() {
		return speseAccessorie;
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
	 * Sets the value of the arrotondamento property.
	 *
	 * @param value
	 *            allowed object is {@link BigDecimal }
	 *
	 */
	public void setArrotondamento(BigDecimal value) {
		this.arrotondamento = value;
	}

	/**
	 * Sets the value of the esigibilitaIVA property.
	 *
	 * @param value
	 *            allowed object is {@link EsigibilitaIVAType }
	 *
	 */
	public void setEsigibilitaIVA(EsigibilitaIVAType value) {
		this.esigibilitaIVA = value;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Sets the value of the imponibileImporto property.
	 *
	 * @param value
	 *            allowed object is {@link BigDecimal }
	 *
	 */
	public void setImponibileImporto(BigDecimal value) {
		this.imponibileImporto = value;
	}

	/**
	 * Sets the value of the imposta property.
	 *
	 * @param value
	 *            allowed object is {@link BigDecimal }
	 *
	 */
	public void setImposta(BigDecimal value) {
		this.imposta = value;
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
	 * Sets the value of the riferimentoNormativo property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setRiferimentoNormativo(String value) {
		this.riferimentoNormativo = value;
	}

	/**
	 * Sets the value of the speseAccessorie property.
	 *
	 * @param value
	 *            allowed object is {@link BigDecimal }
	 *
	 */
	public void setSpeseAccessorie(BigDecimal value) {
		this.speseAccessorie = value;
	}
}
