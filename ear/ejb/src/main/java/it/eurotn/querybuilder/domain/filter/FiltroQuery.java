package it.eurotn.querybuilder.domain.filter;

import it.eurotn.querybuilder.domain.ProprietaEntity;

public abstract class FiltroQuery {

    /**
     *
     * @param field
     *            field da filtrare
     * @param value
     *            valore da filtrare
     * @return filtro sql
     */
    public abstract String getSql(ProprietaEntity field, Object value);

    /**
     * Nome del campo da utilizzare per comporre la where.
     *
     * @param field
     *            properità
     * @return nome campo
     */
    public String getWhereFieldName(ProprietaEntity field) {
        if (field.getParentProprietaEntity() != null && field.getParentProprietaEntity().isEmbedded()) {
            return field.getAliasBean() + "." + field.getParentProprietaEntity().getNome() + "." + field.getNome();
        } else {
            return field.getAliasBean() + "." + field.getNome();
        }
    }
}
