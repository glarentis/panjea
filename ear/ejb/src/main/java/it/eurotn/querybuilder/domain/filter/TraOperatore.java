package it.eurotn.querybuilder.domain.filter;

import it.eurotn.querybuilder.domain.ProprietaEntity;

public class TraOperatore extends MultiValueFiltroQuery {

    @Override
    public String getSql(ProprietaEntity field, Object value) {
        String whereSql = "";

        if (value != null && value instanceof String) {
            String whereFieldName = getWhereFieldName(field);
            String[] values = parseValues((String) value, field);
            String dataIniziale = values.length > 0 ? values[0] : "";
            String dataFinale = values.length > 1 ? values[1] : "";

            whereSql = "(" + whereFieldName + " >= " + dataIniziale + " and " + whereFieldName + " <= " + dataFinale
                    + ") ";
        }

        return whereSql;
    }

}
