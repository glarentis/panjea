/**
 *
 */
package it.eurotn.panjea.magazzino.rich.editors.articolo.articolodeposito;

import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.ArticoloDeposito;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.table.EditFrame;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author leonardo
 */
public class ArticoliDepositoTablePage extends AbstractTablePageEditor<ArticoloDeposito> {

	private static final String PAGE_ID = "articoliDepositoTablePage";
	private Articolo articolo = null;
	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD = null;

	/**
	 * Costruttore di default.
	 */
	protected ArticoliDepositoTablePage() {
		super(PAGE_ID, new String[] { "deposito", "scorta" }, ArticoloDeposito.class);
	}

	@Override
	public Collection<ArticoloDeposito> loadTableData() {
		List<ArticoloDeposito> codici = Collections.emptyList();
		if (articolo != null && articolo.getId() != null) {
			codici = magazzinoAnagraficaBD.caricaArticoliDeposito(articolo.getId());
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
	public Collection<ArticoloDeposito> refreshTableData() {
		return loadTableData();
	}

	@Override
	public void setFormObject(Object object) {
		articolo = (Articolo) object;
		((ArticoloDepositoPage) getEditPages().get(EditFrame.DEFAULT_OBJECT_CLASS_NAME)).setArticolo(articolo);
	}

	/**
	 * @param magazzinoAnagraficaBD
	 *            magazzinoAnagraficaBD
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}

}
