package it.gov.fatturapa.sdi.fatturapa.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for SoggettoEmittenteType.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * <p>
 *
 * <pre>
 * &lt;simpleType name="SoggettoEmittenteType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;length value="2"/>
 *     &lt;enumeration value="CC"/>
 *     &lt;enumeration value="TZ"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 *
 */
@XmlType(name = "SoggettoEmittenteType")
@XmlEnum
public enum SoggettoEmittenteType {

	/**
	 * Cessionario / Committente.
	 *
	 */
	CC,

	/**
	 * Terzo.
	 *
	 */
	TZ;

	/**
	 * @param v
	 *            valore
	 * @return {@link SoggettoEmittenteType} corrispondente
	 */
	public static SoggettoEmittenteType fromValue(String v) {
		return valueOf(v);
	}

	/**
	 * @return the valore
	 */
	public String value() {
		return name();
	}

}
