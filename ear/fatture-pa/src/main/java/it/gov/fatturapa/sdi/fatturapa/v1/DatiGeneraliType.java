package it.gov.fatturapa.sdi.fatturapa.v1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * Blocco relativo ai Dati Generali della Fattura Elettronica
 *
 *
 * <p>
 * Java class for DatiGeneraliType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="DatiGeneraliType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DatiGeneraliDocumento" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}DatiGeneraliDocumentoType"/>
 *         &lt;element name="DatiOrdineAcquisto" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}DatiDocumentiCorrelatiType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="DatiContratto" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}DatiDocumentiCorrelatiType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="DatiConvenzione" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}DatiDocumentiCorrelatiType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="DatiRicezione" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}DatiDocumentiCorrelatiType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="DatiFattureCollegate" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}DatiDocumentiCorrelatiType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="DatiSAL" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}DatiSALType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="DatiDDT" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}DatiDDTType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="DatiTrasporto" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}DatiTrasportoType" minOccurs="0"/>
 *         &lt;element name="NormaDiRiferimento" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}String100LatinType" minOccurs="0"/>
 *         &lt;element name="FatturaPrincipale" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}FatturaPrincipaleType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DatiGeneraliType", propOrder = { "datiGeneraliDocumento", "datiOrdineAcquisto", "datiContratto",
		"datiConvenzione", "datiRicezione", "datiFattureCollegate", "datiSAL", "datiDDT", "datiTrasporto",
		"normaDiRiferimento", "fatturaPrincipale" })
public class DatiGeneraliType implements Serializable {

	private static final long serialVersionUID = 6596601332657724587L;
	@XmlElement(name = "DatiGeneraliDocumento", required = true)
	protected DatiGeneraliDocumentoType datiGeneraliDocumento;
	@XmlElement(name = "DatiOrdineAcquisto")
	protected List<DatiDocumentiCorrelatiType> datiOrdineAcquisto;
	@XmlElement(name = "DatiContratto")
	protected List<DatiDocumentiCorrelatiType> datiContratto;
	@XmlElement(name = "DatiConvenzione")
	protected List<DatiDocumentiCorrelatiType> datiConvenzione;
	@XmlElement(name = "DatiRicezione")
	protected List<DatiDocumentiCorrelatiType> datiRicezione;
	@XmlElement(name = "DatiFattureCollegate")
	protected List<DatiDocumentiCorrelatiType> datiFattureCollegate;
	@XmlElement(name = "DatiSAL")
	protected List<DatiSALType> datiSAL;
	@XmlElement(name = "DatiDDT")
	protected List<DatiDDTType> datiDDT;
	@XmlElement(name = "DatiTrasporto")
	protected DatiTrasportoType datiTrasporto;
	@XmlElement(name = "NormaDiRiferimento")
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	protected String normaDiRiferimento;
	@XmlElement(name = "FatturaPrincipale")
	protected FatturaPrincipaleType fatturaPrincipale;

	/**
	 * Gets the value of the datiContratto property.
	 *
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
	 * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
	 * the datiContratto property.
	 *
	 * <p>
	 * For example, to add a new item, do as follows:
	 *
	 * <pre>
	 * getDatiContratto().add(newItem);
	 * </pre>
	 *
	 *
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link DatiDocumentiCorrelatiType }
	 *
	 * @return dati contratti
	 */
	public List<DatiDocumentiCorrelatiType> getDatiContratto() {
		if (datiContratto == null) {
			datiContratto = new ArrayList<DatiDocumentiCorrelatiType>();
		}
		return this.datiContratto;
	}

	/**
	 * Gets the value of the datiConvenzione property.
	 *
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
	 * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
	 * the datiConvenzione property.
	 *
	 * <p>
	 * For example, to add a new item, do as follows:
	 *
	 * <pre>
	 * getDatiConvenzione().add(newItem);
	 * </pre>
	 *
	 *
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link DatiDocumentiCorrelatiType }
	 *
	 * @return dati convenzioni
	 */
	public List<DatiDocumentiCorrelatiType> getDatiConvenzione() {
		if (datiConvenzione == null) {
			datiConvenzione = new ArrayList<DatiDocumentiCorrelatiType>();
		}
		return this.datiConvenzione;
	}

	/**
	 * Gets the value of the datiDDT property.
	 *
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
	 * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
	 * the datiDDT property.
	 *
	 * <p>
	 * For example, to add a new item, do as follows:
	 *
	 * <pre>
	 * getDatiDDT().add(newItem);
	 * </pre>
	 *
	 *
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link DatiDDTType }
	 *
	 * @return dati ddt
	 */
	public List<DatiDDTType> getDatiDDT() {
		if (datiDDT == null) {
			datiDDT = new ArrayList<DatiDDTType>();
		}
		return this.datiDDT;
	}

