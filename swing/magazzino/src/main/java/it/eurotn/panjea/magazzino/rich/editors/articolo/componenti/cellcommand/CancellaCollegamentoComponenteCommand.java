/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.editors.articolo.componenti.cellcommand;

import it.eurotn.panjea.magazzino.rich.editors.articolo.ComponentePM;
import it.eurotn.panjea.magazzino.rich.editors.articolo.componenti.ArticoloComponentiGraphComponent;

import org.springframework.richclient.command.config.CommandFaceDescriptor;
import org.springframework.richclient.util.RcpSupport;

/**
 * @author fattazzo
 * 
 */
@Deprecated
public class CancellaCollegamentoComponenteCommand extends AbstractToolbarGraphCommand {

	/**
	 * Costruttore.
	 * 
	 * @param commandId
	 */
	public CancellaCollegamentoComponenteCommand() {
		super("cancellaCollegamentoComponenteCommand");

		setFaceDescriptor(new CommandFaceDescriptor(null, RcpSupport.getIcon("deleteCommand.icon"),
				"Rimuovi il componente"));
	}

	@Override
	protected void doExecuteCommand() {
		ArticoloComponentiGraphComponent graphComponent = (ArticoloComponentiGraphComponent) getParameter(PARAM_GRAPH_COMPONENT);
		// graphComponent.removeComponent((ComponentePM) nodeObject);
	}

	@Override
	protected void doUpdate(Object nodeObject) {
		// solo se sono su un componente di primo livello dell'articolo di riferimento posso rimuoverlo
		setVisible(nodeObject instanceof ComponentePM
				&& ((ComponentePM) nodeObject).getComponente().getDistinta().equals(articolo));
	}

	@Override
	public boolean isRelayoutGraph() {
		return false;
	}

}
