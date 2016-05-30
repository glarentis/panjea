package it.eurotn.panjea.anagrafica.rich.search;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractButton;

import org.springframework.richclient.application.ApplicationPage;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.util.parametriricerca.ParametriRicercaEntita;
import it.eurotn.panjea.anagrafica.util.parametriricerca.ParametriRicercaEntita.FieldSearch;
import it.eurotn.panjea.rich.pages.AbstractTableSearchResult;
import it.eurotn.panjea.rich.pages.PanjeaDockingApplicationPage;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.command.JideToggleCommand;

public class SearchResultEntita extends AbstractTableSearchResult<EntitaLite> {

	private class AggiungiAbilitatiCommand extends JideToggleCommand {

		public static final String COMMAND_ID = "aggiungiAbilitatiCommand";

		/**
		 * Costruttore.
		 *
		 */
		public AggiungiAbilitatiCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void onButtonAttached(AbstractButton button) {
			super.onButtonAttached(button);
			button.setName(SearchResultEntita.this.getId() + "." + COMMAND_ID);
		}

		@Override
		protected void onDeselection() {
			super.onDeselection();
			ParametriRicercaEntita param = (ParametriRicercaEntita) parameters.get("parametri");
			if (param != null) {
				param.setAbilitato(Boolean.TRUE);
				parameters.put("parametri", param);
				search(parameters);
			}
		}

		@Override
		protected void onSelection() {
			super.onSelection();
			ParametriRicercaEntita param = (ParametriRicercaEntita) parameters.get("parametri");
			if (param != null) {
				param.setAbilitato(null);
				parameters.put("parametri", param);
				search(parameters);
			}
		}
	}

	// Apro il form di ricerca per l'anagrafica con i parametri di ricerca
	// dell'entità
	private class ApriAnagraficaCommand extends ActionCommand {

		private static final String COMMAND_ID = "ApriAnagraficaCommand";

		/**
		 * Costruttore.
		 */
		public ApriAnagraficaCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			Map<String, Object> mapParamAnag = new HashMap<String, Object>();

			ParametriRicercaEntita parametri = new ParametriRicercaEntita();
			parametri.setTipoEntita(null);
			parametri.setAbilitato(null);
			parametri.setFieldSearch(FieldSearch.NONE);

			mapParamAnag.put("parametri", parametri);

			ApplicationPage applicationPage = getActiveWindow().getPage();
			((PanjeaDockingApplicationPage) applicationPage)
					.openResultView("it.eurotn.panjea.anagrafica.domain.Anagrafica", mapParamAnag);
		}

		@Override
		public String getSecurityControllerId() {
			return super.getId();
		}

		@Override
		protected void onButtonAttached(AbstractButton button) {
			super.onButtonAttached(button);
			button.setName(SearchResultEntita.this.getId() + "." + COMMAND_ID);
		}
	}

	protected IAnagraficaBD anagraficaBD;
	private String id;
	private Map<String, Object> parameters;
	private EntitaLite entita;
	private String idNewEntitaCommand;

	private ActionCommand apriAnagraficaCommand;

	/**
	 * Costruttore.
	 */
	public SearchResultEntita() {
		super();
		parameters = new HashMap<String, Object>();
	}

	@Override
	protected EntitaLite doDelete(EntitaLite objectToRemove) {
		final EntitaLite entitaLiteResult = objectToRemove;
		// la searchResultView dell'entita' contiene una entita' lite
		// Entita entitaResult = entitaLiteResult.creaProxyEntita();
		Entita entitaResult = anagraficaBD.caricaEntita(entitaLiteResult, false);
		getAnagraficaBD().cancellaEntita(entitaResult);
		return entitaLiteResult;
	}

	/**
	 * @return setter of IAnagraficaBD
	 */
	public IAnagraficaBD getAnagraficaBD() {
		return anagraficaBD;
	}

	@Override
	protected String[] getColumnPropertyNames() {
		return new String[] { Entita.PROP_CODICE, "anagrafica.denominazione", "anagrafica.sedeAnagrafica.indirizzo",
				"anagrafica.partiteIVA", "anagrafica.sedeAnagrafica.datiGeografici.cap.descrizione",
				"anagrafica.sedeAnagrafica.datiGeografici.descrizioneLocalita",
				"anagrafica.sedeAnagrafica.datiGeografici.livelloAmministrativo1.nome",
				"anagrafica.sedeAnagrafica.datiGeografici.livelloAmministrativo2.nome",
				"anagrafica.sedeAnagrafica.datiGeografici.livelloAmministrativo3.nome",
				"anagrafica.sedeAnagrafica.datiGeografici.livelloAmministrativo4.nome",
				"anagrafica.sedeAnagrafica.datiGeografici.descrizioneNazione", "anagrafica.sedeAnagrafica.telefono",
				"anagrafica.sedeAnagrafica.fax", "anagrafica.codiceFiscale" };
	}

	@Override
	protected AbstractCommand[] getCommand() {
		AbstractCommand[] abstractCommands = getDefaultCommand();
		// Ai comandi di default aggiung il nuovo e ApriAnagraficaCommand
		abstractCommands = (AbstractCommand[]) PanjeaSwingUtil.resizeArray(abstractCommands,
				abstractCommands.length + 3);
		abstractCommands[abstractCommands.length - 3] = (AbstractCommand) getActiveWindow().getCommandManager()
				.getCommand(idNewEntitaCommand);

		if (apriAnagraficaCommand == null) {
			apriAnagraficaCommand = new ApriAnagraficaCommand();
		}

		abstractCommands[abstractCommands.length - 2] = apriAnagraficaCommand;
		abstractCommands[abstractCommands.length - 1] = new AggiungiAbilitatiCommand();
		return abstractCommands;
	}

	@Override
	protected Collection<EntitaLite> getData(Map<String, Object> params) {
		this.parameters = params;
		return anagraficaBD.ricercaEntita((ParametriRicercaEntita) parameters.get("parametri"));
	}

	/**
	 * @return Returns the entitaResult.
	 */
	public EntitaLite getEntita() {
		return entita;
	}

	@Override
	public String getId() {
		return this.id;
	}

	/**
	 * @return getter of idNewEntitaCommand
	 */
	public String getIdNewEntitaCommand() {
		return idNewEntitaCommand;
	}

	@Override
	protected Class<EntitaLite> getObjectsClass() {
		return EntitaLite.class;
	}

	@Override
	protected Map<String, Object> getParameters() {
		return null;
	}

	@Override
	public Object reloadObject(EntitaLite object) {

		EntitaLite entitaLoad = null;
		try {
			entitaLoad = (EntitaLite) Class.forName(entita.getClass().getName()).newInstance();
			entitaLoad.setId(object.getId());
			entitaLoad.setVersion(object.getVersion());
		} catch (Exception e) {
			throw new RuntimeException("errore, impossibile instanziare l'entitaLite ", e);
		}

		return anagraficaBD.caricaEntita(entitaLoad, false);
	}

	/**
	 * @param anagraficaBD
	 *            anagraficaBD
	 */
	public void setAnagraficaBD(IAnagraficaBD anagraficaBD) {
		this.anagraficaBD = anagraficaBD;
	}

	/**
	 * @param entita
	 *            The entitaResult to set.
	 */
	public void setEntita(EntitaLite entita) {
		this.entita = entita;
	}

	/**
	 * @param id
	 *            id della pagina
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @param idNewEntitaCommand
	 *            setter of idNewEntitaCommand
	 */
	public void setIdNewEntitaCommand(String idNewEntitaCommand) {
		this.idNewEntitaCommand = idNewEntitaCommand;
	}

}
