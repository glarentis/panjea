/**
 * 
 */
package it.eurotn.panjea.pos.service;

import it.eurotn.panjea.pos.domain.PosSettings;
import it.eurotn.panjea.pos.manager.interfaces.PosSettingsManager;
import it.eurotn.panjea.pos.service.interfaces.PosSettingsService;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * @author fattazzo
 * 
 */
@Stateless(name = "Panjea.PosSettingsService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.PosSettingsService")
public class PosSettingsServiceBean implements PosSettingsService {

	@EJB
	private PosSettingsManager posSettingsManager;

	@Override
	public PosSettings caricaPosSettings() {
		return posSettingsManager.caricaPosSettings();
	}

	@Override
	public PosSettings salvaPosSettings(PosSettings posSettings) {
		return posSettingsManager.salvaPosSettings(posSettings);
	}

}
