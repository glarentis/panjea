package it.gov.fatturapa.sdi.fatturapa;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for FormatoTrasmissioneType.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * <p>
 *
 * <pre>
 * &lt;simpleType name="FormatoTrasmissioneType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;length value="5"/>
 *     &lt;enumeration value="SDI10"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 *
 */
@XmlType(name = "FormatoTrasmissioneType")
@XmlEnum
public enum FormatoTrasmissioneType {

    /**
     *
     * Formato di Trasmissione SDI Versione 1.0.
     *
     *
     */
    @javax.xml.bind.annotation.XmlEnumValue("SDI10") SDI_10("SDI10", "1.0"),

    /**
     *
     * Formato di Trasmissione SDI Versione 1.1.
     *
     *
     */
    @javax.xml.bind.annotation.XmlEnumValue("SDI11") SDI_11("SDI11", "1.1");

    private final String value;
    private final String codice;

    /**
     *
     * Costruttore.
     *
     * @param val
     *            valore
     * @param paramCodice
     *            codice
     */
    FormatoTrasmissioneType(final String val, final String paramCodice) {
        value = val;
        codice = paramCodice;
    }

    /**
     * @param codice
     *            codice
     * @return formato di trasmissione corrispondente al codice
     */
    public static FormatoTrasmissioneType fromCodice(String codice) {
        for (FormatoTrasmissioneType c : FormatoTrasmissioneType.values()) {
            if (c.codice.equals(codice)) {
                return c;
            }
        }
        throw new IllegalArgumentException(codice);
    }

    /**
     * @param val
     *            valore
     * @return formato di trasmissione corrispondente al valore
     */
    public static FormatoTrasmissioneType fromValue(String val) {
        for (FormatoTrasmissioneType c : FormatoTrasmissioneType.values()) {
            if (c.value.equals(val)) {
                return c;
            }
        }
        throw new IllegalArgumentException(val);
    }

    /**
     * @return the codice
     */
    public String getCodice() {
        return codice;
    }

    /**
     * @return the valore
     */
    public String value() {
        return value;
    }

}
