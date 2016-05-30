package it.eurotn.panjea.centricosto.rich.editors;

import it.eurotn.panjea.centricosto.domain.CentroCosto;
import it.eurotn.panjea.centricosto.rich.bd.ICentriCostoBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.Collection;

public class CentriCostoTablePage extends AbstractTablePageEditor<CentroCosto> {

	public static final String PAGE_ID = "centriCostoTablePage";
	private ICentriCostoBD centriCostoBD;

	/**
	 * Costruttore.
	 */
	protected CentriCostoTablePage() {
		super(PAGE_ID, new String[] { "codice", "descrizione" }, CentroCosto.class);
	}

	@Override
	public Collection<CentroCosto> loadTableData() {
		return centriCostoBD.caricaCentriCosto(null);
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public Collection<CentroCosto> refreshTableData() {
		return loadTableData();
	}

	/**
	 * @param centriCostoBD
	 *            The centriCostoBD to set.
	 */
	public void setCentriCostoBD(ICentriCostoBD centriCostoBD) {
		this.centriCostoBD = centriCostoBD;
	}

	@Override
	public void setFormObject(Object object) {
	}

}