package it.eurotn.panjea.magazzino.rich.editors.listinotipomezzozonageografica;

import it.eurotn.panjea.magazzino.domain.moduloprezzo.ListinoTipoMezzoZonaGeografica;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.Collection;

public class ListinoTipoMezzoZonaGeograficaTablePage extends AbstractTablePageEditor<ListinoTipoMezzoZonaGeografica> {

	public static final String PAGE_ID = "listinoTipoMezzoZonaGeograficaTablePage";

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD = null;

	/**
	 * Costruttore di default.
	 */
	public ListinoTipoMezzoZonaGeograficaTablePage() {
		super(PAGE_ID, new ListinoTipoMezzoZonaGeograficaTableModel(PAGE_ID));
	}

	@Override
	public Collection<ListinoTipoMezzoZonaGeografica> loadTableData() {
		return magazzinoAnagraficaBD.caricaListiniTipoMezzoZonaGeografica();
	}

	@Override
	public void onPostPageOpen() {

	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public Collection<ListinoTipoMezzoZonaGeografica> refreshTableData() {
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
