package it.eurotn.panjea.manutenzioni.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;

@Entity
@Audited
@DiscriminatorValue("PI")
public class ProdottoInstallazione extends ProdottoCollegato {

    private static final long serialVersionUID = 1475006223634871531L;

    @ManyToOne(fetch = FetchType.LAZY)
    private Installazione installazione;

    /**
     * @return the installazione
     */
    public final Installazione getInstallazione() {
        return installazione;
    }

    /**
     * @param installazione
     *            the installazione to set
     */
    public final void setInstallazione(Installazione installazione) {
        this.installazione = installazione;
    }
}
