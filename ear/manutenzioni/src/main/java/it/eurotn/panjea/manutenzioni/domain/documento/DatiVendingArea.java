package it.eurotn.panjea.manutenzioni.domain.documento;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Embeddable
public class DatiVendingArea implements Serializable {

    private static final long serialVersionUID = -2989761649459480060L;

    @Embedded
    private LettureContatore lettureContatore;

    @Embedded
    private Battute battute;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataInizioIntervento;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataFineIntervento;

    /**
     * @return the battute
     */
    public Battute getBattute() {
        if (battute == null) {
            battute = new Battute();
        }

        return battute;
    }

    /**
     * @return the dataFineIntervento
     */
    public Date getDataFineIntervento() {
        return dataFineIntervento;
    }

    /**
     * @return the dataInizioIntervento
     */
    public Date getDataInizioIntervento() {
        return dataInizioIntervento;
    }

    /**
     * @return the lettureContatore
     */
    public LettureContatore getLettureContatore() {
        if (lettureContatore == null) {
            lettureContatore = new LettureContatore();
        }

        return lettureContatore;
    }

    /**
     * @param battute
     *            the battute to set
     */
    public void setBattute(Battute battute) {
        this.battute = battute;
    }

    /**
     * @param dataFineIntervento
     *            the dataFineIntervento to set
     */
    public void setDataFineIntervento(Date dataFineIntervento) {
        this.dataFineIntervento = dataFineIntervento;
    }

    /**
     * @param dataInizioIntervento
     *            the dataInizioIntervento to set
     */
    public void setDataInizioIntervento(Date dataInizioIntervento) {
        this.dataInizioIntervento = dataInizioIntervento;
    }

    /**
     * @param lettureContatore
     *            the lettureContatore to set
     */
    public void setLettureContatore(LettureContatore lettureContatore) {
        this.lettureContatore = lettureContatore;
    }
}
