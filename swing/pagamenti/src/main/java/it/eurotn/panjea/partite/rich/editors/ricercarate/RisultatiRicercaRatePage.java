/**
 *
 */
package it.eurotn.panjea.partite.rich.editors.ricercarate;

import it.eurotn.panjea.anagrafica.domain.Anagrafica;
import it.eurotn.panjea.anagrafica.domain.lite.AnagraficaLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.commands.OpenAreeDocumentoCommand;
import it.eurotn.panjea.partite.rich.commands.RiemettiRataCommand;
import it.eurotn.panjea.partite.util.ParametriRicercaRate;
import it.eurotn.panjea.rate.domain.Rata;
import it.eurotn.panjea.rate.domain.Rata.StatoRata;
import it.eurotn.panjea.rate.rich.bd.IRateBD;
import it.eurotn.panjea.rich.components.JecSplitPane;
import it.eurotn.panjea.rich.factory.PanjeaComponentFactory;
import it.eurotn.panjea.tesoreria.rich.bd.ITesoreriaBD;
import it.eurotn.panjea.tesoreria.util.RataRiemessa;
import it.eurotn.panjea.tesoreria.util.SituazioneRata;
import it.eurotn.panjea.tesoreria.util.SituazioneRata.StatoCarrello;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.util.PanjeaEJBUtil;

import java.awt.BorderLayout;
import java.awt.Window;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.dialog.InputApplicationDialog;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.closure.Closure;

import com.jidesoft.grid.TableModelWrapperUtils;
import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;
import com.jidesoft.swing.JideTabbedPane;

/**
 * Aggiorna lo stato dei risultati rate.
 * 
 * @author Leonardo
 */
