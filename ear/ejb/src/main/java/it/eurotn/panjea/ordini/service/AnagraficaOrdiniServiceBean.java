package it.eurotn.panjea.ordini.service;

import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.ordini.domain.OrdiniSettings;
import it.eurotn.panjea.ordini.domain.SedeOrdine;
import it.eurotn.panjea.ordini.manager.documento.evasione.interfaces.DistintaCaricoManager;
import it.eurotn.panjea.ordini.manager.interfaces.OrdiniSettingsManager;
import it.eurotn.panjea.ordini.manager.interfaces.SediOrdineManager;
import it.eurotn.panjea.ordini.service.interfaces.AnagraficaOrdiniService;

import java.util.Map;
import java.util.Set;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * @author fattazzo
 */
@Stateless(name = "Panjea.AnagraficaOrdiniService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.AnagraficaOrdiniService")
public class AnagraficaOrdiniServiceBean implements AnagraficaOrdiniService {

	@EJB
	private SediOrdineManager sediOrdineManager;

	@EJB
	private DistintaCaricoManager distintaCaricoManager;

	@EJB
	private OrdiniSettingsManager ordiniSettingsManager;

	@Override
	public void associaTipoAreaEvasione(Map<TipoAreaMagazzino, Set<EntitaLite>> map) {
		distintaCaricoManager.associaTipoAreaEvasione(map);
	}

	@Override
	public OrdiniSettings caricaOrdiniSettings() {
		return ordiniSettingsManager.caricaOrdiniSettings();
	}

	@Override
	public SedeOrdine caricaSedeOrdineBySedeEntita(SedeEntita sedeEntita, boolean ignoraEreditaDatiCommerciali) {
		return sediOrdineManager.caricaSedeOrdineBySedeEntita(sedeEntita, ignoraEreditaDatiCommerciali);
	}

	@Override
	public OrdiniSettings salvaOrdiniSettings(OrdiniSettings ordiniSettings) {
		return ordiniSettingsManager.salvaOrdiniSettings(ordiniSettings);
	}

	@Override
	public SedeOrdine salvaSedeOrdine(SedeOrdine sedeOrdine) {
		return sediOrdineManager.salvaSedeOrdine(sedeOrdine);
	}

}
