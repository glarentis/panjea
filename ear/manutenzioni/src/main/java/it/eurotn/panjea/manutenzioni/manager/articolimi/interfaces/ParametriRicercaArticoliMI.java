package it.eurotn.panjea.manutenzioni.manager.articolimi.interfaces;

import java.io.Serializable;

public class ParametriRicercaArticoliMI implements Serializable {
    private static final long serialVersionUID = 4805848847915317661L;
    private String codice;
    private String descrizione;

    private boolean soloDisponibili;
    private boolean soloProprietaCliente;

    {
        soloDisponibili = false;
        soloProprietaCliente = false;
    }

    /**
     * @return Returns the codice.
     */
    public String getCodice() {
        return codice;
    }

    /**
     * @return Returns the descrizione.
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * @return the soloDisponibili
     */
    public boolean isSoloDisponibili() {
        return soloDisponibili;
    }

    /**
     * @return the soloProprietaCliente
     */
    public boolean isSoloProprietaCliente() {
        return soloProprietaCliente;
    }

    /**
     * @param codice
     *            The codice to set.
     */
    public void setCodice(String codice) {
        this.codice = codice;
    }

    /**
     * @param descrizione
     *            The descrizione to set.
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * @param soloDisponibili
     *            the soloDisponibili to set
     */
    public void setSoloDisponibili(boolean soloDisponibili) {
        this.soloDisponibili = soloDisponibili;
    }

    /**
     * @param soloProprietaCliente
     *            the soloProprietaCliente to set
     */
    public void setSoloProprietaCliente(boolean soloProprietaCliente) {
        this.soloProprietaCliente = soloProprietaCliente;
    }

}
