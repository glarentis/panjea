/**
 * 
 */
package it.eurotn.panjea.spedizioni.rich.editors.etichette;

import it.eurotn.panjea.anagrafica.domain.Vettore;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;
import it.eurotn.panjea.rich.PanjeaLifecycleApplicationEvent;
import it.eurotn.panjea.rich.factory.PanjeaComponentFactory;
import it.eurotn.panjea.spedizioni.rich.generazione.SoftwareEtichette;
import it.eurotn.panjea.spedizioni.util.ParametriRicercaSpedizioni;
import it.eurotn.rich.control.table.DefaultBeanTableModel;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.awt.BorderLayout;
import java.util.Collections;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.jdesktop.swingx.HorizontalLayout;
import org.springframework.context.ApplicationEvent;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.list.ListMultipleSelectionGuard;
import org.springframework.richclient.list.ListSelectionValueModelAdapter;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;
import com.toedter.calendar.IDateEditor;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;

/**
 * TablePage per la visualizzazione dei risultati della ricerca di {@link AreaMagazzino}.
 * 
 * @author adriano
 * @version 1.0, 30/set/2008
 */
public class RisultatiRicercaAreaMagazzinoSpedizioniTablePage extends AbstractTablePageEditor<AreaMagazzinoRicerca> {

	private class OpenAreaMagazzinoEditor extends ApplicationWindowAwareCommand {

		private static final String COMMAND_ID = "openAreaMagazzinoCommand";

