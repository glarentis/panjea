package it.eurotn.panjea.contabilita.rich.editors.tabelle.strutturacontabile;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;

public class EntitaPM {

    private EntitaLite entita;
    private final TipoEntita tipoEntita;

    /**
     * Costruttore.
     *
     * @param tipoEntita
     *            {@link TipoEntita}
     */
    public EntitaPM(final TipoEntita tipoEntita) {
        super();
        this.tipoEntita = tipoEntita;
    }

    /**
     * @return the entita
     */
    public EntitaLite getEntita() {
        return entita;
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

}