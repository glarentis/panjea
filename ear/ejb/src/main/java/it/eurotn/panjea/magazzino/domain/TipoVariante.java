package it.eurotn.panjea.magazzino.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;

/**
 * @author fattazzo
 */
@Entity
@Audited
@DiscriminatorValue("V")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "tipoAttributo")
public class TipoVariante extends TipoAttributo {

    private static final long serialVersionUID = -8517313637304904490L;

    /**
     * @uml.property name="valoriVarianti"
     */
    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
    private List<ValoreVariante> valoriVarianti;

    /**
     * Costruttore.
     * 
     */
    public TipoVariante() {
        super();
        initialize();
    }

    /**
     * @return valoriVarianti
     * @uml.property name="valoriVarianti"
     */
    public List<ValoreVariante> getValoriVarianti() {
        return valoriVarianti;
    }

    /**
     * Inizializza i valori di default.
     */
    private void initialize() {
        this.valoriVarianti = new ArrayList<ValoreVariante>();
    }

    /**
     * @param valoriVarianti
     *            the valoriVarianti to set
     * @uml.property name="valoriVarianti"
     */
    public void setValoriVarianti(List<ValoreVariante> valoriVarianti) {
        this.valoriVarianti = valoriVarianti;
    }

}
