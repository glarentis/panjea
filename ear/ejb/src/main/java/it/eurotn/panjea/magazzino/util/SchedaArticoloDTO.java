package it.eurotn.panjea.magazzino.util;

import java.io.Serializable;

/**
 * @author fattazzo
 *
 */
public class SchedaArticoloDTO implements Serializable {

    private static final long serialVersionUID = 4080080069399188540L;

    private Integer anno;

    private Integer mese;

    private ArticoloRicerca articolo;

    private String note;

    private String azienda;

    /**
     * Costruttore.
     * 
     * @param anno
     *            anno
     * @param mese
     *            mese
     * @param articolo
     *            articolo
     * @param note
     *            note
     * @param azienda
     *            codiceAzienda
     */
    public SchedaArticoloDTO(final Integer anno, final Integer mese, final ArticoloRicerca articolo, final String note,
            final String azienda) {
        super();
        this.anno = anno;
        this.mese = mese;
        this.articolo = articolo;
        this.note = note;
        this.azienda = azienda;
    }

    /**
     * @return the anno
     */
    public Integer getAnno() {
        return anno;
    }

    /**
     * @return the articolo
     */
    public ArticoloRicerca getArticolo() {
        return articolo;
    }

    /**
     * @return the azienda
     */
    public String getAzienda() {
        return azienda;
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

}
