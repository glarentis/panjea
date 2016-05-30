/**
 *
 */
package it.eurotn.panjea.beniammortizzabili2.manager.interfaces;

import it.eurotn.panjea.beniammortizzabili2.domain.BeniSettings;

import javax.ejb.Local;

/**
 * @author fattazzo
 *
 */
@Local
public interface BeniAmmortizzabiliSettingsManager {

	/**
	 * Carica le settings dei beni ammortizzabili.
	 *
	 * @return settings
	 */
	BeniSettings caricaBeniSettings();

	/**
	 * Salva le settings dei beni ammortizzabili.
	 *
	 * @param beniSettings
	 *            settings da salvare
	 * @return settings salvate
	 */
	BeniSettings salvaBeniSettings(BeniSettings beniSettings);

}
