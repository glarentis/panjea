package it.eurotn.panjea.anagrafica.rich.editors.tabelle.noteanagrafica;

import it.eurotn.panjea.anagrafica.domain.NotaAnagrafica;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaTabelleBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.Collection;

import com.jidesoft.grid.JideTable;

public class NoteAnagraficaTablePage extends AbstractTablePageEditor<NotaAnagrafica> {

	public static final String PAGE_ID = "noteAnagraficaTablePage";

	private IAnagraficaTabelleBD anagraficaTabelleBD;

	/**
	 * 
	 * Costruttore.
	 * 
	 */
	protected NoteAnagraficaTablePage() {
		super(PAGE_ID, new String[] { "codice", "descrizione" }, NotaAnagrafica.class);
		((JideTable) getTable().getTable()).setRowAutoResizes(true);
	}

	@Override
	public Collection<NotaAnagrafica> loadTableData() {
		return anagraficaTabelleBD.caricaNoteAnagrafica();
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public Collection<NotaAnagrafica> refreshTableData() {
		return loadTableData();
	}

	/**
	 * @param anagraficaTabelleBD
	 *            the anagraficaTabelleBD to set
	 */
	public void setAnagraficaTabelleBD(IAnagraficaTabelleBD anagraficaTabelleBD) {
		this.anagraficaTabelleBD = anagraficaTabelleBD;
	}

	@Override
	public void setFormObject(Object object) {

	}

}
