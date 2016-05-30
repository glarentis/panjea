package it.eurotn.panjea.partite.rich.tabelle.codicepagamento;

import it.eurotn.panjea.partite.domain.RigaStrutturaPartite;
import it.eurotn.panjea.partite.domain.StrutturaPartita;
import it.eurotn.panjea.partite.domain.StrutturaPartitaLite;
import it.eurotn.panjea.partite.rich.bd.IPartiteBD;
import it.eurotn.rich.dialog.PanjeaListSelectionDialog;
import it.eurotn.rich.editors.AbstractTreeTableDialogPageEditor;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.MutableTreeTableNode;
import org.jdesktop.swingx.treetable.TreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableNode;
import org.springframework.binding.value.ValueModel;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.selection.dialog.ListSelectionDialog;
import org.springframework.richclient.util.RcpSupport;

public class RigheStrutturaPartiteTablePage extends AbstractTreeTableDialogPageEditor {

	public class AggiungiCommand extends ActionCommand {

		private static final String COMMAND_ID = "aggiungiCommand";

		/**
		 * Costruttore.
		 */
		public AggiungiCommand() {
			super(COMMAND_ID);
			CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
					CommandConfigurer.class);
			this.setSecurityControllerId("codicePagamentoPage.controller");
			c.configure(this);
		}

