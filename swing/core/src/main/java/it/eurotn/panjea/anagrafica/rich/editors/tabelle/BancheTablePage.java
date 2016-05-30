package it.eurotn.panjea.anagrafica.rich.editors.tabelle;

import it.eurotn.panjea.anagrafica.domain.Banca;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.List;

public class BancheTablePage extends AbstractTablePageEditor<Banca> {

	public static final String PAGE_ID = "bancheTablePage";

	private IAnagraficaBD anagraficaBD;

	/**
	 * Costruttore.
	 */
	protected BancheTablePage() {
		super(PAGE_ID, new String[] { "cin", "codice", "descrizione" }, Banca.class);
	}

	@Override
	public List<Banca> loadTableData() {
		return anagraficaBD.caricaBanche("codice", null);
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public List<Banca> refreshTableData() {
		return loadTableData();
	}

	/**
	 * @param anagraficaBD
	 *            the anagraficaBD to set
	 */
	public void setAnagraficaBD(IAnagraficaBD anagraficaBD) {
		this.anagraficaBD = anagraficaBD;
	}

	@Override
	public void setFormObject(Object object) {
		// Non utilizzato, carico tutte le banche
	}

}
