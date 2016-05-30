package it.eurotn.panjea.magazzino.rich.editors.righemagazzino.importarigheordini;

import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCarico;
import it.eurotn.panjea.ordini.rich.editors.evasione.StatoRigaDistintaCaricoCellRenderer;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.EditorContext;
import com.jidesoft.grid.NavigableTableModel;

public class ImportaRigheOrdiniTableModel extends DefaultBeanTableModel<RigaDistintaCarico> implements
		NavigableTableModel {

	private static final long serialVersionUID = 1063283163610414948L;

	private static final ConverterContext NUMBERQTACONVERSIONCONTEXT;
	private static final ConverterContext DECIMALI_PREZZO_CONVERSIONCONTEXT;

	private EditorContext numeroDecimaliEditorContext = new EditorContext("numeroDecimaliEditorContext", 2);

	private static final EditorContext NUMBERQTAEDITORCONTEXT;
	static {
		NUMBERQTACONVERSIONCONTEXT = new NumberWithDecimalConverterContext();
		NUMBERQTACONVERSIONCONTEXT.setUserObject(6);

		NUMBERQTAEDITORCONTEXT = new EditorContext("numberQtaEditorContext");
		NUMBERQTAEDITORCONTEXT.setUserObject(6);

		DECIMALI_PREZZO_CONVERSIONCONTEXT = new NumberWithDecimalConverterContext();
		DECIMALI_PREZZO_CONVERSIONCONTEXT.setUserObject(4);
	}

	/**
	 * Costruttore.
	 */
	public ImportaRigheOrdiniTableModel() {
		super("carrelloEvasioneOrdiniTableModel", new String[] { "stato", "dataConsegna", "articolo", "qtaOrdinata",
				"qtaDaEvadere", "qtaEvasa", "qtaResidua", "forzata", "numeroDocumento", "sedeEntita", "codiceValuta",
				"prezzoUnitario", "prezzoNetto", "prezzoTotale", "variazione1", "variazione2", "variazione3",
				"variazione4", "omaggio", "dataRegistrazione", "descrizioneTestata" }, RigaDistintaCarico.class);
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int column) {
		switch (column) {
		case 3:
		case 4:
		case 5:
		case 6:
			return NUMBERQTACONVERSIONCONTEXT;
		case 11:
		case 12:
			DECIMALI_PREZZO_CONVERSIONCONTEXT.setUserObject(getElementAt(row).getNumeroDecimaliPrezzo());
			// ctx.setUserObject(getElementAt(row).getNumeroDecimaliPrezzo());
			return DECIMALI_PREZZO_CONVERSIONCONTEXT;
		case 13:
		case 14:
		case 15:
		case 16:
		case 17:
			DECIMALI_PREZZO_CONVERSIONCONTEXT.setUserObject(2);
			return DECIMALI_PREZZO_CONVERSIONCONTEXT;
		default:
			return null;
		}
	}

	@Override
	public EditorContext getEditorContextAt(int row, int column) {
		if (row == -1) {
			return StatoRigaDistintaCaricoCellRenderer.STATO_RIGA_DISTINTA_CARICO_CONTEXT;
		}
		switch (column) {
		case 11:
			numeroDecimaliEditorContext.setUserObject(getElementAt(row).getNumeroDecimaliPrezzo());
			return numeroDecimaliEditorContext;
		case 14:
		case 15:
		case 16:
		case 17:
			numeroDecimaliEditorContext.setUserObject(2);
			return numeroDecimaliEditorContext;
		default:
			return super.getEditorContextAt(row, column);
		}
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		switch (column) {
		case 4:
			return true;
		case 11:
		case 14:
		case 15:
		case 16:
		case 17:
			return !getElementAt(row).isOmaggio();
		default:
			return false;
		}
	}

	@Override
	public boolean isNavigableAt(int row, int column) {
		return isCellEditable(row, column);
	}

	@Override
	public boolean isNavigationOn() {
		return true;
	}

	@Override
	public void setValueAt(Object arg0, int row, int column) {
		super.setValueAt(arg0, row, column);
		fireTableRowsUpdated(row, row);
	}

}
