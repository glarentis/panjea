package it.eurotn.panjea.tesoreria.manager.interfaces;

import it.eurotn.panjea.tesoreria.domain.TesoreriaSettings;

import javax.ejb.Local;

@Local
public interface TesoreriaSettingsManager {
	/**
	 * 
	 * @return settings di tesoreria
	 */
	TesoreriaSettings caricaSettings();

	/**
	 * 
	 * @param tesoreriaSettings
	 *            settings da salvare
	 * @return settings salvati
	 */
	TesoreriaSettings salva(TesoreriaSettings tesoreriaSettings);
}
