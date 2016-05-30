package it.eurotn.panjea.anagrafica.rich.search;

import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.anagrafica.domain.Anagrafica;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.lite.AnagraficaLite;
import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.anagrafica.domain.lite.ClientePotenzialeLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.domain.lite.FornitoreLite;
import it.eurotn.panjea.anagrafica.domain.lite.VettoreLite;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.commands.NewEntitaCommand;
import it.eurotn.panjea.anagrafica.util.parametriricerca.ParametriRicercaEntita;
import it.eurotn.panjea.rich.PanjeaLifecycleApplicationEvent;
import it.eurotn.panjea.rich.factory.PanjeaComponentFactory;
import it.eurotn.panjea.rich.pages.AbstractSearchResult;
import it.eurotn.rich.command.JECCommandGroup;
import it.eurotn.rich.settings.support.PanjeaTableMemento;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableNode;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.CommandGroup;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.command.config.CommandFaceDescriptor;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.image.IconSource;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.util.PopupMenuMouseListener;
import org.springframework.richclient.util.RcpSupport;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.GroupingList;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

public class SearchResultAnagrafica extends AbstractSearchResult<Anagrafica> implements InitializingBean,
		ApplicationListener {

	/**
	 * Aggiungo un listener per controllare la riga selezionare;<br>
	 * se ho un'anagrafica selezionata controllo se ho un cliente o un fornitore e abilito i pulsanti di conseguenza.
	 * 
	 * @author
	 */
	private class AnagraficaListSelectionListener implements TreeSelectionListener {
		@Override
		public void valueChanged(final TreeSelectionEvent e) {
			// TODO spostare all'interno dei command il caricamento degli
			// oggetti di dominio angrafica ed entita
			TreePath selPath = e.getPath();
			proprietaCommand.setEnabled(false);
			nuovoCommandGroup.setEnabled(false);
			getDeleteCommand().setEnabled(false);
			if (selPath == null) {
				logger.debug("--> Nulla di selezionato");
				return;
			}
			DefaultMutableTreeTableNode node = (DefaultMutableTreeTableNode) selPath.getLastPathComponent();
			if (node == null) {
				logger.debug("--> Nulla di selezionato");
				return;
			}
			SearchResultAnagrafica.this.enabledNewCommand(node);
			if (logger.isDebugEnabled()) {
				logger.debug("--> nodo selezionato " + node.getUserObject());
			}
			AnagraficaLite anagraficaLite = null;
			if (!node.isLeaf()) {
				logger.debug("--> Selezionata l'anagrafica");
				anagraficaLite = (AnagraficaLite) node.getUserObject();
				nuovoCommandGroup.setEnabled(true);
				SearchResultAnagrafica.this.getDeleteCommand().setEnabled(false);
				proprietaCommand.setEnabled(false);
			} else {
				logger.debug("--> Selezionata l'entità");
				EntitaLite entitaLite = (EntitaLite) node.getUserObject();
				proprietaCommand.setEntitaLite(entitaLite);
				proprietaCommand.setEnabled(true);
				SearchResultAnagrafica.this.getDeleteCommand().setEnabled(true);
				anagraficaLite = ((EntitaLite) node.getUserObject()).getAnagrafica();
				nuovoCommandGroup.setEnabled(false);
			}

			for (final NewEntitaCommand newCommand : commandMap.values()) {
				newCommand.setAnagraficaLite(anagraficaLite);
			}
		}
	}

	private class AnagraficaPopupListener extends PopupMenuMouseListener {
		@Override
		protected JPopupMenu getPopupMenu() {
			return popUpMenu;
		}
	}

	private class AnagraficaTableModel extends DefaultTreeTableModel {

		/**
		 * Costruttore.
		 * 
		 * @param root
		 *            root node
		 */
		public AnagraficaTableModel(final TreeTableNode root) {
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
			return 3;
		}

		@Override
		public String getColumnName(final int column) {
			return getMessage("SearchResultAnagraficaColonna" + column);
		}

		@Override
		public Object getValueAt(final Object node, final int column) {
			if (((DefaultMutableTreeTableNode) node).getUserObject() instanceof AnagraficaLite) {
				final AnagraficaLite anagrafica = (AnagraficaLite) ((DefaultMutableTreeTableNode) node).getUserObject();
				if (anagrafica == null) {
					return "";
				}
				switch (column) {
				case 0:
					return anagrafica.getDenominazione();
				case 1:
					return anagrafica.getPartiteIVA();
				case 2:
					if (anagrafica.getSedeAnagrafica().getDatiGeografici().getLocalita() == null) {
						return "";
					}
					return anagrafica.getSedeAnagrafica().getDatiGeografici().getLocalita().getDescrizione();
				default:
					return "";
				}
			}
			return "";
		}

		@Override
		public boolean isCellEditable(Object node, int column) {
			return false;
		}
	}

	private class AnagraficaTreeCellRenderer extends DefaultTreeCellRenderer {
		private static final long serialVersionUID = -8813682511205137384L;

		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
				boolean leaf, int row, boolean hasFocus) {
			IconSource iconSource = (IconSource) ApplicationServicesLocator.services().getService(IconSource.class);
			JLabel c = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
			DefaultMutableTreeTableNode node = (DefaultMutableTreeTableNode) value;
			// setto l'icona
			if (node.getUserObject() != null) {
				if (node.getUserObject() instanceof AnagraficaLite) {
					c.setIcon(iconSource.getIcon(AnagraficaLite.class.getName()));
					c.setText(((AnagraficaLite) node.getUserObject()).getDenominazione());
				} else {
					EntitaLite entita = (EntitaLite) node.getUserObject();
					for (Entry<String, String> entry : mapTipiEntita.entrySet()) {

						if (entita.getTipo().equals(entry.getValue())) {
							c.setIcon(iconSource.getIcon(entry.getKey()));

							final StringBuilder sb = new StringBuilder();
							sb.append(getMessage(entry.getKey()));
							sb.append(" - ");
							sb.append(entita.getCodice().toString());
							c.setText(sb.toString());
							// Se l'entità è disabilitata setto l'icona
							// disabilitata
							if (!entita.isAbilitato()) {
								c.setIcon(iconSource.getIcon("entitaDisabled"));
							}
							break;
						}
					}
				}

			}
			return c;
		}
	}

	private class DoubleClickListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				if (proprietaCommand.isEnabled()) {
					proprietaCommand.execute();
				}
			}
		}
	}

	private class ExpandCommand extends ActionCommand {

		private static final String ESPANDI_COMMAND = ".expandCommand";
		private static final String EXPAND_STATE = "expand";
		private static final String COLLAPSE_STATE = "collapse";
		private final CommandFaceDescriptor expandDescriptor;
		private final CommandFaceDescriptor collapseDescriptor;

		private boolean collapse;

		/**
		 * Costruttore.
		 * 
		 * @param collapse
		 *            stato inizale del comando
		 */
		public ExpandCommand(final boolean collapse) {
			super(ESPANDI_COMMAND);
			RcpSupport.configure(this);
			this.collapse = collapse;

			Icon toExpandIcon = RcpSupport.getIcon(EXPAND_STATE + ".icon");
			Icon toCollapseIcon = RcpSupport.getIcon(COLLAPSE_STATE + ".icon");
			collapseDescriptor = new CommandFaceDescriptor(null, toExpandIcon, null);
			expandDescriptor = new CommandFaceDescriptor(null, toCollapseIcon, null);
			if (collapse) {
				setFaceDescriptor(collapseDescriptor);
			} else {
				setFaceDescriptor(expandDescriptor);

			}
		}

		@Override
		protected void doExecuteCommand() {
			collapse = !collapse;

			if (collapse) {
				treeTable.collapseAll();
			} else {
				treeTable.expandAll();
			}

			if (getFaceDescriptor().equals(collapseDescriptor)) {
				setFaceDescriptor(expandDescriptor);
			} else {
				setFaceDescriptor(collapseDescriptor);
			}
		}
	}

	private class PropertyKeyListener extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent key) {
			if (key.getKeyCode() == KeyEvent.VK_ENTER) {
				if (proprietaCommand.isEnabled()) {
					proprietaCommand.execute();
				}
				key.consume();
			}
		}
	}

	private class ProprietaCommand extends ActionCommand {

		private EntitaLite entitaLite = null;

		/*
		 * Carico l'entita' selezionata
		 */
		@Override
		protected void doExecuteCommand() {

			EntitaLite entitaLoad = null;
			for (Entry<String, String> entry : mapTipiEntita.entrySet()) {
				if (entitaLite.getTipo().equals(entry.getValue())) {
					try {
						entitaLoad = (EntitaLite) Class.forName(entry.getKey()).newInstance();
						entitaLoad.setId(entitaLite.getId());
						entitaLoad.setVersion(entitaLite.getVersion());
					} catch (Exception e) {
						throw new RuntimeException("errore, impossibile instanziare l'entitaLite ", e);
					}
					break;
				}
			}

			// Ricarico l'entita per avere gli ultimi dati aggiornati
			Entita entita = anagraficaBD.caricaEntita(entitaLoad, false);
			LifecycleApplicationEvent event;
			event = new OpenEditorEvent((entita));
			Application.instance().getApplicationContext().publishEvent(event);
		}

		/**
		 * @param entitaLite
		 *            the entitaLite to set
		 */
		public void setEntitaLite(EntitaLite entitaLite) {
			this.entitaLite = entitaLite;
		}
	}

	protected static final String IDSEARCHANAGRAFICA = "searchResultAnagrafica";
	private final Logger logger = Logger.getLogger(SearchResultAnagrafica.class);
	private IAnagraficaBD anagraficaBD;
	private Map<String, String> commandIdMap;
	// Mappa che contiene classe dell'entita che devo creare e il command
	// associato alla classe
	// Mi serve il commandIdMap perch� � quello che setto tramite il context.
	// Non posso associare direttamente i command perch� si trovano nel
	// command-context.xml che non viene visto
	// dal pages.context. Devo settare gli id e poi caricare i comandi
	private Map<String, NewEntitaCommand> commandMap;

	private CommandGroup nuovoCommandGroup;

	private JPopupMenu popUpMenu;

	private final ProprietaCommand proprietaCommand = new ProprietaCommand();

	private JECCommandGroup toolbar;

	private JXTreeTable treeTable;
	private JPanel controlPanel;

	private Map<String, String> mapTipiEntita;

	/**
	 * Costruttore.
	 * 
	 */
	public SearchResultAnagrafica() {
		super();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		org.springframework.util.Assert.notNull(commandIdMap);

		mapTipiEntita = new HashMap<String, String>();
		mapTipiEntita.put(AgenteLite.class.getName(), AgenteLite.TIPO);
		mapTipiEntita.put(ClienteLite.class.getName(), ClienteLite.TIPO);
		mapTipiEntita.put(ClientePotenzialeLite.class.getName(), ClientePotenzialeLite.TIPO);
		mapTipiEntita.put(FornitoreLite.class.getName(), FornitoreLite.TIPO);
		mapTipiEntita.put(VettoreLite.class.getName(), VettoreLite.TIPO);
	}

	/**
	 * Crea la lista raggruppata per anagrafica.
	 * 
	 * @param entita
	 *            lista di entità
	 * @return lista di entità raggruppate
	 */
	private GroupingList<EntitaLite> createGroupingList(List<EntitaLite> entita) {
		// La lista e' ordinata per anagrafica e cliente-fornitore-vettore
		EventList<EntitaLite> entitaList = GlazedLists.eventList(entita);
		GroupingList<EntitaLite> anagrafiche = new GroupingList<EntitaLite>(entitaList, new Comparator<EntitaLite>() {
			@Override
			public int compare(EntitaLite o1, EntitaLite o2) {
				// dato che possono avere la stessa denominazione, ma id diversi e devono essere ordinati
				if (o1.getAnagrafica().getDenominazione().compareTo(o2.getAnagrafica().getDenominazione()) != 0) {
					// se hanno denominazione diversa va sulla denominazione
					return o1.getAnagrafica().getDenominazione().compareTo(o2.getAnagrafica().getDenominazione());
				}
				// altrimenti va per id in modo da avere tutte le anagrafiche presenti
				return o1.getAnagrafica().getId().compareTo(o2.getAnagrafica().getId());
			}
		});
		return anagrafiche;
	}

	/**
	 * Crea il nodo delle entità.
	 * 
	 * @param anagrafiche
	 *            anagrafiche
	 * @return nodo creato
	 */
	private DefaultMutableTreeTableNode createTreeNode(GroupingList<EntitaLite> anagrafiche) {
		DefaultMutableTreeTableNode rootNode = new DefaultMutableTreeTableNode();
		for (List<EntitaLite> entitaLiteGrouping : anagrafiche) {
			DefaultMutableTreeTableNode node = new DefaultMutableTreeTableNode(entitaLiteGrouping.get(0)
					.getAnagrafica());
			for (EntitaLite entita : entitaLiteGrouping) {
				node.add(new DefaultMutableTreeTableNode(entita));
			}
			rootNode.add(node);
		}
		return rootNode;
	}

	@Override
	protected Object delete() {
		// get the first selected row. If not selected row, return
		int row = treeTable.getSelectedRow();

		// get full path for the selected row
		TreePath path = treeTable.getPathForRow(row);

		// finally, get the object for the last path component
		Object lpc = path.getLastPathComponent();
		logger.debug("--> object  = " + lpc.getClass().getName());
		DefaultMutableTreeTableNode node = (DefaultMutableTreeTableNode) lpc;

		final EntitaLite entitaLite = (EntitaLite) node.getUserObject();

		// cancello l'entit�
		MessageSourceAccessor messageSourceAccessor = (MessageSourceAccessor) ApplicationServicesLocator.services()
				.getService(MessageSourceAccessor.class);
		Object[] parameters = new Object[] {
				messageSourceAccessor.getMessage(entitaLite.getDomainClassName(), new Object[] {}, Locale.getDefault()),
				entitaLite.getAnagrafica().getDenominazione() };
		String titolo = messageSourceAccessor.getMessage("entita.delete.confirm.title", new Object[] {},
				Locale.getDefault());
		String messaggio = messageSourceAccessor.getMessage("entita.delete.confirm.message", parameters,
				Locale.getDefault());
		ConfirmationDialog dialog = new ConfirmationDialog(titolo, messaggio) {

			@Override
			protected void onConfirm() {
				Entita entita = entitaLite.creaProxyEntita();

				anagraficaBD.cancellaEntita(entita);
				PanjeaLifecycleApplicationEvent event = new PanjeaLifecycleApplicationEvent(
						LifecycleApplicationEvent.DELETED, entita);
				Application.instance().getApplicationContext().publishEvent(event);
			}
		};
		dialog.showDialog();
		return null;
	}

	@Override
	public void dispose() {
		// all'interno della dispose rimuovo l'istanza da
		// ApplicationEventMulticaster
		ApplicationEventMulticaster applicationEvent = getApplicationEvent();
		if (applicationEvent != null) {
			applicationEvent.removeApplicationListener(this);
		}
		super.dispose();
	}

	/**
	 * Abilita i vari comandi Nuovo in base all'entità già presente. Esempio. Se ho un già un fornitore il
	 * newCommandFornitore sara' disabilitato
	 * 
	 * @param anagraficaNode
	 *            nodo di riferimento
	 */

	private void enabledNewCommand(final TreeNode anagraficaNode) {
		logger.debug("--> Enter enabledNewCommand");
		// Abilito tutti i comandi
		for (final NewEntitaCommand newEntitaCommand : commandMap.values()) {
			newEntitaCommand.setEnabled(true);
		}

		// Disabilito quelli che hanno un'entità
		for (@SuppressWarnings("unchecked")
		final Enumeration<DefaultMutableTreeTableNode> e = anagraficaNode.children(); e.hasMoreElements();) {
			final DefaultMutableTreeTableNode node = e.nextElement();
			if (node != null && node.getUserObject() != null) {
				commandMap.get(((EntitaLite) node.getUserObject()).getTipo()).setEnabled(false);
			}
		}
		logger.debug("--> Exit enabledNewCommand");
	}

	@Override
	protected void executeSearch(final Map<String, Object> parameters) {
		List<EntitaLite> risultati = anagraficaBD.ricercaEntita((ParametriRicercaEntita) parameters.get("parametri"));

		// La ricerca anagrafica non deve essere filtrata per abilitato
		// Se c'e' il filtro abilitato lo tolgo.
		// if (parameters.containsKey(Entita.PROP_ABILITATO)) {
		// parameters.remove(Entita.PROP_ABILITATO);
		// }
		GroupingList<EntitaLite> anagrafiche = createGroupingList(risultati);
		((DefaultTreeTableModel) treeTable.getTreeTableModel()).setRoot(createTreeNode(anagrafiche));

		// Seleziono il primo
		if (risultati.size() > 0) {
			treeTable.getSelectionModel().setSelectionInterval(0, 0);
		}
	}

	/**
	 * La toolbar di questa ricerca devo personalizzarla. Quindi non inserisco la toolbar di Default
	 * 
	 * @return <code>null</code>
	 */
	@Override
	protected AbstractCommand[] getCommand() {
		return null;
	}

	/**
	 * @return getter of commandIdMap
	 */
	public Map<String, String> getCommandIdMap() {
		return commandIdMap;
	}

	@Override
	public String getId() {
		return IDSEARCHANAGRAFICA;
	}

	@Override
	protected CommandGroup getPopupCommandGroup() {
		return getToolBarCommandGroup();
	}

	@Override
	protected JComponent getSearchControl() {
		if (controlPanel == null) {
			logger.debug("--> Costruisco la treeview per la ricerca anagrafica");
			controlPanel = new JPanel(new BorderLayout());
			treeTable = (JXTreeTable) ((PanjeaComponentFactory) getComponentFactory()).createTreeTable();
			treeTable.setName("searchResultAnagraficaTable");
			treeTable.setColumnControlVisible(true);
			treeTable.setAutoscrolls(true);
			treeTable.setAutoCreateColumnsFromModel(true);
			treeTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			treeTable.addTreeSelectionListener(new AnagraficaListSelectionListener());
			treeTable.addMouseListener(new DoubleClickListener());
			treeTable.addKeyListener(new PropertyKeyListener());
			treeTable.addMouseListener(new AnagraficaPopupListener());
			treeTable.setTreeCellRenderer(new AnagraficaTreeCellRenderer());
			AbstractTreeTableModel model = new AnagraficaTableModel(
					createTreeNode(createGroupingList(new ArrayList<EntitaLite>())));
			treeTable.setTreeTableModel(model);
			controlPanel.add(getToolBarCommandGroup().createToolBar(), BorderLayout.NORTH);
			controlPanel.add(getComponentFactory().createScrollPane(treeTable), BorderLayout.CENTER);
		}
		return controlPanel;
	}

	/**
	 * @return toolbar
	 */
	private JECCommandGroup getToolBarCommandGroup() {
		if (toolbar == null) {
			nuovoCommandGroup = new JECCommandGroup("buttonNuovaEntita");
			final CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
					CommandConfigurer.class);
			nuovoCommandGroup.setSecurityControllerId(getId() + ".controller");
			c.configure(nuovoCommandGroup);

			// Ciclo sui comandi impostati
			commandMap = new HashMap<String, NewEntitaCommand>();
			// Trasformo la mappa con gli id dei comandi in quella con l'oggetto
			// command
			for (final String clazz : commandIdMap.keySet()) {
				commandMap.put(mapTipiEntita.get(clazz), (NewEntitaCommand) getActiveWindow().getCommandManager()
						.getCommand(commandIdMap.get(clazz)));
			}

			for (final NewEntitaCommand newEntitaCommand : commandMap.values()) {
				nuovoCommandGroup.add(newEntitaCommand);
			}

			toolbar = new JECCommandGroup("toolbarSearchResultAnagrafica");
			toolbar.add(new ExpandCommand(true));
			toolbar.add(this.getRefreshCommand());
			toolbar.add(this.getDeleteCommand());
			toolbar.add(nuovoCommandGroup);
		}
		popUpMenu = toolbar.createPopupMenu();
		return toolbar;
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	/**
	 * 
	 * @return false
	 */
	public boolean isLocked() {
		return false;
	}

	/*
	 * Se ho un entita' modificata/creata devo aggiornare quella nella treeView
	 */
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof PanjeaLifecycleApplicationEvent && event.getSource() instanceof Entita) {
			PanjeaLifecycleApplicationEvent le = (PanjeaLifecycleApplicationEvent) event;
			if (le.getEventType().equals(LifecycleApplicationEvent.CREATED)) {
				logger.debug("---> PanjeaLifecycleApplicationEvent.CREATED Aggiungo nel treeview");
				refreshData();
			} else if (le.getEventType().equals(LifecycleApplicationEvent.MODIFIED)) {
				logger.debug("---> PanjeaLifecycleApplicationEvent.MODIFIED modifico nel treeview");
				refreshData();
			} else if (le.getEventType().equals(LifecycleApplicationEvent.DELETED)) {
				logger.debug("---> PanjeaLifecycleApplicationEvent.DELETED cancello nel treeview");
				refreshData();
			}
		}
	}

	/**
	 * Esegue il refresh dei dati.
	 */
	protected void refreshData() {
		super.refresh();
	}

	@Override
	public void restoreState(Settings settings) {
		logger.debug("--> Enter restoreState");
		try {
			new PanjeaTableMemento(treeTable, IDSEARCHANAGRAFICA + ".table").restoreState(settings);
		} catch (Exception e) {
			logger.error("--> Errore nel ripristinare lo stato della tabella", e);
		}
		logger.debug("--> Exit restoreState");
	}

	@Override
	public void saveState(Settings settings) {
		logger.debug("--> Enter saveState");
		new PanjeaTableMemento(treeTable, IDSEARCHANAGRAFICA + ".table").saveState(settings);
		logger.debug("--> Exit saveState");
	}

	/**
	 * @param anagraficaBD
	 *            setter of anagraficaBD
	 */
	public void setAnagraficaBD(final IAnagraficaBD anagraficaBD) {
		this.anagraficaBD = anagraficaBD;
	}

	/**
	 * @param commandIdMap
	 *            setter of commandIdMap
	 */
	public void setCommandIdMap(final Map<String, String> commandIdMap) {
		this.commandIdMap = commandIdMap;
	}

	@Override
	public void viewResults(Collection<Anagrafica> results) {

	}
}
