package it.eurotn.querybuilder.service.interfaces;

import java.util.List;

import javax.ejb.Remote;

import it.eurotn.entity.EntityBase;
import it.eurotn.querybuilder.domain.EntitaQuerableMetaData;
import it.eurotn.querybuilder.domain.ParametriRicercaQueryBuilder;
import it.eurotn.querybuilder.domain.ProprietaEntity;
import it.eurotn.querybuilder.domain.ProprietaEntityPersister;
import it.eurotn.querybuilder.domain.ResultCriteria;

@Remote
public interface QueryBuilderService {

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

    /**
     * Carica i paramentri di ricerca salvati per la classe indicata.
     *
     * @param clazz
     *            classe
     * @return parametri caricati
     */
    List<ParametriRicercaQueryBuilder> caricaParametriRicerca(Class<? extends EntityBase> clazz);

    /**
     *
     * @param proprietaBase
     *            prop del bean di inizio della query
     * @param proprieta
     *            prop da ricercare
     * @return resultCriteria
     */
    ResultCriteria execute(ProprietaEntityPersister proprietaBase, List<ProprietaEntity> proprieta);

    /**
     * Salva i {@link ParametriRicercaQueryBuilder}.
     *
     * @param parametri
     *            parametri da salvare
     * @return parametri salvati
     */
    ParametriRicercaQueryBuilder salvaParametriRicerca(ParametriRicercaQueryBuilder parametri);

}
