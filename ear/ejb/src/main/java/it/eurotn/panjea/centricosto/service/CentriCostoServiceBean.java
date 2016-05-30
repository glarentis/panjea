package it.eurotn.panjea.centricosto.service;

import it.eurotn.panjea.centricosto.domain.CentroCosto;
import it.eurotn.panjea.centricosto.manager.interfaces.CentriCostoManager;
import it.eurotn.panjea.centricosto.service.interfaces.CentriCostoService;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.CentriCostoService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.CentriCostoService")
public class CentriCostoServiceBean implements CentriCostoService {
	@EJB
	private CentriCostoManager centriCostoManager;

	@Override
	public void cancellaCentroCosto(CentroCosto centroCosto) {
		centriCostoManager.cancellaCentroCosto(centroCosto);
	}

	@Override
	public List<CentroCosto> caricaCentriCosto(String codice) {
		return centriCostoManager.caricaCentriCosto(codice);
	}

	@Override
	public CentroCosto salvaCentroCosto(CentroCosto centroCosto) {
		return centriCostoManager.salvaCentroCosto(centroCosto);
	}

}
