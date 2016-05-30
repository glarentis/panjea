package it.eurotn.panjea.magazzino.rich.editors.categoriacontabile;

import it.eurotn.panjea.magazzino.domain.CategoriaContabileArticolo;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoContabilizzazioneBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.Collection;

/**
 * 
 * @author angelo
 * 
 */
public class CategoriaContabileArticoloTablePage extends AbstractTablePageEditor<CategoriaContabileArticolo> {

	private IMagazzinoContabilizzazioneBD magazzinoContabilizzazioneBD;

	/**
	 * Costruttore.
	 */
	protected CategoriaContabileArticoloTablePage() {
		super("categoriaContabileArticoloTablePage", new String[] { "codice" }, CategoriaContabileArticolo.class);
	}

	@Override
	public Collection<CategoriaContabileArticolo> loadTableData() {
		return magazzinoContabilizzazioneBD.caricaCategorieContabileArticolo("codice", null);
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public Collection<CategoriaContabileArticolo> refreshTableData() {
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
