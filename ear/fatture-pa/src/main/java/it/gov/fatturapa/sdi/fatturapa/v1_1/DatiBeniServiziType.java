package it.gov.fatturapa.sdi.fatturapa.v1_1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * Blocco relativo ai dati di Beni Servizi della Fattura Elettronica
 *
 *
 * <p>
 * Java class for DatiBeniServiziType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="DatiBeniServiziType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DettaglioLinee" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.1}DettaglioLineeType" maxOccurs="unbounded"/>
 *         &lt;element name="DatiRiepilogo" type="{http://www.fatturapa.gov.it/sdi/fatturapa/v1.1}DatiRiepilogoType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DatiBeniServiziType", propOrder = { "dettaglioLinee", "datiRiepilogo" })
public class DatiBeniServiziType implements Serializable {

	private static final long serialVersionUID = 2782714162562387600L;
	@XmlElement(name = "DettaglioLinee", required = true)
	protected List<DettaglioLineeType> dettaglioLinee;
	@XmlElement(name = "DatiRiepilogo", required = true)
	protected List<DatiRiepilogoType> datiRiepilogo;

	/**
	 * Gets the value of the datiRiepilogo property.
	 *
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
	 * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
	 * the datiRiepilogo property.
	 *
	 * <p>
	 * For example, to add a new item, do as follows:
	 *
	 * <pre>
	 * getDatiRiepilogo().add(newItem);
	 * </pre>
	 *
	 *
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link DatiRiepilogoType }
	 *
	 * @return dati riepilogo
	 */
	public List<DatiRiepilogoType> getDatiRiepilogo() {
		if (datiRiepilogo == null) {
			datiRiepilogo = new ArrayList<DatiRiepilogoType>();
		}
		return this.datiRiepilogo;
	}

	/**
	 * Gets the value of the dettaglioLinee property.
	 *
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
	 * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
	 * the dettaglioLinee property.
	 *
	 * <p>
	 * For example, to add a new item, do as follows:
	 *
	 * <pre>
	 * getDettaglioLinee().add(newItem);
	 * </pre>
	 *
	 *
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link DettaglioLineeType }
	 *
	 * @return dettaglio linee
	 */
	public List<DettaglioLineeType> getDettaglioLinee() {
		if (dettaglioLinee == null) {
			dettaglioLinee = new ArrayList<DettaglioLineeType>();
		}
		return this.dettaglioLinee;
	}

}
