/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.editors.formulatrasformazione;

import it.eurotn.panjea.magazzino.domain.FormulaTrasformazione;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.Collection;

/**
 * 
 * 
 * @author adriano
 * @version 1.0, 25/nov/2008
 * 
 */
public class FormuleTrasformazioneTablePage extends AbstractTablePageEditor<FormulaTrasformazione> {

	private static final String PAGE_ID = "formuleTrasformazioneTablePage";

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	/**
	 * Costruttore.
	 */
	protected FormuleTrasformazioneTablePage() {
		super(PAGE_ID, new String[] { "codice", "formula" }, FormulaTrasformazione.class);
	}

	@Override
	public Collection<FormulaTrasformazione> loadTableData() {
		return magazzinoAnagraficaBD.caricaFormuleTrasformazione(null);
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public Collection<FormulaTrasformazione> refreshTableData() {
		return null;
	}

	@Override
	public void setFormObject(Object object) {
	}

	/**
	 * @param magazzinoAnagraficaBD
	 *            magazzinoAnagraficaBD to set
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}

}
