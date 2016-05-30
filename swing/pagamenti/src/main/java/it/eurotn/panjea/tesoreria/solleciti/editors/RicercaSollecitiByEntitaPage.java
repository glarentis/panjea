package it.eurotn.panjea.tesoreria.solleciti.editors;

import it.eurotn.panjea.anagrafica.domain.Cliente;
import it.eurotn.panjea.rate.domain.Rata.StatoRata;
import it.eurotn.panjea.rich.factory.PanjeaComponentFactory;
import it.eurotn.panjea.tesoreria.rich.bd.ITesoreriaBD;
import it.eurotn.panjea.tesoreria.solleciti.RigaSollecito;
import it.eurotn.panjea.tesoreria.solleciti.Sollecito;
import it.eurotn.rich.binding.PanjeaTextFieldDateEditor;
import it.eurotn.rich.control.table.JideTableWidget.TableType;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.awt.BorderLayout;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.util.RcpSupport;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.matchers.Matcher;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.grid.DefaultGroupRow;
import com.jidesoft.grid.GroupTable;
import com.toedter.calendar.JDateChooser;

public class RicercaSollecitiByEntitaPage extends AbstractTablePageEditor<RigaSollecito> {

	private class CercaSollecitiCommand extends ApplicationWindowAwareCommand {

		public static final String COMMAND_ID = "cercaSollecitiCommand";

		/**
		 * Costruttore.
		 */
		public CercaSollecitiCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			Date daData = dataIniziale.getDate();
			Date aData = dataFinale.getDate();

