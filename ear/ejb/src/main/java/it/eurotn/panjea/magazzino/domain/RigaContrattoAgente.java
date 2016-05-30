package it.eurotn.panjea.magazzino.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.magazzino.domain.RigaContratto.Azione;

@Entity
@Audited
@Table(name = "maga_righe_contratto_agente", uniqueConstraints = @UniqueConstraint(columnNames = { "rigaContratto_id",
        "agente_id" }) )
public class RigaContrattoAgente extends EntityBase {

    private static final long serialVersionUID = -8685458256428256176L;

    private Azione azione;

    private boolean blocco;

    private boolean ignoraBloccoPrecedente;

    @ManyToOne(optional = true)
    private AgenteLite agente;

    @Column(precision = 19, scale = 6)
    private BigDecimal valoreProvvigione;

    @ManyToOne
    private RigaContratto rigaContratto;

    /**
     * Costruttore.
     * 
     */
    public RigaContrattoAgente() {
        super();
        azione = Azione.VARIAZIONE;
        blocco = false;
        ignoraBloccoPrecedente = false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        RigaContrattoAgente other = (RigaContrattoAgente) obj;
        if (agente == null) {
            if (other.agente != null) {
                return false;
            }
        } else if (!agente.equals(other.agente)) {
            return false;
        }
        if (rigaContratto == null) {
            if (other.rigaContratto != null) {
                return false;
            }
        } else if (!rigaContratto.equals(other.rigaContratto)) {
            return false;
        }
        return true;
    }

    /**
     * @return the agente
     */
    public AgenteLite getAgente() {
        return agente;
    }

    /**
     * @return the azione
     */
    public Azione getAzione() {
        return azione;
    }

    /**
     * @return the rigaContratto
     */
    public RigaContratto getRigaContratto() {
        return rigaContratto;
    }

    /**
     * @return the valoreProvvigione
     */
    public BigDecimal getValoreProvvigione() {
        return valoreProvvigione;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((agente == null) ? 0 : agente.hashCode());
        result = prime * result + ((rigaContratto == null) ? 0 : rigaContratto.hashCode());
        return result;
    }

    /**
     * @return the blocco
     */
    public boolean isBlocco() {
        return blocco;
    }

    /**
     * @return the ignoraBloccoPrecedente
     */
    public boolean isIgnoraBloccoPrecedente() {
        return ignoraBloccoPrecedente;
    }

    /**
     * @param agente
     *            the agente to set
     */
    public void setAgente(AgenteLite agente) {
        this.agente = agente;
    }

    /**
     * @param azione
     *            the azione to set
     */
    public void setAzione(Azione azione) {
        this.azione = azione;
    }

    /**
     * @param blocco
     *            the blocco to set
     */
    public void setBlocco(boolean blocco) {
        this.blocco = blocco;
    }

    /**
     * @param ignoraBloccoPrecedente
     *            the ignoraBloccoPrecedente to set
     */
    public void setIgnoraBloccoPrecedente(boolean ignoraBloccoPrecedente) {
        this.ignoraBloccoPrecedente = ignoraBloccoPrecedente;
    }

    /**
     * @param rigaContratto
     *            the rigaContratto to set
     */
    public void setRigaContratto(RigaContratto rigaContratto) {
        this.rigaContratto = rigaContratto;
    }

    /**
     * @param valoreProvvigione
     *            the valoreProvvigione to set
     */
    public void setValoreProvvigione(BigDecimal valoreProvvigione) {
        this.valoreProvvigione = valoreProvvigione;
    }
}
