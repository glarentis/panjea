package it.eurotn.panjea.magazzino.rich.forms.rigamagazzino.componenti;

import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.EditorContext;
import com.jidesoft.grid.HierarchicalTableModel;

public class RigheArticoliComponentiDistintaTableModel extends DefaultBeanTableModel<RigaArticolo> implements
HierarchicalTableModel {

	private static final long serialVersionUID = -4736078919439842853L;

	private static ConverterContext numberQtaContext = new NumberWithDecimalConverterContext(0);
	private static EditorContext numberQtaEditorContext = new EditorContext("DecimalNumberEditorContext", 0);

	/**
	 * Costruttore.
	 */
	public RigheArticoliComponentiDistintaTableModel() {
		super(RigheArticoliComponentiDistintaTablePage.PAGE_ID, new String[] { "articolo", "qta" }, RigaArticolo.class);
	}

	@Override
	public Object getChildValueAt(int arg0) {
		return getElementAt(arg0).getComponenti();
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int column) {
		switch (column) {
		case 1:
			RigaArticolo rigaArticolo = getElementAt(row);
			if (rigaArticolo != null && rigaArticolo.getArticolo() != null) {
				numberQtaContext.setUserObject(rigaArticolo.getArticolo().getNumeroDecimaliQta());
			}
			return numberQtaContext;
		default:
			return null;
		}
	}

	@Override
	public EditorContext getEditorContextAt(int row, int column) {
		switch (column) {
		case 0:
			return RigheArticoliFigliArticoloCellRenderer.RIGHE_ARTICOLI_FIGLI_ARTICOLO_CONTEXT;
		case 1:
			RigaArticolo rigaArticolo = getElementAt(row);
			if (rigaArticolo != null && rigaArticolo.getArticolo() != null) {
				numberQtaEditorContext.setUserObject(rigaArticolo.getArticolo().getNumeroDecimaliQta());
			}
			return numberQtaEditorContext;
		default:
			return null;
		}
	}

	@Override
	public boolean hasChild(int arg0) {
		return !getElementAt(arg0).getComponenti().isEmpty();
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		if (column == 1) {
			return true;
		}
		return super.isCellEditable(row, column);
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
