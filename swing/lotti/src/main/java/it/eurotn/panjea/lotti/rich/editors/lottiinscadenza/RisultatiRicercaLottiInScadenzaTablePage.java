package it.eurotn.panjea.lotti.rich.editors.lottiinscadenza;

import it.eurotn.panjea.lotti.rich.bd.ILottiBD;
import it.eurotn.panjea.lotti.util.ParametriRicercaScadenzaLotti;
import it.eurotn.panjea.lotti.util.StatisticaLotto;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.List;

public class RisultatiRicercaLottiInScadenzaTablePage extends AbstractTablePageEditor<StatisticaLotto> {

	public static final String PAGE_ID = "risultatiRicercaLottiInScadenzaTablePage";

	private ParametriRicercaScadenzaLotti parametriRicercaScadenzaLotti;

	private ILottiBD lottiBD;

	/**
	 * Costruttore.
	 */
	protected RisultatiRicercaLottiInScadenzaTablePage() {
		super(PAGE_ID, new LottiInScadenzaTableModel());
	}

	@Override
	public List<StatisticaLotto> loadTableData() {
		List<StatisticaLotto> righe = null;
		if (parametriRicercaScadenzaLotti.isEffettuaRicerca()) {
			righe = lottiBD.caricaLottiInScadenza(parametriRicercaScadenzaLotti);
		}
		return righe;
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public List<StatisticaLotto> refreshTableData() {
		return loadTableData();
	}

	@Override
	public void setFormObject(Object object) {
		this.parametriRicercaScadenzaLotti = (ParametriRicercaScadenzaLotti) object;
	}

	/**
	 * @param lottiBD
	 *            the lottiBD to set
	 */
	public void setLottiBD(ILottiBD lottiBD) {
		this.lottiBD = lottiBD;
	}

}
