/**
 * 
 */
package it.eurotn.panjea.anagrafica.rich.tabelle;

import it.eurotn.panjea.anagrafica.domain.TipoSedeEntita;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaTabelleBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.List;

/**
 * @author Leonardo
 * 
 */
public class TipiSedeEntitaTablePage extends AbstractTablePageEditor<TipoSedeEntita> {
	private static final String PAGE_ID = "tipiSedeEntitaTablePage";
	private IAnagraficaTabelleBD anagraficaTabelleBD;

	/**
	 * Costruttore.
	 */
	protected TipiSedeEntitaTablePage() {
		super(PAGE_ID, new String[] { TipoSedeEntita.PROP_CODICE, TipoSedeEntita.PROP_DESCRIZIONE,
				TipoSedeEntita.PROP_SEDE_PRINCIPALE }, TipoSedeEntita.class);
	}

	@Override
	public List<TipoSedeEntita> loadTableData() {
		return anagraficaTabelleBD.caricaTipiSede(null);
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public List<TipoSedeEntita> refreshTableData() {
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
