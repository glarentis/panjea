package it.eurotn.panjea.bi.domain.analisi.tabelle.dimensioni;

import it.eurotn.panjea.bi.domain.analisi.tabelle.TabellaDimensione;

public class TabellaOperatori extends TabellaDimensione {
    private static final long serialVersionUID = -2869732202984795677L;

    /**
     * Costruttore
     */
    public TabellaOperatori() {
        super("operatori", "manu", "", "operatori");
        addColumn("codice", String.class, "Cod. operatore");
        addColumn("CONCAT(operatori.nome ,' ', operatori.cognome)", String.class, "Nome");
    }
}
