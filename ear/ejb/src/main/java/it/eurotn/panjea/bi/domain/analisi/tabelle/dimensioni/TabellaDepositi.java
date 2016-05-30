package it.eurotn.panjea.bi.domain.analisi.tabelle.dimensioni;

import it.eurotn.panjea.bi.domain.analisi.tabelle.TabellaDimensione;

public class TabellaDepositi extends TabellaDimensione {

    private static final long serialVersionUID = 3580509621770353097L;

    /**
     *
     * Costruttore.
     */
    public TabellaDepositi() {
        super("depositi", "dw", null, "depositi");
        addColumn("descrizione", Character.class, "Dep");
        addColumn("tipoDeposito", String.class, "Tipo dep.");
    }

}
