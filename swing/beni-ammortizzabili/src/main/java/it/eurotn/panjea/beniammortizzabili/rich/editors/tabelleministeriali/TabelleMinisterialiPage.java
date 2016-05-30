/**
 *
 */
package it.eurotn.panjea.beniammortizzabili.rich.editors.tabelleministeriali;

import it.eurotn.panjea.beniammortizzabili.rich.bd.IBeniAmmortizzabiliBD;
import it.eurotn.panjea.beniammortizzabili2.domain.Gruppo;
import it.eurotn.panjea.beniammortizzabili2.domain.SottoSpecie;
import it.eurotn.panjea.beniammortizzabili2.domain.SottocontiBeni;
import it.eurotn.panjea.beniammortizzabili2.domain.Specie;
import it.eurotn.rich.command.JECCommandGroup;
import it.eurotn.rich.editors.AbstractTreeTableDialogPageEditor;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableNode;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.dialog.CloseAction;
import org.springframework.richclient.image.IconSource;
import org.springframework.richclient.list.ComboBoxListModel;
import org.springframework.richclient.util.GuiStandardUtils;
import org.springframework.richclient.util.PopupMenuMouseListener;

import com.jidesoft.converter.ObjectConverterManager;

/**
 * Pagina che gestisce la visualizzazione e gestione di gruppi, specie e sottospecie.
 *
 * @author Leonardo
 * @version 1.0, 18/ott/07
 */
public class TabelleMinisterialiPage extends AbstractTreeTableDialogPageEditor {

	/**
	 * @author
	 */
	private class CancellaSottoSpecieCommand extends ActionCommand {

		/**
		 * Costruttore.
		 *
		 */
		public CancellaSottoSpecieCommand() {
			super(pageId + CANCELLA_SOTTOSPECIE_COMMAND_ID);
			setVisible(false);
		}

		@Override
		protected void doExecuteCommand() {
			if (getTreeTable().getTreeSelectionModel().getSelectionPath() == null) {
				return;
			} else {
				final TreePath selPath = getTreeTable().getTreeSelectionModel().getSelectionPath();
				DefaultMutableTreeTableNode node = (DefaultMutableTreeTableNode) selPath.getLastPathComponent();

				beniAmmortizzabiliBD.cancellaSottoSpecie((SottoSpecie) node.getUserObject());

				node.removeFromParent();
				DefaultMutableTreeTableNode root = (DefaultMutableTreeTableNode) getTreeTable().getTreeTableModel()
						.getRoot();
				updateTreeModel(root);

				int idx = comboBoxGruppi.getSelectedIndex();
				comboBoxGruppi.setSelectedIndex(-1);
				comboBoxGruppi.setSelectedIndex(idx);
			}
		}
	}

	/**
	 * @author
	 */
	private class CancellaSpecieCommand extends ActionCommand {

		/**
		 * Costruttore.
		 *
		 */
		public CancellaSpecieCommand() {
			super(pageId + CANCELLA_SPECIE_COMMAND_ID);
			setVisible(false);
		}

		@Override
		protected void doExecuteCommand() {
			if (getTreeTable().getTreeSelectionModel().getSelectionPath() == null) {
				return;
			} else {
				final TreePath selPath = getTreeTable().getTreeSelectionModel().getSelectionPath();
				DefaultMutableTreeTableNode node = (DefaultMutableTreeTableNode) selPath.getLastPathComponent();
				Specie specieDaEliminare = (Specie) node.getUserObject();
				beniAmmortizzabiliBD.cancellaSpecie(specieDaEliminare);

				node.removeFromParent();
				DefaultMutableTreeTableNode root = (DefaultMutableTreeTableNode) getTreeTable().getTreeTableModel()
						.getRoot();
				updateTreeModel(root);

				int idx = comboBoxGruppi.getSelectedIndex();
				comboBoxGruppi.setSelectedIndex(-1);
				comboBoxGruppi.setSelectedIndex(idx);
			}
		}
	}

