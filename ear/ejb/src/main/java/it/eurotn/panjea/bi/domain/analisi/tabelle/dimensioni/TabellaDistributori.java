package it.eurotn.panjea.bi.domain.analisi.tabelle.dimensioni;

import it.eurotn.panjea.bi.domain.analisi.tabelle.TabellaDimensione;

public class TabellaDistributori extends TabellaDimensione {
    private static final long serialVersionUID = -5974540029455798742L;

    /**
     * Costruttore.
     */
    public TabellaDistributori() {
        super("distributori", "vista", "", "distributore");
        addColumn("codice", String.class, "Distributore", false);
        addColumn("descrizione", String.class, "Desc. distr.");
        addColumn("modello", String.class, "Modello");
        addColumn("tipomodello", String.class, "Tipo Modello");
        addColumn("acqua", String.class, "Acqua");
        addColumn("caldo", String.class, "Caldo");
        addColumn("freddo", String.class, "Freddo");
        addColumn("kit", String.class, "Kit");
    }
}
