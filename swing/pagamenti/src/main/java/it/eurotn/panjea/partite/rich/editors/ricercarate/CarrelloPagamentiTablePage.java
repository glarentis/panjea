package it.eurotn.panjea.partite.rich.editors.ricercarate;

import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.exceptions.PanjeaRuntimeException;
import it.eurotn.panjea.pagamenti.util.PagamentoDTO;
import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.panjea.partite.domain.TipoAreaPartita.TipoPartita;
import it.eurotn.panjea.partite.rich.forms.ricercarate.ParametriCreazioneAreaChiusureForm;
import it.eurotn.panjea.plugin.PluginManager;
import it.eurotn.panjea.rate.domain.Rata.StatoRata;
import it.eurotn.panjea.rich.bd.ValutaAziendaCache;
import it.eurotn.panjea.rich.commands.StampaRVCommand;
import it.eurotn.panjea.rich.commands.StampaRVCommandRaggruppato;
import it.eurotn.panjea.rich.components.JecSplitPane;
import it.eurotn.panjea.rich.forms.PanjeaFormGuard;
import it.eurotn.panjea.tesoreria.domain.AreaChiusure;
import it.eurotn.panjea.tesoreria.domain.AreaTesoreria;
import it.eurotn.panjea.tesoreria.domain.Pagamento;
import it.eurotn.panjea.tesoreria.rich.bd.ITesoreriaBD;
import it.eurotn.panjea.tesoreria.solleciti.editors.CreaEStampaSollecitiDialog;
import it.eurotn.panjea.tesoreria.util.ParametriCreazioneAreaChiusure;
import it.eurotn.panjea.tesoreria.util.ParametriRicercaAreeTesoreria;
import it.eurotn.panjea.tesoreria.util.SituazioneRata;
import it.eurotn.panjea.tesoreria.util.SituazioneRata.StatoCarrello;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.support.ActionCommandInterceptorAdapter;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.settings.SettingsManager;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.JideCellEditorListener;
import com.jidesoft.grid.JideTable;
import com.jidesoft.grid.TableModelWrapperUtils;
import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

public class CarrelloPagamentiTablePage extends AbstractTablePageEditor<PagamentoPM> implements TableModelListener {

	public class CellEditorListener implements JideCellEditorListener {

		@Override
		public void editingCanceled(ChangeEvent changeevent) {
		}

		@Override
		public void editingStarted(ChangeEvent arg0) {
		}

		@Override
		public boolean editingStarting(ChangeEvent arg0) {
			return true;
		}

		@Override
		public void editingStopped(ChangeEvent changeevent) {
			TableModelEvent event = new TableModelEvent(getTableModel());
			getTableModel().fireTableChanged(event);
		}

		@Override
		public boolean editingStopping(ChangeEvent arg0) {
			return true;
		}

	}

	/**
	 * Command per la generazione di AreePartite con {@link ParametriCreazioneAreaChiusure} e una {@link List} di
	 * {@link Pagamento}.
	 * 
	 * @author Leonardo
	 */
	public class CreaAreaPartitaPerPagamentiCommand extends ApplicationWindowAwareCommand {

		private static final String COMMAND_ID = "creaAreaPartitaPerPagamentiCommand";

		/**
		 * Costruttore.
		 */
		public CreaAreaPartitaPerPagamentiCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			if (getTableModel().getRowCount() == 0) {
				return;
			}
			if (getTable().getTable().getCellEditor() != null) {
				getTable().getTable().getCellEditor().stopCellEditing();
			}
			ParametriCreazioneAreaChiusure parametriCreazioneAreaChiusure = (ParametriCreazioneAreaChiusure) parametriCreazioneAreaChiusureForm
					.getFormObject();
			boolean anticipoFattureVerifica = situazioneRataVerifica
					.verificaScadenzeAnticipoFatture(parametriCreazioneAreaChiusure);
			if (!anticipoFattureVerifica) {
				return;
			}

