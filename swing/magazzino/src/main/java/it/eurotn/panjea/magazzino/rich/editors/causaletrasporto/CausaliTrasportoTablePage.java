/**
 *
 */
package it.eurotn.panjea.magazzino.rich.editors.causaletrasporto;

import it.eurotn.panjea.magazzino.domain.CausaleTrasporto;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.Collection;

/**
 * @author fattazzo
 * 
 */
public class CausaliTrasportoTablePage extends AbstractTablePageEditor<CausaleTrasporto> {

	public static final String PAGE_ID = "causaliTrasportoTablePage";

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	/**
	 * Costruttore.
	 */
	protected CausaliTrasportoTablePage() {
		super(PAGE_ID, new String[] { "descrizione" }, CausaleTrasporto.class);
	}

	@Override
	public Collection<CausaleTrasporto> loadTableData() {
		return magazzinoAnagraficaBD.caricaCausaliTraporto(null);
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public Collection<CausaleTrasporto> refreshTableData() {
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
