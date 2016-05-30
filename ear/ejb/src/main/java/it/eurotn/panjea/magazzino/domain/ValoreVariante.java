package it.eurotn.panjea.magazzino.domain;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;

/**
 * @author fattazzo
 */
@Entity
@Audited
@Table(name = "maga_valori_varianti")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO_VALORE_VARIANTE", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("VV")
public abstract class ValoreVariante extends EntityBase {

    static final long serialVersionUID = 1L;

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
     * @return valore del tipo variante
     */
    public abstract Object getValore();

    /**
     * @param codice
     *            the codice to set
     * @uml.property name="codice"
     */
    public void setCodice(String codice) {
        this.codice = codice;
    }

    /**
     * Costruisce una <code>String</code> con tutti gli attributi accodati con questo formato nome attributo = valore.
     * 
     * @return a <code>String</code> come risultato di questo oggetto
     */
    @Override
    public String toString() {

        StringBuilder retValue = new StringBuilder();

        retValue.append("ValoreVariante[ ").append(super.toString()).append(" codice = ").append(this.codice)
                .append(" ]");

        return retValue.toString();
    }

}
