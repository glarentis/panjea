package it.eurotn.panjea.magazzino.manager.listinoprezzi;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;

public class ParametriListinoPrezzi implements Serializable {
    private static final long serialVersionUID = 8952803738119166547L;
    private Date data;
    private EntitaLite entita;
    private ArticoloLite articoloPartenza;
    private Integer numPagina;
    private Integer numRecordInPagina;

    /**
     * Costruttore.
     */
    public ParametriListinoPrezzi() {
        data = Calendar.getInstance().getTime();
        numRecordInPagina = 20;
    }

    /**
     * @return Returns the articoloPartenza.
     */
    public ArticoloLite getArticoloPartenza() {
        return articoloPartenza;
    }

    /**
     * @return Returns the data.
     */
    public Date getData() {
        return data;
    }

    /**
     * @return Returns the entita.
     */
    public EntitaLite getEntita() {
        return entita;
    }

    /**
     * @return Returns the idArticoloPartenza.
     */
    public Integer getIdArticoloPartenza() {
        return articoloPartenza.getId();
    }

    /**
     * @return Returns the idEntita.
     */
    public Integer getIdEntita() {
        return entita.getId();
    }

    /**
     * @return Returns the numPagina.
     */
    public Integer getNumPagina() {
        return numPagina;
    }

    /**
     * @return Returns the numRecordInPagina.
     */
    public Integer getNumRecordInPagina() {
        return numRecordInPagina;
    }

    /**
     * @param articolo
     *            The articolo to set.
     */
    public void setArticolo(ArticoloLite articolo) {
        this.articoloPartenza = articolo;
    }

    /**
     * @param articoloPartenza
     *            The articoloPartenza to set.
     */
    public void setArticoloPartenza(ArticoloLite articoloPartenza) {
        this.articoloPartenza = articoloPartenza;
    }

    /**
     * @param data
     *            The data to set.
     */
    public void setData(Date data) {
        this.data = data;
    }

    /**
     * @param entita
     *            The entita to set.
     */
    public void setEntita(EntitaLite entita) {
        this.entita = entita;
    }

    /**
     * @param numPagina
     *            The numPagina to set.
     */
    public void setNumPagina(Integer numPagina) {
        this.numPagina = numPagina;
    }

    /**
     * @param numRecordInPagina
     *            The numRecordInPagina to set.
     */
    public void setNumRecordInPagina(Integer numRecordInPagina) {
        this.numRecordInPagina = numRecordInPagina;
    }
}
