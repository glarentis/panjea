package it.eurotn.panjea.magazzino.rich.editors.articolo.componenti.cellcommand;

import it.eurotn.panjea.magazzino.domain.Articolo;

import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;

import com.mxgraph.model.mxCell;

@Deprecated
public abstract class AbstractToolbarGraphCommand extends ApplicationWindowAwareCommand {

	protected Articolo articolo;

	protected mxCell cell;
	protected Object nodeObject;

	public static final String PARAM_GRAPH_COMPONENT = "graphComponent";

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
	 * @param nodeObject
	 *            oggetto di riferimento
	 */
	protected abstract void doUpdate(Object nodeObject);

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
	 * @param paramArticolo
	 *            articolo di riferimento
	 */
	public void update(mxCell paramCell, Articolo paramArticolo) {
		this.cell = paramCell;
		this.articolo = paramArticolo;
		this.nodeObject = cell.getValue();
		doUpdate(nodeObject);
	}
}
