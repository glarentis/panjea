package it.eurotn.panjea.magazzino.rich.editors.articolo;

import it.eurotn.panjea.magazzino.util.StatisticaArticolo;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.HierarchicalTableModel;

public class StatisticheArticoloTableModel extends DefaultBeanTableModel<StatisticaArticolo> implements
		HierarchicalTableModel {

	public static final String MODEL_ID = "statisticheArticoloTableModel";

	private static final long serialVersionUID = 2755540451740444700L;

	private static final ConverterContext TOTALE_CONTEXT = new NumberWithDecimalConverterContext();

	private int numeroDecimaliQta = 2;

	/**
	 * Costruttore.
	 *
	 * @param numeroDecimaliQta
	 *            numero decimali quantità dell'articolo
	 */
	public StatisticheArticoloTableModel(final int numeroDecimaliQta) {
		super(MODEL_ID, new String[] { "depositoLite", "giacenza" }, StatisticaArticolo.class);
		this.numeroDecimaliQta = numeroDecimaliQta;
	}

	@Override
	public Object getChildValueAt(int row) {

		StatisticaArticolo statisticaArticolo = getElementAt(row);

		return statisticaArticolo;
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int j) {

		switch (j) {
		case 1:
			TOTALE_CONTEXT.setUserObject(numeroDecimaliQta);
			return TOTALE_CONTEXT;
		default:
			return null;
		}
	}

	@Override
	public boolean hasChild(int arg0) {
		return true;
	}

	@Override
	public boolean isExpandable(int arg0) {
		return true;
	}

	@Override
	public boolean isHierarchical(int arg0) {
		return true;
	}

}
