package it.eurotn.panjea.anagrafica.manager.interfaces;

import java.util.List;

import javax.ejb.Local;

import it.eurotn.panjea.anagrafica.domain.Anagrafica;
import it.eurotn.panjea.anagrafica.domain.Azienda;
import it.eurotn.panjea.anagrafica.domain.CategoriaEntita;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.SedeAnagrafica;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.AnagraficaLite;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;
import it.eurotn.panjea.anagrafica.util.RubricaDTO;

@Local
public interface AnagraficheManager {

    /**
     *
     * @param categoriaEntita
     *            entità da cancellare.
     */
    void cancellaCategoriaEntita(CategoriaEntita categoriaEntita);

    /**
     * Esegue la cancellazione di {@link SedeAnagrafica}.
     *
     * @param sedeAnagrafica
     *            sedeAnagrafica da cancellare
     */
    void cancellaSedeAnagrafica(SedeAnagrafica sedeAnagrafica);

    /**
     * Recupera {@link Anagrafica} identificata dall'argomento idAnagrafica.
     *
     * @param idAnagrafica
     *            id dell'anagrafica da caricare
     * @return anagrafica
     * @throws AnagraficaServiceException
     *             eccezione generica
     */
    Anagrafica caricaAnagrafica(Integer idAnagrafica) throws AnagraficaServiceException;

    /**
     * recupera la list di Anagrafica, filtrate per l'argomento partitaIva e/o codice fiscale se
     * valorizzato.
     *
     * @param partitaIva
     *            per la quale caricare l'anagrafica (può essere vuota o null
     * @param codiceFiscale
     *            per la quale caricare l'anagrafica (può essere vuota o null
     * @return lista di anagrafiche filtrate da partitaIva e codicefiscale
     * @throws AnagraficaServiceException
     *             eccezione generica
     *
     */
    List<Anagrafica> caricaAnagrafiche(String partitaIva, String codiceFiscale) throws AnagraficaServiceException;

    /**
     *
     * @return tutte le anagrafiche aziendali con "l'albero" completo (entità,sedi,contatti)
     */
    List<RubricaDTO> caricaAnagraficheFull();

    /**
     * Carica tutte le anagrafiche presenti per l'azienda loggata.
     *
     * @param codice
     *            codice fiscale o partita iva da filtrare
     * @param denominazione
     *            denominazione
     * @return anagrafiche caricate
     */
    List<AnagraficaLite> caricaAnagraficheSearchObject(String codice, String denominazione);

    /**
     * Recupera la lista di categorie per un entita.
     *
     * @param idEntita
     *            id dell'entità interessata
     * @return categorie dell'entità.
     * @throws AnagraficaServiceException
     *             eccezione generica
     */
    List<CategoriaEntita> caricaCategoriaEntitaByEntita(Integer idEntita) throws AnagraficaServiceException;

    /**
     * Recupera la lista delle categorie entita.
     *
     * @param fieldSearch
     *            .
     * @param valueSearch
     *            .
     * @return lista delle categorie associabili all'entità
     * @throws AnagraficaServiceException
     *             eccezione generica
     */
    List<CategoriaEntita> caricaCategorieEntita(String fieldSearch, String valueSearch)
            throws AnagraficaServiceException;

    /**
     * carica la liste de categorie della entita.
     *
     * @param idEntita
     * @return
     * @throws AnagraficaServiceException
     */
    List<SedeEntita> caricaEntitaByCategorie(List<CategoriaEntita> categorie);

    /**
     * Recupera la {@link List} di {@link SedeAnagrafica} associate ad {@link Anagrafica}.
     *
     * @param anagrafica
     *            anagrafica per la quale recuperare le sedi
     * @return lista di sediAnagrafiche collegate all'anagrafica
     * @throws AnagraficaServiceException
     *             eccezione generica
     */
    List<SedeAnagrafica> caricaSediAnagrafica(Anagrafica anagrafica) throws AnagraficaServiceException;

    /**
     * Esegue l'eliminazione delle {@link SedeAnagrafica} rimaste senza associazione.
     */
    void eliminaSediAnagraficheOrfane();

    /**
     * verifica se {@link SedeAnagrafica} risulta essere non associata a nessuna {@link Azienda} e a
     * nessuna {@link Entita}, esclusa la SedeEntita come sua ultima relazione.
     *
     * @param sedeAnagrafica
     *            sedeAnagrafica interessata
     * @return true se la dese non è associate a nessuna sedeEntità.
     * @throws AnagraficaServiceException
     *             eccezione generica
     */
    boolean isSedeOrphan(SedeAnagrafica sedeAnagrafica) throws AnagraficaServiceException;

    /**
     *
     * @param categoriaEntita
     *            categorie da salvare
     * @return categoria salvata
     */
    CategoriaEntita salvaCategoriaEntita(CategoriaEntita categoriaEntita);

    List<?> test();
}
