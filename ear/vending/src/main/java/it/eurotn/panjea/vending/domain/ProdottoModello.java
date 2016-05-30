package it.eurotn.panjea.vending.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;

import it.eurotn.panjea.manutenzioni.domain.ProdottoCollegato;

@Entity
@Audited
@DiscriminatorValue("PM")
public class ProdottoModello extends ProdottoCollegato {

    private static final long serialVersionUID = 3012910385130854670L;

    @ManyToOne(fetch = FetchType.LAZY)
    private Modello modello;

    /**
     * @return the modello
     */
    public final Modello getModello() {
        return modello;
    }

    /**
     * @param modello
     *            the modello to set
     */
    public final void setModello(Modello modello) {
        this.modello = modello;
    }
}
