package it.eurotn.panjea.ordini.rich.editors.ordiniimportati;

import foxtrot.Worker;
import it.eurotn.panjea.ordini.domain.OrdineImportato.EProvenienza;
import it.eurotn.panjea.ordini.domain.RigaOrdineImportata;
import it.eurotn.panjea.ordini.rich.bd.IOrdiniDocumentoBD;
import it.eurotn.panjea.ordini.rich.editors.ordiniimportati.RigheImportazioneTableModel.DatiErrore;
import it.eurotn.panjea.ordini.rich.editors.ordiniimportati.RigheImportazioneTableModel.DatiMancanti;
import it.eurotn.panjea.ordini.util.parametriricerca.ParametriRicercaOrdiniImportati;
import it.eurotn.panjea.rich.factory.PanjeaComponentFactory;
import it.eurotn.rich.binding.PanjeaTextFieldDateEditor;
import it.eurotn.rich.command.AbstractDeleteCommand;
import it.eurotn.rich.command.JideToggleCommand;
import it.eurotn.rich.control.table.DefaultBeanTableModel;
import it.eurotn.rich.control.table.JideTableWidget.TableType;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.table.EditFrame;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ExclusiveCommandGroup;
import org.springframework.richclient.command.support.ActionCommandInterceptorAdapter;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.DefaultGroupTableModel;
import com.jidesoft.grid.TableModelWrapperUtils;
import com.toedter.calendar.IDateEditor;
import com.toedter.calendar.JDateChooser;

public class ImportazioneOrdiniPage extends AbstractTablePageEditor<RigaOrdineImportata> implements InitializingBean {

	private class CopiaPrezzoDeterminatoCommand extends ActionCommand {

		public static final String COMMAND_ID = "copiaPrezzoDeterminatoCommand";

		/**
		 * Costruttore.
		 */
		public CopiaPrezzoDeterminatoCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {

			@SuppressWarnings("unchecked")
			DefaultBeanTableModel<RigaOrdineImportata> tableModel = (DefaultBeanTableModel<RigaOrdineImportata>) TableModelWrapperUtils
					.getActualTableModel(getTable().getTable().getModel());

			List<RigaOrdineImportata> righeAggiornate = new ArrayList<RigaOrdineImportata>();

			for (RigaOrdineImportata rigaOrdineImportata : tableModel.getObjects()) {

				if (rigaOrdineImportata.isSelezionata()
						&& (rigaOrdineImportata.getOmaggio() != null && !rigaOrdineImportata.getOmaggio()
								.booleanValue())) {
					rigaOrdineImportata.sostituisciPrezzoDeterminato();

					rigaOrdineImportata = ordiniDocumentoBD.salvaRigaOrdineImportata(rigaOrdineImportata).get(0);
					rigaOrdineImportata.setSelezionata(true);
					righeAggiornate.add(rigaOrdineImportata);
				}
			}

			for (RigaOrdineImportata rigaOrdineImportata : righeAggiornate) {
				int index = tableModel.getObjects().indexOf(rigaOrdineImportata);
				tableModel.setObject(rigaOrdineImportata, index);

				int indexOf = righeOrdineImportate.indexOf(rigaOrdineImportata);
				if (indexOf != -1) {
					righeOrdineImportate.set(indexOf, rigaOrdineImportata);
				}
			}

			getTable().getTable().repaint();
		}

	}

	private class DeleteOrdiniImportatiCommand extends AbstractDeleteCommand {

		/**
		 * costruttore.
		 */
		public DeleteOrdiniImportatiCommand() {
			super("deleteOrdiniImportatiCommand.controller");
		}