public class RisultatiRicercaRatePage extends AbstractTablePageEditor<SituazioneRata> implements InitializingBean,
		Observer {

	private class CarrelloTabChangeListener implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {
			if (getTable().getSelectedObject() != null) {
				update(null, getTable().getSelectedObject());
			}
		}
	}

	private class CreaAreaPartitaPerPagamentiInterceptor implements ActionCommandInterceptor {

		@Override
		public void postExecution(ActionCommand actioncommand) {
			loadData();
		}

		@Override
		public boolean preExecution(ActionCommand actioncommand) {
			return true;
		}
	}

	/**
	 * Command per creare i pagamenti dalle rate selezionate.
	 * 
	 * @author Leonardo
	 */
	private class CreaPagamentiDaRateCommand extends ActionCommand {

		private static final String COMMAND_ID = "creaPagamentiDaRateCommand";

		/**
		 * Costruttore.
		 */
		public CreaPagamentiDaRateCommand() {
			super(COMMAND_ID);
			CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
					CommandConfigurer.class);
			this.setSecurityControllerId("risultatiRicercaRatePage.controller");
			c.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			if (getTable().getTable().getCellEditor() != null) {
				getTable().getTable().getCellEditor().stopCellEditing();
			}

			for (SituazioneRata situazioneRata : getTable().getVisibleObjects()) {
				if (situazioneRata.getStatoCarrello() == StatoCarrello.SELEZIONABILE) {

					situazioneRata.setStatoCarrello(StatoCarrello.AGGIUNTO_CARRELLO);
					boolean updateSituazioneRata = carrelloPagamentiTablePage.updateSituazioneRata(situazioneRata);
					if (!updateSituazioneRata) {
						situazioneRata.setStatoCarrello(StatoCarrello.SELEZIONABILE);
						break;
					}
				}
			}
			splitPane.expand(0.7d);
		}
	}

	private class EditNotaRataCommand extends ActionCommand {

		public static final String COMMAND_ID = "editNotaRataCommand";

		private IRateBD rateBD;

		/**
		 * Costruttore.
		 */
		public EditNotaRataCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
			rateBD = RcpSupport.getBean("rateBD");
		}

		@Override
		protected void doExecuteCommand() {
			final SituazioneRata situazioneRata = getTable().getSelectedObject();

			if (situazioneRata == null) {
				return;
			}

			final InputApplicationDialog dialog = new InputApplicationDialog("Modifica note della rata", (Window) null);
			dialog.setInputField(new JTextField(situazioneRata.getRata().getNote(), 50));
			dialog.setInputLabelMessage("Note");
			dialog.setFinishAction(new Closure() {

				@Override
				public Object call(Object obj) {
					String note = ((JTextField) dialog.getInputField()).getText();

					Rata rata = tesoreriaBD.caricaRata(situazioneRata.getRata().getId());
					rata.setNote(note);
					rata = rateBD.salvaRataNoCheck(rata);

					situazioneRata.setNoteRata(note);
					getTable().replaceOrAddRowObject(situazioneRata, situazioneRata, RisultatiRicercaRatePage.this);
					return null;
				}
			});
			dialog.showDialog();

		}

	}

	private class OpenAreeDocumentoCommandInterceptor implements ActionCommandInterceptor {

		@Override
		public void postExecution(ActionCommand arg0) {
		}

		@Override
		public boolean preExecution(ActionCommand command) {
			SituazioneRata situazioneRata = getTable().getSelectedObject();
			if (situazioneRata != null) {
				command.addParameter(OpenAreeDocumentoCommand.PARAM_ID_DOCUMENTO, situazioneRata.getDocumento().getId());
				return true;
			}
			return false;
		}
	}

	private class OpenRicercaCompensazioneEntitaCommand extends ActionCommand {

		public static final String COMMAND_ID = "openRicercaCompensazioneEntitaCommand";

		/**
		 * Costruttore.
		 */
		public OpenRicercaCompensazioneEntitaCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			final SituazioneRata situazioneRata = getTable().getSelectedObject();

			if (situazioneRata == null) {
				return;
			}

			AnagraficaLite anagraficaLite = situazioneRata.getEntita().getAnagrafica();
			// ricarico l'anagrafica perchè nella situazione rata ho solo id e denominazione mentre nei parametri di
			// ricerca faccio vedere anche codice fiscale/partica iva
			Anagrafica anagrafica = anagraficaBD.caricaAnagrafica(anagraficaLite.getId());
			anagraficaLite.setCodiceFiscale(anagrafica.getCodiceFiscale());
			anagraficaLite.setPartiteIVA(anagrafica.getPartiteIVA());

			ParametriRicercaRate parametriRicercaRateCompensazione = new ParametriRicercaRate();
			parametriRicercaRateCompensazione.setCompensazione(true);
			parametriRicercaRateCompensazione.setAnagrafica(anagraficaLite);
			parametriRicercaRateCompensazione.setEffettuaRicerca(true);
			OpenEditorEvent event = new OpenEditorEvent(parametriRicercaRateCompensazione);
			Application.instance().getApplicationContext().publishEvent(event);
		};
	}

	private class RiemettiRataCommandInterceptor implements ActionCommandInterceptor {

		@Override
		public void postExecution(ActionCommand arg0) {
			if (((RiemettiRataCommand) arg0).isRiemesse()) {
				RisultatiRicercaRatePage.this.refreshData();
			}
		}

		@Override
		public boolean preExecution(ActionCommand arg0) {
			List<SituazioneRata> situazioniRata = getTable().getSelectedObjects();
			if (situazioniRata != null && situazioniRata.size() > 0) {
				List<RataRiemessa> rate = new ArrayList<>();
				for (SituazioneRata situazioneRata : situazioniRata) {
					// se ho selezionato rate non insolute, non posso riemetterle
					if (situazioneRata.getStatoRata().equals(StatoRata.IN_RIASSEGNAZIONE)
							|| situazioneRata.getStatoRata().equals(StatoRata.RIEMESSA)) {
						RataRiemessa rataRiemessa = new RataRiemessa(situazioneRata.getRata());
						EntitaLite el = new EntitaLite();
						PanjeaEJBUtil.copyProperties(el, situazioneRata.getEntita());
						rataRiemessa.getAreaRate().getDocumento().setEntita(el);
						rate.add(rataRiemessa);
					}
				}
				arg0.addParameter(RiemettiRataCommand.PARAM_RATE, rate);
				return true;
			}
			return false;
		}
	}

	private class RimuoviPagamentoCommandInterceptor implements ActionCommandInterceptor {
		@Override
		public void postExecution(ActionCommand actioncommand) {
			for (SituazioneRata situazioneRata : getTableModel().getObjects()) {
				situazioneRata.setStatoCarrello(StatoCarrello.SELEZIONABILE);
			}
			aggiornaStatoRate(getTableModel().getObjects());

			getTableModel().fireTableDataChanged();
		}

		@Override
		public boolean preExecution(ActionCommand actioncommand) {
			return true;
		}
	}

	// publics
	public static final String PAGE_ID = "risultatiRicercaRatePage";
	public static final String KEY_ICON_NON_SELEZIONABILE = "nonSelezionabile.icon";
	public static final String KEY_ICON_SELEZIONABILE = "selezionabile.icon";
	public static final String KEY_ICON_AGGIUNTA = "aggiuntoCarrello.icon";
	public static final String KEY_MSG_NON_SELEZIONABILE = PAGE_ID + ".nonSelezionabile.label";
	public static final String KEY_MSG_SELEZIONABILE = PAGE_ID + ".selezionabile.label";
	public static final String KEY_MSG_AGGIUNTA = PAGE_ID + ".aggiuntoCarrello.label";

	// privates
	private static final String KEY_CARRELLO_TAB_TITLE = PAGE_ID + ".carrello.tab.title";
	private static final String ICON_CARRELLO_TAB_TITLE = PAGE_ID + ".carrello.tab.icon";
	private static final String KEY_PAGAMENTI_TAB_TITLE = PAGE_ID + ".pagamenti.tab.title";
	private static final String ICON_PAGAMENTI_TAB_TITLE = PAGE_ID + ".pagamenti.tab.icon";
	private static final String KEY_SOLLECITI_TAB_TITLE = PAGE_ID + ".solleciti.tab.title";
	private static final String ICON_SOLLECITI_TAB_TITLE = PAGE_ID + ".solleciti.tab.icon";

	// params
	private ParametriRicercaRate parametriRicercaRate = null;

	// bds
	private ITesoreriaBD tesoreriaBD = null;
	private IAnagraficaBD anagraficaBD = null;
	private IRateBD rateBD = null;

	// components
	private CarrelloPagamentiTablePage carrelloPagamentiTablePage = null;
	private PagamentiSituazioneRataComponent pagamentiSituazioneRataComponent = null;
	private SollecitiPagamentiRataComponent sollecitiPagamentiRataComponent = null;
	private JecSplitPane splitPane = null;
	private JideTabbedPane carrelloTabbedPane = null;

	// commands
	private AbstractCommand creaPagamentiDaRateCommand = null;
	private SelectSituazioneRateCommand selezionaTuttoCommand = null;
	private SelectSituazioneRateCommand deselezionaTuttoCommand = null;
	private EditNotaRataCommand editNotaRataCommand = null;
	private OpenRicercaCompensazioneEntitaCommand ricercaCompensazioneEntitaCommand = null;
	private RiemettiRataCommand riemettiRataCommand = null;
	private OpenAreeDocumentoCommand openAreeDocumentoCommand = null;

	// interceptors
	private RiemettiRataCommandInterceptor riemettiRataCommandInterceptor = null;
	private OpenAreeDocumentoCommandInterceptor openAreeDocumentoCommandInterceptor = null;
	private RimuoviPagamentoCommandInterceptor rimuoviPagamentoCommandInterceptor;
	private CreaAreaPartitaPerPagamentiInterceptor creaAreaPartitaPerPagamentiInterceptor;

	// listeners
	private CarrelloTabChangeListener carrelloTabChangeListener;

	/**
	 * Costruttore.
	 */
	public RisultatiRicercaRatePage() {
		super(PAGE_ID, new RisultatiRicercaRateTableModel(PAGE_ID));
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		openAreeDocumentoCommand = new OpenAreeDocumentoCommand();
		getTable().setPropertyCommandExecutor(getOpenAreeDocumentoCommand());

		// inizializzo il model con la lista di rate e pagamenti
		org.springframework.util.Assert.notNull(tesoreriaBD, "tesoreriaBD cannot be null!");
		org.springframework.util.Assert.notNull(anagraficaBD, "anagraficaBD cannot be null!");
		org.springframework.util.Assert.notNull(rateBD, "rateBD cannot be null!");

		org.springframework.util.Assert.notNull(carrelloPagamentiTablePage,
				"carrelloPagamentiTablePage cannot be null!");
		org.springframework.util.Assert.notNull(pagamentiSituazioneRataComponent,
				"pagamentiSituazioneRataComponent cannot be null!");
		org.springframework.util.Assert.notNull(sollecitiPagamentiRataComponent,
				"sollecitiPagamentiRataComponent cannot be null!");

		// listener per aggiornare la situazione rate dopo aver cancellato i
		// pagamenti dal carrello
		carrelloPagamentiTablePage.getRimuoviPagamentoCommand().addCommandInterceptor(
				getRimuoviPagamentoCommandInterceptor());
		carrelloPagamentiTablePage.getCreaAreaPartitaPerPagamentiCommand().addCommandInterceptor(
				getCreaAreaPartitaPerPagamentiInterceptor());

		getTable().addExternalPopupContextMenu(getEditNotaRataCommand());
		getTable().addExternalPopupContextMenu(getRicercaCompensazioneEntitaCommand());
		getTable().addExternalPopupContextMenu(getRiemettiRataCommand());

		carrelloPagamentiTablePage.setTableModelRate(getTableModel());
	}

	/**
	 * Aggiorna lo stato delle rate in base al contenuto del carrello dei pagamenti.
	 * 
	 * @param rate
	 *            rate da aggiornare
	 */
	private void aggiornaStatoRate(List<SituazioneRata> rate) {
		for (PagamentoPM pagamentoPM : carrelloPagamentiTablePage.getTable().getRows()) {
			int index = rate.indexOf(pagamentoPM.getRata());
			if (index > -1) {
				rate.get(index).setStatoCarrello(StatoCarrello.AGGIUNTO_CARRELLO);
			}
		}
	}

	/**
	 * Crea i controlli per il pannello della legenda.
	 * 
	 * @return pannello creato
	 */
	protected JPanel createLegendaPanel() {
		JPanel panel = getComponentFactory().createPanel(new BorderLayout());
		LegendaRisultatiRicercaRatePanel legenda = new LegendaRisultatiRicercaRatePanel();
		panel.add(legenda, BorderLayout.LINE_START);
		return panel;
	}

	/**
	 * Crea i controlli per la gestione del carrello e il dettaglio dei pagamenti della rata selezionata.
	 * 
	 * @return controlli creati
	 */
	private JComponent createPagamentiECarrelloControl() {
		carrelloTabbedPane = (JideTabbedPane) getComponentFactory().createTabbedPane();
		carrelloTabbedPane.setTabResizeMode(JideTabbedPane.RESIZE_MODE_FIT);
		carrelloTabbedPane.setColorTheme(JideTabbedPane.COLOR_THEME_VSNET);
		carrelloTabbedPane.setTabShape(JideTabbedPane.SHAPE_BOX);
		carrelloTabbedPane.setBoldActiveTab(true);
		carrelloTabbedPane.setTabPlacement(SwingConstants.LEFT);
		carrelloTabbedPane.setBorder(BorderFactory.createEmptyBorder());
		if (PanjeaSwingUtil.hasPermission("gestionePagamenti")) {
			carrelloTabbedPane.addTab(RcpSupport.getMessage(KEY_CARRELLO_TAB_TITLE),
					RcpSupport.getIcon(ICON_CARRELLO_TAB_TITLE), carrelloPagamentiTablePage.getControl());
			carrelloPagamentiTablePage.setCarrelloSplitPane(splitPane);
		}
		carrelloTabbedPane.addTab(RcpSupport.getMessage(KEY_PAGAMENTI_TAB_TITLE),
				RcpSupport.getIcon(ICON_PAGAMENTI_TAB_TITLE), pagamentiSituazioneRataComponent.getControl());
		carrelloTabbedPane.addTab(RcpSupport.getMessage(KEY_SOLLECITI_TAB_TITLE),
				RcpSupport.getIcon(ICON_SOLLECITI_TAB_TITLE), sollecitiPagamentiRataComponent.getControl());

		carrelloTabbedPane.addChangeListener(getCarrelloTabChangeListener());
		return carrelloTabbedPane;
	}

	@Override
	public JComponent createToolbar() {
		JPanel panel = getComponentFactory().createPanel(new BorderLayout());
		LegendaRisultatiRicercaRatePanel legenda = new LegendaRisultatiRicercaRatePanel();
		panel.add(legenda, BorderLayout.WEST);
		panel.add(super.createToolbar(), BorderLayout.CENTER);
		return panel;
	}

	@Override
	public void dispose() {
		if (riemettiRataCommand != null) {
			riemettiRataCommand.removeCommandInterceptor(getRiemettiRataCommandInterceptor());
		}
		if (openAreeDocumentoCommand != null) {
			openAreeDocumentoCommand.removeCommandInterceptor(getOpenAreeDocumentoCommandInterceptor());
		}
		if (carrelloPagamentiTablePage != null && carrelloPagamentiTablePage.getRimuoviPagamentoCommand() != null) {
			carrelloPagamentiTablePage.getRimuoviPagamentoCommand().removeCommandInterceptor(
					getRimuoviPagamentoCommandInterceptor());
		}
		if (carrelloPagamentiTablePage != null
				&& carrelloPagamentiTablePage.getCreaAreaPartitaPerPagamentiCommand() != null) {
			carrelloPagamentiTablePage.getCreaAreaPartitaPerPagamentiCommand().removeCommandInterceptor(
					getCreaAreaPartitaPerPagamentiInterceptor());
		}
		if (carrelloTabbedPane != null) {
			carrelloTabbedPane.removeChangeListener(getCarrelloTabChangeListener());
		}
		super.dispose();
	}

	/**
	 * @return carrelloTabChangeListener
	 */
	private CarrelloTabChangeListener getCarrelloTabChangeListener() {
		if (carrelloTabChangeListener == null) {
			carrelloTabChangeListener = new CarrelloTabChangeListener();
		}
		return carrelloTabChangeListener;
	}

	@Override
	public AbstractCommand[] getCommands() {
		return new AbstractCommand[] { getCreaPagamentiDaRateCommand() };
	}

	@Override
	public JComponent getControl() {
		if (splitPane == null) {
			splitPane = (JecSplitPane) ((PanjeaComponentFactory) getComponentFactory())
					.createSplitPane(JSplitPane.VERTICAL_SPLIT);

			org.springframework.util.Assert.notNull(carrelloPagamentiTablePage,
					"CarrelloPagamentiTablePage cannot be null!");

			splitPane.setTopComponent(super.getControl());
			splitPane.setBottomComponent(createPagamentiECarrelloControl());
			splitPane.collapse();
			getTable().addSelectionObserver(this);

			splitPane.expand(0.5);

			new RicercaRateController(this, carrelloPagamentiTablePage);
		}
		splitPane.collapse();
		return splitPane;
	}

	/**
	 * @return creaAreaPartitaPerPagamentiInterceptor
	 */
	private CreaAreaPartitaPerPagamentiInterceptor getCreaAreaPartitaPerPagamentiInterceptor() {
		if (creaAreaPartitaPerPagamentiInterceptor == null) {
			creaAreaPartitaPerPagamentiInterceptor = new CreaAreaPartitaPerPagamentiInterceptor();
		}
		return creaAreaPartitaPerPagamentiInterceptor;
	}

	/**
	 * @return comando per la creazione dei pagamenti.
	 */
	private AbstractCommand getCreaPagamentiDaRateCommand() {
		if (creaPagamentiDaRateCommand == null) {
			creaPagamentiDaRateCommand = new CreaPagamentiDaRateCommand();
		}
		return creaPagamentiDaRateCommand;
	}

	/**
	 * @return comando per deselezionare tutte le rate.
	 */
	public SelectSituazioneRateCommand getDeselezionaTuttoCommand() {
		if (deselezionaTuttoCommand == null) {
			deselezionaTuttoCommand = new SelectSituazioneRateCommand(SelectSituazioneRateCommand.UNSELECT_COMMAND_ID,
					false, this);
		}
		return deselezionaTuttoCommand;
	}

	/**
	 * @return Returns the editNotaRataCommand.
	 */
	public EditNotaRataCommand getEditNotaRataCommand() {
		if (editNotaRataCommand == null) {
			editNotaRataCommand = new EditNotaRataCommand();
		}
		return editNotaRataCommand;
	}

	/**
	 * @return the openAreaDocumentoRataCommand
	 */
	public OpenAreeDocumentoCommand getOpenAreeDocumentoCommand() {
		if (openAreeDocumentoCommand != null) {
			openAreeDocumentoCommand = new OpenAreeDocumentoCommand();
			openAreeDocumentoCommand.addCommandInterceptor(getOpenAreeDocumentoCommandInterceptor());
		}
		return openAreeDocumentoCommand;
	}

	/**
	 * @return the OpenAreeDocumentoCommandInterceptor
	 */
	private OpenAreeDocumentoCommandInterceptor getOpenAreeDocumentoCommandInterceptor() {
		if (openAreeDocumentoCommandInterceptor == null) {
			openAreeDocumentoCommandInterceptor = new OpenAreeDocumentoCommandInterceptor();
		}
		return openAreeDocumentoCommandInterceptor;
	}

	/**
	 * @return the ricercaCompensazioneEntitaCommand
	 */
	private OpenRicercaCompensazioneEntitaCommand getRicercaCompensazioneEntitaCommand() {
		if (ricercaCompensazioneEntitaCommand == null) {
			ricercaCompensazioneEntitaCommand = new OpenRicercaCompensazioneEntitaCommand();
		}
		return ricercaCompensazioneEntitaCommand;
	}

	/**
	 * @return riemettiRataCommand
	 */
	private RiemettiRataCommand getRiemettiRataCommand() {
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
	 * @return rimuoviPagamentoCommandInterceptor
	 */
	private RimuoviPagamentoCommandInterceptor getRimuoviPagamentoCommandInterceptor() {
		if (rimuoviPagamentoCommandInterceptor == null) {
			rimuoviPagamentoCommandInterceptor = new RimuoviPagamentoCommandInterceptor();
		}
		return rimuoviPagamentoCommandInterceptor;
	}

	/**
	 * @return comando per selezionare tutte le rate.
	 */
	public SelectSituazioneRateCommand getSelezionaTuttoCommand() {
		if (selezionaTuttoCommand == null) {
			selezionaTuttoCommand = new SelectSituazioneRateCommand(SelectSituazioneRateCommand.SELECT_COMMAND_ID,
					true, this);
		}
		return selezionaTuttoCommand;
	}

	/**
	 * 
	 * @return tableModel della pagina
	 */
	RisultatiRicercaRateTableModel getTableModel() {
		RisultatiRicercaRateTableModel tableModel = (RisultatiRicercaRateTableModel) TableModelWrapperUtils
				.getActualTableModel(getTable().getTable().getModel());
		return tableModel;
	}

	/**
	 * Carica le rate in base ai parametri di ricerca.
	 * 
	 * @return situazione rate caricate
	 */
	private List<SituazioneRata> loadSituazioneRate() {
		List<SituazioneRata> rate = new ArrayList<SituazioneRata>();
		if (this.parametriRicercaRate != null && this.parametriRicercaRate.isEffettuaRicerca()) {
			rate = tesoreriaBD.ricercaRate(parametriRicercaRate);
		}
		if (this.parametriRicercaRate != null && !this.parametriRicercaRate.isEffettuaRicerca()
				&& this.parametriRicercaRate.getRate() != null) {
			rate = this.parametriRicercaRate.getRate();
		}
		getTableModel().setCompensazione(this.parametriRicercaRate.getCompensazione());
		return rate;
	}

	@Override
	public Collection<SituazioneRata> loadTableData() {
		// aggiungo le rate della ricerca
		carrelloPagamentiTablePage.loadData();
		return loadSituazioneRate();
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public void preSetFormObject(Object object) {
		ParametriRicercaRate nuoviParametri = (ParametriRicercaRate) object;
		if (nuoviParametri != null && parametriRicercaRate != null
				&& !parametriRicercaRate.getCompensazione().equals(nuoviParametri.getCompensazione())) {
			carrelloPagamentiTablePage.setRows(new ArrayList<PagamentoPM>());
		}
		super.preSetFormObject(object);
	}

	@Override
	public void processTableData(Collection<SituazioneRata> results) {
		// metto a stato <in carrello> tutte le rate in carrello
		aggiornaStatoRate(new ArrayList<SituazioneRata>(results));
		splitPane.collapse();
		super.processTableData(results);
	}

	@Override
	public Collection<SituazioneRata> refreshTableData() {
		return loadSituazioneRate();
	}

	/**
	 * @param anagraficaBD
	 *            the anagraficaBD to set
	 */
	public void setAnagraficaBD(IAnagraficaBD anagraficaBD) {
		this.anagraficaBD = anagraficaBD;
	}

	/**
	 * @param carrelloPagamentiTablePage
	 *            the carrelloPagamentiTablePage to set
	 */
	public void setCarrelloPagamentiTablePage(CarrelloPagamentiTablePage carrelloPagamentiTablePage) {
		this.carrelloPagamentiTablePage = carrelloPagamentiTablePage;
	}

	@Override
	public void setFormObject(Object object) {
		parametriRicercaRate = (ParametriRicercaRate) object;
		if (parametriRicercaRate != null && parametriRicercaRate.getParametriCreazioneAreaChiusure() != null) {
			carrelloPagamentiTablePage.setFormObject(parametriRicercaRate.getParametriCreazioneAreaChiusure());
		}
	}

	/**
	 * @param pagamentiSituazioneRataComponent
	 *            the pagamentiSituazioneRataComponent to set
	 */
	public void setPagamentiSituazioneRataComponent(PagamentiSituazioneRataComponent pagamentiSituazioneRataComponent) {
		this.pagamentiSituazioneRataComponent = pagamentiSituazioneRataComponent;
	}

	/**
	 * @param rateBD
	 *            the rateBD to set
	 */
	public void setRateBD(IRateBD rateBD) {
		this.rateBD = rateBD;
	}

	/**
	 * @param sollecitiPagamentiRataComponent
	 *            the pagamentiSituazioneRataComponent to set
	 */
	public void setSollecitiPagamentiRataComponent(SollecitiPagamentiRataComponent sollecitiPagamentiRataComponent) {
		this.sollecitiPagamentiRataComponent = sollecitiPagamentiRataComponent;
	}

	/**
	 * @param tesoreriaBD
	 *            the tesoreriaBD to set
	 */
	public void setTesoreriaBD(ITesoreriaBD tesoreriaBD) {
		this.tesoreriaBD = tesoreriaBD;
	}

	@Override
	public void update(Observable o, Object arg) {
		// carico solamente ho visualizzato il pannello dei pagamenti

		getRiemettiRataCommand().setEnabled(false);

		if (!(carrelloTabbedPane.getSelectedIndex() == 0)) {
			pagamentiSituazioneRataComponent.updateControl((SituazioneRata) arg);
			sollecitiPagamentiRataComponent.updateControl((SituazioneRata) arg);
		}
		SituazioneRata situazioneRata = getTable().getSelectedObject();
		if (situazioneRata != null) {
			StatoRata statoRata = situazioneRata.getStatoRata();
			getRiemettiRataCommand().setEnabled(
					statoRata.equals(StatoRata.IN_RIASSEGNAZIONE) || statoRata.equals(StatoRata.RIEMESSA));
		}
	}

}
