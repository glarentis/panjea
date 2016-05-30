package it.gov.fatturapa.sdi.fatturapa.v1_1;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for Art73Type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * <p>
 *
 * <pre>
 * &lt;simpleType name="BolloVirtualeType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;length value="2"/>
 *     &lt;enumeration value="SI"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 *
 */
@XmlType(name = "BolloVirtualeType")
@XmlEnum
public enum BolloVirtualeType implements Serializable {

	/**
	 * SI.
	 */
	SI,

	/**
	 * NO.
	 */
	NO;

	/**
	 * @param v
	 *            valore
	 * @return {@link BolloVirtualeType} corrispondente
	 */
	public static BolloVirtualeType fromValue(String v) {
		return valueOf(v);
	}

	/**
	 * @return the valore
	 */
	public String value() {
		return name();
	}

}
