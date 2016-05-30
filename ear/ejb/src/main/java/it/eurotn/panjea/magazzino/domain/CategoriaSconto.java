package it.eurotn.panjea.magazzino.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;

/**
 * Classe per gestire una categoria sconto. La classe <code>CategoriaSconto</code> è astratta perchè viene poi
 * specializzata con le classi <code>CategoriaScontoSede</code> e <code>CategoriaScontoArticolo</code>
 *
 * @author fattazzo
 */
@Entity
@Audited
@Table(name = "maga_categorie_sconto")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO_CATEGORIA", discriminatorType = DiscriminatorType.STRING, length = 1)
@DiscriminatorValue("C")
public abstract class CategoriaSconto extends EntityBase {

    private static final long serialVersionUID = -8065627198659397513L;

    /**
     * @uml.property name="codiceAzienda"
     */
    @Column(length = 10, nullable = false)
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

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("CategoriaSconto[");
        buffer.append("codice = ").append(codice);
        buffer.append("]");
        return buffer.toString();
    }
}
