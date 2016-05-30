package it.eurotn.panjea.rate.rich.editors.rate;

import it.eurotn.locking.IDefProperty;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.RapportoBancarioAzienda;
import it.eurotn.panjea.anagrafica.rich.factory.table.CustomEntitaCellRenderer;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento.TipologiaPartita;
import it.eurotn.panjea.partite.domain.TipoAreaPartita.TipoPartita;
import it.eurotn.panjea.partite.rich.commands.RiemettiRataCommand;
import it.eurotn.panjea.rate.domain.Rata;
import it.eurotn.panjea.rate.domain.Rata.StatoRata;
import it.eurotn.panjea.rate.rich.bd.IRateBD;
import it.eurotn.panjea.rate.rich.commands.AreaRateCommands;
import it.eurotn.panjea.rate.rich.forms.AbstractAreaRateModel;
import it.eurotn.panjea.rich.IEditorListener;
import it.eurotn.panjea.rich.PanjeaLifecycleApplicationEvent;
import it.eurotn.panjea.rich.commands.StampaRVCommand;
import it.eurotn.panjea.tesoreria.domain.AreaChiusure;
import it.eurotn.panjea.tesoreria.domain.Pagamento;
import it.eurotn.panjea.tesoreria.domain.Pagamento.TipoPagamentoDocumento;
import it.eurotn.panjea.tesoreria.rich.bd.ITesoreriaBD;
import it.eurotn.panjea.tesoreria.rich.commands.OpenAreaChiusuraPagamentoCommand;
import it.eurotn.panjea.tesoreria.util.ParametriCreazionePagamento;
import it.eurotn.panjea.tesoreria.util.RataRiemessa;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.dialog.DefaultTitledPageApplicationDialog;
import it.eurotn.rich.editors.AbstractTreeTableDialogPageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableModel;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEvent;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.command.support.ActionCommandInterceptorAdapter;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.util.Assert;

import com.jidesoft.swing.JideButton;

/**
 * Page per la gestione delle rate.
 * 
 * @author Adriano,Leonardo
 */
