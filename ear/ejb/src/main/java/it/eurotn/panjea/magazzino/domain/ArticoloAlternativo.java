package it.eurotn.panjea.magazzino.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;

@Entity
@Audited
@Table(name = "maga_articoli_alternativi")
@NamedQueries({
        @NamedQuery(name = "ArticoloAlternativo.caricaByArticolo", query = "select a.articoliAlternativi from Articolo a where a=:articolo") })
public class ArticoloAlternativo extends EntityBase {
    private static final long serialVersionUID = -8136290946642377615L;

    @ManyToOne
    @JoinColumn(name = "articolo_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ArticoloLite articolo;

    @ManyToOne
    @JoinColumn(name = "articolo_alternativo_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ArticoloLite articoloAlternativo;

    private Integer corrispondenza;

    /**
     * @return Returns the articolo.
     */
    public ArticoloLite getArticolo() {
        return articolo;
    }

    /**
     * @return the articoloAlternativo
     */
    public ArticoloLite getArticoloAlternativo() {
        return articoloAlternativo;
    }

    /**
     * @return Returns the corrispondenza.
     */
    public Integer getCorrispondenza() {
        return corrispondenza;
    }

    /**
     * @param articolo
     *            The articolo to set.
     */
    public void setArticolo(ArticoloLite articolo) {
        this.articolo = articolo;
    }

    /**
     * @param articoloAlternativo
     *            the articoloAlternativo to set
     */
    public void setArticoloAlternativo(ArticoloLite articoloAlternativo) {
        this.articoloAlternativo = articoloAlternativo;
    }

    /**
     * @param corrispondenza
     *            The corrispondenza to set.
     */
    public void setCorrispondenza(Integer corrispondenza) {
        this.corrispondenza = corrispondenza;
    }

}
