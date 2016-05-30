package it.eurotn.panjea.bi.domain.analisi.tabelle;

import java.util.HashMap;
import java.util.Map;

import it.eurotn.panjea.bi.domain.analisi.AnalisiBIDomain;

public class ColumnMeasure extends Colonna {
    private static final long serialVersionUID = -7198640644270163235L;

    private String funzioneAggregazione;

    private int funzioneAggregazioneIndex;

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
     * @param title
     *            title tabella
     */
    public ColumnMeasure(final String nome, final Class<?> columnClass, final Tabella tabella, final String title) {
        this(nome, columnClass, tabella, title, 0);
    }

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
     * @param title
     *            title colonna
     * @param funzioneAggregazioneIndex
     *            indice della funzione da usare nell'aggregazione
     */
    public ColumnMeasure(final String nome, final Class<?> columnClass, final Tabella tabella, final String title,
            final int funzioneAggregazioneIndex) {
        super(nome, columnClass, tabella, title);
        this.funzioneAggregazioneIndex = funzioneAggregazioneIndex;
        this.funzioneAggregazione = AnalisiBIDomain.FUNZIONI_AGGREGAZIONE[funzioneAggregazioneIndex];
        this.key = new StringBuffer(50).append(funzioneAggregazione).append("_").append(key).toString();
        setSeparatorVisible(true);
    }

    /**
     *
     * Crea una colonna per ogni funzione di aggregazione disponibile.
     *
     * @param nome
     *            nome colonna
     * @param columnClass
     *            classe della colonna
     * @param tabella
     *            tabella della colonna
     * @param title
     *            titolo
     * @param sezione
     *            sezione
     * @param numDecimali
     *            numero di decimali da utilizzare
     * @param prefisso
     *            prefisso
     *
     * @return colonne create
     */
    public static Map<String, ColumnMeasure> creaColonneAggregate(final String nome, final Class<?> columnClass,
            final TabellaFatti tabella, final String title, final String sezione, final int numDecimali,
            final String prefisso) {
        Map<String, ColumnMeasure> result = new HashMap<String, ColumnMeasure>(
                AnalisiBIDomain.FUNZIONI_AGGREGAZIONE.length);
        for (int funzioneAggregazioneIndex = 0; funzioneAggregazioneIndex < AnalisiBIDomain.FUNZIONI_AGGREGAZIONE.length; funzioneAggregazioneIndex++) {
            ColumnMeasure colonna = new ColumnMeasure(nome, columnClass, tabella, title, funzioneAggregazioneIndex);
            colonna.setNumeroDecimali(numDecimali);
            colonna.setDecimaliDaArticoli(false);
            colonna.setPreFisso(prefisso);
            if (funzioneAggregazioneIndex == 6) {
                colonna.setPreFisso("");
                colonna.setNumeroDecimali(0);
            }
            colonna.setSezione(sezione);
            result.put(colonna.getKey(), colonna);
        }
        return result;
    }

    /**
     *
     * Crea una colonna per ogni funzione di aggregazione disponibile.
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
     *            sezione
     * @param prefisso
     *            prefisso
     *
     * @return colonne create
     */
    public static Map<String, ColumnMeasure> creaColonneAggregate(final String nome, final Class<?> columnClass,
            final TabellaFatti tabella, final String alias, final String sezione, final String prefisso) {
        return creaColonneAggregate(nome, columnClass, tabella, alias, sezione, 0, prefisso);
    }

    /**
     * @return Returns the funzioneAggregazione.
     */
    public String getFunzioneAggregazione() {
        return funzioneAggregazione;
    }

    /**
     * @return Returns the funzioneAggregazioneIndex.
     */
    public int getFunzioneAggregazioneIndex() {
        return funzioneAggregazioneIndex;
    }

    @Override
    public String getSql() {
        // boolean isFunction = Pattern.matches("\\*|\\/|\\+|\\-|if\\(", getNome());
        // boolean isFunction = Pattern.matches("\\+", getNome());
        boolean isFunction = getNome().indexOf("+") > 0;
        if (!isFunction) {
            isFunction = getNome().indexOf("-") > 0;
        }
        if (!isFunction) {
            isFunction = getNome().indexOf("/") > 0;
        }
        if (!isFunction) {
            isFunction = getNome().indexOf("*") > 0;
        }
        if (!isFunction) {
            isFunction = getNome().indexOf("if(") > 0;
        }
        // String nome = getNome();
        // System.out.println("DEBUG:ColumnMeasure->getSql:" + getNome() + Pattern.matches("\\+", nome));
        StringBuilder sbSql = new StringBuilder(" ").append(funzioneAggregazione).append("(");
        // se la colonna contiene una funzione non la wrappo con gli apici
        if (!isFunction) {
            sbSql.append("`");
        }
        sbSql.append(getNome());
        if (!isFunction) {
            sbSql.append("`");
        }
        sbSql.append(")");
        return sbSql.toString();
    }

    @Override
    public String getSqlGroupBy() {
        return "";
    }

    /**
     * @param funzioneAggregazione
     *            The funzioneAggregazione to set.
     */
    public void setFunzioneAggregazione(String funzioneAggregazione) {
        this.funzioneAggregazione = funzioneAggregazione;
    }
}
