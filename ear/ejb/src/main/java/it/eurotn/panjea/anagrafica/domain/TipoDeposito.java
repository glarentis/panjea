package it.eurotn.panjea.anagrafica.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;

/**
 *
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Entity
@Audited
@Table(name = "anag_tipo_deposito", uniqueConstraints = @UniqueConstraint(columnNames = "codice") )
@NamedQueries({
        @NamedQuery(name = "TipoDeposito.caricaByCodice", query = "select t from TipoDeposito t where t.codice=:codice") })
public class TipoDeposito extends EntityBase {

    /**
     * Comment for <code>serialVersionUID</code>.
     */
    private static final long serialVersionUID = 1969767618628157463L;

    /**
     * @uml.property name="codice"
     */
    @Column(name = "codice", length = 20)
    @Index(name = "tipoDepositoCodice")
    private String codice;

    /**
     * @return String codice.
     * @uml.property name="codice"
     */
    public String getCodice() {
        return codice;
    }

    /**
     * @param codice
     *            .
     * @uml.property name="codice"
     */
    public void setCodice(String codice) {
        this.codice = codice;
    }

    /**
     *
     * @return String.
     * @author
     */
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("TipoDeposito[");
        buffer.append(super.toString());
        buffer.append(" codice = ").append(codice);
        buffer.append("]");
        return buffer.toString();
    }

}