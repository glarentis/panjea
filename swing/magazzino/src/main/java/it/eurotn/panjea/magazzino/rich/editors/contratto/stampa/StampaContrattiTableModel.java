package it.eurotn.panjea.magazzino.rich.editors.contratto.stampa;

import it.eurotn.panjea.magazzino.util.ContrattoStampaDTO;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.converter.ConverterContext;

public class StampaContrattiTableModel extends DefaultBeanTableModel<ContrattoStampaDTO> {

	private static final long serialVersionUID = -6971923108008422202L;

	private final ConverterContext totaleContext = new NumberWithDecimalConverterContext();

	private static final ConverterContext SCONTO_CONTEXT = new NumberWithDecimalConverterContext();

	{
		SCONTO_CONTEXT.setUserObject(2);
	}

	/**
	 * Costruttore.
	 */
	public StampaContrattiTableModel() {
		super("stampaContrattiTableModel",
				new String[] { "ordinamento", "contratto", "contratto.dataInizio", "contratto.dataFine",
						"contrattoAttivo", "categoriaSedeMagazzino", "entita", "sedeEntita",
						"categoriaCommercialeContratto", "articolo", "azionePrezzo", "quantitaSogliaPrezzo",
						"valorePrezzo", "tipoValorePrezzo", "azioneSconto", "quantitaSogliaSconto", "sconto1",
						"sconto2", "sconto3", "sconto4" }, ContrattoStampaDTO.class);
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int j) {
		ContrattoStampaDTO contrattoStampaDTO = getElementAt(row);

		switch (j) {
		case 12:
			totaleContext.setUserObject(contrattoStampaDTO.getNumeroDecimaliPrezzo());
			return totaleContext;
		case 16:
		case 17:
		case 18:
		case 19:
			return SCONTO_CONTEXT;
		default:
			return null;
		}
	}
}
