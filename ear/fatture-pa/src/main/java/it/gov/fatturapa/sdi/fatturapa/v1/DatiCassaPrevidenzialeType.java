package it.gov.fatturapa.sdi.fatturapa.v1;

import it.gov.fatturapa.sdi.fatturapa.adapter.BigDecimal2Adapter;
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
 * Java class for DatiCassaPrevidenzialeType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="DatiCassaPrevidenzialeType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TipoCassa" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}TipoCassaType"/>
 *         &lt;element name="AlCassa" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}RateType"/>
 *         &lt;element name="ImportoContributoCassa" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}Amount2DecimalType"/>
 *         &lt;element name="ImponibileCassa" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}Amount2DecimalType" minOccurs="0"/>
 *         &lt;element name="AliquotaIVA" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}RateType"/>
 *         &lt;element name="Ritenuta" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}RitenutaType" minOccurs="0"/>
 *         &lt;element name="Natura" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}NaturaType" minOccurs="0"/>
 *         &lt;element name="RiferimentoAmministrazione" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}String20Type" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DatiCassaPrevidenzialeType", propOrder = { "tipoCassa", "alCassa", "importoContributoCassa",
		"imponibileCassa", "aliquotaIVA", "ritenuta", "natura", "riferimentoAmministrazione" })
public class DatiCassaPrevidenzialeType implements Serializable {

	private static final long serialVersionUID = -3537914661583334604L;
	@XmlElement(name = "TipoCassa", required = true)
	protected TipoCassaType tipoCassa;
	@XmlJavaTypeAdapter(PercBigDecimal2Adapter.class)
	@XmlElement(name = "AlCassa", required = true)
	protected BigDecimal alCassa;
	@XmlJavaTypeAdapter(BigDecimal2Adapter.class)
	@XmlElement(name = "ImportoContributoCassa", required = true)
	protected BigDecimal importoContributoCassa;
	@XmlJavaTypeAdapter(BigDecimal2Adapter.class)
	@XmlElement(name = "ImponibileCassa")
	protected BigDecimal imponibileCassa;
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

	@XmlTransient
	private String id;

	/**
	 * Gets the value of the alCassa property.
	 *
	 * @return possible object is {@link BigDecimal }
	 *
	 */
	public BigDecimal getAlCassa() {
		return alCassa;
	}

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
	 * @return the id
	 */
	public String getId() {
		if (id == null) {
			id = UUID.randomUUID().toString();
		}
		return id;
	}

	/**
	 * Gets the value of the imponibileCassa property.
	 *
	 * @return possible object is {@link BigDecimal }
	 *
	 */
	public BigDecimal getImponibileCassa() {
		return imponibileCassa;
	}

	/**
	 * Gets the value of the importoContributoCassa property.
	 *
	 * @return possible object is {@link BigDecimal }
	 *
	 */
	public BigDecimal getImportoContributoCassa() {
		return importoContributoCassa;
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
	 * Gets the value of the tipoCassa property.
	 *
	 * @return possible object is {@link TipoCassaType }
	 *
	 */
	public TipoCassaType getTipoCassa() {
		return tipoCassa;
	}

	/**
	 * Sets the value of the alCassa property.
	 *
	 * @param value
	 *            allowed object is {@link BigDecimal }
	 *
	 */
	public void setAlCassa(BigDecimal value) {
		this.alCassa = value;
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
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Sets the value of the imponibileCassa property.
	 *
	 * @param value
	 *            allowed object is {@link BigDecimal }
	 *
	 */
	public void setImponibileCassa(BigDecimal value) {
		this.imponibileCassa = value;
	}

	/**
	 * Sets the value of the importoContributoCassa property.
	 *
	 * @param value
	 *            allowed object is {@link BigDecimal }
	 *
	 */
	public void setImportoContributoCassa(BigDecimal value) {
		this.importoContributoCassa = value;
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
	 * Sets the value of the tipoCassa property.
	 *
	 * @param value
	 *            allowed object is {@link TipoCassaType }
	 *
	 */
	public void setTipoCassa(TipoCassaType value) {
		this.tipoCassa = value;
	}

}
