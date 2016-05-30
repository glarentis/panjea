package it.eurotn.panjea.agenti.service.interfaces;

import javax.ejb.Remote;

import it.eurotn.panjea.agenti.domain.AgentiSettings;

/**
 * @author fattazzo
 *
 */
@Remote
public interface AgentiSettingsService {

    /**
     * Carica i settings degli agenti. Se non esistono ne viene creato uno, salvato e lo restituito.
     *
     * @return settings degli agenti
     */
    AgentiSettings caricaAgentiSettings();

    /**
     * Salva un {@link AgentiSettings}.
     *
     * @param agentiSettings
     *            settings da salvare
     * @return settings salvate
     */
    AgentiSettings salvaAgentiSettings(AgentiSettings agentiSettings);
}
