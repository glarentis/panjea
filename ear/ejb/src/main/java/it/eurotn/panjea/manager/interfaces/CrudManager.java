package it.eurotn.panjea.manager.interfaces;

import java.util.List;

import it.eurotn.entity.EntityBase;
import it.eurotn.entity.IEntityCodiceAzienda;

public interface CrudManager<T extends EntityBase> {

    /**
     * Cancella l'oggetto passato come parametro.
     *
     * @param id
     *            id
     */
    void cancella(Integer id);

    /**
     * Cancella l'oggetto passato come parametro.
     *
     * @param object
     *            oggetto da cancellare
     */
    void cancella(T object);

    /**
     * Carica tutti gli oggetti presenti.
     *
     * @return oggetti caricati
     */
    List<T> caricaAll();

    /**
     * Carica l'oggetto in base al suo id.
     *
     * @param id
     *            id
     * @return oggetto caricato
     */
    T caricaById(Integer id);

    /**
     * Salva l'oggetto passato come parametro. Se l'oggetto implementa {@link IEntityCodiceAzienda} viene avvalorata la
     * proprietà relativa al codice azienda.
     *
     * @param object
     *            oggetto da salvare
     * @return oggetto salvato
     */
    T salva(T object);
}
