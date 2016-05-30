/**
 * 
 */
package it.eurotn.panjea.auvend.rich.editors;

import it.eurotn.panjea.auvend.domain.LetturaFlussoAuVend;
import it.eurotn.panjea.auvend.rich.bd.IAuVendBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.Collection;

/**
 * Table popup per letture flusso.
 * 
 * @author adriano
 * @version 1.0, 31/dic/2008
 * 
 */
public class LettureFlussoAuVendTablePage extends AbstractTablePageEditor<LetturaFlussoAuVend> {

	private static final String PAGE_ID = "lettureFlussoAuVendTablePage";

	private IAuVendBD auVendBD;

	/**
	 * Costruttore.
	 * 
	 */
	protected LettureFlussoAuVendTablePage() {
		super(PAGE_ID, new String[] { "deposito.codice", "deposito.descrizione", "ultimaLetturaFlussoCarichi",
				"ultimaLetturaFlussoFatture" }, LetturaFlussoAuVend.class);

	}

	/**
	 * @return the auVendBD
	 */
	public IAuVendBD getAuVendBD() {
		return auVendBD;
	}

	@Override
	public Collection<LetturaFlussoAuVend> loadTableData() {
		return auVendBD.caricaLettureFlussoAuVend();
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public Collection<LetturaFlussoAuVend> refreshTableData() {
		return null;
	}

	/**
	 * @param auVendBD
	 *            the auVendBD to set
	 */
	public void setAuVendBD(IAuVendBD auVendBD) {
		this.auVendBD = auVendBD;
	}

	@Override
	public void setFormObject(Object object) {
	}

}
