package it.eurotn.panjea.tesoreria.solleciti.editors;

import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.panjea.tesoreria.solleciti.RigaSollecito;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.converter.ConverterContext;

public class RicercaSollecitiTableModel extends DefaultBeanTableModel<RigaSollecito> {

	private static final long serialVersionUID = 179146597821513709L;

	private static final ConverterContext IMPORTO_CONTEXT = new NumberWithDecimalConverterContext();

	{
		IMPORTO_CONTEXT.setUserObject(2);
	}

	/**
	 * Costruttore.
	 */
	public RicercaSollecitiTableModel() {
		super("ricercaSollecitiTableModel", new String[] { "sollecito", "rata.areaRate.documento.entita",
				"rata.statoRata", "dataScadenza", "rata.areaRate.documento", "residuo.importoInValutaAzienda",
				"importo.importoInValutaAzienda", "sollecito.nota" }, RigaSollecito.class);
	}

	@Override
	public ConverterContext getConverterContextAt(int i, int j) {
		switch (j) {
		case 5:
		case 6:
			return IMPORTO_CONTEXT;
		default:
			return null;
		}
	}
}
