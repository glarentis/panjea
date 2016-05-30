package it.eurotn.panjea.vending.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Cap;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Localita;

@SuppressWarnings("serial")
@Entity
@Audited
@Table(name = "vend_asl")
public class Asl extends EntityBase {

    @ManyToOne
    private AnagraficaAsl anagraficaAsl;

    @ManyToOne
    private Cap cap;

    @ManyToOne
    private Localita localita;

    public AnagraficaAsl getAnagraficaAsl() {
        return anagraficaAsl;
    }

    public Cap getCap() {
        return cap;
    }

    public Localita getLocalita() {
        return localita;
    }

    public void setAnagraficaAsl(AnagraficaAsl anagraficaAsl) {
        this.anagraficaAsl = anagraficaAsl;
    }

    public void setCap(Cap cap) {
        this.cap = cap;
    }

    public void setLocalita(Localita localita) {
        this.localita = localita;
    }
}