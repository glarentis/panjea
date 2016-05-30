package it.eurotn.panjea.vending.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;

@Entity
@Audited
@Table(name = "vend_movimenti_cassa")
@NamedQueries({
        @NamedQuery(name = "MovimentoCassa.cancellaByCassaId", query = "delete from MovimentoCassa mc where mc.cassa.id = :paramCassaId ") })
public class MovimentoCassa extends EntityBase {

    public static class RigheMovimentoCassaComparator implements Comparator<RigaMovimentoCassa>, Serializable {

        private static final long serialVersionUID = 447384544203314103L;

        @Override
        public int compare(RigaMovimentoCassa o1, RigaMovimentoCassa o2) {
            BigDecimal valGettone1 = o1.getGettone() != null ? o1.getGettone().getValore() : BigDecimal.ZERO;
            BigDecimal valGettone2 = o2.getGettone() != null ? o2.getGettone().getValore() : BigDecimal.ZERO;

            return valGettone1.compareTo(valGettone2);
        }

    }

    private static final long serialVersionUID = 3323929100664463447L;

    @ManyToOne
    private Cassa cassa;

    private Date data;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "movimentoCassa")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Sort(type = SortType.COMPARATOR, comparator = RigheMovimentoCassaComparator.class)
    private Set<RigaMovimentoCassa> righe;

    private boolean apertura;

    @Transient
    private Cassa cassaDestinazione;

    // Transienti perchè calcolati al momento del caricamento dei movimenti
    @Transient
    private BigDecimal totaleEntrate;

    @Transient
    private BigDecimal totaleUscite;

    @Transient
    private BigDecimal totale;

    {
        apertura = false;
    }

    /**
     * @return the cassa
     */
    public Cassa getCassa() {
        return cassa;
    }

    /**
     * @return the cassaDestinazione
     */
    public Cassa getCassaDestinazione() {
        return cassaDestinazione;
    }

    /**
     * @return the data
     */
    public Date getData() {
        return data;
    }

    /**
     * @return the righe
     */
    public Set<RigaMovimentoCassa> getRighe() {
        return righe;
    }

    /**
     * @return the totale
     */
    public BigDecimal getTotale() {
        if (totale == null) {
            totale = ObjectUtils.defaultIfNull(totaleEntrate, BigDecimal.ZERO)
                    .subtract(ObjectUtils.defaultIfNull(totaleUscite, BigDecimal.ZERO));
        }

        return totale;
    }

    /**
     * @return the totaleEntrate
     */
    public BigDecimal getTotaleEntrate() {
        return totaleEntrate;
    }

    /**
     * @return the totaleUscite
     */
    public BigDecimal getTotaleUscite() {
        return totaleUscite;
    }

    /**
     * @return the apertura
     */
    public boolean isApertura() {
        return apertura;
    }

    /**
     * @param apertura
     *            the apertura to set
     */
    public void setApertura(boolean apertura) {
        this.apertura = apertura;
    }

    /**
     * @param cassa
     *            the cassa to set
     */
    public void setCassa(Cassa cassa) {
        this.cassa = cassa;
    }

    /**
     * @param cassaDestinazione
     *            the cassaDestinazione to set
     */
    public void setCassaDestinazione(Cassa cassaDestinazione) {
        this.cassaDestinazione = cassaDestinazione;
    }

    /**
     * @param data
     *            the data to set
     */
    public void setData(Date data) {
        this.data = data;
    }

    /**
     * @param righe
     *            the righe to set
     */
    public void setRighe(Set<RigaMovimentoCassa> righe) {
        this.righe = righe;
    }

    /**
     * @param totaleEntrate
     *            the totaleEntrate to set
     */
    public void setTotaleEntrate(BigDecimal totaleEntrate) {
        this.totaleEntrate = totaleEntrate;
        this.totale = null;
    }

    /**
     * @param totaleUscite
     *            the totaleUscite to set
     */
    public void setTotaleUscite(BigDecimal totaleUscite) {
        this.totaleUscite = totaleUscite;
        this.totale = null;
    }
}
