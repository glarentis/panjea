package it.eurotn.panjea.partite.rich.tabelle.righestruttura;

import it.eurotn.panjea.partite.domain.RigaStrutturaPartite;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.converter.ConverterContext;

public class RigheStrutturaTableModel extends DefaultBeanTableModel<RigaStrutturaPartite> {

	private static final long serialVersionUID = 8804404700035071419L;
	private static final ConverterContext TOTALE_CONTEXT = new NumberWithDecimalConverterContext();

	{
		TOTALE_CONTEXT.setUserObject(2);
	}

	/**
	 * Costruttore di default.
	 */
	public RigheStrutturaTableModel() {
		super(RigheStrutturaTablePage.PAGE_ID, new String[] { "numeroRata", "primaPercentuale", "secondaPercentuale",
				"intervallo" }, RigaStrutturaPartite.class);
	}

	@Override
	public ConverterContext getConverterContextAt(int i, int j) {
		switch (j) {
		case 1:
		case 2:
			return TOTALE_CONTEXT;
		default:
			return null;
		}
	}

}
