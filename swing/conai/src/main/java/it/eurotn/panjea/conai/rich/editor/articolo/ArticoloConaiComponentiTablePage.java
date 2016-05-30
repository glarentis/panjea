package it.eurotn.panjea.conai.rich.editor.articolo;

import it.eurotn.panjea.conai.domain.ConaiComponente;
import it.eurotn.panjea.conai.rich.bd.IConaiBD;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.table.EditFrame;

import java.util.Collection;

public class ArticoloConaiComponentiTablePage extends AbstractTablePageEditor<ConaiComponente> {

	public static final String PAGE_ID = "articoloConaiComponentiTablePage";

	private Articolo articolo;

	private IConaiBD conaiBD;

	/**
	 * Costruttore.
	 */
	protected ArticoloConaiComponentiTablePage() {
		super(PAGE_ID, new ArticoliConaiComponenteTableModel());
	}

	/**
	 * @return Returns the conaiBD.
	 */
	public IConaiBD getConaiBD() {
		return conaiBD;
	}

	@Override
	public Collection<ConaiComponente> loadTableData() {
		return conaiBD.caricaComponentiConai(articolo.getArticoloLite());
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public void postSetFormObject(Object object) {
		articolo = (Articolo) object;
		ArticoloConaiComponentePage page = (ArticoloConaiComponentePage) getEditPages().get(
				EditFrame.DEFAULT_OBJECT_CLASS_NAME);
		page.setArticoloCorrente(articolo);
	}

	@Override
	public Collection<ConaiComponente> refreshTableData() {
		return loadTableData();
	}

	/**
	 * @param conaiBD
	 *            The conaiBD to set.
	 */
	public void setConaiBD(IConaiBD conaiBD) {
		this.conaiBD = conaiBD;
	}

	@Override
	public void setFormObject(Object object) {
	}
}