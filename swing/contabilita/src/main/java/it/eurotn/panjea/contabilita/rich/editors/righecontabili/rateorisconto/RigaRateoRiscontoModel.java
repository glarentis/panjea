package it.eurotn.panjea.contabilita.rich.editors.righecontabili.rateorisconto;

import it.eurotn.panjea.contabilita.domain.RigaContabile;
import it.eurotn.panjea.contabilita.domain.rateirisconti.RigaRateoRisconto;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanEditableTableModel;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.HierarchicalTableModel;

public class RigaRateoRiscontoModel extends DefaultBeanEditableTableModel<RigaRateoRisconto> implements
HierarchicalTableModel {
	private static final long serialVersionUID = -8893872917377852124L;
	private static NumberWithDecimalConverterContext numberPrezzoContext;

	static {
		numberPrezzoContext = new NumberWithDecimalConverterContext();
		numberPrezzoContext.setUserObject(new Integer(2));
	}

	private RigaContabile rigaContabile;

	/**
	 * Costruttore.
	 */
	public RigaRateoRiscontoModel() {
		super("rigaRateoRiscontoModel", new String[] { "importo", "inizio", "fine", "giorniTotali", "nota" },
				RigaRateoRisconto.class);
	}

	@Override
	protected RigaRateoRisconto createNewObject() {
		RigaRateoRisconto riga = super.createNewObject();
		riga.setRigaContabile(rigaContabile);
		return riga;
	}

	@Override
	public Object getChildValueAt(int row) {
		RigaRateoRisconto rigaRateoRisconto = getElementAt(row);
		return rigaRateoRisconto.getRateiRiscontiAnno();
	}

	@Override
	public ConverterContext getConverterContextAt(int i, int j) {
		switch (j) {
		case 0:
			return numberPrezzoContext;
		default:
			return super.getConverterContextAt(i, j);
		}
	}

	/**
	 * @return Returns the rigaContabile.
	 */
	public RigaContabile getRigaContabile() {
		return rigaContabile;
	}

	@Override
	public boolean hasChild(int paramInt) {
		RigaRateoRisconto el = getElementAt(paramInt);
		return el.getRateiRiscontiAnno().size() > 0;
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return column != 3;
	}

	@Override
	public boolean isExpandable(int paramInt) {
		return true;
	}

	@Override
	public boolean isHierarchical(int paramInt) {
		return true;
	}

	/**
	 * @param rigaContabile
	 *            The rigaContabile to set.
	 */
	public void setRigaContabile(RigaContabile rigaContabile) {
		this.rigaContabile = rigaContabile;
	}
}
