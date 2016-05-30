package it.eurotn.panjea.magazzino.domain.moduloprezzo;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;

import it.eurotn.panjea.magazzino.domain.Sconto;

/**
 * Contiene i dati che sono stati calcolati durante la creazione del prezzo per ogni singolo modulo trattato
 * (listino,contratto etc..).
 *
 * @author giangi
 */
public class RisultatoModuloPrezzo<T> {

    /**
     * @author giangi
     * @version 1.0, 10/nov/2010
     */
    public enum EStrategia {
        VARIAZIONE, SOSTITUZIONE, ASSEGNAZIONE
    }

    /**
     * @author giangi
     * @version 1.0, 10/nov/2010
     */
    public enum ETipoModulo {

        LISTINO, CONTRATTO
    }

    public static final String STR_ZERI = "0000000000000000000000000";
    private static final String NUMBER_FORMAT = "###,###,###,##0";

    public static final NumberFormat DEFAULT_FORMAT = new DecimalFormat("###,###,###,##0.######");
    private static final String CHAR_SEPARATOR = "|";

    private T value = null;
    private String stringValue;
    private String descrizioneModulo;
    private String tipoModulo;

    private String codiceModulo;
    private String quantita;
    private EStrategia strategia;
    private Integer numeroDecimali;
    private boolean abilitato;

    /**
     * Costruttore.
     */
    public RisultatoModuloPrezzo() {
        abilitato = true;
    }

    /**
     * Crea la classe con i valori contenuti in una stringa generata con la proprietà getStringValue.
     *
     * @param descrizioneCalcoloModulo
     *            stringa generata dal medoto {@link RisultatoPrezzo#getStringValue()}
     */
    public RisultatoModuloPrezzo(final String descrizioneCalcoloModulo) {
        String[] descrizioneCalcoloModuloSplit = descrizioneCalcoloModulo.split("\\|");
        this.tipoModulo = descrizioneCalcoloModuloSplit[0];
        this.codiceModulo = descrizioneCalcoloModuloSplit[1];
        this.descrizioneModulo = descrizioneCalcoloModuloSplit[2];
        this.quantita = descrizioneCalcoloModuloSplit[3];
        this.numeroDecimali = 4;

        if ("SOSTITUZIONE".equals(descrizioneCalcoloModuloSplit[4])) {
            this.strategia = EStrategia.SOSTITUZIONE;
        } else {
            if ("VARIAZIONE".equals(descrizioneCalcoloModuloSplit[4])) {
                this.strategia = EStrategia.VARIAZIONE;
            } else {
                if ("ASSEGNAZIONE".equals(descrizioneCalcoloModuloSplit[4])) {
                    this.strategia = EStrategia.ASSEGNAZIONE;
                }
            }
        }
        this.stringValue = descrizioneCalcoloModuloSplit[5];
        this.abilitato = new Boolean(descrizioneCalcoloModuloSplit[6]);
    }

    /**
     * @return the codiceModulo
     */
    public String getCodiceModulo() {
        return codiceModulo;
    }

    /**
     * @return the descrizioneModulo
     */
    public String getDescrizioneModulo() {
        return descrizioneModulo;
    }

    /**
     * @return the numeroDecimali
     */
    public Integer getNumeroDecimali() {
        return numeroDecimali;
    }

    /**
     * @return the quantita
     */
    public String getQuantita() {
        return quantita;
    }

    /**
     * @return the strategia
     */
    public EStrategia getStrategia() {
        return strategia;
    }

    /**
     * @return valore calcolato del prezzo in formato stringa.
     */
    public String getStringValue() {
        return stringValue;
    }

    /**
     * @return the tipoModulo
     */
    public String getTipoModulo() {
        return tipoModulo;
    }

    /**
     * @return the value
     */
    public T getValue() {
        return value;
    }

    /**
     * @return the abilitato
     */
    public boolean isAbilitato() {
        return abilitato;
    }

    /**
     * @return Stringa che descrive i valori calcolati per il singolo modulo (listino o contratto).
     */
    public String serializeToString() {
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append(tipoModulo);
        stringBuffer.append(CHAR_SEPARATOR);
        stringBuffer.append(this.codiceModulo);
        stringBuffer.append(CHAR_SEPARATOR);
        stringBuffer.append(this.descrizioneModulo);
        stringBuffer.append(CHAR_SEPARATOR);
        stringBuffer.append(this.quantita);
        stringBuffer.append(CHAR_SEPARATOR);
        if (this.strategia == null) {
            stringBuffer.append("NON DEFINITA");
        } else {
            switch (this.strategia) {
            case SOSTITUZIONE:
                stringBuffer.append("SOSTITUZIONE");
                break;
            case VARIAZIONE:
                stringBuffer.append("VARIAZIONE");
                break;
            case ASSEGNAZIONE:
                stringBuffer.append("ASSEGNAZIONE");
                break;
            default:
                stringBuffer.append(" ");
                break;
            }
        }
        stringBuffer.append(CHAR_SEPARATOR);
        stringBuffer.append(this.stringValue);
        stringBuffer.append(CHAR_SEPARATOR);
        stringBuffer.append(abilitato);

        return stringBuffer.toString();
    }

    /**
     * @param abilitato
     *            the abilitato to set
     */
    public void setAbilitato(boolean abilitato) {
        this.abilitato = abilitato;
    }

    /**
     * @param codiceModulo
     *            the codiceModulo to set
     */
    public void setCodiceModulo(String codiceModulo) {
        this.codiceModulo = codiceModulo;
    }

    /**
     * @param descrizioneModulo
     *            the descrizioneModulo to set
     */
    public void setDescrizioneModulo(String descrizioneModulo) {
        this.descrizioneModulo = descrizioneModulo;
    }

    /**
     * @param numeroDecimali
     *            the numeroDecimali to set
     */
    public void setNumeroDecimali(Integer numeroDecimali) {
        this.numeroDecimali = numeroDecimali;
    }

    /**
     * @param quantita
     *            the quantita to set
     */
    public void setQuantita(String quantita) {
        this.quantita = quantita;
    }

    /**
     * @param strategia
     *            the strategia to set
     */
    public void setStrategia(EStrategia strategia) {
        this.strategia = strategia;
    }

    /**
     * @param tipoModulo
     *            the tipoModulo to set
     */
    public void setTipoModulo(String tipoModulo) {
        this.tipoModulo = tipoModulo;
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue(T value) {
        this.value = value;
        if (value instanceof Sconto) {
            StringBuilder sb = new StringBuilder();
            Sconto sconto = (Sconto) value;
            sb.append(sconto.getSconto1());
            sb.append("% ");
            if (sconto.getSconto2() != null) {
                sb.append(sconto.getSconto2());
                sb.append("% ");
            }
            if (sconto.getSconto3() != null) {
                sb.append(sconto.getSconto3());
                sb.append("% ");
            }
            if (sconto.getSconto4() != null) {
                sb.append(sconto.getSconto4());
                sb.append("% ");
            }
            this.stringValue = sb.toString();
        } else {
            BigDecimal valore = (BigDecimal) value;
            String decimalValueFormat = "";
            if (numeroDecimali != null && numeroDecimali.intValue() != 0) {
                decimalValueFormat = "." + STR_ZERI.substring(0, numeroDecimali);
            }
            Format format = new DecimalFormat(NUMBER_FORMAT + decimalValueFormat);
            if (value != null) {
                this.stringValue = format.format(valore);
            }
        }
    }
}
