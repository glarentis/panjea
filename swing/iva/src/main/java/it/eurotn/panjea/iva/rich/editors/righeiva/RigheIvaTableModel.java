package it.eurotn.panjea.iva.rich.editors.righeiva;

import it.eurotn.panjea.iva.domain.RigaIva;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.EditorContext;

public class RigheIvaTableModel extends DefaultBeanTableModel<RigaIva> {
	private static final long serialVersionUID = -3456701473905235161L;

	private static ConverterContext numberPrezzoContext;

	static {
		numberPrezzoContext = new NumberWithDecimalConverterContext();
		numberPrezzoContext.setUserObject(new Integer(2));
	}

	/**
	 *
	 * @param id
	 *            id tablemodel
	 */
	public RigheIvaTableModel(final String id) {
		super(id, new String[] { "imponibileVisualizzato", "codiceIva", "impostaVisualizzata" }, RigaIva.class);
	}

	@Override
	public ConverterContext getConverterContextAt(int i, int j) {
		switch (j) {
		case 0:
		case 2:
			return numberPrezzoContext;
		default:
			break;
		}
		return super.getConverterContextAt(i, j);
	}

	@Override
	public EditorContext getEditorContextAt(int i, int j) {
		switch (j) {
		case 1:
			return CodiceIvaCollegatoTableCellRenderer.CODICE_IVA_COLLEGATO_CELL_RENDERER_CONTEXT;
		default:
			break;
		}
		return super.getEditorContextAt(i, j);
	}
}
