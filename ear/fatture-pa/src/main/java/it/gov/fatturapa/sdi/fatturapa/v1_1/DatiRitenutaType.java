package it.gov.fatturapa.sdi.fatturapa.v1_1;

import it.gov.fatturapa.sdi.fatturapa.adapter.BigDecimal2Adapter;
import it.gov.fatturapa.sdi.fatturapa.adapter.PercBigDecimal2Adapter;
import it.gov.fatturapa.sdi.fatturapa.v1.TipoRitenutaType;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * <p>
 * Java class for DatiRitenutaType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="DatiRitenutaType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TipoRitenuta" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}TipoRitenutaType"/>
 *         &lt;element name="ImportoRitenuta" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}Amount2DecimalType"/>
 *         &lt;element name="AliquotaRitenuta" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}RateType"/>
 *         &lt;element name="CausalePagamento" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}CausalePagamentoType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DatiRitenutaType", propOrder = { "tipoRitenuta", "importoRitenuta", "aliquotaRitenuta",
		"causalePagamento" })
public class DatiRitenutaType implements Serializable {

	private static final long serialVersionUID = 1712823470339071146L;

	@XmlElement(name = "TipoRitenuta", required = true)
	protected TipoRitenutaType tipoRitenuta;

	@XmlJavaTypeAdapter(BigDecimal2Adapter.class)
	@XmlElement(name = "ImportoRitenuta", required = true)
	protected BigDecimal importoRitenuta;
	@XmlJavaTypeAdapter(PercBigDecimal2Adapter.class)
	@XmlElement(name = "AliquotaRitenuta", required = true)
	protected BigDecimal aliquotaRitenuta;
	@XmlElement(name = "CausalePagamento", required = true)
	protected CausalePagamentoType causalePagamento;

	/**
	 * Gets the value of the aliquotaRitenuta property.
	 *
	 * @return possible object is {@link BigDecimal }
	 *
	 */
	public BigDecimal getAliquotaRitenuta() {
		return aliquotaRitenuta;
	}

	/**
	 * Gets the value of the causalePagamento property.
	 *
	 * @return possible object is {@link CausalePagamentoType }
	 *
	 */
	public CausalePagamentoType getCausalePagamento() {
		return causalePagamento;
	}

	/**
	 * Gets the value of the importoRitenuta property.
	 *
	 * @return possible object is {@link BigDecimal }
	 *
	 */
	public BigDecimal getImportoRitenuta() {
		return importoRitenuta;
	}

	/**
	 * Gets the value of the tipoRitenuta property.
	 *
	 * @return possible object is {@link TipoRitenutaType }
	 *
	 */
	public TipoRitenutaType getTipoRitenuta() {
		return tipoRitenuta;
	}

	/**
	 * Sets the value of the aliquotaRitenuta property.
	 *
	 * @param value
	 *            allowed object is {@link BigDecimal }
	 *
	 */
	public void setAliquotaRitenuta(BigDecimal value) {
		this.aliquotaRitenuta = value;
	}

	/**
	 * Sets the value of the causalePagamento property.
	 *
	 * @param value
	 *            allowed object is {@link CausalePagamentoType }
	 *
	 */
	public void setCausalePagamento(CausalePagamentoType value) {
		this.causalePagamento = value;
	}

	/**
	 * Sets the value of the importoRitenuta property.
	 *
	 * @param value
	 *            allowed object is {@link BigDecimal }
	 *
	 */
	public void setImportoRitenuta(BigDecimal value) {
		this.importoRitenuta = value;
	}

	/**
	 * Sets the value of the tipoRitenuta property.
	 *
	 * @param value
	 *            allowed object is {@link TipoRitenutaType }
	 *
	 */
	public void setTipoRitenuta(TipoRitenutaType value) {
		this.tipoRitenuta = value;
	}

}
