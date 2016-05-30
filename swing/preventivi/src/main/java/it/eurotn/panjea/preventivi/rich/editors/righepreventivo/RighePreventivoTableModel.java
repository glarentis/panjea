package it.eurotn.panjea.preventivi.rich.editors.righepreventivo;

import it.eurotn.panjea.preventivi.util.RigaArticoloDTO;
import it.eurotn.panjea.preventivi.util.RigaNotaDTO;
import it.eurotn.panjea.preventivi.util.RigaPreventivoDTO;
import it.eurotn.panjea.preventivi.util.RigaTestataDTO;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.CellSpan;
import com.jidesoft.grid.EditorContext;
import com.jidesoft.grid.SpanTableModel;

public class RighePreventivoTableModel extends DefaultBeanTableModel<RigaPreventivoDTO> implements SpanTableModel {
	private static final long serialVersionUID = 1L;
	private static ConverterContext numberPrezzoContext;
	private static ConverterContext numberQtaContext;

	private static CellSpan cellSpanNote = null;

	static {
		numberPrezzoContext = new NumberWithDecimalConverterContext();
		numberPrezzoContext.setUserObject(new Integer(5));

		numberQtaContext = new NumberWithDecimalConverterContext();
		numberQtaContext.setUserObject(new Integer(3));
		cellSpanNote = new CellSpan(0, 0, 1, 10);
	}

	/**
	 * Costruttore.
	 */
	public RighePreventivoTableModel() {
		super(RighePreventivoTablePage.PAGE_ID, new String[] { "articolo.codice", "codiceEntita", "descrizione",
				"prezzoUnitario", "qta", "qtaChiusa", "prezzoNetto", "variazione", "prezzoTotale" },
				RigaPreventivoDTO.class, RigaArticoloDTO.class);
	}

	@Override
	public CellSpan getCellSpanAt(int row, int column) {
		if (column != 0) {
			return null;
		}

		if (getObject(row) instanceof RigaArticoloDTO) {
			return null;
		}

		cellSpanNote.setRow(row);
		return cellSpanNote;
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int column) {

		switch (column) {
		case 3:
		case 6:
		case 8:
			return numberPrezzoContext;
		case 4:
		case 5:
			return numberQtaContext;
		default:
			return null;
		}
	}

	@Override
	public EditorContext getEditorContextAt(int row, int column) {
		switch (column) {
		case 0:
			return LivelloRigaPreventivoCellRenderer.LIVELLO_RIGA_PREVENTIVO_CONTEXT;
		default:
			return super.getEditorContextAt(row, column);
		}
	}

	@Override
	public Object getValueAt(int row, int column) {
		RigaPreventivoDTO rigaPreventivoDTO = getObject(row);

		if (rigaPreventivoDTO instanceof RigaArticoloDTO) {
			return super.getValueAt(row, column);
		}

		if (column == 0) {
			if (rigaPreventivoDTO instanceof RigaNotaDTO) {
				return ((RigaNotaDTO) rigaPreventivoDTO).getNota();
			} else if (rigaPreventivoDTO instanceof RigaTestataDTO) {
				return ((RigaTestataDTO) rigaPreventivoDTO).getDescrizione();
			}
		}

		return null;
	}

	@Override
	public boolean isCellSpanOn() {
		return true;
	}
}
