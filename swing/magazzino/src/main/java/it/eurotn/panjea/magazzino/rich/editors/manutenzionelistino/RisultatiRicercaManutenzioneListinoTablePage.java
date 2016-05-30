package it.eurotn.panjea.magazzino.rich.editors.manutenzionelistino;

import it.eurotn.panjea.magazzino.domain.Listino;
import it.eurotn.panjea.magazzino.domain.Listino.ETipoListino;
import it.eurotn.panjea.magazzino.domain.ScaglioneListino;
import it.eurotn.panjea.magazzino.exception.ArticoliDuplicatiManutenzioneListinoException;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.forms.manutenzionelistino.ParametriAggiornaManutenzioneListinoForm;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriAggiornaManutenzioneListino;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaManutenzioneListino;
import it.eurotn.panjea.magazzino.util.parametriricerca.RigaManutenzioneListino;
import it.eurotn.panjea.rich.forms.PanjeaFormGuard;
import it.eurotn.rich.control.table.JideTableWidget.TableType;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.dialog.AlertMessageAreaPane;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.grid.JideTable;
import com.jidesoft.validation.TableValidationObject;
import com.jidesoft.validation.ValidationObject;
import com.jidesoft.validation.ValidationResult;
import com.jidesoft.validation.Validator;

public class RisultatiRicercaManutenzioneListinoTablePage extends AbstractTablePageEditor<RigaManutenzioneListino> {

	private class AggiornaListinoDaManutenzioneCommand extends ActionCommand {

		public static final String COMMAND_ID = "aggiornaListinoDaManutenzioneCommand";

		/**
		 * Cancella le righe manutenzione listino selezionate in tabella.
		 */
		public AggiornaListinoDaManutenzioneCommand() {
			super(COMMAND_ID);
			setEnableAutoResizeRow(true);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			ParametriAggiornaManutenzioneListino parametriAggiornaManutenzioneListino = (ParametriAggiornaManutenzioneListino) getParametriAggiornaManutenzioneListinoForm()
					.getFormObject();

			// controllo che il listino di destinazione scelto sia corretto rispetto alle righe manutenzione
			ETipoListino tipoListinoManutenzione = getTable().getRows().get(0).getListino() == null
					|| getTable().getRows().get(0).getListino().getTipoListino() == ETipoListino.NORMALE ? ETipoListino.NORMALE
					: ETipoListino.SCAGLIONE;

			if (tipoListinoManutenzione != parametriAggiornaManutenzioneListino.getVersioneListino().getListino()
					.getTipoListino()) {
				MessageDialog dialog = new MessageDialog("ATTENZIONE", new DefaultMessage(
						"E' possibile eseguire la manutenzione solamente<br>segliendo come destinazione un listino di tipo <b>"
								+ ObjectConverterManager.toString(tipoListinoManutenzione) + "</b>", Severity.WARNING));
				dialog.showDialog();
				return;
			}

			try {
				magazzinoAnagraficaBD.aggiornaListinoDaManutenzione(parametriAggiornaManutenzioneListino);
				getParametriAggiornaManutenzioneListinoForm().setFormObject(new ParametriAggiornaManutenzioneListino());
				loadData();
			} catch (ArticoliDuplicatiManutenzioneListinoException e) {
				Map<String, Integer> mapArticoli = e.getArticoliDuplicati();

				DefaultListModel model = new DefaultListModel();
				for (Entry<String, Integer> entry : mapArticoli.entrySet()) {
					model.addElement("(" + entry.getValue() + ")  " + entry.getKey());
				}
				final JList listArticoli = new JList(model);

				MessageDialog dialog = new MessageDialog("ATTENZIONE",
						"I seguenti articoli risultano essere duplicati:") {
					@Override
					protected JComponent createDialogContentPane() {
						JPanel panel = getComponentFactory().createPanel(new BorderLayout(0, 10));

						AlertMessageAreaPane messageAreaPane = new AlertMessageAreaPane();
						messageAreaPane.setMessage(getMessage());
						panel.add(messageAreaPane.getControl(), BorderLayout.NORTH);
						panel.add(new JScrollPane(listArticoli), BorderLayout.CENTER);
						return panel;
					}
				};
				dialog.setPreferredSize(new Dimension(500, 300));
				dialog.showDialog();
			}
		}
	}

