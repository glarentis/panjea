/**
 *
 */
package it.eurotn.panjea.rich.editors.documentograph.toolbar.cell;

import it.eurotn.panjea.anagrafica.rich.commands.OpenAreeDocumentoCommand;
import it.eurotn.panjea.documenti.graph.node.DocumentoNode;
import it.eurotn.panjea.rich.editors.documentograph.toolbar.AbstractToolbarGraphCommand;

import org.springframework.richclient.util.RcpSupport;

/**
 * @author fattazzo
 *
 */
public class OpenDocumentCommand extends AbstractToolbarGraphCommand {

	public static final String COMMAND_ID = "openDocumentCommand";

	private OpenAreeDocumentoCommand openAreeDocumentoCommand;

	/**
	 * Costruttore.
	 */
	public OpenDocumentCommand() {
		super(COMMAND_ID);
		RcpSupport.configure(this);
		this.openAreeDocumentoCommand = new OpenAreeDocumentoCommand();
	}

	@Override
	protected void doExecuteCommand() {
		openAreeDocumentoCommand.addParameter(OpenAreeDocumentoCommand.PARAM_ID_DOCUMENTO, node.getIdDocumento());
		openAreeDocumentoCommand.execute();
	}

	@Override
	protected void doUpdate(DocumentoNode node) {
		// Non faccio niente
		setVisible(node.getIdDocumento() != null);
	}

}
