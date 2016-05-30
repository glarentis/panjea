/**
 *
 */
package it.eurotn.panjea.bi.rich.editors.chart.config;

import it.eurotn.panjea.bi.domain.AnalisiChartParametri;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;

import org.springframework.richclient.form.builder.TableFormBuilder;

/**
 * Crea il form delle impostazioni del grafico.
 *
 * @author fattazzo
 *
 */
public class ChartForm extends PanjeaAbstractForm {

	public static final String FORM_ID = "chartForm";

	/**
	 * Costruttore di default.
	 *
	 * @param paramAnalisiChartParametri
	 *            parametri di analisi
	 */
	public ChartForm(final AnalisiChartParametri paramAnalisiChartParametri) {
		super(PanjeaFormModelHelper.createFormModel(paramAnalisiChartParametri, false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");

		builder.row();
		builder.add("chartType", "align=left");
		builder.row();
		builder.add("chart3D", "align=left");
		builder.row();
		builder.add("showTitle", "align=left");
		builder.row();
		builder.add("chartTitle", "align=left");
		builder.row();

		return builder.getForm();
	}

}
