package it.eurotn.panjea.lotti.rich.editors.lottiinscadenza;

import it.eurotn.panjea.lotti.util.StatisticaLotto;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.converter.ConverterContext;

public class LottiInScadenzaTableModel extends DefaultBeanTableModel<StatisticaLotto> {

	private static final long serialVersionUID = -7701936553091891552L;

	private static final ConverterContext DECIMALIPREZZOCONTEXT2 = new NumberWithDecimalConverterContext();

	{
		DECIMALIPREZZOCONTEXT2.setUserObject(2);
	}

	/**
	 * Costruttore.
	 */
	public LottiInScadenzaTableModel() {
		super(RisultatiRicercaLottiInScadenzaTablePage.PAGE_ID, new String[] { "articolo", "lotto", "scadenzaLotto",
				"rimanenza" }, StatisticaLotto.class);
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int column) {

		switch (column) {
		case 3:
			StatisticaLotto statisticaLotto = getElementAt(row);
			DECIMALIPREZZOCONTEXT2.setUserObject(statisticaLotto.getArticolo().getNumeroDecimaliQta());
			return DECIMALIPREZZOCONTEXT2;
		default:
			return null;
		}
	}

}