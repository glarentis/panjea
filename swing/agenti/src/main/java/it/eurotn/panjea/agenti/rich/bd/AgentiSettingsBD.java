/**
 * 
 */
package it.eurotn.panjea.agenti.rich.bd;

import it.eurotn.panjea.agenti.domain.AgentiSettings;
import it.eurotn.panjea.agenti.service.interfaces.AgentiSettingsService;
import it.eurotn.panjea.rich.bd.AbstractBaseBD;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

import org.apache.log4j.Logger;

/**
 * @author fattazzo
 * 
 */
public class AgentiSettingsBD extends AbstractBaseBD implements IAgentiSettingsBD {

	private static Logger logger = Logger.getLogger(AgentiSettingsBD.class);

	private AgentiSettingsService agentiSettingsService;

	@Override
	public AgentiSettings caricaAgentiSettings() {
		logger.debug("--> Enter caricaAgentiSettings");
		start("caricaAgentiSettings");
		AgentiSettings agentiSettingsCaricato = null;
		try {
			agentiSettingsCaricato = agentiSettingsService.caricaAgentiSettings();
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaAgentiSettings");
		}
		logger.debug("--> Exit caricaAgentiSettings ");
		return agentiSettingsCaricato;
	}

	@Override
	public AgentiSettings salvaAgentiSettings(AgentiSettings agentiSettings) {
		logger.debug("--> Enter salvaAgentiSettings");
		start("salvaAgentiSettings");
		AgentiSettings agentiSettingsSalvato = null;
		try {
			agentiSettingsSalvato = agentiSettingsService.salvaAgentiSettings(agentiSettings);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaAgentiSettings");
		}
		logger.debug("--> Exit salvaAgentiSettings ");
		return agentiSettingsSalvato;
	}

	/**
	 * @param agentiSettingsService
	 *            the agentiSettingsService to set
	 */
	public void setAgentiSettingsService(AgentiSettingsService agentiSettingsService) {
		this.agentiSettingsService = agentiSettingsService;
	}

}
