/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.bd;

import it.eurotn.panjea.pos.domain.PosSettings;

/**
 * @author fattazzo
 * 
 */
public interface IPosSettingsBD {

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
