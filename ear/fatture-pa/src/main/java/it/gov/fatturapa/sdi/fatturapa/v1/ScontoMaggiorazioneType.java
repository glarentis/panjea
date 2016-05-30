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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * <p>
 * Java class for ScontoMaggiorazioneType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="ScontoMaggiorazioneType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Tipo" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}TipoScontoMaggiorazioneType"/>
 *         &lt;element name="Percentuale" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}RateType" minOccurs="0"/>
 *         &lt;element name="Importo" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}Amount2DecimalType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ScontoMaggiorazioneType", propOrder = { "tipo", "percentuale", "importo" })
public class ScontoMaggiorazioneType implements Serializable {

	private static final long serialVersionUID = -7077114019537948517L;
	@XmlElement(name = "Tipo", required = true)
	protected TipoScontoMaggiorazioneType tipo;
	@XmlJavaTypeAdapter(PercBigDecimal2Adapter.class)
	@XmlElement(name = "Percentuale")
	protected BigDecimal percentuale;
	@XmlJavaTypeAdapter(BigDecimal2Adapter.class)
	@XmlElement(name = "Importo")
	protected BigDecimal importo;

	@XmlTransient
	private String id;

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
	 * Gets the value of the importo property.
	 *
	 * @return possible object is {@link BigDecimal }
	 *
	 */
	public BigDecimal getImporto() {
		return importo;
	}

	/**
	 * Gets the value of the percentuale property.
	 *
	 * @return possible object is {@link BigDecimal }
	 *
	 */
	public BigDecimal getPercentuale() {
		return percentuale;
	}

	/**
	 * Gets the value of the tipo property.
	 *
	 * @return possible object is {@link TipoScontoMaggiorazioneType }
	 *
	 */
	public TipoScontoMaggiorazioneType getTipo() {
		return tipo;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Sets the value of the importo property.
	 *
	 * @param value
	 *            allowed object is {@link BigDecimal }
	 *
	 */
	public void setImporto(BigDecimal value) {
		this.importo = value;
	}

	/**
	 * Sets the value of the percentuale property.
	 *
	 * @param value
	 *            allowed object is {@link BigDecimal }
	 *
	 */
	public void setPercentuale(BigDecimal value) {
		this.percentuale = value;
	}

	/**
	 * Sets the value of the tipo property.
	 *
	 * @param value
	 *            allowed object is {@link TipoScontoMaggiorazioneType }
	 *
	 */
	public void setTipo(TipoScontoMaggiorazioneType value) {
		this.tipo = value;
	}

}
