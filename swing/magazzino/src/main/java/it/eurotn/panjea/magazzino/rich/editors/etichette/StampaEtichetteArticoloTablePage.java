package it.eurotn.panjea.magazzino.rich.editors.etichette;

import foxtrot.AsyncTask;
import foxtrot.AsyncWorker;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.rich.search.EntitaByTipoSearchObject;
import it.eurotn.panjea.magazzino.domain.etichetta.EtichettaArticolo;
import it.eurotn.panjea.magazzino.domain.etichetta.LayoutStampaEtichette;
import it.eurotn.panjea.magazzino.domain.etichetta.ParametriStampaEtichetteArticolo;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.rich.forms.PanjeaFormGuard;
import it.eurotn.panjea.rich.stampe.ILayoutStampeManager;
import it.eurotn.panjea.rich.stampe.LayoutStampeManager;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.table.EditFrame;
import it.eurotn.rich.form.PanjeaFormModelHelper;
import it.eurotn.rich.report.ReportManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;

import org.springframework.binding.form.ValidatingFormModel;
import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.form.FormGuard;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.binding.BindingFactoryProvider;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.list.ComboBoxListModel;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.pivot.AggregateTable;

/**
 * Page per la stampa delle etichette articolo.
 * 
 * @author Leonardo
 */
public class StampaEtichetteArticoloTablePage extends AbstractTablePageEditor<EtichettaArticolo> {

	private class AggiornaDatiCommandInterceptor implements ActionCommandInterceptor {

		@Override
		public void postExecution(ActionCommand command) {
			// refresh della tabella
			((AggregateTable) getTable().getTable()).getAggregateTableModel().fireTableDataChanged();
			getTable().selectRow(0, null);
			if (getTable().getRows().size() > 0) {
				getEditPages().get(EditFrame.DEFAULT_OBJECT_CLASS_NAME).setFormObject(getTable().getRows().get(0));
			}
		}

		@Override
		public boolean preExecution(ActionCommand command) {
			if (getTable().getRows().isEmpty()) {
				return false;
			}
			command.addParameter(AggiornaDatiCommand.PARAM_ETICHETTE_ARTICOLO, getTable().getRows());
			return true;
		}
	}

	private class StampaCommandInterceptor implements ActionCommandInterceptor {

		@Override
		public void postExecution(ActionCommand command) {
		}

		@Override
		public boolean preExecution(ActionCommand command) {
			if (getTable().getRows().isEmpty()) {
				return false;
			}

			// pulisco tutti gli eventuali esiti di stampa precedenti
			for (EtichettaArticolo etichettaArticolo : getTable().getRows()) {
				etichettaArticolo.setEsitoStampa("");
			}

			command.addParameter(StampaEtichetteCommand.PARAM_TABLE_ETICHETTE_ARTICOLO, getTable());
			command.addParameter(StampaEtichetteCommand.PARAM_PARAMETRI_ETICHETTE_ARTICOLO,
					parametriStampaEtichetteArticolo);
			command.addParameter(StampaEtichetteCommand.PARAM_PRINTER_NAME, etichettePrinterComboBox.getSelectedItem());
			return !getTable().getVisibleObjects().isEmpty();
		}
	}

	private class SvuotaTabellaCommand extends ApplicationWindowAwareCommand {

		public static final String COMMAND_ID = "svuotaTabellaCommand";

