package it.eurotn.panjea.magazzino.util.parametriricerca;

import java.io.Serializable;

/**
 * @author fattazzo
 *
 */
public class ParametriRicercaElaborazioni implements Serializable {

    private static final long serialVersionUID = 3091620887432320485L;

    private String nota;

    private Integer anno;

    private Integer mese;

    private boolean effettuaRicerca;

    {
        effettuaRicerca = Boolean.FALSE;
        nota = "";
    }

    /**
     * @return the anno
     */
    public Integer getAnno() {
        return anno;
    }

    /**
     * @return the mese
     */
    public Integer getMese() {
        return mese;
    }

    /**
     * @return the nota
     */
    public String getNota() {
        return nota;
    }

    /**
     * @return the effettuaRicerca
     */
    public boolean isEffettuaRicerca() {
        return effettuaRicerca;
    }

    /**
     * @param anno
     *            the anno to set
     */
    public void setAnno(Integer anno) {
        this.anno = anno;
    }

    /**
     * @param effettuaRicerca
     *            the effettuaRicerca to set
     */
    public void setEffettuaRicerca(boolean effettuaRicerca) {
        this.effettuaRicerca = effettuaRicerca;
    }

    /**
     * @param mese
     *            the mese to set
     */
    public void setMese(Integer mese) {
        this.mese = mese;
    }

    /**
     * @param nota
     *            the nota to set
     */
    public void setNota(String nota) {
        this.nota = nota;
    }

}
