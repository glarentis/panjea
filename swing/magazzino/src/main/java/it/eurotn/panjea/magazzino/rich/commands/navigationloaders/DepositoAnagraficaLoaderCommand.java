package it.eurotn.panjea.magazzino.rich.commands.navigationloaders;

import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.rich.factory.navigationloader.OpenEditorLoaderActionCommand;

public class DepositoAnagraficaLoaderCommand extends OpenEditorLoaderActionCommand<String> {

	/**
	 * constructor.
	 */
	public DepositoAnagraficaLoaderCommand() {
		super("depositoAnagraficaLoaderCommand");
	}

	@Override
	protected String getObjectForOpenEditor(Object loaderObject) {
		return "aziendaEditor#depositiSedeAziendaTablePage";
	}

	@Override
	public Class<?>[] getTypes() {
		return new Class[] { Deposito.class, DepositoLite.class };
	}
}
