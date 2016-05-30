package it.eurotn.panjea.anagrafica.rich.tabelle;

import it.eurotn.panjea.anagrafica.domain.Lingua;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaTabelleBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.List;

public class LingueTablePage extends AbstractTablePageEditor<Lingua> {
	private static final String PAGE_ID = "lingueTablePage";
	private IAnagraficaTabelleBD anagraficaTabelleBD;

	/**
	 * Costruttore.
	 */
	protected LingueTablePage() {
		super(PAGE_ID, new String[] { "codice", "descrizione" }, Lingua.class);
	}

	@Override
	public List<Lingua> loadTableData() {
		return anagraficaTabelleBD.caricaLingue();
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public List<Lingua> refreshTableData() {
		return null;
	}

	/**
	 * @param anagraficaTabelleBD
	 *            anagraficaTabelleBD to set
	 */
	public void setAnagraficaTabelleBD(IAnagraficaTabelleBD anagraficaTabelleBD) {
		this.anagraficaTabelleBD = anagraficaTabelleBD;
	}

	@Override
	public void setFormObject(Object object) {
	}

}
