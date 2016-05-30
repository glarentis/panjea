package it.eurotn.panjea.contabilita.rich.editors.righecontabili.rateorisconto;

import it.eurotn.panjea.contabilita.domain.rateirisconti.RigaRiscontoAnno;
import it.eurotn.rich.control.table.JideTableWidget;

import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

import com.jidesoft.grid.CellStyle;
import com.jidesoft.grid.CellStyleTableHeader;

public class RigheRateoRiscontoAnnoTable extends JideTableWidget<RigaRiscontoAnno> {

	private class CellStyleRatei extends CellStyleTableHeader {

		private static final long serialVersionUID = -4659449947148880447L;

		public CellStyleRatei(final JTable table) {
			super(table);
		}

		@Override
		public CellStyle getCellStyleAt(int paramInt1, int paramInt2) {
			CellStyle cs = new CellStyle();
			cs.setBorder(new EmptyBorder(0, 0, 0, 0));
			return cs;
		}
	}

	/**
	 * Costruttore.
	 */
	public RigheRateoRiscontoAnnoTable() {
		super("rigaRateoRiscontoCalcolato", new RigaRateoRiscontoAnnoModel());

		getTable().setColumnSelectionAllowed(false);
		getTable().setRowSelectionAllowed(false);
		getTable().setCellSelectionEnabled(false);

		setNumberRowVisible(false);

		CellStyleTableHeader header = new CellStyleRatei(getTable());
		getTable().setTableHeader(header);
	}
}