	private class CancellaRigheManutenzioneListinoCommand extends ActionCommand {

		public static final String COMMAND_ID = "cancellaParametriRicercaManutenzioneListinoCommand";

		/**
		 * Cancella le righe manutenzione listino selezionate in tabella.
		 */
		public CancellaRigheManutenzioneListinoCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			List<RigaManutenzioneListino> righeManutenzioneListino = getTable().getSelectedObjects();
			magazzinoAnagraficaBD.cancellaRigheManutenzioneListino(righeManutenzioneListino);
			for (RigaManutenzioneListino rigaManutenzioneListino : righeManutenzioneListino) {
				getTable().removeRowObject(rigaManutenzioneListino);
			}

		}
	}

	private class QtaScaglioneRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = -6515357931381244653L;

		private DecimalFormat format = new DecimalFormat();

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
				boolean hasFocus, int row, int column) {
			JLabel label = (JLabel) super
					.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			label.setHorizontalAlignment(SwingConstants.RIGHT);

			if (getTable().getSelectedObject().getListino() == null
					|| getTable().getSelectedObject().getListino().getTipoListino() == ETipoListino.NORMALE) {
				return label;
			}

			if (ScaglioneListino.MAX_SCAGLIONE.equals(value)) {
				label.setText("OLTRE");
			} else if (value instanceof Double) {

				format.applyPattern("#,##0.##");
				label.setText("fino a " + format.format(value));
			}
			return label;
		}

	}

	private class SvuotaTabellaCommand extends ApplicationWindowAwareCommand {

		public static final String COMMAND_ID = "svuotaRigheManutenzioneListinoCommand";

		/**
		 * Costruttore.
		 */
		public SvuotaTabellaCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			if (getTable().getTable().isEditing()) {
				getTable().getTable().getCellEditor().stopCellEditing();
			}
			magazzinoAnagraficaBD.cancellaRigheManutenzioneListino(null);
			loadData();
		}
	}

	public static final String PAGE_ID = "risultatiRicercaManutenzioneListinoTablePage";
	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD = null;

	private ParametriRicercaManutenzioneListino parametriRicercaManutenzioneListino = null;
	private CancellaRigheManutenzioneListinoCommand cancellaRigheManutenzioneListinoCommand = null;
	private SvuotaTabellaCommand svuotaTabellaCommand = null;

	private AggiornaListinoDaManutenzioneCommand aggiornaListinoDaManutenzioneCommand = null;

	private ParametriAggiornaManutenzioneListinoForm parametriAggiornaManutenzioneListinoForm = null;

	private ManutenzioneListinoLayoutManager manutenzioneListinoLayoutManager = null;

	/**
	 * Default constructor.
	 * 
	 * @param magazzinoAnagraficaBD
	 *            magazzinoAnagraficaBD
	 */
	public RisultatiRicercaManutenzioneListinoTablePage(final IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		super(PAGE_ID, new RigheManutenzioneListinoTableModel(magazzinoAnagraficaBD));
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
		manutenzioneListinoLayoutManager = new ManutenzioneListinoLayoutManager(getTable(), ETipoListino.NORMALE);
		getTable().getTableLayoutView().setLayoutManager(manutenzioneListinoLayoutManager);
		getTable().setTableType(TableType.GROUP);
		getTable().getTable().getColumnModel().getColumn(2).setCellRenderer(new QtaScaglioneRenderer());
		getTable().getOverlayTable().setShowOptionPanel(false);

		((JideTable) getTable().getTable()).addValidator(new Validator() {

			@Override
			public ValidationResult validating(ValidationObject validationObject) {
				TableValidationObject tableValidationObject = (TableValidationObject) validationObject;
				JTable table = (JTable) validationObject.getSource();

				if ("Numero decimali".equals(table.getColumnName(tableValidationObject.getColumn()))) {
					boolean valid = tableValidationObject.getNewValue() != null
							&& ((Integer) tableValidationObject.getNewValue() <= 6)
							&& ((Integer) tableValidationObject.getNewValue() >= 0);
					if (!valid) {
						ValidationResult result = new ValidationResult(ValidationResult.FAIL_BEHAVIOR_REVERT);
						return result;
					}
				}
				if ("Valore".equals(table.getColumnName(tableValidationObject.getColumn()))) {
					boolean valid = tableValidationObject.getNewValue() != null
							&& ((BigDecimal) tableValidationObject.getNewValue()).compareTo(BigDecimal.ZERO) >= 0;
					if (!valid) {
						ValidationResult result = new ValidationResult(ValidationResult.FAIL_BEHAVIOR_REVERT);
						return result;
					}
				}
				return ValidationResult.OK;
			}
		});
	}

	/**
	 * @return AggiornaListinoDaManutenzioneCommand
	 */
	public AggiornaListinoDaManutenzioneCommand getAggiornaListinoDaManutenzioneCommand() {
		if (aggiornaListinoDaManutenzioneCommand == null) {
			aggiornaListinoDaManutenzioneCommand = new AggiornaListinoDaManutenzioneCommand();
		}
		return aggiornaListinoDaManutenzioneCommand;
	}

	/**
	 * @return CancellaRigheManutenzioneListinoCommand
	 */
	public CancellaRigheManutenzioneListinoCommand getCancellaRigheManutenzioneListinoCommand() {
		if (cancellaRigheManutenzioneListinoCommand == null) {
			cancellaRigheManutenzioneListinoCommand = new CancellaRigheManutenzioneListinoCommand();
		}
		return cancellaRigheManutenzioneListinoCommand;
	}

	@Override
	public AbstractCommand[] getCommands() {
		return new AbstractCommand[] { getAggiornaListinoDaManutenzioneCommand(),
				getCancellaRigheManutenzioneListinoCommand(), getSvuotaTabellaCommand() };
	}

	@Override
	public JComponent getHeaderControl() {
		ParametriAggiornaManutenzioneListinoForm form = getParametriAggiornaManutenzioneListinoForm();
		new PanjeaFormGuard(form.getFormModel(), getAggiornaListinoDaManutenzioneCommand());

		JComponent formControl = form.getControl();
		formControl.setBorder(BorderFactory.createEmptyBorder(4, 4, 0, 0));
		return formControl;
	}

	/**
	 * @return ParametriAggiornaManutenzioneListinoForm
	 */
	public ParametriAggiornaManutenzioneListinoForm getParametriAggiornaManutenzioneListinoForm() {
		if (parametriAggiornaManutenzioneListinoForm == null) {
			parametriAggiornaManutenzioneListinoForm = new ParametriAggiornaManutenzioneListinoForm();
		}
		return parametriAggiornaManutenzioneListinoForm;
	}

	/**
	 * @return SvuotaTabellaCommand
	 */
	public SvuotaTabellaCommand getSvuotaTabellaCommand() {
		if (svuotaTabellaCommand == null) {
			svuotaTabellaCommand = new SvuotaTabellaCommand();
		}
		return svuotaTabellaCommand;
	}

	@Override
	public void loadData() {
		// svuoto le righe prima di ricaricarle per far capire all'utente che ho lanciato la ricerca
		setRows(new ArrayList<RigaManutenzioneListino>());
		super.loadData();
	}

	@Override
	public Collection<RigaManutenzioneListino> loadTableData() {
		return magazzinoAnagraficaBD.caricaRigheManutenzioneListino();
	}

	@Override
	public void onPostPageOpen() {

	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public void processTableData(Collection<RigaManutenzioneListino> results) {
		super.processTableData(results);

		if (!results.isEmpty()) {
			ETipoListino tipoListino = ETipoListino.NORMALE;
			Listino listinoRighe = results.iterator().next().getListino();
			if (listinoRighe != null && listinoRighe.getTipoListino() == ETipoListino.SCAGLIONE) {
				tipoListino = ETipoListino.SCAGLIONE;
			}

			this.manutenzioneListinoLayoutManager.setTipoListino(tipoListino);
			getTable().getTableLayoutView().updateLayoutsToolBar();
			getTable().getTableLayoutView().getLayoutManager().applica(null);
		}
	}

	@Override
	public void refreshData() {
		// svuoto le righe prima di ricaricarle per far capire all'utente che ho lanciato la ricerca
		setRows(new ArrayList<RigaManutenzioneListino>());
		super.refreshData();
	}

	@Override
	public Collection<RigaManutenzioneListino> refreshTableData() {
		return loadTableData();
	}

	@Override
	public void setFormObject(Object object) {
		parametriRicercaManutenzioneListino = (ParametriRicercaManutenzioneListino) object;
	}

}
