package it.eurotn.panjea.magazzino.domain.moduloprezzo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.List;

import it.eurotn.panjea.magazzino.domain.Sconto;

/**
 * Contiene valore e descrizione di un calcolo del prezzo.<br>
 *
 * @author fattazzo
 */
public class RisultatoPrezzo<T> implements Serializable {
    private static final long serialVersionUID = 3744253293543520286L;
    public static final String STR_ZERI = "0000000000000000000000000";
    private static final String NUMBER_FORMAT = "###,###,###,##0";

    private String moduliPrezzoString = "";
    private T value = null;
    private String stringValue;
    private Double quantita;
    private Integer numeroDecimali;

    /**
     * Costruttore di default.
     */
    public RisultatoPrezzo() {
        super();
        numeroDecimali = 2;
    }

    /**
     * Aggiunge un risultato calcolcato da un modulo.
     * 
     * @param risultatoModuloPrezzo
     *            risultato utilizzato per descrivere l'algoritmo di calcolo per il risultato ottenuto.
     */
    public void addRisultatoModuloPrezzo(RisultatoModuloPrezzo<T> risultatoModuloPrezzo) {
        // Aggiungo alla stringa di descrizione dei moduli il modulo interessato.
        if (moduliPrezzoString == null) {
            moduliPrezzoString = "";
        }
        StringBuilder sb = new StringBuilder(moduliPrezzoString);
        sb.append("#");
        sb.append(risultatoModuloPrezzo.serializeToString());
        moduliPrezzoString = sb.toString();
    }

    /**
     * Disabilita tutti i moduli precedenti settando il flag disabilitato a true.<br>
     * Nel caso della sostituzione i moduli precedenti non devono essere considerati, voglio però mantenere la
     * possibilità di vedere quali moduli sono stati disabilitati.
     */
    public void disabilitaModuli() {
        StringBuilder moduliPrezzoDisabilitatiStringBuilder = new StringBuilder();
        for (RisultatoModuloPrezzo<T> risultatoModuloPrezzo : getRisultatiModuloPrezzo()) {
            risultatoModuloPrezzo.setAbilitato(false);
            moduliPrezzoDisabilitatiStringBuilder.append("#");
            moduliPrezzoDisabilitatiStringBuilder.append(risultatoModuloPrezzo.serializeToString());
        }
        moduliPrezzoString = moduliPrezzoDisabilitatiStringBuilder.toString();
    }

    /**
     * @return rappresentazione in stringa dei vari moduli che hanno contribuito a generare il prezzo/Sconto
     */
    public String getModuliPrezzoString() {
        return moduliPrezzoString;
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
    public Double getQuantita() {
        return quantita;
    }

    /**
     * @return lista di RisultatoModuloPrezzo che descrive il percorso per il calcolo del prezzo.
     */
    public List<RisultatoModuloPrezzo<T>> getRisultatiModuloPrezzo() {
        // i moduli sono descritti in una stringa separati da #
        List<RisultatoModuloPrezzo<T>> risultatiModuloPrezzo = new ArrayList<RisultatoModuloPrezzo<T>>();
        if (moduliPrezzoString == null) {
            return risultatiModuloPrezzo;
        }
        String[] moduliPrezzo = moduliPrezzoString.split("#");
        for (String moduloPrezzoString : moduliPrezzo) {
            if (!moduloPrezzoString.trim().isEmpty()) {
                RisultatoModuloPrezzo<T> risultatoModuloPrezzo = new RisultatoModuloPrezzo<T>(moduloPrezzoString);
                risultatiModuloPrezzo.add(risultatoModuloPrezzo);
            }
        }
        return risultatiModuloPrezzo;
    }

    /**
     * @return the stringValue
     */
    public String getStringValue() {
        return stringValue;
    }

    /**
     * @return the value
     */
    public T getValue() {
        return value;
    }

    /**
     * @param moduliPrezzoString
     *            the moduliPrezzoString to set
     */
    public void setModuliPrezzoString(String moduliPrezzoString) {
        this.moduliPrezzoString = moduliPrezzoString;
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
    public void setQuantita(Double quantita) {
        this.quantita = quantita;
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
            Format format = new DecimalFormat(NUMBER_FORMAT + "." + STR_ZERI.substring(0, numeroDecimali));
            if (value != null) {
                this.stringValue = format.format(valore);
            }
        }
    }

    @Override
    public String toString() {
        return "RisultatoPrezzo [moduliPrezzoString=" + moduliPrezzoString + "]";
    }

}
