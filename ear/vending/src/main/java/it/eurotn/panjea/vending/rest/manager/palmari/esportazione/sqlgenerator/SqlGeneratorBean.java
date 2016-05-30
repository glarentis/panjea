package it.eurotn.panjea.vending.rest.manager.palmari.esportazione.sqlgenerator;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.hibernate.SQLQuery;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.sqlgenerator.interfaces.SqlGenerator;
import it.eurotn.util.PanjeaEJBUtil;

@Stateless(name = "Panjea.SqlGenerator")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.SqlGenerator")
public abstract class SqlGeneratorBean implements SqlGenerator {

    @EJB
    private PanjeaDAO panjeaDAO;

    private String createSQLInserts(String codiceOperatore) {
        StringBuilder sb = new StringBuilder(10000);

        String exportTableName = getTableName();
        Collection<String> exportFields = getSelectInsertFields().values();

        StringBuilder sbHeader = new StringBuilder(300);
        sbHeader.append("INSERT INTO " + exportTableName + " (");
        for (Iterator<String> iterator = exportFields.iterator(); iterator.hasNext();) {
            sbHeader.append(iterator.next());
            if (iterator.hasNext()) {
                sbHeader.append(",");
            }
        }
        sbHeader.append(")");

        List<Object[]> selectResults = executeSQLSelect(codiceOperatore);
        for (Object[] row : selectResults) {
            sb.append(sbHeader);
            sb.append(" VALUES (");
            for (int i = 0; i < exportFields.size(); i++) {
                sb.append(formatValueForInsert(row[i]) + (i < exportFields.size() - 1 ? "," : ""));
            }
            sb.append(")\n");
        }

        return sb.toString();
    }

    @Override
    public String esporta(String codiceOperatore) {
        StringBuilder sb = new StringBuilder(10000);

        // drop table
        // sb.append(getSQLDropTable(codiceOperatore));
        // sb.append("\n");

        // create table
        sb.append(getSQLCreateTable(codiceOperatore));
        sb.append("\n");

        // insert data
        if (!getSelectInsertFields().isEmpty()) {
            sb.append(createSQLInserts(codiceOperatore));
        }

        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    private List<Object[]> executeSQLSelect(String codiceOperatore) {
        StringBuilder sb = new StringBuilder(1000);
        sb.append("select ");
        Set<String> selectFields = getSelectInsertFields().keySet();
        int idx = 0;
        for (Iterator<String> iterator = selectFields.iterator(); iterator.hasNext();) {
            sb.append(iterator.next() + " as field_" + idx);
            if (iterator.hasNext()) {
                sb.append(",");
            }
            idx++;
        }
        sb.append(" ");
        sb.append(getSQLSelectRows(codiceOperatore));
        SQLQuery query = panjeaDAO.prepareNativeQuery(sb.toString());
        idx = 0;
        for (Iterator<String> iterator = selectFields.iterator(); iterator.hasNext();) {
            iterator.next();
            query.addScalar("field_" + idx);
            idx++;
        }

        List<Object[]> result = null;
        try {
            result = query.list();
        } catch (Exception e) {
            throw new RuntimeException("Errore durante l'esecuzione della query " + query.getQueryString(), e);
        }

        return result;
    }

    private Object formatValueForInsert(Object value) {
        Object result = null;

        if (value instanceof String) {
            String valueAsString = (String) value;
            valueAsString = valueAsString.replace("'", "''");
            result = PanjeaEJBUtil.addQuote(valueAsString);
        } else if (value instanceof Date) {
            result = PanjeaEJBUtil.addQuote(DateFormatUtils.format((Date) value, "yyyyMMdd"));
        } else if (value instanceof Boolean) {
            result = ((Boolean) value) ? "1" : "0";
        } else {
            result = value;
        }

        return result;
    }

    /**
     * @return elenco dei nomi dei campi utilizzati per generare le insert. La chiave rappresenta il
     *         campo da utilizzare nella select, il valore il campo da utilizzare per creare la
     *         insert.
     */
    protected abstract Map<String, String> getSelectInsertFields();

    protected abstract String getSQLCreateTable(String codiceOperatore);

    /**
     *
     * @param codiceOperatore
     *            cancella la tabella dell'operatore
     * @return DDL string
     */
    // private String getSQLDropTable(String codiceOperatore) {
    // return "drop table " + getTableName();
    // }

    /**
     * SQL per eseguire la ricerca dei valori da esportare.
     *
     * @param codiceOperatore
     *            codice operatore
     * @return SQL
     */
    protected abstract String getSQLSelectRows(String codiceOperatore);

    protected abstract String getTableName();

}
