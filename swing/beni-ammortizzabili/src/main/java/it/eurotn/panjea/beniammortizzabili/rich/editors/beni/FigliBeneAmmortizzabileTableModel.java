package it.eurotn.panjea.beniammortizzabili.rich.editors.beni;

import com.jidesoft.converter.ConverterContext;

import it.eurotn.panjea.beniammortizzabili2.domain.BeneAmmortizzabile;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

public class FigliBeneAmmortizzabileTableModel extends DefaultBeanTableModel<BeneAmmortizzabile> {

	private static final long serialVersionUID = -3687331091773839831L;

	private static ConverterContext numberPrezzoContext;

	static {
		numberPrezzoContext = new NumberWithDecimalConverterContext();
		numberPrezzoContext.setUserObject(new Integer(2));
	}

	/**
	 * Costruttore.
	 */
	public FigliBeneAmmortizzabileTableModel() {
		super("FigliBeneAmmortizzabileTableModel",
				new String[] { "codice", "descrizione",
						BeneAmmortizzabile.PROP_IMPORTO_SOGGETTO_AD_AMMORTAMENTO_SINGOLO,
						BeneAmmortizzabile.PROP_IND_AMMORTAMENTO },
				BeneAmmortizzabile.class);
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int column) {
		switch (column) {
		case 2:
			return numberPrezzoContext;
		default:
			return null;
		}
	}

}
