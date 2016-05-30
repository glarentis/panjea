package it.eurotn.panjea.fatturepa.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author fattazzo
 *
 */
@Embeddable
public class DatiIscrizioneRea implements Serializable {

    public enum StatoLiquidazione {
        LS, LN
    }

    public enum TipologiaSoci {
        SU, SM
    }

    private static final long serialVersionUID = -4223711710840610934L;

    private boolean enable;

    @Column(length = 2)
    private String provincia;

    @Column(length = 20)
    private String numeroRea;

    private BigDecimal importoCapitaleSociale;

    private TipologiaSoci tipologiaSoci;

    private StatoLiquidazione statoLiquidazione;

    {
        statoLiquidazione = StatoLiquidazione.LN;
        enable = Boolean.TRUE;
    }

    /**
     * @return the importoCapitaleSociale
     */
    public BigDecimal getImportoCapitaleSociale() {
        return importoCapitaleSociale;
    }

    /**
     * @return the numeroRea
     */
    public String getNumeroRea() {
        return numeroRea;
    }

    /**
     * @return the provincia
     */
    public String getProvincia() {
        return provincia;
    }

    /**
     * @return the statoLiquidazione
     */
    public StatoLiquidazione getStatoLiquidazione() {
        return statoLiquidazione;
    }

    /**
     * @return the tipologiaSoci
     */
    public TipologiaSoci getTipologiaSoci() {
        return tipologiaSoci;
    }

    /**
     * @return the enable
     */
    public boolean isEnable() {
        return enable;
    }

    /**
     * @param enable
     *            the enable to set
     */
    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    /**
     * @param importoCapitaleSociale
     *            the importoCapitaleSociale to set
     */
    public void setImportoCapitaleSociale(BigDecimal importoCapitaleSociale) {
        this.importoCapitaleSociale = importoCapitaleSociale;
    }

    /**
     * @param numeroRea
     *            the numeroRea to set
     */
    public void setNumeroRea(String numeroRea) {
        this.numeroRea = numeroRea;
    }

    /**
     * @param provincia
     *            the provincia to set
     */
    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    /**
     * @param statoLiquidazione
     *            the statoLiquidazione to set
     */
    public void setStatoLiquidazione(StatoLiquidazione statoLiquidazione) {
        this.statoLiquidazione = statoLiquidazione;
    }

    /**
     * @param tipologiaSoci
     *            the tipologiaSoci to set
     */
    public void setTipologiaSoci(TipologiaSoci tipologiaSoci) {
        this.tipologiaSoci = tipologiaSoci;
    }

}
