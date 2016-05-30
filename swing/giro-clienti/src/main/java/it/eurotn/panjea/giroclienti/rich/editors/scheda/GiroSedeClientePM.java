package it.eurotn.panjea.giroclienti.rich.editors.scheda;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.giroclienti.domain.GiroSedeCliente;

public class GiroSedeClientePM {

    private GiroSedeCliente giroSedeCliente;

    private EntitaLite entita;

    private TipoEntita tipoEntita = TipoEntita.CLIENTE;

    /**
     * @return the entita
     */
    public EntitaLite getEntita() {
        return entita;
    }

    /**
     * @return the giroSedeCliente
     */
    public GiroSedeCliente getGiroSedeCliente() {
        return giroSedeCliente;
    }

    /**
     * @return the tipoEntita
     */
    public TipoEntita getTipoEntita() {
        return tipoEntita;
    }

    /**
     * @param entita
     *            the entita to set
     */
    public void setEntita(EntitaLite entita) {
        this.entita = entita;
    }

    /**
     * @param giroSedeCliente
     *            the giroSedeCliente to set
     */
    public void setGiroSedeCliente(GiroSedeCliente giroSedeCliente) {
        this.giroSedeCliente = giroSedeCliente;
    }

    /**
     * @param tipoEntita
     *            the tipoEntita to set
     */
    public void setTipoEntita(TipoEntita tipoEntita) {
        this.tipoEntita = tipoEntita;
    }
}
