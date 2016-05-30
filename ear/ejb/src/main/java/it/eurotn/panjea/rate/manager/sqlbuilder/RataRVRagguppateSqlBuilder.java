/**
 *
 */
package it.eurotn.panjea.rate.manager.sqlbuilder;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;

/**
 * @author fattazzo
 *
 */
public class RataRVRagguppateSqlBuilder extends RataRVSqlBuilder {

    @Override
    protected void addScalarDocumento(SQLQuery query) {
        query.addScalar("documenti", Hibernate.STRING);
    }

    @Override
    protected String buildSqlCodiceDocumento() {
        return " group_concat(doc.codice order by doc.codiceOrder separator ' ') as documenti ,";
    }

    @Override
    protected String buildSqlImportoRata() {
        return "		  sum(rata.importoInValutaAzienda-(select coalesce(sum(p.importoInValutaAzienda+p.importoInValutaAziendaForzato),0) from part_pagamenti p where p.rata_id = rata.id)) as importo, ";
    }

    @Override
    protected String getOrderSql() {
        return "group by doc.entita_id,rata.dataScadenza order by ent.codice,rata.dataScadenza ";
    }
}
