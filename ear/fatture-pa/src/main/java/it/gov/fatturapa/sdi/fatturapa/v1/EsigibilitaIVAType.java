package it.gov.fatturapa.sdi.fatturapa.v1;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for EsigibilitaIVAType.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * <p>
 *
 * <pre>
 * &lt;simpleType name="EsigibilitaIVAType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;minLength value="1"/>
 *     &lt;maxLength value="1"/>
 *     &lt;enumeration value="D"/>
 *     &lt;enumeration value="I"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 *
 */
@XmlType(name = "EsigibilitaIVAType")
@XmlEnum
public enum EsigibilitaIVAType implements Serializable {

	/**
	 * esigibilità differita.
	 *
	 */
	D,

	/**
	 * esigibilità immediata.
	 *
	 */
	I;

	/**
	 * @param v
	 *            valore
	 * @return {@link EsigibilitaIVAType} corrispondente
	 */
	public static EsigibilitaIVAType fromValue(String v) {
		return valueOf(v);
	}

	/**
	 * @return the valore
	 */
	public String value() {
		return name();
	}

}
