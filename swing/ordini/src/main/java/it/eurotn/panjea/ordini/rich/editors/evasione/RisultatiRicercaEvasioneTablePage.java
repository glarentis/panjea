package it.eurotn.panjea.ordini.rich.editors.evasione;

import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCarico;
import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCarico.GestioneGiacenza;
import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCarico.StatoRiga;
import it.eurotn.panjea.ordini.rich.bd.IOrdiniDocumentoBD;
import it.eurotn.panjea.ordini.util.parametriricerca.ParametriRicercaEvasione;
import it.eurotn.rich.command.JideToggleCommand;
import it.eurotn.rich.control.table.JideTableWidget.TableType;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.settings.SettingsException;
import org.springframework.richclient.settings.SettingsManager;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.TableModelWrapperUtils;

public class RisultatiRicercaEvasioneTablePage extends AbstractTablePageEditor<RigaDistintaCarico> {

	private class AggiungiRigheEvasioneCommand extends ActionCommand {

		public static final String COMMAND_ID = "aggiungiRigheEvasioneCommand";

		/**
		 * Costruttore.
		 */
		public AggiungiRigheEvasioneCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			if (controller.isGiacenzaCaluculatorRunning()) {
				return;
			}
			controller.aggiungiRighe(RisultatiRicercaEvasioneTablePage.this.getTable().getVisibleObjects());
		}
	}

	public static final String PAGE_ID = "risultatiRicercaEvasioneTablePage";

	private ParametriRicercaEvasione parametriRicercaEvasione;
	private IOrdiniDocumentoBD ordiniDocumentoBD;
	private RisultatiRicercaEvasioneController controller;

	private JideToggleCommand daEvadereEqInevasaConGiacCommand;

	public static final String GESTIONE_GIACENZA_SETTING = PAGE_ID + ".gestioneGiacenza";

	private Boolean isIgnoraGiacenza = null;

	/**
	 * Costruttore.
	 */
	protected RisultatiRicercaEvasioneTablePage() {
		super(PAGE_ID, new RisultatiRicercaEvasioneTableModel());
		getTable().setTableType(TableType.GROUP);
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public AbstractCommand[] getCommands() {
		return new AbstractCommand[] { getDaEvadereEqInevasaConGiacCommand(), new AggiungiRigheEvasioneCommand() };
	}

	/**
	 * @return the daEvadereEqInevasaConGiacCommand
	 */
	public JideToggleCommand getDaEvadereEqInevasaConGiacCommand() {
		if (daEvadereEqInevasaConGiacCommand == null) {
			daEvadereEqInevasaConGiacCommand = new DaEvadereEqInevasaConGiacCommand();
		}

		return daEvadereEqInevasaConGiacCommand;
	}

	/**
	 * @return <code>false</code> la qta da evadere = qtaInevasa sempre. <code>true</code> la qta da evadere =
	 *         qtaInevasa se giacenza>qtaInevasa altrimenti qta da evadere=giacenza
	 */
	public GestioneGiacenza getGestioneCalcoloQtaDaEvadere() {

		// nel caso in cui apro l'editor con i parametri con evasione
		// diretta mi vien chiamata prima la load data e
		// l'aggiunta delle righe nel carrello e poi il restoreSettings. Per
		// questo motivi verifico che sia stata
		// inizializzata la setting e ne carico il valore.
		if (isIgnoraGiacenza == null) {
			SettingsManager settings = (SettingsManager) Application.instance().getApplicationContext()
					.getBean("settingManagerLocal");
			try {
				boolean selectCommand = settings.getUserSettings().getBoolean(GESTIONE_GIACENZA_SETTING);
				getDaEvadereEqInevasaConGiacCommand().setSelected(selectCommand);
			} catch (SettingsException e) {
				throw new RuntimeException(e);
			}
		}
		isIgnoraGiacenza = getDaEvadereEqInevasaConGiacCommand().isSelected();
		return isIgnoraGiacenza ? GestioneGiacenza.DAEVADERE_EQ_INEVASA : GestioneGiacenza.DAEVADERE_CONSIDERA_GIACENZA;
	}

	/**
	 * 
	 * @return tableModel della pagina
	 */
	RisultatiRicercaEvasioneTableModel getTableModel() {
		RisultatiRicercaEvasioneTableModel tableModel = (RisultatiRicercaEvasioneTableModel) TableModelWrapperUtils
				.getActualTableModel(getTable().getTable().getModel());
		return tableModel;
	}

	@Override
	public Collection<RigaDistintaCarico> loadTableData() {
		List<RigaDistintaCarico> righeEvasione = new ArrayList<RigaDistintaCarico>();
		if (this.parametriRicercaEvasione != null && this.parametriRicercaEvasione.isEffettuaRicerca()) {
			righeEvasione = ordiniDocumentoBD.caricaRigheEvasione(parametriRicercaEvasione);
		}

		return righeEvasione;
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public void processTableData(Collection<RigaDistintaCarico> results) {
		if (this.parametriRicercaEvasione != null && this.parametriRicercaEvasione.isEffettuaRicerca()) {

			if (parametriRicercaEvasione.isEvadiOrdini()) {
				for (RigaDistintaCarico rigaDistintaCarico : results) {
					rigaDistintaCarico.setStato(StatoRiga.NEL_CARRELLO);
					controller.sincronizzaRiga(rigaDistintaCarico);
				}
			}
		}
		super.processTableData(results);
		controller.aggiornaStatoRighe();
	}

	@Override
	public Collection<RigaDistintaCarico> refreshTableData() {
		return loadTableData();
	}

	@Override
	public void restoreState(Settings settings) {
		super.restoreState(settings);
		// carrelloEvasionePage.restoreState(settings);
		if (settings.contains(GESTIONE_GIACENZA_SETTING)) {
			isIgnoraGiacenza = settings.getBoolean(GESTIONE_GIACENZA_SETTING);
			getDaEvadereEqInevasaConGiacCommand().setSelected(settings.getBoolean(GESTIONE_GIACENZA_SETTING));
		}
	}

	@Override
	public void saveState(Settings settings) {
		super.saveState(settings);
		settings.setBoolean(GESTIONE_GIACENZA_SETTING, getDaEvadereEqInevasaConGiacCommand().isSelected());
		// carrelloEvasionePage.saveState(settings);
	}

	/**
	 * @param risultatiRicercaEvasioneController
	 *            The risultatiRicercaEvasioneController to set.
	 */
	public void setController(RisultatiRicercaEvasioneController risultatiRicercaEvasioneController) {
		this.controller = risultatiRicercaEvasioneController;
	}

	// /**
	// * @param carrelloEvasionePage
	// * the carrelloEvasionePage to set
	// */
	// public void setCarrelloEvasionePage(final CarrelloEvasioneOrdiniTablePage carrelloEvasionePage) {
	// this.carrelloEvasionePage = carrelloEvasionePage;
	// this.carrelloEvasionePage.getSvuotaCarrelloCommand().addCommandInterceptor(new ActionCommandInterceptor() {
	//
	// @Override
	// public void postExecution(ActionCommand arg0) {
	// // setto tutte le righe come selezionabili dopo aver svuotato il
	// // carrello
	// for (RigaDistintaCarico rigaEvasione : getTable().getRows()) {
	// rigaEvasione.setStato(StatoRiga.SELEZIONABILE);
	// }
	// getTableModel().fireTableDataChanged();
	// }
	//
	// @Override
	// public boolean preExecution(ActionCommand arg0) {
	// return true;
	// }
	// });
	// getTableModel().addTableModelListener(new TableModelListener() {
	//
	// @Override
	// public void tableChanged(TableModelEvent e) {
	// System.out.println(e.getColumn() + ":" + e.getType() + ":" + e.getFirstRow() + ":" + e.getLastRow());
	//
	// }
	// });
	// this.carrelloEvasionePage.getRimuoviRigheSelezionateCommand().addCommandInterceptor(
	// new ActionCommandInterceptor() {
	//
	// @Override
	// public void postExecution(ActionCommand actioncommand) {
	// }
	//
	// @Override
	// public boolean preExecution(ActionCommand actioncommand) {
	// List<RigaDistintaCarico> righeSelezionate = carrelloEvasionePage.getTable()
	// .getSelectedObjects();
	//
	// for (RigaDistintaCarico rigaEvasione : righeSelezionate) {
	// rigaEvasione.setStato(StatoRiga.SELEZIONABILE);
	// RisultatiRicercaEvasioneTablePage.this.getTable().replaceOrAddRowObject(rigaEvasione,
	// rigaEvasione, RisultatiRicercaEvasioneTablePage.this);
	// }
	// return true;
	// }
	// });
	//
	// this.carrelloEvasionePage.getForzaRimuoviRigheSelezionateCommand().addCommandInterceptor(
	// new ActionCommandInterceptor() {
	//
	// @Override
	// public void postExecution(ActionCommand paramActionCommand) {
	// }
	//
	// @Override
	// public boolean preExecution(ActionCommand paramActionCommand) {
	// List<RigaDistintaCarico> righeSelezionate = carrelloEvasionePage.getTable()
	// .getSelectedObjects();
	//
	// for (RigaDistintaCarico rigaEvasione : righeSelezionate) {
	// RisultatiRicercaEvasioneTablePage.this.getTable().removeRowObject(rigaEvasione);
	// }
	// return true;
	// }
	// });
	// }

	@Override
	public void setFormObject(Object object) {
		this.parametriRicercaEvasione = (ParametriRicercaEvasione) object;
	}

	/**
	 * @param ordiniDocumentoBD
	 *            the ordiniDocumentoBD to set
	 */
	public void setOrdiniDocumentoBD(IOrdiniDocumentoBD ordiniDocumentoBD) {
		this.ordiniDocumentoBD = ordiniDocumentoBD;
	}

}
