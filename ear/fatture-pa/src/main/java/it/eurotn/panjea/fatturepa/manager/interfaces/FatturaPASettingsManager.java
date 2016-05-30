package it.eurotn.panjea.fatturepa.manager.interfaces;

import javax.ejb.Local;

import it.eurotn.panjea.fatturepa.domain.FatturaPASettings;

/**
 * @author fattazzo
 *
 */
@Local
public interface FatturaPASettingsManager {

    /**
     * Carica i settings della fattura PA. Se non esistono ne viene creato uno, salvato e lo restituito.
     *
     * @return settings
     */
    FatturaPASettings caricaFatturaPASettings();

    /**
     * Salva un {@link FatturaPASettings}.
     *
     * @param fatturaPaSettings
     *            settings da salvare
     * @return settings salvate
     */
    FatturaPASettings salvaFatturaPASettings(FatturaPASettings fatturaPaSettings);

}
