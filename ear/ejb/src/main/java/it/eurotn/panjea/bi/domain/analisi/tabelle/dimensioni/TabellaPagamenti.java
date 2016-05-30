package it.eurotn.panjea.bi.domain.analisi.tabelle.dimensioni;

import it.eurotn.panjea.bi.domain.analisi.AnalisiBILayout;
import it.eurotn.panjea.bi.domain.analisi.tabelle.TabellaDimensione;

public class TabellaPagamenti extends TabellaDimensione {
    private static final long serialVersionUID = -391605216691040353L;

    /**
     * Costruttore.
     */
    public TabellaPagamenti() {
        super(" part_codici_pagamento pagamenti inner join part_codici_pagamento_part_struttura_partita pagstrutt on pagstrutt.part_codici_pagamento_id=pagamenti.id inner join part_struttura_partita strutt on strutt.id=pagstrutt.strutturePartita_id",
                "", "", "pagamenti");
        addColumn("codicePagamento", String.class, "Pagamento");
        addColumn("descrizione", String.class, "Desc. pagamento");
        addColumn("tipoPagamento", Integer.class, "Tipo pagamento");
    }

    @Override
    public String getSqlTable(AnalisiBILayout analisiBILayout) {
        StringBuilder sqlTable = new StringBuilder(super.getSqlTable(analisiBILayout));
        if (analisiBILayout == null) {
            return sqlTable.toString();
        }

        sqlTable.append("");
        return super.getSqlTable(analisiBILayout);
    }
}
