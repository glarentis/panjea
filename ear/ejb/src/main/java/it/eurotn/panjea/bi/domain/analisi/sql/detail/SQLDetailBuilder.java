package it.eurotn.panjea.bi.domain.analisi.sql.detail;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import it.eurotn.panjea.bi.domain.analisi.AnalisiBILayout;
import it.eurotn.panjea.bi.domain.analisi.AnalisiBi;
import it.eurotn.panjea.bi.domain.analisi.AnalisiFiltro;
import it.eurotn.panjea.bi.domain.analisi.AnalisiValueSelected;
import it.eurotn.panjea.bi.domain.analisi.FieldBILayout;
import it.eurotn.panjea.bi.domain.analisi.tabelle.Colonna;
import it.eurotn.panjea.bi.domain.analisi.tabelle.ColonnaFunzione;
import it.eurotn.panjea.parametriricerca.domain.Periodo;

/**
 * @author fattazzo
 *
 */
public abstract class SQLDetailBuilder {

    /**
     * crea la string sql per la where
     *
     * @param indexField
     * @param colonna
     * @return
     */
    private StringBuilder createSqlWhere(AnalisiBi analisiBi, Colonna colonna) {
        AnalisiValueSelected filtro = analisiBi.getFiltri().get(colonna.getKey());
        StringBuilder whereSelected = new StringBuilder(800);
        if (filtro == null) {
            return whereSelected;
        }

        if (filtro instanceof AnalisiFiltro) {
            AnalisiFiltro filtroPeriodo = (AnalisiFiltro) filtro;
            Periodo periodo = new Periodo();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                if (filtroPeriodo.getParametro1() != null) {
                    periodo.setDataIniziale(format.parse(filtroPeriodo.getParametro1()));
                }
                if (filtroPeriodo.getParametro2() != null) {
                    periodo.setDataFinale(format.parse(filtroPeriodo.getParametro2()));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            periodo.setTipoPeriodo(filtroPeriodo.getTipoPeriodo());
            Date dataInizio = periodo.getDataIniziale();
            Date dataFinale = periodo.getDataFinale();
            if (dataInizio != null) {
                whereSelected.append(" AND ");
                whereSelected.append(colonna.getTabella().getAlias());
                whereSelected.append(".");
                whereSelected.append(colonna.getNome());
                whereSelected.append(">=");
                whereSelected.append("'");
                whereSelected.append(format.format(dataInizio));
                whereSelected.append("'");
            }
            if (dataFinale != null) {
                whereSelected.append(" AND ");
                whereSelected.append(colonna.getTabella().getAlias());
                whereSelected.append(".");
                whereSelected.append(colonna.getNome());
                whereSelected.append("<=");
                whereSelected.append("'");
                whereSelected.append(format.format(dataFinale));
                whereSelected.append("'");
            }
        } else {
            whereSelected.append(" AND ");
            if (!(colonna instanceof ColonnaFunzione)) {
                whereSelected.append(colonna.getTabella().getAlias());
                whereSelected.append(".");
            }
            whereSelected.append(colonna.getNome());
            whereSelected.append(" IN (");

            Object[] valoriDaFiltrare = filtro.getParameter();
            for (Object object : valoriDaFiltrare) {
                if (object != null) {
                    whereSelected.append("'");
                    whereSelected.append(object.toString().replace("'", "''"));
                    whereSelected.append("'");
                    whereSelected.append(",");
                }
            }
            whereSelected = new StringBuilder(whereSelected.substring(0, whereSelected.length() - 1));
            whereSelected.append(" )");
        }
        return whereSelected;
    }

    /**
     * Restituisce la stringa SQL per la from.
     *
     * @param analisiBi
     *            analisi
     * @return SQL creato
     */
    public abstract String getFromSQL(AnalisiBi analisiBi);

    /**
     * Restituisce la string SQL per la select.
     *
     * @return SQL creato
     */
    public abstract String getSelectSQL();

    /**
     * Restituisce la string SQL per la where.
     *
     * @param analisiBi
     *            analisi
     * @param detailFilter
     *            filtri da applicare oltre a quelli dell'analisi
     * @param colonnaMisura
     *            misura di riferimento
     * @return SQL creato
     */
    public String getWhereSQL(AnalisiBi analisiBi, Map<Colonna, Object[]> detailFilter, Colonna colonnaMisura) {
        AnalisiBILayout analisiBILayout = analisiBi.getAnalisiLayout();

        StringBuilder sb = new StringBuilder(500);
        sb.append(" where 1=1 ");

        // where sui filtri dell'analisi
        for (FieldBILayout fieldLayout : analisiBILayout.getFiltri()) {
            StringBuilder whereFieldBuilder = createSqlWhere(analisiBi, fieldLayout.getColonna());
            sb.append(whereFieldBuilder);
        }

        // where sui filtri del dettaglio
        for (Entry<Colonna, Object[]> entry : detailFilter.entrySet()) {
            sb.append(" and ");
            sb.append(entry.getKey().getTabella().getAlias());
            sb.append(".");
            sb.append(entry.getKey().getNome());

            if (entry.getValue().length > 1) {
                sb.append(" IN (");
                for (Object object : entry.getValue()) {
                    if (object == null) {
                        continue;
                    }
                    sb.append("'");
                    sb.append(object.toString().replaceAll("'", "''"));
                    sb.append("'");
                    sb.append(",");
                }
                sb = new StringBuilder(sb.substring(0, sb.length() - 1));
                sb.append(" )");
            } else {
                if (entry.getValue()[0] == null) {
                    continue;
                }
                sb.append(" = '");
                sb.append(entry.getValue()[0].toString().replace("'", "''"));
                sb.append("'");
            }
        }

        // where sulla misura
        sb.append(" and ");
        sb.append(colonnaMisura.getTabella().getAlias());
        sb.append(".`");
        sb.append(colonnaMisura.getNome());
        sb.append("` <> 0");

        return sb.toString();
    }

}
