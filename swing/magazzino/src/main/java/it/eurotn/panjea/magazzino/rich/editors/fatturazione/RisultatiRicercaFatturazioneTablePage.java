/**
 *
 */
package it.eurotn.panjea.magazzino.rich.editors.fatturazione;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzinoLite;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.commands.documento.GestisciRigheNonValideCommand;
import it.eurotn.panjea.magazzino.rich.editors.fatturazione.AreaMagazzinoLitePM.StatoRigaAreaMagazzinoLitePM;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaFatturazione;
import it.eurotn.panjea.rich.components.JecSplitPane;
import it.eurotn.panjea.rich.factory.PanjeaComponentFactory;
import it.eurotn.rich.editors.AbstractTreeTableDialogPageEditor;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.table.AbstractTableModel;
import javax.swing.tree.TreeCellRenderer;

import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.TreeTableModel;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.command.config.CommandConfigurer;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

/**
 * Visualizza i risulati della ricerca dando la possibilita di selezionare i documenti e aggiungerli al carrello.
 *
 * @author fattazzo
 */
public class RisultatiRicercaFatturazioneTablePage extends AbstractTreeTableDialogPageEditor implements
InitializingBean {

	/**
	 * Command per aggiungere al carrello le aree selezionate.
	 *
	 * @author fattazzo
	 */
	private class AggiungiAreeACarrelloCommand extends ActionCommand {

		private static final String COMMAND_ID = "aggiungiAreeACarrelloCommand";

		/**
		 * Costruttore di default.
		 */
		public AggiungiAreeACarrelloCommand() {
			super(COMMAND_ID);
			CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
					CommandConfigurer.class);
			c.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			carrelloFatturazioneTablePage.aggiungiAreeMagazzino(getAreeSelezionate());
			// mostra il carrello dei pagamenti
			openCarrello();
		}
	}

	/**
	 * Property change che si preoccupa di aggiornare lo stato delle aree a seconda delle operazioni eseguite dal model.
	 *
	 * @author Leonardo
	 */
	private class AreeChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt != null && evt.getNewValue() != null) {
				if (evt.getPropertyName().equals(RisultatiRicercaFatturazioneModel.AREA_AGGIUNTA)) {
					// se aggiungo un'area devo portare lo stato dell'area dei
					// risultati ricerca a AGGIUNTO_CARRELLO
					AreaMagazzinoLitePM areaMagazzinoLitePM = (AreaMagazzinoLitePM) evt.getNewValue();
					getRisultatiRicercaFatturazioneModel().updateStatoAreaPM(areaMagazzinoLitePM,
							StatoRigaAreaMagazzinoLitePM.AGGIUNTO_CARRELLO);
				} else if (evt.getPropertyName().equals(RisultatiRicercaFatturazioneModel.RIMUOVI_AREE_SELEZIONATE)) {
					// se svuoto il carrello, chiudo lo stesso per mostrare solo
					// i risultati ricerca
					closeCarrello();
				} else if (evt.getPropertyName().equals(RisultatiRicercaFatturazioneModel.AREA_RIMOSSA)) {
					// se rimuovo l'area dal carrello devo rendere l'area
					// nuovamente SELEZIONABILE
					AreaMagazzinoLitePM areaMagazzinoLitePM = (AreaMagazzinoLitePM) evt.getNewValue();
					getRisultatiRicercaFatturazioneModel().updateStatoAreaPM(areaMagazzinoLitePM,
							StatoRigaAreaMagazzinoLitePM.SELEZIONABILE);
				}
			}
		}
	}

	private class FatturazioneMovimentiListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			RisultatiRicercaFatturazioneTablePage.this.firePropertyChange(
					RisultatiRicercaFatturazioneTablePage.FATTURA_MOVIMENTI, null, evt.getNewValue());
		}

	}

	private class RefreshPropertyChange implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			// L'evento è generato quando eseguo una fatturazione.
			// se oldValue=true allora la fatturazione è andata a buon fine
			if ((Boolean) evt.getOldValue()) {
				refreshData();
			} else {
				if (evt.getNewValue() instanceof ParametriRicercaFatturazione) {
					// setFormObject(evt.getNewValue());
					// loadData();
					setTableData(createTreeNode(new TreeSet<AreaMagazzinoLitePM>()));
					risultatiRicercaFatturazioneModel.setAreeMagazzinoLite(new ArrayList<AreaMagazzinoLite>());
				}
			}
		}
	}

	/**
	 * Interceptor per settare le aree magazzino da verificare al command prima dell'esecuzione.
	 *
	 * @author Leonardo
	 */
	private class RigheFatturazioneNonValideCommandInterceptor implements ActionCommandInterceptor {

		@Override
		public void postExecution(ActionCommand command) {

		}

		@Override
		public boolean preExecution(ActionCommand command) {
			getGestisciRigheNonValideCommand().setAreeMagazzino(
					risultatiRicercaFatturazioneModel.getAreeMagazzinoLite());
			return true;
		}
	}

	/**
	 * TreeListener per rendere le cambiare lo stato selected dell'area magazzino.
	 *
	 * @author Leonardo
	 */
	private class TreeMouseListener extends MouseAdapter implements KeyListener {

		/**
		 * Cambia il valore del campo selected dell'area.
		 */
		private void changeAreaSelected() {
			List<DefaultMutableTreeTableNode> nodi = getSelectedNodes();
			if (nodi != null && !nodi.isEmpty()) {

				int firstSelRow = getTreeTable().getSelectionModel().getMinSelectionIndex();
				int lastSelRow = getTreeTable().getSelectionModel().getMaxSelectionIndex();

				for (DefaultMutableTreeTableNode node : nodi) {
					// se e' area magazzino la seleziono altrimenti e' un tipo
					// documento non faccio niente
					if (node.getUserObject() instanceof AreaMagazzinoLitePM) {
						AreaMagazzinoLitePM areaMagazzinoLitePM = (AreaMagazzinoLitePM) node.getUserObject();
						if (areaMagazzinoLitePM.getStatoRigaAreaMagazzinoLitePM() == StatoRigaAreaMagazzinoLitePM.SELEZIONABILE) {
							areaMagazzinoLitePM.setSelected(!areaMagazzinoLitePM.isSelected());
						}
					}
				}

				((AbstractTableModel) RisultatiRicercaFatturazioneTablePage.this.getTreeTable().getModel())
				.fireTableRowsUpdated(firstSelRow, lastSelRow);
			}
		}

		@Override
		public void keyPressed(KeyEvent e) {

		}

		@Override
		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				changeAreaSelected();
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {

		}

		@Override
		public void mouseClicked(MouseEvent e) {
			changeAreaSelected();
		}
	}

	private static final double CARRELLO_INITIAL_HEIGHT = 0.6;
	public static final String PAGE_ID = "risultatiRicercaFatturazioneTablePage";
	public static final String KEY_ICON_NON_SELEZIONABILE = "nonSelezionabile.icon";
	public static final String KEY_ICON_SELEZIONABILE = "selezionabile.icon";
	public static final String KEY_ICON_AGGIUNTA = "aggiuntoCarrello.icon";
	public static final String KEY_MSG_NON_SELEZIONABILE = PAGE_ID + ".nonSelezionabile.label";

	public static final String KEY_MSG_SELEZIONABILE = PAGE_ID + ".selezionabile.label";

	public static final String KEY_MSG_AGGIUNTA = PAGE_ID + ".aggiuntoCarrello.label";
	public static final String SELEZIONA_TUTTO_COMMAND_ID = "selezionaTuttoCommand";

	public static final String DESELEZIONA_TUTTO_COMMAND_ID = "deselezionaTuttoCommand";

	public static final String FATTURA_MOVIMENTI = "fatturaMovimenti";

	private ParametriRicercaFatturazione parametriRicercaFatturazione = null;

	private IMagazzinoDocumentoBD magazzinoDocumentoBD = null;

	private RisultatiRicercaFatturazioneModel risultatiRicercaFatturazioneModel = null;

	private CarrelloFatturazioneTablePage carrelloFatturazioneTablePage = null;

	private JecSplitPane splitPane = null;

	private AggiungiAreeACarrelloCommand aggiungiAreeACarrelloCommand = null;
	private SelectTreeNodesCommand selezionaTuttoCommand = null;

	private SelectTreeNodesCommand deselezionaTuttoCommand = null;

	private GestisciRigheNonValideCommand gestisciRigheNonValideCommand = null;

	/**
	 * Costruttore di default.
	 */
	public RisultatiRicercaFatturazioneTablePage() {
		super(PAGE_ID);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.risultatiRicercaFatturazioneModel = new RisultatiRicercaFatturazioneModel();
		this.carrelloFatturazioneTablePage.setRisultatiRicercaFatturazioneModel(risultatiRicercaFatturazioneModel);
	}

	/**
	 * Chiude il carrello lasciando pieno spazio ai risultati aree.
	 */
	private void closeCarrello() {
		splitPane.collapse();
	}

	@Override
	protected void configureTreeTable(JXTreeTable treeTable) {
		treeTable.setEditable(false);
		treeTable.setDragEnabled(true);

		// listeners per la selezione dei record, posso selezionarli via mouse o
		// con lo SPAZIO
		TreeMouseListener treeMouseListener = new TreeMouseListener();
		treeTable.addMouseListener(treeMouseListener);
		treeTable.addKeyListener(treeMouseListener);
	}

	/**
	 * Aggiunge il panel legenda al pannello con i commands per la tabella.
	 *
	 * @return buttonBar per la tabella
	 */
	@Override
	protected JPanel createButtonPanel() {
		JPanel panel = super.createButtonPanel();
		LegendaRisultatiRicercaFatturazionePanel legenda = new LegendaRisultatiRicercaFatturazionePanel();
		panel.add(legenda, BorderLayout.LINE_START);
		return panel;
	}

	/**
	 * Costruisce il tree per il treeTableModel.
	 *
	 * @param areeMagazzino
	 *            areeMagazzino da visualizzare
	 * @return DefaultMutableTreeTableNode che rappresenta le aree magazzino raggruppate per tipo documento
	 */
	private DefaultMutableTreeTableNode createTreeNode(Set<AreaMagazzinoLitePM> areeMagazzino) {

		DefaultMutableTreeTableNode rootNode = new DefaultMutableTreeTableNode();

		DefaultMutableTreeTableNode tipoDocNode = null;

		TipoDocumento tipoDocumento = null;

		for (AreaMagazzinoLitePM areaMagazzinoLitePM : areeMagazzino) {

			if (tipoDocumento == null
					|| !tipoDocumento.equals(areaMagazzinoLitePM.getAreaMagazzinoLite().getDocumento()
							.getTipoDocumento())) {

				if (tipoDocumento != null) {
					rootNode.add(tipoDocNode);
				}

				tipoDocumento = areaMagazzinoLitePM.getAreaMagazzinoLite().getDocumento().getTipoDocumento();
				tipoDocNode = new DefaultMutableTreeTableNode(tipoDocumento);
			}

			DefaultMutableTreeTableNode areaNode = new DefaultMutableTreeTableNode(areaMagazzinoLitePM);

			if (tipoDocNode != null) {
				tipoDocNode.add(areaNode);
			}
		}

		if (tipoDocumento != null) {
			rootNode.add(tipoDocNode);
		}

		return rootNode;
	}

	@Override
	protected TreeTableModel createTreeTableModel() {
		return getTreeTableModel(null);
	}

	/**
	 *
	 * @return command per aggiunger le aree selezionate al carrello
	 */
	public AggiungiAreeACarrelloCommand getAggiungiAreeACarrelloCommand() {
		if (aggiungiAreeACarrelloCommand == null) {
			aggiungiAreeACarrelloCommand = new AggiungiAreeACarrelloCommand();
		}

		return aggiungiAreeACarrelloCommand;
	}

	/**
	 * Restituisce la lista di tutte le aree selezionate.
	 *
	 * @return aree selezionate nella tabella
	 *
	 */
	public Set<AreaMagazzinoLitePM> getAreeSelezionate() {
		Set<AreaMagazzinoLitePM> areeSelezionate = new HashSet<AreaMagazzinoLitePM>();

		Set<AreaMagazzinoLitePM> aree = this.risultatiRicercaFatturazioneModel.getAreePM();
		for (AreaMagazzinoLitePM areaMagazzinoLitePM : aree) {
			if (areaMagazzinoLitePM.isSelected()
					&& areaMagazzinoLitePM.getStatoRigaAreaMagazzinoLitePM().compareTo(
							StatoRigaAreaMagazzinoLitePM.SELEZIONABILE) == 0) {
				areeSelezionate.add(areaMagazzinoLitePM);
			}
		}
		return areeSelezionate;
	}

	/**
	 *
	 * @return pagina del carrello
	 */
	public CarrelloFatturazioneTablePage getCarrelloFatturazioneTablePage() {
		return carrelloFatturazioneTablePage;
	}

	@Override
	public AbstractCommand[] getCommand() {
		return new AbstractCommand[] { getGestisciRigheNonValideCommand(), getSelezionaTuttoCommand(),
				getDeselezionaTuttoCommand(), getAggiungiAreeACarrelloCommand() };
	}

	@Override
	public JComponent getControl() {
		if (splitPane == null) {
			risultatiRicercaFatturazioneModel.addPropertyChangeListener(new AreeChangeListener());

			carrelloFatturazioneTablePage
			.addPropertyChangeListener(CarrelloFatturazioneTablePage.PROPERTY_DOCUMENTO_FATTURAZIONE_GENERATO,
					new RefreshPropertyChange());
			carrelloFatturazioneTablePage.addPropertyChangeListener(CarrelloFatturazioneTablePage.FATTURA_MOVIMENTI,
					new FatturazioneMovimentiListener());

			splitPane = (JecSplitPane) ((PanjeaComponentFactory) getComponentFactory())
					.createSplitPane(JSplitPane.VERTICAL_SPLIT);

			JComponent carrelloFatturazioneTablePageControl = carrelloFatturazioneTablePage.getControl();
			splitPane.setTopComponent(super.getControl().getComponent(0));
			splitPane.setOneTouchExpandable(true);
			splitPane.setBottomComponent(carrelloFatturazioneTablePageControl);
			splitPane.collapse();
		}
		return splitPane;
	}

	/**
	 *
	 * @return comando per deselezionare i movimenti selezionati
	 */
	public SelectTreeNodesCommand getDeselezionaTuttoCommand() {
		if (deselezionaTuttoCommand == null) {
			deselezionaTuttoCommand = new SelectTreeNodesCommand(DESELEZIONA_TUTTO_COMMAND_ID, false, this);
		}

		return deselezionaTuttoCommand;
	}

	/**
	 *
	 * @return comando per aprire la gestione delle regole per le righe articolo
	 */
	public GestisciRigheNonValideCommand getGestisciRigheNonValideCommand() {
		if (gestisciRigheNonValideCommand == null) {
			gestisciRigheNonValideCommand = new GestisciRigheNonValideCommand(PAGE_ID);
			gestisciRigheNonValideCommand.addCommandInterceptor(new RigheFatturazioneNonValideCommandInterceptor());
		}
		return gestisciRigheNonValideCommand;
	}

	/**
	 *
	 * @return RisultatiRicercaFatturazioneModel
	 */
	public RisultatiRicercaFatturazioneModel getRisultatiRicercaFatturazioneModel() {
		return risultatiRicercaFatturazioneModel;
	}

	/**
	 *
	 * @return command per selezionare tutto
	 */
	public SelectTreeNodesCommand getSelezionaTuttoCommand() {
		if (selezionaTuttoCommand == null) {
			selezionaTuttoCommand = new SelectTreeNodesCommand(SELEZIONA_TUTTO_COMMAND_ID, true, this);
		}

		return selezionaTuttoCommand;
	}

	@Override
	protected TreeCellRenderer getTreeCellRender() {
		return new RisultatiRicercaFatturazioneTreeCellRenderer();
	}

	/**
	 *
	 * @param rootNode
	 *            rootNode
	 * @return treetableModel dei risultati
	 */
	private TreeTableModel getTreeTableModel(DefaultMutableTreeTableNode rootNode) {
		DefaultMutableTreeTableNode root = null;
		if (rootNode != null) {
			root = rootNode;
		} else {
			root = createTreeNode(ricercaAreeMagazzino());
		}
		return new FatturazioneTableModel(root);
	}

	@Override
	public void loadData() {
		if (parametriRicercaFatturazione.isEffettuaRicerca()) {
			setTableData(createTreeNode(ricercaAreeMagazzino()));
			List<AreaMagazzinoLitePM> areeNelCarrello = carrelloFatturazioneTablePage.getTable().getRows();
			if (areeNelCarrello.size() > 0) {
				for (AreaMagazzinoLitePM area : areeNelCarrello) {
					risultatiRicercaFatturazioneModel.aggiungiAreaSelezionata(area);
				}
			}
			getTreeTable().expandAll();
		}
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	/**
	 * Mostra il carrello delle aree dividendo la split alla location scelta.
	 */
	private void openCarrello() {
		splitPane.expand(CARRELLO_INITIAL_HEIGHT);
	}

	@Override
	protected void openSelectedNode(DefaultMutableTreeTableNode node) {

		if (node.getUserObject() instanceof AreaMagazzinoLitePM) {
			AreaMagazzinoLitePM areaMagazzinoLitePM = (AreaMagazzinoLitePM) node.getUserObject();
			AreaMagazzino areaMagazzino = new AreaMagazzino();
			areaMagazzino.setId(areaMagazzinoLitePM.getAreaMagazzinoLite().getId());
			AreaMagazzinoFullDTO areaMagazzinoFullDTO = magazzinoDocumentoBD.caricaAreaMagazzinoFullDTO(areaMagazzino);
			LifecycleApplicationEvent event = new OpenEditorEvent(areaMagazzinoFullDTO);
			Application.instance().getApplicationContext().publishEvent(event);
		}
	}

	@Override
	public void refreshData() {
		loadData();
	}

	/**
	 *
	 * @return risultati della ricerca
	 */
	private Set<AreaMagazzinoLitePM> ricercaAreeMagazzino() {
		List<AreaMagazzinoLite> listAree = new ArrayList<AreaMagazzinoLite>();

		if (parametriRicercaFatturazione != null && parametriRicercaFatturazione.isEffettuaRicerca()) {
			listAree = magazzinoDocumentoBD.caricaAreeMagazzino(parametriRicercaFatturazione);
		}

		getRisultatiRicercaFatturazioneModel().setAreeMagazzinoLite(listAree);

		return getRisultatiRicercaFatturazioneModel().getAreePM();
	}

	@Override
	protected void selectionChanged(DefaultMutableTreeTableNode node) {

	}

	/**
	 *
	 * @param carrelloFatturazioneTablePage
	 *            page del carrello
	 */
	public void setCarrelloFatturazioneTablePage(CarrelloFatturazioneTablePage carrelloFatturazioneTablePage) {
		this.carrelloFatturazioneTablePage = carrelloFatturazioneTablePage;
	}

	@Override
	public void setFormObject(Object object) {
		this.parametriRicercaFatturazione = (ParametriRicercaFatturazione) object;
		getRisultatiRicercaFatturazioneModel().setParametriRicercaFatturazione(this.parametriRicercaFatturazione);
	}

	/**
	 *
	 * @param magazzinoDocumentoBD
	 *            magazzinoDocumentoBD
	 */
	public void setMagazzinoDocumentoBD(IMagazzinoDocumentoBD magazzinoDocumentoBD) {
		this.magazzinoDocumentoBD = magazzinoDocumentoBD;
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		getGestisciRigheNonValideCommand().setEnabled(!readOnly);
		getSelezionaTuttoCommand().setEnabled(!readOnly);
		getDeselezionaTuttoCommand().setEnabled(!readOnly);
		getAggiungiAreeACarrelloCommand().setEnabled(!readOnly);

		getCarrelloFatturazioneTablePage().setReadOnly(readOnly);
	}
}