	private class ComboBoxGruppiListCellRenderer extends DefaultListCellRenderer {

		private static final long serialVersionUID = 4511960382568121548L;

		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			if (value instanceof Gruppo) {
				IconSource iconSource = (IconSource) ApplicationServicesLocator.services().getService(IconSource.class);
				label.setIcon(iconSource.getIcon(((Gruppo) value).getClass().getName()));
				label.setText(((Gruppo) value).getCodice() + " - " + ((Gruppo) value).getDescrizione());
			}
			return label;
		}
	}

	/**
	 * @author
	 */
	private class NuovaSottoSpecieCommand extends ActionCommand {

		/**
		 * Costruttore.
		 *
		 */
		public NuovaSottoSpecieCommand() {
			super(pageId + NUOVA_SOTTOSPECIE_COMMAND_ID);
		}

		@Override
		protected void doExecuteCommand() {
			if (getTreeTable().getTreeSelectionModel().getSelectionPath() == null) {
				return;
			} else {
				final TreePath selPath = getTreeTable().getTreeSelectionModel().getSelectionPath();
				DefaultMutableTreeTableNode node = (DefaultMutableTreeTableNode) selPath.getLastPathComponent();

				SottoSpecie sottoSpecie = new SottoSpecie();
				sottoSpecie.setSpecie((Specie) node.getUserObject());
				SottoSpecieTitledPageApplicationDialog dialog = new SottoSpecieTitledPageApplicationDialog(sottoSpecie);
				dialog.setCloseAction(CloseAction.HIDE);
				dialog.setModal(true);
				dialog.showDialog();

				if (dialog.getSottoSpecieSalvata().getId() != null) {
					DefaultMutableTreeTableNode root = (DefaultMutableTreeTableNode) getTreeTable().getTreeTableModel()
							.getRoot();
					node.add(new DefaultMutableTreeTableNode(dialog.getSottoSpecieSalvata()));

					updateTreeModel(root);
					dialog = null;
				}
			}
		}
	}

	/**
	 * @author
	 */
	private class NuovaSpecieCommand extends ActionCommand {

		/**
		 * Costruttore.
		 *
		 */
		public NuovaSpecieCommand() {
			super(pageId + NUOVA_SPECIE_COMMAND_ID);
		}

		@Override
		protected void doExecuteCommand() {
			Specie specie = new Specie();
			specie.setGruppo((Gruppo) comboBoxGruppi.getSelectedItem());
			SpecieTitledPageApplicationDialog dialog = new SpecieTitledPageApplicationDialog(specie);
			dialog.setCloseAction(CloseAction.HIDE);
			dialog.setModal(true);
			dialog.showDialog();

			if (dialog.getSpecieSalvata().getId() != null) {
				DefaultMutableTreeTableNode root = (DefaultMutableTreeTableNode) getTreeTable().getTreeTableModel()
						.getRoot();
				root.add(new DefaultMutableTreeTableNode(dialog.getSpecieSalvata()));

				updateTreeModel(root);
			}
		}
	}

	/**
	 * @author
	 */
	private class ProprietaSottoSpecieCommand extends ActionCommand {

		/**
		 * Costruttore.
		 *
		 */
		public ProprietaSottoSpecieCommand() {
			super(pageId + PROPRIETA_SOTTOSPECIE_COMMAND_ID);
			setVisible(false);
		}

		@Override
		protected void doExecuteCommand() {
			if (getTreeTable().getTreeSelectionModel().getSelectionPath() == null) {
				return;
			} else {
				final TreePath selPath = getTreeTable().getTreeSelectionModel().getSelectionPath();
				DefaultMutableTreeTableNode node = (DefaultMutableTreeTableNode) selPath.getLastPathComponent();

				if (node.getUserObject() instanceof SottoSpecie) {
					SottoSpecie sottoSpecie = (SottoSpecie) node.getUserObject();
					SottoSpecieTitledPageApplicationDialog dialog = new SottoSpecieTitledPageApplicationDialog(
							sottoSpecie);
					dialog.setModal(true);
					dialog.setCloseAction(CloseAction.HIDE);
					dialog.showDialog();

					node.setUserObject(dialog.getSottoSpecieSalvata());
					dialog = null;
				}
			}
		}
	}

	/**
	 * @author
	 */
	private class ProprietaSpecieCommand extends ActionCommand {

		/**
		 * Costruttore.
		 *
		 */
		public ProprietaSpecieCommand() {
			super(pageId + PROPRIETA_SPECIE_COMMAND_ID);
			setVisible(false);
		}

		@Override
		protected void doExecuteCommand() {
			if (getTreeTable().getTreeSelectionModel().getSelectionPath() == null) {
				return;
			} else {
				final TreePath selPath = getTreeTable().getTreeSelectionModel().getSelectionPath();
				DefaultMutableTreeTableNode node = (DefaultMutableTreeTableNode) selPath.getLastPathComponent();

				SpecieTitledPageApplicationDialog dialog = new SpecieTitledPageApplicationDialog(
						(Specie) node.getUserObject());
				dialog.setModal(true);
				dialog.setCloseAction(CloseAction.HIDE);
				dialog.showDialog();

				node.setUserObject(dialog.getSpecieSalvata());
				dialog = null;
			}
		}
	}

	private class SelectionListener implements ActionListener {

		private boolean actionEnable = true;

		@Override
		public void actionPerformed(ActionEvent e) {

			if (actionEnable) {
				Gruppo gruppo = (Gruppo) comboBoxGruppi.getSelectedItem();

				changeGruppoAzienda(gruppo);
			}
		}

		/**
		 * @param gruppo
		 *            gruppo da associare all'azienda
		 */
		private void changeGruppoAzienda(Gruppo gruppo) {

			actionEnable = false;

			// cambio il gruppo aziendale alla selezione
			beniAmmortizzabiliBD.settaGruppoAzienda(gruppo);

			comboBoxGruppi.setModel(new ComboBoxListModel(beniAmmortizzabiliBD.caricaGruppi("codice", null)));

			Gruppo gruppoAzienda = beniAmmortizzabiliBD.caricaGruppoAzienda();
			if (gruppoAzienda != null) {
				comboBoxGruppi.setSelectedItem(gruppoAzienda);
			}

			List<Specie> listSpecie = beniAmmortizzabiliBD.caricaSpecie(gruppo);
			DefaultMutableTreeTableNode rootNode = createTreeNode(listSpecie);
			updateTreeModel(rootNode);

			actionEnable = true;
		}
	}

	/**
	 * @author
	 */
	private class SottospecieTableModel extends DefaultTreeTableModel {

		/**
		 * Costruttore.
		 *
		 * @param root
		 *            root node
		 */
		public SottospecieTableModel(final TreeTableNode root) {
			super(root);
		}

		@Override
		public Class<?> getColumnClass(final int column) {
			if (column == 0) {
				return TreeTableModel.class;
			} else {
				return String.class;
			}
		}

		@Override
		public int getColumnCount() {
			return 7;
		}

		@Override
		public String getColumnName(final int column) {
			return getMessage("SottoSpecieColonna" + column);
		}

		@Override
		public Object getValueAt(final Object node, final int column) {

			if (((DefaultMutableTreeTableNode) node).getUserObject() == null) {
				return "";
			}

			Double percAmm = null;
			SottocontiBeni sottocontiBeni = null;
			if (((DefaultMutableTreeTableNode) node).getUserObject() instanceof SottoSpecie) {
				sottocontiBeni = ((SottoSpecie) ((DefaultMutableTreeTableNode) node).getUserObject())
						.getSottocontiBeni();
				percAmm = ((SottoSpecie) ((DefaultMutableTreeTableNode) node).getUserObject())
						.getPercentualeAmmortamento();
			} else if (((DefaultMutableTreeTableNode) node).getUserObject() instanceof Specie) {
				sottocontiBeni = ((Specie) ((DefaultMutableTreeTableNode) node).getUserObject()).getSottocontiBeni();
			}

			if (sottocontiBeni != null) {
				switch (column) {
				case 1:
					return percAmm;
				case 2:
					return ObjectConverterManager.toString(sottocontiBeni.getSottoContoImmobilizzazione());
				case 3:
					return ObjectConverterManager.toString(sottocontiBeni.getSottoContoAmmortamento());
				case 4:
					return ObjectConverterManager.toString(sottocontiBeni.getSottoContoFondoAmmortamento());
				case 5:
					return ObjectConverterManager.toString(sottocontiBeni.getSottoContoAmmortamentoAnticipato());
				case 6:
					return ObjectConverterManager.toString(sottocontiBeni.getSottoContoFondoAmmortamentoAnticipato());
				default:
					return "";
				}
			}
			return "";
		}

		/*
		 * @see org.jdesktop.swingx.treetable.DefaultTreeTableModel#isCellEditable(java.lang.Object, int)
		 */
		@Override
		public boolean isCellEditable(Object node, int column) {
			return false;
		}

		@Override
		public void setValueAt(final Object value, final Object node, final int column) {
		}
	}

	/**
	 * @author
	 */
	private class SottoSpecieTitledPageApplicationDialog extends PanjeaTitledPageApplicationDialog {

		private SottoSpecie sottoSpecieSalvata;

		/**
		 * Costruttore.
		 *
		 * @param sottoSpecie
		 *            sottoSpecie
		 */
		public SottoSpecieTitledPageApplicationDialog(final SottoSpecie sottoSpecie) {
			super(new SottoSpeciePage(sottoSpecie, beniAmmortizzabiliBD));
			sottoSpecieSalvata = (SottoSpecie) ((FormBackedDialogPageEditor) this.getDialogPage()).getForm()
					.getFormObject();
		}

		/**
		 * @return sottoSpecie salvata
		 */
		public SottoSpecie getSottoSpecieSalvata() {
			return sottoSpecieSalvata;
		}

		@Override
		protected String getTitle() {
			return getMessage("sottoSpecieTitledPageApplicationDialog.title");
		}

		@Override
		protected void onAboutToShow() {
			SottoSpeciePage sottoSpeciePage = (SottoSpeciePage) this.getDialogPage();
			sottoSpeciePage.getLockCommand().execute();
		}

		@Override
		protected void onCancel() {
			SottoSpeciePage sottoSpeciePage = (SottoSpeciePage) this.getDialogPage();
			sottoSpeciePage.getUndoCommand().execute();
			sottoSpecieSalvata = (SottoSpecie) sottoSpeciePage.getForm().getFormObject();
			super.onCancel();
		}

		@Override
		protected boolean onFinish() {
			SottoSpeciePage sottoSpeciePage = (SottoSpeciePage) this.getDialogPage();
			if (sottoSpeciePage.getForm().isDirty()) {
				sottoSpeciePage.onSave();
			}
			sottoSpecieSalvata = (SottoSpecie) sottoSpeciePage.getForm().getFormObject();
			return true;
		}
	}

	private class SottoSpecieTreeTablePopupMenuMouseListener extends PopupMenuMouseListener {

		@Override
		protected JPopupMenu getPopupMenu() {
			return TabelleMinisterialiPage.this.getPopupMenu();
		}
	}

	/**
	 * @author
	 */
	private class SpecieTitledPageApplicationDialog extends PanjeaTitledPageApplicationDialog {

		private Specie specieSalvata;

		/**
		 * Costruttore.
		 *
		 * @param specie
		 *            specie
		 */
		public SpecieTitledPageApplicationDialog(final Specie specie) {
			super(new SpeciePage(specie, beniAmmortizzabiliBD));
			specieSalvata = (Specie) ((FormBackedDialogPageEditor) this.getDialogPage()).getForm().getFormObject();
		}

		/**
		 * @return specie salvata
		 */
		public Specie getSpecieSalvata() {
			return specieSalvata;
		}

		@Override
		protected String getTitle() {
			return getMessage("specieTitledPageApplicationDialog.title");
		}

		@Override
		protected void onAboutToShow() {
			SpeciePage speciePage = (SpeciePage) this.getDialogPage();
			speciePage.getLockCommand().execute();
		}

		@Override
		protected void onCancel() {
			SpeciePage speciePage = (SpeciePage) this.getDialogPage();
			speciePage.getUndoCommand().execute();
			specieSalvata = (Specie) speciePage.getForm().getFormObject();
			super.onCancel();
		}

		@Override
		protected boolean onFinish() {
			SpeciePage speciePage = (SpeciePage) this.getDialogPage();
			if (speciePage.getForm().isDirty()) {
				speciePage.onSave();
			}
			specieSalvata = (Specie) speciePage.getForm().getFormObject();
			return true;
		}
	}

	private static Logger logger = Logger.getLogger(TabelleMinisterialiPage.class);
	private static final String NUOVA_SPECIE_COMMAND_ID = ".nuovaSpecieCommand";
	private static final String CANCELLA_SPECIE_COMMAND_ID = ".cancellaSpecieCommand";
	private static final String PROPRIETA_SPECIE_COMMAND_ID = ".proprietaSpecieCommand";
	private NuovaSpecieCommand nuovaSpecieCommand = null;
	private CancellaSpecieCommand cancellaSpecieCommand = null;

	private ProprietaSpecieCommand proprietaSpecieCommand = null;

	private static final String NUOVA_SOTTOSPECIE_COMMAND_ID = ".nuovaSottoSpecieCommand";

	private static final String CANCELLA_SOTTOSPECIE_COMMAND_ID = ".cancellaSottoSpecieCommand";

	private static final String PROPRIETA_SOTTOSPECIE_COMMAND_ID = ".proprietaSottoSpecieCommand";

	private NuovaSottoSpecieCommand nuovaSottoSpecieCommand = null;

	private CancellaSottoSpecieCommand cancellaSottoSpecieCommand = null;

	private ProprietaSottoSpecieCommand proprietaSottoSpecieCommand = null;

	private JPopupMenu popUpMenu;

	private JECCommandGroup nuovoCommandGroup;

	private final IBeniAmmortizzabiliBD beniAmmortizzabiliBD;

	private String pageId = "";

	private JComboBox comboBoxGruppi;

	/**
	 * Costruttore.
	 *
	 * @param pageId
	 *            id della pagina
	 * @param beniAmmortizzabiliBD
	 *            beniAmmortizzabiliBD
	 */
	public TabelleMinisterialiPage(final String pageId, final IBeniAmmortizzabiliBD beniAmmortizzabiliBD) {
		super(pageId);
		setPageId(pageId);
		this.beniAmmortizzabiliBD = beniAmmortizzabiliBD;
		nuovaSpecieCommand = new NuovaSpecieCommand();
		nuovaSottoSpecieCommand = new NuovaSottoSpecieCommand();
		cancellaSpecieCommand = new CancellaSpecieCommand();
		proprietaSpecieCommand = new ProprietaSpecieCommand();
		cancellaSottoSpecieCommand = new CancellaSottoSpecieCommand();
		proprietaSottoSpecieCommand = new ProprietaSottoSpecieCommand();
	}

	@Override
	protected void configureTreeTable(JXTreeTable treeTable) {
		treeTable.addMouseListener(new SottoSpecieTreeTablePopupMenuMouseListener());

		if (comboBoxGruppi.getSelectedItem() != null) {
			List<Specie> listSpecie = beniAmmortizzabiliBD.caricaSpecie((Gruppo) comboBoxGruppi.getSelectedItem());
			DefaultMutableTreeTableNode rootNode = createTreeNode(listSpecie);
			// JList list = new JList(beniAmmortizzabiliBD.caricaSpecie(gruppo).toArray());
			updateTreeModel(rootNode);
			// Seleziono il promo
			if (listSpecie.size() > 0) {
				treeTable.getSelectionModel().setSelectionInterval(0, 0);
			}
		}
	}

	/**
	 * Crea i controlli per la combo della selezione del gruppo.
	 *
	 * @return JPanel contenente la combo box gruppi
	 */
	private JPanel createPanelGruppiControl() {
		JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout());
		comboBoxGruppi = getComponentFactory().createComboBox();
		comboBoxGruppi.setRenderer(new ComboBoxGruppiListCellRenderer());
		comboBoxGruppi.addActionListener(new SelectionListener());
		rootPanel.add(comboBoxGruppi, BorderLayout.NORTH);
		GuiStandardUtils.attachDialogBorder(rootPanel);
		return rootPanel;
	}

	/**
	 * @param listSpecie
	 *            lista delle specie
	 * @return node
	 */
	private DefaultMutableTreeTableNode createTreeNode(List<Specie> listSpecie) {
		// Creo la treeView
		final DefaultMutableTreeTableNode rootNode = new DefaultMutableTreeTableNode();

		DefaultMutableTreeTableNode sottoSpecieNode = new DefaultMutableTreeTableNode();

		Integer oldCodice = null;
		for (final Specie specie : listSpecie) {
			DefaultMutableTreeTableNode node = null;

			if (!specie.getId().equals(oldCodice)) {
				// Inserisco il nodo padre
				sottoSpecieNode = new DefaultMutableTreeTableNode(specie);
				rootNode.add(sottoSpecieNode);
				oldCodice = specie.getId();
			}

			for (SottoSpecie sottoSpecie : specie.getSottoSpecie()) {
				// devo aggiornare la sottospecie per caricare i dati transient
				// sottoSpecie = beniAmmortizzabiliBD.caricaSottoSpecie(sottoSpecie.getId());

				node = new DefaultMutableTreeTableNode(sottoSpecie);
				sottoSpecieNode.add(node);
			}
		}
		return rootNode;
	}

	@Override
	protected TreeTableModel createTreeTableModel() {
		return new SottospecieTableModel(createTreeNode(new ArrayList<Specie>()));
	}

	@Override
	public AbstractCommand[] getCommand() {
		AbstractCommand[] abstractCommands = new AbstractCommand[] { getExpandCommand() };
		return abstractCommands;
	}

	@Override
	public JComponent getHeadControl() {
		return createPanelGruppiControl();
	}

	/**
	 * @return PopupMenu
	 */
	private JPopupMenu getPopupMenu() {
		if (popUpMenu == null) {
			JECCommandGroup commandGroup;
			final CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
					CommandConfigurer.class);
			commandGroup = new JECCommandGroup("toolbarSottoSpeciePage");
			nuovoCommandGroup = new JECCommandGroup(pageId + ".nuovoCommand");
			c.configure(nuovoCommandGroup);

			nuovoCommandGroup.add(c.configure(nuovaSpecieCommand));
			nuovoCommandGroup.add(c.configure(nuovaSottoSpecieCommand));
			commandGroup.add(nuovoCommandGroup);

			commandGroup.add(c.configure(cancellaSpecieCommand));
			commandGroup.add(c.configure(proprietaSpecieCommand));

			commandGroup.add(c.configure(cancellaSottoSpecieCommand));
			commandGroup.add(c.configure(proprietaSottoSpecieCommand));
			popUpMenu = commandGroup.createPopupMenu();

			updateCommand(null);
		}
		return popUpMenu;
	}

	@Override
	protected TreeCellRenderer getTreeCellRender() {
		return new DefaultTreeCellRenderer() {

			private static final long serialVersionUID = -8813682511205137384L;

			@Override
			public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
					boolean leaf, int row, boolean hasFocus) {
				IconSource iconSource = (IconSource) ApplicationServicesLocator.services().getService(IconSource.class);
				JLabel c = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
				DefaultMutableTreeTableNode node = (DefaultMutableTreeTableNode) value;
				// setto l'icona
				if (node.getUserObject() != null) {
					c.setIcon(iconSource.getIcon(node.getUserObject().getClass().getName()));
				}

				if (node.getUserObject() instanceof Specie) {
					c.setText(((Specie) node.getUserObject()).getDescrizione());
				} else {
					if (node.getUserObject() != null) {
						c.setText(((SottoSpecie) node.getUserObject()).getDescrizione());
					}
				}
				return c;
			}
		};
	}

	@Override
	public void loadData() {
		comboBoxGruppi.setModel(new ComboBoxListModel(beniAmmortizzabiliBD.caricaGruppi("codice", null)));
		Gruppo gruppoAzienda = beniAmmortizzabiliBD.caricaGruppoAzienda();
		if (gruppoAzienda != null) {
			comboBoxGruppi.setSelectedItem(gruppoAzienda);
		}
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
		logger.debug("--> nodo selezionato " + node);
		if (!node.isLeaf()) {
			logger.debug("--> Selezionata la specie");
			proprietaSpecieCommand.execute();
		} else {
			logger.debug("--> Selezionata la sottospecie");
			proprietaSottoSpecieCommand.execute();
		}
	}

	@Override
	public void postSetFormObject(Object object) {
	}

	@Override
	public void preSetFormObject(Object object) {
	}

	@Override
	public void refreshData() {
		loadData();
	}

	@Override
	protected void selectionChanged(DefaultMutableTreeTableNode node) {
		if (node != null) {
			if (node.getUserObject().getClass().getName().equals(Specie.class.getName())) {
				logger.debug("--> Selezionata la specie");
				updateCommand(node.getUserObject());
			} else {
				logger.debug("--> Selezionata la sottospecie");
				updateCommand(node.getUserObject());
			}
		} else {
			updateCommand(null);
		}
	}

	@Override
	public void setFormObject(Object object) {
	}

	/**
	 * @param pageid
	 *            id della pagina
	 */
	public void setPageId(String pageid) {
		pageId = pageid;
	}

	/**
	 * Aggiorna tutti i comandi della pagina.
	 *
	 * @param object
	 *            oggetto di riferimento
	 */
	private void updateCommand(Object object) {
		if (getPopupMenu() == null) {
			return;
		}
		nuovoCommandGroup.setVisible(false);
		cancellaSpecieCommand.setVisible(false);
		proprietaSpecieCommand.setVisible(false);
		cancellaSottoSpecieCommand.setVisible(false);
		proprietaSottoSpecieCommand.setVisible(false);

		nuovaSpecieCommand.setEnabled(true);
		nuovaSottoSpecieCommand.setEnabled(true);

		if (comboBoxGruppi.getItemCount() == 0) {
			nuovoCommandGroup.setVisible(true);
			nuovaSpecieCommand.setEnabled(false);
			nuovaSottoSpecieCommand.setEnabled(false);
			return;
		}
		if (object instanceof Specie) {
			nuovoCommandGroup.setVisible(true);
			cancellaSpecieCommand.setVisible(true);
			proprietaSpecieCommand.setVisible(true);
		} else if (object instanceof SottoSpecie) {
			cancellaSottoSpecieCommand.setVisible(true);
			proprietaSottoSpecieCommand.setVisible(true);
		} else {
			nuovoCommandGroup.setVisible(true);
		}
	}

	/**
	 * Aggiorna il modello associando il nuovo root node.
	 *
	 * @param rootNode
	 *            root node
	 */
	private void updateTreeModel(DefaultMutableTreeTableNode rootNode) {
		setTableData(rootNode);
		if (getTreeTable().getRowCount() > 0) {
			getExpandCommand().setEnabled(true);
		} else {
			getExpandCommand().setEnabled(false);
		}
	}

}