		@Override
		public Object onDelete() {
			Set<String> ordiniDaCancellare = new HashSet<String>();
			for (RigaOrdineImportata rigaOrdineImportata : getTable().getRows()) {
				if (rigaOrdineImportata.isSelezionata()) {
					ordiniDaCancellare.add(rigaOrdineImportata.getOrdine().getNumero());
				}
			}
			if (!ordiniDaCancellare.isEmpty()) {
				ordiniDocumentoBD.cancellaOrdiniImportati(ordiniDaCancellare);

				List<RigaOrdineImportata> righe = new ArrayList<RigaOrdineImportata>();
				List<RigaOrdineImportata> righeOriginali = ImportazioneOrdiniPage.this.getTable().getRows();
				for (RigaOrdineImportata rigaOrdineImportata : righeOriginali) {
					if (!rigaOrdineImportata.isSelezionata()) {
						righe.add(rigaOrdineImportata);
					} else {
						righeOrdineImportate.remove(rigaOrdineImportata);
					}
				}
			}
			return null;
		}
	}

	private class FiltroErroreAbilitatoInterceptor extends ActionCommandInterceptorAdapter {

		@Override
		public void postExecution(ActionCommand command) {
			super.postExecution(command);
			MostraRigheErroreCommand selectedCommand = null;
			for (DatiErrore tipoErrore : DatiErrore.values()) {
				if (mostraRigheErroreCommands[tipoErrore.ordinal()].isSelected()) {
					selectedCommand = mostraRigheErroreCommands[tipoErrore.ordinal()];
				}
			}
			restoreAllRows();

			if (selectedCommand != null) {
				if (selectedCommand.isVisible()) {
					selectedCommand.setSelected(true);
				}
			}
		}

		@Override
		public boolean preExecution(ActionCommand command) {
			for (DatiErrore tipoErrore : DatiErrore.values()) {
				mostraRigheErroreCommands[tipoErrore.ordinal()].setSelected(false);
			}
			return true;
		}
	}

	private class MostraRigheErroreCommand extends JideToggleCommand {

		private static final String COMMAND_ID = "mostraRigheErroreCommand";
		private Collection<RigaOrdineImportata> righeErrore;
		private AbstractButton button;
		private DatiErrore tipoErrore;

		/**
		 * Costruttore.
		 * 
		 * @param tipoErrore
		 *            tipoErrore
		 */
		public MostraRigheErroreCommand(final DatiErrore tipoErrore) {
			super(COMMAND_ID + "." + tipoErrore.name());
			this.tipoErrore = tipoErrore;
			setVisible(false);
		}

		@Override
		protected void onButtonAttached(AbstractButton buttonParma) {
			super.onButtonAttached(buttonParma);
			this.button = buttonParma;
			this.button.setIcon(RcpSupport.getIcon("severity.error"));
		}

		@Override
		protected void onDeselection() {
			getTable().setRows(righeOrdineImportate);
		}

		@Override
		protected void onSelection() {
			if (righeErrore != null) {
				getTable().setRows(righeErrore);
			}
		}

		/**
		 * @param righe
		 *            the righeErrore to set
		 */
		public void setRigheErrore(Collection<RigaOrdineImportata> righe) {
			this.righeErrore = righe;
			String buttonLabel = RcpSupport.getMessage(COMMAND_ID + "." + tipoErrore.name());
			String label = buttonLabel + " (" + righeErrore.size() + ")";
			if (this.button != null) {
				this.button.setText(label);
			}
			setVisible(righeErrore.size() > 0);
		}
	}

	private class ReloadInterceptor extends ActionCommandInterceptorAdapter {

		@SuppressWarnings("unchecked")
		@Override
		public void postExecution(ActionCommand arg0) {
			List<RigaOrdineImportata> righe = new ArrayList<RigaOrdineImportata>();
			try {
				righe = (List<RigaOrdineImportata>) Worker.post(new foxtrot.Task() {
					@Override
					public Object run() throws Exception {
						List<RigaOrdineImportata> righe = new ArrayList<RigaOrdineImportata>();
						List<RigaOrdineImportata> righeOriginali = ImportazioneOrdiniPage.this.getTable().getRows();
						for (RigaOrdineImportata rigaOrdineImportata : righeOriginali) {
							if (!rigaOrdineImportata.isSelezionata()) {
								righe.add(rigaOrdineImportata);
							}
						}
						return righe;
					}
				});
			} catch (Exception e) {
				logger.error("-->errore nel rimuovere le righe selezionate nel backorder ", e);
			}
			ImportazioneOrdiniPage.this.setRows(righe);
		}
	}

