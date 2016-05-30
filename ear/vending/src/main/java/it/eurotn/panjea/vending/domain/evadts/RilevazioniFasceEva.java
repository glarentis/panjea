package it.eurotn.panjea.vending.domain.evadts;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;

@SuppressWarnings("serial")
@Entity
@Audited
@Table(name = "vend_rilevazioni_eva_dts_fasce")
public class RilevazioniFasceEva extends EntityBase {
    protected int la101;

    protected int la102;

    protected BigDecimal la103;

    protected int la104;

    @ManyToOne(fetch = FetchType.LAZY)
    protected RilevazioneEvaDts rilevazioneEvaDts;

    /**
     * @return Returns the la101.
     */
    public int getLa101() {
        return la101;
    }

    /**
     * @return Returns the la102.
     */
    public int getLa102() {
        return la102;
    }

    /**
     * @return the la103
     */
    public BigDecimal getLa103() {
        return la103;
    }

    /**
     * @return Returns the la104.
     */
    public int getLa104() {
        return la104;
    }

    /**
     * @return Returns the rilevazioneEvaDts.
     */
    public RilevazioneEvaDts getRilevazioneEvaDts() {
        return rilevazioneEvaDts;
    }

    /**
     * @param la101
     *            The la101 to set.
     */
    public void setLa101(int la101) {
        this.la101 = la101;
    }

    /**
     * @param la102
     *            The la102 to set.
     */
    public void setLa102(int la102) {
        this.la102 = la102;
    }

    /**
     * @param la103
     *            the la103 to set
     */
    public void setLa103(BigDecimal la103) {
        this.la103 = la103;
    }

    /**
     * @param la104
     *            The la104 to set.
     */
    public void setLa104(int la104) {
        this.la104 = la104;
    }

    /**
     * @param rilevazioneEvaDts
     *            The rilevazioneEvaDts to set.
     */
    public void setRilevazioneEvaDts(RilevazioneEvaDts rilevazioneEvaDts) {
        this.rilevazioneEvaDts = rilevazioneEvaDts;
    }

}
