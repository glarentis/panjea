package it.eurotn.panjea.vending.manager.vendingsettings.interfaces;

import javax.ejb.Local;

import it.eurotn.panjea.vending.domain.VendingSettings;

@Local
public interface VendingSettingsManager {

    /**
     * Carica il settings del vending.<br/>
     * Se non esiste ne crea uno, lo salva e lo restituisce.
     *
     * @return <code>VendingSettings</code> caricato
     */
    VendingSettings caricaVendingSettings();

    /**
     * Salva un {@link VendingSettings}.
     *
     * @param vendingSettings
     *            settings da salvare
     * @return <code>VendingSettings</code> salvato
     */
    VendingSettings salvaVendingSettings(VendingSettings vendingSettings);
}