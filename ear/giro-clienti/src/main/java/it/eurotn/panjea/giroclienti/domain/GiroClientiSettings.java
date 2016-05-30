package it.eurotn.panjea.giroclienti.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.ordini.domain.documento.TipoAreaOrdine;

@Entity
@Audited
@Table(name = "gcli_settings")
@NamedQueries({
        @NamedQuery(name = "GiroClientiSettings.caricaAll", query = "from GiroClientiSettings gclis where gclis.codiceAzienda = :codiceAzienda", hints = {
                @QueryHint(name = "org.hibernate.cacheable", value = "true"),
                @QueryHint(name = "org.hibernate.cacheRegion", value = "ordiniSettings") }) })
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "ordiniSettings")
public class GiroClientiSettings extends EntityBase {

    private static final long serialVersionUID = -5626237553661797185L;

    @Column(length = 10, nullable = false)
    private String codiceAzienda;

    @ManyToOne
    private TipoAreaOrdine tipoAreaOrdineScheda;

    /**
     * @return the codiceAzienda
     */
    public String getCodiceAzienda() {
        return codiceAzienda;
    }

    /**
     * @return the tipoAreaOrdineScheda
     */
    public TipoAreaOrdine getTipoAreaOrdineScheda() {
        return tipoAreaOrdineScheda;
    }

    /**
     * @param codiceAzienda
     *            the codiceAzienda to set
     */
    public void setCodiceAzienda(String codiceAzienda) {
        this.codiceAzienda = codiceAzienda;
    }

    /**
     * @param tipoAreaOrdineScheda
     *            the tipoAreaOrdineScheda to set
     */
    public void setTipoAreaOrdineScheda(TipoAreaOrdine tipoAreaOrdineScheda) {
        this.tipoAreaOrdineScheda = tipoAreaOrdineScheda;
    }
}
