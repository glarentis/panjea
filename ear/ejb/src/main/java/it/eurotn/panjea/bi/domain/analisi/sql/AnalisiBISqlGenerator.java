package it.eurotn.panjea.bi.domain.analisi.sql;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import it.eurotn.panjea.bi.domain.analisi.AnalisiBILayout;
import it.eurotn.panjea.bi.domain.analisi.AnalisiBi;
import it.eurotn.panjea.bi.domain.analisi.AnalisiValueSelected;
import it.eurotn.panjea.bi.domain.analisi.FieldBILayout;
import it.eurotn.panjea.bi.domain.analisi.tabelle.Colonna;
import it.eurotn.panjea.bi.domain.analisi.tabelle.ColumnMeasure;
import it.eurotn.panjea.bi.domain.analisi.tabelle.ColumnMeasureFunction;
import it.eurotn.panjea.bi.domain.analisi.tabelle.Tabella;
import it.eurotn.panjea.bi.domain.analisi.tabelle.TabellaDimensione;
import it.eurotn.panjea.bi.domain.analisi.tabelle.TabellaDimensione.ColumnLink;
import it.eurotn.panjea.magazzino.exception.DataWarehouseException;
import it.eurotn.panjea.magazzino.exception.DataWarehouseVincoloException;

public class AnalisiBISqlGenerator {
    private static Logger logger = Logger.getLogger(AnalisiBISqlGenerator.class);
    private AnalisiBi analisiBi;

    /**
     * Il modello del datawarehouse deve essere inizializzato con i valori da utilizzare nella
     * query.<br>
     * <ul>
     * <li>colonne da utilizzare</li>
     * <li>eventuali filtri</li>
     * </ul>
     *
     * @param analisiBi
     *            analisi
     *
     */
    public AnalisiBISqlGenerator(final AnalisiBi analisiBi) {
        super();
        this.analisiBi = analisiBi;
    }

