package it.eurotn.panjea.magazzino.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.contabilita.domain.SottoConto;

/**
 * Recupera un sottoConto dalle categorie contabilizzazione.
 *
 * @author giangi
 */

@Entity
@Audited
@Table(name = "maga_sottoconto_contabilizzazione")
@NamedQueries({
        @NamedQuery(name = "SottoContoContabilizzazione.caricaByTipoEconomico", query = "select scc from SottoContoContabilizzazione scc left join fetch scc.sottoConto left join fetch scc.sottoContoNotaAccredito left join fetch scc.categoriaContabileDeposito left join fetch scc.categoriaContabileSedeMagazzino left join fetch scc.categoriaContabileArticolo where scc.tipoEconomico = :paramTipoEconomico and scc.codiceAzienda=:paramCodiceAzienda", hints = {
                @QueryHint(name = "org.hibernate.cacheable", value = "true"),
                @QueryHint(name = "org.hibernate.cacheRegion", value = "sottoContiContabilizzazione") }) })
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "sottoContiContabilizzazione")
public class SottoContoContabilizzazione extends EntityBase {

    public enum ETipoEconomico {
        /**
         * @uml.property name="rICAVO"
         * @uml.associationEnd
         */
        RICAVO, /**
                 * @uml.property name="cOSTO"
                 * @uml.associationEnd
                 */
        COSTO
    }

    private static final long serialVersionUID = 5064366302679669422L;

    /**
     * @uml.property name="categoriaContabileArticolo"
     * @uml.associationEnd
     */
    @ManyToOne(optional = true)
    private CategoriaContabileArticolo categoriaContabileArticolo;
    /**
     * @uml.property name="categoriaContabileDeposito"
     * @uml.associationEnd
     */
    @ManyToOne(optional = true)
    private CategoriaContabileDeposito categoriaContabileDeposito;
    /**
     * @uml.property name="categoriaContabileSedeMagazzino"
     * @uml.associationEnd
     */
    @ManyToOne(optional = true)
    private CategoriaContabileSedeMagazzino categoriaContabileSedeMagazzino;
    /**
     * @uml.property name="sottoConto"
     * @uml.associationEnd
     */
    @ManyToOne(optional = false)
    private SottoConto sottoConto;
    /**
     * @uml.property name="sottoContoNotaAccredito"
     * @uml.associationEnd
     */
    @ManyToOne(optional = true)
    private SottoConto sottoContoNotaAccredito;

    /**
     * @uml.property name="sottoContoNull"
     * @uml.associationEnd
     */
    @Transient
    private SottoConto sottoContoNull;

    /**
     * @uml.property name="tipoEconomico"
     * @uml.associationEnd
     */
    private ETipoEconomico tipoEconomico;

    /**
     * @uml.property name="codiceAzienda"
     */
    @Column(length = 30, nullable = false)
    @Index(name = "index_codiceAzienda")
    private String codiceAzienda = null;

    /**
     * @return categoriaContabileArticolo
     * @uml.property name="categoriaContabileArticolo"
     */
    public CategoriaContabileArticolo getCategoriaContabileArticolo() {
        return categoriaContabileArticolo;
    }

    /**
     * @return categoriaContabileDeposito
     * @uml.property name="categoriaContabileDeposito"
     */
    public CategoriaContabileDeposito getCategoriaContabileDeposito() {
        return categoriaContabileDeposito;
    }

    /**
     * @return categoriaContabileSedeMagazzino
     * @uml.property name="categoriaContabileSedeMagazzino"
     */
    public CategoriaContabileSedeMagazzino getCategoriaContabileSedeMagazzino() {
        return categoriaContabileSedeMagazzino;
    }

    /**
     * @return codiceAzienda
     * @uml.property name="codiceAzienda"
     */
    public String getCodiceAzienda() {
        return codiceAzienda;
    }

    /**
     * @return sottoConto
     * @uml.property name="sottoConto"
     */
    public SottoConto getSottoConto() {
        return sottoConto;
    }

    /**
     * Se non esiste un sotto conto per la nota di accredito ritorno il sotto conto standard.
     * 
     * @return sottoConto
     * @uml.property name="sottoContoNotaAccredito"
     */
    public SottoConto getSottoContoNotaAccredito() {
        if (sottoContoNotaAccredito == null) {
            return sottoConto;
        }
        return sottoContoNotaAccredito;
    }

    /**
     * @return sottoContoNull
     * @uml.property name="sottoContoNull"
     */
    public SottoConto getSottoContoNull() {
        if (sottoContoNull == null) {
            sottoContoNull = new SottoConto();
            // setto l'id a -1
            sottoContoNull.setId(null);
            // setto il codice vuoto
            sottoContoNull.setCodice("");
        }

        return sottoContoNull;
    }

    /**
     * @return tipoEconomico
     * @uml.property name="tipoEconomico"
     */
    public ETipoEconomico getTipoEconomico() {
        return tipoEconomico;
    }

    /**
     * @param categoriaContabileArticolo
     *            the categoriaContabileArticolo to set
     * @uml.property name="categoriaContabileArticolo"
     */
    public void setCategoriaContabileArticolo(CategoriaContabileArticolo categoriaContabileArticolo) {
        this.categoriaContabileArticolo = categoriaContabileArticolo;
    }

    /**
     * @param categoriaContabileDeposito
     *            the categoriaContabileDeposito to set
     * @uml.property name="categoriaContabileDeposito"
     */
    public void setCategoriaContabileDeposito(CategoriaContabileDeposito categoriaContabileDeposito) {
        this.categoriaContabileDeposito = categoriaContabileDeposito;
    }

    /**
     * @param categoriaContabileSedeMagazzino
     *            the categoriaContabileSedeMagazzino to set
     * @uml.property name="categoriaContabileSedeMagazzino"
     */
    public void setCategoriaContabileSedeMagazzino(CategoriaContabileSedeMagazzino categoriaContabileSedeMagazzino) {
        this.categoriaContabileSedeMagazzino = categoriaContabileSedeMagazzino;
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
     * @param sottoConto
     *            the sottoConto to set
     * @uml.property name="sottoConto"
     */
    public void setSottoConto(SottoConto sottoConto) {
        this.sottoConto = sottoConto;
    }

    /**
     * @param sottoContoNotaAccredito
     *            the sottoContoNotaAccredito to set
     * @uml.property name="sottoContoNotaAccredito"
     */
    public void setSottoContoNotaAccredito(SottoConto sottoContoNotaAccredito) {
        this.sottoContoNotaAccredito = sottoContoNotaAccredito;
    }

    /**
     * @param tipoEconomico
     *            the tipoEconomico to set
     * @uml.property name="tipoEconomico"
     */
    public void setTipoEconomico(ETipoEconomico tipoEconomico) {
        this.tipoEconomico = tipoEconomico;
    }
}
