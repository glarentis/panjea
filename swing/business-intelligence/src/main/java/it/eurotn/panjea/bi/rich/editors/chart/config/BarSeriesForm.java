/**
 *
 */
package it.eurotn.panjea.bi.rich.editors.chart.config;

import it.eurotn.panjea.bi.domain.AnalisiChartParametri;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.builder.TableFormBuilder;

/**
 * Crea il form per le impostazioni delle serie del grafico a barre.
 *
 * @author fattazzo
 *
 */
public class BarSeriesForm extends PanjeaAbstractForm {

	public static final String FORM_ID = "barSeriesForm";

	private List<String> listRenderer;

	/**
	 * Costruttore di default.
	 *
	 * @param paramAnalisiChartParametri
	 *            parametri di analisi
	 */
	public BarSeriesForm(final AnalisiChartParametri paramAnalisiChartParametri) {
		super(PanjeaFormModelHelper.createFormModel(paramAnalisiChartParametri, false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");

		builder.row();
		builder.add(bf.createBoundComboBox("rendererQuantita", getRendererListValueHolder(), true));
		// builder.add("rendererQuantita", "align=left");
		builder.row();
		builder.add(bf.createBoundComboBox("rendererImporto", getRendererListValueHolder(), true));
		// builder.add("rendererImporto", "align=left");
		builder.row();
		builder.add(bf.createBoundComboBox("rendererCount", getRendererListValueHolder(), true));
		// builder.add("rendererCount", "align=left");
		builder.row();

		return builder.getForm();
	}

	private ValueHolder getRendererListValueHolder() {

		if (listRenderer == null) {
			listRenderer = new ArrayList<String>();
			listRenderer.add("org.jfree.chart.renderer.category.BarRenderer");
			listRenderer.add("org.jfree.chart.renderer.category.BarRenderer3D");
			listRenderer.add("org.jfree.chart.renderer.category.LineAndShapeRenderer");
			listRenderer.add("org.jfree.chart.renderer.category.LineRenderer3D");
		}

		return new ValueHolder(listRenderer);
	}

}
