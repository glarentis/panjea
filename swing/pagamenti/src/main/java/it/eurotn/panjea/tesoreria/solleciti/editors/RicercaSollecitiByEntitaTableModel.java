package it.eurotn.panjea.tesoreria.solleciti.editors;

import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.panjea.tesoreria.solleciti.RigaSollecito;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.converter.ConverterContext;

public class RicercaSollecitiByEntitaTableModel extends DefaultBeanTableModel<RigaSollecito> {

	private static final long serialVersionUID = 179146597821513709L;

	private static final ConverterContext IMPORTO_CONTEXT = new NumberWithDecimalConverterContext();

	{
		IMPORTO_CONTEXT.setUserObject(2);
	}

	/**
	 * Costruttore.
	 * 
	 */
	public RicercaSollecitiByEntitaTableModel() {
		super("ricercaSollecitiByEntitaTableModel", new String[] { "sollecito", "rata.statoRata", "dataScadenza",
				"rata.areaRate.documento", "residuo.importoInValutaAzienda", "importo.importoInValutaAzienda",
				"sollecito.nota", "sollecito.email", "sollecito.stampa", "sollecito.telefono", "sollecito.fax" },
				RigaSollecito.class);
	}

	@Override
	public ConverterContext getConverterContextAt(int i, int j) {
		switch (j) {
		case 4:
		case 5:
			return IMPORTO_CONTEXT;
		default:
			return null;
		}
	}

}
