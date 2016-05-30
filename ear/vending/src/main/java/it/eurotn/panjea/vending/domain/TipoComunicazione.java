package it.eurotn.panjea.vending.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;

@Entity
@Audited
@Table(name = "vend_tipi_comunicazione")
@NamedQueries({ @NamedQuery(name = "TipoComunicazione.caricaAll", query = "from TipoComunicazione tc ") })
public class TipoComunicazione extends EntityBase {

    private static final long serialVersionUID = 472019173944423234L;

    @Column(length = 10)
    private String codice;

    @Column(length = 30)
    private String descrizione;

    private boolean comunicazioneAsl;

    private boolean comunicazioneComuni;

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
     * @return the comunicazioneAsl
     */
    public boolean isComunicazioneAsl() {
        return comunicazioneAsl;
    }

    /**
     * @return the comunicazioneComuni
     */
    public boolean isComunicazioneComuni() {
        return comunicazioneComuni;
    }

    /**
     * @param codice
     *            the codice to set
     */
    public void setCodice(String codice) {
        this.codice = codice;
    }

    /**
     * @param comunicazioneAsl
     *            the comunicazioneAsl to set
     */
    public void setComunicazioneAsl(boolean comunicazioneAsl) {
        this.comunicazioneAsl = comunicazioneAsl;
    }

    /**
     * @param comunicazioneComuni
     *            the comunicazioneComuni to set
     */
    public void setComunicazioneComuni(boolean comunicazioneComuni) {
        this.comunicazioneComuni = comunicazioneComuni;
    }

    /**
     * @param descrizione
     *            the descrizione to set
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

}
