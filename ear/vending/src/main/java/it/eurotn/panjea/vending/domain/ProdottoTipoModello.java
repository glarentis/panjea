package it.eurotn.panjea.vending.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;

import it.eurotn.panjea.manutenzioni.domain.ProdottoCollegato;

@Entity
@Audited
@DiscriminatorValue("PT")
public class ProdottoTipoModello extends ProdottoCollegato {

    private static final long serialVersionUID = -8683540822432404478L;

    @ManyToOne(fetch = FetchType.LAZY)
    private TipoModello tipoModello;

    /**
     * @return the tipoModello
     */
    public final TipoModello getTipoModello() {
        return tipoModello;
    }

    /**
     * @param tipoModello
     *            the tipoModello to set
     */
    public final void setTipoModello(TipoModello tipoModello) {
        this.tipoModello = tipoModello;
    }
}
