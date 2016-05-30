package it.eurotn.panjea.ordini.rich.forms.righeordine.componenti;

import it.eurotn.panjea.ordini.domain.RigaArticolo;
import it.eurotn.panjea.ordini.rich.bd.IOrdiniDocumentoBD;
import it.eurotn.panjea.ordini.rich.bd.OrdiniDocumentoBD;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanEditableTableModel;

import java.util.Date;

import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.EditorContext;
import com.jidesoft.grid.HierarchicalTableModel;

public class RigheArticoliComponentiDistintaTableModel extends DefaultBeanEditableTableModel<RigaArticolo> implements
HierarchicalTableModel {

	private static final long serialVersionUID = -4736078919439842853L;

	private static ConverterContext numberQtaContext = new NumberWithDecimalConverterContext(0);
	private static EditorContext numberQtaEditorContext = new EditorContext("DecimalNumberEditorContext", 0);

	private IOrdiniDocumentoBD ordiniDocumentoBD;

	/**
	 * Costruttore.
	 */
	public RigheArticoliComponentiDistintaTableModel() {
		super(RigheArticoliComponentiDistintaTablePage.PAGE_ID, new String[] { "articolo", "qta", "dataConsegna",
				"dataProduzione" }, RigaArticolo.class);
		this.ordiniDocumentoBD = RcpSupport.getBean(OrdiniDocumentoBD.BEAN_ID);
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
		return getElementAt(arg0).getComponenti() != null && !getElementAt(arg0).getComponenti().isEmpty();
	}

	@Override
	protected boolean isAllowInsert() {
		return false;
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return column >= 2;
	}

	@Override
	public boolean isExpandable(int arg0) {
		return true;
	}

	@Override
	public boolean isHierarchical(int arg0) {
		return true;
	}

	@Override
	public void setValueAt(Object editedValue, int row, int column) {
		RigaArticolo rigaArticolo = getElementAt(row);
		Date newDate = null;
		boolean dateChanged = false;
		switch (column) {
		case 2:
			newDate = (Date) editedValue;
			dateChanged = rigaArticolo.getDataConsegna() == null
					|| newDate.compareTo(rigaArticolo.getDataConsegna()) != 0;
			break;
		case 3:
			newDate = (Date) editedValue;
			dateChanged = rigaArticolo.getDataProduzione() == null
					|| newDate.compareTo(rigaArticolo.getDataProduzione()) != 0;
			break;
		default:
			break;
		}

		super.setValueAt(editedValue, row, column);

		if (dateChanged && !rigaArticolo.isNew()) {
			// il salvataggio del componente non cambia stato del documento e/o invalida le righe per cui posso chiamare
			// la salva senza fare controlli
			ordiniDocumentoBD.salvaRigaOrdine(rigaArticolo, false);
		}
	}

}
