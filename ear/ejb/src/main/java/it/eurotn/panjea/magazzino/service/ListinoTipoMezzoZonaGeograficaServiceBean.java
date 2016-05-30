package it.eurotn.panjea.magazzino.service;

import it.eurotn.panjea.magazzino.domain.moduloprezzo.ListinoTipoMezzoZonaGeografica;
import it.eurotn.panjea.magazzino.manager.interfaces.ListinoTipoMezzoZonaGeograficaManager;
import it.eurotn.panjea.magazzino.service.interfaces.ListinoTipoMezzoZonaGeograficaService;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * 
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.ListinoTipoMezzoZonaGeograficaService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.ListinoTipoMezzoZonaGeograficaService")
public class ListinoTipoMezzoZonaGeograficaServiceBean implements ListinoTipoMezzoZonaGeograficaService {

	/**
	 * @uml.property name="listinoTipoMezzoZonaGeograficaManager"
	 * @uml.associationEnd
	 */
	@EJB
	protected ListinoTipoMezzoZonaGeograficaManager listinoTipoMezzoZonaGeograficaManager;

	@Override
	public void cancellaListinoTipoMezzoZonaGeografica(ListinoTipoMezzoZonaGeografica listinoTipoMezzoZonaGeografica) {
		listinoTipoMezzoZonaGeograficaManager.cancellaListinoTipoMezzoZonaGeografica(listinoTipoMezzoZonaGeografica);
	}

	@Override
	public List<ListinoTipoMezzoZonaGeografica> caricaListiniTipoMezzoZonaGeografica() {
		return listinoTipoMezzoZonaGeograficaManager.caricaListiniTipoMezzoZonaGeografica();
	}

	@Override
	public ListinoTipoMezzoZonaGeografica salvaListinoTipoMezzoZonaGeografica(
			ListinoTipoMezzoZonaGeografica listinoTipoMezzoZonaGeografica) {
		return listinoTipoMezzoZonaGeograficaManager
				.salvaListinoTipoMezzoZonaGeografica(listinoTipoMezzoZonaGeografica);
	}

}
