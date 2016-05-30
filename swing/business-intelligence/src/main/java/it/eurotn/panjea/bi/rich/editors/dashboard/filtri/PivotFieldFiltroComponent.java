/**
 *
 */
package it.eurotn.panjea.bi.rich.editors.dashboard.filtri;

import it.eurotn.panjea.bi.rich.editors.analisi.AnalisiBiPivotTablePane;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jdesktop.swingx.HorizontalLayout;

import com.jidesoft.pivot.FieldBox;
import com.jidesoft.pivot.PivotField;

/**
 * @author fattazzo
 *
 */
public class PivotFieldFiltroComponent extends JPanel {

	private static final long serialVersionUID = 2084250253608853059L;

	private JLabel valuesLabel = new JLabel();

	private PivotField pivotField;

	private FieldBox fieldBox;

	/**
	 * Costruttore.
	 *
	 * @param pivotField
	 *            pivotField
	 * @param pivotTablePane
	 *            pivotTablePane
	 */
	public PivotFieldFiltroComponent(final PivotField pivotField, final AnalisiBiPivotTablePane pivotTablePane) {
		super();
		this.pivotField = pivotField;

		setLayout(new HorizontalLayout(2));

		initControls(pivotTablePane);
	}

	private void initControls(AnalisiBiPivotTablePane pivotTablePane) {

		fieldBox = new FieldBox(pivotField);
		fieldBox.setFilterButtonVisible(true);
		fieldBox.setPivotTablePane(pivotTablePane);
		fieldBox.setFocusable(false);
		pivotField.addPropertyChangeListener(PivotField.PROPERTY_SELECTED_POSSIBLE_VALUES,
				new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				refreshValuesLabel();
			}
		});
		add(fieldBox);

		valuesLabel = new JLabel();
		refreshValuesLabel();
		add(valuesLabel);
	}

	private void refreshValuesLabel() {
		StringBuilder sbFiltroField = new StringBuilder("<html>");
		if (pivotField.getSelectedPossibleValues() != null) {
			sbFiltroField.append("<I>");
			for (Object selectValue : pivotField.getSelectedPossibleValues()) {
				sbFiltroField.append(selectValue != null ? selectValue.toString() : "");
				if (selectValue != pivotField.getSelectedPossibleValues()[pivotField.getSelectedPossibleValues().length - 1]) {
					sbFiltroField.append(" - ");
				}
			}
			sbFiltroField.append("</I>&nbsp;&nbsp;&nbsp;&nbsp;");
		}
		sbFiltroField.append("</html>");
		valuesLabel.setText(sbFiltroField.toString());
		valuesLabel.setToolTipText(sbFiltroField.toString());
	}

	/**
	 * @param readOnly
	 *            readOnly
	 */
	public void setReadOnly(boolean readOnly) {
		fieldBox.setFilterButtonVisible(!readOnly);
	}
}
