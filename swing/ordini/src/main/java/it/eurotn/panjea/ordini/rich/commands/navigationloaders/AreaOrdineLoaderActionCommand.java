package it.eurotn.panjea.ordini.rich.commands.navigationloaders;

import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.rich.factory.navigationloader.OpenEditorLoaderActionCommand;

public class AreaOrdineLoaderActionCommand extends OpenEditorLoaderActionCommand<AreaOrdine> {

	/**
	 * Costruttore.
	 */
	public AreaOrdineLoaderActionCommand() {
		super("areaOrdineLoaderActionCommand");
	}

	@Override
	protected AreaOrdine getObjectForOpenEditor(Object loaderObject) {
		return (AreaOrdine) loaderObject;
	}

	@Override
	public Class<?>[] getTypes() {
		return new Class<?>[] { AreaOrdine.class };
	}
}
