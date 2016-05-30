package it.eurotn.panjea.manutenzioni.domain.documento;

import java.io.Serializable;

import javax.persistence.Column;

public class Battute implements Serializable {

    private static final long serialVersionUID = -4061319116713804718L;

    @Column(nullable = false)
    private Integer contatori;

    @Column(nullable = false)
    private Integer rifornite;

    @Column(nullable = false)
    private Integer prepagate;

    @Column(nullable = false)
    private Integer omaggio;

    {
        contatori = 0;
        rifornite = 0;
        prepagate = 0;
        omaggio = 0;
    }

    /**
     * @return the contatori
     */
    public Integer getContatori() {
        return contatori;
    }

    /**
     * @return the omaggio
     */
    public Integer getOmaggio() {
        return omaggio;
    }

    /**
     * @return the prepagate
     */
    public Integer getPrepagate() {
        return prepagate;
    }

    /**
     * @return the rifornite
     */
    public Integer getRifornite() {
        return rifornite;
    }

    /**
     * @param contatori
     *            the contatori to set
     */
    public void setContatori(Integer contatori) {
        this.contatori = contatori;
    }

    /**
     * @param omaggio
     *            the omaggio to set
     */
    public void setOmaggio(Integer omaggio) {
        this.omaggio = omaggio;
    }

    /**
     * @param prepagate
     *            the prepagate to set
     */
    public void setPrepagate(Integer prepagate) {
        this.prepagate = prepagate;
    }

    /**
     * @param rifornite
     *            the rifornite to set
     */
    public void setRifornite(Integer rifornite) {
        this.rifornite = rifornite;
    }
}
