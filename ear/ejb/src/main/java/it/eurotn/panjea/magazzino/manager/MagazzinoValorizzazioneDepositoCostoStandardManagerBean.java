package it.eurotn.panjea.magazzino.manager;

import it.eurotn.panjea.magazzino.manager.interfaces.MagazzinoValorizzazioneDepositoManager;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.MagazzinoValorizzazioneDepositoCostoStandardManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.MagazzinoValorizzazioneDepositoCostoStandardManager")
public class MagazzinoValorizzazioneDepositoCostoStandardManagerBean extends MagazzinoValorizzazioneDepositoManagerBean
		implements MagazzinoValorizzazioneDepositoManager {

	@Override
	protected String getCostoAnagrafica() {
		return " art.costoStandard as costoVal, ";
	}

	@Override
	protected String getCostoInventario() {
		return " art.costoStandard as costoVal, ";
	}

	@Override
	protected String getCostoMovimentato() {
		return " art.costoStandard as costoVal, ";
	}
}
