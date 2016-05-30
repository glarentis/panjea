/**
 *
 */
package it.eurotn.panjea.bi.rich.editors.chart.config;

import it.eurotn.panjea.bi.domain.AnalisiChartParametri;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.ColorComboBoxBinder;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.util.HashMap;

import javax.swing.JComponent;

import org.springframework.richclient.form.builder.TableFormBuilder;

/**
 * @author fattazzo
 *
 */
public class ColorSeriesForm extends PanjeaAbstractForm {

	public static final String FORM_ID = "colorSeriesForm";

	/**
	 * Costruttore di default.
	 *
	 * @param paramAnalisiChartParametri
	 *            parametri di analisi
	 */
	public ColorSeriesForm(final AnalisiChartParametri paramAnalisiChartParametri) {
		super(PanjeaFormModelHelper.createFormModel(paramAnalisiChartParametri, false, FORM_ID), FORM_ID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.richclient.form.AbstractForm#createFormControl()
	 */
	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");

		HashMap<String, Object> context = new HashMap<String, Object>();

		builder.row();
		builder.add(new ColorComboBoxBinder().bind(getFormModel(), "colorSerie1", context), "align=left");
		builder.row();
		builder.add(new ColorComboBoxBinder().bind(getFormModel(), "colorSerie2", context), "align=left");
		builder.row();
		builder.add(new ColorComboBoxBinder().bind(getFormModel(), "colorSerie3", context), "align=left");
		builder.row();
		builder.add(new ColorComboBoxBinder().bind(getFormModel(), "colorSerie4", context), "align=left");
		builder.row();
		builder.add(new ColorComboBoxBinder().bind(getFormModel(), "colorSerie5", context), "align=left");
		builder.row();
		builder.add(new ColorComboBoxBinder().bind(getFormModel(), "colorSerie6", context), "align=left");
		builder.row();
		builder.add(new ColorComboBoxBinder().bind(getFormModel(), "colorSerie7", context), "align=left");
		builder.row();
		builder.add(new ColorComboBoxBinder().bind(getFormModel(), "colorSerie8", context), "align=left");
		builder.row();
		builder.add(new ColorComboBoxBinder().bind(getFormModel(), "colorSerie9", context), "align=left");
		builder.row();
		builder.add(new ColorComboBoxBinder().bind(getFormModel(), "colorSerie10", context), "align=left");
		builder.row();
		builder.add(new ColorComboBoxBinder().bind(getFormModel(), "colorSerie11", context), "align=left");
		builder.row();
		builder.add(new ColorComboBoxBinder().bind(getFormModel(), "colorSerie12", context), "align=left");
		builder.row();

		return builder.getForm();
	}

}
