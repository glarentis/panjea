package it.eurotn.panjea.magazzino.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;

/**
 * @author leonardo
 */
@Entity
@Audited
@Table(name = "maga_articolo_deposito", uniqueConstraints = @UniqueConstraint(columnNames = { "deposito_id",
        "articolo_id" }) )
@NamedQueries({
        @NamedQuery(name = "ArticoloDeposito.caricaByArticolo", query = "select ad from ArticoloDeposito ad where ad.articolo.id=:paramIdArticolo"),
        @NamedQuery(name = "ArticoloDeposito.caricaByDeposito", query = "select ad from ArticoloDeposito ad where ad.deposito.id = :paramIdDeposito"),
        @NamedQuery(name = "ArticoloDeposito.caricaByArticoloDeposito", query = "select ad from ArticoloDeposito ad where ad.articolo.id=:paramIdArticolo and ad.deposito.id = :paramIdDeposito") })
public class ArticoloDeposito extends EntityBase {

    private static final long serialVersionUID = 915754818098316918L;

    @ManyToOne
    @JoinColumn(name = "articolo_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Articolo articolo;

    @ManyToOne
    @JoinColumn(name = "deposito_id", nullable = false)
    private DepositoLite deposito;

    private Double scorta;

    /**
     * Costruttore.
     */
    public ArticoloDeposito() {
        super();
    }

    /**
     * @return the articolo
     */
    public Articolo getArticolo() {
        return articolo;
    }

    /**
     * @return the deposito
     */
    public DepositoLite getDeposito() {
        return deposito;
    }

    /**
     * @return the scorta
     */
    public Double getScorta() {
        return scorta;
    }

    /**
     * @param articolo
     *            the articolo to set
     */
    public void setArticolo(Articolo articolo) {
        this.articolo = articolo;
    }

    /**
     * @param deposito
     *            the deposito to set
     */
    public void setDeposito(DepositoLite deposito) {
        this.deposito = deposito;
    }

    /**
     * @param scorta
     *            the scorta to set
     */
    public void setScorta(Double scorta) {
        this.scorta = scorta;
    }

}
