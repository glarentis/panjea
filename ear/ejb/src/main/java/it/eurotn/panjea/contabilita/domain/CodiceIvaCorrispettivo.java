package it.eurotn.panjea.contabilita.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.entity.IEntityCodiceAzienda;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.domain.CodiceIva;

/**
 * @author Fattazzo <g.fattarsi@eurotn.it>
 */
@Entity
@Audited
@Table(name = "cont_codici_iva_corrispettivi")
@NamedQueries({
        @NamedQuery(name = "CodiceIvaCorrispettivo.caricaCodiciByTipoDocumento", query = "select c from CodiceIvaCorrispettivo c where c.tipoDocumento.id = :paramIdTipoDocumento and c.codiceAzienda = :paramCodiceAzienda order by c.ordinamento") })
public class CodiceIvaCorrispettivo extends EntityBase implements java.io.Serializable, IEntityCodiceAzienda {

    /**
     * 
     */
    private static final long serialVersionUID = 7002481029704296039L;

    /**
     * @uml.property name="codiceAzienda"
     */
    @Column(length = 10, nullable = false)
    @Index(name = "index_codiceAzienda")
    private String codiceAzienda;

    /**
     * @uml.property name="tipoDocumento"
     * @uml.associationEnd
     */
    @ManyToOne(optional = false)
    private TipoDocumento tipoDocumento;

    /**
     * @uml.property name="codiceIva"
     * @uml.associationEnd
     */
    @ManyToOne
    private CodiceIva codiceIva;

    /**
     * @uml.property name="ordinamento"
     */
    private int ordinamento;

    /**
     * Costruttore.
     * 
     */
    public CodiceIvaCorrispettivo() {
        initialize();
    }

    /**
     * @return codiceAzienda
     * @uml.property name="codiceAzienda"
     */
    public String getCodiceAzienda() {
        return codiceAzienda;
    }

    /**
     * @return codiceIva
     * @uml.property name="codiceIva"
     */
    public CodiceIva getCodiceIva() {
        return codiceIva;
    }

    /**
     * @return ordinamento
     * @uml.property name="ordinamento"
     */
    public int getOrdinamento() {
        return ordinamento;
    }

    /**
     * @return tipoDocumento
     * @uml.property name="tipoDocumento"
     */
    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    /**
     * Inizializza i valori di default.
     */
    private void initialize() {
        this.codiceIva = new CodiceIva();
        this.codiceIva.setId(-1);
        this.codiceIva.setDescrizioneInterna("");

        this.tipoDocumento = new TipoDocumento();
        this.tipoDocumento.setId(-1);
        this.tipoDocumento.setDescrizione("");

        this.ordinamento = 0;
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
     * @param codiceIva
     *            the codiceIva to set
     * @uml.property name="codiceIva"
     */
    public void setCodiceIva(CodiceIva codiceIva) {
        this.codiceIva = codiceIva;
    }

    /**
     * @param ordinamento
     *            the ordinamento to set
     * @uml.property name="ordinamento"
     */
    public void setOrdinamento(int ordinamento) {
        this.ordinamento = ordinamento;
    }

    /**
     * @param tipoDocumento
     *            the tipoDocumento to set
     * @uml.property name="tipoDocumento"
     */
    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

}
