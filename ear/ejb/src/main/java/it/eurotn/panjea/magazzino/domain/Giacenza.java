package it.eurotn.panjea.magazzino.domain;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

import org.apache.commons.lang3.ObjectUtils;

@Embeddable
public class Giacenza implements Serializable {

    private static final long serialVersionUID = 3772250056139510132L;

    private Double giacenza;

    @Transient
    private Double scorta;

    @Transient
    private Integer idArticolo;

    /**
     * Default constructor.
     */
    public Giacenza() {
        super();
    }

    /**
     * Costruttore.
     *
     * @param giacenza
     *            giacenza articolo
     * @param scorta
     *            scorta articolo
     */
    public Giacenza(final Double giacenza, final Double scorta) {
        super();
        this.giacenza = giacenza;
        this.scorta = scorta;
    }

    /**
     * @return Returns the giacenza.
     */
    public Double getGiacenza() {
        return giacenza;
    }

    /**
     * @return the idArticolo
     */
    public Integer getIdArticolo() {
        return idArticolo;
    }

    /**
     * @return Returns the scorta.
     */
    public Double getScorta() {
        return ObjectUtils.defaultIfNull(scorta, 0.0);
    }

    /**
     * @return true se l'articolo è sottoscoscorta
     */
    public boolean isSottoScorta() {
        return scorta != null && giacenza.compareTo(scorta) < 0;
    }

    /**
     * @param giacenza
     *            the giacenza to set
     */
    public void setGiacenza(Double giacenza) {
        this.giacenza = giacenza;
    }

    /**
     * @param idArticolo
     *            the idArticolo to set
     */
    public void setIdArticolo(Integer idArticolo) {
        this.idArticolo = idArticolo;
    }

    /**
     * @param scorta
     *            the scorta to set
     */
    public void setScorta(Double scorta) {
        this.scorta = scorta;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Giacenza [giacenza=" + giacenza + ", scorta=" + scorta + "]";
    }
}
