package it.eurotn.panjea.tesoreria.manager;

import it.eurotn.panjea.tesoreria.domain.AreaAnticipo;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaAnticipoContabilitaManager;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "AreaAnticipoContabilitaManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AreaAnticipoContabilitaManager")
public class AreaAnticipoContabilitaManagerBean implements AreaAnticipoContabilitaManager {

	@Override
	public void creaAreaContabileAnticipo(AreaAnticipo areaAnticipo) {
		

	}

}
