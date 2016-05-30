package it.eurotn.panjea.rich.editors.documentograph.toolbar.cell;

import it.eurotn.panjea.documenti.graph.node.DocumentoNode;
import it.eurotn.panjea.rich.editors.documentograph.toolbar.AbstractToolbarGraphCommand;

import javax.swing.Icon;

import org.springframework.richclient.command.config.CommandFaceDescriptor;
import org.springframework.richclient.util.RcpSupport;

public class ExpandCommand extends AbstractToolbarGraphCommand {

	public static final String COMMAND_ID = "searchContrattiCommand";

	private static final String EXPAND_STATE = "expand.icon";
	private static final String COLLAPSE_STATE = "collapse.icon";

	private CommandFaceDescriptor expandDescriptor;
	private CommandFaceDescriptor collapseDescriptor;

	/**
	 * Costruttore.
	 */
	public ExpandCommand() {
		super(COMMAND_ID);
		RcpSupport.configure(this);

		Icon toExpandIcon = RcpSupport.getIcon(EXPAND_STATE);
		Icon toCollapseIcon = RcpSupport.getIcon(COLLAPSE_STATE);
		collapseDescriptor = new CommandFaceDescriptor("", toExpandIcon, null);
		expandDescriptor = new CommandFaceDescriptor("", toCollapseIcon, null);
		setFaceDescriptor(collapseDescriptor);
	}

	@Override
	protected void doExecuteCommand() {
		node.setFullDescription(!node.isFullDescription());
		doUpdate(node);
	}

	@Override
	protected void doUpdate(DocumentoNode node) {
		setVisible(node.getIdDocumento() != null);
		if (node.isFullDescription()) {
			setFaceDescriptor(expandDescriptor);
		} else {
			setFaceDescriptor(collapseDescriptor);
		}
	}

	@Override
	public boolean isRelayoutGraph() {
		return true;
	}

}