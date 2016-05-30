/**
 *
 */
package it.eurotn.panjea.bi.rich.editors.chart.modelbuilder;

import it.eurotn.panjea.bi.domain.analisi.AnalisiBIDomain;
import it.eurotn.panjea.bi.domain.analisi.tabelle.Colonna;
import it.eurotn.panjea.bi.rich.MappeBiManager;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.ObjectUtils;

import com.jidesoft.pivot.IPivotDataModel;
import com.jidesoft.pivot.PivotField;
import com.steema.teechart.TChart;
import com.steema.teechart.legend.LegendStyle;
import com.steema.teechart.styles.SHPMap;

/**
 * @author fattazzo
 *
 */
public class SerieMapModelBuilder extends ModelBuilder {

	private Set<String> mapsFile;

	private MappeBiManager mappeBiManager;

	/**
	 * Costruttore.
	 *
	 * @param mapFile
	 *            file della mappa
	 */
	public SerieMapModelBuilder(final Set<String> mapFile) {
		super();
		this.mapsFile = mapFile;
		this.mappeBiManager = new MappeBiManager();
	}

	private SHPMap createSerie(TChart chartPanel, IPivotDataModel pivotDataModel) {
		SHPMap serie = new SHPMap(chartPanel.getChart());
		serie.setTitle("Mappa " + pivotDataModel.getDataFields()[0].getTitle());
		serie.setUseColorRange(true);
		serie.setUsePalette(true);
		serie.getShadow().setVisible(false);
		serie.getMarks().setVisible(false);

		return serie;
	}

	@Override
	protected Map<String, int[]> fillModel(TChart chartPanel, IPivotDataModel pivotDataModel) {

		initChart(chartPanel);

		Map<String, int[]> rowColumnByValueIndex = new HashMap<String, int[]>();

		for (String mappa : mapsFile) {
			SHPMap serie = createSerie(chartPanel, pivotDataModel);
			try {
				serie.beginUpdate();
				serie.setShpMapFile(mappeBiManager.getPathMappa(mappa));

				// il grafico è valido solo se non ho colonne ed esiste una sola riga che gestisce il campo associato
				// alla
				// serie
				if (pivotDataModel.getColumnFields().length == 0 && pivotDataModel.getRowFields().length == 1
						&& serie.getManagedField().contains(pivotDataModel.getRowFields()[0].getName())) {
					for (int row = 0; row < pivotDataModel.getDataTableModel().getRowCount(); row++) {
						String text = (String) ObjectUtils.defaultIfNull(pivotDataModel.getRowHeaderTableModel()
								.getValueAt(row, 0), "");
						PivotField field = getField(pivotDataModel, row, 0);
						Colonna colonnaBi = AnalisiBIDomain.getColonna(field.getName());
						if (colonnaBi.getColumnClass() == BigDecimal.class) {
							BigDecimal value = (BigDecimal) ObjectUtils.defaultIfNull(pivotDataModel
									.getDataTableModel().getValueAt(row, 0), BigDecimal.ZERO);
							serie.addValue(value.doubleValue(), text);
						} else {
							double value = (double) ObjectUtils.defaultIfNull(pivotDataModel.getDataTableModel()
									.getValueAt(row, 0), 0.0);
							serie.addValue(value, text);
						}
						rowColumnByValueIndex.put(serie.getTitle() + text.toLowerCase(), new int[] { row, 0 });
					}
				}

				serie.loadMapFromResource();
			} catch (Exception e) {
				throw new RuntimeException(e);
			} finally {
				serie.endUpdate();
			}
		}
		return rowColumnByValueIndex;
	}

	private void initChart(TChart chartPanel) {
		chartPanel.removeAllSeries();
		chartPanel.getAxes().setVisible(false);
		chartPanel.getAspect().setView3D(false);
		chartPanel.getLegend().setLegendStyle(LegendStyle.PALETTE);
	}
}
