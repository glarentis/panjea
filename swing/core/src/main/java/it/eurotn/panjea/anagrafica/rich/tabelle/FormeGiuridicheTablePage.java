/**
 * 
 */
package it.eurotn.panjea.anagrafica.rich.tabelle;

import it.eurotn.panjea.anagrafica.domain.FormaGiuridica;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaTabelleBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.List;

/**
 * 
 * @author Aracno
 * @version 1.0, 27/nov/06
 * 
 */
public class FormeGiuridicheTablePage extends AbstractTablePageEditor<FormaGiuridica> {
	private static final String PAGE_ID = "formeGiuridicheTablePage";
	private IAnagraficaTabelleBD anagraficaTabelleBD;

	/**
	 * Costruttore.
	 */
	protected FormeGiuridicheTablePage() {
		super(PAGE_ID, new String[] { FormaGiuridica.PROP_SIGLA, FormaGiuridica.PROP_DESCRIZIONE },
				FormaGiuridica.class);
	}

	@Override
	public List<FormaGiuridica> loadTableData() {
		return anagraficaTabelleBD.caricaFormeGiuridiche("sigla", null);
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public List<FormaGiuridica> refreshTableData() {
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
