package it.eurotn.panjea.dms.manager.interfaces;

import javax.ejb.Local;

import it.eurotn.panjea.dms.domain.DmsSettings;

@Local
public interface DMSSettingsManager {

    /**
     * Carica i settings presenti.
     *
     * @return {@link DmsSettings}
     */
    DmsSettings caricaDmsSettings();

    /**
     *
     * @return true/false se ho logicaldoc abilitato
     */
    boolean isEnable();

    /**
     * Salva un {@link DmsSettings}.
     *
     * @param dmsSettings
     *            settings da salvare
     * @return settings salvate
     */
    DmsSettings salvaDmsSettings(DmsSettings dmsSettings);
}
