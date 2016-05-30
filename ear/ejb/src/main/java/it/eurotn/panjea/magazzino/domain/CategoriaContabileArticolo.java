package it.eurotn.panjea.magazzino.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;

/**
 * Definisce una categoria contabile per l'articolo.<br/>
 * Utilizzata per creare l'area contabile dall'area magazzino
 *
 * @author giangi
 */
@Entity
@Audited
@Table(name = "maga_categoria_contabile_articolo")
public class CategoriaContabileArticolo extends EntityBase {

    private static final long serialVersionUID = 1L;

    @Column(length = 30, nullable = false)
    @Index(name = "index_codiceAzienda")
    private String codiceAzienda = null;

    private String codice;

    /**
     * @return codice
     */
    public String getCodice() {
        return codice;
    }

    /**
     * @return codiceAzienda
     */
    public String getCodiceAzienda() {
        return codiceAzienda;
    }

    /**
     * @param codice
     *            the codice to set
     */
    public void setCodice(String codice) {
        this.codice = codice;
    }

    /**
     * @param codiceAzienda
     *            the codiceAzienda to set
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

        retValue = "CategoriaContabileArticolo ( " + super.toString() + tab + "codiceAzienda = " + this.codiceAzienda
                + tab + "codice = " + this.codice + tab + " )";

        return retValue;
    }

}
