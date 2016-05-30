/**
 * 
 */
package it.eurotn.panjea.anagrafica.rich.tabelle;

import it.eurotn.panjea.anagrafica.domain.ConversioneUnitaMisura;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaTabelleBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.List;

import org.springframework.richclient.command.AbstractCommand;

/**
 * @author Leonardo
 */
public class ConversioniUnitaMisuraTablePage extends AbstractTablePageEditor<ConversioneUnitaMisura> {

	private static final String PAGE_ID = "conversioniUnitaMisuraTablePage";

	private IAnagraficaTabelleBD anagraficaTabelleBD;

	/**
	 * Costruttore.
	 */
	protected ConversioniUnitaMisuraTablePage() {
		super(PAGE_ID, new String[] { "unitaMisuraOrigine", "formula", "unitaMisuraDestinazione" },
				ConversioneUnitaMisura.class);
	}

	@Override
	public AbstractCommand[] getCommands() {
		return new AbstractCommand[] {};
	}

	@Override
	public List<ConversioneUnitaMisura> loadTableData() {
		return anagraficaTabelleBD.caricaConversioniUnitaMisura();
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public List<ConversioneUnitaMisura> refreshTableData() {
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
