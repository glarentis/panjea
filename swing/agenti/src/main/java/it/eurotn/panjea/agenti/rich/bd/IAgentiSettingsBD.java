/**
 * 
 */
package it.eurotn.panjea.agenti.rich.bd;

import it.eurotn.panjea.agenti.domain.AgentiSettings;

/**
 * @author fattazzo
 * 
 */
public interface IAgentiSettingsBD {

	/**
	 * Carica i settings degli agenti. Se non esistono ne viene creato uno,
	 * salvato e lo restituito.
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