	private class RigaOrdineImportataChangeListener implements PropertyChangeListener {

		@SuppressWarnings("unchecked")
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			List<RigaOrdineImportata> righe = (List<RigaOrdineImportata>) evt.getNewValue();

			List<RigaOrdineImportata> righeTabella = getTable().getRows();

			for (RigaOrdineImportata rigaOrdineImportata : righe) {

				// cerco la riga precendente per allineare il valore della proprietà "selezionata" perchè
				// transiente e con il salvataggio delle righe viene perso
				int index = righeTabella.indexOf(rigaOrdineImportata);
				if (index != -1) {
					rigaOrdineImportata.setSelezionata(righeTabella.get(index).isSelezionata());
				}
				// aggiorno anche la lista completa
				int indexOf = righeOrdineImportate.indexOf(rigaOrdineImportata);
				if (indexOf != -1) {
					righeOrdineImportate.set(indexOf, rigaOrdineImportata);
				}

				getTable().replaceOrAddRowObject(rigaOrdineImportata, rigaOrdineImportata, ImportazioneOrdiniPage.this);
			}
			restoreAllRows();
		}

	}

	public static final String PAGE_ID = "importazioneOrdiniPage";
	private static final String KEY_EVADI_ORDINI = PAGE_ID + ".evadiOrdiniImportati";

	private IOrdiniDocumentoBD ordiniDocumentoBD = null;

	private List<RigaOrdineImportata> righeOrdineImportate = null;

	private ActionCommand creaOrdiniCommand = null;
	private ActionCommand creaEdEvadiOrdiniCommand = null;
	private AbstractDeleteCommand deleteCommand = null;
	private CopiaPrezzoDeterminatoCommand copiaPrezzoDeterminatoCommand = null;
	private MostraRigheErroreCommand[] mostraRigheErroreCommands = null;

	private BackOrderErrorPanel backOrderErrorPanel = null;
	private JCheckBox evadiOrdiniCheckBox = null;

	private RigaOrdineImportataChangeListener rigaOrdineImportataChangeListener = null;

	private FiltroErroreAbilitatoInterceptor filtroErroreAbilitatoInterceptor = null;
	private ReloadInterceptor reloadInterceptor = null;

	/**
	 * Costruttore.
	 */
	public ImportazioneOrdiniPage() {
		super(PAGE_ID, new RigheImportazioneTableModel(PAGE_ID));
		getTable().setTableType(TableType.GROUP);
		new SelectRowListener(this);

		final DefaultGroupTableModel groupTableModel = (DefaultGroupTableModel) TableModelWrapperUtils
				.getActualTableModel(getTable().getTable().getModel(), DefaultGroupTableModel.class);

		final RigheImportazioneTableModel tableModel = (RigheImportazioneTableModel) TableModelWrapperUtils
				.getActualTableModel(getTable().getTable().getModel());

		groupTableModel.addTableModelListener(new TableModelListener() {

			@Override
			public void tableChanged(TableModelEvent e) {

				boolean groupAndRefresh = false;

				int[] nonGroupableCols = tableModel.getNonGroupableColumns();
				for (int i = 0; i < nonGroupableCols.length; i++) {
					if (groupTableModel.isColumnGrouped(nonGroupableCols[i])) {
						groupTableModel.removeGroupColumn(nonGroupableCols[i]);
						groupAndRefresh = true;
					}
				}
				if (groupAndRefresh) {
					groupTableModel.groupAndRefresh();
				}
			}
		});
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		getEditPages().get(EditFrame.DEFAULT_OBJECT_CLASS_NAME).addPropertyChangeListener(
				RigaOrdineImportataPage.RIGHE_IMPORTATE_CHANGED, getRigaOrdineImportataChangeListener());
	}

	/**
	 * @return CheckBox
	 */
	private JCheckBox createCheckBox() {
		JCheckBox checkBox = new JCheckBox();
		checkBox.setSelected(false);
		return checkBox;
	}

	/**
	 * @return date chooser data evasione
	 */
	private JDateChooser createDataEvasioneDateChooser() {
		IDateEditor textFieldDateEditor = new PanjeaTextFieldDateEditor("dd/MM/yyyy", "dd/MM/yyyy", '_');

		final JDateChooser dataEvasioneDateChooser = ((PanjeaComponentFactory) getComponentFactory())
				.createDateChooser(textFieldDateEditor);
		dataEvasioneDateChooser.setDate(Calendar.getInstance().getTime());
		dataEvasioneDateChooser.setEnabled(false);

		return dataEvasioneDateChooser;
	}

	@Override
	public JComponent createToolbar() {
		JPanel panel = getComponentFactory().createPanel(new FlowLayout(FlowLayout.RIGHT));

		evadiOrdiniCheckBox = createCheckBox();
		final JDateChooser dataEvasioneDateChooser = createDataEvasioneDateChooser();

		panel.add(getComponentFactory().createLabel("Evadi ordini"));
		panel.add(evadiOrdiniCheckBox);
		panel.add(getComponentFactory().createLabel("Data evasione"));
		panel.add(dataEvasioneDateChooser);
		panel.add(super.createToolbar());

		evadiOrdiniCheckBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dataEvasioneDateChooser.setEnabled(evadiOrdiniCheckBox.isSelected());
			}
		});

		return super.createToolbar();
	}

	@Override
	public void dispose() {
		getEditPages().get(EditFrame.DEFAULT_OBJECT_CLASS_NAME).removePropertyChangeListener(
				RigaOrdineImportataPage.RIGHE_IMPORTATE_CHANGED, getRigaOrdineImportataChangeListener());
		if (creaEdEvadiOrdiniCommand != null) {
			creaEdEvadiOrdiniCommand.removeCommandInterceptor(reloadInterceptor);
			creaEdEvadiOrdiniCommand.removeCommandInterceptor(filtroErroreAbilitatoInterceptor);
		}
		if (creaOrdiniCommand != null) {
			creaOrdiniCommand.removeCommandInterceptor(reloadInterceptor);
		}
		if (copiaPrezzoDeterminatoCommand != null) {
			copiaPrezzoDeterminatoCommand.removeCommandInterceptor(filtroErroreAbilitatoInterceptor);
		}
		if (creaOrdiniCommand != null) {
			creaOrdiniCommand.removeCommandInterceptor(filtroErroreAbilitatoInterceptor);
		}
		if (deleteCommand != null) {
			deleteCommand.removeCommandInterceptor(filtroErroreAbilitatoInterceptor);
		}
		filtroErroreAbilitatoInterceptor = null;
		super.dispose();
	}

	@Override
	public AbstractCommand[] getCommands() {
		MostraRigheErroreCommand[] mostraRigheErrore = getMostraRigheErroreCommands();
		AbstractCommand[] commandsArray = new AbstractCommand[] { getCopiaPrezzoDeterminatoCommand(),
				getDeleteCommand(), getCreaOrdiniCommand(), getCreaEdEvadiOrdiniCommand(), getRefreshCommand() };

		List<AbstractCommand> defaultCommands = Arrays.asList(commandsArray);
		List<MostraRigheErroreCommand> errori = Arrays.asList(mostraRigheErrore);

		List<AbstractCommand> commands = new ArrayList<>();
		commands.addAll(errori);
		// non funziona a causa del glue di default già aggiunto che allinea a destra i commands della toolbar
		// commands.add(GlueActionCommand.getInstance());
		commands.addAll(defaultCommands);
		return commands.toArray(new AbstractCommand[commands.size()]);
	}

	/**
	 * @return the copiaPrezzoDeterminatoCommand
	 */
	public CopiaPrezzoDeterminatoCommand getCopiaPrezzoDeterminatoCommand() {
		if (copiaPrezzoDeterminatoCommand == null) {
			copiaPrezzoDeterminatoCommand = new CopiaPrezzoDeterminatoCommand();
			copiaPrezzoDeterminatoCommand.addCommandInterceptor(getFiltroErroreAbilitatoInterceptor());
		}
		return copiaPrezzoDeterminatoCommand;
	}

	/**
	 * @return command per creare ed evadere gli ordini importati.
	 */
	private ActionCommand getCreaEdEvadiOrdiniCommand() {
		if (creaEdEvadiOrdiniCommand == null) {
			creaEdEvadiOrdiniCommand = new CreaEdEvadiOrdiniCommand(ordiniDocumentoBD, this);
			creaEdEvadiOrdiniCommand.addCommandInterceptor(getReloadInterceptor());
			creaEdEvadiOrdiniCommand.addCommandInterceptor(getFiltroErroreAbilitatoInterceptor());
		}
		return creaEdEvadiOrdiniCommand;
	}

	/**
	 * @return command per creare gli ordini importati.
	 */
	private ActionCommand getCreaOrdiniCommand() {
		if (creaOrdiniCommand == null) {
			creaOrdiniCommand = new CreaOrdiniCommand(ordiniDocumentoBD, this);
			creaOrdiniCommand.addCommandInterceptor(getReloadInterceptor());
			creaOrdiniCommand.addCommandInterceptor(getFiltroErroreAbilitatoInterceptor());
		}
		return creaOrdiniCommand;
	}

	/**
	 * @return comando per cancellare gli ordini selezionati
	 */
	private AbstractDeleteCommand getDeleteCommand() {
		if (deleteCommand == null) {
			deleteCommand = new DeleteOrdiniImportatiCommand();
			deleteCommand.addCommandInterceptor(getFiltroErroreAbilitatoInterceptor());
		}
		return deleteCommand;
	}

	@Override
	public AbstractCommand getEditorDeleteCommand() {
		return getDeleteCommand();
	}

	/**
	 * @return filtroErroreAbilitatoInterceptor
	 */
	private FiltroErroreAbilitatoInterceptor getFiltroErroreAbilitatoInterceptor() {
		if (filtroErroreAbilitatoInterceptor == null) {
			filtroErroreAbilitatoInterceptor = new FiltroErroreAbilitatoInterceptor();
		}
		return filtroErroreAbilitatoInterceptor;
	}

	@Override
	public JComponent getFooterControl() {
		backOrderErrorPanel = new BackOrderErrorPanel();
		return backOrderErrorPanel;
	}

	/**
	 * @return mostraRighePrezzoZeroCommand
	 */
	public MostraRigheErroreCommand[] getMostraRigheErroreCommands() {
		if (mostraRigheErroreCommands == null) {
			ExclusiveCommandGroup exclusiveCommandGroup = new ExclusiveCommandGroup();
			exclusiveCommandGroup.setAllowsEmptySelection(true);
			mostraRigheErroreCommands = new MostraRigheErroreCommand[DatiErrore.values().length];
			for (DatiErrore tipoErrore : DatiErrore.values()) {
				mostraRigheErroreCommands[tipoErrore.ordinal()] = new MostraRigheErroreCommand(tipoErrore);
				exclusiveCommandGroup.add(mostraRigheErroreCommands[tipoErrore.ordinal()]);
			}
		}
		return mostraRigheErroreCommands;
	}

	/**
	 * @return reloadInterceptor
	 */
	private ReloadInterceptor getReloadInterceptor() {
		if (reloadInterceptor == null) {
			reloadInterceptor = new ReloadInterceptor();
		}
		return reloadInterceptor;
	}

	/**
	 * @return rigaOrdineImportataChangeListener
	 */
	private RigaOrdineImportataChangeListener getRigaOrdineImportataChangeListener() {
		if (rigaOrdineImportataChangeListener == null) {
			rigaOrdineImportataChangeListener = new RigaOrdineImportataChangeListener();
		}
		return rigaOrdineImportataChangeListener;
	}

	@Override
	public boolean isIgnoreDetailCommands() {
		return true;
	}

	@Override
	public Collection<RigaOrdineImportata> loadTableData() {
		getTable().setRows(new ArrayList<RigaOrdineImportata>());
		ParametriRicercaOrdiniImportati parametri = new ParametriRicercaOrdiniImportati();
		parametri.setProvenienza(EProvenienza.TUTTI);
		righeOrdineImportate = ordiniDocumentoBD.caricaRigheOrdineImportate(parametri);

		return righeOrdineImportate;
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	protected void onRefresh() {
		new ConfirmationDialog("Attenzione!", "Verranno aggiornate le righe ordine importate, continuare ?") {

			@Override
			protected void onConfirm() {
				ImportazioneOrdiniPage.super.onRefresh();
			}
		}.showDialog();
	}

	/**
	 * Processa le righe in tabella per raccogliere gli errori presenti.
	 */
	private void processRowsForErrors() {
		TableModel tableModel = getTable().getTable().getModel();
		RigheImportazioneTableModel tableModelImportazione = (RigheImportazioneTableModel) TableModelWrapperUtils
				.getActualTableModel(tableModel, RigheImportazioneTableModel.class);

		tableModelImportazione.initDatiMancanti();
		Map<DatiMancanti, Collection<String>> datiMancanti = tableModelImportazione.getDatiMancanti();
		Map<DatiErrore, Collection<RigaOrdineImportata>> datiErrore = tableModelImportazione.getDatiErrore();

		Collection<String> clienti = datiMancanti.get(DatiMancanti.ENTITA);
		Collection<String> articoli = datiMancanti.get(DatiMancanti.ARTICOLI);
		Collection<String> agenti = datiMancanti.get(DatiMancanti.AGENTI);
		Collection<String> pagamenti = datiMancanti.get(DatiMancanti.PAGAMENTI);

		if (backOrderErrorPanel != null) {
			backOrderErrorPanel.updateControl(clienti, articoli, agenti, pagamenti);
		}

		for (DatiErrore tipoErrore : DatiErrore.values()) {
			mostraRigheErroreCommands[tipoErrore.ordinal()].setSelected(false);
			mostraRigheErroreCommands[tipoErrore.ordinal()].setRigheErrore(datiErrore.get(tipoErrore));
		}

		getCreaOrdiniCommand().setEnabled(
				clienti.size() == 0 && articoli.size() == 0 && agenti.size() == 0 && pagamenti.size() == 0);
		getCreaEdEvadiOrdiniCommand().setEnabled(
				clienti.size() == 0 && articoli.size() == 0 && agenti.size() == 0 && pagamenti.size() == 0);
	}

	@Override
	public void processTableData(Collection<RigaOrdineImportata> results) {
		super.processTableData(results);

		processRowsForErrors();
	}

	@Override
	public Collection<RigaOrdineImportata> refreshTableData() {
		return null;
	}

	/**
	 * Reimposto tutte le righe aggiornate in tabella. (i commands MostraRigheErroreCommand impostano alla table solo le
	 * righe dell'errore specificato da DatiErrore).
	 */
	public void restoreAllRows() {
		processTableData(righeOrdineImportate);
	}

	@Override
	public void restoreState(Settings settings) {
		evadiOrdiniCheckBox.setSelected(false);
		if (settings.contains(KEY_EVADI_ORDINI)) {
			Boolean select = settings.getBoolean(KEY_EVADI_ORDINI);
			if (select) {
				evadiOrdiniCheckBox.doClick();
			}
		}
		super.restoreState(settings);
	}

	@Override
	public void saveState(Settings settings) {
		settings.setBoolean(KEY_EVADI_ORDINI, evadiOrdiniCheckBox.isSelected());
		super.saveState(settings);
	}

	@Override
	public void setFormObject(Object object) {
	}

	/**
	 * @param ordiniDocumentoBD
	 *            The ordiniDocumentoBD to set.
	 */
	public void setOrdiniDocumentoBD(IOrdiniDocumentoBD ordiniDocumentoBD) {
		this.ordiniDocumentoBD = ordiniDocumentoBD;
	}

}
