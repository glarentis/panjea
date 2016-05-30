package it.eurotn.querybuilder.domain.filter;

import it.eurotn.querybuilder.domain.ProprietaEntity;

public class LikeOperatore extends FiltroOperatore {

    /**
     * Costruttore
     */
    public LikeOperatore() {
        super("like");
    }

    @Override
    public String getSql(ProprietaEntity field, Object filtro) {
        String where = super.getSql(field, filtro);
        return where.replace("*", "%");
    }

}
