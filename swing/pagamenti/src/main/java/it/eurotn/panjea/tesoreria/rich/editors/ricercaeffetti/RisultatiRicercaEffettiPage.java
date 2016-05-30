/**
 *
 */
package it.eurotn.panjea.tesoreria.rich.editors.ricercaeffetti;

import it.eurotn.panjea.tesoreria.domain.AreaTesoreria;
import it.eurotn.panjea.tesoreria.rich.bd.ITesoreriaBD;
import it.eurotn.panjea.tesoreria.rich.editors.ricercaeffetti.EffettiLayoutManager.Mode;
import it.eurotn.panjea.tesoreria.rich.editors.ricercaeffetti.carrello.CarrelloEffettiPage;
import it.eurotn.panjea.tesoreria.rich.editors.ricercaeffetti.carrello.CarrelloEffettiPage.GeneraCommand;
import it.eurotn.panjea.tesoreria.util.ParametriRicercaAreeTesoreria;
import it.eurotn.panjea.tesoreria.util.ParametriRicercaEffetti;
import it.eurotn.panjea.tesoreria.util.SituazioneEffetto;
import it.eurotn.panjea.tesoreria.util.SituazioneEffetto.StatoCarrello;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.TableModelWrapperUtils;
import com.jidesoft.pivot.AggregateTable;
import com.jidesoft.pivot.AggregateTableModel;
import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;
import com.jidesoft.swing.JideSplitPane;
import com.jidesoft.swing.JideTabbedPane;

/**
 * @author Leonardo
 *
 */
public class RisultatiRicercaEffettiPage extends AbstractTablePageEditor<SituazioneEffetto> implements InitializingBean {

	private class AggiungiSituazioneEffettoSelezionateCommand extends ActionCommand {

		public static final String COMMAND_ID = "aggiungiSituazioneEffettoSelezionateCommand";

		/**
		 * Costruttore.
		 */
		public AggiungiSituazioneEffettoSelezionateCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
			setVisible(false);
		}

