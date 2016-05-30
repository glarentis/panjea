package it.eurotn.panjea.preventivi.rich.editors.areapreventivo;

import it.eurotn.panjea.anagrafica.documenti.domain.ITipoAreaDocumento;
import it.eurotn.panjea.magazzino.rich.commands.documento.SpedizioneDocumentiCommand;
import it.eurotn.panjea.parametriricerca.domain.AbstractParametriRicerca;
import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo;
import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo.StatoAreaPreventivo;
import it.eurotn.panjea.preventivi.domain.documento.interfaces.IAreaDocumentoTestata;
import it.eurotn.panjea.preventivi.domain.documento.interfaces.ITipoAreaStampabile;
import it.eurotn.panjea.preventivi.rich.bd.ICopiaDocumentoBD;
import it.eurotn.panjea.preventivi.rich.bd.interfaces.IAreaDocumentoBD;
import it.eurotn.panjea.preventivi.util.AbstractAreaDocumentoRicerca;
import it.eurotn.panjea.preventivi.util.AreaPreventivoRicerca;
import it.eurotn.panjea.preventivi.util.interfaces.IAreaDTO;
import it.eurotn.panjea.rich.PanjeaLifecycleApplicationEvent;
import it.eurotn.panjea.rich.editors.documento.StampaAreeDocumentoCommand;
import it.eurotn.rich.control.table.DefaultBeanTableModel;
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
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

