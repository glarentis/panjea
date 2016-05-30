package it.eurotn.panjea.anagrafica.service.interfaces;

import it.eurotn.panjea.anagrafica.domain.Preference;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;
import it.eurotn.panjea.anagrafica.service.exception.PreferenceNotFoundException;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

@Remote
public interface PreferenceService {

	/**
	 * Cancella la {@link Preference} passata come parametro.
	 * 
	 * @param preference
	 *            {@link Preference} da cancellare
	 * @throws AnagraficaServiceException
	 *             eccezione generica
	 */
	void cancellaPreference(Preference preference) throws AnagraficaServiceException;

	/**
	 * Carica la {@link Preference} in base alla chiave fornita.
	 * 
	 * @param key
	 *            chiave
	 * @return {@link Preference} caricata
	 * @throws PreferenceNotFoundException
	 *             Preference non trovata.
	 */
	Preference caricaPreference(String key) throws PreferenceNotFoundException;

	/**
	 * Carica tutte le {@link Preference} configurate.
	 * 
	 * @return lista di {@link Preference} caricate
	 */
	List<Preference> caricaPreferences();

	/**
	 * Carica tutte le prefereze dell'utente.
	 * 
	 * @param userName
	 *            utente di riferimento
	 * @return mappa contenente chiave e valore delle {@link Preference}
	 */
	Map<String, String> caricaPreferences(String userName);

	/**
	 * Salva una {@link Preference}.
	 * 
	 * @param preference
	 *            {@link Preference} da salvare
	 * @return {@link Preference} salvata
	 */
	Preference salvaPreference(Preference preference);

	/**
	 * Salva una lista di {@link Preference} associandole all'utente.
	 * 
	 * @param preferences
	 *            {@link Preference} da salvare
	 * @param userName
	 *            utente di riferimento
	 */
	void salvaPreferences(Map<String, String> preferences, String userName);

}
