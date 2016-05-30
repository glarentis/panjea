package it.eurotn.panjea.bi.rich.editors.analisi.detail;

import it.eurotn.panjea.bi.domain.analisi.sql.detail.RigaDettaglioAnalisiBi;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.converter.ConverterContext;

public class AnalisiBiDetailTableModel extends DefaultBeanTableModel<RigaDettaglioAnalisiBi> {

	private static final long serialVersionUID = 1124813834613364885L;

	private static final ConverterContext NUMBERQTACONTEXT = new NumberWithDecimalConverterContext();
	private static final ConverterContext NUMBERPREZZOCONTEXT = new NumberWithDecimalConverterContext();

	{
		NUMBERPREZZOCONTEXT.setUserObject(6);
		NUMBERQTACONTEXT.setUserObject(6);
	}

	/**
	 * Costruttore.
	 * 
	 */
	public AnalisiBiDetailTableModel() {
		super("analisiBiDetailTableModel", new String[] { "categoriaLite", "articoloLite", "depositoLite",
				"dataRegistrazione", "documento.dataDocumento", "documento.tipoDocumento", "documento.codice",
				"entitaDocumento", "qta", "qtaMagazzino", "prezzoTotale", "prezzoUnitario", "prezzoNetto",
				"variazione", "noteRiga", "sedeEntita", "descrizioneRiga", "tipoOmaggio" },
				RigaDettaglioAnalisiBi.class);
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int column) {
		switch (column) {
		case 8:
		case 9:
			return NUMBERQTACONTEXT;
		case 10:
		case 11:
		case 12:
			return NUMBERPREZZOCONTEXT;
		default:
			return null;
		}
	}
}
