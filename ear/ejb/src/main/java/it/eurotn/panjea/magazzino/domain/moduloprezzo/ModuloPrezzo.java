package it.eurotn.panjea.magazzino.domain.moduloprezzo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;

/**
 * Rappresenta un modulo di calcolo prezzo.<br/>
 * <code>managerName</code> indica il nome jndi del bean che si occupa di calcolare il prezzo per questo modulo.
 *
 * @author fattazzo
 */
@Entity
@Audited
@Table(name = "maga_moduli_prezzo")
@NamedQueries({
        @NamedQuery(name = "ModuloPrezzo.caricaByProvenienza", query = "from ModuloPrezzo mp where mp.codiceAzienda = :paramCodiceAzienda and mp.tipoProvenienza= :provenienza order by mp.ordine ", hints = {
                @QueryHint(name = "org.hibernate.cacheable", value = "true"),
                @QueryHint(name = "org.hibernate.cacheRegion", value = "moduloprezzo") }) })
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "moduloprezzo")
public class ModuloPrezzo extends EntityBase {

    private static final long serialVersionUID = 2810259787357705094L;

    /**
     * @uml.property name="codiceAzienda"
     */
    @Column(length = 10, nullable = false)
    @Index(name = "index_codiceAzienda")
    private String codiceAzienda = null;

    /**
     * @uml.property name="managerName"
     */
    @Column(length = 100)
    private String managerName;

    /**
     * @uml.property name="ordine"
     */
    private Integer ordine;

    /**
     * @uml.property name="descrizione"
     */
    @Column(length = 50)
    private String descrizione;

    /**
     * @uml.property name="tipoProvenienza"
     */
    @Column(length = 30)
    private String tipoProvenienza;

    /**
     * @return codiceAzienda
     * @uml.property name="codiceAzienda"
     */
    public String getCodiceAzienda() {
        return codiceAzienda;
    }

    /**
     * @return descrizione
     * @uml.property name="descrizione"
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * @return managerName
     * @uml.property name="managerName"
     */
    public String getManagerName() {
        return managerName;
    }

    /**
     * @return ordine
     * @uml.property name="ordine"
     */
    public Integer getOrdine() {
        return ordine;
    }

    /**
     * @return the tipoProvenienza
     * @uml.property name="tipoProvenienza"
     */
    public String getTipoProvenienza() {
        return tipoProvenienza;
    }

    /**
     * @param codiceAzienda
     *            the codiceAzienda to set
     * @uml.property name="codiceAzienda"
     */
    public void setCodiceAzienda(String codiceAzienda) {
        this.codiceAzienda = codiceAzienda;
    }

    /**
     * @param managerName
     *            the managerName to set
     * @uml.property name="managerName"
     */
    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    /**
     * @param ordine
     *            the ordine to set
     * @uml.property name="ordine"
     */
    public void setOrdine(Integer ordine) {
        this.ordine = ordine;
    }

    /**
     * @param tipoProvenienza
     *            the tipoProvenienza to set
     * @uml.property name="tipoProvenienza"
     */
    public void setTipoProvenienza(String tipoProvenienza) {
        this.tipoProvenienza = tipoProvenienza;
    }
}
