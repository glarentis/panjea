/**
 * 
 */
package it.eurotn.panjea.magazzino.manager.manutenzionelistino.updater;

import it.eurotn.panjea.magazzino.manager.manutenzionelistino.updater.interfaces.ListinoManutenzioneUpdater;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;

/**
 * @author fattazzo
 * 
 */
public abstract class ListinoManutenzioneAbstractUpdaterBean implements ListinoManutenzioneUpdater {

	@EJB
	protected PanjeaDAO panjeaDAO;

	@Resource
	private SessionContext sessionContext;

	/**
	 * 
	 * @return codice azienda loggata
	 */
	public JecPrincipal getPrincipal() {
		return ((JecPrincipal) sessionContext.getCallerPrincipal());
	}
}
