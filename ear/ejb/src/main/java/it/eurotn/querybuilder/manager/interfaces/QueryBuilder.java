package it.eurotn.querybuilder.manager.interfaces;

import java.util.List;

import javax.ejb.Local;

import it.eurotn.querybuilder.domain.EntitaQuerableMetaData;
import it.eurotn.querybuilder.domain.ProprietaEntity;

@Local
public interface QueryBuilder {

    /**
     *
     * @return lista di classi che possono essere ricercate
     */
    List<String> caricaAllEntityQuerable();

    /**
     *
     * @param entity
     *            entity
     * @return dataQuery per costruire la query su entityBase
     */
    EntitaQuerableMetaData caricaEntitaQuerableMetaData(ProprietaEntity entity);
}