			boolean anticipoFatture = false;
			List<Pagamento> pagamenti = new ArrayList<Pagamento>();
			for (PagamentoPM pagamentoPM : getTableModel().getObjects()) {
				Pagamento pagamento = pagamentoPM.getPagamentoDTO().getPagamento();
				pagamenti.add(pagamento);
				if (!anticipoFatture) {
					if (pagamentoPM.getRata().getStatoRata().equals(StatoRata.ANTICIPO_FATTURA)) {
						anticipoFatture = true;
					}
				}
			}

			parametriCreazioneAreaChiusure.setCompensazioneRate(getTableModelRicercaRate().isCompensazione());
			parametriCreazioneAreaChiusure.setAnticipoFattura(anticipoFatture);

			List<AreaChiusure> areeChiusure = tesoreriaBD.creaAreaChiusurePerPagamenti(parametriCreazioneAreaChiusure,
					pagamenti);

			// Apri l'editor dei documento di pagamento con i pagamenti creati.
			ParametriRicercaAreeTesoreria parametriRicercaAreeTesoreria = new ParametriRicercaAreeTesoreria();
			List<AreaTesoreria> list = new ArrayList<AreaTesoreria>();
			list.addAll(areeChiusure);
			parametriRicercaAreeTesoreria.setAreeTesoreria(list);
			parametriRicercaAreeTesoreria.setPeriodo(new Periodo());
			parametriRicercaAreeTesoreria.setEffettuaRicerca(true);
			LifecycleApplicationEvent event = new OpenEditorEvent(parametriRicercaAreeTesoreria);
			Application.instance().getApplicationContext().publishEvent(event);

			// Svuoto i pagamenti
			totalePanel.resetTotali();
			setRows(new ArrayList<PagamentoPM>());
			parametriCreazioneAreaChiusure.setNoteContabili("");
			parametriCreazioneAreaChiusureForm.setFormObject(parametriCreazioneAreaChiusure);

