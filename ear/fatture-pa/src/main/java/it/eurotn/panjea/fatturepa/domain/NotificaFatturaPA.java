package it.eurotn.panjea.fatturepa.domain;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;

@Entity
@Audited
@Table(name = "ftpa_notifiche_fattura")
public class NotificaFatturaPA extends EntityBase {

    private static final long serialVersionUID = -7399628433019728687L;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date data;

    private StatoFatturaPA statoFattura;

    @Lob
    private String datiEsito;

    private boolean datiEsitoDaSDI;

    /**
     * Indica se l'esito della notifica è positivo. Non lo è in caso di notifica di scarto o notifica di esito rifiuto.
     */
    private boolean esitoPositivo;

    /**
     * Rappresenta l'ID della mail che ha generato la notifica.
     */
    private String emailMessageID;

    @Transient
    private String datiEsitoHTML;

    {
        data = Calendar.getInstance().getTime();
        datiEsitoDaSDI = false;
        datiEsitoHTML = "";
        esitoPositivo = true;
    }

    /**
     * @return the data
     */
    public Date getData() {
        return data;
    }

    /**
     * @return the datiEsito
     */
    public String getDatiEsito() {
        return datiEsito;
    }

    /**
     * @return the datiEsitoHTML
     */
    public String getDatiEsitoHTML() {
        return datiEsitoHTML;
    }

    /**
     * @return the emailMessageID
     */
    public String getEmailMessageID() {
        return emailMessageID;
    }

    /**
     * @return the statoFattura
     */
    public StatoFatturaPA getStatoFattura() {
        return statoFattura;
    }

    /**
     * @return the datiEsitoDaSDI
     */
    public boolean isDatiEsitoDaSDI() {
        return datiEsitoDaSDI;
    }

    /**
     * @return the esitoPositivo
     */
    public boolean isEsitoPositivo() {
        return esitoPositivo;
    }

    /**
     * @param data
     *            the data to set
     */
    public void setData(Date data) {
        this.data = data;
    }

    /**
     * @param datiEsito
     *            the datiEsito to set
     */
    public void setDatiEsito(String datiEsito) {
        this.datiEsito = datiEsito;
    }

    /**
     * @param datiEsitoDaSDI
     *            the datiEsitoDaSDI to set
     */
    public void setDatiEsitoDaSDI(boolean datiEsitoDaSDI) {
        this.datiEsitoDaSDI = datiEsitoDaSDI;
    }

    /**
     * @param datiEsitoHTML
     *            the datiEsitoHTML to set
     */
    public void setDatiEsitoHTML(String datiEsitoHTML) {
        this.datiEsitoHTML = datiEsitoHTML;
    }

    /**
     * @param emailMessageID
     *            the emailMessageID to set
     */
    public void setEmailMessageID(String emailMessageID) {
        this.emailMessageID = emailMessageID;
    }

    /**
     * @param esitoPositivo
     *            the esitoPositivo to set
     */
    public void setEsitoPositivo(boolean esitoPositivo) {
        this.esitoPositivo = esitoPositivo;
    }

    /**
     * @param statoFattura
     *            the statoFattura to set
     */
    public void setStatoFattura(StatoFatturaPA statoFattura) {
        this.statoFattura = statoFattura;
        if (statoFattura != null && statoFattura == StatoFatturaPA.NS) {
            esitoPositivo = false;
        }
    }
}