		/**
		 * Costruttore.
		 */
		public SvuotaTabellaCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			setRows(new ArrayList<EtichettaArticolo>());
		}
	}

	public static final String PAGE_ID = "stampaEtichetteArticoloTablePage";
	private ReportManager reportManager = null;
	private IMagazzinoDocumentoBD magazzinoDocumentoBD = null;
	private ILayoutStampeManager layoutStampeManager = null;

	private StampaEtichetteLayoutManager stampaEtichetteLayoutManager = null;
	private ParametriStampaEtichetteArticolo parametriStampaEtichetteArticolo = null;
	private ValidatingFormModel formModelParametriStampa = null;
	private final ValueHolder reportEtichetteValueHolder;
	private PanjeaFormGuard stampaCommandFormGuarded = null;

	private JComboBox<String> etichettePrinterComboBox = null;
	private StampaEtichetteCommand stampaCommand = null;
	private AggiornaDatiCommand aggiornaDatiCommand = null;
	private SvuotaTabellaCommand svuotaTabellaCommand = null;

	private StampaCommandInterceptor stampaCommandInterceptor = null;
	private AggiornaDatiCommandInterceptor aggiornaDatiCommandInterceptor = null;

	/**
	 * Costruttore.
	 */
	public StampaEtichetteArticoloTablePage() {
		super(PAGE_ID, new StampaEtichetteArticoloTableModel());
		parametriStampaEtichetteArticolo = new ParametriStampaEtichetteArticolo();
		reportEtichetteValueHolder = new ValueHolder(Collections.emptySet());
		stampaEtichetteLayoutManager = new StampaEtichetteLayoutManager(getTable(), parametriStampaEtichetteArticolo);
		getTable().getTableLayoutView().setLayoutManager(stampaEtichetteLayoutManager);
	}

	/**
	 * Aggiunge una proprietà tipiEntita al formModel e gli assegna un oggetto List<TipoEntita> che contiente
	 * TipoEntita.CLIENTE.
	 * 
	 * @param formModel
	 *            il formModel da modificare
	 */
	private void addTipoEntitaCliente(ValidatingFormModel formModel) {
		// aggiungo la finta proprietà tipiEntita per far si che la search text mostri solo i clienti
		List<TipoEntita> tipiEntita = new ArrayList<TipoEntita>();
		tipiEntita.add(TipoEntita.CLIENTE);

		ValueModel tipiEntitaValueModel = new ValueHolder(tipiEntita);
		DefaultFieldMetadata tipiEntitaData = new DefaultFieldMetadata(formModel, new FormModelMediatingValueModel(
				tipiEntitaValueModel), List.class, true, null);
		formModel.add("tipoEntita", tipiEntitaValueModel, tipiEntitaData);
	}

	/**
	 * Carica da jasperReport tutti i layout presenti per le etichette.<br/>
	 * i layout sono nella cartella di jasper {@link ParametriStampaEtichetteArticolo#FOLDER_REPORT_PATH}
	 */
	private void caricaLayoutEtichette() {
		AsyncWorker.post(new AsyncTask() {

			@Override
			public void failure(Throwable arg0) {
				Set<LayoutStampaEtichette> setReportsPM = new HashSet<LayoutStampaEtichette>();
				LayoutStampaEtichette errorePM = new LayoutStampaEtichette("Errore nel caricare la lista dei report.");
				setReportsPM.add(errorePM);
				reportEtichetteValueHolder.setValue(errorePM);
			}

			@Override
			public Object run() throws Exception {
				return reportManager.listReport(parametriStampaEtichetteArticolo.getReportPath());
			}

			@SuppressWarnings("unchecked")
			@Override
			public void success(Object arg0) {

				Set<LayoutStampaEtichette> etichettePerEntita = new HashSet<LayoutStampaEtichette>();

				try {
					Set<String> setReports = (Set<String>) arg0;
					for (String report : setReports) {
						LayoutStampaEtichette layoutEtichetta = new LayoutStampaEtichette(report);
						etichettePerEntita.add(layoutEtichetta);
					}
				} catch (Exception ex) {
					etichettePerEntita.add(new LayoutStampaEtichette("Errore nel caricare la lista dei report."));
				}

				reportEtichetteValueHolder.setValue(etichettePerEntita);
			}
		});
	}

	@Override
	public void dispose() {
		super.dispose();
		if (stampaCommandFormGuarded != null) {
			stampaCommandFormGuarded.removeGuarded(stampaCommand);
			stampaCommandFormGuarded = null;
		}
		if (aggiornaDatiCommand != null) {
			aggiornaDatiCommand.removeCommandInterceptor(getAggiornaDatiCommandInterceptor());
			aggiornaDatiCommand = null;
		}
		aggiornaDatiCommand = null;
		if (stampaCommand != null) {
			stampaCommand.removeCommandInterceptor(stampaCommandInterceptor);
			stampaCommand = null;
		}
	}

	/**
	 * @return command per aggiornare i dati nella tabella
	 */
	private AggiornaDatiCommand getAggiornaDatiCommand() {
		if (aggiornaDatiCommand == null) {
			aggiornaDatiCommand = new AggiornaDatiCommand(magazzinoDocumentoBD, formModelParametriStampa);
			aggiornaDatiCommand.addCommandInterceptor(getAggiornaDatiCommandInterceptor());
			new FormGuard(formModelParametriStampa, aggiornaDatiCommand, FormGuard.ON_ISDIRTY & FormGuard.ON_NOERRORS);
		}
		return aggiornaDatiCommand;
	}

	/**
	 * @return aggiornaDatiCommandInterceptor
	 */
	private AggiornaDatiCommandInterceptor getAggiornaDatiCommandInterceptor() {
		if (aggiornaDatiCommandInterceptor == null) {
			aggiornaDatiCommandInterceptor = new AggiornaDatiCommandInterceptor();
		}
		return aggiornaDatiCommandInterceptor;
	}

	/**
	 * Crea e restituisce il SearchTextBinding per l' entita aggiungendo il pulsante per la richiesta della situazione
	 * rate.
	 * 
	 * @param bf
	 *            il binding factory
	 * @return Binding
	 */
	private Binding getEntitaBinding(PanjeaSwingBindingFactory bf) {
		Binding bindingEntita = bf.createBoundSearchText("entita",
				new String[] { "codice", "anagrafica.denominazione" }, new String[] { "tipoEntita" },
				new String[] { EntitaByTipoSearchObject.TIPI_ENTITA_LIST_KEY });
		SearchPanel searchPanel = (SearchPanel) bindingEntita.getControl();
		searchPanel.getTextFields().get("codice").setColumns(5);
		searchPanel.getTextFields().get("anagrafica.denominazione").setColumns(20);
		return bindingEntita;
	}

	/**
	 * Carica dal file di configurazione la stampante associata alla stampa etichette.
	 * 
	 * @return nome della stampante, <code>null</code> se non esiste
	 */
	private String getEtichettePrinterName() {

		Properties properties = layoutStampeManager.loadPrinterConfigFile();

		return properties.getProperty(LayoutStampeManager.ETICHETTE_PRINTER_NAME_KEY, null);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public JComponent getHeaderControl() {
		String formParametriStampaId = "parametriStampaEtichetteArticolo";
		formModelParametriStampa = (ValidatingFormModel) PanjeaFormModelHelper.createFormModel(
				parametriStampaEtichetteArticolo, false, formParametriStampaId);
		BindingFactoryProvider bindingFactoryProvider = (BindingFactoryProvider) getService(BindingFactoryProvider.class);
		PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) bindingFactoryProvider
				.getBindingFactory(formModelParametriStampa);
		FormLayout layout = new FormLayout(
				"right:pref, 4dlu, left:pref, 10dlu,right:pref, 4dlu,left:pref,10dlu,right:pref, 4dlu,130dlu:g,10dlu,right:pref, 4dlu,left:pref,4dlu,left:pref:g",
				"default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");
		builder.setLabelAttributes("r, c");

		((StampaEtichetteArticoloPage) getEditPages().get(EditFrame.DEFAULT_OBJECT_CLASS_NAME))
				.setFormModelParametriStampa(formModelParametriStampa);

		Binding bindingListino = bf.createBoundSearchText("listino", new String[] { "codice" });
		builder.addLabel("listino");
		SearchPanel searchListinoPanel = (SearchPanel) builder.addBinding(bindingListino, 3);
		searchListinoPanel.getTextFields().get("codice").setColumns(15);

		builder.addPropertyAndLabel("data", 5);

		Binding reportListBinding = bf.createBoundComboBox("reportName", reportEtichetteValueHolder, "descrizione");
		StampaEtichetteReportListCellRenderer reportsRenderer = new StampaEtichetteReportListCellRenderer();
		((JComboBox) reportListBinding.getControl()).setRenderer(reportsRenderer);
		builder.addLabel("reportName", 9);
		builder.addBinding(reportListBinding, 11);

		builder.addPropertyAndLabel("aggiungiIva", 13);
		builder.addComponent(getAggiornaDatiCommand().createButton(), 17);
		builder.nextRow();

		builder.addComponent(new JLabel("Stampante"), 1);

		ComboBoxListModel model = new ComboBoxListModel(getPrintersName());
		etichettePrinterComboBox = new JComboBox(model);
		builder.addComponent(etichettePrinterComboBox, 3);

		addTipoEntitaCliente(formModelParametriStampa);
		Binding bindingEntita = getEntitaBinding(bf);
		builder.addLabel("entita", 9);
		builder.addBinding(bindingEntita, 11);

		// String printerName;
		// try {
		// printerName =
		// settingsManagerLocal.getUserSettings().getString(GeneralSettingsPM.LABEL_PRINTER_NAME);
		// if (printerName.isEmpty()) {
		// printerName = "Nessuna stampante selezionata";
		// }
		// } catch (SettingsException e) {
		// printerName = "Nessuna stampante impostata";
		// }
		// builder.addComponent(new JLabel(printerName), 3);
		builder.addComponent(getStampaEtichetteCommand().createButton(), 17);

		JComponent pannello = builder.getPanel();
		pannello.setBorder(BorderFactory.createTitledBorder("Parametri di stampa"));
		return pannello;
	}

	/**
	 * @return the magazzinoDocumentoBD
	 */
	public IMagazzinoDocumentoBD getMagazzinoDocumentoBD() {
		return magazzinoDocumentoBD;
	}

	/**
	 * Carica i nomi delle stampanti di sistema.
	 * 
	 * @return nomi delle stampanti caricati
	 */
	private List<String> getPrintersName() {
		PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);

		List<String> printers = new ArrayList<String>();
		for (PrintService printer : printServices) {
			printers.add(printer.getName());
		}

		return printers;
	}

	@Override
	public ActionCommand getRefreshCommand() {
		// al posto del refresh command visto che non serve, metto il comando
		// per svuotare la tabella
		if (svuotaTabellaCommand == null) {
			svuotaTabellaCommand = new SvuotaTabellaCommand();
		}
		return svuotaTabellaCommand;
	}

	/**
	 * @return the reportManager
	 */
	public ReportManager getReportManager() {
		return reportManager;
	}

	/**
	 * @return StampaCommandInterceptor
	 */
	private StampaCommandInterceptor getStampaCommandInterceptor() {
		if (stampaCommandInterceptor == null) {
			stampaCommandInterceptor = new StampaCommandInterceptor();
		}
		return stampaCommandInterceptor;
	}

	/**
	 * 
	 * @return comando per la stampa delle etichette
	 */
	private StampaEtichetteCommand getStampaEtichetteCommand() {
		if (stampaCommand == null) {
			stampaCommand = new StampaEtichetteCommand(formModelParametriStampa.getValueModel("reportName"),
					reportManager);
			stampaCommand.addCommandInterceptor(getStampaCommandInterceptor());
			stampaCommandFormGuarded = new PanjeaFormGuard(formModelParametriStampa, stampaCommand,
					FormGuard.ON_ISDIRTY + FormGuard.ON_NOERRORS + FormGuard.ON_ENABLED);
		}
		return stampaCommand;
	}

	@Override
	public Collection<EtichettaArticolo> loadTableData() {
		Collection<EtichettaArticolo> result = parametriStampaEtichetteArticolo.getEtichetteArticolo();
		return result;
	}

	@Override
	public void onPostPageOpen() {

	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public Collection<EtichettaArticolo> refreshTableData() {
		return loadTableData();
	}

	@Override
	public void restoreState(Settings settings) {
		super.restoreState(settings);

		// carica dal file di configurazione la stampante associata e se esiste
		// la seleziono nella combo
		String etichettePrinter = getEtichettePrinterName();
		if (etichettePrinter != null) {
			etichettePrinterComboBox.setSelectedItem(etichettePrinter);
		}
	}

	@Override
	public void saveState(Settings settings) {
		super.saveState(settings);

		// salvo la stampante selezionata per la stampa etichette
		layoutStampeManager.salvaAssociazioneStampante(LayoutStampeManager.ETICHETTE_PRINTER_NAME_KEY,
				(String) etichettePrinterComboBox.getSelectedItem());
	}

	@Override
	public void setFormObject(Object object) {
		this.parametriStampaEtichetteArticolo = (ParametriStampaEtichetteArticolo) object;

		if (formModelParametriStampa != null) {
			formModelParametriStampa.setFormObject(parametriStampaEtichetteArticolo);
		}

		((StampaEtichetteArticoloPage) getEditPages().get(EditFrame.DEFAULT_OBJECT_CLASS_NAME))
				.setParametriStampaEtichetteArticolo(parametriStampaEtichetteArticolo);

		this.stampaEtichetteLayoutManager.setParametriStampaEtichetteArticolo(parametriStampaEtichetteArticolo);
		getTable().getTableLayoutView().updateLayoutsToolBar();
		getTable().getTableLayoutView().getLayoutManager().applica(null);

		caricaLayoutEtichette();
	}

	/**
	 * @param layoutStampeManager
	 *            The layoutStampeManager to set.
	 */
	public void setLayoutStampeManager(ILayoutStampeManager layoutStampeManager) {
		this.layoutStampeManager = layoutStampeManager;
	}

	/**
	 * @param magazzinoDocumentoBD
	 *            the magazzinoDocumentoBD to set
	 */
	public void setMagazzinoDocumentoBD(IMagazzinoDocumentoBD magazzinoDocumentoBD) {
		this.magazzinoDocumentoBD = magazzinoDocumentoBD;
	}

	/**
	 * @param reportManager
	 *            the reportManager to set
	 */
	public void setReportManager(ReportManager reportManager) {
		this.reportManager = reportManager;
	}

}
