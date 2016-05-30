package it.eurotn.panjea.magazzino.rich.editors.sottocontocontabilizzazione;

import it.eurotn.panjea.magazzino.domain.SottoContoContabilizzazione;
import it.eurotn.panjea.magazzino.domain.SottoContoContabilizzazione.ETipoEconomico;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoContabilizzazioneBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.Collection;

public class SottoContoContabilizzazioneCostiTablePage extends AbstractTablePageEditor<SottoContoContabilizzazione> {
	public static final String PAGE_ID = "sottoContoContabilizzazioneCostiTablePage";

	private IMagazzinoContabilizzazioneBD magazzinoContabilizzazioneBD;

	/**
	 * Costruttore.
	 */
	protected SottoContoContabilizzazioneCostiTablePage() {

		super(PAGE_ID, new String[] { "categoriaContabileArticolo", "categoriaContabileDeposito",
				"categoriaContabileSedeMagazzino", "sottoConto", "sottoContoNotaAccredito" },
				SottoContoContabilizzazione.class);
	}

	@Override
	public Collection<SottoContoContabilizzazione> loadTableData() {
		return magazzinoContabilizzazioneBD.caricaSottoContiContabilizzazione(ETipoEconomico.COSTO);
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
	 *            The magazzinoContabilizzazioneBD to set.
	 */
	public void setMagazzinoContabilizzazioneBD(IMagazzinoContabilizzazioneBD magazzinoContabilizzazioneBD) {
		this.magazzinoContabilizzazioneBD = magazzinoContabilizzazioneBD;
	}

}
