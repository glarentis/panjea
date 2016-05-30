package it.eurotn.panjea.magazzino.domain;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;

/**
 * Definisce una categoria magazzino per l'articolo.<br/>
 *
 * @author giangi
 */
@Entity
@Audited
@Table(name = "maga_categoria_commerciale_articolo")
public class CategoriaCommercialeArticolo extends EntityBase {

    private static final long serialVersionUID = 1L;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "categoriaCommercialeArticolo")
    @OrderBy("codice ASC")
    private Set<ArticoloLite> articoli;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "categoriaCommercialeArticolo2")
    @OrderBy("codice ASC")
    private Set<ArticoloLite> articoli2;

    private String codice;

    /**
     * @return Returns the articoli.
     */
    public Set<ArticoloLite> getArticoli() {
        return articoli;
    }

    /**
     * @return the articoli2
     */
    public Set<ArticoloLite> getArticoli2() {
        return articoli2;
    }

    /**
     * @return codice
     */
    public String getCodice() {
        return codice;
    }

    /**
     * @param articoli
     *            The articoli to set.
     */
    public void setArticoli(Set<ArticoloLite> articoli) {
        this.articoli = articoli;
    }

    /**
     * @param articoli2
     *            the articoli2 to set
     */
    public void setArticoli2(Set<ArticoloLite> articoli2) {
        this.articoli2 = articoli2;
    }

    /**
     * @param codice
     *            the codice to set
     */
    public void setCodice(String codice) {
        this.codice = codice;
    }

    /**
     * Constructs a <code>String</code> with all attributes in name = value format.
     * 
     * @return a <code>String</code> representation of this object.
     */
    @Override
    public String toString() {
        final String tab = "    ";

        String retValue = "";

        retValue = "CategoriaCommercialeArticolo ( " + super.toString() + tab + "codice = " + this.codice + tab + " )";

        return retValue;
    }

}
