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
 * @author fattazzo
 *
 */
@Entity
@Audited
@Table(name = "maga_settings_soglie_addebito_dichiarazione_intento")
public class SogliaAddebitoDichiarazioneSettings extends EntityBase {

    private static final long serialVersionUID = 7801771914651391983L;

    @Temporal(TemporalType.DATE)
    private Date dataVigore;

    @Column(precision = 19, scale = 6)
    private BigDecimal prezzo;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private MagazzinoSettings magazzinoSettings;

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
