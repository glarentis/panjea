package it.gov.fatturapa.sdi.fatturapa.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for SocioUnicoType.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * <p>
 *
 * <pre>
 * &lt;simpleType name="SocioUnicoType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="SU"/>
 *     &lt;enumeration value="SM"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 *
 */
@XmlType(name = "SocioUnicoType")
@XmlEnum
public enum SocioUnicoType {

	/**
	 * socio unico.
	 *
	 */
	SU,

	/**
	 * più soci.
	 *
	 */
	SM;

	/**
	 * @param v
	 *            valore
	 * @return {@link SocioUnicoType} corrispondente
	 */
	public static SocioUnicoType fromValue(String v) {
		return valueOf(v);
	}

	/**
	 * @return the valore
	 */
	public String value() {
		return name();
	}

}
