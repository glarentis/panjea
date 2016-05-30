package it.eurotn.panjea.manutenzioni.manager.manutenzionisettings.interfaces;

import javax.ejb.Local;

import it.eurotn.panjea.manutenzioni.domain.ManutenzioneSettings;

@Local
public interface ManutenzioniSettingsManager {

    /**
     * Carica il settings delle manutenzioni.<br/>
     * Se non esiste ne crea uno, lo salva e lo restituisce.
     *
     * @return <code>ManutenzioneSettings</code> caricato
     */
    ManutenzioneSettings caricaManutenzioniSettings();

    /**
     * Salva un {@link ManutenzioneSettings}.
     *
     * @param manutenzioneSettings
     *            settings da salvare
     * @return <code>ManutenzioneSettings</code> salvato
     */
    ManutenzioneSettings salvaManutenzioneSettings(ManutenzioneSettings manutenzioneSettings);
}