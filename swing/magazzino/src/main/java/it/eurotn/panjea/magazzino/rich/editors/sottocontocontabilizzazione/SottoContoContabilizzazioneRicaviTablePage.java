package it.eurotn.panjea.magazzino.rich.editors.sottocontocontabilizzazione;

import it.eurotn.panjea.magazzino.domain.SottoContoContabilizzazione;
import it.eurotn.panjea.magazzino.domain.SottoContoContabilizzazione.ETipoEconomico;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoContabilizzazioneBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.Collection;

/**
 * Permette di incrociare i dati di categorie contabili per deposito,sedeentita e articolo e recuperare il sottoConto
 * per i ricavi (utilizzato quando trasformo l'area di magazzino in contabile).
 * 
 * @author giangi
 * 
 */
public class SottoContoContabilizzazioneRicaviTablePage extends AbstractTablePageEditor<SottoContoContabilizzazione> {

	public static final String PAGE_ID = "sottoContoContabilizzazioneRicaviTablePage";
	private IMagazzinoContabilizzazioneBD magazzinoContabilizzazioneBD;

	/**
	 * Costruttore.
	 */
	protected SottoContoContabilizzazioneRicaviTablePage() {
		super(PAGE_ID, new String[] { "categoriaContabileArticolo", "categoriaContabileDeposito",
				"categoriaContabileSedeMagazzino", "sottoConto", "sottoContoNotaAccredito" },
				SottoContoContabilizzazione.class);
	}

	@Override
	public Collection<SottoContoContabilizzazione> loadTableData() {
		return magazzinoContabilizzazioneBD.caricaSottoContiContabilizzazione(ETipoEconomico.RICAVO);
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public Collection<SottoContoContabilizzazione> refreshTableData() {
		return loadTableData();
	}

	@Override
	public void setFormObject(Object object) {
	}

	/**
	 * @param magazzinoContabilizzazioneBD
	 *            magazzinoContabilizzazioneBD to set
	 */
	public void setMagazzinoContabilizzazioneBD(IMagazzinoContabilizzazioneBD magazzinoContabilizzazioneBD) {
		this.magazzinoContabilizzazioneBD = magazzinoContabilizzazioneBD;
	}

}
