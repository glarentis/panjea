/**
 * 
 */
package it.eurotn.panjea.anagrafica.rich.editors.documento;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.Collection;

import org.springframework.richclient.command.AbstractCommand;

/**
 * @author leonardo
 */
public class DocumentiTablePage extends AbstractTablePageEditor<Documento> {

	public static final String PAGE_ID = "documentiTablePage";

	/**
	 * Costruttore.
	 */
	public DocumentiTablePage() {
		this(PAGE_ID);
		setShowTitlePane(false);
	}

	/**
	 * Costruttore.
	 * 
	 * @param pageId
	 *            l'id della table page
	 */
	public DocumentiTablePage(final String pageId) {
		super(pageId, new String[] { "codice", "dataDocumento", "tipoDocumento", "entita", "totale.importoInValuta" },
				Documento.class);
	}

	@Override
	public AbstractCommand[] getCommands() {
		return null;
	}

	@Override
	public Collection<Documento> loadTableData() {
		return null;
	}

	@Override
	public void onPostPageOpen() {

	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public Collection<Documento> refreshTableData() {
		return loadTableData();
	}

	@Override
	public void setFormObject(Object object) {

	}

}
