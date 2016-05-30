package it.eurotn.panjea.magazzino.rich.editors.statistiche.indicerotazione;

import it.eurotn.panjea.magazzino.util.IndiceGiacenzaArticolo;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.converter.ConverterContext;

public class RisultatiIndiceRotazioneTableModel extends
DefaultBeanTableModel<IndiceGiacenzaArticolo> {
	private static final ConverterContext DECIMALIPREZZOCONTEXT = new NumberWithDecimalConverterContext();
	private static final long serialVersionUID = -8031280019928604482L;

	/**
	 * costruttore
	 */
	public RisultatiIndiceRotazioneTableModel() {
		super("risultatiIndiceRotazioneTableModel", new String[] { "articolo",
				"deposito", "rotazione", "giacenzaMedia" },
				IndiceGiacenzaArticolo.class);
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int col) {
		switch (col) {
		case 2:
		case 3:
			DECIMALIPREZZOCONTEXT.setUserObject(getElementAt(row).getArticolo()
					.getNumeroDecimaliQta());
			return new NumberWithDecimalConverterContext(getElementAt(row)
					.getArticolo().getNumeroDecimaliQta());

		default:
			break;
		}
		return super.getConverterContextAt(row, col);
	}

}
