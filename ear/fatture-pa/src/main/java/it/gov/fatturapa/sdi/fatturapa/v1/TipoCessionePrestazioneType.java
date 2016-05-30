package it.gov.fatturapa.sdi.fatturapa.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for TipoCessionePrestazioneType.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * <p>
 *
 * <pre>
 * &lt;simpleType name="TipoCessionePrestazioneType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;length value="2"/>
 *     &lt;enumeration value="SC"/>
 *     &lt;enumeration value="PR"/>
 *     &lt;enumeration value="AB"/>
 *     &lt;enumeration value="AC"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 *
 */
@XmlType(name = "TipoCessionePrestazioneType")
@XmlEnum
public enum TipoCessionePrestazioneType {

	/**
	 * Sconto.
	 *
	 */
	SC,

	/**
	 * Premio.
	 *
	 */
	PR,

	/**
	 * Abbuono.
	 *
	 */
	AB,

	/**
	 * Spesa accessoria.
	 *
	 */
	AC;

	/**
	 * @param v
	 *            valore
	 * @return {@link TipoCessionePrestazioneType} corrispondente
	 */
	public static TipoCessionePrestazioneType fromValue(String v) {
		return valueOf(v);
	}

	/**
	 * @return the valore
	 */
	public String value() {
		return name();
	}

}
