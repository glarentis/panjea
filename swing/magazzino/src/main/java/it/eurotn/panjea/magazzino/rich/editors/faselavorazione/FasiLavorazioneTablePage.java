package it.eurotn.panjea.magazzino.rich.editors.faselavorazione;

import it.eurotn.panjea.anagrafica.domain.FaseLavorazione;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.Collection;

public class FasiLavorazioneTablePage extends AbstractTablePageEditor<FaseLavorazione> {

	private static final String PAGE_ID = "fasiLavorazioneTablePage";

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	/**
	 * Costruttore.
	 */
	protected FasiLavorazioneTablePage() {
		super(PAGE_ID, new String[] { "codice", "descrizione", "ordinamento" }, FaseLavorazione.class);
		getTable().setDelayForSelection(500);
	}

	@Override
	public Collection<FaseLavorazione> loadTableData() {
		return magazzinoAnagraficaBD.caricaFasiLavorazione(null);
	}

	@Override
	public void onPostPageOpen() {

	}

	@Override
	public Collection<FaseLavorazione> refreshTableData() {
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
