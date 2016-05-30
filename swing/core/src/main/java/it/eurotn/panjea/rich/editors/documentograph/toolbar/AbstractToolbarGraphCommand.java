package it.eurotn.panjea.rich.editors.documentograph.toolbar;

import it.eurotn.panjea.documenti.graph.node.DocumentoNode;

import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;

import com.mxgraph.model.mxCell;

public abstract class AbstractToolbarGraphCommand extends ApplicationWindowAwareCommand {

	protected mxCell cell;
	protected DocumentoNode node;

	/**
	 * Costruttore.
	 * 
	 * @param commandId
	 *            id del comando
	 */
	public AbstractToolbarGraphCommand(final String commandId) {
		super(commandId);
	}

	/**
	 * Azione da eseguire per il nodo di riferimento.
	 * 
	 * @param node
	 *            nodo di riferimento
	 */
	protected abstract void doUpdate(DocumentoNode node);

	/**
	 * Indica se dopo l'esecuzione del comando debba essere rifatto il layout del grafico.
	 * 
	 * @return <code>false</code> di default
	 */
	public boolean isRelayoutGraph() {
		return false;
	}

	/**
	 * Aggiorna il command in base alla cella di riferimento.
	 * 
	 * @param paramCell
	 *            cella di riferimento
	 */
	public void update(mxCell paramCell) {
		this.cell = paramCell;
		this.node = (DocumentoNode) cell.getValue();
		doUpdate(node);
	}
}