		@Override
		public void doExecuteCommand() {
			logger.debug("--> Enter execute");
			List<StrutturaPartitaLite> list = partiteBD.caricaStrutturePartita();
			String titolo = getMessage("righeStrutturePartite.selectDialog.title");
			String messaggio = getMessage("righeStrutturePartite.selectDialog.message");
			ListSelectionDialog listSelectionDialog = new PanjeaListSelectionDialog(titolo, (Window) null, list) {

				@SuppressWarnings("unchecked")
				@Override
				protected void onSelect(Object selection) {
					logger.debug("--> Struttura selezionata " + selection);
					DefaultTreeTableModel model = (DefaultTreeTableModel) RigheStrutturaPartiteTablePage.this
							.getTreeTable().getTreeTableModel();
					// ottengo una strutturaPartitalite e devo caricare la strutturaPartite-FULL
					StrutturaPartitaLite strutturaPartitaLite = (StrutturaPartitaLite) selection;
					StrutturaPartita strutturaPartitaFull = partiteBD.caricaStrutturaPartita(strutturaPartitaLite
							.getId());

					DefaultMutableTreeTableNode nodo = new DefaultMutableTreeTableNode(strutturaPartitaFull);
					if (strutturaPartitaFull.getRigheStrutturaPartita() != null) {
						for (RigaStrutturaPartite rigaStrutturaPartite : strutturaPartitaFull
								.getRigheStrutturaPartita()) {
							DefaultMutableTreeTableNode child = new DefaultMutableTreeTableNode(rigaStrutturaPartite);
							nodo.add(child);
						}
					}
					model.insertNodeInto(nodo, (MutableTreeTableNode) model.getRoot(), model.getRoot().getChildCount());
					if (model.getRoot().getChildCount() == 1) {
						model.setRoot(model.getRoot());
					}
					List<StrutturaPartita> strutture = new ArrayList<StrutturaPartita>();
					List<StrutturaPartita> strutturePresenti = (List<StrutturaPartita>) RigheStrutturaPartiteTablePage.this.valueModelStrutturePartite
							.getValue();
					strutture.addAll(strutturePresenti);
					strutture.add(strutturaPartitaFull);
					RigheStrutturaPartiteTablePage.this.valueModelStrutturePartite.setValue(strutture);
				}

			};
			listSelectionDialog.setRenderer(new DefaultListCellRenderer() {
				/**
				 * Comment for <code>serialVersionUID</code>
				 */
				private static final long serialVersionUID = -6600465552132434943L;

				@Override
				public Component getListCellRendererComponent(JList arg0, Object arg1, int arg2, boolean arg3,
						boolean arg4) {
					JLabel label = (JLabel) super.getListCellRendererComponent(arg0, arg1, arg2, arg3, arg4);
					label.setText(((StrutturaPartitaLite) arg1).getDescrizione());
					return label;
				}
			});

			listSelectionDialog.setDescription(messaggio);
			listSelectionDialog.showDialog();
			logger.debug("--> Exit execute");
		}

	}

	public class EliminaCommand extends ActionCommand {

		private static final String COMMAND_ID = "deleteCommand";

		/**
		 * Costruttore.
		 */
		public EliminaCommand() {
			super(COMMAND_ID);
			CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
					CommandConfigurer.class);
			this.setSecurityControllerId("codicePagamentoPage.controller");
			c.configure(this);
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void doExecuteCommand() {
			DefaultTreeTableModel model = (DefaultTreeTableModel) RigheStrutturaPartiteTablePage.this.getTreeTable()
					.getTreeTableModel();
			DefaultMutableTreeTableNode nodo = getSelectedNode();

			if (nodo != null) {
				model.removeNodeFromParent(nodo);
				List<StrutturaPartita> strutturePresenti = (List<StrutturaPartita>) RigheStrutturaPartiteTablePage.this.valueModelStrutturePartite
						.getValue();
				strutturePresenti.remove(nodo.getUserObject());
				RigheStrutturaPartiteTablePage.this.valueModelStrutturePartite.setValue(RcpSupport
						.getClone(strutturePresenti));
			}
		}

	}

	protected class StrutturaPartitaRenderer extends DefaultTreeCellRenderer {

		private static final long serialVersionUID = -7137676555104279261L;

		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
				boolean leaf, int row, boolean hasFocus) {
			JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row,
					hasFocus);

			DefaultMutableTreeTableNode node = (DefaultMutableTreeTableNode) value;
			if (node.getUserObject() instanceof StrutturaPartita) {
				StrutturaPartita strutturaPartita = (StrutturaPartita) node.getUserObject();
				label.setText(strutturaPartita.getDescrizione());
				label.setIcon(RcpSupport.getIcon(StrutturaPartita.class.getName()));
			}
			if (node.getUserObject() instanceof RigaStrutturaPartite) {
				RigaStrutturaPartite rigaStrutturaPartite = (RigaStrutturaPartite) node.getUserObject();
				label.setText(rigaStrutturaPartite.getNumeroRata() + "");
				label.setIcon(RcpSupport.getIcon(RigaStrutturaPartite.class.getName()));
			}
			return label;
		}
	}

	/**
	 * TreeTableModel dove vengono descritti: il numero delle colonne: i tipi per ogni colonna e la proprieta'da
	 * visualizzare per ogni colonna.
	 * 
	 * @author Leonardo
	 */
	protected class StrutturaPartitaTableModel extends DefaultTreeTableModel {

		/**
		 * Costruttore.
		 * 
		 * @param node
		 *            root node
		 */
		public StrutturaPartitaTableModel(final TreeTableNode node) {
			super(node);
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		@Override
		public Class getColumnClass(int column) {
			switch (column) {
			case 0:
				return TreeTableModel.class; // descrizione struttura
			case 1:
				return String.class;
			default:
				throw new UnsupportedOperationException("Operazione non supportata");
			}
		}

		@Override
		public int getColumnCount() {
			return 2;
		}

		@Override
		public String getColumnName(final int column) {
			return getMessage("righeStrutturaPartitaTableColumn" + column);
		}

		@Override
		public Object getValueAt(Object node, int column) {
			Object object = ((DefaultMutableTreeTableNode) node).getUserObject();
			switch (column) {
			case 0:
				return super.getValueAt(node, column);
			case 1:
				if (object instanceof StrutturaPartita) {
					return "";
				}
				if (object instanceof RigaStrutturaPartite) {
					RigaStrutturaPartite rigaStrutturaPartite = (RigaStrutturaPartite) object;
					if ((rigaStrutturaPartite != null && rigaStrutturaPartite.getIntervallo() == null)) {
						return "Non impostato";
					}
					return rigaStrutturaPartite.getIntervallo().toString();
				}
				return object;
			default:
				throw new UnsupportedOperationException("Operazione non supportata");
			}
		}

		@Override
		public boolean isCellEditable(Object arg0, int arg1) {
			return false;
		}

	}

	private static final String PAGE_ID = "righeStrutturaPartiteTablePage";

	private AggiungiCommand aggiungiCommand = null;
	private EliminaCommand eliminaCommand = null;
	private IPartiteBD partiteBD = null;
	private ValueModel valueModelStrutturePartite = null;

	/**
	 * Costruttore.
	 * 
	 * @param partiteBD
	 *            partiteBD
	 * @param valueModelStrutturePartite
	 *            value model con la lista di struttura partite
	 */
	public RigheStrutturaPartiteTablePage(final ValueModel valueModelStrutturePartite, final IPartiteBD partiteBD) {
		super(PAGE_ID);
		this.partiteBD = partiteBD;
		this.valueModelStrutturePartite = valueModelStrutturePartite;
	}

	@Override
	protected void configureTreeTable(final JXTreeTable treeTableParam) {
		super.configureTreeTable(treeTableParam);
		treeTableParam.setSize(new Dimension(100, 100));
		treeTableParam.setMaximumSize(new Dimension(100, 100));
		treeTableParam.setMinimumSize(new Dimension(100, 100));
	}

	/**
	 * Crea il tree delle strutture partite.<br/>
	 * StrutturaPartita - RigaStrutturaPartite
	 * 
	 * @param strutturePartita
	 *            lista da cui creare il tree
	 * @return TreeTableNode
	 */
	private TreeTableNode createTreeNode(List<StrutturaPartita> strutturePartita) {
		DefaultMutableTreeTableNode root = new DefaultMutableTreeTableNode();
		for (StrutturaPartita strutturaPartita : strutturePartita) {
			DefaultMutableTreeTableNode nodo = new DefaultMutableTreeTableNode(strutturaPartita);
			root.add(nodo);
			for (RigaStrutturaPartite rigaStrutturaPartite : strutturaPartita.getRigheStrutturaPartita()) {
				DefaultMutableTreeTableNode child = new DefaultMutableTreeTableNode(rigaStrutturaPartite);
				nodo.add(child);
			}
		}
		return root;
	}

	@Override
	protected TreeTableModel createTreeTableModel() {
		return new StrutturaPartitaTableModel(null);
	}

	/**
	 * Aggiunge al value model la strutturapartite selezionata.
	 * 
	 * @return AggiungiCommand
	 */
	public AbstractCommand getAggiungiCommand() {
		if (aggiungiCommand == null) {
			aggiungiCommand = new AggiungiCommand();
		}
		return aggiungiCommand;
	}

	@Override
	public AbstractCommand[] getCommand() {
		AbstractCommand[] abstractCommands = new AbstractCommand[] { getAggiungiCommand(), getEliminaCommand(),
				getExpandCommand() };
		return abstractCommands;
	}

	/**
	 * Rimuove dal value model la strutturapartite selezionata.
	 * 
	 * @return EliminaCommand
	 */
	public AbstractCommand getEliminaCommand() {
		if (eliminaCommand == null) {
			eliminaCommand = new EliminaCommand();
		}
		return eliminaCommand;
	}

	@Override
	protected TreeCellRenderer getTreeCellRender() {
		return new StrutturaPartitaRenderer();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void loadData() {
		List<StrutturaPartita> struttureCodicePagamento = (List<StrutturaPartita>) (valueModelStrutturePartite
				.getValue());
		org.springframework.util.Assert.notNull(valueModelStrutturePartite, "Codice pagamento nullo");
		setTableData(createTreeNode(struttureCodicePagamento));
		getExpandCommand().setEnabled(struttureCodicePagamento.size() > 0);
		getDeleteCommand().setEnabled(struttureCodicePagamento.size() > 0);
	}

	@Override
	public void onPostPageOpen() {

	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	protected void openSelectedNode(DefaultMutableTreeTableNode node) {

	}

	@Override
	public void refreshData() {
		loadData();
	}

	@Override
	protected void selectionChanged(DefaultMutableTreeTableNode node) {
		if (aggiungiCommand.isEnabled()) {
			getEliminaCommand().setEnabled(!node.isLeaf());
		}
	}

	@Override
	public void setFormObject(Object object) {

	}

}
