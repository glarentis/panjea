/**
 *
 */
package it.eurotn.panjea.rate.manager.sqlbuilder;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;

/**
 * @author fattazzo
 */
public class RataRVSqlBuilder {

    /**
     * @param query
     *            query
     */
    public void addScalar(SQLQuery query) {
        query.addScalar("importo");
        query.addScalar("dataScadenza");
        addScalarDocumento(query);
        query.addScalar("dataDocumento");
        query.addScalar("codicePaese");
        query.addScalar("checkDigit");
        query.addScalar("descrizioneBanca");
        query.addScalar("cin");
        query.addScalar("abi");
        query.addScalar("cab");
        query.addScalar("numero");
        query.addScalar("entitaId");
    }

    /**
     * @param query
     *            SQLQuery a cui impostare lo scalar
     */
    protected void addScalarDocumento(SQLQuery query) {
        query.addScalar("numeroDocumento", Hibernate.STRING);
    }

    /**
     * Costruisce la query per la richiesta versamento rate.
     *
     * @param codiceAzienda
     *            codiceAzienda
     * @param aree
     *            aree magazzino
     * @param data
     *            data
     * @param idRate
     *            idRate
     * @return String sql
     */
    public String buildSql(String codiceAzienda, String aree, String data, String idRate) {
        StringBuffer sb = new StringBuffer();
        sb.append("select ");
        sb.append(buildSqlImportoRata());
        sb.append("       rata.dataScadenza as dataScadenza, ");
        sb.append(buildSqlCodiceDocumento());
        sb.append("       doc.dataDocumento as dataDocumento, ");
        sb.append("       rapp.codicePaese as codicePaese, ");
        sb.append("       rapp.checkDigit as checkDigit, ");
        sb.append("       rapp.cin as cin, ");
        sb.append("       banca.codice as abi, ");
        sb.append("       banca.descrizione as descrizioneBanca, ");
        sb.append("       filiale.codice as cab, ");
        sb.append("       rapp.numero as numero, ");
        sb.append("       doc.entita_id as entitaId ");
        sb.append("from part_rate rata inner join part_area_partite areapart on (rata.areaRate_id = areapart.id) ");
        sb.append("                    inner join docu_documenti doc on (areapart.documento_id = doc.id) ");
        if (aree != null || data != null) {
            sb.append("                    inner join maga_area_magazzino areamag on (areamag.documento_id = doc.id) ");
        }
        sb.append(
                "                    inner join anag_rapporti_bancari rapp on (rata.rapportoBancarioAzienda_id = rapp.id) ");
        sb.append("                    inner join anag_banche banca on (rapp.banca_id = banca.id) ");
        sb.append("                    inner  join anag_filiali filiale on (rapp.filiale_id = filiale.id) ");
        sb.append("                    inner join anag_entita ent on (ent.id = doc.entita_id) ");
        sb.append("where doc.codiceAzienda = '").append(codiceAzienda).append("' ");
        if (aree != null) {
            sb.append(" and areamag.id in (").append(aree).append(") ");
        } else if (data != null) {
            sb.append(" and areamag.dataCreazione = '").append(data).append("' ");
        } else {
            sb.append(" and rata.id in (").append(idRate).append(") ");
        }
        sb.append(" and rata.stampaRV = true ");
        sb.append(getOrderSql());
        return sb.toString();
    }

    /**
     * @return codice documento
     */
    protected String buildSqlCodiceDocumento() {
        return "       doc.codice as numeroDocumento, ";
    }

    /**
     * @return importo rata
     */
    protected String buildSqlImportoRata() {
        return "		  rata.importoInValutaAzienda-(select coalesce(sum(p.importoInValutaAzienda+p.importoInValutaAziendaForzato),0) from part_pagamenti p where p.rata_id = rata.id) as importo, ";
    }

    /**
     * @return ordine per la query sql
     */
    protected String getOrderSql() {
        return "order by doc.codiceOrder, rata.numeroRata ";
    }
}
