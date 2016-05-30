package it.eurotn.panjea.fatturepa.util;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang3.StringUtils;

public final class FatturazionePAUtils {

    /**
     * Costruttore.
     */
    private FatturazionePAUtils() {
        super();
    }

    /**
     *
     * @param value
     *            valore
     * @return stringa ( se il valore è nullo o vuto viene restituito <code>null</code>)
     */
    public static String getString(String value) {
        return StringUtils.isBlank(value) ? null : value;
    }

    /**
     * @param date
     *            data
     * @return xml gregorian calendar
     */
    public static XMLGregorianCalendar getXMLGregorianCalendar(Date date) {
        XMLGregorianCalendar dateGragorian;
        try {
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(date);
            dateGragorian = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        } catch (Exception e) {
            dateGragorian = null;
        }
        return dateGragorian;
    }
}
