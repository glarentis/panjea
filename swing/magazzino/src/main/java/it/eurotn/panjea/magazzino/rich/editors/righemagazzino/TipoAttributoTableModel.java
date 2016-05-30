package it.eurotn.panjea.magazzino.rich.editors.righemagazzino;

import it.eurotn.panjea.magazzino.domain.TipoAttributo;
import it.eurotn.panjea.magazzino.domain.TipoVariante;
import it.eurotn.panjea.magazzino.rich.editors.TipiAttributoTablePage;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.grid.CellSpan;
import com.jidesoft.grid.SpanTableModel;

public class TipoAttributoTableModel extends DefaultBeanTableModel<TipoAttributo> implements SpanTableModel {

	private static final long serialVersionUID = -1645016728140540380L;

	/**
	 * Costruttore.
	 */
	public TipoAttributoTableModel() {
		super(TipiAttributoTablePage.PAGE_ID, new String[] { "codice", "nome", "tipoDato" }, TipoAttributo.class);

	}

	@Override
	public CellSpan getCellSpanAt(int arg0, int arg1) {
		return null;
	}

	@Override
	public Object getValueAt(int row, int column) {
		TipoAttributo tipoAttributo = getObject(row);

		if (tipoAttributo instanceof TipoAttributo) {
			return super.getValueAt(row, column);
		} else {
			if (column == 0) {
				if (tipoAttributo instanceof TipoVariante) {
					return (tipoAttributo).getCodice();
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
