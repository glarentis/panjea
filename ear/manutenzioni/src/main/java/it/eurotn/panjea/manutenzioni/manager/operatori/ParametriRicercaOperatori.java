package it.eurotn.panjea.manutenzioni.manager.operatori;

import java.io.Serializable;

public class ParametriRicercaOperatori implements Serializable {

    private static final long serialVersionUID = 1062306102004506375L;

    private String codice;

    private String denominazione;

    private Boolean tecnico;
    private Boolean caricatore;

    {
        tecnico = null;
        caricatore = null;
    }

    /**
     * @return the caricatore
     */
    public Boolean getCaricatore() {
        return caricatore;
    }

    /**
     * @return the codice
     */
    public String getCodice() {
        return codice;
    }

    /**
     * @return the denominazione
     */
    public String getDenominazione() {
        return denominazione;
    }

    /**
     * @return the tecnico
     */
    public Boolean getTecnico() {
        return tecnico;
    }

    /**
     * @param caricatore
     *            the caricatore to set
     */
    public void setCaricatore(Boolean caricatore) {
        this.caricatore = caricatore;
    }

    /**
     * @param codice
     *            the codice to set
     */
    public void setCodice(String codice) {
        this.codice = codice;
    }

    /**
     * @param denominazione
     *            the denominazione to set
     */
    public void setDenominazione(String denominazione) {
        this.denominazione = denominazione;
    }

    /**
     * @param tecnico
     *            the tecnico to set
     */
    public void setTecnico(Boolean tecnico) {
        this.tecnico = tecnico;
    }
}
