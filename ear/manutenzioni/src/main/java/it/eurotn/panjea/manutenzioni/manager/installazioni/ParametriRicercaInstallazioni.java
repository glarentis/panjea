package it.eurotn.panjea.manutenzioni.manager.installazioni;

import java.io.Serializable;

public class ParametriRicercaInstallazioni implements Serializable {
    private static final long serialVersionUID = 6959787008968382366L;

    private String codice;
    private String descrizione;
    private Integer idEntita;
    private Integer idSedeEntita;
    private boolean includeEmpty; // include le installazioni senza distributori.

    private Integer idTecnico;
    private Integer idCaricatore;

    /**
     * Costruttore.
     */
    public ParametriRicercaInstallazioni() {
        includeEmpty = false;
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
     * @return the idCaricatore
     */
    public Integer getIdCaricatore() {
        return idCaricatore;
    }

    /**
     * @return Returns the idEntita.
     */
    public Integer getIdEntita() {
        return idEntita;
    }

    /**
     * @return Returns the idSedeEntita.
     */
    public Integer getIdSedeEntita() {
        return idSedeEntita;
    }

    /**
     * @return the idTecnico
     */
    public Integer getIdTecnico() {
        return idTecnico;
    }

    /**
     * @return Returns the includeEmpty.
     */
    public boolean isIncludeEmpty() {
        return includeEmpty;
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
     * @param idCaricatore
     *            the idCaricatore to set
     */
    public void setIdCaricatore(Integer idCaricatore) {
        this.idCaricatore = idCaricatore;
    }

    /**
     * @param idEntita
     *            The idEntita to set.
     */
    public void setIdEntita(Integer idEntita) {
        this.idEntita = idEntita;
    }

    /**
     * @param idSedeEntita
     *            The idSedeEntita to set.
     */
    public void setIdSedeEntita(Integer idSedeEntita) {
        this.idSedeEntita = idSedeEntita;
    }

    /**
     * @param idTecnico
     *            the idTecnico to set
     */
    public void setIdTecnico(Integer idTecnico) {
        this.idTecnico = idTecnico;
    }

    /**
     * @param includeEmpty
     *            The includeEmpty to set.
     */
    public void setIncludeEmpty(boolean includeEmpty) {
        this.includeEmpty = includeEmpty;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ParametriRicercaInstallazioni [codice=" + codice + ", descrizione=" + descrizione + ", idEntita="
                + idEntita + ", idSedeEntita=" + idSedeEntita + "]";
    }

}
