package it.eurotn.panjea.ordini.rich.editors.evasione.carrello;

import it.eurotn.panjea.ordini.domain.OrdiniSettings;
import it.eurotn.panjea.ordini.domain.documento.evasione.DistintaCarico;
import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCarico;
import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCarico.StatoRiga;
import it.eurotn.panjea.ordini.rich.bd.IAnagraficaOrdiniBD;
import it.eurotn.panjea.ordini.rich.bd.IOrdiniDocumentoBD;
import it.eurotn.panjea.ordini.rich.editors.evasione.RisultatiRicercaEvasioneController;
import it.eurotn.panjea.ordini.rich.editors.evasione.carrello.sostituzione.RigaSostituzioneTablePage;
import it.eurotn.panjea.ordini.rich.editors.evasione.carrello.sostituzione.RigaSostituzioneTablePage.PageMode;
import it.eurotn.panjea.ordini.util.AreaOrdineFullDTO;
import it.eurotn.rich.control.table.JideTableWidget.TableType;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.form.builder.support.OverlayHelper;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandExecutor;
import org.springframework.richclient.command.support.ActionCommandInterceptorAdapter;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.TableModelWrapperUtils;
import com.jidesoft.pane.CollapsiblePane;
import com.jidesoft.plaf.basic.BasicCollapsiblePaneTitlePane;
import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

/**
 *
 *
 * @author fattazzo
 * @version 1.0, 01/dic/2010
 *
 */
