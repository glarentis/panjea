package it.eurotn.panjea.contabilita.manager.spesometro.builder;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

/**
 * @author fattazzo
 *
 */
public class SpesometroDataHandler {

    /**
     * Costruisce il codice del quadro con il seguente formato :<br>
     * codQuadro + numOperazione (formato 000) + prograssivo.
     *
     * @param codQuadro
     *            codice
     * @param numOperazione
     *            numero operazione
     * @param progQuadro
     *            progressivo del quadro
     * @return codice
     */
    public String formatQuadro(String codQuadro, Integer numOperazione, String progQuadro) {
        StringBuilder sbQuadro = new StringBuilder();
        sbQuadro.append(codQuadro);
        sbQuadro.append(StringUtils.leftPad(numOperazione.toString(), 3, "0"));
        sbQuadro.append(progQuadro);
        return sbQuadro.toString();
    }

    /**
     * Restituisce la stringa per un campo alfanumerico non posizionale.
     *
     * @param value
     *            valore
     * @return campo formattato
     */
    public String getANNP(String value) {
        String valueTrunk = StringUtils.left(StringUtils.defaultString(value), 16);
        return StringUtils.rightPad(valueTrunk, 16, " ");
    }

    /**
     * Restituisce la stringa per un campo boolean non posizionale.
     *
     * @param value
     *            valore
     * @return campo formattato
     */
    public String getCBNP(boolean value) {
        String numBool = "0";
        if (value) {
            numBool = "1";
        }
        return StringUtils.leftPad(numBool, 16, " ");
    }

    /**
     * Restituisce la stringa per un campo codice fiscale non posizionale.
     *
     * @param value
     *            valore
     * @return campo formattato
     */
    public String getCFNP(String value) {
        return StringUtils.rightPad(value, 16, " ");
    }

    /**
     * Restituisce la stringa per un campo data non posizionale.
     *
     * @param value
     *            valore
     * @return campo formattato
     */
    public String getDTNP(Date value) {
        return StringUtils.leftPad(new SimpleDateFormat("ddMMyyyy").format(value), 16, " ");
    }

    /**
     * Restituisce la stringa per un campo data posizionale.
     *
     * @param value
     *            valore
     * @return campo formattato
     */
    public String getDTP(Date value) {
        if (value == null) {
            return StringUtils.repeat("0", 8);
        }
        return new SimpleDateFormat("ddMMyyyy").format(value);
    }

    /**
     * Restituisce la stringa per una campo filler.
     *
     * @param fillerChar
     *            carattere da usare
     * @param fillerLength
     *            numero di ripetizione del carattere
     * @return campo formattato
     */
    public String getFiller(String fillerChar, int fillerLength) {
        return StringUtils.repeat(fillerChar, fillerLength);
    }

    /**
     * Restituisce la stringa per un importo non posizionale.
     *
     * @param value
     *            valore
     * @return campo formattato
     */
    public String getImportoNPNP(BigDecimal value) {
        BigDecimal importo = value.compareTo(BigDecimal.ONE) < 0 ? BigDecimal.ONE : value;
        return StringUtils.leftPad(new DecimalFormat("0").format(importo), 16, " ");
    }

    /**
     * Restituisce la stringa per un campo numerico positivo non posizionale.
     *
     * @param value
     *            valore
     * @return campo formattato
     */
    public String getNPNP(BigDecimal value) {
        return StringUtils.leftPad(new DecimalFormat("0").format(value), 16, " ");
    }

    /**
     * Restituisce la stringa per un campo partita iva non posizionale.
     *
     * @param value
     *            valore
     * @return campo formattato
     */
    public String getPINP(String value) {
        return StringUtils.rightPad(value, 16, " ");
    }

}
