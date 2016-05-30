package it.eurotn.panjea.conai.rich.editor.conaiarticoli;

import it.eurotn.panjea.conai.domain.ConaiArticolo;
import it.eurotn.panjea.conai.rich.bd.IConaiBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.Collection;

public class ConaiArticoliTablePage extends AbstractTablePageEditor<ConaiArticolo> {

	public static final String PAGE_ID = "conaiArticoliTablePage";

	private IConaiBD conaiBD;

	/**
	 * Costruttore.
	 */
	public ConaiArticoliTablePage() {
		super(PAGE_ID, new String[] { "materiale", "articolo" }, ConaiArticolo.class);
	}

	/**
	 * @return Returns the conaiBD.
	 */
	public IConaiBD getConaiBD() {
		return conaiBD;
	}

	@Override
	public Collection<ConaiArticolo> loadTableData() {
		return conaiBD.caricaArticoliConai();
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public Collection<ConaiArticolo> refreshTableData() {
		return null;
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
