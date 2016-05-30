package it.eurotn.panjea.magazzino.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;

/**
 * @author leonardo
 */
@Entity
@Audited
@Table(name = "maga_settings_addebito_dichiarazione_intento")
public class AddebitoDichiarazioneIntentoSettings extends EntityBase {

    private static final long serialVersionUID = -2499961074403413365L;

    @ManyToOne
    private ArticoloLite articolo;

    @Temporal(TemporalType.DATE)
    private Date dataVigore;

    @Column(precision = 19, scale = 6)
    private BigDecimal prezzo;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private MagazzinoSettings magazzinoSettings;

    /**
     * Costruttore.
     */
    public AddebitoDichiarazioneIntentoSettings() {
        super();
    }

    /**
     * @return the articolo
     */
    public ArticoloLite getArticolo() {
        return articolo;
    }

    /**
     * @return the dataVigore
     */
    public Date getDataVigore() {
        return dataVigore;
    }

    /**
     * @return the magazzinoSettings
     */
    public MagazzinoSettings getMagazzinoSettings() {
        return magazzinoSettings;
    }

    /**
     * @return the prezzo
     */
    public BigDecimal getPrezzo() {
        return prezzo;
    }

    /**
     * @param articolo
     *            the articolo to set
     */
    public void setArticolo(ArticoloLite articolo) {
        this.articolo = articolo;
    }

    /**
     * @param dataVigore
     *            the dataVigore to set
     */
    public void setDataVigore(Date dataVigore) {
        this.dataVigore = dataVigore;
    }

    /**
     * @param magazzinoSettings
     *            the magazzinoSettings to set
     */
    public void setMagazzinoSettings(MagazzinoSettings magazzinoSettings) {
        this.magazzinoSettings = magazzinoSettings;
    }

    /**
     * @param prezzo
     *            the prezzo to set
     */
    public void setPrezzo(BigDecimal prezzo) {
        this.prezzo = prezzo;
    }

}
