package it.eurotn.panjea.magazzino.rich.editors.categoriacontabile;

import it.eurotn.panjea.magazzino.domain.CategoriaContabileSedeMagazzino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoContabilizzazioneBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.Collection;

public class CategoriaContabileSedeMagazzinoTablePage extends AbstractTablePageEditor<CategoriaContabileSedeMagazzino> {

	private IMagazzinoContabilizzazioneBD magazzinoContabilizzazioneBD;

	/**
	 * Costruttore.
	 */
	protected CategoriaContabileSedeMagazzinoTablePage() {
		super("categoriaContabileSedeMagazzinoTablePage", new String[] { "codice" },
				CategoriaContabileSedeMagazzino.class);
	}

	@Override
	public Collection<CategoriaContabileSedeMagazzino> loadTableData() {
		return magazzinoContabilizzazioneBD.caricaCategorieContabileSedeMagazzino("codice", null);
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public Collection<CategoriaContabileSedeMagazzino> refreshTableData() {
		return null;
	}

	@Override
	public void setFormObject(Object object) {
	}

	/**
	 * @param magazzinoContabilizzazioneBD
	 *            magazzinoContabilizzazioneBD to set
	 */
	public void setMagazzinoContabilizzazioneBD(IMagazzinoContabilizzazioneBD magazzinoContabilizzazioneBD) {
		this.magazzinoContabilizzazioneBD = magazzinoContabilizzazioneBD;
	}

}
