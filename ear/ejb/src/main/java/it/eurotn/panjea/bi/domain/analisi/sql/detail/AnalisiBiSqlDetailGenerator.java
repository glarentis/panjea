package it.eurotn.panjea.bi.domain.analisi.sql.detail;

import java.util.HashMap;
import java.util.Map;

import it.eurotn.panjea.bi.domain.analisi.AnalisiBi;
import it.eurotn.panjea.bi.domain.analisi.tabelle.Colonna;

/**
 * @author fattazzo
 *
 */
public class AnalisiBiSqlDetailGenerator {

    private AnalisiBi analisiBi;

    private Map<Colonna, Object[]> detailFilter;

    private Colonna colonnaMisura;

    private Map<String, SQLDetailBuilder> mapSQLBuilders;

    {
        mapSQLBuilders = new HashMap<String, SQLDetailBuilder>();
        mapSQLBuilders.put("magazzino", new MagazzinoSQLDetailBuilder());
        mapSQLBuilders.put("ordini", new OrdiniSQLDetailBuilder());
    }

    /**
     * Costruttore.
     * 
     * @param analisiBi
     *            analisi
     * @param detailFilter
     *            filtri da applicare oltre a quelli dell'analisi
     * @param colonnaMisura
     *            misura di riferimento
     * 
     */
    public AnalisiBiSqlDetailGenerator(final AnalisiBi analisiBi, final Map<Colonna, Object[]> detailFilter,
            final Colonna colonnaMisura) {
        super();
        this.analisiBi = analisiBi;
        this.detailFilter = detailFilter;
        this.colonnaMisura = colonnaMisura;
    }

    /**
     * Crea l'SQL per il caricamento del dettaglio.
     * 
     * @return SQL creato
     */
    public String buildSql() {
        StringBuilder sqlDetail = new StringBuilder(1000);

        SQLDetailBuilder sqlDetailBuilder = mapSQLBuilders.get(colonnaMisura.getTabella().getAlias());

        if (sqlDetailBuilder != null) {
            sqlDetail.append(sqlDetailBuilder.getSelectSQL());
            sqlDetail.append(sqlDetailBuilder.getFromSQL(analisiBi));
            sqlDetail.append(sqlDetailBuilder.getWhereSQL(analisiBi, detailFilter, colonnaMisura));
        }

        return sqlDetail.toString();
    }

}
