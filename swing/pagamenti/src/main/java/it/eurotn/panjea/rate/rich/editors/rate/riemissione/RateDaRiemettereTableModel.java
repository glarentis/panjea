package it.eurotn.panjea.rate.rich.editors.rate.riemissione;

import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.panjea.tesoreria.util.RataRiemessa;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.converter.ConverterContext;

public class RateDaRiemettereTableModel extends DefaultBeanTableModel<RataRiemessa> {

	private static ConverterContext numberImportoContext;

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -7682345211547791631L;

	static {
		numberImportoContext = new NumberWithDecimalConverterContext();
		numberImportoContext.setUserObject(new Integer(2));
	}

	/**
	 * Costruttore.
	 */
	public RateDaRiemettereTableModel() {
		super("rateDaRiemettereTableModel", new String[] { "areaRate.documento", "areaRate.documento.entita",
				"numeroRata", "importo.importoInValuta", "tipoPagamento", "importoRateRiemesse" }, RataRiemessa.class);
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int column) {
		switch (column) {
		case 3:
		case 5:
			return numberImportoContext;
		default:
			return super.getConverterContextAt(row, column);
		}
	}

}
