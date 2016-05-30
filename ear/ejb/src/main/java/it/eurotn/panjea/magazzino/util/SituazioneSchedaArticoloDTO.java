package it.eurotn.panjea.magazzino.util;

import java.io.Serializable;

/**
 * @author fattazzo
 *
 */
public class SituazioneSchedaArticoloDTO implements Serializable {

    private static final long serialVersionUID = -5129946879922860481L;

    private Integer anno;

    private Integer mese;

    /**
     * Numero di articoli che hanno la scheda creata per il mese e anno.
     */
    private Integer articoliStampati;

    /**
     * Numero di articoli di cui si è creata la scheda per il periodo ma che è stata poi invalidata.
     */
    private Integer articoliNonValidi;

    /**
     * Numero di articoli di cui non si è ancora creata la scheda.
     */
    private Integer articoliRimanenti;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        SituazioneSchedaArticoloDTO other = (SituazioneSchedaArticoloDTO) obj;
        if (anno == null) {
            if (other.anno != null) {
                return false;
            }
        } else if (!anno.equals(other.anno)) {
            return false;
        }
        if (mese == null) {
            if (other.mese != null) {
                return false;
            }
        } else if (!mese.equals(other.mese)) {
            return false;
        }
        return true;
    }

    /**
     * @return the anno
     */
    public Integer getAnno() {
        return anno;
    }

    /**
     * @return the articoliNonValidi
     */
    public Integer getArticoliNonValidi() {
        return articoliNonValidi;
    }

    /**
     * @return the articoliRimanenti
     */
    public Integer getArticoliRimanenti() {
        return articoliRimanenti;
    }

    /**
     * @return the articoliStampati
     */
    public Integer getArticoliStampati() {
        return articoliStampati;
    }

    /**
     * @return the mese
     */
    public Integer getMese() {
        return mese;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((anno == null) ? 0 : anno.hashCode());
        result = prime * result + ((mese == null) ? 0 : mese.hashCode());
        return result;
    }

    /**
     * @param anno
     *            the anno to set
     */
    public void setAnno(Integer anno) {
        this.anno = anno;
    }

    /**
     * @param articoliNonValidi
     *            the articoliNonValidi to set
     */
    public void setArticoliNonValidi(Integer articoliNonValidi) {
        this.articoliNonValidi = articoliNonValidi;
    }

    /**
     * @param articoliRimanenti
     *            the articoliRimanenti to set
     */
    public void setArticoliRimanenti(Integer articoliRimanenti) {
        this.articoliRimanenti = articoliRimanenti;
    }

    /**
     * @param articoliStampati
     *            the articoliStampati to set
     */
    public void setArticoliStampati(Integer articoliStampati) {
        this.articoliStampati = articoliStampati;
    }

    /**
     * @param mese
     *            the mese to set
     */
    public void setMese(Integer mese) {
        this.mese = mese;
    }

}
