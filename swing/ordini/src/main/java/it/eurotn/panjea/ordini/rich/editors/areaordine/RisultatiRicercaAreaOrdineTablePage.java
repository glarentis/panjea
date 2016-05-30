package it.eurotn.panjea.ordini.rich.editors.areaordine;

import it.eurotn.panjea.anagrafica.rich.editors.documento.AbstractEliminaDocumentoCommand.ETipoCancellazione;
import it.eurotn.panjea.magazzino.rich.commands.documento.SpedizioneDocumentiCommand;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine.StatoAreaOrdine;
import it.eurotn.panjea.ordini.rich.bd.IOrdiniDocumentoBD;
import it.eurotn.panjea.ordini.util.AreaOrdineFullDTO;
import it.eurotn.panjea.ordini.util.AreaOrdineRicerca;
import it.eurotn.panjea.ordini.util.parametriricerca.ParametriRicercaAreaOrdine;
import it.eurotn.panjea.rich.PanjeaLifecycleApplicationEvent;
import it.eurotn.panjea.rich.editors.documento.StampaAreeDocumentoCommand;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.context.ApplicationEvent;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.command.support.ActionCommandInterceptorAdapter;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.list.ListMultipleSelectionGuard;
import org.springframework.richclient.list.ListSelectionValueModelAdapter;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

public class RisultatiRicercaAreaOrdineTablePage extends AbstractTablePageEditor<AreaOrdineRicerca> {

	private class CopiaAreaOrdineCommandInterceptor extends ActionCommandInterceptorAdapter {

		@Override
		public boolean preExecution(ActionCommand command) {

			AreaOrdineRicerca areaOrdineRicerca = getTable().getSelectedObject();

			Integer idArea = null;
			if (areaOrdineRicerca != null) {
				idArea = areaOrdineRicerca.getIdAreaOrdine();
			}

			command.addParameter(CopiaAreaOrdineCommand.PARAM_AREA_ORDINE_ID, idArea);
			return true;
		}
	}

	private final class EliminaAreaOrdineCommandInterceptor implements ActionCommandInterceptor {
		@Override
		public void postExecution(ActionCommand command) {
			if (!((EliminaAreaOrdineCommand) command).getTipoCancellazione().equals(ETipoCancellazione.NESSUNO)) {
				for (AreaOrdineRicerca areaOrdineRicerca : ((EliminaAreaOrdineCommand) command).getAreeOrdineRicerca()) {
					getTable().removeRowObject(areaOrdineRicerca);
				}
			}
		}

		@Override
		public boolean preExecution(ActionCommand command) {
			((EliminaAreaOrdineCommand) command).setAreeOrdineRicerca(getTable().getSelectedObjects());
			return true;
		}
	}

	private class OpenAreaOrdineEditorCommand extends ApplicationWindowAwareCommand {

		private static final String COMMAND_ID = "openAreaOrdineCommand";

		/**
		 * Costruttore.
		 */
		public OpenAreaOrdineEditorCommand() {
			super(COMMAND_ID);
			this.setSecurityControllerId("visualizzaAreaOrdineController");
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			RisultatiRicercaAreaOrdineTablePage.this.openAreaOrdineEditor();
		}
	}

	private class SpedizioneOrdiniCommandInterceptor extends ActionCommandInterceptorAdapter {

		@Override
		public boolean preExecution(ActionCommand command) {

			List<AreaOrdineRicerca> selectedObjects = getTable().getSelectedObjects();

			if (selectedObjects != null) {
				List<Integer> idDocumenti = new ArrayList<Integer>();
				for (AreaOrdineRicerca areaOrdineRicerca : selectedObjects) {
					if (areaOrdineRicerca.getStatoAreaOrdine() == StatoAreaOrdine.CONFERMATO) {
						idDocumenti.add(areaOrdineRicerca.getDocumento().getId());
					}
				}
				command.addParameter(SpedizioneDocumentiCommand.PARAM_ID_DOCUMENTI_DA_SPEDIRE, idDocumenti);
			}

			return selectedObjects != null && !selectedObjects.isEmpty();
		}
	}

	private ParametriRicercaAreaOrdine parametriRicercaAreaOrdine = null;

	public static final String PAGE_ID = "risultatiRicercaAreaOrdineTablePage";
	private IOrdiniDocumentoBD ordiniDocumentoBD = null;

	// Adapter per la selezione della tabella
	private ListSelectionValueModelAdapter listSelectionAdapter;

	private ListSelectionValueModelAdapter listSelectionAdapterElimina;
	private EliminaAreaOrdineCommand eliminaCommand;

	private OpenAreaOrdineEditorCommand openAreaOrdineEditorCommand = null;

	private ListMultipleSelectionGuard listMultipleSelectionGuardOpen;
	private ListMultipleSelectionGuard listMultipleSelectionGuardElimina;
	private EliminaAreaOrdineCommandInterceptor eliminaAreaOrdineCommandInterceptor;
	private CambiaStatoAreaOrdineCommand cambiaStatoCommand;
	private SpedizioneDocumentiCommand<AreaOrdine> stampaAreeOrdineCommand;

