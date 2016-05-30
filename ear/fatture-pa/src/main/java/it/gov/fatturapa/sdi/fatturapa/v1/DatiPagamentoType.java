package it.gov.fatturapa.sdi.fatturapa.v1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * Blocco relativo ai dati di Pagamento della Fattura Elettronica
 *
 *
 * <p>
 * Java class for DatiPagamentoType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="DatiPagamentoType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CondizioniPagamento" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}CondizioniPagamentoType"/>
 *         &lt;element name="DettaglioPagamento" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}DettaglioPagamentoType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DatiPagamentoType", propOrder = { "condizioniPagamento", "dettaglioPagamento" })
public class DatiPagamentoType implements Serializable {

	private static final long serialVersionUID = -6303050643219871384L;
	@XmlElement(name = "CondizioniPagamento", required = true)
	protected CondizioniPagamentoType condizioniPagamento;
	@XmlElement(name = "DettaglioPagamento", required = true)
	protected List<DettaglioPagamentoType> dettaglioPagamento;

	@XmlTransient
	private String id;

	/**
	 * Gets the value of the condizioniPagamento property.
	 *
	 * @return possible object is {@link CondizioniPagamentoType }
	 *
	 */
	public CondizioniPagamentoType getCondizioniPagamento() {
		return condizioniPagamento;
	}

	/**
	 * Gets the value of the dettaglioPagamento property.
	 *
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
	 * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
	 * the dettaglioPagamento property.
	 *
	 * <p>
	 * For example, to add a new item, do as follows:
	 *
	 * <pre>
	 * getDettaglioPagamento().add(newItem);
	 * </pre>
	 *
	 *
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link DettaglioPagamentoType }
	 *
	 * @return dettaglio pagamento
	 */
	public List<DettaglioPagamentoType> getDettaglioPagamento() {
		if (dettaglioPagamento == null) {
			dettaglioPagamento = new ArrayList<DettaglioPagamentoType>();
		}
		return this.dettaglioPagamento;
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
	 * Sets the value of the condizioniPagamento property.
	 *
	 * @param value
	 *            allowed object is {@link CondizioniPagamentoType }
	 *
	 */
	public void setCondizioniPagamento(CondizioniPagamentoType value) {
		this.condizioniPagamento = value;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

}
