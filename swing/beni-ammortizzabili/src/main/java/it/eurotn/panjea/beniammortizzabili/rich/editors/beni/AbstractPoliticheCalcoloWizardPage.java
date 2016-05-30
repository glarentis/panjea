/**
 *
 */
package it.eurotn.panjea.beniammortizzabili.rich.editors.beni;

import it.eurotn.panjea.beniammortizzabili2.domain.PoliticaCalcolo;
import it.eurotn.panjea.beniammortizzabili2.domain.PoliticaCalcoloBene;
import it.eurotn.panjea.beniammortizzabili2.domain.Simulazione;
import it.eurotn.panjea.rich.factory.PanjeaComponentFactory;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Enumeration;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableModel;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.CommandGroup;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.command.config.CommandFaceDescriptor;
import org.springframework.richclient.dialog.CloseAction;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.settings.support.Memento;
import org.springframework.richclient.util.GuiStandardUtils;
import org.springframework.richclient.wizard.AbstractWizardPage;

/**
 * Classe base wizard per visualizzare le politiche di calcolo (fiscali/civilistiche) in una treeTable ediabile.
 *
 * @author Leonardo
 * @version 1.0, 20/ott/07
 *
 */
public abstract class AbstractPoliticheCalcoloWizardPage extends AbstractWizardPage implements Memento {

	/**
	 * Command che gestisce la funzione di expand/collapse sulla tree table.
	 *
	 * @author Leonardo
	 */
	private class ExpandCommand extends ActionCommand {

		private static final String ESPANDI_COMMAND = ".expandCommand";
		private static final String EXPAND_STATE = ".expand";
		private static final String COLLAPSE_STATE = ".collapse";
		private boolean expandTree;
		private CommandFaceDescriptor expandDescriptor;
		private CommandFaceDescriptor collapseDescriptor;

		/**
		 * Costruttore.
		 *
		 */
		public ExpandCommand() {
			super("abstractTreeTableDialogPageEditor" + ESPANDI_COMMAND);
			CommandConfigurer configurer = (CommandConfigurer) ApplicationServicesLocator.services().getService(
					CommandConfigurer.class);

			setSecurityControllerId("abstractTreeTableDialogPageEditor" + ESPANDI_COMMAND);
			configurer.configure(this);
			expandTree = true;

			String toExpandString = getMessage("abstractTreeTableDialogPageEditor" + EXPAND_STATE + ".label");
			String toCollapseString = getMessage("abstractTreeTableDialogPageEditor" + COLLAPSE_STATE + ".label");
			Icon toExpandIcon = getIconSource().getIcon("abstractTreeTableDialogPageEditor" + EXPAND_STATE + ".icon");
			Icon toCollapseIcon = getIconSource().getIcon(
					"abstractTreeTableDialogPageEditor" + COLLAPSE_STATE + ".icon");
			collapseDescriptor = new CommandFaceDescriptor(toExpandString, toExpandIcon, null);
			expandDescriptor = new CommandFaceDescriptor(toCollapseString, toCollapseIcon, null);
			setFaceDescriptor(collapseDescriptor);
		}

		@Override
		protected void doExecuteCommand() {
			if (!expandTree) {
				simulazioneTreeTable.expandAll();
				setFaceDescriptor(expandDescriptor);
			} else {
				simulazioneTreeTable.collapseAll();
				setFaceDescriptor(collapseDescriptor);
			}
			this.expandTree = !expandTree;
		}
	}

	/**
	 * Dialogo di richiesta per estendere a tutti i figli del nodo selezionato il cambio di valore.
	 *
	 * @author Leonardo
	 */
	private class ForceUpdateDialog extends ConfirmationDialog {

		private static final int XDIMENSION = 300;
		private static final int YDIMENSION = 100;

		private int forceUpdate = 0;

