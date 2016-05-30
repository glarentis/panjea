package it.eurotn.panjea.conai.rich.editor.analisi;

import it.eurotn.panjea.conai.util.AnalisiConaiDTO;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.converter.ConverterContext;

public class RisultatiRicercaAnalisiConaiTableModel extends DefaultBeanTableModel<AnalisiConaiDTO> {

	private static final long serialVersionUID = -40147289258126131L;

	private static ConverterContext qtaContext;

	static {
		qtaContext = new NumberWithDecimalConverterContext();
	}

	/**
	 * Costruttore.
	 */
	public RisultatiRicercaAnalisiConaiTableModel() {
		super("risultatiRicercaAnalisiConaiTableModel", new String[] { "documento", "documento.entita", "articolo",
				"materiale", "tipoImballo", "pesoTotaleConai", "pesoEsenzioneConai", "percentualeEsenzioneConai",
				"prezzoNettoConai", "prezzoTotaleConai", "pesoUnitarioConai" }, AnalisiConaiDTO.class);
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int column) {
		switch (column) {
		case 5:
		case 6:
		case 8:
		case 9:
		case 10:
			qtaContext.setUserObject(6);
			return qtaContext;
		case 7:
			qtaContext.setUserObject(2);
			return qtaContext;
		default:
			return super.getConverterContextAt(row, column);
		}
	}
}
