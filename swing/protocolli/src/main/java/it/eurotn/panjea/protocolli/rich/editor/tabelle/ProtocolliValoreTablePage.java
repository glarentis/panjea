/**
 * 
 */
package it.eurotn.panjea.protocolli.rich.editor.tabelle;

import it.eurotn.panjea.protocolli.domain.ProtocolloValore;
import it.eurotn.panjea.protocolli.rich.bd.IProtocolliBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.Collection;

/**
 * Numeratori legati alla gestione dell'azienda.
 * 
 * @author adriano
 * @version 1.0, 11/mag/07
 * 
 */
public class ProtocolliValoreTablePage extends AbstractTablePageEditor<ProtocolloValore> {

	private static final String PAGE_ID = "protocolliValoreTablePage";
	private IProtocolliBD protocolliBD;

	/**
	 * Costruttore.
	 */
	protected ProtocolliValoreTablePage() {
		super(PAGE_ID, new String[] { "codice", "descrizione", "valore" }, ProtocolloValore.class);
	}

	@Override
	public Collection<ProtocolloValore> loadTableData() {
		return protocolliBD.caricaProtocolliValore(null);
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public Collection<ProtocolloValore> refreshTableData() {
		return null;
	}

	@Override
	public void setFormObject(Object object) {
	}

	/**
	 * @param protocolliBD
	 *            protocolliBD to set
	 */
	public void setProtocolliBD(IProtocolliBD protocolliBD) {
		this.protocolliBD = protocolliBD;
	}

}
