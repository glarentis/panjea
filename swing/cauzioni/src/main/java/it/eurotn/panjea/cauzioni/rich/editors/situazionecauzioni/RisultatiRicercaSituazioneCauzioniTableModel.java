package it.eurotn.panjea.cauzioni.rich.editors.situazionecauzioni;

import it.eurotn.panjea.cauzioni.util.parametriricerca.SituazioneCauzioniDTO;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.converter.ConverterContext;

public class RisultatiRicercaSituazioneCauzioniTableModel extends DefaultBeanTableModel<SituazioneCauzioniDTO> {

	private static final long serialVersionUID = 1262169057174815714L;

	private static final ConverterContext TOTALE_CONTEXT = new NumberWithDecimalConverterContext();

	/**
	 * 
	 * Costruttore.
	 * 
	 */
	public RisultatiRicercaSituazioneCauzioniTableModel() {
		super("risultatiRicercaSituazioneCauzioniTableModel", new String[] { "entitaDocumento", "sedeEntita",
				"categoria", "articolo", "dati", "resi", "saldo", "importoDati", "importoResi", "saldoImporto" },
				SituazioneCauzioniDTO.class);
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int j) {

		if (row == -1) {
			return null;
		}

		SituazioneCauzioniDTO situazioneCauzioniDTO = getElementAt(row);

		switch (j) {
		case 4:
		case 5:
		case 6:
			TOTALE_CONTEXT.setUserObject(situazioneCauzioniDTO.getNumeroDecimaliQta());
			return TOTALE_CONTEXT;
		case 7:
		case 8:
		case 9:
			TOTALE_CONTEXT.setUserObject(6);
			return TOTALE_CONTEXT;
		default:
			return null;
		}
	}

}
