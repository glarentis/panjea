package it.eurotn.panjea.magazzino.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.domain.Deposito;

/**
 * Contiene i dati del deposito relativi al magazzino.
 *
 * @author giangi
 */
@Entity
@Audited
@Table(name = "maga_deposito_magazzino")
@NamedQueries({
        @NamedQuery(name = "DepositoMagazzino.caricaByDeposito", query = "select dep from DepositoMagazzino dep where dep.deposito.id = :paramIdDeposito"),
        @NamedQuery(name = "DepositoMagazzino.caricaCategoriaContabileByDeposito", query = "select dm.categoriaContabileDeposito from DepositoMagazzino dm where dm.deposito.id = :paramIdDeposito") })
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "depositi")
public class DepositoMagazzino extends EntityBase {
    private static final long serialVersionUID = 7147733757231448087L;

    /**
     * @uml.property name="categoriaContabileDeposito"
     * @uml.associationEnd
     */
    @ManyToOne
    private CategoriaContabileDeposito categoriaContabileDeposito;
    /**
     * @uml.property name="deposito"
     * @uml.associationEnd
     */
    @ManyToOne
    private Deposito deposito;

    /**
     * @return categoriaContabileDeposito
     * @uml.property name="categoriaContabileDeposito"
     */
    public CategoriaContabileDeposito getCategoriaContabileDeposito() {
        return categoriaContabileDeposito;
    }

    /**
     * @return deposito
     * @uml.property name="deposito"
     */
    public Deposito getDeposito() {
        return deposito;
    }

    /**
     * @param categoriaContabileDeposito
     *            the categoriaContabileDeposito to set
     * @uml.property name="categoriaContabileDeposito"
     */
    public void setCategoriaContabileDeposito(CategoriaContabileDeposito categoriaContabileDeposito) {
        this.categoriaContabileDeposito = categoriaContabileDeposito;
    }

    /**
     * @param deposito
     *            the deposito to set
     * @uml.property name="deposito"
     */
    public void setDeposito(Deposito deposito) {
        this.deposito = deposito;
    }

}
