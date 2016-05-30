/**
 *
 */
package it.eurotn.panjea.magazzino.rich.editors.tipoporto;

import it.eurotn.panjea.magazzino.domain.TipoPorto;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.Collection;

/**
 * @author fattazzo
 * 
 */
public class TipiPortoTablePage extends AbstractTablePageEditor<TipoPorto> {

	public static final String PAGE_ID = "tipiPortoTablePage";

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	/**
	 * Costruttore.
	 */
	protected TipiPortoTablePage() {
		super(PAGE_ID, new String[] { "descrizione" }, TipoPorto.class);
	}

	@Override
	public Collection<TipoPorto> loadTableData() {
		return magazzinoAnagraficaBD.caricaTipiPorto(null);
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public Collection<TipoPorto> refreshTableData() {
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
