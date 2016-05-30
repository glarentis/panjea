package it.eurotn.panjea.bi.domain.analisi.tabelle;

import it.eurotn.panjea.bi.domain.analisi.AnalisiBIDomain;

/**
 * @author fattazzo
 *
 */
public class ColumnMeasureFunction extends ColumnMeasure {

    private static final long serialVersionUID = 1838283375902868229L;

    /**
     *
     * Costruttore.
     *
     * @param nome
     *            nome colonna
     * @param columnClass
     *            classe della colonna
     * @param tabella
     *            tabella della colonna
     * @param alias
     *            alias tabella
     * @param sezione
     *            nome della sezione
     * @param numDecimali
     *            numero di decimali da utilizzare
     */
    public ColumnMeasureFunction(final String nome, final Class<?> columnClass, final Tabella tabella,
            final String alias, final String sezione, final int numDecimali) {
        super(nome, columnClass, tabella, alias);
        setFunzioneAggregazione(null);
        setSezione(sezione);
        setDecimaliDaArticoli(false);
        setNumeroDecimali(numDecimali);
    }

    @Override
    public int getFunzioneAggregazioneIndex() {
        return AnalisiBIDomain.FUNZIONI_AGGREGAZIONE.length;
    }
}
