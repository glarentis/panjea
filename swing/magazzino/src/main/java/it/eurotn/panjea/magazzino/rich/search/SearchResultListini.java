/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.search;

import it.eurotn.panjea.magazzino.domain.Listino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.rich.pages.AbstractTableSearchResult;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.richclient.command.AbstractCommand;

/**
 * @author fattazzo
 * 
 */
public class SearchResultListini extends AbstractTableSearchResult<Listino> {

	private static final String VIEW_ID = "searchResultListini";

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	@Override
	protected Listino doDelete(Listino objectToDelete) {
		Listino listino = objectToDelete;
		magazzinoAnagraficaBD.cancellaListino(listino);
		return listino;
	}

	@Override
	protected String[] getColumnPropertyNames() {

		return new String[] { "codice", "descrizione", "tipoListino", "codiceValuta" };
	}

	@Override
	protected AbstractCommand[] getCommand() {
		AbstractCommand[] abstractCommands = new AbstractCommand[] { getRefreshCommand() };
		abstractCommands = (AbstractCommand[]) PanjeaSwingUtil.resizeArray(abstractCommands,
				abstractCommands.length + 1);
		AbstractCommand abstractCommand = (AbstractCommand) getActiveWindow().getCommandManager().getCommand(
				"newListinoCommand");
		abstractCommands[abstractCommands.length - 1] = abstractCommand;
		return abstractCommands;
	}

	@Override
	protected Collection<Listino> getData(Map<String, Object> parameters) {
		List<Listino> list = magazzinoAnagraficaBD.caricaListini();
		return list;
	}

	@Override
	public String getId() {
		return VIEW_ID;
	}

	@Override
	protected Class<Listino> getObjectsClass() {
		return Listino.class;
	}

	@Override
	protected Map<String, Object> getParameters() {
		return null;
	}

	@Override
	public Object reloadObject(Listino object) {
		Listino listino = object;
		listino = magazzinoAnagraficaBD.caricaListino(listino, true);
		return listino;
	}

	/**
	 * @param magazzinoAnagraficaBD
	 *            setter of magazzinoAnagraficaBD
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}
}
