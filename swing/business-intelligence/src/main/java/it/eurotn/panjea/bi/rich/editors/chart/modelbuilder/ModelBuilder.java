package it.eurotn.panjea.bi.rich.editors.chart.modelbuilder;

import java.util.Map;

import com.jidesoft.pivot.HeaderTableModel;
import com.jidesoft.pivot.IPivotDataModel;
import com.jidesoft.pivot.PivotField;
import com.steema.teechart.TChart;

public abstract class ModelBuilder {

	/**
	 * Crea il modello del grafico
	 *
	 * @param chartPanel
	 *            chart
	 * @param pivotDataModel
	 *            dataModel con i dati per riempire il grafico
	 * @return indici contenente riga e colonna
	 */
	public Map<String, int[]> createModel(TChart chartPanel, IPivotDataModel pivotDataModel) {
		Map<String, int[]> result = fillModel(chartPanel, pivotDataModel);
		return result;
	}

	protected abstract Map<String, int[]> fillModel(TChart chartPanel, IPivotDataModel pivotDataModel);

	/**
	 *
	 * @param pivotDataModel
	 *            dataModel
	 * @param row
	 *            riga
	 * @param col
	 *            colonna
	 * @return field associato alla riga e colonna.
	 */
	protected PivotField getField(IPivotDataModel pivotDataModel, int row, int col) {
		PivotField result = null;
		if (pivotDataModel.getDataFields().length == 1) {
			return pivotDataModel.getDataFields()[0];
		}
		HeaderTableModel rowHeaderTableModel = pivotDataModel.getRowHeaderTableModel();
		for (int rh = 0; rh < rowHeaderTableModel.getColumnCount(); rh++) {
			PivotField field = rowHeaderTableModel.getFieldAt(row, rh);
			if (field.getAreaType() == PivotField.AREA_DATA) {
				result = field;
			}
		}

		if (result == null) {
			HeaderTableModel colHeaderTableModel = pivotDataModel.getColumnHeaderTableModel();
			for (int ch = 0; ch < colHeaderTableModel.getRowCount(); ch++) {
				PivotField field = colHeaderTableModel.getFieldAt(ch, col);
				if (field.getAreaType() == PivotField.AREA_DATA) {
					result = field;
				}
			}
		}
		return result;
	}
}
