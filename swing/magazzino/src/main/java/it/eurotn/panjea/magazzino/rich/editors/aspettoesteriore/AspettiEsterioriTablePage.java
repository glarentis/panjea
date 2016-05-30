package it.eurotn.panjea.magazzino.rich.editors.aspettoesteriore;

import it.eurotn.panjea.magazzino.domain.AspettoEsteriore;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.Collection;

/**
 * 
 * @author angelo
 * 
 */
public class AspettiEsterioriTablePage extends AbstractTablePageEditor<AspettoEsteriore> {

	public static final String PAGE_ID = "aspettiEsterioriTablePage";

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	/**
	 * Costruttore.
	 */
	protected AspettiEsterioriTablePage() {
		super(PAGE_ID, new String[] { "descrizione" }, AspettoEsteriore.class);
	}

	@Override
	public Collection<AspettoEsteriore> loadTableData() {
		return magazzinoAnagraficaBD.caricaAspettiEsteriori(null);
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public Collection<AspettoEsteriore> refreshTableData() {
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
