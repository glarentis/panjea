package it.eurotn.panjea.magazzino.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;

/**
 * Definisce una categoria contabile per il deposito.<br/>
 * Utilizzata per creare l'area contabile dall'area magazzino
 *
 * @author giangi
 */
@Entity
@Audited
@Table(name = "maga_categoria_contabile_deposito")
public class CategoriaContabileDeposito extends EntityBase {

    private static final long serialVersionUID = 1L;

    /**
     * @uml.property name="codiceAzienda"
     */
    @Column(length = 30, nullable = false)
    @Index(name = "index_codiceAzienda")
    private String codiceAzienda = null;

    /**
     * @uml.property name="codice"
     */
    private String codice;

    /**
     * @return codice
     * @uml.property name="codice"
     */
    public String getCodice() {
        return codice;
    }

    /**
     * @return codiceAzienda
     * @uml.property name="codiceAzienda"
     */
    public String getCodiceAzienda() {
        return codiceAzienda;
    }

    /**
     * @param codice
     *            the codice to set
     * @uml.property name="codice"
     */
    public void setCodice(String codice) {
        this.codice = codice;
    }

    /**
     * @param codiceAzienda
     *            the codiceAzienda to set
     * @uml.property name="codiceAzienda"
     */
    public void setCodiceAzienda(String codiceAzienda) {
        this.codiceAzienda = codiceAzienda;
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

        retValue = "CategoriaContabileDeposito ( " + super.toString() + tab + "codiceAzienda = " + this.codiceAzienda
                + tab + "codice = " + this.codice + tab + " )";

        return retValue;
    }
}