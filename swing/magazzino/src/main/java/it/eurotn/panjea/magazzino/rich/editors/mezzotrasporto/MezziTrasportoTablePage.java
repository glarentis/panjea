package it.eurotn.panjea.magazzino.rich.editors.mezzotrasporto;

import it.eurotn.panjea.magazzino.domain.MezzoTrasporto;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.Collection;

/**
 * Tabella che gestisce i mezzi di trasporto.<br>
 *
 * @author Leonardo
 */
public class MezziTrasportoTablePage extends AbstractTablePageEditor<MezzoTrasporto> {

	public static final String PAGE_ID = "mezziTrasportoTablePage";

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD = null;

	/**
	 * Costruttore di default.
	 */
	public MezziTrasportoTablePage() {
		super(PAGE_ID, new String[] { "tipoMezzoTrasporto", "targa", "descrizione", "abilitato", "entita" },
				MezzoTrasporto.class);
		getTable().setAggregatedColumns(new String[] { "tipoMezzoTrasporto" });
	}

	@Override
	public Collection<MezzoTrasporto> loadTableData() {
		return magazzinoAnagraficaBD.caricaMezziTrasporto(null, false, null);
	}

	@Override
	public void onPostPageOpen() {

	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public Collection<MezzoTrasporto> refreshTableData() {
		return loadTableData();
	}

	@Override
	public void setFormObject(Object object) {

	}

	/**
	 * Set magazzinoAnagraficaBD.
	 *
	 * @param magazzinoAnagraficaBD
	 *            il bd da utilizzare per accedere alle operazioni sui mezzi di trasporto
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}
}
