package it.gov.fatturapa.sdi.fatturapa.v1_1;

import it.gov.fatturapa.sdi.fatturapa.adapter.BigDecimal2Adapter;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * <p>
 * Java class for DatiBolloType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="DatiBolloType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="NumeroBollo" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}NumeroBolloType"/>
 *         &lt;element name="ImportoBollo" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}Amount2DecimalType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DatiBolloType", propOrder = { "bolloVirtuale", "importoBollo" })
public class DatiBolloType implements Serializable {

	private static final long serialVersionUID = 2389321430350585479L;
	@XmlElement(name = "BolloVirtuale", required = true)
	protected BolloVirtualeType bolloVirtuale;
	@XmlJavaTypeAdapter(BigDecimal2Adapter.class)
	@XmlElement(name = "ImportoBollo", required = true)
	protected BigDecimal importoBollo;

	/**
	 * Gets the value of the numeroBollo property.
	 *
	 * @return possible object is {@link BolloVirtualeType }
	 *
	 */
	public BolloVirtualeType getBolloVirtuale() {
		return bolloVirtuale;
	}

	/**
	 * Gets the value of the importoBollo property.
	 *
	 * @return possible object is {@link BigDecimal }
	 *
	 */
	public BigDecimal getImportoBollo() {
		return importoBollo;
	}

	/**
	 * Sets the value of the numeroBollo property.
	 *
	 * @param value
	 *            allowed object is {@link BolloVirtualeType }
	 *
	 */
	public void setBolloVirtuale(BolloVirtualeType value) {
		this.bolloVirtuale = value;
	}

	/**
	 * Sets the value of the importoBollo property.
	 *
	 * @param value
	 *            allowed object is {@link BigDecimal }
	 *
	 */
	public void setImportoBollo(BigDecimal value) {
		this.importoBollo = value;
	}

}
