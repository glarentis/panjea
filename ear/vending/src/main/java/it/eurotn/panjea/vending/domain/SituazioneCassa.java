package it.eurotn.panjea.vending.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class SituazioneCassa implements Serializable {

    private static final long serialVersionUID = 8281379333095788119L;

    private Gettone gettone;

    private Integer quantitaIniziale;
    private Integer quantitaEntrate;
    private Integer quantitaUscite;

    private BigDecimal valoreIniziale;
    private BigDecimal valoreEntrate;
    private BigDecimal valoreUscite;
    private BigDecimal valoreFinale;

    {
        quantitaIniziale = 0;
        quantitaEntrate = 0;
        quantitaUscite = 0;
    }

    /**
     * Costruttore.
     *
     */
    public SituazioneCassa() {
        super();
    }

    /**
     * @return the gettone
     */
    public Gettone getGettone() {
        return gettone;
    }

    /**
     * @return the quantitaEntrate
     */
    public Integer getQuantitaEntrate() {
        return quantitaEntrate;
    }

    /**
     * @return the quantitaFinale
     */
    public Integer getQuantitaFinale() {
        return quantitaIniziale + quantitaEntrate - quantitaUscite;
    }

    /**
     * @return the quantitaIniziale
     */
    public Integer getQuantitaIniziale() {
        return quantitaIniziale;
    }

    /**
     * @return the quantitaUscite
     */
    public Integer getQuantitaUscite() {
        return quantitaUscite;
    }

    /**
     * @return the valoreEntrate
     */
    public BigDecimal getValoreEntrate() {
        if (valoreEntrate == null) {
            valoreEntrate = getValoreGettone().multiply(new BigDecimal(quantitaEntrate)).setScale(2,
                    RoundingMode.HALF_UP);
        }

        return valoreEntrate;
    }

    /**
     * @return the valoreFinale
     */
    public BigDecimal getValoreFinale() {
        if (valoreFinale == null) {
            valoreFinale = getValoreIniziale().add(getValoreEntrate()).subtract(getValoreUscite());
        }

        return valoreFinale;
    }

    /**
     * @return valore del gettone
     */
    public BigDecimal getValoreGettone() {
        return gettone != null ? gettone.getValore() : BigDecimal.ZERO;
    }

    /**
     * @return the valoreIniziale
     */
    public BigDecimal getValoreIniziale() {
        if (valoreIniziale == null) {
            valoreIniziale = getValoreGettone().multiply(new BigDecimal(quantitaIniziale)).setScale(2,
                    RoundingMode.HALF_UP);
        }

        return valoreIniziale;
    }

    /**
     * @return the valoreUscite
     */
    public BigDecimal getValoreUscite() {
        if (valoreUscite == null) {
            valoreUscite = getValoreGettone().multiply(new BigDecimal(quantitaUscite)).setScale(2,
                    RoundingMode.HALF_UP);
        }

        return valoreUscite;
    }

    /**
     * @param gettone
     *            the gettone to set
     */
    public void setGettone(Gettone gettone) {
        this.gettone = gettone;
    }

    /**
     * @param quantitaEntrate
     *            the quantitaEntrate to set
     */
    public void setQuantitaEntrate(Integer quantitaEntrate) {
        this.quantitaEntrate = quantitaEntrate;
    }

    /**
     * @param quantitaIniziale
     *            the quantitaIniziale to set
     */
    public void setQuantitaIniziale(Integer quantitaIniziale) {
        this.quantitaIniziale = quantitaIniziale;
    }

    /**
     * @param quantitaUscite
     *            the quantitaUscite to set
     */
    public void setQuantitaUscite(Integer quantitaUscite) {
        this.quantitaUscite = quantitaUscite;
    }
}
