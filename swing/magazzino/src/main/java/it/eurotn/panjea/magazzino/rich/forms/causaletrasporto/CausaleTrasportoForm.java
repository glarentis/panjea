package it.eurotn.panjea.magazzino.rich.forms.causaletrasporto;

import it.eurotn.panjea.magazzino.domain.CausaleTrasporto;
import it.eurotn.panjea.plugin.PluginManager;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.form.builder.TableFormBuilder;

public class CausaleTrasportoForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "causaleTrasportoForm";
	private PluginManager pluginManager = null;

	/**
	 * Costruttore.
	 * 
	 * @param causaleTrasporto
	 *            {@link CausaleTrasporto}
	 */
	public CausaleTrasportoForm(final CausaleTrasporto causaleTrasporto) {
		super(PanjeaFormModelHelper.createFormModel(causaleTrasporto, false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");
		builder.row();

		((JTextField) builder.add("descrizione", "align=left")[1]).setColumns(25);
		builder.row();

		pluginManager = (PluginManager) Application.instance().getApplicationContext().getBean("pluginManager");
		if (pluginManager.isPresente(PluginManager.PLUGIN_INTRA)) {
			builder.add("naturaTransazione", "align=left");
			builder.row();
		}

		return builder.getForm();
	}

}
