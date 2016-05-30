package it.eurotn.panjea.bi.domain.analisi.tabelle.fatti;

import java.math.BigDecimal;

import it.eurotn.panjea.bi.domain.analisi.tabelle.ColumnMeasure;
import it.eurotn.panjea.bi.domain.analisi.tabelle.TabellaFatti;

/**
 * @author fattazzo
 *
 */
public class TabellaDocumentiMagazzino extends TabellaFatti {

    private static final long serialVersionUID = -1730935534553990941L;

    /**
     * Costruttore.
     */
    public TabellaDocumentiMagazzino() {
        super("documentiMagazzino", "vista", null, "documenti Magazzino");

        colonne.putAll(ColumnMeasure.creaColonneAggregate("Colli", BigDecimal.class, this, "Colli", null, 0, null));
        colonne.putAll(
                ColumnMeasure.creaColonneAggregate("Peso netto", BigDecimal.class, this, "Peso netto", null, 2, null));
        colonne.putAll(
                ColumnMeasure.creaColonneAggregate("Peso lordo", BigDecimal.class, this, "Peso lordo", null, 2, null));
        colonne.putAll(ColumnMeasure.creaColonneAggregate("Volume", BigDecimal.class, this, "Volume", null, 2, null));

        colonne.putAll(
                ColumnMeasure.creaColonneAggregate("Tot. doc.", BigDecimal.class, this, "Tot. doc.", null, 2, "€"));
        colonne.putAll(ColumnMeasure.creaColonneAggregate("Imponibile. doc.", BigDecimal.class, this,
                "Imponibile. doc.", null, 2, "€"));
        colonne.putAll(ColumnMeasure.creaColonneAggregate("Imposta. doc.", BigDecimal.class, this, "Imposta. doc.",
                null, 2, "€"));
    }

}