		@Override
		protected void doExecuteCommand() {

			List<SituazioneEffetto> situazioniEffettoDaAggiungere = new ArrayList<SituazioneEffetto>();

			for (SituazioneEffetto situazioneEffetto : getTableModel().getObjects()) {
				if (situazioneEffetto.getStatoCarrello() == StatoCarrello.DA_AGGIUNGERE) {
					situazioniEffettoDaAggiungere.add(situazioneEffetto);
				}
			}

			if (!situazioniEffettoDaAggiungere.isEmpty()) {
				carrelloEffettiPage.addSituazioneEffetti(situazioniEffettoDaAggiungere);
				aggiornaStatoEffetti(getTable().getRows());
			}
			AggregateTableModel aggregateTableModel = (AggregateTableModel) TableModelWrapperUtils.getActualTableModel(
					getTable().getTable().getModel(), AggregateTableModel.class);
			aggregateTableModel.aggregate();
		}

	}

	private class ApriDocumentoEffettoCommand extends ActionCommand {

		@Override
		protected void doExecuteCommand() {

			SituazioneEffetto situazioneEffetto = getTable().getSelectedObject();

			if (situazioneEffetto == null) {
				return;
			}

			AreaTesoreria areaTesoreria = tesoreriaBD.caricaAreaTesoreriaByStatoEffetto(situazioneEffetto.getEffetto());

			ParametriRicercaAreeTesoreria parametriRicerca = new ParametriRicercaAreeTesoreria();
			parametriRicerca.setAreeTesoreria(new ArrayList<AreaTesoreria>());
			parametriRicerca.getAreeTesoreria().add(areaTesoreria);
			parametriRicerca.setEffettuaRicerca(true);
			LifecycleApplicationEvent event = new OpenEditorEvent(parametriRicerca);
			Application.instance().getApplicationContext().publishEvent(event);
		}

	}

	private class CercaEffettiCommandInterceptor implements ActionCommandInterceptor {

		@Override
		public void postExecution(ActionCommand arg0) {
		}

		@Override
		public boolean preExecution(ActionCommand arg0) {

			switch (RisultatiRicercaEffettiPage.this.mode) {
			case ACCREDITI:
			case INSOLUTI:
			case ANTICIPI:
				ParametriRicercaEffetti parametri = (ParametriRicercaEffetti) parametriRicercaEffettiPage.getForm()
				.getFormObject();

				if (parametri.getDaDataValuta() == null || parametri.getADataValuta() == null) {
					MessageDialog dialog = new MessageDialog("ATTENZIONE", "Inserire un intervallo di date valide");
					dialog.showDialog();

					setRows(new ArrayList<SituazioneEffetto>());
					return false;
				}
			default:
				break;
			}
			return true;
		}

	}

	private class GeneraCommandInterceptor implements ActionCommandInterceptor {
		@Override
		public void postExecution(ActionCommand actioncommand) {

			Boolean generated = (Boolean) ((GeneraCommand) actioncommand).getParameter(GeneraCommand.PARAM_GENERATED);

			if (generated) {
				carrelloEffettiPage.getSvuotaCarrelloCommand().execute();
				parametriRicercaEffettiPage.getCercaEffettiCommand().execute();
			}
		}

		@Override
		public boolean preExecution(ActionCommand actioncommand) {
			return true;
		}
	}

	private class RimuoviEffettoCommandInterceptor implements ActionCommandInterceptor {
		@Override
		public void postExecution(ActionCommand actioncommand) {
			for (SituazioneEffetto situazioneEffetto : getTableModel().getObjects()) {
				situazioneEffetto.setStatoCarrello(StatoCarrello.SELEZIONABILE);
			}
			aggiornaStatoEffetti(getTableModel().getObjects());
			getTableModel().fireTableCellUpdated(-1, -1);
		}

		@Override
		public boolean preExecution(ActionCommand actioncommand) {
			return true;
		}
	}

	private static final String PAGE_ID = "risultatiRicercaEffettiPage";

	private ParametriRicercaEffetti parametriRicercaEffetti;

	private ITesoreriaBD tesoreriaBD;

	private CarrelloEffettiPage carrelloEffettiPage;

	private final LegendaRisultatiRicercaEffettiPanel legenda = new LegendaRisultatiRicercaEffettiPanel();

	private AggiungiSituazioneEffettoSelezionateCommand aggiungiSituazioneEffettoSelezionateCommand;

	private ParametriRicercaEffettiPage parametriRicercaEffettiPage;

	private Mode mode;

	private EffettiLayoutManager layoutManager;

	private ApriDocumentoEffettoCommand apriDocumentoEffettoCommand;

	private EffettoSelectRowListener effettoSelectRowListener;

	/**
	 * Costruttore.
	 */
	protected RisultatiRicercaEffettiPage() {
		super(PAGE_ID, new RisultatiRicercaEffettiTableModel(PAGE_ID));
		setShowTitlePane(false);
		layoutManager = new EffettiLayoutManager(getTable());
		getTable().getTableLayoutView().setLayoutManager(layoutManager);
		mode = Mode.GENERALE;
		effettoSelectRowListener = new EffettoSelectRowListener((AggregateTable) getTable().getTable());
		effettoSelectRowListener.setEnabledRow(mode.getColonneAbilitatePerSelezione());
		getTable().setPropertyCommandExecutor(getApriDocumentoEffettoCommand());
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		carrelloEffettiPage.setVisible(false);
		carrelloEffettiPage.getSvuotaCarrelloCommand().addCommandInterceptor(new RimuoviEffettoCommandInterceptor());
		carrelloEffettiPage.getCancellaEffettoCommand().addCommandInterceptor(new RimuoviEffettoCommandInterceptor());
		carrelloEffettiPage.getGeneraCommand().addCommandInterceptor(new GeneraCommandInterceptor());
		parametriRicercaEffettiPage.addPropertyChangeListener(IPageLifecycleAdvisor.OBJECT_CHANGED,
				new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				RisultatiRicercaEffettiPage.this.setFormObject(arg0.getNewValue());
				RisultatiRicercaEffettiPage.this.refreshData();
			}
		});

		parametriRicercaEffettiPage.getCercaEffettiCommand()
		.addCommandInterceptor(new CercaEffettiCommandInterceptor());
	}

	/**
	 * Aggiorna lo stato degli effetti in base al contenuto del carrello.
	 *
	 * @param effetti
	 *            effetti da aggiornare
	 */
	private void aggiornaStatoEffetti(List<SituazioneEffetto> effetti) {
		for (SituazioneEffetto situazioneEffetto : carrelloEffettiPage.getSituazioneEffetti()) {
			int index = effetti.indexOf(situazioneEffetto);
			if (index > -1) {
				effetti.get(index).setStatoCarrello(StatoCarrello.AGGIUNTO_CARRELLO);
			}
		}
	}

	@Override
	protected JComponent createControl() {
		JideSplitPane splitPane = new JideSplitPane(JideSplitPane.VERTICAL_SPLIT);
		splitPane.add(super.createControl());
		splitPane.add(carrelloEffettiPage.getControl());
		return splitPane;
	}

	/**
	 * @return the aggiungiSituazioneEffettoSelezionateCommand
	 */
	public AggiungiSituazioneEffettoSelezionateCommand getAggiungiSituazioneEffettoSelezionateCommand() {
		if (aggiungiSituazioneEffettoSelezionateCommand == null) {
			aggiungiSituazioneEffettoSelezionateCommand = new AggiungiSituazioneEffettoSelezionateCommand();
		}

		return aggiungiSituazioneEffettoSelezionateCommand;
	}

	/**
	 * @return Returns the apriDocumentoEffettoCommand.
	 */
	public ApriDocumentoEffettoCommand getApriDocumentoEffettoCommand() {
		if (apriDocumentoEffettoCommand == null) {
			apriDocumentoEffettoCommand = new ApriDocumentoEffettoCommand();
		}

		return apriDocumentoEffettoCommand;
	}

	/**
	 * @return the carrelloEffettiPage
	 */
	public CarrelloEffettiPage getCarrelloEffettiPage() {
		return carrelloEffettiPage;
	}

	@Override
	public AbstractCommand[] getCommands() {
		return new AbstractCommand[] { getAggiungiSituazioneEffettoSelezionateCommand() };
	}

	@Override
	public JComponent getFooterControl() {
		legenda.setVisible(false);
		return legenda;
	}

	@Override
	public JComponent getHeaderControl() {
		JPanel panel = getComponentFactory().createPanel(new BorderLayout());
		EffettiTabHeader effettiTabHeader = new EffettiTabHeader();
		effettiTabHeader.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				int index = ((JideTabbedPane) e.getSource()).getSelectedIndex();
				switch (index) {
				case 0:
					RisultatiRicercaEffettiPage.this.setMode(Mode.GENERALE);
					break;
				case 1:
					RisultatiRicercaEffettiPage.this.setMode(Mode.ACCREDITI);
					break;
				case 2:
					RisultatiRicercaEffettiPage.this.setMode(Mode.INSOLUTI);
					break;
				case 3:
					RisultatiRicercaEffettiPage.this.setMode(Mode.ANTICIPI);
				default:
					break;
				}
			}
		});
		panel.add(effettiTabHeader, BorderLayout.SOUTH);
		panel.add(parametriRicercaEffettiPage.getControl(), BorderLayout.CENTER);
		return panel;
	}

	/**
	 * @return tableModel della pagina
	 */
	RisultatiRicercaEffettiTableModel getTableModel() {
		RisultatiRicercaEffettiTableModel tableModel = (RisultatiRicercaEffettiTableModel) TableModelWrapperUtils
				.getActualTableModel(getTable().getTable().getModel());
		return tableModel;
	}

	@Override
	public Collection<SituazioneEffetto> loadTableData() {
		List<SituazioneEffetto> effetti = null;

		if (parametriRicercaEffetti.isEffettuaRicerca()) {

			// setto il parametro per l'esclusione degli effetti già accreditati solo per gli accrediti
			parametriRicercaEffetti.setEscludiEffettiAccreditati(mode == Mode.ACCREDITI);
			effetti = tesoreriaBD.ricercaEffetti(parametriRicercaEffetti);
		}

		return effetti;
	}

	/**
	 * Blocca la possibilità di raggruppare le colonne in base al mode impostato.
	 */
	private void lockAggregateColum() {

		final AggregateTable table = ((AggregateTable) getTable().getTable());

		switch (mode) {
		case ACCREDITI:
		case ANTICIPI:
		case INSOLUTI:
			table.getTableHeader().setReorderingAllowed(false);
			break;
		default:
			table.getTableHeader().setReorderingAllowed(true);
			break;
		}

	}

	@Override
	public void onPostPageOpen() {
		parametriRicercaEffettiPage.onPostPageOpen();
	};

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public void processTableData(java.util.Collection<SituazioneEffetto> results) {
		aggiornaStatoEffetti(new ArrayList<SituazioneEffetto>(results));
		super.processTableData(results);
	}

	@Override
	public Collection<SituazioneEffetto> refreshTableData() {
		return loadTableData();
	}

	@Override
	public void restoreState(Settings settings) {
		layoutManager.restoreState(settings);
		super.restoreState(settings);
	}

	/**
	 * @param carrelloEffettiPage
	 *            the carrelloEffettiPage to set
	 */
	public void setCarrelloEffettiPage(CarrelloEffettiPage carrelloEffettiPage) {
		this.carrelloEffettiPage = carrelloEffettiPage;
	}

	@Override
	public void setFormObject(Object object) {
		if (object instanceof ParametriRicercaEffetti) {
			this.parametriRicercaEffetti = (ParametriRicercaEffetti) object;
		} else {
			this.parametriRicercaEffetti = new ParametriRicercaEffetti();
		}
	}

	/**
	 * @param mode
	 *            the mode to set
	 */
	public void setMode(Mode mode) {
		this.mode = mode;
		effettoSelectRowListener.setEnabledRow(mode.getColonneAbilitatePerSelezione());
		layoutManager.setMode(mode);
		getTableModel().setMode(mode);
		getCarrelloEffettiPage().setMode(mode);
		parametriRicercaEffettiPage.setMode(mode);
		lockAggregateColum();
		setRows(new ArrayList<SituazioneEffetto>());
		getAggiungiSituazioneEffettoSelezionateCommand().setVisible(mode != Mode.GENERALE);
		getFooterControl().setVisible(mode != Mode.GENERALE);
		getTable().getTableLayoutView().updateLayoutsToolBar();
	}

	/**
	 * @param parametriRicercaEffettiPage
	 *            the parametriRicercaEffettiPage to set
	 */
	public void setParametriRicercaEffettiPage(ParametriRicercaEffettiPage parametriRicercaEffettiPage) {
		this.parametriRicercaEffettiPage = parametriRicercaEffettiPage;
	}

	/**
	 * @param tesoreriaBD
	 *            the tesoreriaBD to set
	 */
	public void setTesoreriaBD(ITesoreriaBD tesoreriaBD) {
		this.tesoreriaBD = tesoreriaBD;
	}

}
