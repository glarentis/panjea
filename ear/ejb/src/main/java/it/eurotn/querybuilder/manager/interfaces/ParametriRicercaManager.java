package it.eurotn.querybuilder.manager.interfaces;

import java.util.List;

import javax.ejb.Local;

import it.eurotn.entity.EntityBase;
import it.eurotn.querybuilder.domain.ParametriRicercaQueryBuilder;

@Local
public interface ParametriRicercaManager {

    /**
     * Carica i paramentri di ricerca salvati per la classe indicata.
     *
     * @param clazz
     *            classe
     * @return parametri caricati
     */
    List<ParametriRicercaQueryBuilder> caricaParametriRicerca(Class<? extends EntityBase> clazz);

    /**
     * Salva i {@link ParametriRicercaQueryBuilder}.
     *
     * @param parametri
     *            parametri da salvare
     * @return parametri salvati
     */
    ParametriRicercaQueryBuilder salvaParametriRicerca(ParametriRicercaQueryBuilder parametri);
}
