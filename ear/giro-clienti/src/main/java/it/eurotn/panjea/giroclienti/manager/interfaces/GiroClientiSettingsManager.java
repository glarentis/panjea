package it.eurotn.panjea.giroclienti.manager.interfaces;

import javax.ejb.Local;

import it.eurotn.panjea.giroclienti.domain.GiroClientiSettings;

@Local
public interface GiroClientiSettingsManager {

    /**
     * Carica i settings del giro clienti. Se non esistono ne viene creato uno, salvato e restituito.
     *
     * @return settings
     */
    GiroClientiSettings caricaGiroClientiSettings();

    /**
     * Salva un {@link GiroClientiSettings}.
     *
     * @param giroClientiSettings
     *            settings da slavare
     * @return settings salvate
     */
    GiroClientiSettings salvaGiroClientiSettings(GiroClientiSettings giroClientiSettings);
}
