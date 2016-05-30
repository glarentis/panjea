package it.eurotn.panjea.magazzino.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author giangi
 */
@Embeddable
public class GenerazioneCodiceArticoloData implements Serializable {

    private static final long serialVersionUID = -4887962062250555477L;
    public static final String NUMERATORE = "NUMERATORE";

    /**
     * @uml.property name="mascheraCodiceArticolo"
     */
    @Column(length = 100)
    private String mascheraCodiceArticolo;

    /**
     * @uml.property name="mascheraDescrizioneArticolo"
     */
    @Column(length = 100)
    private String mascheraDescrizioneArticolo;

    /**
     * @uml.property name="numeratore"
     */
    @Column(length = 50)
    private String numeratore;

    private Integer numCaratteriNumeratore;

    /**
     * @return mascheraCodiceArticolo
     * @uml.property name="mascheraCodiceArticolo"
     */
    public String getMascheraCodiceArticolo() {
        return mascheraCodiceArticolo;
    }

    /**
     * @return mascheraDescrizioneArticolo
     * @uml.property name="mascheraDescrizioneArticolo"
     */
    public String getMascheraDescrizioneArticolo() {
        return mascheraDescrizioneArticolo;
    }

    /**
     * @return Returns the numCaratteriNumeratore.
     */
    public Integer getNumCaratteriNumeratore() {
        if (numCaratteriNumeratore == null) {
            return 0;
        }
        return numCaratteriNumeratore;
    }

    /**
     * @return numeratore
     * @uml.property name="numeratore"
     */
    public String getNumeratore() {
        return numeratore;
    }

    /**
     * @param mascheraCodiceArticolo
     *            the mascheraCodiceArticolo to set
     * @uml.property name="mascheraCodiceArticolo"
     */
    public void setMascheraCodiceArticolo(String mascheraCodiceArticolo) {
        this.mascheraCodiceArticolo = mascheraCodiceArticolo;
    }

    /**
     * @param mascheraDescrizioneArticolo
     *            the mascheraDescrizioneArticolo to set
     * @uml.property name="mascheraDescrizioneArticolo"
     */
    public void setMascheraDescrizioneArticolo(String mascheraDescrizioneArticolo) {
        this.mascheraDescrizioneArticolo = mascheraDescrizioneArticolo;
    }

    /**
     * @param numCaratteriNumeratore
     *            The numCaratteriNumeratore to set.
     */
    public void setNumCaratteriNumeratore(Integer numCaratteriNumeratore) {
        this.numCaratteriNumeratore = numCaratteriNumeratore;
    }

    /**
     * @param numeratore
     *            the numeratore to set
     * @uml.property name="numeratore"
     */
    public void setNumeratore(String numeratore) {
        this.numeratore = numeratore;
    }

}
