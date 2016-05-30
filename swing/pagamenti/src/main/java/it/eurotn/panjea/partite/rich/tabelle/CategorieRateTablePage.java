package it.eurotn.panjea.partite.rich.tabelle;

import it.eurotn.panjea.partite.domain.CategoriaRata;
import it.eurotn.panjea.partite.rich.bd.IPartiteBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.Collection;

public class CategorieRateTablePage extends AbstractTablePageEditor<CategoriaRata> {
	private static final String PAGE_ID = "categoriaRateTablePage";
	private IPartiteBD partiteBD;

	/**
	 * Costruttore.
	 */
	protected CategorieRateTablePage() {
		super(PAGE_ID, new String[] { "descrizione" }, CategoriaRata.class);
	}

	@Override
	public Collection<CategoriaRata> loadTableData() {
		return partiteBD.caricaCategorieRata("descrizione", null);
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public Collection<CategoriaRata> refreshTableData() {
		return null;
	}

	@Override
	public void setFormObject(Object object) {
	}

	/**
	 * @param partiteBD
	 *            partiteBD to set
	 */
	public void setPartiteBD(IPartiteBD partiteBD) {
		this.partiteBD = partiteBD;
	}

}