public class RateTablePage extends AbstractTreeTableDialogPageEditor implements InitializingBean,
		PropertyChangeListener, IEditorListener {

	private class AreaModelAggiornataChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getPropertyName().equals(AbstractAreaRateModel.AREA_MODEL_AGGIORNATA)) {
				RateTablePage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null, evt.getNewValue());
				updateControl(getSelectedNode());
			}
		}
	}

	public class CloseRateCommandInterceptor implements ActionCommandInterceptor {

		@Override
		public void postExecution(ActionCommand arg0) {
			RateTablePage.this.firePropertyChange(VALIDA_AREA_RATE, false, true);
		}

		@Override
		public boolean preExecution(ActionCommand arg0) {
			return true;
		}

	}

	private class CreaPagamentoCommand extends ActionCommand {

		private static final String COMMAND_ID = "creaPagamentoCommand";
		private PagamentoRataPage pagamentoRataPage;

		/**
		 * Costruttore.
		 **/
		public CreaPagamentoCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
			this.setSecurityControllerId(PAGE_ID + ".controller");
		}

		@Override
		protected void doExecuteCommand() {
			if (pagamentoRataPage == null) {
				pagamentoRataPage = new PagamentoRataPage(tesoreriaBD);
			}
			DefaultMutableTreeTableNode node = getSelectedNode();
			if (node != null && node.getUserObject() instanceof Rata) {
				Rata rata = (Rata) node.getUserObject();
				ParametriCreazionePagamento parametriCreazionePagamento = new ParametriCreazionePagamento(rata);
				DefaultTitledPageApplicationDialog pageApplicationDialog = new DefaultTitledPageApplicationDialog(
						parametriCreazionePagamento, RateTablePage.this, pagamentoRataPage);
				pageApplicationDialog.setTitlePaneTitle(RcpSupport.getMessage(pagamentoRataPage.getDescription()));
				pageApplicationDialog.showDialog();
				pageApplicationDialog = null;
			}
		}

		@Override
		protected void onButtonAttached(AbstractButton button) {
			super.onButtonAttached(button);
			// per la toolbar viene creato un jidebutton, per il menù contestuale un JideMenuItem
			// rimuovo la label del button solo per la toolbar
			if (button instanceof JideButton) {
				button.setText(" ");
			}
		}
	}

	private class NewRataCommand extends ActionCommand {
		private static final String COMMAND_ID = "newCommand";

		/**
		 * Costruttore.
		 **/
		public NewRataCommand() {
			super(COMMAND_ID);
			CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
					CommandConfigurer.class);
			this.setSecurityControllerId(PAGE_ID + ".controller");
			c.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			Rata newRata = new Rata();
			newRata.getImporto().setCodiceValuta(
					areaRateModel.getAreaRate().getDocumento().getTotale().getCodiceValuta());
			newRata.setAreaRate(areaRateModel.getAreaRate());
			openDialog(newRata);
		}

		@Override
		protected void onButtonAttached(AbstractButton button) {
			super.onButtonAttached(button);
			button.setName(PAGE_ID + "." + COMMAND_ID);
		}

	}

	private class RiemettiRataCommandInterceptor implements ActionCommandInterceptor {

		@Override
		public void postExecution(ActionCommand arg0) {
			boolean isRiemesse = ((RiemettiRataCommand) arg0).isRiemesse();
			if (isRiemesse) {
				areaRateModel.aggiornaModel();
				RateTablePage.this.refreshData();
			}
		}

		@Override
		public boolean preExecution(ActionCommand arg0) {
			if (getSelectedNode() == null || !(getSelectedNode().getUserObject() instanceof Rata)) {
				return false;
			}
			Rata rata = (Rata) getSelectedNode().getUserObject();
			List<RataRiemessa> rate = new ArrayList<>();
			rate.add(new RataRiemessa(rata));
			arg0.addParameter(RiemettiRataCommand.PARAM_RATE, rate);
			return true;
		}

	}

	private class StampaRVCommandInterceptor extends ActionCommandInterceptorAdapter {

		@Override
		public boolean preExecution(ActionCommand command) {

			DefaultMutableTreeTableNode node = getSelectedNode();
			boolean stampaRata = node != null && node.getUserObject() instanceof Rata;

			if (stampaRata && node != null) {
				command.addParameter(StampaRVCommand.PARAM_LIST_RATE_ID,
						(((Rata) node.getUserObject()).getId()).toString());
			}
			return stampaRata;
		}
	}

	public static final String PAGE_ID = "rateTablePage";
	public static final String VALIDA_AREA_RATE = "validaAreaRate";

	private static Logger logger = Logger.getLogger(RateTablePage.class);

	private AbstractAreaRateModel areaRateModel = null;
	private PropertyChangeListener areaModelAggiornataChangeListener = null;
	private RatePanel ratePanel = null;
	private RataPage rataPage = null;

	private NewRataCommand newRataCommand = null;
	private OpenAreaChiusuraPagamentoCommand openAreaChiusuraPagamentoCommand = null;
	private StampaRVCommand stampaRVCommand = null;
	private AreaRateCommands areaRateCommands = null;
	private CreaPagamentoCommand creaPagamentoCommand = null;
	private RiemettiRataCommand riemettiRataCommand = null;

	private ITesoreriaBD tesoreriaBD = null;
	private IRateBD rateBD = null;
	private RiemettiRataCommandInterceptor riemettiRataCommandInterceptor = null;

	/**
	 * Costruttore.
	 */
	public RateTablePage() {
		super(PAGE_ID);
		setShowTitlePane(false);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(tesoreriaBD, "TesoreriaBD non può essere nullo");
		Assert.notNull(rateBD, "TesoreriaBD non può essere nullo");
		// set di areaPartiteModel in areaPartiteCommands
		areaRateCommands = new AreaRateCommands();
		areaRateCommands.setAreaRateModel(areaRateModel);
		areaRateCommands.getCloseRateCommand().addCommandInterceptor(new CloseRateCommandInterceptor());
		rataPage.setAreaRateModel(this.areaRateModel);
		ratePanel = new RatePanel();
		ratePanel.setAreaRateModel(areaRateModel);
	}

	@Override
	protected void configureTreeTable(final JXTreeTable treeTable) {
		treeTable.setEditable(false);
		treeTable.setDefaultRenderer(RapportoBancarioAzienda.class, new CustomEntitaCellRenderer());

		// higlight per segnare i pagamenti anticipati di una rata chiusa
		treeTable.addHighlighter(new ColorHighlighter(new HighlightPredicate() {

			@Override
			public boolean isHighlighted(Component comp, ComponentAdapter adapter) {
				int depth = adapter.getDepth();
				int row = adapter.row;
				int modelRow = treeTable.convertRowIndexToModel(row);

				TreePath path = treeTable.getPathForRow(modelRow);
				DefaultMutableTreeTableNode node = (DefaultMutableTreeTableNode) path.getLastPathComponent();
				int pagamentiPerRata = node.getParent().getChildCount();

				Object value = adapter.getValue(2);

				// per importo a 0 di un figlio (pagamento) che ha una rata con più di un pagamento, imposto un higlight
				// grigio al testo
				if (value instanceof Importo && depth == 2 && pagamentiPerRata > 1) {
					Importo importo = (Importo) value;
					if (importo.getImportoInValutaAzienda().compareTo(BigDecimal.ZERO) == 0) {
						return true;
					}
				}
				return false;
			}
		}, null, Color.LIGHT_GRAY));

		treeTable.setBorder(BorderFactory.createEmptyBorder());
	}

	@Override
	protected JPanel createButtonPanel() {
		JPanel panel = super.createButtonPanel();
		panel.add(ratePanel, BorderLayout.LINE_START);
		return panel;
	}

	/**
	 * @param rate
	 *            dell'area
	 * @return nodo root per la tree con le rate
	 */
	private DefaultMutableTreeTableNode createTreeNode(Collection<Rata> rate) {
		DefaultMutableTreeTableNode rootNode = new DefaultMutableTreeTableNode();
		DefaultMutableTreeTableNode nodeRata = null;
		for (Rata rata : rate) {
			nodeRata = new DefaultMutableTreeTableNode(rata);
			rootNode.add(nodeRata);

			Collection<Pagamento> pagamenti = rata.getPagamenti();
			if (pagamenti != null) {
				for (Pagamento pagamento : pagamenti) {
					nodeRata.add(new DefaultMutableTreeTableNode(pagamento));
				}
			}
		}
		return rootNode;
	}

	@Override
	protected TreeTableModel createTreeTableModel() {
		return getTreeTableModel(null);
	}

	@Override
	public void dispose() {
		if (riemettiRataCommand != null) {
			riemettiRataCommand.addCommandInterceptor(getRiemettiRataCommandInterceptor());
		}
		super.dispose();
	}

	@Override
	protected boolean doDelete(DefaultMutableTreeTableNode node) {
		logger.debug("--> Enter doDelete");
		if (node != null && node.getUserObject() instanceof Rata) {
			this.areaRateModel.cancellaRataPartita((Rata) node.getUserObject());
			logger.debug("--> Exit doDelete");
			return true;
		} else if (node != null && node.getUserObject() instanceof Pagamento) {
			Pagamento pagamento = (Pagamento) node.getUserObject();
			if (isAccontoLiquidazione(pagamento)) {
				tesoreriaBD.cancellaPagamentoAccontoLiquidazione(pagamento);
				// per rimuovere il pagamento, normalmente, viene lanciato l'evento di cancellazione dell'area tesoreria
				// associata, ma nel caso di acconto per documento di liquidazione, il documento di pagamento è il
				// documento di liquidazione stesso.
				refreshData();
			}
		}

		return false;
	}

	/**
	 * @return propertychangeListener per "ascoltare" se cambia l'areamodel
	 */
	private PropertyChangeListener getAreaRateChangeListener() {
		if (areaModelAggiornataChangeListener == null) {
			areaModelAggiornataChangeListener = new AreaModelAggiornataChangeListener();
		}
		return areaModelAggiornataChangeListener;
	}

	/**
	 * @return Returns the areaPartiteCommands.
	 */
	public AreaRateCommands getAreaRateCommands() {
		return areaRateCommands;
	}

	/**
	 * @return Returns the areaPartiteModel.
	 */
	public AbstractAreaRateModel getAreaRateModel() {
		return areaRateModel;
	}

	@Override
	public AbstractCommand[] getCommand() {
		areaRateCommands.add(getRiemettiRataCommand());
		areaRateCommands.add(getCreaPagamentoCommand());
		areaRateCommands.add(getStampaRVCommand());
		areaRateCommands.add(areaRateCommands.getCloseRateCommand());
		areaRateCommands.add(getNewRataCommand());
		areaRateCommands.add(getDeleteCommand());
		AbstractCommand property = getPropertyCommand();
		property.setVisible(false);
		areaRateCommands.add(property);
		return areaRateCommands.getCommands();
	}

	/**
	 * @return command per il pagamento di una rata
	 */
	private ActionCommand getCreaPagamentoCommand() {
		if (creaPagamentoCommand == null) {
			creaPagamentoCommand = new CreaPagamentoCommand();
		}
		return creaPagamentoCommand;
	}

	/**
	 * @return command per una nuova rata
	 */
	private ActionCommand getNewRataCommand() {
		if (newRataCommand == null) {
			newRataCommand = new NewRataCommand();
		}
		return newRataCommand;
	}

	/**
	 * @return the openAreaChiusuraPagamentoCommand
	 */
	public OpenAreaChiusuraPagamentoCommand getOpenAreaChiusuraPagamentoCommand() {
		if (openAreaChiusuraPagamentoCommand == null) {
			openAreaChiusuraPagamentoCommand = new OpenAreaChiusuraPagamentoCommand();
		}
		return openAreaChiusuraPagamentoCommand;
	}

	@Override
	public String getOverlayMessage() {
		return "AREA PARTITE NON PRESENTE";
	}

	/**
	 * @return the rataPartitaPage
	 */
	public RataPage getRataPartitaPage() {
		return rataPage;
	}

	/**
	 * @return lista delle rate nell'area Rate. se non ci sono rate ritorna una lista vuota
	 */
	private Set<Rata> getRate() {
		Set<Rata> rate = new TreeSet<Rata>();
		if (areaRateModel.getAreaRate() != null && areaRateModel.getAreaRate().getRate() != null) {
			rate = areaRateModel.getAreaRate().getRate();
		}
		return rate;
	}

	/**
	 * @return Returns the riemettiRataCommand.
	 */
	public RiemettiRataCommand getRiemettiRataCommand() {
		if (riemettiRataCommand == null) {
			riemettiRataCommand = new RiemettiRataCommand();
			riemettiRataCommand.addCommandInterceptor(getRiemettiRataCommandInterceptor());
		}
		return riemettiRataCommand;
	}

	/**
	 * @return RiemettiRataCommandInterceptor
	 */
	public RiemettiRataCommandInterceptor getRiemettiRataCommandInterceptor() {
		if (riemettiRataCommandInterceptor == null) {
			riemettiRataCommandInterceptor = new RiemettiRataCommandInterceptor();
		}
		return riemettiRataCommandInterceptor;
	}

	/**
	 * @return the stampaRVCommand
	 */
	public StampaRVCommand getStampaRVCommand() {
		if (stampaRVCommand == null) {
			stampaRVCommand = new StampaRVCommand();
			stampaRVCommand.addCommandInterceptor(new StampaRVCommandInterceptor());
		}
		return stampaRVCommand;
	}

	@Override
	protected TreeCellRenderer getTreeCellRender() {
		return new RateResultCellRender();
	}

	/**
	 * @param rootNode
	 *            nodo principale
	 * @return treetablenode con le rate
	 */
	private TreeTableModel getTreeTableModel(DefaultMutableTreeTableNode rootNode) {
		DefaultMutableTreeTableNode root = null;
		if (rootNode != null) {
			root = rootNode;
		} else {
			root = createTreeNode(getRate());
		}
		return new PartiteTreeTableModel(root);
	}

	/**
	 * Restituisce true se il pagamento è di un acconto e se l'area rate è di un documento di liquidazione.
	 * 
	 * @param pagamento
	 *            il pagamento da verificare
	 * @return true o false
	 */
	private boolean isAccontoLiquidazione(Pagamento pagamento) {
		boolean isAcconto = pagamento.getTipoPagamentoDocumento().equals(TipoPagamentoDocumento.ACCONTO);
		// da mail arriva una null pointer. Controllo tutti i valiri che potrebbero essere null
		if (areaRateModel.getAreaRate() == null) {
			return false;
		}
		if (areaRateModel.getAreaRate().getCodicePagamento() == null) {
			return false;
		}
		if (areaRateModel.getAreaRate().getCodicePagamento().getTipologiaPartita() == null) {
			return false;
		}
		boolean isAreaRateLiquidazione = areaRateModel.getAreaRate().getCodicePagamento().getTipologiaPartita()
				.equals(TipologiaPartita.LIQUIDAZIONE);
		return isAcconto && isAreaRateLiquidazione;
	}

	@Override
	public void loadData() {
		logger.debug("--> Enter loadData");
		setTableData(createTreeNode(getRate()));
		updateControl(getSelectedNode());
		logger.debug("--> Exit loadData");
	}

	/**
	 * @param event
	 *            evento ricevuto dall'editor
	 */
	@Override
	public void onEditorEvent(ApplicationEvent event) {
		PanjeaLifecycleApplicationEvent panjeaEvent = (PanjeaLifecycleApplicationEvent) event;
		String evtName = panjeaEvent.getEventType();
		// Se viene cancellato un documento di chiusura viene cancellata anche un pagamento.<br>
		// Ricarico l'area rate perche' potrebbe essere un pagamento presente in questa pagina.<br>
		// se l'oggetto e' una rata ricarico sempre l'area rate, non verifico se e' presente nella pagina, teoricamente
		// ho poche RateTablePage quindi non dovrei vedere troppa diff. di prestazioni
		boolean ricarica = LifecycleApplicationEvent.DELETED.equals(evtName)
				&& panjeaEvent.getSource() instanceof AreaChiusure;
		if (!ricarica) {
			ricarica = LifecycleApplicationEvent.CREATED.equals(evtName)
					&& panjeaEvent.getSource() instanceof AreaChiusure;
		}
		if (!ricarica) {
			ricarica = panjeaEvent.getSource() instanceof Rata && panjeaEvent.getSourceContainer() != areaRateModel;
		}
		if (ricarica) {
			this.areaRateModel.ricaricaAreaRate();
			setTableData(createTreeNode(this.areaRateModel.getAreaRate().getRate()));
		}
	}

	@Override
	public void onPostPageOpen() {
		// nothing to do
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	/**
	 * refresh dei dati.
	 */
	public void onRefresh() {
		logger.debug("--> Enter onRefresh");
		areaRateModel.ricaricaAreaRate();
		loadData();
		logger.debug("--> Exit onRefresh");
	}

	/**
	 * Apre il dialog per la modifica di una rata.
	 * 
	 * @param rata
	 *            rata da modificare
	 */
	private void openDialog(Rata rata) {
		DefaultTitledPageApplicationDialog pageApplicationDialog = new DefaultTitledPageApplicationDialog(rata, this,
				rataPage);
		pageApplicationDialog.showDialog();
		pageApplicationDialog = null;
		updateControl(getSelectedNode());
	}

	@Override
	protected void openSelectedNode(DefaultMutableTreeTableNode node) {
		if (node.getUserObject() instanceof Rata) {
			Rata rataPartita = (Rata) node.getUserObject();
			logger.debug("--> Selezionata la rata, la modifico");
			openDialog((Rata) PanjeaSwingUtil.cloneObject(rataPartita));
		} else if (node.getUserObject() instanceof Pagamento
				&& !isAccontoLiquidazione((Pagamento) node.getUserObject())) {
			logger.debug("--> Selezionato il pagamento, apro il documento di pagamento collegato");
			Pagamento pagamento = (Pagamento) node.getUserObject();
			getOpenAreaChiusuraPagamentoCommand().setPagamento(pagamento);
			getOpenAreaChiusuraPagamentoCommand().execute();
		}
	}

	/**
	 * Riceve l'evento lanciato dalla popUp che indica che l'oggetto e' stato cambiato. getNewValue restituisce
	 * l'oggetto dopo essere cambiato.
	 * 
	 * @param evt
	 *            evento
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void propertyChange(PropertyChangeEvent evt) {
		logger.debug("--> Modificato l'oggetto nella popup - Aggiorno la tabella.");
		IDefProperty objectDefProperty = (IDefProperty) evt.getNewValue();

		Integer vs = objectDefProperty.getVersion();

		if (vs != null) {
			// considero di default la modifica di una riga esistente; se non trovo nessuna riga e quindi 'updated'
			// rimane false, allora inserisco nel tree la nuova rata.
			// NOTA:Prima inserivo la riga se la versione era 0, altrimenti modificavo; questo non andava bene nel caso
			// confermassi il dialog senza modificare nessun dato della rata (salvare senza modifiche non incrementa la
			// version).
			// Disabilitare la conferma del popup con dirty=false risultava più complesso del previsto, quindi ho girato
			// la logica dando priorità alla modifica rispetto all'inserimento del nuovo nodo.
			// MANTIS BUG 16
			boolean updated = false;
			DefaultMutableTreeTableNode node = (DefaultMutableTreeTableNode) getTreeTable().getTreeTableModel()
					.getRoot();
			Enumeration<DefaultMutableTreeTableNode> rateNodes = (Enumeration<DefaultMutableTreeTableNode>) node
					.children();
			while (rateNodes.hasMoreElements()) {
				DefaultMutableTreeTableNode nodeRata = rateNodes.nextElement();
				Rata rata = (Rata) nodeRata.getUserObject();
				if (rata.equals(objectDefProperty)) {
					nodeRata.setUserObject(objectDefProperty);
					updated = true;
					break;
				}
			}

			// se non ho aggiornato nessuna riga è nuova e quindi la inserisco
			// nel tree
			if (!updated) {
				int initialChildCount = node.getChildCount();
				DefaultTreeTableModel model = ((DefaultTreeTableModel) getTreeTable().getTreeTableModel());

				// areaPartiteModel.getAreaPartite().getRatePartita().add((RataPartita)objectDefProperty);
				DefaultMutableTreeTableNode nuovoNode = new DefaultMutableTreeTableNode(objectDefProperty);

				model.insertNodeInto(nuovoNode, node, node.getChildCount());
				// risetto il rootnode aggiornato solo nel caso in cui il rootnode non ha figli ci deve essere un bug
				// che non mi visualizza il primo elemento inserito (vedi bug 406)
				if (initialChildCount == 0) {
					model.setRoot(node);
				}
			}
		}
	}

	@Override
	protected void publishApplicationEvent(String e, Object obj) {
		PanjeaLifecycleApplicationEvent event = new PanjeaLifecycleApplicationEvent(e, obj, this.areaRateModel);
		Application.instance().getApplicationContext().publishEvent(event);
	}

	@Override
	public void refreshData() {
		logger.debug("--> Enter refreshData");
		areaRateModel.ricaricaAreaRate();
		loadData();
		logger.debug("--> Exit refreshData");
	}

	@Override
	protected void selectionChanged(DefaultMutableTreeTableNode node) {
		updateControl(node);
	}

	/**
	 * @param paramAreaRateCommands
	 *            The areaRateCommands to set.
	 */
	public void setAreaRateCommands(AreaRateCommands paramAreaRateCommands) {
		this.areaRateCommands = paramAreaRateCommands;
	}

	/**
	 * @param areaRateModel
	 *            The areaRateModel to set.
	 */
	public void setAreaRateModel(AbstractAreaRateModel areaRateModel) {
		this.areaRateModel = areaRateModel;
		this.areaRateModel.addPropertyChangeListener(AbstractAreaRateModel.AREA_MODEL_AGGIORNATA,
				getAreaRateChangeListener());
	}

	/**
	 * Sovrascrivo per aggiornare il model con il nuovo form object della page.
	 * 
	 * @param object
	 *            objet da settare nella pagina
	 */
	@Override
	public void setFormObject(Object object) {
		logger.debug("--> Enter setFormObject");
		areaRateModel.setObject(object);
		setOverlayVisible(areaRateModel.getAreaDocumento().isNew() || !areaRateModel.isAreaRatePresente());
		logger.debug("--> Exit setFormObject");
	}

	/**
	 * @param rataPage
	 *            the rataPage to set
	 */
	public void setRataPage(RataPage rataPage) {
		this.rataPage = rataPage;
	}

	/**
	 * @param rateBD
	 *            the rateBD to set
	 */
	public void setRateBD(IRateBD rateBD) {
		this.rateBD = rateBD;
	}

	/**
	 * @param tesoreriaBD
	 *            the tesoreriaBD to set
	 */
	public void setTesoreriaBD(ITesoreriaBD tesoreriaBD) {
		this.tesoreriaBD = tesoreriaBD;
	}

	/**
	 * Aggiorna i controlli della pagina.
	 * 
	 * @param node
	 *            nodo di riferimento per aggiornare i controlli
	 */
	private void updateControl(DefaultMutableTreeTableNode node) {
		logger.debug("--> Enter updateControl");
		areaRateCommands.setEnabled(areaRateModel.isEnabled());
		enable(areaRateModel.isEnabled());
		// il property deve essere sempre enable
		getPropertyCommand().setEnabled(true);
		ratePanel.update();

		getStampaRVCommand().setVisible(false);
		getCreaPagamentoCommand().setEnabled(false);
		getRiemettiRataCommand().setEnabled(false);
		if (node != null) {
			Object obj = node.getUserObject();
			if (obj instanceof Rata) {
				getCreaPagamentoCommand().setEnabled(true);
				getDeleteCommand().setEnabled(isEnable());
				Rata rata = ((Rata) obj);
				if (rata.isStampaRV() && rata.getAreaRate().getTipoAreaPartita().getTipoPartita() == TipoPartita.ATTIVA) {
					getStampaRVCommand().setEnabled(true);
					getStampaRVCommand().setVisible(true);
				}
				getRiemettiRataCommand().setEnabled(
						rata.getStatoRata().equals(StatoRata.IN_RIASSEGNAZIONE)
								|| rata.getStatoRata().equals(StatoRata.RIEMESSA));
			} else if (obj instanceof Pagamento) {
				Pagamento pag = (Pagamento) obj;
				// abilito la cancellazione del pagamento solo nel caso in cui il pagamento sia di un acconto
				getDeleteCommand().setEnabled(isAccontoLiquidazione(pag));
			}
		}
	}
}
