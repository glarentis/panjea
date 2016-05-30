package it.eurotn.panjea.anagrafica.domain.datigeografici;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import it.eurotn.entity.annotation.EntityConverter;

@Entity
@Audited
@Table(name = "geog_cap")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "cap")
@EntityConverter(properties = "descrizione")
public class Cap extends IdentificativoLocalita {

    private static final long serialVersionUID = 315606398896566909L;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "cap")
    private Set<Localita> localita;

    /**
     * Costruttore.
     */
    public Cap() {
        super();
        initialize();
    }

    /**
     * @return the localita
     */
    public Set<Localita> getLocalita() {
        return localita;
    }

    /**
     * Init delle località.
     */
    private void initialize() {
        localita = new HashSet<>();
    }

    /**
     * @param localita
     *            the localita to set
     */
    public void setLocalita(java.util.Set<Localita> localita) {
        this.localita = localita;
    }

}
