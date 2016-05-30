package it.eurotn.panjea.corrispettivi.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;

import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;

/**
 * @author fattazzo
 */
@Entity
@Audited
@Table(name = "cont_corrispettivi")
@NamedQueries({
        @NamedQuery(name = "Corrispettivo.caricaCorrispettivo", query = "select c from Corrispettivo c where c.data = :paramData and c.codiceAzienda = :paramCodiceAzienda and c.tipoDocumento.id = :paramTipoDocumentoId"),
        @NamedQuery(name = "Corrispettivo.caricaCorrispettiviGroupByCodiceIva", query = "select sum(r.importo), r.codiceIva from Corrispettivo c inner join c.righeCorrispettivo r where c.data >= :paramDataIni and c.data <= :paramDataFin and c.codiceAzienda = :paramCodiceAzienda and c.tipoDocumento.id = :paramTipoDocumentoId and (c.totale is not null and c.totale <> 0) group by r.codiceIva") })
public class Corrispettivo extends EntityBase implements java.io.Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1742982515647196517L;

    /**
     * @uml.property name="codiceAzienda"
     */
    @Column(length = 10, nullable = false)
    @Index(name = "index_codiceAzienda")
    private String codiceAzienda;

    /**
     * @uml.property name="data"
     */
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date data;

    /**
     * @uml.property name="totale"
     */
    private BigDecimal totale;

    /**
     * @uml.property name="righeCorrispettivo"
     */
    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    private List<RigaCorrispettivo> righeCorrispettivo;

    /**
     * @uml.property name="tipoDocumento"
     * @uml.associationEnd
     */
    @ManyToOne
    private TipoDocumento tipoDocumento;

    /**
     * Costruttore.
     *
     */
    public Corrispettivo() {
        super();
        this.totale = BigDecimal.ZERO;
    }

    /**
     * @return codiceAzienda
     * @uml.property name="codiceAzienda"
     */
    public String getCodiceAzienda() {
        return codiceAzienda;
    }

    /**
     * @return data
     * @uml.property name="data"
     */
    public Date getData() {
        return data;
    }

    /**
     * @return righeCorrispettivo
     * @uml.property name="righeCorrispettivo"
     */
    public List<RigaCorrispettivo> getRigheCorrispettivo() {
        return righeCorrispettivo;
    }

    /**
     * @return tipoDocumento
     * @uml.property name="tipoDocumento"
     */
    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    /**
     * @return totale
     * @uml.property name="totale"
     */
    public BigDecimal getTotale() {
        return totale;
    }

    /**
     * Calcola il totale dell'importo delle righe del corrispettivo.
     *
     * @return importo delle righe
     */
    public BigDecimal getTotaleRighe() {

        BigDecimal totaleRighe = BigDecimal.ZERO;

        for (RigaCorrispettivo rigaCorrispettivo : getRigheCorrispettivo()) {
            if (rigaCorrispettivo.getImporto() != null) {
                totaleRighe = totaleRighe.add(rigaCorrispettivo.getImporto());
            }
        }

        return totaleRighe;
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
     * @param data
     *            the data to set
     * @uml.property name="data"
     */
    public void setData(Date data) {
        this.data = data;
    }

    /**
     * @param righeCorrispettivo
     *            the righeCorrispettivo to set
     * @uml.property name="righeCorrispettivo"
     */
    public void setRigheCorrispettivo(List<RigaCorrispettivo> righeCorrispettivo) {
        this.righeCorrispettivo = righeCorrispettivo;
    }

    /**
     * @param tipoDocumento
     *            the tipoDocumento to set
     * @uml.property name="tipoDocumento"
     */
    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    /**
     * @param totale
     *            the totale to set
     * @uml.property name="totale"
     */
    public void setTotale(BigDecimal totale) {
        this.totale = totale;
    }

}
