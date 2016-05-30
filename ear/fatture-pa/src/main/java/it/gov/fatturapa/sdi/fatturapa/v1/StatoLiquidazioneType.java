package it.gov.fatturapa.sdi.fatturapa.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for StatoLiquidazioneType.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * <p>
 *
 * <pre>
 * &lt;simpleType name="StatoLiquidazioneType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="LS"/>
 *     &lt;enumeration value="LN"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 *
 */
@XmlType(name = "StatoLiquidazioneType")
@XmlEnum
public enum StatoLiquidazioneType {

	/**
	 * in liquidazione.
	 *
	 */
	LS,

	/**
	 * non in liquidazione.
	 *
	 */
	LN;

	/**
	 * @param v
	 *            valore
	 * @return {@link StatoLiquidazioneType} corrispondente
	 */
	public static StatoLiquidazioneType fromValue(String v) {
		return valueOf(v);
	}

	/**
	 * @return the valore
	 */
	public String value() {
		return name();
	}

}
