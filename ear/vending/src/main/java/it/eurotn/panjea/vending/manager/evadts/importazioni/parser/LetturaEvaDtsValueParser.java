package it.eurotn.panjea.vending.manager.evadts.importazioni.parser;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

public final class LetturaEvaDtsValueParser {

    /**
     * Costruttore.
     */
    private LetturaEvaDtsValueParser() {
        super();
    }

    /**
     * Restituisce il valore Date.
     *
     * @param data
     *            data
     * @param ora
     *            ora
     * @return valore formattato
     */
    public static Date valoreDataOra(String data, String ora) {
        try {
            int lunghezzaAnno = 4;
            if (data.length() == 6) {
                lunghezzaAnno = 2;
            }

            int anno = 2000 + new Integer(data.substring(0, lunghezzaAnno));
            int mese = Integer.parseInt(data.substring(lunghezzaAnno, lunghezzaAnno + 2));
            int giorno = Integer.parseInt(data.substring(lunghezzaAnno + 2, lunghezzaAnno + 4));
            int ore = 0;
            int minuti = 0;

            if (!StringUtils.isBlank(ora)) {
                ore = new Integer(ora.substring(0, 2));
                minuti = new Integer(ora.substring(2, 4));
            }

            return new DateTime(anno, mese, giorno, ore, minuti, 0).toDate();
        } catch (Exception e) { // NOSONAR
            return null;
        }
    }

    /**
     * Restituisce il valore BigDecimal.
     *
     * @param valore
     *            valore
     * @return valore formattato
     */
    public static BigDecimal valoreDecimal(String valore) {
        try {
            return new BigDecimal(valore);
        } catch (Exception e) { // NOSONAR
            return BigDecimal.ZERO;
        }
    }

    /**
     * Restituisce il valore int.
     *
     * @param valore
     *            valore
     * @return valore formattato
     */
    public static int valoreInt(String valore) {
        try {
            return Integer.parseInt(valore);
        } catch (Exception e) { // NOSONAR
            return 0;
        }
    }

    /**
     * Restituisce il valore String.
     *
     * @param valore
     *            valore
     * @return valore formattato
     */
    public static String valoreString(String valore) {
        return StringUtils.trimToEmpty(valore);
    }
}
