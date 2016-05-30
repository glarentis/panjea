package it.eurotn.panjea.magazzino.rich.editors.categoriacontabile;

import it.eurotn.panjea.magazzino.domain.CategoriaContabileDeposito;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoContabilizzazioneBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.Collection;

/**
 * 
 * @author angelo
 * 
 */
public class CategoriaContabileDepositoTablePage extends AbstractTablePageEditor<CategoriaContabileDeposito> {

	private IMagazzinoContabilizzazioneBD magazzinoContabilizzazioneBD;

	/**
	 * Costruttore.
	 */
	protected CategoriaContabileDepositoTablePage() {
		super("categoriaContabileDepositoTablePage", new String[] { "codice" }, CategoriaContabileDeposito.class);
	}

	@Override
	public Collection<CategoriaContabileDeposito> loadTableData() {
		return magazzinoContabilizzazioneBD.caricaCategorieContabileDeposito("codice", null);
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public Collection<CategoriaContabileDeposito> refreshTableData() {
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
