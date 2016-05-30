package it.eurotn.panjea.magazzino.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.apache.commons.lang3.ObjectUtils;

import it.eurotn.panjea.anagrafica.domain.Importo;

/**
 * @author fattazzo
 *
 */
public class ValoreConfronto implements Serializable {

    private static final long serialVersionUID = -720953124695985848L;

    private String descrizione;

    private BigDecimal prezzoBase;
    private BigDecimal prezzo;

    private Integer numeroDecimaliPrezzo;

    {
        prezzo = BigDecimal.ZERO;
        prezzoBase = BigDecimal.ZERO;
    }

    /**
     * Costruttore.
     */
    public ValoreConfronto() {
        super();
    }

    /**
     * @return the descrizione
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * @return the prezzo
     */
    public BigDecimal getPrezzo() {
        return prezzo;
    }

    /**
     * @return the variazionePercentuale
     */
    public BigDecimal getVariazionePercentuale() {
        BigDecimal percentuale = null;

        if (BigDecimal.ZERO.compareTo(prezzoBase) != 0
                || (BigDecimal.ZERO.compareTo(prezzoBase) != 0 && BigDecimal.ZERO.compareTo(prezzo) != 0)) {
            percentuale = getVariazioneValore().divide(prezzoBase, 4, RoundingMode.HALF_UP).multiply(Importo.HUNDRED)
                    .setScale(2, BigDecimal.ROUND_HALF_UP);
        }

        return percentuale;
    }

    /**
     * @return the variazioneValore
     */
    public BigDecimal getVariazioneValore() {
        return prezzo.subtract(prezzoBase).setScale(numeroDecimaliPrezzo);
    }

    /**
     * @param descrizione
     *            the descrizione to set
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * @param numeroDecimaliPrezzo
     *            the numeroDecimaliPrezzo to set
     */
    public void setNumeroDecimaliPrezzo(Integer numeroDecimaliPrezzo) {
        this.numeroDecimaliPrezzo = numeroDecimaliPrezzo;
    }

    /**
     * @param prezzo
     *            the prezzo to set
     */
    public void setPrezzo(BigDecimal prezzo) {
        this.prezzo = ObjectUtils.defaultIfNull(prezzo, BigDecimal.ZERO);
    }

    /**
     * @param prezzoBase
     *            the prezzoBase to set
     */
    public void setPrezzoBase(BigDecimal prezzoBase) {
        this.prezzoBase = ObjectUtils.defaultIfNull(prezzoBase, BigDecimal.ZERO);
    }

}
