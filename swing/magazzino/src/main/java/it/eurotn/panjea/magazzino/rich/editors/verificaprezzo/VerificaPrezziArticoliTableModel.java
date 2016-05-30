package it.eurotn.panjea.magazzino.rich.editors.verificaprezzo;

import it.eurotn.panjea.magazzino.domain.RigaArticoloLite;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.converter.ConverterContext;

public class VerificaPrezziArticoliTableModel extends DefaultBeanTableModel<RigaArticoloLite> {

	private static final long serialVersionUID = 4191968753484432018L;
	private final ConverterContext numberContext = new NumberWithDecimalConverterContext();

	/**
	 * Costruttore.
	 */
	public VerificaPrezziArticoliTableModel() {
		super(VerificaPrezziArticoliPage.PAGE_ID, new String[] { "articolo", "prezzoDeterminato", "qta" },
				RigaArticoloLite.class);
		numberContext.setUserObject(6);
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int column) {
		switch (column) {
		case 1:
			return numberContext;
		default:
			return null;
		}
	}

}
