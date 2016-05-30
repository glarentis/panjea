package it.eurotn.panjea.rate.service;

import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.partite.domain.CategoriaRata;
import it.eurotn.panjea.rate.domain.CalendarioRate;
import it.eurotn.panjea.rate.domain.RigaCalendarioRate;
import it.eurotn.panjea.rate.manager.interfaces.CalendariRateManager;
import it.eurotn.panjea.rate.service.interfaces.CalendariRateService;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.CalendariRateService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.CalendariRateService")
public class CalendariRateServiceBean implements CalendariRateService {

	@EJB
	private CalendariRateManager calendariRateManager;

	@Override
	public void cancellaCalendarioRate(CalendarioRate calendarioRate) {
		calendariRateManager.cancellaCalendarioRate(calendarioRate);
	}

	@Override
	public void cancellaRigaCalendarioRate(RigaCalendarioRate rigaCalendarioRate) {
		calendariRateManager.cancellaRigaCalendarioRate(rigaCalendarioRate);
	}

	@Override
	public CalendarioRate caricaCalendarioRate(CalendarioRate calendarioRate, boolean loadLazy) {
		return calendariRateManager.caricaCalendarioRate(calendarioRate, loadLazy);
	}

	@Override
	public List<CalendarioRate> caricaCalendariRate(String fieldSearch, String valueSearch) {
		return calendariRateManager.caricaCalendariRate(fieldSearch, valueSearch);
	}

	@Override
	public List<CalendarioRate> caricaCalendariRateAzienda() {
		return calendariRateManager.caricaCalendariRateAzienda();
	}

	@Override
	public List<CalendarioRate> caricaCalendariRateCliente(ClienteLite clienteLite) {
		return calendariRateManager.caricaCalendariRateCliente(clienteLite);
	}

	@Override
	public List<ClienteLite> caricaClientiNonDisponibiliPerCalendario(List<CategoriaRata> categorieRateDaScludere) {
		return calendariRateManager.caricaClientiNonDisponibiliPerCalendario(categorieRateDaScludere);
	}

	@Override
	public List<RigaCalendarioRate> caricaRigheCalendarioRate(CalendarioRate calendarioRate) {
		return calendariRateManager.caricaRigheCalendarioRate(calendarioRate);
	}

	@Override
	public CalendarioRate salvaCalendarioRate(CalendarioRate calendarioRate) {
		return calendariRateManager.salvaCalendarioRate(calendarioRate);
	}

	@Override
	public RigaCalendarioRate salvaRigaCalendarioRate(RigaCalendarioRate rigaCalendarioRate) {
		return calendariRateManager.salvaRigaCalendarioRate(rigaCalendarioRate);
	}

}
