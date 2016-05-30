package it.eurotn.panjea.ordini.rich.editors.righeordine;

import it.eurotn.panjea.ordini.util.RigaArticoloDTO;
import it.eurotn.panjea.ordini.util.RigaNotaDTO;
import it.eurotn.panjea.ordini.util.RigaOrdineDTO;
import it.eurotn.panjea.ordini.util.RigaTestataDTO;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.CellSpan;
import com.jidesoft.grid.EditorContext;
import com.jidesoft.grid.SpanTableModel;

public class RigheOrdineTableModel extends DefaultBeanTableModel<RigaOrdineDTO> implements SpanTableModel {

	private static final long serialVersionUID = 653417679653805632L;
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
	public RigheOrdineTableModel() {
		super(RigheOrdineTablePage.PAGE_ID, new String[] { "articolo.codice", "codiceEntita", "descrizione",
				"prezzoUnitario", "qta", "qtaChiusa", "prezzoNetto", "variazione", "prezzoTotale", "dataConsegna" },
				RigaOrdineDTO.class, RigaArticoloDTO.class);
	}

	@Override
	public CellSpan getCellSpanAt(int row, int column) {
		RigaOrdineDTO rigaOrdineDTO = getObject(row);

		if (rigaOrdineDTO instanceof RigaArticoloDTO) {
			return null;
		} else {
			if (column == 0) {
				cellSpanNote.setRow(row);
				return cellSpanNote;
			} else {
				return null;
			}
		}
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
			return LivelloRigaOrdineCellRenderer.LIVELLO_RIGA_ORDINE_CONTEXT;
		default:
			return super.getEditorContextAt(row, column);
		}
	}

	@Override
	public Object getValueAt(int row, int column) {
		RigaOrdineDTO rigaOrdineDTO = getObject(row);
		if (rigaOrdineDTO instanceof RigaArticoloDTO) {
			return super.getValueAt(row, column);
		} else {
			if (column == 0) {
				if (rigaOrdineDTO instanceof RigaNotaDTO) {
					return ((RigaNotaDTO) rigaOrdineDTO).getNota();
				} else if (rigaOrdineDTO instanceof it.eurotn.panjea.ordini.util.RigaTestataDTO) {
					return ((RigaTestataDTO) rigaOrdineDTO).getDescrizione();
				} else {
					return null;
				}
			} else {
				return null;
			}
		}
	}

	@Override
	public boolean isCellSpanOn() {
		return true;
	}
}