	/**
	 * Gets the value of the datiFattureCollegate property.
	 *
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
	 * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
	 * the datiFattureCollegate property.
	 *
	 * <p>
	 * For example, to add a new item, do as follows:
	 *
	 * <pre>
	 * getDatiFattureCollegate().add(newItem);
	 * </pre>
	 *
	 *
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link DatiDocumentiCorrelatiType }
	 *
	 * @return dati fatture collegate
	 */
	public List<DatiDocumentiCorrelatiType> getDatiFattureCollegate() {
		if (datiFattureCollegate == null) {
			datiFattureCollegate = new ArrayList<DatiDocumentiCorrelatiType>();
		}
		return this.datiFattureCollegate;
	}

	/**
	 * Gets the value of the datiGeneraliDocumento property.
	 *
	 * @return possible object is {@link DatiGeneraliDocumentoType }
	 *
	 */
	public DatiGeneraliDocumentoType getDatiGeneraliDocumento() {
		return datiGeneraliDocumento;
	}

	/**
	 * Gets the value of the datiOrdineAcquisto property.
	 *
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
	 * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
	 * the datiOrdineAcquisto property.
	 *
	 * <p>
	 * For example, to add a new item, do as follows:
	 *
	 * <pre>
	 * getDatiOrdineAcquisto().add(newItem);
	 * </pre>
	 *
	 *
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link DatiDocumentiCorrelatiType }
	 *
	 * @return dati ordini acquisto
	 */
	public List<DatiDocumentiCorrelatiType> getDatiOrdineAcquisto() {
		if (datiOrdineAcquisto == null) {
			datiOrdineAcquisto = new ArrayList<DatiDocumentiCorrelatiType>();
		}
		return this.datiOrdineAcquisto;
	}

	/**
	 * Gets the value of the datiRicezione property.
	 *
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
	 * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
	 * the datiRicezione property.
	 *
	 * <p>
	 * For example, to add a new item, do as follows:
	 *
	 * <pre>
	 * getDatiRicezione().add(newItem);
	 * </pre>
	 *
	 *
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link DatiDocumentiCorrelatiType }
	 *
	 * @return dati ricezione
	 */
	public List<DatiDocumentiCorrelatiType> getDatiRicezione() {
		if (datiRicezione == null) {
			datiRicezione = new ArrayList<DatiDocumentiCorrelatiType>();
		}
		return this.datiRicezione;
	}

	/**
	 * Gets the value of the datiSAL property.
	 *
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
	 * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
	 * the datiSAL property.
	 *
	 * <p>
	 * For example, to add a new item, do as follows:
	 *
	 * <pre>
	 * getDatiSAL().add(newItem);
	 * </pre>
	 *
	 *
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link DatiSALType }
	 *
	 * @return datiSAL
	 */
	public List<DatiSALType> getDatiSAL() {
		if (datiSAL == null) {
			datiSAL = new ArrayList<DatiSALType>();
		}
		return this.datiSAL;
	}

	/**
	 * Gets the value of the datiTrasporto property.
	 *
	 * @return possible object is {@link DatiTrasportoType }
	 *
	 */
	public DatiTrasportoType getDatiTrasporto() {
		return datiTrasporto;
	}

	/**
	 * Gets the value of the fatturaPrincipale property.
	 *
	 * @return possible object is {@link FatturaPrincipaleType }
	 *
	 */
	public FatturaPrincipaleType getFatturaPrincipale() {
		return fatturaPrincipale;
	}

	/**
	 * Gets the value of the normaDiRiferimento property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getNormaDiRiferimento() {
		return normaDiRiferimento;
	}

	/**
	 * Sets the value of the datiGeneraliDocumento property.
	 *
	 * @param value
	 *            allowed object is {@link DatiGeneraliDocumentoType }
	 *
	 */
	public void setDatiGeneraliDocumento(DatiGeneraliDocumentoType value) {
		this.datiGeneraliDocumento = value;
	}

	/**
	 * Sets the value of the datiTrasporto property.
	 *
	 * @param value
	 *            allowed object is {@link DatiTrasportoType }
	 *
	 */
	public void setDatiTrasporto(DatiTrasportoType value) {
		this.datiTrasporto = value;
	}

	/**
	 * Sets the value of the fatturaPrincipale property.
	 *
	 * @param value
	 *            allowed object is {@link FatturaPrincipaleType }
	 *
	 */
	public void setFatturaPrincipale(FatturaPrincipaleType value) {
		this.fatturaPrincipale = value;
	}

	/**
	 * Sets the value of the normaDiRiferimento property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setNormaDiRiferimento(String value) {
		this.normaDiRiferimento = value;
	}

}
