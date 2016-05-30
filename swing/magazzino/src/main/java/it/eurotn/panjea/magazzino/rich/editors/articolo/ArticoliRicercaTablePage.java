package it.eurotn.panjea.magazzino.rich.editors.articolo;

import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.util.ArticoloRicerca;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.Collection;

import org.springframework.richclient.command.AbstractCommand;

/**
 * Classe base con model per mostrare in tabella una lista di ArticoloLite.
 *
 * @author leonardo
 */
public class ArticoliRicercaTablePage extends AbstractTablePageEditor<ArticoloRicerca> {

	public static final String PAGE_ID = "articoliTablePage";
	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	/**
	 * Costruttore di default.
	 */
	public ArticoliRicercaTablePage() {
		this(PAGE_ID);
	}

	/**
	 * Costruttore.
	 *
	 * @param pageId
	 *            l'id della pagina
	 */
	protected ArticoliRicercaTablePage(final String pageId) {
		super(pageId, new ArticoliRicercaTableModel());
		GiacenzaCellRenderer giacenzaCellRenderer = new GiacenzaCellRenderer();
		getTable().getTable().getColumnModel().getColumn(4).setCellRenderer(giacenzaCellRenderer);
		getTable().getTable().getColumnModel().getColumn(4).setCellEditor(giacenzaCellRenderer);
	}

	@Override
	public AbstractCommand[] getCommands() {
		return new AbstractCommand[] { getRefreshCommand() };
	}

	/**
	 * @return Returns the magazzinoAnagraficaBD.
	 */
	public IMagazzinoAnagraficaBD getMagazzinoAnagraficaBD() {
		return magazzinoAnagraficaBD;
	}

	@Override
	public Collection<ArticoloRicerca> loadTableData() {
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
	public void postSetFormObject(Object object) {

	}

	@Override
	public Collection<ArticoloRicerca> refreshTableData() {
		return loadTableData();
	}

	@Override
	public void setFormObject(Object object) {
	}

	/**
	 * @param magazzinoAnagraficaBD
	 *            The magazzinoAnagraficaBD to set.
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}

}
