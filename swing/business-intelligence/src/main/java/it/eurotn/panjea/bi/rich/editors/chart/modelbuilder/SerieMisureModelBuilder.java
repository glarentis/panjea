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
import org.apache.log4j.Logger;

import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.pivot.HeaderTableModel;
import com.jidesoft.pivot.IPivotDataModel;
import com.jidesoft.pivot.PivotField;
import com.steema.teechart.TChart;
import com.steema.teechart.styles.Bar;
import com.steema.teechart.styles.Series;
import com.steema.teechart.styles.SeriesCollection;
import com.steema.teechart.styles.VerticalAxis;

public class SerieMisureModelBuilder extends ModelBuilder {

	private static Logger logger = Logger.getLogger(SerieMisureModelBuilder.class);

	private void createSeries(TChart chartPanel, IPivotDataModel pivotDataModel) {
		Set<String> fieldByTitlePresenti = new HashSet<String>();
		for (PivotField dataField : pivotDataModel.getDataFields()) {
			Series serie = chartPanel.getSeries().withTitle(dataField.getTitle());
			fieldByTitlePresenti.add(dataField.getTitle());

			if (serie == null) {
				serie = new Bar(chartPanel.getChart());
				serie.setTitle(dataField.getTitle());
				serie.getMarks().setVisible(false);
			} else {
				serie.clear();
			}

			chartPanel.getAxes().setVisible(true);

			Colonna colonnaBi = AnalisiBIDomain.getColonna(dataField.getName());
			if (colonnaBi.getColumnClass() == double.class) {
				serie.setVerticalAxis(VerticalAxis.RIGHT);
			} else if (colonnaBi.getColumnClass() == BigDecimal.class) {
				serie.setVerticalAxis(VerticalAxis.LEFT);
			}
		}

		// Cancello le serie che non esistono più
		SeriesCollection series = chartPanel.getSeries();
		@SuppressWarnings("unchecked")
		Iterator<Object> serieIterator = series.iterator();
		while (serieIterator.hasNext()) {
			Series serie = (Series) serieIterator.next();
			// Se ho una sola misura coloro ogni dato
			serie.setColorEach(pivotDataModel.getDataFields().length == 1);
			if (!fieldByTitlePresenti.contains(serie.getTitle())) {
				serieIterator.remove();
			}
		}

	}

	@Override
	protected Map<String, int[]> fillModel(TChart chartPanel, IPivotDataModel pivotDataModel) {
		createSeries(chartPanel, pivotDataModel);
		TableModel dataModel = pivotDataModel.getDataTableModel();
		Map<String, int[]> rowColumnByValueIndex = new HashMap<String, int[]>();

		SeriesCollection series = chartPanel.getSeries();
		for (Object object : series) {
			Series serie = (Series) object;
			serie.beginUpdate();
		}
		try {
			int counter = 0;
			for (int col = 0; col < pivotDataModel.getDataTableModel().getColumnCount(); col++) {
				for (int row = 0; row < pivotDataModel.getDataTableModel().getRowCount(); row++) {
					PivotField field = getField(pivotDataModel, row, col);
					Series serie = chartPanel.getSeries().withTitle(field.getTitle());
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
					counter++;
				}
			}
		} catch (Exception e) {
			System.err.println(e);
			logger.error("-->errore nel fillare il grafico", e);
		} finally {
			for (Object object : series) {
				Series serie = (Series) object;
				serie.endUpdate();
			}
		}
		return rowColumnByValueIndex;
	}

	private String getLabelAsseX(IPivotDataModel pivotDataModel, int row, int col) {
		HeaderTableModel columnHeaderTableModel = pivotDataModel.getColumnHeaderTableModel();
		HeaderTableModel rowHeaderTableModel = pivotDataModel.getRowHeaderTableModel();
		StringBuilder nomeCategoriaBuilder = new StringBuilder(100);
		for (int rh = 0; rh < rowHeaderTableModel.getColumnCount(); rh++) {
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

}