    /**
     * Costruisce la stringa per la query.<br/>
     *
     * @return query da eseguire
     */
    public String buildSql() {
        logger.debug("--> Enter buildSql");
        Set<Tabella> tabelleFatti = new HashSet<Tabella>();
        Set<Tabella> tabelleDimensioni = new HashSet<Tabella>();
        StringBuilder sql = new StringBuilder();
        StringBuilder sqlUnion = new StringBuilder();
        AnalisiBILayout analisiBILayout = analisiBi.getAnalisiLayout();

        // Recupero le tabelle presenti e verifico che ci siano sufficienti dati per eseguire una
        // query, almeno una
        // misura e una dimensione

        if ((analisiBILayout.getRighe().size() + analisiBILayout.getColonne().size()) == 0) {
            DataWarehouseException dataWarehouseException = new DataWarehouseException(
                    "Inserire almeno una dimensione");
            throw new RuntimeException(dataWarehouseException);
        }

        if (analisiBILayout.getMisure().size() == 0) {
            DataWarehouseException dataWarehouseException = new DataWarehouseException(
                    "Inserire almeno misura nell'analisi");
            throw new RuntimeException(dataWarehouseException);
        }

        for (FieldBILayout fieldBILayout : analisiBILayout.getRighe()) {
            tabelleDimensioni.add(fieldBILayout.getColonna().getTabella());
        }

        for (FieldBILayout fieldBILayout : analisiBILayout.getColonne()) {
            tabelleDimensioni.add(fieldBILayout.getColonna().getTabella());
        }

        for (FieldBILayout fieldBILayout : analisiBILayout.getFiltri()) {
            tabelleDimensioni.add(fieldBILayout.getColonna().getTabella());
        }

        for (FieldBILayout fieldBILayout : analisiBILayout.getMisure()) {
            tabelleFatti.add(fieldBILayout.getColonna().getTabella());
        }

        // Per ogni tabella dei fatti creo la query in join con le
        // tabelle delle dimensioni,
        for (Tabella tabellaFatti : tabelleFatti) {
            StringBuilder columnSelected = new StringBuilder();
            StringBuilder fromSelected = new StringBuilder(" from ");
            StringBuilder joinSelected = new StringBuilder();
            StringBuilder whereSelected = new StringBuilder(" where 1=1 ");
            StringBuilder groupBySelected = new StringBuilder(" group by ");

            // Creo la select e la group by
            for (FieldBILayout fieldLayout : analisiBILayout.getFields().values()) {
                Colonna colonna = fieldLayout.getColonna();
                boolean presenteInFrom = (!(colonna instanceof ColumnMeasure)
                        || !(colonna instanceof ColumnMeasureFunction) && colonna.getTabella() == tabellaFatti);
                if (presenteInFrom) {
                    columnSelected.append(colonna.getSql());
                } else {
                    columnSelected.append(" 0 ");
                }
                if (!colonna.getKey().isEmpty()) {
                    columnSelected.append(" ").append(colonna.getKey()).append(",");
                }
                groupBySelected.append(colonna.getSqlGroupBy());
                if (!colonna.getSqlGroupBy().isEmpty()) {
                    groupBySelected.append(",");
                }

                StringBuilder whereFieldBuilder = createSqlWhere(colonna, tabellaFatti.getAlias());
                if (whereFieldBuilder != null) {
                    whereSelected.append(whereFieldBuilder);
                }
            }

            // Creo la from per la tabella fatti
            fromSelected.append(tabellaFatti.getSqlTable(analisiBILayout));

            // Creo la from e la join per la tabella dimensioni
            for (Tabella tabellaDimensioni : tabelleDimensioni) {
                // se non ci sono link alla tabella dei fatti sollevo subito un'eccezione
                if (((TabellaDimensione) tabellaDimensioni).getLinkToFactTable(tabellaFatti.getNome()) == null) {
                    DataWarehouseException dataWarehouseException = new DataWarehouseVincoloException(
                            tabellaFatti.getAlias(), tabellaDimensioni.getAlias());
                    throw new RuntimeException(dataWarehouseException);
                }

                ColumnLink columnLink = ((TabellaDimensione) tabellaDimensioni)
                        .getLinkToFactTable(tabellaFatti.getNome());

                fromSelected.append(tabellaDimensioni.getJoin(analisiBi.isAlternativeJoin()));
                fromSelected.append(tabellaDimensioni.getSqlTable(analisiBILayout));
                fromSelected.append(" on ");

                // se nella sql ho una join la dimensione non è una tabella ma una select quindi non
                // devo aggiungere
                // l'alias
                fromSelected.append(tabellaDimensioni.getAlias());
                fromSelected.append(".");
                fromSelected.append(columnLink.getColumnDimension());
                fromSelected.append("=");
                // Se la colonna utilizza un alias della tabella non lo aggiungo in automatico
                if (!columnLink.getColumnFact().contains(("."))) {
                    fromSelected.append(tabellaFatti.getAlias());
                    fromSelected.append(".");
                }
                fromSelected.append(columnLink.getColumnFact());
                if (!tabellaDimensioni.getWhere().isEmpty()) {
                    fromSelected.append(" AND ");
                    fromSelected.append(tabellaDimensioni.getWhereSql());
                }
            }
            sql = sql.append(sqlUnion);

            String columnSelectedChomp = StringUtils.removeEnd(columnSelected.toString(), ",");
            String groupBySelectedChomp = StringUtils.removeEnd(groupBySelected.toString(), ",");

            sql = sql.append(" select ").append(columnSelectedChomp).append(fromSelected).append(joinSelected)
                    .append(whereSelected).append(groupBySelectedChomp);
            if (tabelleFatti.size() == 1) {
                sql.append(" order by null ");
            }
            sqlUnion = new StringBuilder(" UNION ALL");
        }

        // Se ho più tabelle dei fatti raggruppo i risultati della union per avere minori righe
        if (tabelleFatti.size() > 1) {

            // Creo la select e la group by
            StringBuilder groupBySelected = new StringBuilder();
            StringBuilder columnSelected = new StringBuilder();
            for (FieldBILayout fieldLayout : analisiBILayout.getFields().values()) {
                Colonna colonna = fieldLayout.getColonna();
                if (colonna instanceof ColumnMeasure) {
                    columnSelected.append(" sum(");
                }
                columnSelected.append(colonna.getKey());
                if (colonna instanceof ColumnMeasure) {
                    columnSelected.append(" )");
                }
                columnSelected.append(",");
                groupBySelected.append(colonna.getSqlGroupBy());
                if (!colonna.getSqlGroupBy().isEmpty()) {
                    groupBySelected.append(",");
                }
            }

            // groupBySelected = new StringBuilder(groupBySelected.substring(0,
            // groupBySelected.length() - 1));

            // Genero l'alias per la tabella risultante dalla union
            sql.insert(0, "(");
            sql.append(") risultati");

            // Select
            sql.insert(0, " from ");
            String columnSelectedChomp = StringUtils.removeEnd(columnSelected.toString(), ",");
            sql.insert(0, columnSelectedChomp);
            sql.insert(0, "select ");

            // Group by
            sql.append(" group by ");
            String groupSelectedChomp = StringUtils.removeEnd(groupBySelected.toString(), ",");
            sql.append(groupSelectedChomp);
            sql.append(" order by null ");
        }
        logger.debug("--> Exit buildSql con sql " + sql.toString());
        return sql.toString();
    }

    /**
     * crea la string sql per la where
     *
     * @param indexField
     * @param colonna
     * @return
     */
    protected StringBuilder createSqlWhere(Colonna colonna, String aliasTabellaFatti) {
        AnalisiValueSelected filtro = analisiBi.getFiltri().get(colonna.getKey());
        if (filtro == null) {
            return new StringBuilder();
        }
        return filtro.getSql(colonna, aliasTabellaFatti);
    }
}