		/**
		 * Costruttore.
		 *
		 */
		public ForceUpdateDialog() {
			super();
			String title = getMessage("forceUpdateDialog.title");
			String message = getMessage("forceUpdateDialog.message");
			setTitle(title);
			setConfirmationMessage(message);
			setPreferredSize(new Dimension(XDIMENSION, YDIMENSION));
			setModal(true);
			setCloseAction(CloseAction.HIDE);
		}

		/**
		 * @return forceUpdate
		 */
		public int getForceUpdate() {
			return forceUpdate;
		}

		@Override
		protected void onCancel() {
			forceUpdate = 1;
			super.onCancel();
		}

		@Override
		protected void onConfirm() {
			forceUpdate = 2;

		}

	}

	/**
	 * Listener per ascoltare gli eventi di editing sulla cella; quando viene concluso l'edit e cambiato il valore della
	 * cella viene chiesto se estendere la modifica a tutti i figli.
	 *
	 * @author Leonardo
	 */
	private class MyCellEditorListener implements CellEditorListener {

		@Override
		public void editingCanceled(ChangeEvent e) {

		}

		/*
		 * @see javax.swing.event.CellEditorListener#editingStopped(javax.swing.event.ChangeEvent)
		 */
		@Override
		public void editingStopped(ChangeEvent e) {
			updateValueForNodeAndHisChildrens();

			azzeraTotali();
			simulazioneTreeTable.repaint();
		}
	}

	/**
	 * Sul tree viene visualizzata una icona di warning se la politica di calcolo viene impostata a dirty a seguito di
	 * una modifica.
	 *
	 * @author Leonardo
	 */
	public class MyTreeDirtyCellRenderer extends DefaultTreeCellRenderer {

		private static final long serialVersionUID = 2572619892391440400L;

