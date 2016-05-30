package it.eurotn.panjea.beniammortizzabili.rich.editors.tabelle;

import it.eurotn.panjea.beniammortizzabili.rich.bd.IBeniAmmortizzabiliBD;
import it.eurotn.panjea.beniammortizzabili2.domain.TipologiaEliminazione;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.Collection;

/**
 * @author Aracno
 * @version 1.0, 22/set/06
 * 
 */
public class TipologieEliminazioneTablePage extends AbstractTablePageEditor<TipologiaEliminazione> {

	private static final String PAGE_ID = "tipologieEliminazioneTablePage";

	private IBeniAmmortizzabiliBD beniAmmortizzabiliBD;

	/**
	 * Costruttore.
	 * 
	 */
	public TipologieEliminazioneTablePage() {
		super(PAGE_ID, new String[] { TipologiaEliminazione.PROP_CODICE, TipologiaEliminazione.PROP_DESCRIZIONE },
				TipologiaEliminazione.class);
	}

	@Override
	public Collection<TipologiaEliminazione> loadTableData() {
		return beniAmmortizzabiliBD.caricaTipologieEliminazione(null);
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
	public Collection<TipologiaEliminazione> refreshTableData() {
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
