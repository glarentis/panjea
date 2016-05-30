package it.eurotn.panjea.magazzino.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import it.eurotn.util.PanjeaEJBUtil;

/**
 * Contiene i valori dinamici per una riga di magazzino.<br>
 * I valori servono per calcolare la qta della riga<br>
 * Vengono creati quando si sceglie un articolo e si inserisce la sua formula<br>
 * di trasformazione. Nella formula ci sono {@link TipoAttributo} e se il valore di {@link AttributoArticolo} è null il
 * valore può essere modificato
 *
 * @author giangi
 */
@Entity(name = "it.eurotn.panjea.magazzino.domain.AttributoRiga")
@Audited
@Table(name = "maga_attributi_riga")
public class AttributoRiga extends AttributoRigaArticolo implements Cloneable {
    private static final long serialVersionUID = 1L;

    @ManyToOne(optional = false)
    private RigaArticolo rigaArticolo;

    @Override
    public Object clone() {
        AttributoRiga attributoRiga = new AttributoRiga();
        PanjeaEJBUtil.copyProperties(attributoRiga, this);
        attributoRiga.setId(null);
        return attributoRiga;
    }

    /**
     * @return the rigaArticolo
     */
    public RigaArticolo getRigaArticolo() {
        return rigaArticolo;
    }

    /**
     * @param rigaArticolo
     *            the rigaArticolo to set
     */
    public void setRigaArticolo(RigaArticolo rigaArticolo) {
        this.rigaArticolo = rigaArticolo;
    }

    /**
     * Costruisce una <code>String</code> con tutti gli attributi accodati con questo formato nome attributo = valore.
     * 
     * @return a <code>String</code> come risultato di questo oggetto
     */
    @Override
    public String toString() {

        StringBuilder retValue = new StringBuilder();

        retValue.append("AttributoRiga[ ").append(getTipoAttributo().getCodice()).append(" valore = ")
                .append(this.getValore()).append(" updatable = ").append(this.isUpdatable()).append(" ]");
        return retValue.toString();
    }

}
