/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.editors.tipomezzotrasporto;

import it.eurotn.panjea.magazzino.domain.TipoMezzoTrasporto;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.Collection;

/**
 * Tabella che gestisce i tipi mezzo trasporto.<br>
 * 
 * @author Leonardo
 */
public class TipiMezzoTrasportoTablePage extends AbstractTablePageEditor<TipoMezzoTrasporto> {

	public static final String PAGE_ID = "tipiMezzoTrasportoTablePage";

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD = null;

	/**
	 * Costruttore di default.
	 */
	public TipiMezzoTrasportoTablePage() {
		super(PAGE_ID, new String[] { "codice", "descrizione", "articolo" }, TipoMezzoTrasporto.class);
	}

	@Override
	public Collection<TipoMezzoTrasporto> loadTableData() {
		return magazzinoAnagraficaBD.caricaTipiMezzoTrasporto();
	}

	@Override
	public void onPostPageOpen() {

	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public Collection<TipoMezzoTrasporto> refreshTableData() {
		return loadTableData();
	}

	@Override
	public void setFormObject(Object object) {

	}

	/**
	 * Set magazzinoAnagraficaBD.
	 * 
	 * @param magazzinoAnagraficaBD
	 *            il bd da utilizzare per accedere alle operazioni sui tipi mezzo trasporto
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}

}
