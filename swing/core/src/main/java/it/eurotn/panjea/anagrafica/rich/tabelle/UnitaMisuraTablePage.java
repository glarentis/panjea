/**
 *
 */
package it.eurotn.panjea.anagrafica.rich.tabelle;

import it.eurotn.panjea.anagrafica.domain.UnitaMisura;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaTabelleBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.List;

import org.springframework.richclient.command.AbstractCommand;

/**
 * @author fattazzo
 */
public class UnitaMisuraTablePage extends AbstractTablePageEditor<UnitaMisura> {

	private static final String PAGE_ID = "unitaMisuraTablePage";

	private IAnagraficaTabelleBD anagraficaTabelleBD;

	/**
	 * Costruttore.
	 */
	protected UnitaMisuraTablePage() {
		super(PAGE_ID, new String[] { "codice", "descrizione" }, UnitaMisura.class);
	}

	@Override
	public AbstractCommand[] getCommands() {
		return new AbstractCommand[] {};
	}

	@Override
	public List<UnitaMisura> loadTableData() {
		return anagraficaTabelleBD.caricaUnitaMisura();
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public List<UnitaMisura> refreshTableData() {
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
