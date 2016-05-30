/**
 * 
 */
package it.eurotn.panjea.contabilita.rich.editors.tabelle;

import it.eurotn.panjea.contabilita.domain.RegistroIva;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD;

import java.util.Collection;

/**
 * @author Leonardo
 * 
 */
public class RegistriIvaTablePage extends it.eurotn.rich.editors.AbstractTablePageEditor<RegistroIva> {

	private static final String PAGE_ID = "registriIvaTablePage";
	private IContabilitaAnagraficaBD contabilitaAnagraficaBD;

	/**
	 * Costruttore.
	 */
	protected RegistriIvaTablePage() {
		super(PAGE_ID, new String[] { "numero", "descrizione", "tipoRegistro" }, RegistroIva.class);
	}

	@Override
	public Collection<RegistroIva> loadTableData() {
		return contabilitaAnagraficaBD.caricaRegistriIva("nome", null);
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public Collection<RegistroIva> refreshTableData() {
		return null;
	}

	/**
	 * @param contabilitaAnagraficaBD
	 *            contabilitaAnagraficaBD to set
	 */
	public void setContabilitaAnagraficaBD(IContabilitaAnagraficaBD contabilitaAnagraficaBD) {
		this.contabilitaAnagraficaBD = contabilitaAnagraficaBD;
	}

	@Override
	public void setFormObject(Object object) {
	}

}
