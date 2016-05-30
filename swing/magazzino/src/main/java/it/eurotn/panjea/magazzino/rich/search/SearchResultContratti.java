/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.search;

import it.eurotn.panjea.magazzino.domain.Contratto;
import it.eurotn.panjea.magazzino.rich.bd.IContrattoBD;
import it.eurotn.panjea.magazzino.util.ParametriRicercaContratti;
import it.eurotn.panjea.rich.pages.AbstractTableSearchResult;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.richclient.command.AbstractCommand;

/**
 * TableSearchResult per la ricerca di {@link Contratto}.
 * 
 * @author adriano
 * @version 1.0, 18/giu/08
 */
public class SearchResultContratti extends AbstractTableSearchResult<Contratto> {

	private static final String VIEW_ID = "searchResultContratti";

	private IContrattoBD contrattoBD;

	@Override
	protected Contratto doDelete(Contratto objectToDelete) {
		Contratto contratto = objectToDelete;
		contrattoBD.cancellaContratto(contratto);
		return contratto;
	}

	@Override
	protected String[] getColumnPropertyNames() {
		return new String[] { "codice", "descrizione", "dataInizio", "dataFine" };
	}

	@Override
	protected AbstractCommand[] getCommand() {
		AbstractCommand[] abstractCommands = getDefaultCommand();

		abstractCommands = (AbstractCommand[]) PanjeaSwingUtil.resizeArray(abstractCommands,
				abstractCommands.length + 1);
		AbstractCommand newCommand = (AbstractCommand) getActiveWindow().getCommandManager().getCommand(
				"newContrattoCommand");
		abstractCommands[abstractCommands.length - 1] = newCommand;
		return abstractCommands;
	}

	/**
	 * @return Returns the magazzinoAnagraficaBD.
	 */
	public IContrattoBD getContrattoBD() {
		return contrattoBD;
	}

	@Override
	protected Collection<Contratto> getData(Map<String, Object> parameters) {
		ParametriRicercaContratti parametriRicercaContratti = null;
		// recupera dalla Map parameters l'oggetto ParametriRicercaContratti se esiste, per poi essere passato al metodo
		// di ricerca
		if (parameters.containsKey(ParametriRicercaContratti.REF)) {
			parametriRicercaContratti = (ParametriRicercaContratti) parameters.get(ParametriRicercaContratti.REF);
		}
		List<Contratto> contratti = contrattoBD.caricaContratti(parametriRicercaContratti);
		return contratti;
	}

	@Override
	public String getId() {
		return VIEW_ID;
	}

	@Override
	protected Class<Contratto> getObjectsClass() {
		return Contratto.class;
	}

	@Override
	protected Map<String, Object> getParameters() {
		return new HashMap<String, Object>();
	}

	@Override
	public Object reloadObject(Contratto object) {
		Contratto contratto = object;
		contratto = contrattoBD.caricaContratto(contratto, true);
		return contratto;
	}

	/**
	 * @param contrattoBD
	 *            The contrattoBD to set.
	 */
	public void setContrattoBD(IContrattoBD contrattoBD) {
		this.contrattoBD = contrattoBD;
	}
}