	private CopiaAreaOrdineCommand copiaAreaOrdineCommand;
	private SpedizioneDocumentiCommand<AreaOrdine> spedizioneDocumentiCommand;
	private SpedizioneOrdiniCommandInterceptor spedizioneOrdiniCommandInterceptor;

	/**
	 * Costruttore.
	 */
	public RisultatiRicercaAreaOrdineTablePage() {
		super(PAGE_ID, new AreaOrdineRicercaTableModel());
	}

	@Override
	public void dispose() {

		getEliminaCommand().removeCommandInterceptor(eliminaAreaOrdineCommandInterceptor);
		listSelectionAdapterElimina.removeValueChangeListener(listMultipleSelectionGuardElimina);
		getTable().getTable().getSelectionModel().removeListSelectionListener(listSelectionAdapterElimina);

		listSelectionAdapter.removeValueChangeListener(listMultipleSelectionGuardOpen);
		getTable().getTable().getSelectionModel().removeListSelectionListener(listSelectionAdapter);

		getSpedizioneDocumentiCommand().removeCommandInterceptor(spedizioneOrdiniCommandInterceptor);
		super.dispose();
	}

	/**
	 * @return command per cambiare lo stato dell'area ordine
	 */
	private AbstractCommand getCambiaStatoCommand() {
		if (cambiaStatoCommand == null) {
			cambiaStatoCommand = new CambiaStatoAreaOrdineCommand(getTable(), ordiniDocumentoBD);
			getTable().addSelectionObserver(cambiaStatoCommand);
		}
		return cambiaStatoCommand;
	}

	@Override
	public AbstractCommand[] getCommands() {
		AbstractCommand[] abstractCommands = new AbstractCommand[] { getCopiaAreaOrdineCommand(),
				getCambiaStatoCommand(), getSpedizioneDocumentiCommand(), getStampaAreeOrdineCommand(),
				getOpenAreaOrdineEditorCommand(), getEliminaCommand() };
		return abstractCommands;
	}

	/**
	 * @return the copiaAreaOrdineCommand
	 */
	public CopiaAreaOrdineCommand getCopiaAreaOrdineCommand() {
		if (copiaAreaOrdineCommand == null) {
			copiaAreaOrdineCommand = new CopiaAreaOrdineCommand();
			copiaAreaOrdineCommand.addCommandInterceptor(new CopiaAreaOrdineCommandInterceptor());
		}

		return copiaAreaOrdineCommand;
	}

	@Override
	public AbstractCommand getEditorDeleteCommand() {
		return getEliminaCommand();
	}

