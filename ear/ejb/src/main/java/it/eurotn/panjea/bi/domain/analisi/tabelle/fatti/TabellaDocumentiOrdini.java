package it.eurotn.panjea.bi.domain.analisi.tabelle.fatti;

import java.math.BigDecimal;

import it.eurotn.panjea.bi.domain.analisi.tabelle.ColumnMeasure;
import it.eurotn.panjea.bi.domain.analisi.tabelle.TabellaFatti;

/**
 * @author fattazzo
 *
 */
public class TabellaDocumentiOrdini extends TabellaFatti {

    private static final long serialVersionUID = -1530385993480068001L;

    /**
     * Costruttore.
     */
    public TabellaDocumentiOrdini() {
        super("documentiOrdine", "vista", null, "documenti Ordine");

        colonne.putAll(ColumnMeasure.creaColonneAggregate("Colli ord", double.class, this, "Colli ord", null, 0, null));

        colonne.putAll(ColumnMeasure.creaColonneAggregate("Tot. doc. ord.", BigDecimal.class, this, "Tot. doc. ord.",
                null, 2, "€"));
        colonne.putAll(ColumnMeasure.creaColonneAggregate("Imponibile. doc. ord.", BigDecimal.class, this,
                "Imponibile. doc. ord.", null, 2, "€"));
        colonne.putAll(ColumnMeasure.creaColonneAggregate("Imposta. doc. ord.", BigDecimal.class, this,
                "Imposta. doc. ord.", null, 2, "€"));
    }
}
