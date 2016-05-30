/**
 *
 */
package it.eurotn.panjea.magazzino.rich.editors.sconto;

import it.eurotn.panjea.magazzino.domain.Sconto;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.Collection;

/**
 * @author fattazzo
 *
 */
public class ScontiTablePage extends AbstractTablePageEditor<Sconto> {

	public static final String PAGE_ID = "scontiTablePage";
	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	/**
	 * Costruttore.
	 */
	protected ScontiTablePage() {
		super(PAGE_ID, new ScontiTableModel());
	}

	@Override
	public Collection<Sconto> loadTableData() {
		return magazzinoAnagraficaBD.caricaSconti();
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public Collection<Sconto> refreshTableData() {
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
