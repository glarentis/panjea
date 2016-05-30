package it.eurotn.panjea.vending.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.entity.annotation.EntityConverter;

@SuppressWarnings("serial")
@Entity
@Audited
@Table(name = "vend_gettoni")
@EntityConverter(properties = "codice,descrizione")
public class Gettone extends EntityBase {

    @Column(length = 10)
    private String codice;

    @Column(length = 30)
    private String descrizione;

    @Column(precision = 10, scale = 2)
    private BigDecimal valore;

    /**
     * @return the codice
     */
    public String getCodice() {
        return codice;
    }

    /**
     * @return the descrizione
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * @return the valore
     */
    public BigDecimal getValore() {
        return valore;
    }

    /**
     * @param codice
     *            the codice to set
     */
    public void setCodice(String codice) {
        this.codice = codice;
    }

    /**
     * @param descrizione
     *            the descrizione to set
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * @param valore
     *            the valore to set
     */
    public void setValore(BigDecimal valore) {
        this.valore = valore;
    }

}