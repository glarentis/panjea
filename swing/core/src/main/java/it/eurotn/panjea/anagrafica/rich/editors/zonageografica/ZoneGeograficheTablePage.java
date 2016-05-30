/**
 * 
 */
package it.eurotn.panjea.anagrafica.rich.editors.zonageografica;

import it.eurotn.panjea.anagrafica.domain.ZonaGeografica;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaTabelleBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.List;

/**
 * Tabella che gestisce le zone geografiche.<br>
 * 
 * @author Leonardo
 */
public class ZoneGeograficheTablePage extends AbstractTablePageEditor<ZonaGeografica> {

	public static final String PAGE_ID = "zoneGeograficheTablePage";

	private IAnagraficaTabelleBD anagraficaTabelleBD = null;

	/**
	 * Costruttore di default.
	 */
	public ZoneGeograficheTablePage() {
		super(PAGE_ID, new String[] { "codice", "descrizione" }, ZonaGeografica.class);
	}

	@Override
	public List<ZonaGeografica> loadTableData() {
		return anagraficaTabelleBD.caricaZoneGeografiche("codice", null);
	}

	@Override
	public void onPostPageOpen() {

	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public List<ZonaGeografica> refreshTableData() {
		return loadTableData();
	}

	/**
	 * @param anagraficaTabelleBD
	 *            the anagraficaTabelleBD to set
	 */
	public void setAnagraficaTabelleBD(IAnagraficaTabelleBD anagraficaTabelleBD) {
		this.anagraficaTabelleBD = anagraficaTabelleBD;
	}

	@Override
	public void setFormObject(Object object) {

	}

}
