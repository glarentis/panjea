package it.eurotn.panjea.anagrafica.rich.commands.navigationloaders;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.rich.commands.OpenAreeDocumentoCommand;
import it.eurotn.panjea.rich.factory.navigationloader.AbstractLoaderActionCommand;

import java.util.HashMap;
import java.util.Map;

public class DocumentoLoaderActionCommand extends AbstractLoaderActionCommand {

	private OpenAreeDocumentoCommand openAreeDocumentoCommand;

	/**
	 * contructor.
	 */
	public DocumentoLoaderActionCommand() {
		super("documentoLoaderActionCommand");

		this.openAreeDocumentoCommand = new OpenAreeDocumentoCommand();
	}

	@Override
	protected void doExecuteCommand() {

		Documento documento = (Documento) getParameter(PARAM_LOADER_OBJECT, null);
		if (documento != null && !documento.isNew()) {
			Map<Object, Object> params = new HashMap<Object, Object>();
			params.put(OpenAreeDocumentoCommand.PARAM_ID_DOCUMENTO, documento.getId());

			openAreeDocumentoCommand.execute(params);
		}

	}

	@Override
	public Class<?>[] getTypes() {
		return new Class<?>[] { Documento.class };
	}
}
