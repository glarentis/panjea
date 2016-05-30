package it.eurotn.panjea.magazzino.rich.editors.rendicontazione.entita;

import it.eurotn.panjea.magazzino.domain.rendicontazione.EntitaTipoEsportazione;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.Collection;

public class EntitaTipiEsportazioneTablePage extends AbstractTablePageEditor<EntitaTipoEsportazione> {

	public static final String PAGE_ID = "entitaTipiEsportazioneTablePage";

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	/**
	 * Costruttore.
	 */
	protected EntitaTipiEsportazioneTablePage() {
		super(PAGE_ID, new String[] { "tipoEsportazione", "entita" }, EntitaTipoEsportazione.class);
		getTable().setAggregatedColumns(new String[] { "tipoEsportazione" });
	}

	@Override
	public Collection<EntitaTipoEsportazione> loadTableData() {
		return magazzinoAnagraficaBD.caricaEntitaTipoEsportazione();
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public Collection<EntitaTipoEsportazione> refreshTableData() {
		return loadTableData();
	}

	@Override
	public void setFormObject(Object object) {
	}

	/**
	 * @param magazzinoAnagraficaBD
	 *            The magazzinoAnagraficaBD to set.
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}

}
