package it.gov.fatturapa.sdi.fatturapa.v1_1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for NaturaType.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * <p>
 *
 * <pre>
 * &lt;simpleType name="NaturaType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="N1"/>
 *     &lt;enumeration value="N2"/>
 *     &lt;enumeration value="N3"/>
 *     &lt;enumeration value="N4"/>
 *     &lt;enumeration value="N5"/>
 *     &lt;enumeration value="N6"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 *
 */
@XmlType(name = "NaturaType")
@XmlEnum
public enum NaturaType {

	/**
	 * Escluse ex. art. 15.
	 *
	 */
	@XmlEnumValue("N1")
	N_1("N1"),

	/**
	 * Non soggette.
	 *
	 */
	@XmlEnumValue("N2")
	N_2("N2"),

	/**
	 * Non Imponibili.
	 *
	 */
	@XmlEnumValue("N3")
	N_3("N3"),

	/**
	 * Esenti.
	 *
	 */
	@XmlEnumValue("N4")
	N_4("N4"),

	/**
	 * Regime del margine.
	 *
	 */
	@XmlEnumValue("N5")
	N_5("N5"),

	/**
	 * Inversione contabile (reverse charge).
	 *
	 */
	@XmlEnumValue("N6")
	N_6("N6");

	/**
	 *
	 * @param v
	 *            valore
	 * @return {@link NaturaType} corrispondente
	 */
	public static NaturaType fromValue(String v) {
		for (NaturaType c : NaturaType.values()) {
			if (c.value.equals(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException("Natura " + v + " non prevista per il formato di trasmissione selezionato ");
	}

	private final String value;

	/**
	 *
	 * Costruttore.
	 *
	 * @param v
	 *            valore
	 */
	NaturaType(final String v) {
		value = v;
	}

	/**
	 * @return the valore
	 */
	public String value() {
		return value;
	}

}
