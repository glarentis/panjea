package it.eurotn.panjea.lotti.rich.editors.articolo.situazione;

import it.eurotn.panjea.lotti.util.StatisticaLotto;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.converter.ConverterContext;

public class StatisticheLottiArticoloTableModel extends DefaultBeanTableModel<StatisticaLotto> {

	private static final long serialVersionUID = 6430953361906062770L;

	private static ConverterContext qtaContext;
	private int numeroDecimaliQta;

	static {
		qtaContext = new NumberWithDecimalConverterContext();
		qtaContext.setUserObject(2);
	}

	/**
	 * Costruttore.
	 * 
	 */
	public StatisticheLottiArticoloTableModel() {
		super("statisticheLottiArticoloTableModel", new String[] { "deposito", "statoLotto", "rimanenza", "lotto",
				"lottoInterno", "scadenzaLotto", "prioritaLotto" }, StatisticaLotto.class);
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int column) {

		switch (column) {
		case 2:
			qtaContext.setUserObject(numeroDecimaliQta);
			return qtaContext;
		default:
			return null;
		}
	}

	/**
	 * 
	 * @param numeroDecimali
	 *            numero decimali
	 */
	public void setNumeroDecimaliQta(int numeroDecimali) {
		this.numeroDecimaliQta = numeroDecimali;
	}
}
