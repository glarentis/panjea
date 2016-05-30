package it.eurotn.panjea.anagrafica.rich.editors.tabelle;

import it.eurotn.panjea.anagrafica.domain.Filiale;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.List;

public class FilialiTablePage extends AbstractTablePageEditor<Filiale> {

	public static final String PAGE_ID = "filialiTablePage";

	private IAnagraficaBD anagraficaBD;

	/**
	 * Costruttore.
	 */
	protected FilialiTablePage() {
		super(PAGE_ID, new String[] { "banca", "codice", "indirizzo" }, Filiale.class);
	}

	@Override
	public List<Filiale> loadTableData() {
		return anagraficaBD.caricaFiliali();
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public List<Filiale> refreshTableData() {
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
		// Non utilizzato, carico tutte le filiali
	}

}
