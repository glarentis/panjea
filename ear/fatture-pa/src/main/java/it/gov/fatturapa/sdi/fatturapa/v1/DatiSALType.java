package it.gov.fatturapa.sdi.fatturapa.v1;

import java.io.Serializable;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for DatiSALType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="DatiSALType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RiferimentoFase" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}RiferimentoFaseType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DatiSALType", propOrder = { "riferimentoFase" })
public class DatiSALType implements Serializable {

	private static final long serialVersionUID = 6890453053426396866L;
	@XmlElement(name = "RiferimentoFase")
	protected int riferimentoFase;

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
	 * @return Gets the value of the riferimentoFase property.
	 *
	 */
	public int getRiferimentoFase() {
		return riferimentoFase;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @param value
	 *            Sets the value of the riferimentoFase property.
	 *
	 */
	public void setRiferimentoFase(int value) {
		this.riferimentoFase = value;
	}
}
