package it.eurotn.querybuilder.manager.interfaces;

import java.util.List;

import javax.ejb.Local;

import it.eurotn.querybuilder.domain.ProprietaEntity;
import it.eurotn.querybuilder.domain.ProprietaEntityPersister;
import it.eurotn.querybuilder.domain.ResultCriteria;

@Local
public interface QueryExecutor {

    /**
     *
     * @param proprietaBase
     *            prop del bean di inizio della query
     * @param proprieta
     *            prop da ricercare
     * @return resultCriteria
     */
    ResultCriteria execute(ProprietaEntityPersister proprietaBase, List<ProprietaEntity> proprieta);
}
