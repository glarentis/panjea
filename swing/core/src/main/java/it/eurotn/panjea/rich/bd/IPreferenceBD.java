package it.eurotn.panjea.rich.bd;

import java.util.List;

import it.eurotn.panjea.anagrafica.domain.Preference;
import it.eurotn.panjea.anagrafica.service.interfaces.PreferenceService;

public interface IPreferenceBD {

    /**
     * Cancella la {@link Preference} passata come parametro.
     *
     * @param preference
     *            {@link Preference} da cancellare
     */
    void cancellaPreference(Preference preference);

    /**
     * Carica la {@link Preference} in base alla chiave fornita.
     *
     * @param key
     *            chiave
     * @return {@link Preference} caricata
     */
    Preference caricaPreference(String key);

    /**
     * Carica tutte le {@link Preference} configurate.
     *
     * @return lista di {@link Preference} caricate
     */
    @AsyncMethodInvocation
    List<Preference> caricaPreferences();

    /**
     * Carica tutte le prefereze dell'utente.
     *
     * @param userName
     *            utente di riferimento
     * @return mappa contenente chiave e valore delle {@link Preference}
     */
    java.util.Map<String, String> caricaPreferences(java.lang.String userName);

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
    void salvaPreferences(java.util.Map<String, String> preferences, java.lang.String userName);

    /**
     * @param preferenceService
     *            setter of preferenceService
     */
    void setPreferenceService(PreferenceService preferenceService);

}