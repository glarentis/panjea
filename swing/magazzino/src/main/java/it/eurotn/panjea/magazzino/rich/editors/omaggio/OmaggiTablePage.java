/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.editors.omaggio;

import it.eurotn.panjea.magazzino.domain.omaggio.Omaggio;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.Collection;

/**
 * @author leonardo
 * 
 */
public class OmaggiTablePage extends AbstractTablePageEditor<Omaggio> {

	public static final String PAGE_ID = "omaggiTablePage";

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	/**
	 * Costruttore.
	 */
	public OmaggiTablePage() {
		super(PAGE_ID, new String[] { "tipoOmaggio", "codiceIva", "descrizionePerStampa" }, Omaggio.class);
	}

	/**
	 * @return the magazzinoAnagraficaBD
	 */
	public IMagazzinoAnagraficaBD getMagazzinoAnagraficaBD() {
		return magazzinoAnagraficaBD;
	}

	@Override
	public Collection<Omaggio> loadTableData() {
		return magazzinoAnagraficaBD.caricaOmaggi();
	}

	@Override
	public void onPostPageOpen() {

	}

	@Override
	public Collection<Omaggio> refreshTableData() {
		return null;
	}

	@Override
	public void setFormObject(Object object) {

	}

	/**
	 * @param magazzinoAnagraficaBD
	 *            the magazzinoAnagraficaBD to set
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}

}
