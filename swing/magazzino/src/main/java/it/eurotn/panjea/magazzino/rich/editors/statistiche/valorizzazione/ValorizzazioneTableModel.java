package it.eurotn.panjea.magazzino.rich.editors.statistiche.valorizzazione;

import it.eurotn.panjea.magazzino.util.ValorizzazioneArticolo;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.converter.ConverterContext;

public class ValorizzazioneTableModel extends DefaultBeanTableModel<ValorizzazioneArticolo> {

	private static final long serialVersionUID = -7701936553091891552L;

	private static final ConverterContext DECIMALIPREZZOCONTEXT4 = new NumberWithDecimalConverterContext();
	private static final ConverterContext DECIMALIPREZZOCONTEXT2 = new NumberWithDecimalConverterContext();
	private static final ConverterContext DECIMALIPREZZOCONTEXT6 = new NumberWithDecimalConverterContext();

	{
		DECIMALIPREZZOCONTEXT2.setUserObject(2);
		DECIMALIPREZZOCONTEXT4.setUserObject(4);
		DECIMALIPREZZOCONTEXT6.setUserObject(6);
	}

	/**
	 * Costruttore.
	 */
	public ValorizzazioneTableModel() {
		super(RisultatiRicercaValorizzazioneMagazzinoTablePage.PAGE_ID, new String[] { "articolo", "categoria",
				"deposito", "giacenza", "costo", "valore", "qtaInventario", "qtaMagazzinoCarico",
				"qtaMagazzinoScarico", "qtaMagazzinoCaricoAltro", "qtaMagazzinoScaricoAltro", "scorta", "sottoScorta",
				"dataInventario" }, ValorizzazioneArticolo.class);
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int column) {

		switch (column) {
		case 3:
		case 4:
			return DECIMALIPREZZOCONTEXT4;
		case 5:
			return DECIMALIPREZZOCONTEXT2;
		case 6:
		case 7:
		case 8:
		case 9:
		case 10:
			return DECIMALIPREZZOCONTEXT6;
		default:
			return null;
		}
	}

}