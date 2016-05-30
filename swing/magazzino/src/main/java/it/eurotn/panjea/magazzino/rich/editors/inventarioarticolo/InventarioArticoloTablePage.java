package it.eurotn.panjea.magazzino.rich.editors.inventarioarticolo;

import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.magazzino.domain.InventarioArticolo;
import it.eurotn.panjea.magazzino.domain.RigaInventarioArticolo;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzinoLite;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;
import it.eurotn.panjea.magazzino.util.InventarioArticoloDTO;
import it.eurotn.panjea.magazzino.util.ParametriRicercaAreaMagazzino;
import it.eurotn.panjea.rich.PanjeaLifecycleApplicationEvent;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.binding.PanjeaTextFieldDateEditor;
import it.eurotn.rich.dialog.MessageAlert;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import org.apache.commons.io.FileUtils;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.support.ActionCommandInterceptorAdapter;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.list.JideList;
import com.jidesoft.list.SortableListModel;
import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;
import com.toedter.calendar.JDateChooser;

public class InventarioArticoloTablePage extends AbstractTablePageEditor<InventarioArticolo> {

	private class AvvaloraGiacenzeRealiCommand extends ApplicationWindowAwareCommand {

		public static final String COMMAND_ID = "avvaloraGiacenzeRealiCommand";

		/**
		 * Costruttore.
		 * 
		 */
		public AvvaloraGiacenzeRealiCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {

			magazzinoDocumentoBD.avvaloraGiacenzaRealeInventarioArticolo(data, deposito);

			loadData();
		}

	}

	private class GeneraInventarioCommand extends ApplicationWindowAwareCommand {

		public static final String COMMAND_ID = "generaInventarioCommand";

		/**
		 * Costruttore.
		 * 
		 */
		public GeneraInventarioCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		public void doExecuteCommand() {

			String errorMessge = "";

			if (dataInventario.getDate() == null) {
				errorMessge = "Inserire una data valida per la generazione dell'inventario.";
			} else if (data.after(dataInventario.getDate())) {
				errorMessge = "La data per la generazione dell'inventario deve essere successiva a quella di preparazione. ( "
						+ new SimpleDateFormat("dd/MM/yy").format(data) + " )";
			} else {
				AreaMagazzinoLite ultimoInventario = magazzinoDocumentoBD.caricaUltimoInventario(deposito.getId());

				if (ultimoInventario != null
						&& dataInventario.getDate().before(ultimoInventario.getDocumento().getDataDocumento())) {
					errorMessge = "Data generazione inventario non corretta. Ultimo inventario presente per il deposito in data "
							+ new SimpleDateFormat("dd/MM/yy").format(ultimoInventario.getDocumento()
									.getDataDocumento());
				}
			}

			if (errorMessge.isEmpty()) {
				List<AreaMagazzinoRicerca> areeGenerate = magazzinoDocumentoBD.generaInventario(
						dataInventario.getDate(), data, deposito);

				ParametriRicercaAreaMagazzino parametri = new ParametriRicercaAreaMagazzino();
				parametri.setEffettuaRicerca(true);
				parametri.setAnnoCompetenza(aziendaCorrente.getAnnoMagazzino());
				parametri.setAreeMagazzino(areeGenerate);
				LifecycleApplicationEvent event = new OpenEditorEvent(parametri);
				Application.instance().getApplicationContext().publishEvent(event);

				// dopo aver creato l'inventario lancio l'evento della cancellazione di quello in preparazione
				InventarioArticoloDTO inventarioArticoloDTO = new InventarioArticoloDTO(data, deposito);
				PanjeaLifecycleApplicationEvent deleteEvent = new PanjeaLifecycleApplicationEvent(
						LifecycleApplicationEvent.DELETED, inventarioArticoloDTO, this);
				Application.instance().getApplicationContext().publishEvent(deleteEvent);
			} else {
				MessageDialog dialog = new MessageDialog("ATTENZIONE", errorMessge);
				dialog.showDialog();
			}
		}

	}