public class CarrelloEvasioneOrdiniTablePage extends AbstractTablePageEditor<RigaDistintaCarico> implements
		InitializingBean {

	private class CreaDistintaCaricoCommandInterceptor extends ActionCommandInterceptorAdapter {

		@Override
		public void postExecution(ActionCommand command) {
			getSvuotaCarrelloCommand().execute();
			controller.reloadRisultatiRicercaEvasioneTablePage();
			LifecycleApplicationEvent event = new OpenEditorEvent(new DistintaCarico());
			Application.instance().getApplicationContext().publishEvent(event);
		}

		@Override
		public boolean preExecution(ActionCommand command) {

			if (getTableModel().getObjects().isEmpty()) {
				return false;
			}

			if (getTable().getTable().getCellEditor() != null) {
				getTable().getTable().getCellEditor().stopCellEditing();
			}

			boolean executeCmd = true;
			command.addParameter(CreaDistintaCaricoCommand.PARAM_RIGHE, getTableModel().getObjects());
			command.addParameter(CreaDistintaCaricoCommand.PARAM_EVADI_DISTINTE, evadiMissioni);

			return executeCmd;
		}
	}

	private class CreaDistintaCaricoEdEvadiCommandInterceptor extends ActionCommandInterceptorAdapter {

		@Override
		public void postExecution(ActionCommand command) {
			controller.reloadRisultatiRicercaEvasioneTablePage();
		}

		@Override
		public boolean preExecution(ActionCommand command) {

			if (getTableModel().getObjects().isEmpty()) {
				return false;
			}

			if (getTable().getTable().getCellEditor() != null) {
				getTable().getTable().getCellEditor().stopCellEditing();
			}

			boolean executeCmd = true;
			command.addParameter(CreaDistintaCaricoEdEvadiCommand.PARAM_RIGHE, getTableModel().getObjects());
			command.addParameter(CreaDistintaCaricoEdEvadiCommand.PARAM_EVADI_DISTINTE, evadiMissioni);

			return executeCmd;
		}
	}

	private class ForzaRimuoviRigheSelezionateCommandInterceptor extends ActionCommandInterceptorAdapter {

		@Override
		public void postExecution(ActionCommand command) {
			for (RigaDistintaCarico rigaDistinta : getTable().getSelectedObjects()) {
				getTable().removeRowObject(rigaDistinta);
			}
			super.postExecution(command);
		}

		@Override
		public boolean preExecution(ActionCommand command) {
			if (!getTable().getSelectedObjects().isEmpty()) {
				command.addParameter(ForzaRimuoviSelezionateCommand.SELECTED_ROW, getTable().getSelectedObjects());
				return true;
			} else {
				return false;
			}
		}
	}

	private class OpenOrdineCommand implements ActionCommandExecutor {

		@Override
		public void execute() {
			if (getTable().getSelectedObject() != null) {
				RigaDistintaCarico rigaDistintaCarico = getTable().getSelectedObject();
				AreaOrdineFullDTO areaOrdineFullDTO = ordiniDocumentoBD.caricaAreaOrdineFullDTO(rigaDistintaCarico
						.getRigaArticolo().getAreaOrdine());
				OpenEditorEvent event = new OpenEditorEvent(areaOrdineFullDTO);
				Application.instance().getApplicationContext().publishEvent(event);
			}
		}

	}

	private class RicalcolaGiacenzeCommand extends ApplicationWindowAwareCommand {

		public static final String COMMAND_ID = "ricalcolaGiacenzeCommand";

		/**
		 * Costruttore.
		 */
		public RicalcolaGiacenzeCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			getTableModel().ricalcolaGiacenze();
			getTable().getTable().repaint();
		}

	}

	private class RigaSelezionataObserver implements Observer {

		@Override
		public void update(Observable o, Object arg) {

			RigaDistintaCarico rigaEvasione = (RigaDistintaCarico) arg;
			rigaSostituzioneTablePage.preSetFormObject(rigaEvasione);
			rigaSostituzioneTablePage.setFormObject(rigaEvasione);
			rigaSostituzioneTablePage.postSetFormObject(rigaEvasione);
			rigaSostituzioneTablePage.refreshData();

			if (rigaEvasione != null) {
				sostituzioneRigaCollapsiblePane.setEnabled(true);
				if (rigaEvasione.isSostituita()) {
					if (rigaSostituzioneTablePage.isVisualizzaSempre()) {
						sostituzioneRigaCollapsiblePane.collapse(false);
					}
				} else {
					sostituzioneRigaCollapsiblePane.collapse(true);
				}
			} else {
				// lo faccio nell'else e non prima dell'if perchè tengo aperto
				// il popup di sostiuzione anche
				// cambiando riga per non obbligare l'utente ad aprirlo ad ogni
				// combiamento
				sostituzioneRigaCollapsiblePane.collapse(true);
				sostituzioneRigaCollapsiblePane.setEnabled(false);
			}
		}
	}

	private class RigheSostituzionePropertyChange implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			// le righe sostituzione sono cambiate quindi ricalcolo la giacenza
			// attuale
			getTableModel().aggiornaGiacenzaAttuale();
			getTable().getTable().repaint();
		}
	}

	private class RimuoviRigheSelezionateCommand extends ApplicationWindowAwareCommand {

		private static final String COMMAND_ID = "rimuoviRigheSelezionateCommand";

		/**
		 * Costruttore.
		 */
		public RimuoviRigheSelezionateCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			if (!getTable().getSelectedObjects().isEmpty()) {
				for (RigaDistintaCarico rigaEvasione : getTable().getSelectedObjects()) {
					rigaEvasione.setStato(StatoRiga.SELEZIONABILE);
					controller.sincronizzaRiga(rigaEvasione);
				}
			}

		}
	}

	private final class SvuotaCarrelloCommand extends ApplicationWindowAwareCommand {

		private static final String COMMAND_ID = "svuotaCarrelloEvasioneCommand";

		/**
		 * Costruttore.
		 */
		private SvuotaCarrelloCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			// Devo creare una nuova lista perchè la controller.sincronizzaRiga(rigaDistintaCarico); rimuove l'elemento
			// dal carrello
			List<RigaDistintaCarico> righeCarrello = new ArrayList<RigaDistintaCarico>(
					CarrelloEvasioneOrdiniTablePage.this.getTableModel().getObjects());
			for (RigaDistintaCarico rigaDistintaCarico : righeCarrello) {
				rigaDistintaCarico.setStato(StatoRiga.SELEZIONABILE);
				controller.sincronizzaRiga(rigaDistintaCarico);
			}
			sostituzioneRigaCollapsiblePane.collapse(true);
			sostituzioneRigaCollapsiblePane.setEnabled(false);
		}
	}

	public static final String PAGE_ID = "carrelloEvasioneOrdiniTablePage";

	private SvuotaCarrelloCommand svuotaCarrelloCommand;
	private RimuoviRigheSelezionateCommand rimuoviRigheSelezionateCommand;
	private CreaDistintaCaricoCommand creaDistintaCaricoCommand;
	private CreaDistintaCaricoEdEvadiCommand creaDistintaCaricoEdEvadiCommand;
	private RicalcolaGiacenzeCommand ricalcolaGiacenzeCommand;

	private LegendaToggleCommand legendaToggleCommand;

	private IOrdiniDocumentoBD ordiniDocumentoBD;
	private IAnagraficaOrdiniBD anagraficaOrdiniBD;

	private RigaSostituzioneTablePage rigaSostituzioneTablePage;

	private JPanel panelSostituzione;

	private CollapsiblePane sostituzioneRigaCollapsiblePane;

	private boolean evadiMissioni;

	private ForzaRimuoviSelezionateCommand forzaRimuoviRigheSelezionateCommand;

	private RisultatiRicercaEvasioneController controller;

	/**
	 * Costruttore.
	 */
	public CarrelloEvasioneOrdiniTablePage() {
		super(PAGE_ID, new CarrelloEvasioneOrdiniTableModel());
		getTable().setTableType(TableType.GROUP);
		getTable().setPropertyCommandExecutor(new OpenOrdineCommand());
		panelSostituzione = getComponentFactory().createPanel(new BorderLayout());
		this.evadiMissioni = false;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		GiacenzaCalculator giacenzaCalculator = new GiacenzaCalculator(ordiniDocumentoBD);
		getTableModel().setGiacenzaCalculator(giacenzaCalculator);
		this.rigaSostituzioneTablePage = new RigaSostituzioneTablePage(giacenzaCalculator, getLegendaToggleCommand());
		this.rigaSostituzioneTablePage.addPropertyChangeListener(RigaSostituzioneTablePage.RIGHE_SOSTITUZIONE_CHANGE,
				new RigheSostituzionePropertyChange());
		getTable().addSelectionObserver(new RigaSelezionataObserver());
		createOverlaySostituzioneRiga();
	}

	@Override
	protected JComponent createControl() {

		JPanel pagePanel = getComponentFactory().createPanel(new BorderLayout());

		pagePanel.add(super.createControl(), BorderLayout.CENTER);

		panelSostituzione.setPreferredSize(new Dimension(20, 30));
		panelSostituzione.setMinimumSize(new Dimension(20, 30));

		pagePanel.add(panelSostituzione, BorderLayout.EAST);

		return pagePanel;
	}

	/**
	 * Crea i controlli per la gestione della sostituzione riga.
	 */
	private void createOverlaySostituzioneRiga() {
		sostituzioneRigaCollapsiblePane = new CollapsiblePane("Sostituzione");
		sostituzioneRigaCollapsiblePane.setStyle(CollapsiblePane.DROPDOWN_STYLE);
		sostituzioneRigaCollapsiblePane.setSlidingDirection(SwingConstants.WEST);
		sostituzioneRigaCollapsiblePane.setLayout(new BorderLayout());
		sostituzioneRigaCollapsiblePane.add(rigaSostituzioneTablePage.getControl(), BorderLayout.CENTER);
		sostituzioneRigaCollapsiblePane.setEmphasized(true);
		sostituzioneRigaCollapsiblePane.collapse(true);
		sostituzioneRigaCollapsiblePane.setBackground(new Color(204, 204, 214));
		sostituzioneRigaCollapsiblePane.getContentPane().setBackground(UIManager.getColor("JPanel.background"));
		sostituzioneRigaCollapsiblePane.getContentPane().setOpaque(true);
		sostituzioneRigaCollapsiblePane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		sostituzioneRigaCollapsiblePane.setEnabled(false);
		sostituzioneRigaCollapsiblePane.setFocusable(false);
		sostituzioneRigaCollapsiblePane.setName("sostituzioneRigaCollapsiblePane");

		// Serve solo per la parte di test con marathon per dare un nome al
		// pannello
		for (Component component : sostituzioneRigaCollapsiblePane.getComponents()) {
			if (component instanceof BasicCollapsiblePaneTitlePane) {
				component.setName("sostituzioneRigaCollapsibleTitlePane");
				break;
			}
		}

		final JPanel panelOverlay = new JPanel(new BorderLayout());
		panelOverlay.setPreferredSize(new Dimension(500, 200));
		panelOverlay.setMinimumSize(new Dimension(500, 200));
		panelOverlay.add(sostituzioneRigaCollapsiblePane, BorderLayout.EAST);
		panelOverlay.setOpaque(false);

		// aggiungo un listener su resize del pannello per aggiornare l'altezza
		// del componente di overlay
		panelSostituzione.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				Dimension newDimension = e.getComponent().getSize();
				panelOverlay.setPreferredSize(new Dimension(500, newDimension.height));
			}
		});

		OverlayHelper.attachOverlay(panelOverlay, panelSostituzione, SwingConstants.CENTER, -240, 0);
	}

	@Override
	public AbstractCommand[] getCommands() {

		List<AbstractCommand> commands = new ArrayList<AbstractCommand>();
		commands.add(getForzaRimuoviRigheSelezionateCommand());
		commands.add(getRicalcolaGiacenzeCommand());
		commands.add(getLegendaToggleCommand());
		commands.add(getRimuoviRigheSelezionateCommand());
		commands.add(getSvuotaCarrelloCommand());

		OrdiniSettings ordiniSettings = anagraficaOrdiniBD.caricaOrdiniSettings();
		if (ordiniSettings.isCreazioneMissioniAbilitata()) {
			commands.add(getCreaDistintaCaricoCommand());
		}

		commands.add(getCreaDistintaCaricoEdEvadiCommand());

		return commands.toArray(new AbstractCommand[commands.size()]);
	}

	/**
	 *
	 * @return command per creare la distinta di carico
	 */
	public CreaDistintaCaricoCommand getCreaDistintaCaricoCommand() {
		if (creaDistintaCaricoCommand == null) {
			creaDistintaCaricoCommand = new CreaDistintaCaricoCommand(ordiniDocumentoBD);
			creaDistintaCaricoCommand.addCommandInterceptor(new CreaDistintaCaricoCommandInterceptor());
		}

		return creaDistintaCaricoCommand;
	}

	/**
	 *
	 * @return command per creare la distinta di carico
	 */
	public CreaDistintaCaricoEdEvadiCommand getCreaDistintaCaricoEdEvadiCommand() {
		if (creaDistintaCaricoEdEvadiCommand == null) {
			creaDistintaCaricoEdEvadiCommand = new CreaDistintaCaricoEdEvadiCommand(ordiniDocumentoBD, this);
			creaDistintaCaricoEdEvadiCommand.addCommandInterceptor(new CreaDistintaCaricoEdEvadiCommandInterceptor());
		}
		return creaDistintaCaricoEdEvadiCommand;
	}

	/**
	 * @return the rimuoviRigheSelezionateCommand
	 */
	public ForzaRimuoviSelezionateCommand getForzaRimuoviRigheSelezionateCommand() {
		if (forzaRimuoviRigheSelezionateCommand == null) {
			forzaRimuoviRigheSelezionateCommand = new ForzaRimuoviSelezionateCommand(ordiniDocumentoBD);
			forzaRimuoviRigheSelezionateCommand
					.addCommandInterceptor(new ForzaRimuoviRigheSelezionateCommandInterceptor());
		}
		return forzaRimuoviRigheSelezionateCommand;
	}

	/**
	 * @return the legendaToggleCommand
	 */
	public LegendaToggleCommand getLegendaToggleCommand() {
		if (legendaToggleCommand == null) {
			legendaToggleCommand = new LegendaToggleCommand(this);
		}

		return legendaToggleCommand;
	}

	/**
	 *
	 * @return command per il ricalcolo delle giacenze.
	 */
	public RicalcolaGiacenzeCommand getRicalcolaGiacenzeCommand() {
		if (ricalcolaGiacenzeCommand == null) {
			ricalcolaGiacenzeCommand = new RicalcolaGiacenzeCommand();
		}

		return ricalcolaGiacenzeCommand;
	}

	/**
	 *
	 * @return righe nel carrello di evasione
	 */
	public List<RigaDistintaCarico> getRigheEvasione() {
		return getTable().getRows();
	}

	/**
	 * @return the rimuoviRigheSelezionateCommand
	 */
	public RimuoviRigheSelezionateCommand getRimuoviRigheSelezionateCommand() {
		if (rimuoviRigheSelezionateCommand == null) {
			rimuoviRigheSelezionateCommand = new RimuoviRigheSelezionateCommand();
		}
		return rimuoviRigheSelezionateCommand;
	}

	/**
	 *
	 * @return command per svuotare il carrello.
	 */
	public ApplicationWindowAwareCommand getSvuotaCarrelloCommand() {
		if (svuotaCarrelloCommand == null) {
			svuotaCarrelloCommand = new SvuotaCarrelloCommand();
		}

		return svuotaCarrelloCommand;
	}

	/**
	 *
	 * @return tableModel della pagina
	 */
	CarrelloEvasioneOrdiniTableModel getTableModel() {
		CarrelloEvasioneOrdiniTableModel tableModel = (CarrelloEvasioneOrdiniTableModel) TableModelWrapperUtils
				.getActualTableModel(getTable().getTable().getModel());
		return tableModel;
	}

	@Override
	public Collection<RigaDistintaCarico> loadTableData() {
		getTableModel().getGiacenzaCalculator().clear();
		return Collections.emptyList();
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public Collection<RigaDistintaCarico> refreshTableData() {
		return null;
	}

	@Override
	public void restoreState(Settings settings) {
		super.restoreState(settings);
		rigaSostituzioneTablePage.restoreState(settings);
	}

	@Override
	public void saveState(Settings settings) {
		super.saveState(settings);
		rigaSostituzioneTablePage.saveState(settings);
	}

	/**
	 * @param anagraficaOrdiniBD
	 *            the anagraficaOrdiniBD to set
	 */
	public void setAnagraficaOrdiniBD(IAnagraficaOrdiniBD anagraficaOrdiniBD) {
		this.anagraficaOrdiniBD = anagraficaOrdiniBD;
	}

	/**
	 * @param risultatiRicercaEvasioneController
	 *            the risultatiRicercaEvasioneController to set
	 */
	public void setController(RisultatiRicercaEvasioneController risultatiRicercaEvasioneController) {
		this.controller = risultatiRicercaEvasioneController;
	}

	/**
	 * @param evadiMissioni
	 *            The evadiMissioni to set.
	 */
	public void setEvadiMissioni(boolean evadiMissioni) {
		this.evadiMissioni = evadiMissioni;
	}

	@Override
	public void setFormObject(Object object) {
	}

	/**
	 * @param ordiniDocumentoBD
	 *            the ordiniDocumentoBD to set
	 */
	public void setOrdiniDocumentoBD(IOrdiniDocumentoBD ordiniDocumentoBD) {
		this.ordiniDocumentoBD = ordiniDocumentoBD;
	}

	/**
	 * Visualizza la legenda al posto della sostituzione articolo.
	 *
	 * @param show
	 *            <code>true</code> per visualizzare la legenda
	 */
	public void showLegend(boolean show) {

		if (show && sostituzioneRigaCollapsiblePane.isEnabled()) {
			rigaSostituzioneTablePage.setPageMode(PageMode.LEGENDA);
			sostituzioneRigaCollapsiblePane.collapse(false);
		} else {
			rigaSostituzioneTablePage.setPageMode(PageMode.SOSTITUZIONE);
		}
	}

}
