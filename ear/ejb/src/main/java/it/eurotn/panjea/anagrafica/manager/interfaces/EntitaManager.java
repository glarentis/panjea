package it.eurotn.panjea.anagrafica.manager.interfaces;

import java.util.List;

import javax.ejb.Local;

import it.eurotn.panjea.agenti.domain.Agente;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.TipoSedeEntita;
import it.eurotn.panjea.anagrafica.domain.Vettore;
import it.eurotn.panjea.anagrafica.domain.lite.AnagraficaLite;
import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficheDuplicateException;
import it.eurotn.panjea.anagrafica.util.parametriricerca.ParametriRicercaEntita;

@Local
public interface EntitaManager {

	/**
	 * Esegue la variazione della sede principale.
	 *
	 * @param sedeEntita
	 *            nuova sede entita principale
	 * @param tipoSedeEntita
	 *            tipo sede entita per la sede principale esistente
	 */
	void cambiaSedePrincipaleEntita(SedeEntita sedeEntita, TipoSedeEntita tipoSedeEntita);

	/**
	 * Cancella le anagrafiche che non hanno nessuna entità collegata.
	 *
	 * @throws AnagraficaServiceException
	 *             eccezione generica
	 */
	void cancellaAnagraficheOrfane() throws AnagraficaServiceException;

	/**
	 * Esegue la cancellazione di {@link Entita}.
	 *
	 * @param entita
	 *            .
	 * @throws AnagraficaServiceException
	 *             .
	 */
	void cancellaEntita(Entita entita) throws AnagraficaServiceException;

	/**
	 * Carica tutti gli agenti che non sono ancora associati ad un capo area.
	 *
	 * @return agenti caricati
	 */
	List<Agente> caricaAgentiSenzaCapoArea();

	/**
	 * Ricerca tutti i clienti in base al codice e descrizione .
	 *
	 * @param codice
	 *            codice da ricercare
	 * @param descrizione
	 *            descrizione da ricercare
	 * @return clienti trovati
	 */
	List<EntitaLite> caricaClienti(String codice, String descrizione);

	/**
	 *
	 * @return tutti i clienti con codice esterno che devono essere validati
	 */
	List<ClienteLite> caricaClientiConCodiceEsternoDaVerificare();

	/**
	 * Carica tutte le entita associate all'anagrafica specificata.
	 *
	 * @param anagraficaLite
	 *            anagrafica di riferimento
	 * @return entita caricate
	 */
	List<EntitaLite> caricaEntita(AnagraficaLite anagraficaLite);

	/**
	 * restituisce un {@link Entita} di entita.class e identificata da
	 * entita.getId().
	 *
	 * @param entita
	 *            .
	 * @param caricaLazy
	 *            boolean for load lazy.
	 * @return entita.
	 * @throws AnagraficaServiceException
	 *             .
	 */
	Entita caricaEntita(Entita entita, Boolean caricaLazy) throws AnagraficaServiceException;

	/**
	 * restituisce un {@link Entita} attraverso il corrispondente oggetto lite.
	 *
	 * @param entitaLite
	 *            .
	 * @param caricaLazy
	 *            boolean for load lazy.
	 * @return entita.
	 * @throws AnagraficaServiceException
	 *             .
	 */
	Entita caricaEntita(EntitaLite entitaLite, Boolean caricaLazy) throws AnagraficaServiceException;

	/**
	 * Carica una entita del tipo specificato.
	 *
	 * @param tipoEntita
	 *            tipo entita
	 * @param codice
	 *            codice entita
	 * @return entita
	 */
	EntitaLite caricaEntita(TipoEntita tipoEntita, Integer codice);

	/**
	 * restituisce un {@link EntitaLite} di {@link EntitaLite}.class e
	 * identificata da entita.getId().
	 *
	 * @param entitaLite
	 *            entita lite.
	 * @return entita lite.
	 * @throws AnagraficaServiceException
	 *             .
	 */
	EntitaLite caricaEntitaLite(EntitaLite entitaLite) throws AnagraficaServiceException;

	/**
	 * Carica un vettore attraverso il codice di importazione.
	 *
	 * @param codiceEsterno
	 *            codice.
	 * @return Vettore .
	 * @throws AnagraficaServiceException
	 *             .
	 */
	Vettore caricaVettorePerCodiceImportazione(java.lang.Integer codiceEsterno) throws AnagraficaServiceException;

	/**
	 * Conferma un cliente potenziale convertendolo in cliente, nel processo
	 * devo risalvare il codice entita' e associare il conto.
	 *
	 * @param idEntita
	 *            .
	 * @return entita.
	 */
	Entita confermaClientePotenziale(Integer idEntita);

	/**
	 * restituisce {@link List} di {@link EntitaLite} che rispondono ai criteri
	 * presenti in parametri.
	 *
	 * @param parametriRicercaEntita
	 *            parametri di ricerca
	 * @return entità trovate
	 */
	List<EntitaLite> ricercaEntita(ParametriRicercaEntita parametriRicercaEntita);

	/**
	 * restituisce {@link List} di {@link EntitaLite} che rispondono ai criteri
	 * presenti.
	 *
	 * @param codiceFiscale
	 *            codice fiscale
	 * @param partitaIva
	 *            partita iva
	 * @return entità trovate
	 */
	List<EntitaLite> ricercaEntita(String codiceFiscale, String partitaIva);

	/**
	 * Esegue una ricerca ottimizzata di {@link EntitaLite} che rispondono ai
	 * criteri presenti in parametri.
	 *
	 * @param parametriRicercaEntita
	 *            parametri di ricerca
	 * @return entità trovate
	 */
	List<EntitaLite> ricercaEntitaSearchObject(ParametriRicercaEntita parametriRicercaEntita);

	/**
	 * esegue il salvataggio di {@link Entita}.
	 *
	 * @param entita
	 *            da salvare.
	 * @return entita salvata.
	 * @throws AnagraficaServiceException
	 *             .
	 * @throws AnagraficheDuplicateException
	 *             .
	 */
	Entita salvaEntita(Entita entita) throws AnagraficaServiceException, AnagraficheDuplicateException;
}
