/**
 * 
 */
package it.eurotn.panjea.lotti.rich.forms;

import it.eurotn.panjea.lotti.domain.Lotto;
import it.eurotn.panjea.lotti.rich.bd.ILottiBD;
import it.eurotn.rich.control.table.DefaultBeanEditableTableModel;

/**
 * @author fattazzo
 * 
 */
public class LottoTableModel extends DefaultBeanEditableTableModel<Lotto> {

	private ILottiBD lottiBD;

	private static final long serialVersionUID = -6950659266093394303L;

	/**
	 * Costruttore.
	 * 
	 * @param lottiBD
	 *            lottiBD
	 */
	public LottoTableModel(final ILottiBD lottiBD) {
		super("lottoTableModel", new String[] { "codice", "dataScadenza", "priorita" }, Lotto.class);
		this.lottiBD = lottiBD;
	}

	@Override
	protected boolean isAllowInsert() {
		return false;
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return column == 2;
	}

	@Override
	public void setValueAt(Object value, int row, int column) {
		Integer prioritaOld = getElementAt(row).getPriorita();
		super.setValueAt(value, row, column);
		Lotto lotto = getElementAt(row);

		if (column == 2 && !lotto.getPriorita().equals(prioritaOld)) {
			lotto = lottiBD.salvaLotto(lotto);
			setObject(lotto, row);
		}
	}

}
