package it.eurotn.panjea.magazzino.manager.interfaces;

import it.eurotn.panjea.magazzino.domain.MagazzinoSettings;

import javax.ejb.Local;

/**
 * Manager per gestire i settings di magazzino.
 * 
 * @author Leonardo
 */
@Local
public interface MagazzinoSettingsManager {

	/**
	 * Carica il settings del magazziono.<br/>
	 * Se non esiste ne crea uno, lo salva e lo restituisce.
	 * 
	 * @return <code>MagazzinoSettings</code> caricato
	 */
	MagazzinoSettings caricaMagazzinoSettings();

	/**
	 * Salva un {@link MagazzinoSettings}.
	 * 
	 * @param magazzinoSettingsToSave
	 *            il settings di magazzino da salvare
	 * @return <code>MagazzinoSettings</code> salvato
	 */
	MagazzinoSettings salvaMagazzinoSettings(MagazzinoSettings magazzinoSettingsToSave);

}
