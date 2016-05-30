package it.eurotn.panjea.magazzino.rich.editors.articolo;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.steema.teechart.Chart;
import com.steema.teechart.TChart;
import com.steema.teechart.drawing.Color;
import com.steema.teechart.styles.Pie;
import com.steema.teechart.themes.Theme;

public class StatisticaChartDeposito {

	private static Map<String, Color> coloriValue;

	{
		coloriValue = new HashMap<String, Color>();
		coloriValue.put(StatisticaDepositoComponent.ACQUISTATO, Color.yellow);
		coloriValue.put(StatisticaDepositoComponent.VENDUTO, Color.RED);
		coloriValue.put(StatisticaDepositoComponent.RESO_ACQ, Color.GREEN);
		coloriValue.put(StatisticaDepositoComponent.RESO_VEN, Color.BLUE);
		coloriValue.put(StatisticaDepositoComponent.CARICO_ALTRO, Color.ORANGE);
		coloriValue.put(StatisticaDepositoComponent.SCARICO_ALTRO, Color.BLACK);
		coloriValue.put(StatisticaDepositoComponent.CARICO_TRASF, Color.PINK);
		coloriValue.put(StatisticaDepositoComponent.SCARICO_TRASF, Color.MAGENTA);

	}

	/**
	 * Costruttore.
	 *
	 */
	public StatisticaChartDeposito() {
		super();
	}

	/**
	 * @param valori
	 *            chiave nome categoria,valore valore della categoria.
	 * @return chart generato
	 */
	public TChart getChart(Map<String, Double> valori) {

		TChart chartPanel = new TChart();
		Chart chart = chartPanel.getChart();
		Theme theme = new com.steema.teechart.themes.WebTheme(chart);
		theme.apply();
		chart.getAspect().setChart3DPercent(30);
		chart.getAspect().setSmoothingMode(true);
		chart.getAspect().setView3D(false);
		chart.getAspect().setOrthogonal(true);
		chart.getTitle().setVisible(false);
		chart.getPanel().setVisible(false);
		chart.getPanel().setTransparent(true);
		chart.getLegend().setVisible(false);
		chart.getPanel().getBevel().setVisible(false);
		chart.getPanel().getBorderPen().setVisible(false);
		chart.getPanel().setMarginBottom(0);
		chart.getPanel().setMarginLeft(0);
		chart.getPanel().setMarginTop(0);
		chart.getPanel().setMarginRight(0);

		Pie series = new com.steema.teechart.styles.Donut(chart);
		series.setCircled(true);
		series.getMarks().setVisible(true);
		series.setValueFormat("#,###.######");
		series.setColorEach(true);

		series.beginUpdate();
		try {
			for (Entry<String, Double> point : valori.entrySet()) {
				series.add(point.getValue().doubleValue(), point.getKey(), coloriValue.get(point.getKey()));
			}
		} finally {
			series.endUpdate();
		}

		return chartPanel;
	}
}
