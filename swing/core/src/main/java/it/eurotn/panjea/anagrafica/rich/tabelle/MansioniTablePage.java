/**
 * 
 */
package it.eurotn.panjea.anagrafica.rich.tabelle;

import it.eurotn.panjea.anagrafica.domain.Mansione;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaTabelleBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.List;

/**
 * @author Leonardo
 * 
 */
public class MansioniTablePage extends AbstractTablePageEditor<Mansione> {

	private static final String PAGE_ID = "mansioniTablePage";

	private IAnagraficaTabelleBD anagraficaTabelleBD;

	/**
	 * Costruttore.
	 */
	protected MansioniTablePage() {
		super(PAGE_ID, new String[] { Mansione.PROP_DESCRIZIONE }, Mansione.class);
	}

	@Override
	public List<Mansione> loadTableData() {
		return anagraficaTabelleBD.caricaMansioni(null);

	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public List<Mansione> refreshTableData() {
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
