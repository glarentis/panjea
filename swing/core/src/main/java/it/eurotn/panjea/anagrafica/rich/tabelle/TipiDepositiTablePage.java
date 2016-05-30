/**
 * 
 */
package it.eurotn.panjea.anagrafica.rich.tabelle;

import it.eurotn.panjea.anagrafica.domain.TipoDeposito;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaTabelleBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.List;

/**
 * @author Leonardo
 * 
 */
public class TipiDepositiTablePage extends AbstractTablePageEditor<TipoDeposito> {

	private static final String PAGE_ID = "tipiDepositiTablePage";

	private IAnagraficaTabelleBD anagraficaTabelleBD;

	/**
	 * Costruttore.
	 */
	protected TipiDepositiTablePage() {
		super(PAGE_ID, new String[] { "codice" }, TipoDeposito.class);
	}

	@Override
	public List<TipoDeposito> loadTableData() {
		return anagraficaTabelleBD.caricaTipiDepositi();

	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public List<TipoDeposito> refreshTableData() {
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
