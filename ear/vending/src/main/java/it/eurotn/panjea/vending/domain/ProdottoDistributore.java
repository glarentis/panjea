package it.eurotn.panjea.vending.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;

import it.eurotn.panjea.manutenzioni.domain.ProdottoCollegato;

@Entity
@Audited
@DiscriminatorValue("PD")
public class ProdottoDistributore extends ProdottoCollegato {

    private static final long serialVersionUID = 3012910385130854670L;

    @ManyToOne(fetch = FetchType.LAZY)
    private DatiVendingDistributore datiVendingDistributore;

    /**
     * @return the datiVendingDistributore
     */
    public DatiVendingDistributore getDatiVendingDistributore() {
        return datiVendingDistributore;
    }

    /**
     * @param datiVendingDistributore
     *            the datiVendingDistributore to set
     */
    public void setDatiVendingDistributore(DatiVendingDistributore datiVendingDistributore) {
        this.datiVendingDistributore = datiVendingDistributore;
    }
}
