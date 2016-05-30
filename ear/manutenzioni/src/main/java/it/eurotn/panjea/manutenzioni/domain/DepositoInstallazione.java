package it.eurotn.panjea.manutenzioni.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import org.hibernate.envers.Audited;

import it.eurotn.panjea.anagrafica.domain.Deposito;

@Entity
@Audited
@DiscriminatorValue("IN")
@NamedQueries({
        @NamedQuery(name = "DepositoInstallazione.caricaDepositoPerSedeEntita", query = "select dep from DepositoInstallazione dep where dep.sedeEntita= :paramSede ") })
public class DepositoInstallazione extends Deposito {
    private static final long serialVersionUID = 4956554022014061577L;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "deposito")
    private Set<Installazione> installazioni;

    /**
     * @return Returns the installazioni.
     */
    public Set<Installazione> getInstallazioni() {
        return installazioni;
    }

}
