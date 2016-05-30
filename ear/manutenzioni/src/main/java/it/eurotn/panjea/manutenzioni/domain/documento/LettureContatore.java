package it.eurotn.panjea.manutenzioni.domain.documento;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang3.ObjectUtils;

@Embeddable
public class LettureContatore implements Serializable {

    private static final long serialVersionUID = 4122288245849690648L;

    @Column(precision = 19, scale = 6)
    private BigDecimal prezzo;

    @Column(nullable = false)
    private Integer precedente;

    @Column(nullable = false)
    private Integer lettura;

    @Column(nullable = false)
    private Integer prove;

    @Column(nullable = false)
    private Integer battute;

    @Column(precision = 19, scale = 6)
    private BigDecimal importo;

    {
        precedente = 0;
        lettura = 0;
        prove = 0;
        battute = 0;
        prezzo = BigDecimal.ZERO;
        importo = BigDecimal.ZERO;
    }

    private void aggiornaValori() {
        this.battute = ObjectUtils.defaultIfNull(lettura, 0) - ObjectUtils.defaultIfNull(precedente, 0)
                - ObjectUtils.defaultIfNull(prove, 0);
        this.importo = new BigDecimal(battute).multiply(ObjectUtils.defaultIfNull(prezzo, BigDecimal.ZERO)).setScale(2,
                RoundingMode.HALF_UP);
    }

    /**
     * @return the battute
     */
    public Integer getBattute() {
        return battute;
    }

    /**
     * @return the importo
     */
    public BigDecimal getImporto() {
        return importo;
    }

    /**
     * @return the lettura
     */
    public Integer getLettura() {
        return lettura;
    }

    /**
     * @return the precedente
     */
    public Integer getPrecedente() {
        return precedente;
    }

    /**
     * @return the prezzo
     */
    public BigDecimal getPrezzo() {
        return prezzo;
    }

    /**
     * @return the prove
     */
    public Integer getProve() {
        return prove;
    }

    /**
     * @param battute
     *            the battute to set
     */
    public void setBattute(Integer battute) {
        this.battute = battute;
    }

    /**
     * @param importo
     *            the importo to set
     */
    public void setImporto(BigDecimal importo) {
        this.importo = importo;
    }

    /**
     * @param lettura
     *            the lettura to set
     */
    public void setLettura(Integer lettura) {
        this.lettura = lettura;
        aggiornaValori();
    }

    /**
     * @param precedente
     *            the precedente to set
     */
    public void setPrecedente(Integer precedente) {
        this.precedente = precedente;
        aggiornaValori();
    }

    /**
     * @param prezzo
     *            the prezzo to set
     */
    public void setPrezzo(BigDecimal prezzo) {
        this.prezzo = prezzo;
        aggiornaValori();
    }

    /**
     * @param prove
     *            the prove to set
     */
    public void setProve(Integer prove) {
        this.prove = prove;
        aggiornaValori();
    }
}