public abstract class AbstractRisultatiRicercaAreaDocumentoTablePage<E extends IAreaDocumentoTestata, D extends IAreaDTO<E>, T extends ITipoAreaDocumento, R extends AbstractAreaDocumentoRicerca<T, E>, P extends AbstractParametriRicerca>
		extends AbstractTablePageEditor<AreaPreventivoRicerca> {

	private class CopiaAreaDocumentoCommandInterceptor extends ActionCommandInterceptorAdapter {

		@Override
		public boolean preExecution(ActionCommand command) {

			AreaPreventivoRicerca areaPreventivoRicerca = getTable().getSelectedObject();

			Integer idArea = null;
			if (areaPreventivoRicerca != null) {
				idArea = areaPreventivoRicerca.getIdAreaDocumento();
			}

			command.addParameter(CopiaAreaDocumentoCommand.PARAM_AREA_ID, idArea);
			return true;
		}
	}

	private final class EliminaAreaDocumentoCommandInterceptor implements ActionCommandInterceptor {
		@Override
		public void postExecution(ActionCommand command) {
			@SuppressWarnings("unchecked")
			EliminaAreaDocumentoCommand<E> eliminaAreaCommand = (EliminaAreaDocumentoCommand<E>) command;

			List<AreaPreventivoRicerca> aree = eliminaAreaCommand.getAreeRicerca();
			for (AreaPreventivoRicerca areaRicerca : aree) {
				getTable().removeRowObject(areaRicerca);
			}
		}

		@Override
		public boolean preExecution(ActionCommand command) {
			@SuppressWarnings("unchecked")
			EliminaAreaDocumentoCommand<E> eliminaAreaCommand = (EliminaAreaDocumentoCommand<E>) command;
			eliminaAreaCommand.setAreeRicerca(getTable().getSelectedObjects());
			return true;
		}
	}

	private class OpenAreaDocumentoEditorCommand extends ApplicationWindowAwareCommand {

		/**
		 * Costruttore.
		 */
		public OpenAreaDocumentoEditorCommand() {
			super(idOpenDocumentoCommand);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			openAreaPreventivoEditor();
		}
	}

	private P parametriRicerca;

	/**
	 * id da utilizzare per il command di apertura area selezionata nella tabella dei risultati.
	 */
	private final String idOpenDocumentoCommand;

	private IAreaDocumentoBD<E, D> areaBD;
	// Adapter per la selezione della tabella
	private ListSelectionValueModelAdapter listSelectionAdapter;

	private ListSelectionValueModelAdapter listSelectionAdapterElimina;

	private EliminaAreaDocumentoCommand<E> eliminaCommand;
	private OpenAreaDocumentoEditorCommand openAreaPreventivoEditorCommand;

	private ListMultipleSelectionGuard listMultipleSelectionGuardOpen;

	private ListMultipleSelectionGuard listMultipleSelectionGuardElimina;
	private EliminaAreaDocumentoCommandInterceptor eliminaAreaPreventivoCommandInterceptor;

	private CopiaAreaDocumentoCommand copiaAreaDocumentoCommand;

	private CopiaAreaDocumentoCommandInterceptor copiaAreaDocumentoCommandInterceptor;

	private SpedizioneDocumentiCommand<AreaPreventivo> stampaAreePreventivoCommand;

	/**
	 *
	 * @param pageID
	 *            pageID
	 * @param tableModel
	 *            tableModel
	 * @param idOpenDocumentoCommand
	 *            id da utilizzare per il command di apertura del documento
	 */
	public AbstractRisultatiRicercaAreaDocumentoTablePage(final String pageID,
			final DefaultBeanTableModel<AreaPreventivoRicerca> tableModel, final String idOpenDocumentoCommand) {
		super(pageID, tableModel);
		this.idOpenDocumentoCommand = idOpenDocumentoCommand;
	}

	/**
	 *
	 * @return nuova area documento
	 */
	protected abstract E creaAreaDocumento();

	/**
	 *
	 * @return nuova area documento ricerca
	 */
	protected abstract AreaPreventivoRicerca creaAreaDocumentoRicerca();

	/**
	 *
	 * @return lista di command da visualizzare sotto la tabella dei risultati
	 */
	protected List<AbstractCommand> creaCommandsList() {
		List<AbstractCommand> commands = new ArrayList<AbstractCommand>();
		T tipoArea = creaTipoAreaDocumento();
		if (tipoArea instanceof ITipoAreaStampabile) {
			commands.add(getStampaAreePreventivoCommand());
		}
		commands.add(getOpenAreaOrdineEditorCommand());
		commands.add(getEliminaCommand());
		return commands;
	}

	/**
	 *
	 * @return nuovo oggetto parametri ricerca area documento
	 */
	protected abstract P creaParametriRicercaAreaDocumento();

	/**
	 *
	 * @return nuovo tipo area documento
	 */
	protected abstract T creaTipoAreaDocumento();

	@Override
	public void dispose() {

		if (eliminaCommand != null) {
			eliminaCommand.removeCommandInterceptor(eliminaAreaPreventivoCommandInterceptor);
		}

		if (copiaAreaDocumentoCommand != null) {
			copiaAreaDocumentoCommand.removeCommandInterceptor(copiaAreaDocumentoCommandInterceptor);
		}

		listSelectionAdapter.removeValueChangeListener(listMultipleSelectionGuardElimina);
		getTable().getTable().getSelectionModel().removeListSelectionListener(listSelectionAdapterElimina);

		listSelectionAdapter.removeValueChangeListener(listMultipleSelectionGuardOpen);
		getTable().getTable().getSelectionModel().removeListSelectionListener(listSelectionAdapter);
		super.dispose();
	}

	/**
	 *
	 * @return areaBD
	 */
	protected IAreaDocumentoBD<E, D> getAreaBD() {
		return this.areaBD;
	}

	/**
	 *
	 * @return command per cambiare lo stato dell'area ordine
	 */
	// private AbstractCommand getCambiaStatoCommand() {
	// if (cambiaStatoCommand == null) {
	// cambiaStatoCommand = new CambiaStatoAreaOrdineCommand(getTable(), ordiniDocumentoBD);
	// getTable().addSelectionObserver(cambiaStatoCommand);
	// }
	// return cambiaStatoCommand;
	// }

	//

	@Override
	public AbstractCommand[] getCommands() {
		List<AbstractCommand> list = creaCommandsList();
		AbstractCommand[] abstractCommands = new AbstractCommand[list.size()];
		list.toArray(abstractCommands);
		return abstractCommands;
	}

	/**
	 *
	 * @return copiaAreaDocumentoCommandInterceptor
	 */
	protected CopiaAreaDocumentoCommandInterceptor getCopiaAreaDocumentoCommandInterceptor() {
		if (copiaAreaDocumentoCommandInterceptor == null) {
			copiaAreaDocumentoCommandInterceptor = new CopiaAreaDocumentoCommandInterceptor();
		}
		return copiaAreaDocumentoCommandInterceptor;

	}

	/**
	 * @return the copiaAreaOrdineCommand
	 */
	public CopiaAreaDocumentoCommand getCopiaAreaOrdineCommand() {
		if (copiaAreaDocumentoCommand == null) {
			copiaAreaDocumentoCommand = new CopiaAreaDocumentoCommand((ICopiaDocumentoBD) getAreaBD());
			copiaAreaDocumentoCommand.addCommandInterceptor(getCopiaAreaDocumentoCommandInterceptor());
		}

		return copiaAreaDocumentoCommand;
	}

	@Override
	public AbstractCommand getEditorDeleteCommand() {
		return getEliminaCommand();
	}

	/**
	 *
	 * @return comando per cancellare le aree magazzino selezionate
	 */
	private ActionCommand getEliminaCommand() {
		if (eliminaCommand == null) {
			eliminaCommand = new EliminaAreaDocumentoCommand<E>();
			listMultipleSelectionGuardElimina = new ListMultipleSelectionGuard(getListSelectionAdapterElimina(),
					eliminaCommand);
			eliminaCommand.setAreaBD(areaBD);
			eliminaAreaPreventivoCommandInterceptor = new EliminaAreaDocumentoCommandInterceptor();
			eliminaCommand.addCommandInterceptor(eliminaAreaPreventivoCommandInterceptor);

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
	 *
	 * @return command
	 */
	private OpenAreaDocumentoEditorCommand getOpenAreaOrdineEditorCommand() {
		if (openAreaPreventivoEditorCommand == null) {
			openAreaPreventivoEditorCommand = new OpenAreaDocumentoEditorCommand();
			listMultipleSelectionGuardOpen = new ListMultipleSelectionGuard(getListSelectionAdapter(),
					openAreaPreventivoEditorCommand);
			getTable().setPropertyCommandExecutor(openAreaPreventivoEditorCommand);
		}
		return openAreaPreventivoEditorCommand;
	}

	protected SpedizioneDocumentiCommand<AreaPreventivo> getStampaAreePreventivoCommand() {
		if (stampaAreePreventivoCommand == null) {
			stampaAreePreventivoCommand = new SpedizioneDocumentiCommand<AreaPreventivo>(
					StampaAreeDocumentoCommand.COMMAND_ID, AreaPreventivo.class);
			new ListMultipleSelectionGuard(getListSelectionAdapter(), stampaAreePreventivoCommand);
			stampaAreePreventivoCommand.addCommandInterceptor(new ActionCommandInterceptor() {

				@Override
				public void postExecution(ActionCommand command) {
				}

				@Override
				public boolean preExecution(ActionCommand command) {
					List<AreaPreventivoRicerca> selectedObjects = getTable().getSelectedObjects();

					if (selectedObjects != null) {
						List<Integer> idDocumenti = new ArrayList<Integer>();
						for (AreaPreventivoRicerca areaPreventivoRicerca : selectedObjects) {
							if (areaPreventivoRicerca.getStatoAreaPreventivo() == StatoAreaPreventivo.ACCETTATO) {
								idDocumenti.add(areaPreventivoRicerca.getDocumento().getId());
							}
						}
						command.addParameter(SpedizioneDocumentiCommand.PARAM_ID_DOCUMENTI_DA_SPEDIRE, idDocumenti);
						command.addParameter(SpedizioneDocumentiCommand.PARAM_FORZA_STAMPA, true);
					}

					return selectedObjects != null && !selectedObjects.isEmpty();
				}
			});
		}
		return stampaAreePreventivoCommand;
	}

	@Override
	public List<AreaPreventivoRicerca> loadTableData() {
		List<AreaPreventivoRicerca> aree = null;
		if (parametriRicerca.isEffettuaRicerca()) {
			aree = refreshTableData();
		}

		return aree;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onEditorEvent(ApplicationEvent event) {
		// se è una cancellazione di un area magazzino la trasformo in un areamagazzinoricerca per cancellarla
		PanjeaLifecycleApplicationEvent panjeaEvent = (PanjeaLifecycleApplicationEvent) event;
		if (panjeaEvent.getEventType().equals(LifecycleApplicationEvent.DELETED)
				&& panjeaEvent.getSource() instanceof AreaPreventivo) {
			AreaPreventivoRicerca areaPreventivoRicerca = creaAreaDocumentoRicerca();
			E areaPreventivo = (E) panjeaEvent.getSource();
			areaPreventivoRicerca.setIdAreaPreventivo(areaPreventivo.getId());
			getTable().removeRowObject(areaPreventivoRicerca);
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
	 * Lancia l'evento per aprire l'area ordine selezionata.
	 */
	private void openAreaPreventivoEditor() {
		logger.debug("--> Enter openAreaOrdineEditor");
		AreaPreventivoRicerca areaPreventivoRicerca = getTable().getSelectedObject();
		if (areaPreventivoRicerca == null) {
			return;
		}
		E areaPreventivo = creaAreaDocumento();
		areaPreventivo.setId(areaPreventivoRicerca.getIdAreaDocumento());

		D areaPreventivoFullDTO = areaBD.caricaAreaFullDTO(areaPreventivo);
		LifecycleApplicationEvent event = new OpenEditorEvent(areaPreventivoFullDTO);
		Application.instance().getApplicationContext().publishEvent(event);
		logger.debug("--> Exit openAreaOrdineEditor");
	}

	@Override
	public List<AreaPreventivoRicerca> refreshTableData() {
		if (parametriRicerca.isEffettuaRicerca()) {
			return ricercaAreeDocumento(parametriRicerca);
		}
		return Collections.<AreaPreventivoRicerca> emptyList();
	}

	/**
	 *
	 * @param parametri
	 *            parametri di ricerca
	 * @return lista dei documenti trovati
	 */
	protected abstract List<AreaPreventivoRicerca> ricercaAreeDocumento(P parametri);

	/**
	 * @param preventiviBD
	 *            the preventiviBD to set
	 */
	public void setAreaBD(IAreaDocumentoBD<E, D> preventiviBD) {
		this.areaBD = preventiviBD;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setFormObject(Object object) {
		if (object instanceof AbstractParametriRicerca) {
			this.parametriRicerca = (P) object;
		} else {
			this.parametriRicerca = creaParametriRicercaAreaDocumento();
		}
	}

}
