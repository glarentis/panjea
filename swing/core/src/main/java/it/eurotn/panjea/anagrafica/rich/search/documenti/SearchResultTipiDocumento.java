/**
 * 
 */
package it.eurotn.panjea.anagrafica.rich.search.documenti;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.rich.bd.IDocumentiBD;
import it.eurotn.panjea.rich.pages.AbstractTableSearchResult;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.richclient.command.AbstractCommand;

/**
 * TableSearchResult per il caricamento dei tipi documento.
 * 
 * @author adriano
 * @version 1.0, 18/mag/07
 * 
 */
public class SearchResultTipiDocumento extends AbstractTableSearchResult<TipoDocumento> {

	private static final String VIEW_ID = "searchResultTipiDocumento";

	private IDocumentiBD documentiBD;

	@Override
	protected TipoDocumento doDelete(TipoDocumento objectToDelete) {
		documentiBD.cancellaTipoDocumento(objectToDelete);
		return objectToDelete;
	}

	@Override
	protected String[] getColumnPropertyNames() {
		return new String[] { "classeTipoDocumento", "codice", "descrizione", "abilitato" };
	}

	@Override
	protected AbstractCommand[] getCommand() {
		AbstractCommand[] abstractCommands = getDefaultCommand();

		abstractCommands = (AbstractCommand[]) PanjeaSwingUtil.resizeArray(abstractCommands,
				abstractCommands.length + 1);

		AbstractCommand abstractCommand = (AbstractCommand) getActiveWindow().getCommandManager().getCommand(
				"newTipoDocumentoCommand");
		abstractCommands[abstractCommands.length - 1] = abstractCommand;
		return abstractCommands;

	}

	@Override
	protected Collection<TipoDocumento> getData(Map<String, Object> params) {
		List<TipoDocumento> data = documentiBD.caricaTipiDocumento("codice", null, true);
		return data;
	}

	/**
	 * @return Returns the documentiBD.
	 */
	public IDocumentiBD getDocumentiBD() {
		return documentiBD;
	}

	@Override
	public String getId() {
		return VIEW_ID;
	}

	@Override
	protected Class<TipoDocumento> getObjectsClass() {
		return TipoDocumento.class;
	}

	@Override
	protected Map<String, Object> getParameters() {
		return null;
	}

	@Override
	protected DefaultBeanTableModel<TipoDocumento> getTableModel() {
		return new TipoDocumentoSearchObjectTableModel(getId() + ".tableModel", getColumnPropertyNames(),
				TipoDocumento.class);
	}

	@Override
	public Object reloadObject(TipoDocumento object) {
		return documentiBD.caricaTipoDocumento(object.getId());
	}

	/**
	 * @param documentiBD
	 *            The documentiBD to set.
	 */
	public void setDocumentiBD(IDocumentiBD documentiBD) {
		this.documentiBD = documentiBD;
	};
}
