package it.eurotn.panjea.vending.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;

@Entity
@Audited
@Table(name = "vend_movimenti_righe_cassa", uniqueConstraints = @UniqueConstraint(columnNames = { "movimentoCassa_id",
        "gettone_id" }) )
public class RigaMovimentoCassa extends EntityBase {

    private static final long serialVersionUID = -8888144928807307851L;

    @ManyToOne
    private MovimentoCassa movimentoCassa;

    @ManyToOne
    private Gettone gettone;

    @Column(nullable = false)
    private Integer quantitaEntrata;

    @Column(nullable = false)
    private Integer quantitaUscita;

    {
        quantitaEntrata = 0;
        quantitaUscita = 0;
    }

    /**
     * @return the gettone
     */
    public Gettone getGettone() {
        return gettone;
    }

    /**
     * @return the movimentoCassa
     */
    public MovimentoCassa getMovimentoCassa() {
        return movimentoCassa;
    }

    /**
     * @return the quantitaEntrata
     */
    public Integer getQuantitaEntrata() {
        return quantitaEntrata;
    }

    /**
     * @return the quantitaUscita
     */
    public Integer getQuantitaUscita() {
        return quantitaUscita;
    }

    /**
     * @param gettone
     *            the gettone to set
     */
    public void setGettone(Gettone gettone) {
        this.gettone = gettone;
    }

    /**
     * @param movimentoCassa
     *            the movimentoCassa to set
     */
    public void setMovimentoCassa(MovimentoCassa movimentoCassa) {
        this.movimentoCassa = movimentoCassa;
    }

    /**
     * @param quantitaEntrata
     *            the quantitaEntrata to set
     */
    public void setQuantitaEntrata(Integer quantitaEntrata) {
        this.quantitaEntrata = quantitaEntrata;
    }

    /**
     * @param quantitaUscita
     *            the quantitaUscita to set
     */
    public void setQuantitaUscita(Integer quantitaUscita) {
        this.quantitaUscita = quantitaUscita;
    }
}
