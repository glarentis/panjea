package it.eurotn.panjea.anagrafica.domain.lite;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.QueryHint;
import javax.persistence.Table;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.anagrafica.domain.TipoDeposito;
import it.eurotn.panjea.magazzino.domain.MezzoTrasporto;

/**
 *
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Entity
@Audited
@Table(name = "anag_depositi")
@NamedQuery(name = "DepositoLite.caricaDepositiAzienda", query = " select dep from DepositoLite dep where dep.codiceAzienda=:paramCodiceAzienda ", hints = {
        @QueryHint(name = "org.hibernate.cacheable", value = "true"),
        @QueryHint(name = "org.hibernate.cacheRegion", value = "depositi") })
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "depositi")
public class DepositoLite extends EntityBase {

    private static final long serialVersionUID = 8769635049677926044L;

    /**
     * @uml.property name="codice"
     */
    @Column(length = 10, nullable = false)
    @Index(name = "codice")
    private String codice;

    @Column(name = "TIPO")
    private String tipo;

    @Column
    private Boolean attivo;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "deposito")
    private List<MezzoTrasporto> mezziTrasporto;

    /**
     * @uml.property name="codiceAzienda"
     */
    @Column(length = 10, nullable = false)
    @Index(name = "index_codiceAzienda")
    private String codiceAzienda = null;

    /**
     * @uml.property name="principale"
     */
    @Column
    private boolean principale;

    /**
     * @uml.property name="tipoDeposito"
     * @uml.associationEnd
     */
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private TipoDeposito tipoDeposito;

    /**
     * @uml.property name="descrizione"
     */
    @Column(length = 30)
    private String descrizione;

    /**
     * Default constructor.
     */
    public DepositoLite() {
    }

    /**
     * crea un deposito a partire di un depositoLite.
     *
     * @return deposito.
     *
     */
    public Deposito creaProxy() {
        Deposito deposito = new Deposito();
        deposito.setId(this.getId());
        return deposito;
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        DepositoLite other = (DepositoLite) obj;
        if (codice == null) {
            if (other.codice != null) {
                return false;
            }
        } else if (!codice.equals(other.codice)) {
            return false;
        }
        if (codiceAzienda == null) {
            if (other.codiceAzienda != null) {
                return false;
            }
        } else if (!codiceAzienda.equals(other.codiceAzienda)) {
            return false;
        }
        if (descrizione == null) {
            if (other.descrizione != null) {
                return false;
            }
        } else if (!descrizione.equals(other.descrizione)) {
            return false;
        }
        return true;
    }

    /**
     *
     * @return attivo field
     */
    public Boolean getAttivo() {
        return attivo;
    }

    /**
     * @return Returns the codice.
     * @uml.property name="codice"
     */
    public String getCodice() {
        return codice;
    }

    /**
     * @return Returns the codiceAzienda.
     * @uml.property name="codiceAzienda"
     */
    public String getCodiceAzienda() {
        return codiceAzienda;
    }

    /**
     * @return Returns the descrizione.
     * @uml.property name="descrizione"
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * @return Returns the tipo.
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * @return Returns the tipoDeposito.
     * @uml.property name="tipoDeposito"
     */
    public TipoDeposito getTipoDeposito() {
        if (tipoDeposito == null) {
            tipoDeposito = new TipoDeposito();
        }
        return tipoDeposito;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((isNew()) ? 0 : getId().hashCode());
        result = prime * result + ((codice == null) ? 0 : codice.hashCode());
        result = prime * result + ((codiceAzienda == null) ? 0 : codiceAzienda.hashCode());
        result = prime * result + ((descrizione == null) ? 0 : descrizione.hashCode());
        return result;
    }

    /**
     *
     * @return true se il deposito è legato ad un mezzo di trasporto.
     */
    public boolean isDepositoMezzoTrasporto() {
        return !CollectionUtils.isEmpty(mezziTrasporto);
    }

    /**
     * @return Returns the principale.
     * @uml.property name="principale"
     */
    public boolean isPrincipale() {
        return principale;
    }

    /**
     *
     * @param attivo
     *            set to attivo
     */
    public void setAttivo(Boolean attivo) {
        this.attivo = attivo;
    }

    /**
     * @param codice
     *            The codice to set.
     * @uml.property name="codice"
     */
    public void setCodice(String codice) {
        this.codice = codice;
    }

    /**
     * @param codiceAzienda
     *            The codiceAzienda to set.
     * @uml.property name="codiceAzienda"
     */
    public void setCodiceAzienda(String codiceAzienda) {
        this.codiceAzienda = codiceAzienda;
    }

    /**
     * @param descrizione
     *            The descrizione to set.
     * @uml.property name="descrizione"
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * @param principale
     *            The principale to set.
     * @uml.property name="principale"
     */
    public void setPrincipale(boolean principale) {
        this.principale = principale;
    }

    /**
     * @param tipoDeposito
     *            The tipoDeposito to set.
     * @uml.property name="tipoDeposito"
     */
    public void setTipoDeposito(TipoDeposito tipoDeposito) {
        this.tipoDeposito = tipoDeposito;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("DepositoLite[");
        buffer.append(" codice = ").append(codice);
        buffer.append("]");
        return buffer.toString();
    }

}
