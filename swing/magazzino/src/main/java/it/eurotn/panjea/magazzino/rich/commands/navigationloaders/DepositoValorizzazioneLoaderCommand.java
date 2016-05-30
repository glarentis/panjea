package it.eurotn.panjea.magazzino.rich.commands.navigationloaders;

import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.magazzino.util.ParametriRicercaValorizzazione;
import it.eurotn.panjea.rich.factory.navigationloader.OpenEditorLoaderActionCommand;

import java.util.ArrayList;
import java.util.List;

public class DepositoValorizzazioneLoaderCommand extends OpenEditorLoaderActionCommand<ParametriRicercaValorizzazione> {

	/**
	 * constructor.
	 */
	public DepositoValorizzazioneLoaderCommand() {
		super("depositoValorizzazioneLoaderCommand");
	}

	@Override
	protected ParametriRicercaValorizzazione getObjectForOpenEditor(Object loaderObject) {
		Deposito deposito = null;

		if (loaderObject instanceof DepositoLite) {
			DepositoLite lite = (DepositoLite) loaderObject;
			deposito = lite.creaProxy();
		} else {
			deposito = (Deposito) loaderObject;
		}

		ParametriRicercaValorizzazione parametri = new ParametriRicercaValorizzazione();
		List<Deposito> depositiValorizzazione = new ArrayList<Deposito>();
		depositiValorizzazione.add(deposito);
		parametri.setDepositi(depositiValorizzazione);
		parametri.setEffettuaRicerca(true);
		return parametri;
	}

	@Override
	public Class<?>[] getTypes() {
		return new Class[] { Deposito.class, DepositoLite.class };
	}

}