		/**
		 * Costruttore.
		 *
		 */
		public MyTreeDirtyCellRenderer() {
		}

		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
				boolean leaf, int row, boolean hasFocus) {
			JLabel c = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
			DefaultMutableTreeTableNode node = (DefaultMutableTreeTableNode) value;

			PoliticaCalcolo politicaCalcolo = (PoliticaCalcolo) node.getUserObject();
			if (politicaCalcolo != null) {
				c.setText(politicaCalcolo.getDescrizioneEntitaPoliticaCalcolo());
				if (politicaCalcolo.isDirty()) {
					// setto l'icona "edit" se la politica di calcolo è sporca
					c.setIcon(getIconSource().getIcon("edit"));
				} else {
					// imposta l'icona associata alla classe PoliticaCalcolo
					c.setIcon(getIconSource().getIcon(politicaCalcolo.getClass().getName()));
				}
			}
			return c;
		}
	}

	private static Logger logger = Logger.getLogger(AbstractPoliticheCalcoloWizardPage.class);
	private JXTreeTable simulazioneTreeTable = null;
	private Simulazione simulazione = null;
	private MyCellEditorListener myCellEditorListener = null;

	private MyTreeDirtyCellRenderer myColorCellRenderer = null;

	private static final int XDIMENSION = 800;

	private static final int YDIMENSION = 400;
	private ExpandCommand expandCommand;

	/**
	 * Costruttore.
	 *
	 * @param pageId
	 *            id della pagina
	 * @param simulazione
	 *            simulazione
	 */
	public AbstractPoliticheCalcoloWizardPage(final String pageId, final Simulazione simulazione) {
		super(pageId);
		this.simulazione = simulazione;
	}

	/**
	 * Metodo da implementare per applicare le modifiche della colonna selezionata sulla politica di calcolo contenuta
	 * nel node.
	 *
	 * @param politicaCalcolo
	 *            la politica di calcolo sulla quale applicare la modifica
	 * @param selColumn
	 *            la colonna della tabella che identifica un certo attributo della politica di calcolo il cui valore
	 *            deve essere aggiornato; � diverso nei casi civilistico/fiscale
	 * @param value
	 *            il valore da usare per aggiornare il campo scelto
	 */
	protected abstract void applyValueChangedForColumn(PoliticaCalcolo politicaCalcolo, int selColumn, Object value);

	/**
	 * Azzera i totali delle tabelle, la funzione di calcola presenter� i nuovi totali.
	 */
	private void azzeraTotali() {
		for (PoliticaCalcolo politicaCalcolo : simulazione.getPoliticheCalcolo()) {
			politicaCalcolo.getPoliticaCalcoloCivilistica().setTotaleAnticipato(null);
			politicaCalcolo.getPoliticaCalcoloCivilistica().setTotaleOrdinario(null);
			politicaCalcolo.getPoliticaCalcoloFiscale().setTotaleAnticipato(null);
			politicaCalcolo.getPoliticaCalcoloFiscale().setTotaleOrdinario(null);
		}
	}

	/**
	 * Metodo per accedere alla treeTable permettendo di configurarla.
	 *
	 * @param treeTable
	 *            tree table
	 */
	protected void configureTreeTable(JXTreeTable treeTable) {

	}

	@Override
	protected JComponent createControl() {
		JPanel panel = getComponentFactory().createPanel(new BorderLayout());
		panel.setPreferredSize(new Dimension(XDIMENSION, YDIMENSION));
		panel.setLayout(new BorderLayout());
		// creo la tabella e la posiziono al centro del pannello
		JScrollPane scrollPane = getComponentFactory().createScrollPane(createSimulazioneTable());
		panel.add(scrollPane, BorderLayout.CENTER);

		JPanel buttonPanel = getComponentFactory().createPanel(new BorderLayout());
		buttonPanel.add(getButtonBar(), BorderLayout.LINE_END);
		panel.add(buttonPanel, BorderLayout.SOUTH);
		return panel;

	}

	/**
	 * Crea la tree table della simulazione.
	 *
	 * @return tree table creata
	 */
	protected JXTreeTable createSimulazioneTable() {
		logger.debug("--> Enter createTreeTable");

		if (simulazioneTreeTable == null) {
			simulazioneTreeTable = (JXTreeTable) ((PanjeaComponentFactory) getComponentFactory()).createTreeTable();

			// so dal sorgente della JXTable che � stata aggiunta una action find
			// che visualizza il dialog di ricerca; rimuovo questo dalla map di actions registrate
			// e aggiungo la mia action che dalla tabella porta il focus alla filterTextField.
			// lo shortcut ctrl + F � registrato a parte sulla action registrata con la chiave "find"
			// quindi non serve fare altro
			(simulazioneTreeTable).getActionMap().remove("find");
			// ((JXTreeTable)simulazioneTreeTable).getActionMap().put("find", new FindAction("find"));

			simulazioneTreeTable.setColumnControlVisible(true);
			simulazioneTreeTable.setAutoscrolls(true);
			simulazioneTreeTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

			// viene generato il rootNode e il relativo treeTableModel
			DefaultMutableTreeTableNode root = createTreeNode(simulazione);
			simulazioneTreeTable.setTreeTableModel(createTreeTableModel(root));

			// listener per fine editazione cella tabella
			simulazioneTreeTable.getDefaultEditor(Boolean.class).addCellEditorListener(getMyCellEditorListener());
			simulazioneTreeTable.getDefaultEditor(Double.class).addCellEditorListener(getMyCellEditorListener());

			// non permetto di riorganizzare l'ordine delle colonne
			simulazioneTreeTable.getTableHeader().setReorderingAllowed(false);

			// listener per visualizzare su tree le politiche dirty con una icona di warning al posto della
			// classica cartella
			simulazioneTreeTable.setTreeCellRenderer(getMyCellRenderer());
		}

		configureTreeTable(simulazioneTreeTable);

		return simulazioneTreeTable;
	}

	/**
	 * Il treeNode che definisce i dati da visualizzare nella tabella per riempire il model.
	 *
	 * @param paramSimulazione
	 *            simulazione
	 * @return DefaultMutableTreeTableNode
	 */
	public DefaultMutableTreeTableNode createTreeNode(Simulazione paramSimulazione) {
		DefaultMutableTreeTableNode rootNode = new DefaultMutableTreeTableNode();

		if (paramSimulazione == null || (paramSimulazione != null && paramSimulazione.getPoliticheCalcolo() == null)) {
			return rootNode;
		}

		DefaultMutableTreeTableNode nodeGruppo = null;
		DefaultMutableTreeTableNode nodeSpecie = null;
		DefaultMutableTreeTableNode nodeSottoSpecie = null;
		DefaultMutableTreeTableNode nodeBene = null;

		List<PoliticaCalcolo> politicheCalcolo = paramSimulazione.getPoliticheCalcolo();
		for (PoliticaCalcolo politicaCalcolo : politicheCalcolo) {
			int deep = politicaCalcolo.getDeep();
			switch (deep) {
			case PoliticaCalcolo.DEEP_GRUPPO:
				nodeGruppo = new DefaultMutableTreeTableNode(politicaCalcolo);
				rootNode.add(nodeGruppo);
				break;
			case PoliticaCalcolo.DEEP_SPECIE:
				nodeSpecie = new DefaultMutableTreeTableNode(politicaCalcolo);
				nodeGruppo.add(nodeSpecie);
				break;
			case PoliticaCalcolo.DEEP_SOTTOSPECIE:
				nodeSottoSpecie = new DefaultMutableTreeTableNode(politicaCalcolo);
				nodeSpecie.add(nodeSottoSpecie);
				break;
			case PoliticaCalcolo.DEEP_BENE:
				nodeBene = new DefaultMutableTreeTableNode(politicaCalcolo);
				nodeSottoSpecie.add(nodeBene);
				break;
			default:
			}
		}
		return rootNode;
	}

	/**
	 * Metodo da implementare per definire il TreeTableModel.
	 *
	 * @param rootNode
	 *            il root node che definisce il tree da associare al model per visualizzare la treetable
	 * @return TreeTableModel
	 */
	public abstract TreeTableModel createTreeTableModel(DefaultMutableTreeTableNode rootNode);

	/**
	 * Button bar dove vengono aggiunti i commands come ad esempio espandi, nuovo, salva, ecc. Per aggiungere commands
	 * nella button bar devo sovrascrivere sulla classe derivata getCommand()
	 *
	 * @return la button bar o null se getCommand ritorna null.
	 */
	public JComponent getButtonBar() {
		if (getCommand() != null) {
			JComponent panel = getCommandGroup().createButtonBar();
			panel.setBorder(GuiStandardUtils.createTopAndBottomBorder(3));
			return panel;
		} else {
			return null;
		}
	}

	/**
	 * I comandi che vengono aggiunti al command group verranno visualizzati come buttonBar sotto la tablella.
	 *
	 * @return commandGroup dei comandi
	 */
	public AbstractCommand[] getCommand() {
		AbstractCommand[] abstractCommands = new AbstractCommand[] { getExpandCommand() };

		return abstractCommands;
	}

	/**
	 * Se vengono aggiunti dei command viene creato il commandGroup da aggiungere alla button bar.
	 *
	 * @return CommandGroup
	 */
	private CommandGroup getCommandGroup() {
		CommandGroup commandGroup = new CommandGroup();
		if (getCommand() != null) {
			for (int i = 0; i < getCommand().length; i++) {
				commandGroup.add(getCommand()[i]);
			}
		} else {
			commandGroup = null;
		}
		return commandGroup;
	}

	/**
	 * @return expandCommand
	 */
	public ExpandCommand getExpandCommand() {
		if (expandCommand == null) {
			expandCommand = new ExpandCommand();
		}

		return expandCommand;
	}

	/**
	 *
	 * @return myCellEditorListener
	 */
	private MyCellEditorListener getMyCellEditorListener() {
		if (myCellEditorListener == null) {
			myCellEditorListener = new MyCellEditorListener();
		}
		return myCellEditorListener;
	}

	/**
	 * @return myColorCellRenderer
	 */
	private DefaultTreeCellRenderer getMyCellRenderer() {
		if (myColorCellRenderer == null) {
			myColorCellRenderer = new MyTreeDirtyCellRenderer();
		}
		return myColorCellRenderer;
	}

	/**
	 * @return the simulazione
	 */
	public Simulazione getSimulazione() {
		return simulazione;
	}

	/**
	 * @return tree table della simulazione
	 */
	public JXTreeTable getSimulazioneTreeTable() {
		return simulazioneTreeTable;
	}

	/**
	 * Metodo ricorsivo per applicare a tutti i figli il valore aggiornato sulla colonna editata.
	 *
	 * @param selColumn
	 *            la colonna editata
	 * @param value
	 *            il valore da applicare a tutti i figli ricorsivamente
	 * @param selectedNode
	 *            il nodo di partenza
	 */
	@SuppressWarnings("unchecked")
	private void updateChildren(int selColumn, Object value, DefaultMutableTreeTableNode selectedNode) {
		logger.debug("--> Enter updateChildren");
		Enumeration<DefaultMutableTreeTableNode> children = (Enumeration<DefaultMutableTreeTableNode>) selectedNode
				.children();
		while (children.hasMoreElements()) {
			DefaultMutableTreeTableNode node = children.nextElement();
			PoliticaCalcolo politicaCalcolo = (PoliticaCalcolo) node.getUserObject();

			applyValueChangedForColumn(politicaCalcolo, selColumn, value);
			politicaCalcolo.setDirty(true);

			logger.debug("--> politicaCalcolo aggiornata " + politicaCalcolo);
			logger.debug("--> politicaCalcolo dirty " + politicaCalcolo.isDirty());
			updateChildren(selColumn, value, node);
		}
		logger.debug("--> Exit updateChildren");
	}

	/**
	 * Metodo per risettare il modello dei dati per il treeTable da chiamare per aggiornare il tree.
	 *
	 * @param paramSimulazione
	 *            simulazione
	 */
	public void updateTableData(Simulazione paramSimulazione) {
		this.simulazione = paramSimulazione;
		((DefaultTreeTableModel) simulazioneTreeTable.getTreeTableModel()).setRoot(createTreeNode(paramSimulazione));
		simulazioneTreeTable.expandAll();
	}

	/**
	 * Recuperando la riga e colonna editata, chiede di estendere la modifica del valore a tutti i figli del nodo scelto
	 * (se presenti).
	 */
	private void updateValueForNodeAndHisChildrens() {
		logger.debug("--> Enter updateValueForNodeAndHisChildrens");
		int selRow = simulazioneTreeTable.getSelectedRow();
		int selCol = simulazioneTreeTable.getSelectedColumn();

		TreePath treePath = simulazioneTreeTable.getPathForRow(selRow);

		// il valore aggiornato della riga e colonna selezionata
		Object value = simulazioneTreeTable.getValueAt(selRow, selCol);

		// il nodo selezionato che comprende i nodi padri fino al rootNode
		DefaultMutableTreeTableNode selectedNode = (DefaultMutableTreeTableNode) treePath.getLastPathComponent();

		int forceUpadate = -1;
		// presento la finestra di modifica ricorsiva sui figli solo nel caso che il nodo
		// contiene una PoliticaCalcoloGruppo,PoliticaCalcoloSpecie,PoliticaCalcoloSottoSpecie
		if (!(selectedNode.getUserObject() instanceof PoliticaCalcoloBene)) {
			ForceUpdateDialog dialog = new ForceUpdateDialog();
			dialog.showDialog();
			forceUpadate = dialog.getForceUpdate();
		}

		// ho scelto di applicarte le modifiche ai figli
		if (forceUpadate == 2) {
			// devo assegnare ai figli di selectedNode lo stesso valore value per la stessa colonna selCol
			updateChildren(selCol, value, selectedNode);
		}
		logger.debug("--> Exit updateValueForNodeAndHisChildrens");
	}
}