	private class ImportaArticoliInventariCommand extends ActionCommand {

		public static final String COMMAND_ID = "importaArticoliInventariCommand";

		/**
		 * Costruttore.
		 */
		public ImportaArticoliInventariCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			JFileChooser fileChooser = new JFileChooser();
			int returnVal = fileChooser.showOpenDialog(null);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				MessageAlert importMessageAlert = new MessageAlert(new DefaultMessage("Importazione in corso...",
						Severity.INFO), 0);
				importMessageAlert.showAlert();
				articoliMancantiControl.setVisible(false);

				try {
					File file = fileChooser.getSelectedFile();

					byte[] fileToByte = null;
					try {
						fileToByte = FileUtils.readFileToByteArray(file);

					} catch (IOException e) {
						logger.error("--> errore durante il caricamento del file di importazione.", e);
						PanjeaSwingUtil.checkAndThrowException(new GenericException(
								"errore durante il caricamento del file di importazione."));
					}

					List<String> articoloMancanti = magazzinoDocumentoBD.importaArticoliInventario(fileToByte,
							deposito.getId());
					refreshData();

					MessageDialog dialog = null;
					if (articoloMancanti.isEmpty()) {
						dialog = new MessageDialog("ATTENZIONE", "Importazione terminata con successo.");
					} else {
						SortableListModel model = (SortableListModel) articoliMancantiList.getModel();
						DefaultListModel defaultListModel = (DefaultListModel) model.getActualModel();
						defaultListModel.clear();
						for (String codiceArticolo : articoloMancanti) {
							defaultListModel.addElement(codiceArticolo);
						}
						articoliMancantiControl.setVisible(true);

						dialog = new MessageDialog("ATTENZIONE", articoloMancanti.size()
								+ " articoli non sono presenti nell'anagrafica.");
					}
					dialog.showDialog();
				} finally {
					importMessageAlert.closeAlert();
				}
			}
		}

	}

	private class ModificaQuantitaCommand extends ActionCommand {

		@Override
		protected void doExecuteCommand() {
			InventarioArticolo invSelected = getTable().getSelectedObject();

			if (invSelected != null && invSelected.getNumeroRighe() > 0) {
				ModificaQuantitaDialog dialog = new ModificaQuantitaDialog(
						magazzinoDocumentoBD.caricaInventarioArticolo(invSelected));
				dialog.showDialog();

				InventarioArticolo inv = dialog.getInventarioArticolo();
				if (inv != null) {
					inv = magazzinoDocumentoBD.salvaInventarioArticolo(inv);

					Integer numeroRighe = inv.getRigheInventarioArticolo() != null ? inv.getRigheInventarioArticolo()
							.size() : 0;
					BigDecimal qtaRighe = BigDecimal.ZERO;
					if (inv.getRigheInventarioArticolo() != null) {
						for (RigaInventarioArticolo rigaInventarioArticolo : inv.getRigheInventarioArticolo()) {
							qtaRighe = qtaRighe.add(rigaInventarioArticolo.getQuantita());
						}
					}
					invSelected.setNumeroRighe(new Long(numeroRighe));
					invSelected.setQtaRighe(qtaRighe);

					getTable().replaceRowObject(invSelected, invSelected, null);
				}
				dialog = null;
			}
		}

	}

	private class MostraTutteGiacenzeAction extends AbstractAction {

		private static final long serialVersionUID = 4160252065507305273L;

		@Override
		public void actionPerformed(ActionEvent e) {

			caricaGiacenzeAZero = !caricaGiacenzeAZero;
			refreshData();
		}

	}

	public static final String PAGE_ID = "inventarioArticoloTablePage";

	private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

	private Date data;

	private DepositoLite deposito;

	private boolean caricaGiacenzeAZero = false;
	private JDateChooser dataInventario = null;
	private IMagazzinoDocumentoBD magazzinoDocumentoBD;

	private JLabel dataLabel = new JLabel();

	private JLabel depositoLabel = new JLabel();

	private GeneraInventarioCommand generaInventarioCommand;
	private StampaInventarioArticoloCommand stampaInventarioArticoloCommand;

	private AvvaloraGiacenzeRealiCommand avvaloraGiacenzeRealiCommand;
	private ImportaArticoliInventariCommand importaArticoliInventariCommand;
	private AziendaCorrente aziendaCorrente;

	private JComponent articoliMancantiControl;
	private JideList articoliMancantiList;

	{
		dataInventario = new JDateChooser(new PanjeaTextFieldDateEditor("dd/MM/yy", "##/##/##", '_'));
		dataInventario.getDateEditor().getUiComponent().setName("dataInventario");
	}

	/**
	 * Costruttore.
	 * 
	 */
	protected InventarioArticoloTablePage() {
		super(PAGE_ID, new InventarioArticoloTableModel());
		getTable().setPropertyCommandExecutor(new ModificaQuantitaCommand());
	}

	/**
	 * Crea i controlli della lista che visualizza gli articoli mancanti nell'importazione.
	 */
	private JComponent createArticoliMancantiControl() {
		DefaultListModel listModel = new DefaultListModel();

		final SortableListModel sortableListModel = new SortableListModel(listModel);
		sortableListModel.sort(SortableListModel.UNSORTED);
		articoliMancantiList = new JideList(sortableListModel);
		articoliMancantiList.setVisibleRowCount(20);
		articoliMancantiControl = new JPanel(new BorderLayout(6, 6));
		articoliMancantiControl.add(new JScrollPane(articoliMancantiList));
		JCheckBox checkBox = new JCheckBox("Ordina");
		checkBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					sortableListModel.sort(SortableListModel.SORT_ASCENDING);
				} else {
					sortableListModel.sort(SortableListModel.UNSORTED);
				}
			}
		});

		checkBox.setSelected(false);
		articoliMancantiControl.add(checkBox, BorderLayout.BEFORE_FIRST_LINE);
		articoliMancantiControl.setBorder(BorderFactory.createTitledBorder("Articoli mancanti"));
		articoliMancantiControl.setVisible(false);
		articoliMancantiList.setPreferredSize(new Dimension(150, 100));
		articoliMancantiList.setMinimumSize(new Dimension(150, 100));
		return articoliMancantiControl;
	}

	@Override
	protected JComponent createControl() {
		JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout());
		rootPanel.add(super.createControl(), BorderLayout.CENTER);
		rootPanel.add(createArticoliMancantiControl(), BorderLayout.WEST);

		return rootPanel;
	}

	@Override
	public JComponent createToolbar() {
		JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout());
		rootPanel.add(super.createToolbar(), BorderLayout.CENTER);
		rootPanel.add(dataInventario, BorderLayout.EAST);
		return rootPanel;
	}

	/**
	 * @return the avvaloraGiacenzeRealiCommand
	 */
	public AvvaloraGiacenzeRealiCommand getAvvaloraGiacenzeRealiCommand() {
		if (avvaloraGiacenzeRealiCommand == null) {
			avvaloraGiacenzeRealiCommand = new AvvaloraGiacenzeRealiCommand();
		}

		return avvaloraGiacenzeRealiCommand;
	}

	@Override
	public AbstractCommand[] getCommands() {
		return new AbstractCommand[] { getImportaArticoliInventariCommand(), getAvvaloraGiacenzeRealiCommand(),
				getStampaInventarioArticoloCommand(), getGeneraInventarioCommand() };
	}

	/**
	 * @return the generaInventarioCommand
	 */
	public GeneraInventarioCommand getGeneraInventarioCommand() {
		if (generaInventarioCommand == null) {
			generaInventarioCommand = new GeneraInventarioCommand();
		}

		return generaInventarioCommand;
	}

	@Override
	public JComponent getHeaderControl() {

		FormLayout layout = new FormLayout(
				"right:pref, 4dlu, left:pref, 15dlu, right:pref, 4dlu, left:pref,15dlu, right:pref:grow", "default");
		JPanel headerPanel = getComponentFactory().createPanel(layout);

		CellConstraints cc = new CellConstraints();
		headerPanel.add(new JLabel(RcpSupport.getMessage("data"), RcpSupport.getIcon(Date.class.getName()),
				SwingConstants.LEFT), cc.xy(1, 1));
		headerPanel.add(dataLabel, cc.xy(3, 1));
		headerPanel.add(new JLabel(RcpSupport.getMessage("deposito"), RcpSupport.getIcon(Deposito.class.getName()),
				SwingConstants.LEFT), cc.xy(5, 1));
		headerPanel.add(depositoLabel, cc.xy(7, 1));

		JCheckBox showGiacenzeCheckBox = new JCheckBox(new MostraTutteGiacenzeAction());
		showGiacenzeCheckBox.setName("showGiacenzeCheckBox");
		showGiacenzeCheckBox.setText("Mostra gli articoli con giacenza uguale a zero");
		headerPanel.add(showGiacenzeCheckBox, cc.xy(9, 1));

		headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		return headerPanel;
	}

	/**
	 * @return Returns the importaArticoliInventariCommand.
	 */
	public ImportaArticoliInventariCommand getImportaArticoliInventariCommand() {
		if (importaArticoliInventariCommand == null) {
			importaArticoliInventariCommand = new ImportaArticoliInventariCommand();
		}

		return importaArticoliInventariCommand;
	}

	/**
	 * @return the stampaInventarioArticoloCommand
	 */
	public StampaInventarioArticoloCommand getStampaInventarioArticoloCommand() {
		if (stampaInventarioArticoloCommand == null) {
			stampaInventarioArticoloCommand = new StampaInventarioArticoloCommand();
			stampaInventarioArticoloCommand.addCommandInterceptor(new ActionCommandInterceptorAdapter() {
				@Override
				public boolean preExecution(ActionCommand command) {

					command.addParameter(StampaInventarioArticoloCommand.PARAM_DATA, data);
					command.addParameter(StampaInventarioArticoloCommand.PARAM_DEPOSITO, deposito.getId());
					command.addParameter(StampaInventarioArticoloCommand.PARAM_STAMPA_GIACENZE_A_ZERO, new Boolean(
							caricaGiacenzeAZero));

					return super.preExecution(command);
				}
			});
		}

		return stampaInventarioArticoloCommand;
	}

	@Override
	public Collection<InventarioArticolo> loadTableData() {

		List<InventarioArticolo> inventari = Collections.emptyList();

		if (data != null && deposito != null) {
			inventari = magazzinoDocumentoBD.caricaInventarioArticolo(data, deposito, caricaGiacenzeAZero);
		}

		return inventari;
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public void processTableData(Collection<InventarioArticolo> results) {
		getTable().setRows(new ArrayList<InventarioArticolo>());
		super.processTableData(results);

		if (data != null && deposito != null) {
			dataLabel.setText(dateFormat.format(data));
			depositoLabel.setText(ObjectConverterManager.toString(deposito));
		}
	}

	@Override
	public Collection<InventarioArticolo> refreshTableData() {
		return loadTableData();
	}

	/**
	 * @param aziendaCorrente
	 *            the aziendaCorrente to set
	 */
	public void setAziendaCorrente(AziendaCorrente aziendaCorrente) {
		this.aziendaCorrente = aziendaCorrente;
	}

	@Override
	public void setFormObject(Object object) {

		data = null;
		deposito = null;

		if (object != null) {
			InventarioArticoloDTO inventarioArticoloDTO = (InventarioArticoloDTO) object;
			data = inventarioArticoloDTO.getData();
			deposito = inventarioArticoloDTO.getDeposito();
		}
	}

	/**
	 * @param magazzinoDocumentoBD
	 *            the magazzinoDocumentoBD to set
	 */
	public void setMagazzinoDocumentoBD(IMagazzinoDocumentoBD magazzinoDocumentoBD) {
		this.magazzinoDocumentoBD = magazzinoDocumentoBD;
	}

}
