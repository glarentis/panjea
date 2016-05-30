package it.eurotn.querybuilder.domain.filter;

import org.apache.commons.lang3.StringUtils;

import it.eurotn.querybuilder.domain.ProprietaEntity;

public class InOperatore extends MultiValueFiltroQuery {

    @Override
    public String getSql(ProprietaEntity field, Object value) {
        String[] values = parseValues((String) value, field);

        return getWhereFieldName(field) + " IN (" + StringUtils.join(values, ",") + ") ";
    }

}
