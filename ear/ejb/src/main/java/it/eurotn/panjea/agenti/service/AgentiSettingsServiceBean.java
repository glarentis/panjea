package it.eurotn.panjea.agenti.service;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.agenti.domain.AgentiSettings;
import it.eurotn.panjea.agenti.manager.interfaces.AgentiSettingsManager;
import it.eurotn.panjea.agenti.service.interfaces.AgentiSettingsService;

/**
 * @author fattazzo
 *
 */
@Stateless(name = "Panjea.AgentiSettingsService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.AgentiSettingsService")
public class AgentiSettingsServiceBean implements AgentiSettingsService {

    @EJB
    private AgentiSettingsManager agentiSettingsManager;

    @Override
    public AgentiSettings caricaAgentiSettings() {
        return agentiSettingsManager.caricaAgentiSettings();
    }

    @Override
    public AgentiSettings salvaAgentiSettings(AgentiSettings agentiSettings) {
        return agentiSettingsManager.salvaAgentiSettings(agentiSettings);
    }

}
