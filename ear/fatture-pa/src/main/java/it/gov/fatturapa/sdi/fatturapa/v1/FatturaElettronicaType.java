package it.gov.fatturapa.sdi.fatturapa.v1;

import it.gov.fatturapa.sdi.fatturapa.IFatturaElettronicaType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang3.StringUtils;
import org.w3._2000._09.xmldsig_.SignatureType;

/**
 * <p>
 * Java class for FatturaElettronicaType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="FatturaElettronicaType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="FatturaElettronicaHeader" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.1}FatturaElettronicaHeaderType"/>
 *         &lt;element name="FatturaElettronicaBody" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.1}FatturaElettronicaBodyType" maxOccurs="unbounded"/>
 *         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}Signature" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="versione" use="required" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.1}VersioneSchemaType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FatturaElettronicaType", propOrder = { "fatturaElettronicaHeader", "fatturaElettronicaBody",
"signature" })
public class FatturaElettronicaType implements IFatturaElettronicaType, Serializable {

	private static final long serialVersionUID = -221884879465555324L;
	@XmlElement(name = "FatturaElettronicaHeader", required = true)
	protected FatturaElettronicaHeaderType fatturaElettronicaHeader;
	@XmlElement(name = "FatturaElettronicaBody", required = true)
	protected List<FatturaElettronicaBodyType> fatturaElettronicaBody;
	@XmlElement(name = "Signature", namespace = "http://www.w3.org/2000/09/xmldsig#")
	protected SignatureType signature;
	@XmlAttribute(required = true)
	protected String versione;

	@Override
	public void cleanEmptyValues() {

		if (StringUtils.isBlank(fatturaElettronicaHeader.getDatiTrasmissione().getContattiTrasmittente().getTelefono())
				&& StringUtils.isBlank(fatturaElettronicaHeader.getDatiTrasmissione().getContattiTrasmittente()
						.getEmail())) {
			fatturaElettronicaHeader.getDatiTrasmissione().setContattiTrasmittente(null);
		}
	}

	@Override
	public void copyNotRequiredProperty(IFatturaElettronicaType otherFatturaElettronicaType) {
	}

	@XmlTransient
	@Override
	public DatiTrasmissioneType getDatiTrasmissione() {
		return fatturaElettronicaHeader.getDatiTrasmissione();
	}

	/**
	 * @return body
	 */
	public List<FatturaElettronicaBodyType> getFatturaElettronicaBody() {
		if (fatturaElettronicaBody == null) {
			fatturaElettronicaBody = new ArrayList<FatturaElettronicaBodyType>();
		}
		return this.fatturaElettronicaBody;
	}

	/**
	 * Gets the value of the fatturaElettronicaHeader property.
	 *
	 * @return possible object is {@link FatturaElettronicaHeaderType }
	 *
	 */
	public FatturaElettronicaHeaderType getFatturaElettronicaHeader() {
		return fatturaElettronicaHeader;
	}

	@Override
	public SignatureType getSignature() {
		return signature;
	}

	@Override
	public String getVersione() {
		return versione;
	}

	/**
	 * Sets the value of the fatturaElettronicaHeader property.
	 *
	 * @param value
	 *            allowed object is {@link FatturaElettronicaHeader }
	 *
	 */
	public void setFatturaElettronicaHeader(FatturaElettronicaHeaderType value) {
		this.fatturaElettronicaHeader = value;
	}

	/**
	 * Sets the value of the signature property.
	 *
	 * @param value
	 *            allowed object is {@link SignatureType }
	 *
	 */
	public void setSignature(SignatureType value) {
		this.signature = value;
	}

	/**
	 * Sets the value of the versione property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setVersione(String value) {
		this.versione = value;
	}

}
