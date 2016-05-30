/**
 *
 */
package it.eurotn.panjea.bi.rich.editors.chart.config;

import it.eurotn.panjea.bi.domain.AnalisiChartParametri;
import it.eurotn.rich.form.PanjeaAbstractForm;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe che costruisce i form per la modifica delle impostazioni dei grafici in base al tipo di grafico.
 *
 * @author fattazzo
 *
 */
public class ChartFormsBuilder {

	private final AnalisiChartParametri analisiChartParametri;

	/**
	 * Costruttore di default.
	 *
	 * @param paramAnalisiChartParametri
	 *            parametri di analisi
	 */
	public ChartFormsBuilder(final AnalisiChartParametri paramAnalisiChartParametri) {
		super();
		this.analisiChartParametri = paramAnalisiChartParametri;
	}

	/**
	 * Crea i form delle impostazioni del grafico.
	 *
	 * @return lista dei form creati
	 */
	public List<PanjeaAbstractForm> createForms() {

		List<PanjeaAbstractForm> forms = new ArrayList<PanjeaAbstractForm>();

		// impostazioni del grafico
		forms.add(new ChartForm(this.analisiChartParametri));

		// impostazioni delle serie
		// forms.add(new BarSeriesForm(this.analisiChartParametri));

		// impostazioni degli assi
		// forms.add(new AxisForm(this.analisiChartParametri));

		// importazioni dei colori delle serie
		// forms.add(new ColorSeriesForm(this.analisiChartParametri));

		return forms;
	}

}
