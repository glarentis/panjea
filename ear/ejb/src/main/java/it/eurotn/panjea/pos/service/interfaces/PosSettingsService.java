/**
 * 
 */
package it.eurotn.panjea.pos.service.interfaces;

import it.eurotn.panjea.pos.domain.PosSettings;

import javax.ejb.Remote;

/**
 * @author fattazzo
 * 
 */
@Remote
public interface PosSettingsService {

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
