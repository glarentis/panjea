/**
 * 
 */
package it.eurotn.panjea.spedizioni.rich.editors.rendicontazione;

import it.eurotn.panjea.anagrafica.domain.Vettore;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;
import it.eurotn.panjea.rich.PanjeaLifecycleApplicationEvent;
import it.eurotn.panjea.spedizioni.rich.generazione.SoftwareEtichette;
import it.eurotn.panjea.spedizioni.util.ParametriRicercaRendicontazione;
import it.eurotn.rich.control.table.DefaultBeanTableModel;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationEvent;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.list.ListMultipleSelectionGuard;
import org.springframework.richclient.list.ListSelectionValueModelAdapter;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

/**
 * TablePage per la visualizzazione dei risultati della ricerca di {@link AreaMagazzino}.
 * 
 * @author adriano
 * @version 1.0, 30/set/2008
 */
public class RisultatiRicercaAreaMagazzinoRendicontazioneTablePage extends
		AbstractTablePageEditor<AreaMagazzinoRicerca> {

	private class GeneraRendicontazioneCommand extends ApplicationWindowAwareCommand {

		public static final String COMMAND_ID = "generaRendicontazioneCommand";

		/**
		 * Costruttore.
		 * 
		 */
		public GeneraRendicontazioneCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {

			List<AreaMagazzinoRicerca> aree = getTable().getSelectedObjects();

			Vettore vettore = (Vettore) anagraficaBD.caricaEntita(parametriRicercaRendicontazione.getVettore(), false);

			SoftwareEtichette softwareEtichette = new SoftwareEtichette(vettore);
			softwareEtichette.creaRendicontazione(aree);

			loadData();
		}

	}

	private class OpenAreaMagazzinoEditor extends ApplicationWindowAwareCommand {

		private static final String COMMAND_ID = "openAreaMagazzinoCommand";

		/**
		 * Costruttore.
		 */
		public OpenAreaMagazzinoEditor() {
			super(COMMAND_ID);
			CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
					CommandConfigurer.class);
			this.setSecurityControllerId(RisultatiRicercaAreaMagazzinoRendicontazioneTablePage.this.getId()
					+ COMMAND_ID);
			c.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			RisultatiRicercaAreaMagazzinoRendicontazioneTablePage.this.openAreaMagazzinoEditor();
		}
	}

	private static Logger logger = Logger.getLogger(RisultatiRicercaAreaMagazzinoRendicontazioneTablePage.class);

	protected ParametriRicercaRendicontazione parametriRicercaRendicontazione = null;
	public static final String PAGE_ID = "risultatiRicercaAreaMagazzinoSpedizioniTablePage";

	private IMagazzinoDocumentoBD magazzinoDocumentoBD = null;
	private IAnagraficaBD anagraficaBD = null;

	private OpenAreaMagazzinoEditor openAreaMagazzinoEditor = null;

	// Adapter per la selezione della tabella
	private ListSelectionValueModelAdapter listSelectionAdapter;

	private ListMultipleSelectionGuard listMultipleSelectionGuard;

	private GeneraRendicontazioneCommand generaRendicontazioneCommand;

	/**
	 * Costruttore.
	 */
	public RisultatiRicercaAreaMagazzinoRendicontazioneTablePage() {
		super(PAGE_ID, new AreaMagazzinoRicercaRendicontazioneTableModel());
	}

	/**
	 * Costruttore.
	 * 
	 * @param pageId
	 *            id della pagina
	 * @param tableModel
	 *            table model
	 */
	public RisultatiRicercaAreaMagazzinoRendicontazioneTablePage(final String pageId,
			final DefaultBeanTableModel<AreaMagazzinoRicerca> tableModel) {
		super(pageId, tableModel);
	}

	@Override
	public void dispose() {
		listSelectionAdapter.removeValueChangeListener(listMultipleSelectionGuard);
		getTable().getTable().getSelectionModel().removeListSelectionListener(listSelectionAdapter);
		super.dispose();
	}

	@Override
	public AbstractCommand[] getCommands() {
		AbstractCommand[] abstractCommands = new AbstractCommand[] { getGeneraRendicontazioneCommand(),
				getOpenAreaMagazzinoEditor() };
		return abstractCommands;
	}

	/**
	 * @return the stampaEtichettaCommand
	 */
	public GeneraRendicontazioneCommand getGeneraRendicontazioneCommand() {
		if (generaRendicontazioneCommand == null) {
			generaRendicontazioneCommand = new GeneraRendicontazioneCommand();
		}

		return generaRendicontazioneCommand;
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

	@Override
	public Collection<AreaMagazzinoRicerca> loadTableData() {
		List<AreaMagazzinoRicerca> areeMagazzino = Collections.emptyList();

		if (parametriRicercaRendicontazione.getAreeMagazzino() != null
				&& !parametriRicercaRendicontazione.getAreeMagazzino().isEmpty()) {
			areeMagazzino = parametriRicercaRendicontazione.getAreeMagazzino();
		} else {
			if (parametriRicercaRendicontazione.isEffettuaRicerca()) {
				areeMagazzino = magazzinoDocumentoBD.ricercaAreeMagazzino(parametriRicercaRendicontazione);
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
		// areaMagazzino.setDepositoOrigine(areaMagazzinoRicerca.getDepositoOrigine());

		AreaMagazzinoFullDTO areaMagazzinoFullDTO = magazzinoDocumentoBD.caricaAreaMagazzinoFullDTO(areaMagazzino);
		// areaMagazzinoFullDTO.getAreaMagazzino().setDepositoOrigine(areaMagazzino.getDepositoOrigine());
		LifecycleApplicationEvent event = new OpenEditorEvent(areaMagazzinoFullDTO);
		Application.instance().getApplicationContext().publishEvent(event);
		logger.debug("--> Exit openAreaMagazzinoEditor");
	}

	@Override
	public Collection<AreaMagazzinoRicerca> refreshTableData() {
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
		if (object instanceof ParametriRicercaRendicontazione) {
			this.parametriRicercaRendicontazione = (ParametriRicercaRendicontazione) object;
		} else {
			this.parametriRicercaRendicontazione = new ParametriRicercaRendicontazione();
		}
		getTable().setTableHeader(parametriRicercaRendicontazione);
	}

	/**
	 * @param magazzinoDocumentoBD
	 *            The magazzinoDocumentoBD to set.
	 */
	public void setMagazzinoDocumentoBD(IMagazzinoDocumentoBD magazzinoDocumentoBD) {
		this.magazzinoDocumentoBD = magazzinoDocumentoBD;
	}
}
