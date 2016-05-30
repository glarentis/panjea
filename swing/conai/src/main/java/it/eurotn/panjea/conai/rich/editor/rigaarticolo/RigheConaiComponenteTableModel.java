/**
 * 
 */
package it.eurotn.panjea.conai.rich.editor.rigaarticolo;

import it.eurotn.panjea.conai.domain.RigaConaiComponente;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;
import it.eurotn.util.PanjeaEJBUtil;

import org.springframework.binding.value.ValueModel;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.EditorContext;
import com.jidesoft.grid.NavigableTableModel;

/**
 * @author leonardo
 * 
 */
public class RigheConaiComponenteTableModel extends DefaultBeanTableModel<RigaConaiComponente> implements
		NavigableTableModel {

	private static final long serialVersionUID = -922628444299641813L;

	private static ConverterContext numberQtaContext = new NumberWithDecimalConverterContext(6);
	private static EditorContext numberQtaEditorContext = new EditorContext("DecimalNumberEditorContext", 6);

	private ValueModel valueModel = null;
	private boolean readOnly = true;

	/**
	 * Costruttore.
	 * 
	 * @param valueModel
	 *            il value model della collection associata
	 */
	public RigheConaiComponenteTableModel(final ValueModel valueModel) {
		super("righeConaiComponenteForm", new String[] { "materiale", "tipoImballo", "pesoUnitario", "pesoTotale",
				"percentualeEsenzione" }, RigaConaiComponente.class);
		this.valueModel = valueModel;
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int column) {
		switch (column) {
		case 2:
		case 3:
		case 4:
			return numberQtaContext;
		default:
			return null;
		}
	}

	@Override
	public EditorContext getEditorContextAt(int row, int column) {
		switch (column) {
		case 2:
		case 3:
		case 4:
			return numberQtaEditorContext;
		default:
			return null;
		}
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return !readOnly && column == 2;
	}

	@Override
	public boolean isNavigableAt(int row, int col) {
		return col == 2;
	}

	@Override
	public boolean isNavigationOn() {
		return true;
	}

	/**
	 * @param readOnly
	 *            the readOnly to set
	 */
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	@Override
	public void setValueAt(Object editedValue, int row, int column) {
		super.setValueAt(editedValue, row, column);
		valueModel.setValue(PanjeaEJBUtil.cloneObject(valueModel.getValue()));
	}

}