	/**
	 * @return comando per cancellare le aree magazzino selezionate
	 */
	private ActionCommand getEliminaCommand() {
		if (eliminaCommand == null) {
			eliminaCommand = new EliminaAreaOrdineCommand(PAGE_ID);
			listMultipleSelectionGuardElimina = new ListMultipleSelectionGuard(getListSelectionAdapterElimina(),
					eliminaCommand);
			eliminaCommand.setOrdiniDocumentoBD(ordiniDocumentoBD);

			eliminaAreaOrdineCommandInterceptor = new EliminaAreaOrdineCommandInterceptor();
			eliminaCommand.addCommandInterceptor(eliminaAreaOrdineCommandInterceptor);

		}

		return eliminaCommand;
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
	 * Inizializzazione lazy.
	 *
	 * @return adapter per la selezione della tabella
	 */
	private ListSelectionValueModelAdapter getListSelectionAdapterElimina() {
		if (listSelectionAdapterElimina == null) {
			listSelectionAdapterElimina = new ListSelectionValueModelAdapter(getTable().getTable().getSelectionModel());
		}
		return listSelectionAdapterElimina;
	}

	/**
	 * @return command
	 */
	private OpenAreaOrdineEditorCommand getOpenAreaOrdineEditorCommand() {
		if (openAreaOrdineEditorCommand == null) {
			openAreaOrdineEditorCommand = new OpenAreaOrdineEditorCommand();
			listMultipleSelectionGuardOpen = new ListMultipleSelectionGuard(getListSelectionAdapter(),
					openAreaOrdineEditorCommand);
			getTable().setPropertyCommandExecutor(openAreaOrdineEditorCommand);
		}
		return openAreaOrdineEditorCommand;
	}

	/**
	 * @return the spedizioneDocumentiCommand
	 */
	private SpedizioneDocumentiCommand<AreaOrdine> getSpedizioneDocumentiCommand() {
		if (spedizioneDocumentiCommand == null) {
			spedizioneDocumentiCommand = new SpedizioneDocumentiCommand<AreaOrdine>(AreaOrdine.class);
			spedizioneOrdiniCommandInterceptor = new SpedizioneOrdiniCommandInterceptor();
			spedizioneDocumentiCommand.addCommandInterceptor(spedizioneOrdiniCommandInterceptor);
		}

		return spedizioneDocumentiCommand;
	}

	/**
	 * @return command
	 */
	public SpedizioneDocumentiCommand<AreaOrdine> getStampaAreeOrdineCommand() {
		if (stampaAreeOrdineCommand == null) {
			stampaAreeOrdineCommand = new SpedizioneDocumentiCommand<AreaOrdine>(StampaAreeDocumentoCommand.COMMAND_ID,
					AreaOrdine.class);
			new ListMultipleSelectionGuard(getListSelectionAdapter(), stampaAreeOrdineCommand);
			stampaAreeOrdineCommand.addCommandInterceptor(new ActionCommandInterceptor() {

				@Override
				public void postExecution(ActionCommand command) {
				}

				@Override
				public boolean preExecution(ActionCommand command) {
					List<AreaOrdineRicerca> selectedObjects = getTable().getSelectedObjects();

					if (selectedObjects != null) {
						List<Integer> idDocumenti = new ArrayList<Integer>();
						for (AreaOrdineRicerca areaOrdineRicerca : selectedObjects) {
							if (areaOrdineRicerca.getStatoAreaOrdine() == StatoAreaOrdine.CONFERMATO) {
								idDocumenti.add(areaOrdineRicerca.getDocumento().getId());
							}
						}
						command.addParameter(SpedizioneDocumentiCommand.PARAM_ID_DOCUMENTI_DA_SPEDIRE, idDocumenti);
						command.addParameter(SpedizioneDocumentiCommand.PARAM_FORZA_STAMPA, true);
					}

					return selectedObjects != null && !selectedObjects.isEmpty();
				}
			});
		}
		return stampaAreeOrdineCommand;
	}

	@Override
	public List<AreaOrdineRicerca> loadTableData() {
		List<AreaOrdineRicerca> aree = null;
		if (parametriRicercaAreaOrdine.isEffettuaRicerca()) {
			aree = refreshTableData();
		}

		return aree;
	}

	@Override
	public void onEditorEvent(ApplicationEvent event) {
		// se è una cancellazione di un area magazzino la trasformo in un areamagazzinoricerca per cancellarla
		PanjeaLifecycleApplicationEvent panjeaEvent = (PanjeaLifecycleApplicationEvent) event;
		if (panjeaEvent.getEventType().equals(LifecycleApplicationEvent.DELETED)
				&& panjeaEvent.getSource() instanceof AreaOrdine) {
			AreaOrdineRicerca areaOrdineRicerca = new AreaOrdineRicerca();
			areaOrdineRicerca.setIdAreaOrdine(((AreaOrdine) panjeaEvent.getSource()).getId());
			getTable().removeRowObject(areaOrdineRicerca);
		} else {
			super.onEditorEvent(event);
		}
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	/**
	 * Lancia l'evento per aprire l'area ordine selezionata.
	 */
	private void openAreaOrdineEditor() {
		logger.debug("--> Enter openAreaOrdineEditor");
		AreaOrdineRicerca areaOrdineRicerca = getTable().getSelectedObject();
		if (areaOrdineRicerca == null) {
			return;
		}
		AreaOrdine areaOrdine = new AreaOrdine();
		areaOrdine.setId(areaOrdineRicerca.getIdAreaOrdine());

		AreaOrdineFullDTO areaOrdineFullDTO = ordiniDocumentoBD.caricaAreaOrdineFullDTO(areaOrdine);
		LifecycleApplicationEvent event = new OpenEditorEvent(areaOrdineFullDTO);
		Application.instance().getApplicationContext().publishEvent(event);
		logger.debug("--> Exit openAreaOrdineEditor");
	}

	@Override
	public List<AreaOrdineRicerca> refreshTableData() {
		List<AreaOrdineRicerca> areeOrdine = Collections.emptyList();
		if (parametriRicercaAreaOrdine.isEffettuaRicerca()) {
			areeOrdine = ordiniDocumentoBD.ricercaAreeOrdine(parametriRicercaAreaOrdine);
		}
		return areeOrdine;
	}

	@Override
	public void restoreState(Settings settings) {
		super.restoreState(settings);
	}

	@Override
	public void setFormObject(Object object) {
		if (object instanceof ParametriRicercaAreaOrdine) {
			this.parametriRicercaAreaOrdine = (ParametriRicercaAreaOrdine) object;
		} else {
			this.parametriRicercaAreaOrdine = new ParametriRicercaAreaOrdine();
		}
	}

	/**
	 * @param ordiniDocumentoBD
	 *            the ordiniDocumentoBD to set
	 */
	public void setOrdiniDocumentoBD(IOrdiniDocumentoBD ordiniDocumentoBD) {
		this.ordiniDocumentoBD = ordiniDocumentoBD;
	}
}