			if (daData != null && aData != null) {
				refreshData();
			}
		}

	}

	private class DeleteSollecitoCommand extends ApplicationWindowAwareCommand {

		public static final String COMMAND_ID = "deleteCommand";

		private Sollecito sollecito = null;

		/**
		 * Costruttore.
		 */
		public DeleteSollecitoCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {

			sollecito = getSollecitoSelezionato();

			if (sollecito != null) {
				ConfirmationDialog dialog = new ConfirmationDialog("ATTENZIONE", "Cancellare il sollecito "
						+ ObjectConverterManager.toString(sollecito) + " ?") {

					@Override
					protected void onConfirm() {
						tesoreriaBD.cancellaSollecito(sollecito);
						refreshTableData();
					}
				};
				dialog.showDialog();

			}

		}

	}

	private class StampaInviaSollecitoCommand extends ApplicationWindowAwareCommand {

		public static final String COMMAND_ID = "stampaInviaSollecitoCommand";

		/**
		 * Costruttore.
		 */
		public StampaInviaSollecitoCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {

			Sollecito sollecito = getSollecitoSelezionato();

			if (sollecito != null) {
				List<Sollecito> sollecitiStampa = new ArrayList<Sollecito>();
				sollecitiStampa.add(sollecito);

				StampaSollecitiDialog dialog = RcpSupport.getBean(StampaSollecitiDialog.PAGE_ID);
				dialog.setSolleciti(sollecitiStampa);
				dialog.showDialog();

				refreshTableData();
			}

		}

	}

	public static final String PAGE_ID = "ricercaSollecitoByEntitaPage";

	protected ITesoreriaBD tesoreriaBD;

	private Cliente cliente;

	private JDateChooser dataFinale;
	private JDateChooser dataIniziale;
	private JCheckBox chiusiCheckBox;

	private DeleteSollecitoCommand deleteSollecitoCommand;
	private StampaInviaSollecitoCommand stampaInviaSollecitoCommand;
	private CercaSollecitiCommand cercaSollecitiCommand;

	/**
	 * Costruttore.
	 */
	protected RicercaSollecitiByEntitaPage() {
		super(PAGE_ID, new RicercaSollecitiByEntitaTableModel());
		getTable().setTableType(TableType.GROUP);
		getTable().setAggregatedColumns(new String[] { "sollecito" });
		getTable().getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

	/**
	 * Costruttore.
	 * 
	 * @param pageId
	 *            id della pagina
	 * @param ricercaSollecitiTableModel
	 *            table model
	 */
	public RicercaSollecitiByEntitaPage(final String pageId, final RicercaSollecitiTableModel ricercaSollecitiTableModel) {
		super(pageId, ricercaSollecitiTableModel);
		getTable().setTableType(TableType.GROUP);
		getTable().setAggregatedColumns(new String[] { "sollecito" });
		getTable().getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

	/**
	 * filtro secondo i parametri di recerca.
	 * 
	 * @param solleciti
	 *            .
	 * @return lista filtrata.
	 */
	protected List<Sollecito> controlfilter(List<Sollecito> solleciti) {
		EventList<Sollecito> sollecitoEvent = new BasicEventList<Sollecito>();
		sollecitoEvent.addAll(solleciti);
		FilterList<Sollecito> filterList;

		Matcher<Sollecito> matcherIsOpen = new Matcher<Sollecito>() {
			@Override
			public boolean matches(Sollecito sollecito) {
				boolean aperto = false;
				for (RigaSollecito rigaSollecito : sollecito.getRigheSollecito()) {
					rigaSollecito.getRata().getStatoRata();
					if (rigaSollecito.getRata().getStatoRata() != StatoRata.CHIUSA) {
						aperto = true;
					}

				}
				return aperto;
			}
		};

		Matcher<Sollecito> matcherData = new Matcher<Sollecito>() {

			@Override
			public boolean matches(Sollecito arg0) {
				return dataIniziale.getDate().compareTo(arg0.getDataCreazione()) <= 0
						&& dataFinale.getDate().compareTo(arg0.getDataCreazione()) >= 0;
			}
		};

		filterList = new FilterList<Sollecito>(sollecitoEvent, matcherData);
		if (!chiusiCheckBox.isSelected()) {
			filterList = new FilterList<Sollecito>(filterList, matcherIsOpen);
		}
		return filterList;
	}

	/**
	 * @return the cercaSollecitiCommand
	 */
	public CercaSollecitiCommand getCercaSollecitiCommand() {
		if (cercaSollecitiCommand == null) {
			cercaSollecitiCommand = new CercaSollecitiCommand();
		}

		return cercaSollecitiCommand;
	}

	@Override
	public AbstractCommand[] getCommands() {
		return new AbstractCommand[] { getStampaInviaSollecitoCommand(), getDeleteSollecitoCommand(),
				getRefreshCommand() };
	}

	/**
	 * @return the deleteSollecitoCommand
	 */
	public DeleteSollecitoCommand getDeleteSollecitoCommand() {
		if (deleteSollecitoCommand == null) {
			deleteSollecitoCommand = new DeleteSollecitoCommand();
		}

		return deleteSollecitoCommand;
	}

	@Override
	public JComponent getHeaderControl() {
		JPanel filterPanel = getComponentFactory().createPanel(new BorderLayout(30, 10));
		filterPanel.setBorder(BorderFactory.createTitledBorder("Parametri ricerca"));
		FormLayout layout = new FormLayout(
				"left:pref,5dlu,left:pref, 5dlu,left:pref, 5dlu,left:pref, 5dlu,left:pref,5dlu,left:pref", "default");
		PanelBuilder parametriPanel = new PanelBuilder(layout);
		parametriPanel.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		Date now = new Date();
		chiusiCheckBox = getComponentFactory().createCheckBox("Chiusi");

		chiusiCheckBox.setSelected(false);
		JLabel daDataLabel = new JLabel("Da data");
		JLabel finoDataLabel = new JLabel("Fino data");
		dataIniziale = ((PanjeaComponentFactory) getComponentFactory())
				.createDateChooser(new PanjeaTextFieldDateEditor("dd/MM/yy", "##/##/##", '_'));
		DateFormat format = new SimpleDateFormat("dd/MM/yy");
		try {
			dataIniziale.setDate(format.parse("01/01/10"));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		dataFinale = ((PanjeaComponentFactory) getComponentFactory()).createDateChooser(new PanjeaTextFieldDateEditor(
				"dd/MM/yy", "##/##/##", '_'));
		dataFinale.setDate(now);
		parametriPanel.add(daDataLabel, cc.xy(1, 1));
		parametriPanel.add(dataIniziale, cc.xy(3, 1));
		parametriPanel.add(finoDataLabel, cc.xy(5, 1));
		parametriPanel.add(dataFinale, cc.xy(7, 1));
		parametriPanel.add(chiusiCheckBox, cc.xy(9, 1));
		parametriPanel.add(getCercaSollecitiCommand().createButton(), cc.xy(11, 1));
		filterPanel.add(parametriPanel.getPanel());
		return filterPanel;
	}

	/**
	 * Restituisce il sollecito selezionato.
	 * 
	 * @return sollecito selezionato, <code>null</code> se non esiste
	 */
	public Sollecito getSollecitoSelezionato() {

		Sollecito sollecito = null;

		RigaSollecito rigaSollecito = getTable().getSelectedObject();

		if (rigaSollecito != null) {
			sollecito = rigaSollecito.getSollecito();
		} else {
			// sono su una riga gruppata quindi prendo il primo figlio come riferimento
			GroupTable groupTable = (GroupTable) getTable().getTable();
			if (groupTable.getSelectedRow() != -1) {
				DefaultGroupRow obj = (DefaultGroupRow) groupTable.getModel()
						.getValueAt(groupTable.getSelectedRow(), 0);

				if (obj.getConditionValue(0) instanceof Sollecito) {
					sollecito = (Sollecito) obj.getConditionValue(0);
				}
			}
		}

		return sollecito;
	}

	/**
	 * @return the stampaInviaSollecitoCommand
	 */
	public StampaInviaSollecitoCommand getStampaInviaSollecitoCommand() {
		if (stampaInviaSollecitoCommand == null) {
			stampaInviaSollecitoCommand = new StampaInviaSollecitoCommand();
		}

		return stampaInviaSollecitoCommand;
	}

	@Override
	public Collection<RigaSollecito> loadTableData() {
		List<RigaSollecito> righeSolleciti = new ArrayList<RigaSollecito>();

		if (cliente != null) {
			List<Sollecito> solleciti = tesoreriaBD.caricaSollecitiByCliente(cliente.getCodice());
			List<Sollecito> sollecitiFilterList = controlfilter(solleciti);

			for (Sollecito sollecito : sollecitiFilterList) {
				righeSolleciti.addAll(sollecito.getRigheSollecito());
			}
		}

		return righeSolleciti;
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return cliente != null && cliente.getId() != null;
	}

	@Override
	public Collection<RigaSollecito> refreshTableData() {
		return loadTableData();
	}

	@Override
	public void setFormObject(Object object) {
		this.cliente = (Cliente) object;
	}

	/**
	 * @param tesoreriaBD
	 *            the tesoreriaBD to set
	 */
	public void setTesoreriaBD(ITesoreriaBD tesoreriaBD) {
		this.tesoreriaBD = tesoreriaBD;
	}

}
