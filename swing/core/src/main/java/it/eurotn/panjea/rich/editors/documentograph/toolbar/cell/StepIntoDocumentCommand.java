package it.eurotn.panjea.rich.editors.documentograph.toolbar.cell;

import it.eurotn.panjea.documenti.graph.node.DocumentoNode;
import it.eurotn.panjea.documenti.graph.util.DocumentoGraph;
import it.eurotn.panjea.rich.editors.documentograph.toolbar.AbstractToolbarGraphCommand;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

public class StepIntoDocumentCommand extends AbstractToolbarGraphCommand {

	public static final String COMMAND_ID = "stepIntoDocumentCommand";

	/**
	 * Costruttore.
	 */
	public StepIntoDocumentCommand() {
		super(COMMAND_ID);
		RcpSupport.configure(this);
	}

	@Override
	protected void doExecuteCommand() {
		if (node.getIdDocumento() != null) {
			DocumentoGraph documentoGraph = new DocumentoGraph();
			documentoGraph.setIdDocumento(node.getIdDocumento());
			LifecycleApplicationEvent event = new OpenEditorEvent(documentoGraph);
			Application.instance().getApplicationContext().publishEvent(event);
		}
	}

	@Override
	protected void doUpdate(DocumentoNode node) {
		// Non faccio niente
		setVisible(node.getIdDocumento() != null);
	}

}
