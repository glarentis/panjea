package it.eurotn.panjea.rich.editors.documentograph;

import it.eurotn.panjea.documenti.graph.util.DocumentoGraph;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

public class OpenDocumentoGraphEditorCommand extends ApplicationWindowAwareCommand {

	public static final String COMMAND_ID = "openDocumentoGraphEditor";

	public static final String PARAM_ID_DOCUMENTO = "paramIdDocumento";

	/**
	 * Costruttore.
	 */
	public OpenDocumentoGraphEditorCommand() {
		super(COMMAND_ID);
		RcpSupport.configure(this);
	}

	@Override
	protected void doExecuteCommand() {
		Integer idDocumento = (Integer) getParameter(PARAM_ID_DOCUMENTO, null);

		if (idDocumento == null) {
			throw new RuntimeException("Impossibile aprire il grafico del documento: id nullo.");
		}

		DocumentoGraph documentoGraph = new DocumentoGraph();
		documentoGraph.setIdDocumento(idDocumento);
		LifecycleApplicationEvent event = new OpenEditorEvent(documentoGraph);
		Application.instance().getApplicationContext().publishEvent(event);
	}

}
