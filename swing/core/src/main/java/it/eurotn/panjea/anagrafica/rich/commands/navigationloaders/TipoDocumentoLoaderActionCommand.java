package it.eurotn.panjea.anagrafica.rich.commands.navigationloaders;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.rich.bd.IDocumentiBD;
import it.eurotn.panjea.rich.factory.navigationloader.OpenEditorLoaderActionCommand;

public class TipoDocumentoLoaderActionCommand extends OpenEditorLoaderActionCommand<TipoDocumento> {

	private IDocumentiBD documentiBD;

	/**
	 * contructor.
	 */
	public TipoDocumentoLoaderActionCommand() {
		super("tipoDocumentoLoaderActionCommand");
	}

	@Override
	protected TipoDocumento getObjectForOpenEditor(Object loaderObject) {
		TipoDocumento tipoDocumento = (TipoDocumento) loaderObject;
		return documentiBD.caricaTipoDocumento(tipoDocumento.getId().intValue());
	}

	@Override
	public Class<?>[] getTypes() {
		return new Class<?>[] { TipoDocumento.class };
	}

	/**
	 * @param documentiBD
	 *            The documentiBD to set.
	 */
	public void setDocumentiBD(IDocumentiBD documentiBD) {
		this.documentiBD = documentiBD;
	}
}
