package it.eurotn.panjea.ordini.rich.editors.evasione;

import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCarico;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.EditorContext;

public class RisultatiRicercaEvasioneTableModel extends DefaultBeanTableModel<RigaDistintaCarico> {

	private static final long serialVersionUID = 8571839703713229582L;

	private static final ConverterContext NUMBERQTACONTEXT = new NumberWithDecimalConverterContext();

	{
		NUMBERQTACONTEXT.setUserObject(6);
	}

	/**
	 * Costruttore.
	 */
	public RisultatiRicercaEvasioneTableModel() {
		super("risultatiRicercaEvasioneTableModel", new String[] { "stato", "dataRegistrazione", "numeroDocumento",
				"entita", "sedeEntita", "dataConsegna", "deposito", "articolo", "qtaOrdinata", "qtaEvasa",
				"sedeEntita.sede.datiGeografici.livelloAmministrativo1.nome",
				"sedeEntita.sede.datiGeografici.livelloAmministrativo2.nome",
				"sedeEntita.sede.datiGeografici.livelloAmministrativo3.nome", "trasportoCura" },
				RigaDistintaCarico.class);
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int column) {
		switch (column) {
		case 8:
		case 9:
			return NUMBERQTACONTEXT;
		default:
			return null;
		}
	}

	@Override
	public EditorContext getEditorContextAt(int row, int column) {
		if (row == -1) {
			return StatoRigaDistintaCaricoCellRenderer.STATO_RIGA_DISTINTA_CARICO_CONTEXT;
		}
		return super.getEditorContextAt(row, column);
	}
}
