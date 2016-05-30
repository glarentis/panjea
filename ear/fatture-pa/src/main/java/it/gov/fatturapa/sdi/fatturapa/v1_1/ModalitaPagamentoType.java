package it.gov.fatturapa.sdi.fatturapa.v1_1;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for ModalitaPagamentoType.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * <p>
 *
 * <pre>
 * &lt;simpleType name="ModalitaPagamentoType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;length value="4"/>
 *     &lt;enumeration value="MP01"/>
 *     &lt;enumeration value="MP02"/>
 *     &lt;enumeration value="MP03"/>
 *     &lt;enumeration value="MP04"/>
 *     &lt;enumeration value="MP05"/>
 *     &lt;enumeration value="MP06"/>
 *     &lt;enumeration value="MP07"/>
 *     &lt;enumeration value="MP08"/>
 *     &lt;enumeration value="MP09"/>
 *     &lt;enumeration value="MP10"/>
 *     &lt;enumeration value="MP11"/>
 *     &lt;enumeration value="MP12"/>
 *     &lt;enumeration value="MP13"/>
 *     &lt;enumeration value="MP14"/>
 *     &lt;enumeration value="MP15"/>
 *     &lt;enumeration value="MP16"/>
 *     &lt;enumeration value="MP17"/>
 *     &lt;enumeration value="MP18"/>
 *     &lt;enumeration value="MP19"/>
 *     &lt;enumeration value="MP20"/>
 *     &lt;enumeration value="MP21"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 *
 */
@XmlType(name = "ModalitaPagamentoType")
@XmlEnum
public enum ModalitaPagamentoType implements Serializable {

	/**
	 * contanti.
	 *
	 */
	@XmlEnumValue("MP01")
	MP_01("MP01"),

	/**
	 * assegno.
	 *
	 */
	@XmlEnumValue("MP02")
	MP_02("MP02"),

	/**
	 * assegno circolare.
	 *
	 */
	@XmlEnumValue("MP03")
	MP_03("MP03"),

	/**
	 * contanti presso Tesoreria.
	 *
	 */
	@XmlEnumValue("MP04")
	MP_04("MP04"),

	/**
	 * bonifico.
	 *
	 */
	@XmlEnumValue("MP05")
	MP_05("MP05"),

	/**
	 * vaglia cambiario.
	 *
	 */
	@XmlEnumValue("MP06")
	MP_06("MP06"),

	/**
	 * bollettino bancario.
	 *
	 */
	@XmlEnumValue("MP07")
	MP_07("MP07"),

	/**
	 * carta di pagamento.
	 *
	 */
	@XmlEnumValue("MP08")
	MP_08("MP08"),

	/**
	 * RID.
	 *
	 */
	@XmlEnumValue("MP09")
	MP_09("MP09"),

	/**
	 * RID utenze.
	 *
	 */
	@XmlEnumValue("MP10")
	MP_10("MP10"),

	/**
	 * RID veloce.
	 *
	 */
	@XmlEnumValue("MP11")
	MP_11("MP11"),

	/**
	 * RIBA.
	 *
	 */
	@XmlEnumValue("MP12")
	MP_12("MP12"),

	/**
	 * MAV.
	 *
	 */
	@XmlEnumValue("MP13")
	MP_13("MP13"),

	/**
	 * quietanza erario.
	 *
	 */
	@XmlEnumValue("MP14")
	MP_14("MP14"),

	/**
	 * giroconto su conti di contabilità speciale.
	 *
	 */
	@XmlEnumValue("MP15")
	MP_15("MP15"),

	/**
	 * domiciliazione bancaria.
	 *
	 */
	@XmlEnumValue("MP16")
	MP_16("MP16"),

	/**
	 * domiciliazione postale.
	 *
	 */
	@XmlEnumValue("MP17")
	MP_17("MP17"),

	/**
	 * domiciliazione postale.
	 *
	 */
	@XmlEnumValue("MP18")
	MP_18("MP18"),

	/**
	 * domiciliazione postale.
	 *
	 */
	@XmlEnumValue("MP19")
	MP_19("MP19"),

	/**
	 * domiciliazione postale.
	 *
	 */
	@XmlEnumValue("MP20")
	MP_20("MP20"),

	/**
	 * domiciliazione postale.
	 *
	 */
	@XmlEnumValue("MP21")
	MP_21("MP21");

	/**
	 *
	 * @param v
	 *            valore
	 * @return {@link ModalitaPagamentoType} corrispondente
	 */
	public static ModalitaPagamentoType fromValue(String v) {
		for (ModalitaPagamentoType c : ModalitaPagamentoType.values()) {
			if (c.value.equals(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException("Modalità pagamento " + v
				+ " non prevista per il formato di trasmissione selezionato ");
	}

	private final String value;

	/**
	 * Costruttore.
	 *
	 * @param v
	 *            valore
	 */
	ModalitaPagamentoType(final String v) {
		value = v;
	}

	/**
	 * @return the valore
	 */
	public String value() {
		return value;
	}

}
