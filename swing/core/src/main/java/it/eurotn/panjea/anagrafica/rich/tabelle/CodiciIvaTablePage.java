package it.eurotn.panjea.anagrafica.rich.tabelle;

import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaTabelleBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.List;

import org.springframework.richclient.command.AbstractCommand;

public class CodiciIvaTablePage extends AbstractTablePageEditor<CodiceIva> {

	private static final String PAGE_ID = "codiciIvaTablePage";

	private IAnagraficaTabelleBD anagraficaTabelleBD;

	/**
	 * Costruttore.
	 */
	protected CodiciIvaTablePage() {
		super(PAGE_ID, new String[] { "codice", "descrizioneInterna" }, CodiceIva.class);
	}

	@Override
	public AbstractCommand[] getCommands() {
		return new AbstractCommand[] {};
	}

	@Override
	public List<CodiceIva> loadTableData() {
		return anagraficaTabelleBD.caricaCodiciIva(null);
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public List<CodiceIva> refreshTableData() {
		return null;
	}

	/**
	 * @param anagraficaTabelleBD
	 *            anagraficaTabelleBD to set
	 */
	public void setAnagraficaTabelleBD(IAnagraficaTabelleBD anagraficaTabelleBD) {
		this.anagraficaTabelleBD = anagraficaTabelleBD;
	}

	@Override
	public void setFormObject(Object object) {
	}

}
