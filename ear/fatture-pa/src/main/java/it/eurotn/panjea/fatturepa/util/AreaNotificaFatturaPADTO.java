package it.eurotn.panjea.fatturepa.util;

import java.io.Serializable;
import java.util.Date;

import it.eurotn.panjea.fatturepa.domain.StatoFatturaPA;

public class AreaNotificaFatturaPADTO implements Serializable {

    private static final long serialVersionUID = -894771618768741106L;

    private Integer id;

    private Integer idAreaMagazzinoFatturaPA;

    private StatoFatturaPA statoFatturaPA;

    private Date data;

    /**
     * @return the data
     */
    public Date getData() {
        return data;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @return the idAreaMagazzinoFatturaPA
     */
    public Integer getIdAreaMagazzinoFatturaPA() {
        return idAreaMagazzinoFatturaPA;
    }

    /**
     * @return the statoFatturaPA
     */
    public StatoFatturaPA getStatoFatturaPA() {
        return statoFatturaPA;
    }

    /**
     * @param data
     *            the data to set
     */
    public void setData(Date data) {
        this.data = data;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @param idAreaMagazzinoFatturaPA
     *            the idAreaMagazzinoFatturaPA to set
     */
    public void setIdAreaMagazzinoFatturaPA(Integer idAreaMagazzinoFatturaPA) {
        this.idAreaMagazzinoFatturaPA = idAreaMagazzinoFatturaPA;
    }

    /**
     * @param statoFatturaPA
     *            the statoFatturaPA to set
     */
    public void setStatoFatturaPA(StatoFatturaPA statoFatturaPA) {
        this.statoFatturaPA = statoFatturaPA;
    }
}
