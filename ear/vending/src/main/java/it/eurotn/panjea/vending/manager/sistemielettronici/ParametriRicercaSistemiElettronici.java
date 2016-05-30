package it.eurotn.panjea.vending.manager.sistemielettronici;

import java.io.Serializable;

import it.eurotn.panjea.vending.domain.SistemaElettronico.TipoSistemaElettronico;

public class ParametriRicercaSistemiElettronici implements Serializable {

    private static final long serialVersionUID = -5475438187836567097L;

    private String codice;
    private String descrizione;

    private TipoSistemaElettronico tipoSistemaElettronico;

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
     * @return the tipoSistemaElettronico
     */
    public TipoSistemaElettronico getTipoSistemaElettronico() {
        return tipoSistemaElettronico;
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
     * @param tipoSistemaElettronico
     *            the tipoSistemaElettronico to set
     */
    public void setTipoSistemaElettronico(TipoSistemaElettronico tipoSistemaElettronico) {
        this.tipoSistemaElettronico = tipoSistemaElettronico;
    }
}
