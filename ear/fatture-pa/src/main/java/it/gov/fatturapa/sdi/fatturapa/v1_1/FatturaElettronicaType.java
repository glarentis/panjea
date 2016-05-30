package it.gov.fatturapa.sdi.fatturapa.v1_1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang3.StringUtils;
import org.w3._2000._09.xmldsig_.SignatureType;

import it.gov.fatturapa.sdi.fatturapa.IFatturaElettronicaType;
import it.gov.fatturapa.sdi.fatturapa.v1.Art73Type;
import it.gov.fatturapa.sdi.fatturapa.v1.DatiAnagraficiCessionarioType;
import it.gov.fatturapa.sdi.fatturapa.v1.DatiTrasmissioneType;

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

	private static final long serialVersionUID = 2191122114265685083L;
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

		DatiAnagraficiCessionarioType datiAnagraficiCommittente = fatturaElettronicaHeader.getCessionarioCommittente()
				.getDatiAnagrafici();
		if (datiAnagraficiCommittente.getIdFiscaleIVA() != null
				&& (StringUtils.isBlank(datiAnagraficiCommittente.getIdFiscaleIVA().getIdCodice())
						|| StringUtils.isBlank(datiAnagraficiCommittente.getIdFiscaleIVA().getIdPaese()))) {
			fatturaElettronicaHeader.getCessionarioCommittente().getDatiAnagrafici().setIdFiscaleIVA(null);
		}

		if (fatturaElettronicaHeader.getDatiTrasmissione().getContattiTrasmittente() != null
				&& StringUtils
						.isBlank(fatturaElettronicaHeader.getDatiTrasmissione().getContattiTrasmittente().getTelefono())
				&& StringUtils
						.isBlank(fatturaElettronicaHeader.getDatiTrasmissione().getContattiTrasmittente().getEmail())) {
			fatturaElettronicaHeader.getDatiTrasmissione().setContattiTrasmittente(null);
		}

		for (FatturaElettronicaBodyType body : fatturaElettronicaBody) {
			DatiBolloType datiBollo = body.getDatiGenerali().getDatiGeneraliDocumento().getDatiBollo();
			if (datiBollo != null
					&& (datiBollo.getBolloVirtuale() == null || datiBollo.getBolloVirtuale() == BolloVirtualeType.NO)) {
				body.getDatiGenerali().getDatiGeneraliDocumento().setDatiBollo(null);
			}

			if (body.getDatiGenerali().getDatiGeneraliDocumento().getArt73() != null
					&& body.getDatiGenerali().getDatiGeneraliDocumento().getArt73() == Art73Type.NO) {
				body.getDatiGenerali().getDatiGeneraliDocumento().setArt73(null);
			}
		}

	}

	@Override
	public void copyNotRequiredProperty(IFatturaElettronicaType otherFatturaElettronicaType) {

		if (otherFatturaElettronicaType == null
				|| !Objects.equals(versione, otherFatturaElettronicaType.getVersione())) {
			return;
		}

		FatturaElettronicaType otherFTPA = (FatturaElettronicaType) otherFatturaElettronicaType;
		otherFTPA.cleanEmptyValues();

		FatturaElettronicaHeaderType otherHeader = otherFTPA.getFatturaElettronicaHeader();
		FatturaElettronicaBodyType otherBody = otherFTPA.getFatturaElettronicaBody().get(0);

		// riferimento amministrazione
		this.fatturaElettronicaHeader.getCedentePrestatore()
				.setRiferimentoAmministrazione(otherHeader.getCedentePrestatore().getRiferimentoAmministrazione());

		// contatti
		this.fatturaElettronicaHeader.getDatiTrasmissione()
				.setContattiTrasmittente(otherFTPA.getDatiTrasmissione().getContattiTrasmittente());

		// dati bollo
		this.fatturaElettronicaBody.get(0).getDatiGenerali().getDatiGeneraliDocumento()
				.setDatiBollo(otherBody.getDatiGenerali().getDatiGeneraliDocumento().getDatiBollo());

		// articolo 73
		this.fatturaElettronicaBody.get(0).getDatiGenerali().getDatiGeneraliDocumento()
				.setArt73(otherBody.getDatiGenerali().getDatiGeneraliDocumento().getArt73());

		// causali
		this.fatturaElettronicaBody.get(0).getDatiGenerali().getDatiGeneraliDocumento()
				.setCausale(otherBody.getDatiGenerali().getDatiGeneraliDocumento().getCausale());

		// dati ordine acquisto
		this.fatturaElettronicaBody.get(0).getDatiGenerali()
				.setDatiOrdineAcquisto(otherBody.getDatiGenerali().getDatiOrdineAcquisto());
		// dati contratto
		this.fatturaElettronicaBody.get(0).getDatiGenerali()
				.setDatiContratto(otherBody.getDatiGenerali().getDatiContratto());
		// dati convenzione
		this.fatturaElettronicaBody.get(0).getDatiGenerali()
				.setDatiConvenzione(otherBody.getDatiGenerali().getDatiConvenzione());
		// dati ricezione
		this.fatturaElettronicaBody.get(0).getDatiGenerali()
				.setDatiRicezione(otherBody.getDatiGenerali().getDatiRicezione());
		// dati fatture collegate
		this.fatturaElettronicaBody.get(0).getDatiGenerali()
				.setDatiFattureCollegate(otherBody.getDatiGenerali().getDatiFattureCollegate());

		// dati SAL
		this.fatturaElettronicaBody.get(0).getDatiGenerali().setDatiSAL(otherBody.getDatiGenerali().getDatiSAL());
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
