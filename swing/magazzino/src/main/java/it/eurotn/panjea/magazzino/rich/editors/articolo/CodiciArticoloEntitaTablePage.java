package it.eurotn.panjea.magazzino.rich.editors.articolo;

import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.CodiceArticoloEntita;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.table.EditFrame;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Table page per la gestione dei codici articolo entita'.
 * 
 * @author Leonardo
 */
public class CodiciArticoloEntitaTablePage extends AbstractTablePageEditor<CodiceArticoloEntita> {

	private static final String PAGE_ID = "codiciArticoloEntitaTablePage";
	private Articolo articolo;
	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD = null;

	/**
	 * Costruttore di default.
	 */
	protected CodiciArticoloEntitaTablePage() {
		super(PAGE_ID, new String[] { "codice", "barCode", "barCode2", "descrizione", "entita", "consegnaContoTerzi",
				"entitaPrincipale" }, CodiceArticoloEntita.class);
	}

	@Override
	public Collection<CodiceArticoloEntita> loadTableData() {
		List<CodiceArticoloEntita> codici = Collections.emptyList();

		if (articolo != null && articolo.getId() != null) {
			codici = magazzinoAnagraficaBD.caricaCodiciArticoloEntita(articolo.getId());
		}

		return codici;
	}

	@Override
	public void onPostPageOpen() {

	}

	@Override
	public boolean onPrePageOpen() {
		return articolo.getId() != null;
	}

	@Override
	public Collection<CodiceArticoloEntita> refreshTableData() {
		return loadTableData();
	}

	@Override
	public void setFormObject(Object object) {
		articolo = (Articolo) object;
		((CodiceArticoloEntitaPage) getEditPages().get(EditFrame.DEFAULT_OBJECT_CLASS_NAME)).setArticolo(articolo);
	}

	/**
	 * @param magazzinoAnagraficaBD
	 *            magazzinoAnagraficaBD
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}

}
