package it.gov.fatturapa.sdi.fatturapa.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for TipoScontoMaggiorazioneType.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * <p>
 *
 * <pre>
 * &lt;simpleType name="TipoScontoMaggiorazioneType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;length value="2"/>
 *     &lt;enumeration value="SC"/>
 *     &lt;enumeration value="MG"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 *
 */
@XmlType(name = "TipoScontoMaggiorazioneType")
@XmlEnum
public enum TipoScontoMaggiorazioneType {

	/**
	 *
	 * SC = Sconto.
	 *
	 *
	 */
	SC,

	/**
	 *
	 * MG = Maggiorazione.
	 *
	 *
	 */
	MG;

	/**
	 * @param v
	 *            valore
	 * @return {@link TipoScontoMaggiorazioneType} corrispondente
	 */
	public static TipoScontoMaggiorazioneType fromValue(String v) {
		return valueOf(v);
	}

	/**
	 * @return the valore
	 */
	public String value() {
		return name();
	}

}
