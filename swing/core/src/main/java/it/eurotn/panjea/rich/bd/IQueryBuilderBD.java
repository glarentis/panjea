package it.eurotn.panjea.rich.bd;

import java.util.List;

import it.eurotn.entity.EntityBase;
import it.eurotn.querybuilder.domain.EntitaQuerableMetaData;
import it.eurotn.querybuilder.domain.ParametriRicercaQueryBuilder;
import it.eurotn.querybuilder.domain.ProprietaEntity;
import it.eurotn.querybuilder.domain.ResultCriteria;

public interface IQueryBuilderBD {
    /**
     *
     * @return lista di classi che possono essere ricercate
     */
    List<Class<?>> caricaAllEntityQuerable();

    /**
     *
     * @param proprieta
     *            classe
     * @return dataQuery per costruire la query su entityBase
     */
    EntitaQuerableMetaData caricaEntitaQuerableMetaData(ProprietaEntity proprieta);

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
     * @param clazz
     *            classe da eseguire
     * @param proprieta
     *            prop
     * @return result query
     */
    ResultCriteria execute(Class<?> clazz, List<ProprietaEntity> proprieta);

    /**
     * Salva i {@link ParametriRicercaQueryBuilder}.
     *
     * @param parametri
     *            parametri da salvare
     * @return parametri salvati
     */
    ParametriRicercaQueryBuilder salvaParametriRicerca(ParametriRicercaQueryBuilder parametri);
}
