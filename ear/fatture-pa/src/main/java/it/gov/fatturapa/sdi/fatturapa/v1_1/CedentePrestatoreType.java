package it.gov.fatturapa.sdi.fatturapa.v1_1;

import it.eurotn.panjea.fatturepa.util.FatturazionePAUtils;
import it.gov.fatturapa.sdi.fatturapa.v1.ContattiType;
import it.gov.fatturapa.sdi.fatturapa.v1.IndirizzoType;
import it.gov.fatturapa.sdi.fatturapa.v1.IscrizioneREAType;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * Blocco relativo ai dati del Cedente / Prestatore
 *
 *
 * <p>
 * Java class for CedentePrestatoreType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="CedentePrestatoreType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DatiAnagrafici" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}DatiAnagraficiCedenteType"/>
 *         &lt;element name="Sede" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}IndirizzoType"/>
 *         &lt;element name="StabileOrganizzazione" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}IndirizzoType" minOccurs="0"/>
 *         &lt;element name="IscrizioneREA" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}IscrizioneREAType" minOccurs="0"/>
 *         &lt;element name="Contatti" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.0}ContattiType" minOccurs="0"/>
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
@XmlType(name = "CedentePrestatoreType", propOrder = { "datiAnagrafici", "sede", "stabileOrganizzazione",
		"iscrizioneREA", "contatti", "riferimentoAmministrazione" })
public class CedentePrestatoreType implements Serializable {

	private static final long serialVersionUID = 348010405534559160L;
	@XmlElement(name = "DatiAnagrafici", required = true)
	protected DatiAnagraficiCedenteType datiAnagrafici;
	@XmlElement(name = "Sede", required = true)
	protected IndirizzoType sede;
	@XmlElement(name = "StabileOrganizzazione")
	protected IndirizzoType stabileOrganizzazione;
	@XmlElement(name = "IscrizioneREA")
	protected IscrizioneREAType iscrizioneREA;
	@XmlElement(name = "Contatti")
	protected ContattiType contatti;
	@XmlElement(name = "RiferimentoAmministrazione")
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	protected String riferimentoAmministrazione;

	/**
	 * Gets the value of the contatti property.
	 *
	 * @return possible object is {@link ContattiType }
	 *
	 */
	public ContattiType getContatti() {
		return contatti;
	}

	/**
	 * Gets the value of the datiAnagrafici property.
	 *
	 * @return possible object is {@link DatiAnagraficiCedenteType }
	 *
	 */
	public DatiAnagraficiCedenteType getDatiAnagrafici() {
		return datiAnagrafici;
	}

	/**
	 * Gets the value of the iscrizioneREA property.
	 *
	 * @return possible object is {@link IscrizioneREAType }
	 *
	 */
	public IscrizioneREAType getIscrizioneREA() {
		return iscrizioneREA;
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
	 * Gets the value of the sede property.
	 *
	 * @return possible object is {@link IndirizzoType }
	 *
	 */
	public IndirizzoType getSede() {
		return sede;
	}

	/**
	 * Gets the value of the stabileOrganizzazione property.
	 *
	 * @return possible object is {@link IndirizzoType }
	 *
	 */
	public IndirizzoType getStabileOrganizzazione() {
		return stabileOrganizzazione;
	}

	/**
	 * Sets the value of the contatti property.
	 *
	 * @param value
	 *            allowed object is {@link ContattiType }
	 *
	 */
	public void setContatti(ContattiType value) {
		this.contatti = value;
	}

	/**
	 * Sets the value of the datiAnagrafici property.
	 *
	 * @param value
	 *            allowed object is {@link DatiAnagraficiCedenteType }
	 *
	 */
	public void setDatiAnagrafici(DatiAnagraficiCedenteType value) {
		this.datiAnagrafici = value;
	}

	/**
	 * Sets the value of the iscrizioneREA property.
	 *
	 * @param value
	 *            allowed object is {@link IscrizioneREAType }
	 *
	 */
	public void setIscrizioneREA(IscrizioneREAType value) {
		this.iscrizioneREA = value;
	}

	/**
	 * Sets the value of the riferimentoAmministrazione property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setRiferimentoAmministrazione(String value) {
		this.riferimentoAmministrazione = FatturazionePAUtils.getString(value);
	}

	/**
	 * Sets the value of the sede property.
	 *
	 * @param value
	 *            allowed object is {@link IndirizzoType }
	 *
	 */
	public void setSede(IndirizzoType value) {
		this.sede = value;
	}

	/**
	 * Sets the value of the stabileOrganizzazione property.
	 *
	 * @param value
	 *            allowed object is {@link IndirizzoType }
	 *
	 */
	public void setStabileOrganizzazione(IndirizzoType value) {
		this.stabileOrganizzazione = value;
	}

}
