/**
 * 
 */
package it.eurotn.panjea.beniammortizzabili.rich.editors.tabelle;

import it.eurotn.panjea.beniammortizzabili.rich.bd.IBeniAmmortizzabiliBD;
import it.eurotn.panjea.beniammortizzabili2.domain.Ubicazione;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.Collection;

/**
 * @author Aracno
 * @version 1.0, 22/set/06
 * 
 */
public class UbicazioniTablePage extends AbstractTablePageEditor<Ubicazione> {

	private static final String PAGE_ID = "ubicazioniTablePage";

	private IBeniAmmortizzabiliBD beniAmmortizzabiliBD;

	/**
	 * Costruttore.
	 * 
	 */
	public UbicazioniTablePage() {
		super(PAGE_ID, new String[] { Ubicazione.PROP_CODICE, Ubicazione.PROP_DESCRIZIONE }, Ubicazione.class);
	}

	@Override
	public Collection<Ubicazione> loadTableData() {
		return beniAmmortizzabiliBD.caricaUBicazioni(null);
	}

	@Override
	public void onPostPageOpen() {

	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public void onRefresh() {
		loadData();
	}

	@Override
	public Collection<Ubicazione> refreshTableData() {
		return null;
	}

	/**
	 * @param beniAmmortizzabiliBD
	 *            the beniAmmortizzabiliBD to set
	 */
	public void setBeniAmmortizzabiliBD(IBeniAmmortizzabiliBD beniAmmortizzabiliBD) {
		this.beniAmmortizzabiliBD = beniAmmortizzabiliBD;
	}

	@Override
	public void setFormObject(Object object) {
	}

}
