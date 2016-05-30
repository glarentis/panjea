package it.eurotn.panjea.magazzino.util.parametriricerca;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

import it.eurotn.panjea.magazzino.util.ArticoloRicerca;

/**
 * @author fattazzo
 *
 */
public class ParametriCreazioneSchedeArticoli implements Serializable {

    private static final long serialVersionUID = -7690632724646907693L;

    private Set<ArticoloRicerca> articoli;

    private Integer anno;

    private Integer mese;

    private String note;

    {
        articoli = new TreeSet<ArticoloRicerca>();
    }

    /**
     * @return the anno
     */
    public Integer getAnno() {
        return anno;
    }

    /**
     * @return the articoli
     */
    public Set<ArticoloRicerca> getArticoli() {
        return articoli;
    }

    /**
     * @return the mese
     */
    public Integer getMese() {
        return mese;
    }

    /**
     * @return the note
     */
    public String getNote() {
        return note;
    }

    /**
     * @param anno
     *            the anno to set
     */
    public void setAnno(Integer anno) {
        this.anno = anno;
    }

    /**
     * @param articoli
     *            the articoli to set
     */
    public void setArticoli(Set<ArticoloRicerca> articoli) {
        this.articoli = articoli;
    }

    /**
     * @param mese
     *            the mese to set
     */
    public void setMese(Integer mese) {
        this.mese = mese;
    }

    /**
     * @param note
     *            the note to set
     */
    public void setNote(String note) {
        this.note = note;
    }
}
