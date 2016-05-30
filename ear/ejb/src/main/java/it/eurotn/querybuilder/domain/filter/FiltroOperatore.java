package it.eurotn.querybuilder.domain.filter;

import it.eurotn.querybuilder.domain.ProprietaEntity;

public class FiltroOperatore extends FiltroQuery {

    private String operatoreSql;

    /**
     * @param operatoreSql
     *            operatore sql
     */
    public FiltroOperatore(final String operatoreSql) {
        super();
        this.operatoreSql = operatoreSql;
    }

    @Override
    public String getSql(ProprietaEntity field, Object filtro) {
        String quote = String.class.equals(field.getType()) ? "'" : "";
        String filtroWhere = quote + filtro + quote;

        return getWhereFieldName(field) + " " + operatoreSql + " " + filtroWhere + " ";
    }

}
