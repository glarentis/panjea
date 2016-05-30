package it.eurotn.panjea.vending.domain;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Index;

import it.eurotn.entity.EntityBase;

@Entity
@Table(name = "vend_letture_selezionatrice_righe")
@org.hibernate.annotations.Table(appliesTo = "vend_letture_selezionatrice_righe", indexes = {
        @Index(name = "IdxProgressivo", columnNames = { "progressivo" }),
        @Index(name = "IdxCodiceGettone", columnNames = { "codiceGettone" }) })
public class RigaLetturaSelezionatrice extends EntityBase {

    private static final long serialVersionUID = 6950370346869154413L;
    private String codiceGettone;

    private Integer quantita;

    @ManyToOne(fetch = FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
    private Gettone gettone;

    @ManyToOne
    @JoinColumn(name = "progressivo")
    private LetturaSelezionatrice letturaSelezionatrice;

    /**
     * @return the codiceGettone
     */
    public String getCodiceGettone() {
        return codiceGettone;
    }

    /**
     * @return the gettone
     */
    public Gettone getGettone() {
        return gettone;
    }

    /**
     * @return the quantita
     */
    public BigDecimal getImporto() {
        BigDecimal valore = gettone != null && !gettone.isNew() ? gettone.getValore() : BigDecimal.ZERO;
        return valore.multiply(new BigDecimal(ObjectUtils.defaultIfNull(quantita, 0)));
    }

    /**
     * @return the letturaSelezionatrice
     */
    public LetturaSelezionatrice getLetturaSelezionatrice() {
        return letturaSelezionatrice;
    }

    /**
     * @return the quantita
     */
    public Integer getQuantita() {
        return quantita;
    }

    /**
     * @param codiceGettone
     *            the codiceGettone to set
     */
    public void setCodiceGettone(String codiceGettone) {
        this.codiceGettone = codiceGettone;
    }

    /**
     * @param gettone
     *            the gettone to set
     */
    public void setGettone(Gettone gettone) {
        this.gettone = gettone;
        this.codiceGettone = gettone == null ? null : gettone.getCodice();
    }

    /**
     * @param letturaSelezionatrice
     *            the letturaSelezionatrice to set
     */
    public void setLetturaSelezionatrice(LetturaSelezionatrice letturaSelezionatrice) {
        this.letturaSelezionatrice = letturaSelezionatrice;
    }

    /**
     * @param quantita
     *            the quantita to set
     */
    public void setQuantita(Integer quantita) {
        this.quantita = quantita;
    }
}
