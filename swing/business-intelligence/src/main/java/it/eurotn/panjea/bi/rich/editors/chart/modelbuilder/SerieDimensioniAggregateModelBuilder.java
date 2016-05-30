package it.eurotn.panjea.bi.rich.editors.chart.modelbuilder;

import it.eurotn.panjea.bi.domain.analisi.AnalisiBIDomain;
import it.eurotn.panjea.bi.domain.analisi.tabelle.Colonna;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.table.TableModel;

import org.apache.commons.lang3.ObjectUtils;

import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.pivot.HeaderTableModel;
import com.jidesoft.pivot.IPivotDataModel;
import com.jidesoft.pivot.PivotField;
import com.steema.teechart.TChart;
import com.steema.teechart.styles.Bar;
import com.steema.teechart.styles.Series;
import com.steema.teechart.styles.SeriesCollection;

public class SerieDimensioniAggregateModelBuilder extends ModelBuilder {

	private void createSeries(TChart chartPanel, IPivotDataModel pivotDataModel) {
		if (pivotDataModel.getRowFields().length == 0) {
			throw new IllegalArgumentException("dataModel senza dimensioni nelle righe");
		}

		HeaderTableModel tableModelSerie = pivotDataModel.getRowHeaderTableModel();
		SeriesCollection series = chartPanel.getSeries();
		Set<String> fieldByTitlePresenti = new HashSet<String>();
		// Cancello le serie che non esistono più
		series = chartPanel.getSeries();
		@SuppressWarnings("unchecked")
		Iterator<Object> serieIterator = series.iterator();
		while (serieIterator.hasNext()) {
			Series serie = (Series) serieIterator.next();
			// Se ho una sola misura coloro ogni dato
			serie.setColorEach(false);
			if (!fieldByTitlePresenti.contains(serie.getTitle())) {
				serieIterator.remove();
			}
		}

		for (int indexSerie = 0; indexSerie < tableModelSerie.getRowCount(); indexSerie++) {
			// se ho più misure creo una serie per ogni misura
			for (PivotField fieldMisura : pivotDataModel.getDataFields()) {
				String valueSerie = getLabelSerie(fieldMisura, indexSerie, pivotDataModel);
				Series serie = chartPanel.getSeries().withTitle(valueSerie);
				fieldByTitlePresenti.add(valueSerie);
				if (serie == null) {
					serie = new Bar(chartPanel.getChart());
					serie.setTitle(valueSerie);
					serie.getMarks().setVisible(false);
				} else {
					serie.getYValues().clear();
					serie.getXValues().clear();
				}
			}
		}

		chartPanel.getAxes().setVisible(true);
	}

	@Override
	protected Map<String, int[]> fillModel(TChart chartPanel, IPivotDataModel pivotDataModel) {
		createSeries(chartPanel, pivotDataModel);
		TableModel dataModel = pivotDataModel.getDataTableModel();
		Map<String, int[]> rowColumnByValueIndex = new HashMap<String, int[]>();

		int counter = 0;
		for (int col = 0; col < pivotDataModel.getDataTableModel().getColumnCount(); col++) {
			for (int row = 0; row < pivotDataModel.getDataTableModel().getRowCount(); row++) {
				PivotField field = getField(pivotDataModel, row, col);
				Series serie = chartPanel.getSeries().withTitle(getLabelSerie(field, row, pivotDataModel));

				Colonna colonnaBi = AnalisiBIDomain.getColonna(field.getName());
				if (colonnaBi.getColumnClass() == BigDecimal.class) {
					BigDecimal value = (BigDecimal) ObjectUtils.defaultIfNull(dataModel.getValueAt(row, col),
							BigDecimal.ZERO);
					serie.add(value.doubleValue(), getLabelAsseX(pivotDataModel, row, col));
				} else {
					double value = (double) ObjectUtils.defaultIfNull(dataModel.getValueAt(row, col), 0.0);
					serie.add(value, getLabelAsseX(pivotDataModel, row, col));
				}
				rowColumnByValueIndex.put(serie.getTitle() + counter, new int[] { row, col });
			}
			counter++;
		}
		return rowColumnByValueIndex;
	}

	private String getLabelAsseX(IPivotDataModel pivotDataModel, int row, int col) {
		HeaderTableModel columnHeaderTableModel = pivotDataModel.getColumnHeaderTableModel();
		HeaderTableModel rowHeaderTableModel = pivotDataModel.getRowHeaderTableModel();
		StringBuilder nomeCategoriaBuilder = new StringBuilder(100);
		for (int rh = 1; rh < rowHeaderTableModel.getColumnCount(); rh++) {
			PivotField field = rowHeaderTableModel.getFieldAt(row, rh);
			if (field.getAreaType() != PivotField.AREA_DATA) {
				Object captionCategory = ObjectConverterManager.toString(rowHeaderTableModel.getValueAt(row, rh),
						field.getType(), field.getConverterContext());
				nomeCategoriaBuilder.append(captionCategory);
			}
			nomeCategoriaBuilder.append(" ");
		}
		for (int ch = 0; ch < columnHeaderTableModel.getRowCount(); ch++) {
			PivotField field = columnHeaderTableModel.getFieldAt(ch, col);
			if (field.getAreaType() != PivotField.AREA_DATA) {
				Object captionCategory = ObjectConverterManager.toString(columnHeaderTableModel.getValueAt(ch, col),
						field.getType(), field.getConverterContext());
				nomeCategoriaBuilder.append(captionCategory);
				nomeCategoriaBuilder.append(" ");
			}
		}
		return nomeCategoriaBuilder.toString();
	}

	private String getLabelSerie(PivotField fieldMisura, int indexSerie, IPivotDataModel pivotDataModel) {
		StringBuilder sbValueSerie = new StringBuilder(50);

		Object valore = ObjectUtils.defaultIfNull(pivotDataModel.getRowHeaderTableModel().getValueAt(indexSerie, 0),
				" ");

		sbValueSerie.append(ObjectConverterManager.toString(valore.toString(), fieldMisura.getType(),
				fieldMisura.getConverterContext()));
		if (pivotDataModel.getDataFields().length > 1) {
			sbValueSerie.append("-").append(fieldMisura.getTitle());
		}
		return sbValueSerie.toString();
	}
}
