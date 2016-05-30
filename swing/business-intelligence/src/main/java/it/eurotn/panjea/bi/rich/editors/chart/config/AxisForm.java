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
 * Crea il form per le impostazioni degli assi del grafico.
 *
 * @author fattazzo
 *
 */
public class AxisForm extends PanjeaAbstractForm {

	public static final String FORM_ID = "axisForm";

	/**
	 * Costruttore di default.
	 *
	 * @param paramAnalisiChartParametri
	 *            parametri di analisi
	 */
	public AxisForm(final AnalisiChartParametri paramAnalisiChartParametri) {
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

		builder.row();
		builder.add("axisQuantitaTitle", "align=left");
		builder.row();
		builder.add("axisImportoTitle", "align=left");
		builder.row();
		builder.add("axisCountTitle", "align=left");
		builder.row();
		builder.add("axisCategoryTitle", "align=left");
		builder.row();

		return builder.getForm();
	}

}