		/**
		 * Costruttore.
		 */
		public OpenAreaMagazzinoEditor() {
			super(COMMAND_ID);
			CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
					CommandConfigurer.class);
			this.setSecurityControllerId(RisultatiRicercaAreaMagazzinoSpedizioniTablePage.this.getId() + COMMAND_ID);
			c.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			RisultatiRicercaAreaMagazzinoSpedizioniTablePage.this.openAreaMagazzinoEditor();
		}
	}

	private class StampaEtichettaCommand extends ApplicationWindowAwareCommand {

		public static final String COMMAND_ID = "stampaEtichettaCommand";

		/**
		 * Costruttore.
		 * 
		 */
		public StampaEtichettaCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {

			AreaMagazzinoRicerca areaMagazzinoRicerca = getTable().getSelectedObject();

			if (dataTrasportoDateChooser.getDate() == null) {
				MessageDialog dialog = new MessageDialog("ATTENZIONE", "Inserire una data di inizio trasporto");
				dialog.showDialog();
				return;
			}

			if (areaMagazzinoRicerca != null) {
				AreaMagazzino areaMagazzino = new AreaMagazzino();
				areaMagazzino.setId(areaMagazzinoRicerca.getIdAreaMagazzino());
				AreaMagazzinoFullDTO areaMagazzinoFull = magazzinoDocumentoBD.caricaAreaMagazzinoFullDTO(areaMagazzino);

				Vettore vettore = (Vettore) anagraficaBD.caricaEntita(
						areaMagazzinoFull.getAreaMagazzino().getVettore(), false);

				SoftwareEtichette softwareEtichette = new SoftwareEtichette(vettore);
				softwareEtichette.creaEtichette(areaMagazzinoFull, dataTrasportoDateChooser.getDate());
			}
		}
	}

	private static Logger logger = Logger.getLogger(RisultatiRicercaAreaMagazzinoSpedizioniTablePage.class);

	protected ParametriRicercaSpedizioni parametriRicercaSpedizioni = null;
	public static final String PAGE_ID = "risultatiRicercaAreaMagazzinoSpedizioniTablePage";

	private IMagazzinoDocumentoBD magazzinoDocumentoBD = null;
	private IAnagraficaBD anagraficaBD;

	private OpenAreaMagazzinoEditor openAreaMagazzinoEditor = null;

	// Adapter per la selezione della tabella
	private ListSelectionValueModelAdapter listSelectionAdapter;

	private ListMultipleSelectionGuard listMultipleSelectionGuard;

	private StampaEtichettaCommand stampaEtichettaCommand;

	private JDateChooser dataTrasportoDateChooser;

	/**
	 * Costruttore.
	 */
	public RisultatiRicercaAreaMagazzinoSpedizioniTablePage() {
		super(PAGE_ID, new AreaMagazzinoRicercaSpedizioniTableModel());
	}

	/**
	 * Costruttore.
	 * 
	 * @param pageId
	 *            id della pagina
	 * @param tableModel
	 *            table model
	 */
	public RisultatiRicercaAreaMagazzinoSpedizioniTablePage(final String pageId,
			final DefaultBeanTableModel<AreaMagazzinoRicerca> tableModel) {
		super(pageId, tableModel);
	}

	@Override
	public JComponent createToolbar() {
		JComponent toolBar = super.createToolbar();

		JPanel dataTrasportoPanel = getComponentFactory().createPanel(new BorderLayout(10, 0));
		dataTrasportoPanel.add(new JLabel(RcpSupport.getMessage("dataInizioTrasporto")), BorderLayout.WEST);

		IDateEditor textFieldDateEditor = new JTextFieldDateEditor("dd/MM/yy", "__/__/__", '_');
		dataTrasportoDateChooser = ((PanjeaComponentFactory) getComponentFactory())
				.createDateChooser(textFieldDateEditor);

		dataTrasportoPanel.add(dataTrasportoDateChooser, BorderLayout.CENTER);

		JPanel toolBarPanel = getComponentFactory().createPanel(new HorizontalLayout());
		toolBarPanel.add(dataTrasportoPanel);
		toolBarPanel.add(toolBar);
		return toolBarPanel;
	}

	@Override
	public void dispose() {
		listSelectionAdapter.removeValueChangeListener(listMultipleSelectionGuard);
		getTable().getTable().getSelectionModel().removeListSelectionListener(listSelectionAdapter);
		super.dispose();
	}

	@Override
	public AbstractCommand[] getCommands() {
		AbstractCommand[] abstractCommands = new AbstractCommand[] { getStampaEtichettaCommand(),
				getOpenAreaMagazzinoEditor() };
		return abstractCommands;
	}

	/**
	 * Inizializzazione lazy.
	 * 
	 * @return adapter per la selezione della tabella
	 */
	private ListSelectionValueModelAdapter getListSelectionAdapter() {
		if (listSelectionAdapter == null) {
			listSelectionAdapter = new ListSelectionValueModelAdapter(getTable().getTable().getSelectionModel());
		}
		return listSelectionAdapter;
	}

	/**
	 * 
	 * @return command
	 */
	public OpenAreaMagazzinoEditor getOpenAreaMagazzinoEditor() {
		if (openAreaMagazzinoEditor == null) {
			openAreaMagazzinoEditor = new OpenAreaMagazzinoEditor();
			listMultipleSelectionGuard = new ListMultipleSelectionGuard(getListSelectionAdapter(),
					openAreaMagazzinoEditor);
			getTable().setPropertyCommandExecutor(openAreaMagazzinoEditor);
		}
		return openAreaMagazzinoEditor;
	}

	/**
	 * @return the stampaEtichettaCommand
	 */
	public StampaEtichettaCommand getStampaEtichettaCommand() {
		if (stampaEtichettaCommand == null) {
			stampaEtichettaCommand = new StampaEtichettaCommand();
		}

		return stampaEtichettaCommand;
	}

	@Override
	public List<AreaMagazzinoRicerca> loadTableData() {
		List<AreaMagazzinoRicerca> areeMagazzino = Collections.emptyList();

		if (parametriRicercaSpedizioni.getAreeMagazzino() != null
				&& !parametriRicercaSpedizioni.getAreeMagazzino().isEmpty()) {
			areeMagazzino = parametriRicercaSpedizioni.getAreeMagazzino();
		} else {
			if (parametriRicercaSpedizioni.isEffettuaRicerca()) {
				areeMagazzino = magazzinoDocumentoBD.ricercaAreeMagazzino(parametriRicercaSpedizioni);
			}
		}
		return areeMagazzino;
	}

	@Override
	public void onEditorEvent(ApplicationEvent event) {
		// se è una cancellazione di un area magazzino la trasformo in un
		// areamagazzinoricerca per cancellarla
		PanjeaLifecycleApplicationEvent panjeaEvent = (PanjeaLifecycleApplicationEvent) event;
		if (panjeaEvent.getEventType().equals(LifecycleApplicationEvent.DELETED)
				&& panjeaEvent.getSource() instanceof AreaMagazzino) {
			AreaMagazzinoRicerca areaMagazzinoRicerca = new AreaMagazzinoRicerca();
			areaMagazzinoRicerca.setIdAreaMagazzino(((AreaMagazzino) panjeaEvent.getSource()).getId());
			getTable().removeRowObject(areaMagazzinoRicerca);
		} else {
			super.onEditorEvent(event);
		}
	}

	@Override
	public void onPostPageOpen() {
		// nothing to do
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	/**
	 * Lancia l'evento per aprire l'area di magazzino selezionata.
	 */
	private void openAreaMagazzinoEditor() {
		logger.debug("--> Enter openAreaMagazzinoEditor");
		AreaMagazzinoRicerca areaMagazzinoRicerca = getTable().getSelectedObject();
		if (areaMagazzinoRicerca == null) {
			return;
		}
		AreaMagazzino areaMagazzino = new AreaMagazzino();
		areaMagazzino.setId(areaMagazzinoRicerca.getIdAreaMagazzino());

		AreaMagazzinoFullDTO areaMagazzinoFullDTO = magazzinoDocumentoBD.caricaAreaMagazzinoFullDTO(areaMagazzino);
		LifecycleApplicationEvent event = new OpenEditorEvent(areaMagazzinoFullDTO);
		Application.instance().getApplicationContext().publishEvent(event);
		logger.debug("--> Exit openAreaMagazzinoEditor");
	}

	@Override
	public List<AreaMagazzinoRicerca> refreshTableData() {
		return loadTableData();
	}

	/**
	 * @param anagraficaBD
	 *            the anagraficaBD to set
	 */
	public void setAnagraficaBD(IAnagraficaBD anagraficaBD) {
		this.anagraficaBD = anagraficaBD;
	}

	@Override
	public void setFormObject(Object object) {
		if (object instanceof ParametriRicercaSpedizioni) {
			this.parametriRicercaSpedizioni = (ParametriRicercaSpedizioni) object;
		} else {
			this.parametriRicercaSpedizioni = new ParametriRicercaSpedizioni();
		}
		getTable().setTableHeader(parametriRicercaSpedizioni);
	}

	/**
	 * @param magazzinoDocumentoBD
	 *            The magazzinoDocumentoBD to set.
	 */
	public void setMagazzinoDocumentoBD(IMagazzinoDocumentoBD magazzinoDocumentoBD) {
		this.magazzinoDocumentoBD = magazzinoDocumentoBD;
	}
}
