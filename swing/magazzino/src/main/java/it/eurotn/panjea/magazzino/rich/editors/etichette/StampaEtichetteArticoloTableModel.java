package it.eurotn.panjea.magazzino.rich.editors.etichette;

import it.eurotn.panjea.magazzino.domain.etichetta.EtichettaArticolo;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.converter.ConverterContext;

public class StampaEtichetteArticoloTableModel extends DefaultBeanTableModel<EtichettaArticolo> {
	private static final long serialVersionUID = 1351341784950864440L;

	private ConverterContext context;

	{
		context = new NumberWithDecimalConverterContext();
	}

	/**
	 * Costruttore.
	 */
	public StampaEtichetteArticoloTableModel() {
		super(StampaEtichetteArticoloTablePage.PAGE_ID, new String[] { "sedeEntita", "articolo", "descrizione",
				"articolo.barCode", "prezzoNetto", "numeroCopiePerStampa", "esitoStampa", "lotto", "dataDocumento",
				"quantita" }, EtichettaArticolo.class);
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int column) {
		switch (column) {
		case 4:
			EtichettaArticolo etichettaArticolo = getElementAt(row);
			context.setUserObject(etichettaArticolo.getNumeroDecimali());
			return context;
		case 9:
			context.setUserObject(2);
			return context;
		default:
			return super.getConverterContextAt(row, column);
		}
	}
}
