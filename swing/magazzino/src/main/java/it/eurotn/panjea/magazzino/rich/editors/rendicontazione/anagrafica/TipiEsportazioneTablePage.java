package it.eurotn.panjea.magazzino.rich.editors.rendicontazione.anagrafica;

import it.eurotn.panjea.magazzino.domain.rendicontazione.TipoEsportazione;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.Collection;

public class TipiEsportazioneTablePage extends AbstractTablePageEditor<TipoEsportazione> {

	public static final String PAGE_ID = "tipiEsportazioneTablePage";

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	/**
	 * Costruttore.
	 * 
	 */
	protected TipiEsportazioneTablePage() {
		super(PAGE_ID, new String[] { "nome", "datiSpedizione.nomeFile", "tipoSpedizione" }, TipoEsportazione.class);
	}

	@Override
	public Collection<TipoEsportazione> loadTableData() {
		return magazzinoAnagraficaBD.caricaTipiEsportazione("%");
	}

	@Override
	public void onPostPageOpen() {

	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public Collection<TipoEsportazione> refreshTableData() {
		return loadTableData();
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
