/**
 * 
 */
package it.eurotn.panjea.auvend.rich.editors;

import it.eurotn.panjea.auvend.domain.TipoDocumentoBaseAuVend;
import it.eurotn.panjea.auvend.rich.bd.IAuVendBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.Collection;

/**
 * Popup table page per gli oggetti {@link TipoDocumentoBaseAuVend}.
 * 
 * @author adriano
 * @version 1.0, 30/dic/2008
 * 
 */
public class TipiDocumentoBaseAuVendTablePage extends AbstractTablePageEditor<TipoDocumentoBaseAuVend> {

	private static final String PAGE_ID = "tipiDocumentoBaseAuVendTablePage";

	private IAuVendBD auVendBD;

	/**
	 * costruttore.
	 */
	protected TipiDocumentoBaseAuVendTablePage() {
		super(PAGE_ID, new String[] { "tipoOperazione", "tipoAreaMagazzino.tipoDocumento.codice",
				"tipoAreaMagazzino.tipoDocumento.descrizione" }, TipoDocumentoBaseAuVend.class);
	}

	/**
	 * @return the auVendBD
	 */
	public IAuVendBD getAuVendBD() {
		return auVendBD;
	}

	@Override
	public Collection<TipoDocumentoBaseAuVend> loadTableData() {
		return auVendBD.caricaTipiDocumentoBaseAuVend();
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public Collection<TipoDocumentoBaseAuVend> refreshTableData() {
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
