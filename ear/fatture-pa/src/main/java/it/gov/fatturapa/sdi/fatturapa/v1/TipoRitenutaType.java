package it.gov.fatturapa.sdi.fatturapa.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for TipoRitenutaType.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * <p>
 *
 * <pre>
 * &lt;simpleType name="TipoRitenutaType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;length value="4"/>
 *     &lt;enumeration value="RT01"/>
 *     &lt;enumeration value="RT02"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 *
 */
@XmlType(name = "TipoRitenutaType")
@XmlEnum
public enum TipoRitenutaType {

	/**
	 * Ritenuta di acconto persone fisiche.
	 *
	 */
	@XmlEnumValue("RT01")
	RT_01("RT01"),

	/**
	 * Ritenuta di acconto persone giuridiche.
	 *
	 */
	@XmlEnumValue("RT02")
	RT_02("RT02");

	/**
	 * @param v
	 *            valore
	 * @return {@link TipoRitenutaType} corrispondente
	 */
	public static TipoRitenutaType fromValue(String v) {
		for (TipoRitenutaType c : TipoRitenutaType.values()) {
			if (c.value.equals(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v);
	}

	private final String value;

	/**
	 * Costruttore.
	 *
	 * @param v
	 *            valore
	 */
	TipoRitenutaType(final String v) {
		value = v;
	}

	/**
	 * @return the valore
	 */
	public String value() {
		return value;
	}

}
