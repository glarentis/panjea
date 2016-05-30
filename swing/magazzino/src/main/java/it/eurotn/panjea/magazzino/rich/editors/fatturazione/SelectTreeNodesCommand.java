package it.eurotn.panjea.magazzino.rich.editors.fatturazione;

import it.eurotn.panjea.magazzino.rich.editors.fatturazione.AreaMagazzinoLitePM.StatoRigaAreaMagazzinoLitePM;
import it.eurotn.rich.editors.AbstractTreeTableDialogPageEditor;

import java.util.Enumeration;

import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

/**
 * Seleziona o deseleziona tutti i nodi di un TreeTableModel.
 * 
 * @author fattazzo
 * 
 */
public class SelectTreeNodesCommand extends ActionCommand {

	private boolean selectNodes = true;

	private final AbstractTreeTableDialogPageEditor page;

	/**
	 * Costruttore.
	 * 
	 * @param commandId
	 *            id del comando
	 * @param selectNodes
	 *            nodi selezionati
	 * @param page
	 *            pagina
	 */
	public SelectTreeNodesCommand(final String commandId, final boolean selectNodes,
			final AbstractTreeTableDialogPageEditor page) {
		super(commandId);
		RcpSupport.configure(this);

		this.selectNodes = selectNodes;
		this.page = page;
	}

	@Override
	protected void doExecuteCommand() {
		selectAllNode((DefaultMutableTreeTableNode) page.getTreeTable().getTreeTableModel().getRoot());
	}

	/**
	 * Seleziona tutti i nodi ricorsivamente.
	 * 
	 * @param node
	 *            nodo
	 */
	private void selectAllNode(DefaultMutableTreeTableNode node) {
		if (node.getUserObject() instanceof AreaMagazzinoLitePM) {
			AreaMagazzinoLitePM areaMagazzinoLitePM = (AreaMagazzinoLitePM) node.getUserObject();
			if (areaMagazzinoLitePM.getStatoRigaAreaMagazzinoLitePM() == StatoRigaAreaMagazzinoLitePM.SELEZIONABILE) {
				areaMagazzinoLitePM.setSelected(this.selectNodes);
			}
		} else {
			Enumeration<?> children = node.children();
			while (children.hasMoreElements()) {
				DefaultMutableTreeTableNode childNode = (DefaultMutableTreeTableNode) children.nextElement();
				if (childNode.getUserObject() instanceof AreaMagazzinoLitePM) {
					AreaMagazzinoLitePM areaMagazzinoLitePM = (AreaMagazzinoLitePM) childNode.getUserObject();
					if (areaMagazzinoLitePM.getStatoRigaAreaMagazzinoLitePM() == StatoRigaAreaMagazzinoLitePM.SELEZIONABILE) {
						areaMagazzinoLitePM.setSelected(this.selectNodes);
					}
				} else {
					selectAllNode(childNode);
				}
			}
		}
		page.getTreeTable().repaint();
	}
}
