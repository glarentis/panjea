/**
 * 
 */
package it.eurotn.panjea.auvend.rich.editors;

import it.eurotn.panjea.auvend.domain.CodiceIvaAuVend;
import it.eurotn.panjea.auvend.rich.bd.IAuVendBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.Collection;

/**
 * TablePopup per {@link CodiceIvaAuVend}.
 * 
 * @author adriano
 * @version 1.0, 17/feb/2009
 * 
 */
public class CodiciIvaAuVendTablePage extends AbstractTablePageEditor<CodiceIvaAuVend> {
	private static final String PAGE_ID = "codiciIvaAuVendTablePage";

	private IAuVendBD auVendBD;

	/**
	 * costruttore.
	 */
	protected CodiciIvaAuVendTablePage() {
		super(PAGE_ID, new String[] { "codiceIva.codice", "codiceIva.descrizioneInterna" }, CodiceIvaAuVend.class);
	}

	/**
	 * @return the auVendBD
	 */
	public IAuVendBD getAuVendBD() {
		return auVendBD;
	}

	@Override
	public Collection<CodiceIvaAuVend> loadTableData() {
		return auVendBD.caricaCodiciIvaAuVend();
	}

	@Override
	public void onPostPageOpen() {

	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public Collection<CodiceIvaAuVend> refreshTableData() {
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