			PluginManager pluginManager = RcpSupport.getBean(PluginManager.BEAN_ID);
			if (pluginManager.isPresente(PluginManager.PLUGIN_CENTRO_COSTI)) {
				ActionCommand commandVerificaCentriDiCosto = RcpSupport.getCommand("verificaCentriDiCostoCommand");
				if (commandVerificaCentriDiCosto != null) {
					commandVerificaCentriDiCosto.addParameter("check", true);
					commandVerificaCentriDiCosto.execute();
				}
			}
		}
	}

	public class OpenGeneraSollecitiCommand extends ApplicationWindowAwareCommand {
		private static final String COMMAND_ID = "openGeneraSollecitiCommand";

		/**
		 * apre il editor per generare i solleciti.
		 */
		public OpenGeneraSollecitiCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			if (getTableModel().getRowCount() == 0) {
				return;
			}
			if (getTable().getTable().getCellEditor() != null) {
				getTable().getTable().getCellEditor().stopCellEditing();
			}
			CreaEStampaSollecitiDialog dialog = RcpSupport.getBean(CreaEStampaSollecitiDialog.PAGE_ID);
			dialog.setPagamenti(getTable().getRows());
			dialog.showDialog();

			if (getTable().getTable().getCellEditor() != null) {
				getTable().getTable().getCellEditor().stopCellEditing();
			}
			// svuoto i pagamenti
			totalePanel.resetTotali();
			getTable().selectAll();
			getRimuoviPagamentoCommand().execute();
		}

	}

	public class RimuoviPagamentoCommand extends ApplicationWindowAwareCommand {

		/**
		 * Costruttore.
		 */
		public RimuoviPagamentoCommand() {
			super("deleteCommand");
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {

			List<PagamentoPM> pagamentiPM = getTable().getSelectedObjects();

			if (getTable().getTable().getCellEditor() != null) {
				getTable().getTable().getCellEditor().stopCellEditing();
			}

			for (PagamentoPM pagamentoPM : pagamentiPM) {
				getTable().removeRowObject(pagamentoPM);
			}
		}
	}

	private class StampaRVCommandInterceptor extends ActionCommandInterceptorAdapter {

		@Override
		public boolean preExecution(ActionCommand command) {

			List<PagamentoPM> pagamenti = getTable().getRows();

			if (!pagamenti.isEmpty()) {
				StringBuilder sb = new StringBuilder();
				String sep = "";
				for (PagamentoPM pagamentoPM : pagamenti) {
					sb.append(sep);
					sb.append(pagamentoPM.getRata().getRata().getId());
					sep = ",";
				}
				command.addParameter(StampaRVCommand.PARAM_LIST_RATE_ID, sb.toString());
			}
			return !pagamenti.isEmpty();
		}
	}

	private static Logger logger = Logger.getLogger(CarrelloPagamentiTablePage.class);

	public static final String PAGE_ID = "carrelloPagamentiTablePage";
	private ITesoreriaBD tesoreriaBD = null;

	private SettingsManager settingsManager;

	private ParametriCreazioneAreaChiusureForm parametriCreazioneAreaChiusureForm = null;

	private TotaleImportoRatePerPagamentoPanel totalePanel;

	private RimuoviPagamentoCommand rimuoviPagamentoCommand;

	private CreaAreaPartitaPerPagamentiCommand creaAreaPartitaPerPagamentiCommand = null;

	private OpenGeneraSollecitiCommand openGeneraSollecitiCommand = null;

	private StampaRVCommand stampaRVCommand;
	private StampaRVCommandRaggruppato stampaRVCommandRaggruppate;

	private ValutaAziendaCache valutaCache;

	private RisultatiRicercaRateTableModel tableModelRicercaRate;

	private SituazioneRataVerifica situazioneRataVerifica;

	private JecSplitPane carrelloSplitPane = null;

	/**
	 * Costruttore.
	 * 
	 */
	public CarrelloPagamentiTablePage() {
		super(PAGE_ID, new CarrelloPagamentiTableModel(PAGE_ID));
		getTableModel().addTableModelListener(this);
		valutaCache = RcpSupport.getBean(ValutaAziendaCache.BEAN_ID);
		((JideTable) getTable().getTable()).addCellEditorListener(new CellEditorListener());
		situazioneRataVerifica = new SituazioneRataVerifica(this);
	}

	/**
	 * 
	 * @param situazioneRata
	 *            situazioneRata per creare il pagamento. Se le rate hanno valuta diversa dalle rate già presenti nel
	 *            carrello viene visualizzato un messaggio e non vengono aggiunte.
	 * @return true se la rata viene aggiunta.
	 */
	private boolean addPagamentoFromRata(SituazioneRata situazioneRata) {
		logger.debug("--> Enter addPagamentoFromRata");

		// Rimuovo i table listener per non lanciare eventi.
		getTableModel().removeTableModelListener(totalePanel);

		try {
			if (!situazioneRataVerifica.verificaSituazioneRata(situazioneRata)) {
				return false;
			}

			Pagamento pagamento = new Pagamento();
			pagamento.setRata(situazioneRata.getRata());
			pagamento.setDataCreazione(Calendar.getInstance().getTime());

			BigDecimal residuoRata = situazioneRata.getRata().getResiduo().getImportoInValuta();
			if (getTableModelRicercaRate().isCompensazione()
					&& situazioneRata.getRata().getAreaRate().getTipoAreaPartita().getTipoPartita() == TipoPartita.PASSIVA) {
				residuoRata = residuoRata.negate();
			}
			String codValuta = situazioneRata.getRata().getImporto().getCodiceValuta();

			Importo imp = new Importo();
			imp.setCodiceValuta(codValuta);
			imp.setImportoInValuta(residuoRata);
			imp.calcolaImportoValutaAzienda(valutaCache.caricaValutaAziendaCorrente().getNumeroDecimali());
			pagamento.setImporto(imp);

			Importo impForzato = new Importo();
			impForzato.setCodiceValuta(codValuta);
			impForzato.setImportoInValuta(BigDecimal.ZERO);
			impForzato.setImportoInValutaAzienda(BigDecimal.ZERO);
			impForzato.calcolaImportoValutaAzienda(valutaCache.caricaValutaAziendaCorrente().getNumeroDecimali());
			pagamento.setImportoForzato(impForzato);
			pagamento.setInsoluto(Boolean.FALSE);

			// Prima di aggiungere il pagamento verifico che non sfori l'importo massimo.
			// In caso devo aggiungere solamente il residuo
			BigDecimal rimanenza = totalePanel.getImportoMassimo().subtract(getTableModel().getTotalePagamenti());
			// if (totalePanel.getImportoMassimo().compareTo(BigDecimal.ZERO) > 0
			// && rimanenza.compareTo(BigDecimal.ZERO) <= 0) {
			if (totalePanel.checkImportoMassimoRaggiunto(getTableModel().getTotalePagamenti())) {
				MessageDialog importoSuperatoDialog = new MessageDialog("Importo maggiore",
						"Il carrello contiene già pagamenti superiori all'importo di blocco.<br>");
				importoSuperatoDialog.setPreferredSize(new Dimension(250, 150));
				importoSuperatoDialog.showDialog();
				return false;
			} else if (totalePanel.getImportoMassimo().compareTo(BigDecimal.ZERO) != 0
					&& ((totalePanel.getImportoMassimo().signum() == 1 && residuoRata.compareTo(rimanenza) > 0) || (totalePanel
							.getImportoMassimo().signum() == -1 && residuoRata.compareTo(rimanenza) < 0))) {
				pagamento.getImporto().setImportoInValuta(rimanenza);
				pagamento.getImporto().calcolaImportoValutaAzienda(
						valutaCache.caricaValutaAziendaCorrente().getNumeroDecimali());
				MessageDialog importoSuperatoDialog = new MessageDialog("Importo maggiore",
						"Importo del carrello superiore al consentito.<br>Verrà utilizzata solamente una parte della rata.<br>");
				importoSuperatoDialog.setPreferredSize(new Dimension(250, 150));
				importoSuperatoDialog.showDialog();
			}
			PagamentoDTO pagamentoDTO = new PagamentoDTO(pagamento, null);
			PagamentoPM pagamentoPM = new PagamentoPM(pagamentoDTO, situazioneRata);
			getTable().replaceOrAddRowObject(pagamentoPM, pagamentoPM, null);
			aggiornaPerScontoFinanziario();

			carrelloSplitPane.expand(0.7d);
		} catch (Exception e) {
			throw new PanjeaRuntimeException(e);
		} finally {
			getTableModel().addTableModelListener(totalePanel);
			TableModelEvent event = new TableModelEvent(getTableModel());
			getTableModel().fireTableChanged(event);
		}
		logger.debug("--> Exit addPagamentoFromRata");
		return true;
	}

	/**
	 * Per i pagamenti nel carrello verifica se ci sono sconti finanziari validi.<br/>
	 * In tal caso imposta l'importo al residuo della rata + lo sconto finanziario,<br/>
	 * la setta come forzata.
	 */
	private void aggiornaPerScontoFinanziario() {
		Date dataDocumentoPagamento = (Date) parametriCreazioneAreaChiusureForm.getFormModel()
				.getValueModel("dataDocumento").getValue();
		if (dataDocumentoPagamento == null) {
			return;
		}
		for (PagamentoPM pagamentoPM : getTable().getRows()) {
			if (pagamentoPM.getPagamentoDTO().getPagamento().isScontoFinanziario()) {
				// riporto il pagamento come un pagamento standard, in caso dopo lo risetto
				pagamentoPM.getImporto().setImportoInValuta(pagamentoPM.getResiduo());
				pagamentoPM.setChiusuraForzataRata(false);
				if (dataDocumentoPagamento.before(pagamentoPM.getRata().getRata().getAreaRate()
						.getDataScadenzaScontoFinanziario())) {
					pagamentoPM.getImporto().setImportoInValuta(
							pagamentoPM.getRata().getRata().getImportoInValutaScontoFinanziario());
					pagamentoPM.setChiusuraForzataRata(true);
					pagamentoPM.getPagamentoDTO().getPagamento().setScontoFinanziario(true);
				}
			}
		}
	}

	@Override
	public JComponent createToolbar() {
		JPanel toolbarPanel = getComponentFactory().createPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
		toolbarPanel.add(super.createToolbar());

		totalePanel = new TotaleImportoRatePerPagamentoPanel(getTableModel());
		getTableModel().addTableModelListener(totalePanel);
		toolbarPanel.add(totalePanel);
		return toolbarPanel;
	}

	@Override
	public void dispose() {
		super.dispose();
		getTableModel().removeTableModelListener(totalePanel);
		getTableModel().removeTableModelListener(this);
	}

	/**
	 * 
	 * @return codice della valuta contenuta nel carrello (il carrello può avere solamente 1 valuta.)Null se non ho
	 *         rate.
	 */
	public String getCodiceValuta() {
		return getTableModel().getCodiceValuta();
	}

	@Override
	public AbstractCommand[] getCommands() {
		return new AbstractCommand[] { getStampaRVRaggruppateCommand(), getStampaRVCommand(),
				getOpenGeneraSollecitiCommand(), getCreaAreaPartitaPerPagamentiCommand(), getRimuoviPagamentoCommand() };
	}

	/**
	 * @return comando per la crazione delle aree chiusure.
	 */
	public ActionCommand getCreaAreaPartitaPerPagamentiCommand() {
		if (creaAreaPartitaPerPagamentiCommand == null) {
			creaAreaPartitaPerPagamentiCommand = new CreaAreaPartitaPerPagamentiCommand();
		}
		return creaAreaPartitaPerPagamentiCommand;
	}

	/**
	 * Aggiunge i controlli di {@link ParametriCreazioneAreaPartitaForm} come header di questa editableTablePage.
	 * 
	 * @return controlli creati
	 */
	@Override
	public JComponent getHeaderControl() {
		logger.debug("--> Enter getHeaderControl");

		parametriCreazioneAreaChiusureForm = new ParametriCreazioneAreaChiusureForm();
		new PanjeaFormGuard(parametriCreazioneAreaChiusureForm.getFormModel(), creaAreaPartitaPerPagamentiCommand);
		parametriCreazioneAreaChiusureForm.addFormValueChangeListener("dataDocumento", new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getNewValue() != null && !evt.getNewValue().equals(evt.getOldValue())) {
					aggiornaPerScontoFinanziario();
				}
			}
		});
		return parametriCreazioneAreaChiusureForm.getControl();
	}

	/**
	 * @return comando per la crazione delle aree chiusure.
	 */
	public ActionCommand getOpenGeneraSollecitiCommand() {
		if (openGeneraSollecitiCommand == null) {
			openGeneraSollecitiCommand = new OpenGeneraSollecitiCommand();
			openGeneraSollecitiCommand.setEnabled(false);
		}
		return openGeneraSollecitiCommand;
	}

	/**
	 * @return the rimuoviPagamentoCommand
	 */
	public RimuoviPagamentoCommand getRimuoviPagamentoCommand() {
		if (rimuoviPagamentoCommand == null) {
			rimuoviPagamentoCommand = new RimuoviPagamentoCommand();
			rimuoviPagamentoCommand.setEnabled(false);
			getTable().getTable().addKeyListener(new KeyAdapter() {

				@Override
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_DELETE) {
						getRimuoviPagamentoCommand().execute();
					}
				}
			});
		}

		return rimuoviPagamentoCommand;
	}

	/**
	 * @return the settingsManager
	 */
	public SettingsManager getSettingsManager() {
		return settingsManager;
	}

	/**
	 * @return the stampaRVCommand
	 */
	public StampaRVCommand getStampaRVCommand() {
		if (stampaRVCommand == null) {
			stampaRVCommand = new StampaRVCommand("stampaRateRVCommand");
			stampaRVCommand.addCommandInterceptor(new StampaRVCommandInterceptor());
		}

		return stampaRVCommand;
	}

	public StampaRVCommand getStampaRVRaggruppateCommand() {
		if (stampaRVCommandRaggruppate == null) {
			stampaRVCommandRaggruppate = new StampaRVCommandRaggruppato();
			stampaRVCommandRaggruppate.addCommandInterceptor(new StampaRVCommandInterceptor());
		}

		return stampaRVCommandRaggruppate;
	}

	/**
	 * 
	 * @return table model originario.
	 */
	private CarrelloPagamentiTableModel getTableModel() {
		return (CarrelloPagamentiTableModel) TableModelWrapperUtils.getActualTableModel(getTable().getTable()
				.getModel());
	}

	/**
	 * @return the tableModelRicercaRate
	 */
	public RisultatiRicercaRateTableModel getTableModelRicercaRate() {
		return tableModelRicercaRate;
	}

	@Override
	public Collection<PagamentoPM> loadTableData() {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {

				@Override
				public void run() {
					setRows(new ArrayList<PagamentoPM>());
				}
			});
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public Collection<PagamentoPM> refreshTableData() {
		return null;
	}

	/**
	 * @param carrelloSplitPane
	 *            the carrelloSplitPane to set
	 */
	public void setCarrelloSplitPane(JecSplitPane carrelloSplitPane) {
		this.carrelloSplitPane = carrelloSplitPane;
	}

	@Override
	public void setFormObject(Object object) {
		ParametriCreazioneAreaChiusure parametriCreazioneAreaChiusure = (ParametriCreazioneAreaChiusure) object;
		if (parametriCreazioneAreaChiusure != null) {
			parametriCreazioneAreaChiusureForm.setFormObject(parametriCreazioneAreaChiusure);
		}
	}

	/**
	 * @param settingsManager
	 *            the settingsManager to set
	 */
	public void setSettingsManager(SettingsManager settingsManager) {
		this.settingsManager = settingsManager;
	}

	/**
	 * 
	 * @param tableModel
	 *            table model della ricerca rate
	 */
	public void setTableModelRate(RisultatiRicercaRateTableModel tableModel) {
		this.tableModelRicercaRate = tableModel;
	}

	/**
	 * @param tesoreriaBD
	 *            The tesoreriaBD to set.
	 */
	public void setTesoreriaBD(ITesoreriaBD tesoreriaBD) {
		this.tesoreriaBD = tesoreriaBD;
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		getRimuoviPagamentoCommand().setEnabled(getTableModel().getRowCount() != 0);
		getOpenGeneraSollecitiCommand().setEnabled(getTableModel().getRowCount() != 0);
		getStampaRVCommand().setEnabled(getTableModel().getRowCount() != 0);
		getStampaRVRaggruppateCommand().setEnabled(getTableModel().getRowCount() != 0);
		ParametriCreazioneAreaChiusure parametriCreazioneAreaChiusure = (ParametriCreazioneAreaChiusure) parametriCreazioneAreaChiusureForm
				.getFormObject();
		if (getTableModel().getRowCount() > 0) {
			TipoPartita tipoPartitaInCarrello = getTable().getRows().get(0).getPagamentoDTO().getPagamento().getRata()
					.getAreaRate().getTipoAreaPartita().getTipoPartita();

			if (tableModelRicercaRate.isCompensazione()) {
				parametriCreazioneAreaChiusure.setTipoPartita(null);
			} else if (!tipoPartitaInCarrello.equals(parametriCreazioneAreaChiusure.getTipoPartita())) {
				parametriCreazioneAreaChiusure.setTipoPartita(tipoPartitaInCarrello);
				parametriCreazioneAreaChiusure.setTipoAreaPartita(null);
			}
			parametriCreazioneAreaChiusureForm.setFormObject(parametriCreazioneAreaChiusure);
		}
	}

	/**
	 * Aggiunge la rata al carrello.
	 * 
	 * @param rata
	 *            rata da aggiornare
	 * @return true se la tara è stata aggiunta ala carrello
	 */
	public boolean updateSituazioneRata(SituazioneRata rata) {
		boolean result = true;
		if (rata.getStatoCarrello() == StatoCarrello.AGGIUNTO_CARRELLO) {
			result = addPagamentoFromRata(rata);
		} else {
			List<PagamentoPM> pagamenti = getTableModel().getObjects();
			List<PagamentoPM> pagamentiDaRimuovere = new ArrayList<PagamentoPM>();
			for (PagamentoPM pagamentoPM : pagamenti) {
				if (pagamentoPM.getRata().equals(rata)) {
					pagamentiDaRimuovere.add(pagamentoPM);
				}
			}
			// rimuovo da table model .Non posso farlo durante il ciclo precedente altrimenti rilancia una
			// concurrent (rimuovo dalla lista che scorro)
			for (PagamentoPM pagamentoPM2 : pagamentiDaRimuovere) {
				getTableModel().removeObject(pagamentoPM2);
			}
		}
		return result;
	}
}
