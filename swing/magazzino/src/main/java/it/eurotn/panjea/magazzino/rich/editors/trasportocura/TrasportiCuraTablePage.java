/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.editors.trasportocura;

import it.eurotn.panjea.magazzino.domain.TrasportoCura;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.Collection;

/**
 * @author fattazzo
 * 
 */
public class TrasportiCuraTablePage extends AbstractTablePageEditor<TrasportoCura> {

	public static final String PAGE_ID = "trasportiCuraTablePage";

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	/**
	 * Costruttore.
	 */
	protected TrasportiCuraTablePage() {
		super(PAGE_ID, new String[] { "descrizione", "mittente" }, TrasportoCura.class);
	}

	@Override
	public Collection<TrasportoCura> loadTableData() {
		return magazzinoAnagraficaBD.caricaTrasportiCura(null);
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public Collection<TrasportoCura> refreshTableData() {
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
