package it.eurotn.panjea.magazzino.util;

import java.math.BigDecimal;

import it.eurotn.panjea.magazzino.domain.TotalizzatoreTipoAttributo;

/**
 * @author leonardo
 */
public class RigaAttributoTotalizzazioneDTO {

    private BigDecimal valore;
    private Integer totalizzatore;
    private Integer numeroDecimali;
    private String codiceUnitaMisura;

    /**
     * Costruttore.
     */
    public RigaAttributoTotalizzazioneDTO() {
        super();
    }

    /**
     * @return the codiceUnitaMisura
     */
    public String getCodiceUnitaMisura() {
        return codiceUnitaMisura;
    }

    /**
     * @return the numeroDecimali
     */
    public Integer getNumeroDecimali() {
        return numeroDecimali;
    }

    /**
     * @return the totalizzatore
     */
    public Integer getTotalizzatore() {
        return totalizzatore;
    }

    /**
     * @return the TotalizzatoreTipoAttributo to get
     */
    public TotalizzatoreTipoAttributo getTotalizzatoreTipoAttributo() {
        return TotalizzatoreTipoAttributo.values()[totalizzatore];
    }

    /**
     * @return the valore
     */
    public BigDecimal getValore() {
        return valore;
    }

    /**
     * @param codiceUnitaMisura
     *            the codiceUnitaMisura to set
     */
    public void setCodiceUnitaMisura(String codiceUnitaMisura) {
        this.codiceUnitaMisura = codiceUnitaMisura;
    }

    /**
     * @param numeroDecimali
     *            the numeroDecimali to set
     */
    public void setNumeroDecimali(Integer numeroDecimali) {
        this.numeroDecimali = numeroDecimali;
    }

    /**
     * @param totalizzatore
     *            the totalizzatore to set
     */
    public void setTotalizzatore(Integer totalizzatore) {
        this.totalizzatore = totalizzatore;
    }

    /**
     * @param valore
     *            the valore to set
     */
    public void setValore(BigDecimal valore) {
        this.valore = valore;
    }

}
