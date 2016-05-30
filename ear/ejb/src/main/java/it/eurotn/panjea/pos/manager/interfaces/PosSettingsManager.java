/**
 * 
 */
package it.eurotn.panjea.pos.manager.interfaces;

import it.eurotn.panjea.pos.domain.PosSettings;

import javax.ejb.Local;

/**
 * @author fattazzo
 * 
 */
@Local
public interface PosSettingsManager {

	/**
	 * Carica i settings del pos. Se non esistono ne viene restituito uno nuovo.
	 * 
	 * @return settings del pos
	 */
	PosSettings caricaPosSettings();

	/**
	 * Salva un {@link PosSettings}.
	 * 
	 * @param posSettings
	 *            settings da salvare
	 * @return settings salvate
	 */
	PosSettings salvaPosSettings(PosSettings posSettings);

}
