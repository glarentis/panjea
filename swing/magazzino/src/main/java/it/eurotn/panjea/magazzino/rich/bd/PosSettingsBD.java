/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.bd;

import it.eurotn.panjea.pos.domain.PosSettings;
import it.eurotn.panjea.pos.service.interfaces.PosSettingsService;
import it.eurotn.panjea.rich.bd.AbstractBaseBD;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

import org.apache.log4j.Logger;

/**
 * @author fattazzo
 * 
 */
public class PosSettingsBD extends AbstractBaseBD implements IPosSettingsBD {

	private static Logger logger = Logger.getLogger(PosSettingsBD.class);

	public static final String BEAN_ID = "posSettingsBD";

	private PosSettingsService posSettingsService;

	@Override
	public PosSettings caricaPosSettings() {
		logger.debug("--> Enter caricaPosSettings");
		start("caricaPosSettings");

		PosSettings posSettings = null;
		try {
			posSettings = posSettingsService.caricaPosSettings();
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaPosSettings");
		}
		logger.debug("--> Exit caricaPosSettings ");
		return posSettings;
	}

	@Override
	public PosSettings salvaPosSettings(PosSettings posSettings) {
		logger.debug("--> Enter salvaPosSettings");
		start("salvaPosSettings");

		PosSettings posSettingsSalvati = null;
		try {
			posSettingsSalvati = posSettingsService.salvaPosSettings(posSettings);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaPosSettings");
		}
		logger.debug("--> Exit salvaPosSettings ");
		return posSettingsSalvati;
	}

	/**
	 * @param posSettingsService
	 *            the posSettingsService to set
	 */
	public void setPosSettingsService(PosSettingsService posSettingsService) {
		this.posSettingsService = posSettingsService;
	}

}
