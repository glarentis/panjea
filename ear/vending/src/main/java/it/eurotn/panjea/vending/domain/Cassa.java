package it.eurotn.panjea.vending.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.entity.annotation.EntityConverter;

@Entity
@Audited
@Table(name = "vend_casse", uniqueConstraints = @UniqueConstraint(columnNames = { "codice" }) )
@org.hibernate.annotations.Table(appliesTo = "vend_casse", indexes = {
        @Index(name = "IdxCodice", columnNames = { "codice" }) })
@EntityConverter(properties = "codice,descrizione")
public class Cassa extends EntityBase {

    public enum Tipologia {
        CENTRALE, OPERATORE, ESTERNA
    }

    private static final long serialVersionUID = 9147858596171533312L;

    @Column(length = 15, nullable = false)
    private String codice;

    @Column(length = 100, nullable = false)
    private String descrizione;

    @Column(nullable = false)
    private Tipologia tipologia;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "cassa")
    @OrderBy("data")
    private Set<MovimentoCassa> movimenti;

    // Transienti perchè calcolati al momento delle casse
    @Transient
    private BigDecimal totaleIniziale;

    @Transient
    private BigDecimal totaleEntrate;

    @Transient
    private BigDecimal totaleUscite;

    @Transient
    private BigDecimal totale;

    @Transient
    private List<SituazioneCassa> situazioneCassa;

    private Collection<SituazioneCassa> createSituazioneCassa() {
        Map<Gettone, SituazioneCassa> map = new HashMap<>();

        MovimentoCassa movimentoApertura = getMovimentoApertura();
        if (movimentoApertura != null) {
            BigDecimal totEntrate = BigDecimal.ZERO;
            BigDecimal totUscite = BigDecimal.ZERO;
            for (RigaMovimentoCassa riga : movimentoApertura.getRighe()) {
                SituazioneCassa situazione = new SituazioneCassa();
                situazione.setGettone(riga.getGettone());
                situazione.setQuantitaIniziale(riga.getQuantitaEntrata() - riga.getQuantitaUscita());
                map.put(riga.getGettone(), situazione);

                totEntrate = totEntrate
                        .add(situazione.getValoreGettone().multiply(new BigDecimal(riga.getQuantitaEntrata())))
                        .setScale(2, RoundingMode.HALF_UP);
                totUscite = totUscite
                        .add(situazione.getValoreGettone().multiply(new BigDecimal(riga.getQuantitaUscita())))
                        .setScale(2, RoundingMode.HALF_UP);
            }

            // aggiorno anche i totali del movimento
            movimentoApertura.setTotaleEntrate(totEntrate);
            movimentoApertura.setTotaleUscite(totUscite);
        }

        // aggiungo tutti gli altri movimenti
        for (MovimentoCassa movimento : getAltriMovimenti()) {
            BigDecimal totEntrate = BigDecimal.ZERO;
            BigDecimal totUscite = BigDecimal.ZERO;
            for (RigaMovimentoCassa riga : movimento.getRighe()) {

                SituazioneCassa situazione = ObjectUtils.defaultIfNull(map.get(riga.getGettone()),
                        new SituazioneCassa());
                situazione.setGettone(riga.getGettone());
                situazione.setQuantitaEntrate(situazione.getQuantitaEntrate() + riga.getQuantitaEntrata());
                situazione.setQuantitaUscite(situazione.getQuantitaUscite() + riga.getQuantitaUscita());
                map.put(riga.getGettone(), situazione);

                totEntrate = totEntrate
                        .add(situazione.getValoreGettone().multiply(new BigDecimal(riga.getQuantitaEntrata())))
                        .setScale(2, RoundingMode.HALF_UP);
                totUscite = totUscite
                        .add(situazione.getValoreGettone().multiply(new BigDecimal(riga.getQuantitaUscita())))
                        .setScale(2, RoundingMode.HALF_UP);
            }
            // aggiorno anche i totali del movimento
            movimento.setTotaleEntrate(totEntrate);
            movimento.setTotaleUscite(totUscite);
        }

        List<SituazioneCassa> situazione = new ArrayList<>(map.values());
        Collections.sort(situazione, new Comparator<SituazioneCassa>() {

            @Override
            public int compare(SituazioneCassa o1, SituazioneCassa o2) {
                return o1.getValoreGettone().compareTo(o2.getValoreGettone());
            }
        });
        return situazione;
    }

    /**
     * @return lista di tutti i movimenti tranne quello di chiusura.
     */
    public List<MovimentoCassa> getAltriMovimenti() {
        List<MovimentoCassa> altriMovimenti = new ArrayList<>();

        if (movimenti != null) {
            altriMovimenti.addAll(movimenti);
            CollectionUtils.filter(altriMovimenti, new Predicate<MovimentoCassa>() {

                @Override
                public boolean evaluate(MovimentoCassa movimento) {
                    return !movimento.isApertura();
                }
            });
        }
        return altriMovimenti;
    }

    /**
     * @return the codice
     */
    public String getCodice() {
        return codice;
    }

    /**
     * @return the descrizione
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * @return movimento di apertura della cassa, <code>null</code> se non esiste
     */
    public MovimentoCassa getMovimentoApertura() {
        if (movimenti != null) {
            for (MovimentoCassa movimentoCassa : movimenti) {
                if (movimentoCassa.isApertura()) {
                    return movimentoCassa;
                }
            }
        }
        return null;
    }

    /**
     * @return the situazioneCassa
     */
    public List<SituazioneCassa> getSituazioneCassa() {
        if (situazioneCassa == null) {
            situazioneCassa = new ArrayList<>();
            situazioneCassa.addAll(createSituazioneCassa());
        }

        return situazioneCassa;
    }

    /**
     * @return the tipologia
     */
    public Tipologia getTipologia() {
        return tipologia;
    }

    /**
     * @return the totale
     */
    public BigDecimal getTotale() {
        if (totale == null) {
            totale = ObjectUtils.defaultIfNull(totaleIniziale, BigDecimal.ZERO)
                    .add(ObjectUtils.defaultIfNull(totaleEntrate, BigDecimal.ZERO))
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
     * @return the totaleIniziale
     */
    public BigDecimal getTotaleIniziale() {
        return totaleIniziale;
    }

    /**
     * @return the totaleUscite
     */
    public BigDecimal getTotaleUscite() {
        return totaleUscite;
    }

    /**
     * @param codice
     *            the codice to set
     */
    public void setCodice(String codice) {
        this.codice = codice;
    }

    /**
     * @param descrizione
     *            the descrizione to set
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * @param movimenti
     *            the movimenti to set
     */
    public void setMovimenti(Set<MovimentoCassa> movimenti) {
        this.movimenti = movimenti;
    }

    /**
     * @param tipologia
     *            the tipologia to set
     */
    public void setTipologia(Tipologia tipologia) {
        this.tipologia = tipologia;
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
     * @param totaleIniziale
     *            the totaleIniziale to set
     */
    public void setTotaleIniziale(BigDecimal totaleIniziale) {
        this.totaleIniziale = totaleIniziale;
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