package it.eurotn.panjea.vending.domain;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import org.hibernate.envers.Audited;

import it.eurotn.entity.IEntityCodiceAzienda;
import it.eurotn.entity.annotation.EntityConverter;
import it.eurotn.panjea.manutenzioni.domain.ArticoloMI;

@Entity
@Audited
@DiscriminatorValue("DI")
@EntityConverter(properties = "codice,descrizioneLinguaAziendale")
public class Distributore extends ArticoloMI implements IEntityCodiceAzienda {

    private static final long serialVersionUID = -6538362602291410711L;

    @OneToOne(cascade = CascadeType.ALL)
    private DatiVendingDistributore datiVending;

    /**
     * @return the datiVending
     */
    public DatiVendingDistributore getDatiVending() {
        if (datiVending == null) {
            datiVending = new DatiVendingDistributore();
        }
        return datiVending;
    }

    /**
     * @param datiVending
     *            the datiVending to set
     */
    public void setDatiVending(DatiVendingDistributore datiVending) {
        this.datiVending = datiVending;
    }

}
