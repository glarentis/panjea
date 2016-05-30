package it.eurotn.panjea.magazzino.interceptor.sqlbuilder;

public class DataWarehouseArticoliSqlBuilder {

    public enum TipoFiltro {
        ARTICOLO_ID, CATEGORIA_ID, NESSUNO
    }

    public static final String PARAM_FILTRO_ARTICOLO_ID = "articolo_id";
    public static final String PARAM_FILTRO_CATEGORIA_ID = "categoria_id";
    public static final String PARAM_CODICE_CATEGORIA = "codCategoria";
    public static final String PARAM_DESCRIZIONE_CATEGORIA = "descCategoria";

    private TipoFiltro tipoFiltro = null;

    /**
     * Costruttore.
     *
     * @param tipoFiltro
     *            tipoFiltro
     */
    public DataWarehouseArticoliSqlBuilder(final TipoFiltro tipoFiltro) {
        super();
        this.tipoFiltro = tipoFiltro;
    }

    /**
     * Delete Sql, devo definire il filtro PARAM_FILTRO_ARTICOLO_ID nel caso di
     * TipoFiltro.ARTICOLO_ID.
     *
     * @return stringa per la query di delete
     */
    public String getSqlDelete() {
        StringBuffer sb = new StringBuffer();
        if (tipoFiltro.equals(TipoFiltro.ARTICOLO_ID)) {
            sb.append("delete ");
            sb.append("from dw_articoli ");
            sb.append("where id=:" + DataWarehouseArticoliSqlBuilder.PARAM_FILTRO_ARTICOLO_ID);
        } else {
            sb.append("truncate dw_articoli");
        }
        return sb.toString();
    }

    /**
     * @param codiceAzienda
     *            codice dell'azienda corrente
     * @return string per inserire l'articolo non presente nel datawarehouse
     */
    public String getSqlInserisciArticoloNonPresente(String codiceAzienda) {
        // Creo la query per settare l'articolo 'NON PRESENTE' nel datawarehouse
        StringBuffer sb = new StringBuffer();
        sb.append("INSERT INTO dw_articoli ");
        sb.append(
                "(id,codiceazienda,tipoArticolo,codice,descrizioneLinguaAziendale,numeroDecimaliPrezzo,codiceCategoria,descrizioneCategoria,categoria_id) VALUES ");
        sb.append("(0 ,'");
        sb.append(codiceAzienda);
        sb.append(
                "'      ,0           ,'NP'  ,'NON PRESENTE'            ,2                   ,'NP'           ,'NON PRESENTE'      ,0           ) ");
        return sb.toString();
    }

    /**
     * Insert sql, devo definire il filtro PARAM_FILTRO_ARTICOLO_ID nel caso di
     * TipoFiltro.ARTICOLO_ID.
     *
     * @return sql per inserimento
     */
    public String getSqlInsert() {
        StringBuffer sb = new StringBuffer();
        sb.append("insert into dw_articoli ");
        sb.append("select ");
        sb.append("maga_articoli.id, ");
        sb.append("maga_articoli.codiceAzienda, ");
        sb.append("tipoArticolo, ");
        sb.append("maga_articoli.codice, ");
        sb.append("maga_articoli.descrizioneLinguaAziendale, ");
        sb.append("maga_articoli.numeroDecimaliPrezzo, ");
        sb.append("maga_categorie.codice, ");
        sb.append("maga_categorie.descrizioneLinguaAziendale, ");
        sb.append("maga_categorie.id ");
        sb.append("from maga_articoli ");
        sb.append("left join maga_categorie ");
        sb.append("on maga_articoli.categoria_id=maga_categorie.id ");
        sb.append("where TIPO='A' ");
        if (tipoFiltro.equals(TipoFiltro.ARTICOLO_ID)) {
            sb.append("and maga_articoli.id=:" + PARAM_FILTRO_ARTICOLO_ID);
        }
        return sb.toString();
    }

    /**
     * Update sql per aggiornare il codice e la descrizione della categoria in caso di modifica
     * della stessa.<br>
     * Devo definire PARAM_CODICE_CATEGORIA,PARAM_DESCRIZIONE_CATEGORIA,PARAM_FILTRO_CATEGORIA_ID.
     *
     * @return sql per l'aggiornamento di codice e descrizione della categoria degli articoli
     */
    public String getSqlUpdate() {
        StringBuffer sb = new StringBuffer();
        sb.append("update dw_articoli ");
        sb.append("set codiceCategoria=:" + PARAM_CODICE_CATEGORIA + ", ");
        sb.append("descrizioneCategoria=:" + PARAM_DESCRIZIONE_CATEGORIA + " ");
        sb.append("where categoria_id=:" + PARAM_FILTRO_CATEGORIA_ID);
        return sb.toString();
    }

}
