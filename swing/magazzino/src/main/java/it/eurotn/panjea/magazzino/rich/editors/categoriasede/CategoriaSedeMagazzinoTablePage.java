/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.editors.categoriasede;

import it.eurotn.panjea.magazzino.domain.CategoriaSedeMagazzino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.Collection;

/**
 * @author fattazzo
 * 
 */
public class CategoriaSedeMagazzinoTablePage extends AbstractTablePageEditor<CategoriaSedeMagazzino> {

	public static final String PAGE_ID = "categoriaSedeMagazzinoTablePage";

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	/**
	 * Costruttore.
	 */
	protected CategoriaSedeMagazzinoTablePage() {
		super(PAGE_ID, new String[] { "descrizione" }, CategoriaSedeMagazzino.class);
	}

	@Override
	public Collection<CategoriaSedeMagazzino> loadTableData() {
		return magazzinoAnagraficaBD.caricaCategorieSediMagazzino("descrizione", null);
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public Collection<CategoriaSedeMagazzino> refreshTableData() {
		return null;
	}

	@Override
	public void setFormObject(Object object) {
	}

	/**
	 * @param magazzinoAnagraficaBD
	 *            magazzinoAnagraficaBD to set
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}

}
